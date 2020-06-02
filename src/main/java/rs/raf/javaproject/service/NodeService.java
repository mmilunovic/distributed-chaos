package rs.raf.javaproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.raf.javaproject.model.*;
import rs.raf.javaproject.repository.Database;
import rs.raf.javaproject.schedule.JobExecution;

import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class NodeService {

    @Autowired
    private Database repository;

    private JobExecution jobExecution;

    @Autowired
    private MessageService messageService;


    public Node info(Node node){
        return repository.getInfo();
    }

    public Collection<Node> allNodes(){
        return repository.getAllNodes().values();
    }

    public boolean ping(String nodeID){
        if(repository.getInfo().getAddress().equals(nodeID)){
            return true;
        }else{
            // TODO: Posaljem kome treba?
            System.out.println("Nisam ja, saljem dalje");
        }
        return true;
    }

    public void quit(){
    }

    public void left(String nodeID){
        // TODO: Posalji sledbeniku poruku koristeci /api/node/left{nodeID}
        // TODO: Obavesti bootstrap da je cvor nesta sa /api/bootstrap/left

        repository.getAllNodes().remove(nodeID);

        restructure();
    }

    public void newNode(String nodeID){

        Node newNode = new Node(nodeID);

        System.out.println(repository.getInfo() + " prima poruku o cvoru " + newNode);
        repository.getAllNodes().put(newNode.getId(), newNode);

        // Ako ja dobijem poruku da sam se ja ukljucio u mrezu, to znaci da su svi obavesteni
        if(repository.getInfo().equals(newNode)){
            return;
        }

        messageService.sendNewNode(repository.getPredecessor(), newNode);

        restructure();
    }

    public void putBackup(BackupInfo backupInfo) {
        repository.getBackups().put(backupInfo.getId(), backupInfo);
    }

    public void restructure() {
        if (this.jobExecution == null) {
            this.jobExecution = new JobExecution(this.repository, repository.getRegion(), new AtomicBoolean(false));
        }
        this.jobExecution.getPause().set(true);

        List<Point> data = this.repository.getData();
        if (repository.getInfo().getMyRegion() != null) {
            BackupInfo backupInfo = new BackupInfo();
            backupInfo.setData(data);
            backupInfo.setTimestamp(LocalTime.now());
            backupInfo.setJobID(repository.getInfo().getMyRegion().getJob().getId());
            backupInfo.setRegionID(repository.getInfo().getMyRegion().getFullID());
            this.putBackup(backupInfo);
        }

        this.generateRegions();

        data.clear();
        if (this.repository.getRegion() != null)
            data.addAll(this.getBackupFromNode(this.repository.getRegion().getFullID()).getData());

        this.jobExecution.setRegion(repository.getRegion());
        this.jobExecution.getPause().set(true);
    }

    private void generateRegions() {
        String myRegion = "-";

        if (repository.getAllJobs().size() == 0)
            return;

        List<String> nodeIDs = new ArrayList<>(repository.getAllNodes().keySet());
        Collections.sort(nodeIDs);

        //start index and number of nodes on one job
        int nodeIDIndexStart = 0;
        int nodeIDsSize = nodeIDs.size()/repository.getAllJobs().size();
        if (nodeIDs.size() % repository.getAllJobs().size() > 0) {
            nodeIDsSize++;
        }

        int jobIndex = 0;
        for (String jobID : repository.getAllJobs().keySet()) {

            Job job = repository.getAllJobs().get(jobID);
            Map<String, Region> regions = job.getRegions();
            regions.clear();

            jobIndex++;
            int n = job.getStartingPoints().size();
            Queue<String> regionIDQueue = new LinkedList<>();
            Queue<Region> regionQueue = new LinkedList<>();
            int usedNodes = 1;
            regionIDQueue.add("");

            //create Region tree
            Region fullRegion = new Region("","",null,new HashMap<>(),job,job.getStartingPoints());
            regions.put("", fullRegion);
            regionQueue.add(fullRegion);
            while (usedNodes + n - 1 <= nodeIDsSize) {
                String regionID = regionIDQueue.poll();
                Region region = regionQueue.poll();

                if (regionID.equals(""))
                    regions.clear();

                for (int i = 0; i < n; i++) {
                    regionIDQueue.add(regionID + i);
                    Region newRegion = new Region(String.valueOf(i),regionID + i,null,new HashMap<>(),job,RegionUtil.getStartingPointsFromParent(String.valueOf(i), job.getProportion(), region.getStartingPoints()));
                    if (regionID.equals(""))
                        regions.put(regionID + i, newRegion);
                    else
                        region.getChildren().put(regionID + i, newRegion);
                }
                usedNodes += n - 1;
            }


            ArrayList<String> regionIDs = new ArrayList<>(regionIDQueue);
            regionIDs.sort(getSortComparatorRegionIDs());


            //assign IDs to Regions
            int nodeIDIndex = nodeIDIndexStart;
            for (String regionID : regionIDs) {
                repository.getFractalMap().put(getKeyFromJobAndRegion(jobID,regionID), nodeIDs.get(nodeIDIndex));

                Region region = RegionUtil.getRegionFromID(job, regionID);
                region.setNode(repository.getAllNodes().get(nodeIDs.get(nodeIDIndex)));
                if (nodeIDs.get(nodeIDIndex).equals(repository.getInfo().getId())) {
                    myRegion = regionID;
                    repository.getInfo().setMyRegion(region);
                }

                System.out.println("Job: " + job + " Region: " + regionID + " ChordID: " + nodeIDs.get(nodeIDIndex));
                nodeIDIndex++;
            }


            //set new index and size
            nodeIDIndexStart = nodeIDIndexStart + nodeIDsSize;
            nodeIDsSize = nodeIDs.size()/repository.getAllJobs().size();
            if (nodeIDs.size() % repository.getAllJobs().size() > jobIndex)
                nodeIDsSize++;

        }
        System.out.println("My Region: " + myRegion);

    }

    private String getKeyFromJobAndRegion(String job, String region) {
        return job+":"+region;
    }

    private BackupInfo getBackupFromNode(String fractalID) {
        List<BackupInfo> backupList = new ArrayList<>();
        String originalNodeChordID = repository.getFractalMap().get(fractalID);
        BackupInfo info = getBackupDataFromNode(originalNodeChordID);
        if (info != null)
            backupList.add(info);

        String nextNodeChordID = getNextNode(originalNodeChordID);
        BackupInfo nextInfo = getBackupDataFromNode(nextNodeChordID);

        if(nextInfo != null)
            backupList.add(nextInfo);

        String prevNodeChordID = getPrevNode(originalNodeChordID);
        BackupInfo prevInfo = getBackupDataFromNode(prevNodeChordID);

        if(prevInfo != null)
            backupList.add(prevInfo);

        backupList.sort(Comparator.comparing(BackupInfo::getTimestamp));
        return backupList.get(0);
    }

    private BackupInfo getBackupDataFromNode(String id) {
        BackupInfo info = null;
        //TODO: ovo resiti zapravo
        return info;
    }

    private String getNextNode(String key) {
        String result = "ZZZZZZZZZZZZZZZZZZZZZ";
        for (Node node : repository.getAllNodes().values())
            if (node.getId().compareTo(key) > 0 && node.getId().compareTo(result) < 0)
                result = node.getId();
        return result;
    }

    private String getPrevNode(String key) {
        String result = "";
        for (Node node : repository.getAllNodes().values())
            if (node.getId().compareTo(key) < 0 && node.getId().compareTo(result) > 0)
                result = node.getId();
        return result;
    }
    /**
     * Function that returns a comparator for sorting Region IDs
     * @return a Comparator that first sorts by length, then lexicographically
     */
    private Comparator<String> getSortComparatorRegionIDs() {
        return (o1, o2) -> {
            int result = o2.length() - o1.length();
            if (result != 0)
                return result;
            else
                return o1.compareTo(o2);
        };
    }

}
