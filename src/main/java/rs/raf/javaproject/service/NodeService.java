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
    private Database database;

    @Autowired
    private SuccessorTable successorTable;

    private JobExecution jobExecution;

    @Autowired
    private MessageService messageService;


    public Node info(Node node){
        return database.getInfo();
    }

    public Collection<Node> allNodes(){
        return database.getAllNodes().values();
    }

    public boolean ping(String nodeID){
        if(database.getInfo().getAddress().equals(nodeID)){
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

        database.getAllNodes().remove(nodeID);

        //restructure();
    }

    public void newNode(String nodeID){

        Node newNode = new Node(nodeID);

        System.out.println(database.getInfo() + " prima poruku o cvoru " + newNode);
        database.getAllNodes().put(newNode.getId(), newNode);

        // Ako ja dobijem poruku da sam se ja ukljucio u mrezu, to znaci da su svi obavesteni
        if(database.getInfo().equals(newNode)){
            return;
        }

        /// BITNO: NE MENJATI REDOSLED
        successorTable.reconstructTable();
        messageService.sendNewNode(database.getPredecessor(), newNode);

        //restructure();
    }

    public void putBackup(BackupInfo backupInfo) {
        database.getBackups().put(backupInfo.getID(), backupInfo);
    }

    public void restructure() {

        if (this.jobExecution == null) {
            this.jobExecution = new JobExecution(this.database, database.getRegion(), new AtomicBoolean(false));
            Thread t = new Thread(jobExecution);
            t.start();
        }
        this.jobExecution.getPause().set(true);

        List<Point> data = this.database.getData();
        if (database.getInfo().getMyRegion() != null) {
            BackupInfo backupInfo = new BackupInfo();
            backupInfo.setData(data);
            backupInfo.setTimestamp(LocalTime.now());
            backupInfo.setJobID(database.getInfo().getMyRegion().getJob().getId());
            backupInfo.setRegionID(database.getInfo().getMyRegion().getFullID());
            this.putBackup(backupInfo);
        }

        this.generateRegions();

        data.clear();
        //if (this.repository.getRegion() != null)
        //    data.addAll(this.getBackupFromNode(this.repository.getRegion().getFullID()).getData());

        if (database.getRegion() != null) {
            if (database.getData().size() == 0)
                database.setTracepoint(new Point(database.getRegion().getJob().getWidth()/2.0, database.getRegion().getJob().getHeight()/2.0));
            else
                database.setTracepoint(database.getData().get(database.getData().size() - 1));

            this.jobExecution.setRegion(database.getRegion());
            this.jobExecution.getPause().set(false);
        }
    }


    private void generateRegions() {
        String myRegion = "-";
        database.setRegion(null);

        if (database.getAllJobs().size() == 0)
            return;

        List<String> nodeIDs = new ArrayList<>(database.getAllNodes().keySet());
        Collections.sort(nodeIDs);

        //start index and number of nodes on one job
        int nodeIDIndexStart = 0;
        int nodeIDsSize = nodeIDs.size()/ database.getAllJobs().size();
        if (nodeIDs.size() % database.getAllJobs().size() > 0) {
            nodeIDsSize++;
        }

        int jobIndex = 0;
        for (String jobID : database.getAllJobs().keySet()) {

            Job job = database.getAllJobs().get(jobID);
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
                        region.getChildren().put(String.valueOf(i), newRegion);
                    regionQueue.add(newRegion);
                }
                usedNodes += n - 1;
            }


            ArrayList<String> regionIDs = new ArrayList<>(regionIDQueue);
            regionIDs.sort(getSortComparatorRegionIDs());


            //assign IDs to Regions
            int nodeIDIndex = nodeIDIndexStart;
            for (String regionID : regionIDs) {
                database.getFractalMap().put(getKeyFromJobAndRegion(jobID,regionID), nodeIDs.get(nodeIDIndex));

                Region region = RegionUtil.getRegionFromID(job, regionID);
                region.setNode(database.getAllNodes().get(nodeIDs.get(nodeIDIndex)));
                if (nodeIDs.get(nodeIDIndex).equals(database.getInfo().getId())) {
                    myRegion = regionID;
                    database.getInfo().setMyRegion(region);
                    database.getInfo().setMyRegion(region);
                    database.setRegion(region);
                }

                System.out.println("Job: " + job + " Region: " + regionID + " ChordID: " + nodeIDs.get(nodeIDIndex));
                nodeIDIndex++;
            }


            //set new index and size
            nodeIDIndexStart = nodeIDIndexStart + nodeIDsSize;
            nodeIDsSize = nodeIDs.size()/ database.getAllJobs().size();
            if (nodeIDs.size() % database.getAllJobs().size() > jobIndex)
                nodeIDsSize++;

        }
        System.out.println("Node: " + database.getInfo() + " My Region: " + myRegion);

    }

    private String getKeyFromJobAndRegion(String job, String region) {
        return job+":"+region;
    }

    //job:regionID
    private BackupInfo getBackupFromNode(String fractalID) {
        List<BackupInfo> backupList = new ArrayList<>();
        String originalNodeChordID = database.getFractalMap().get(fractalID);
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

    private BackupInfo getBackupDataFromNode(String nodeID) {
        BackupInfo info = null;
        //TODO: ovo resiti zapravo
        return info;
    }

    private String getNextNode(String key) {
        String result = "ZZZZZZZZZZZZZZZZZZZZZ";
        for (Node node : database.getAllNodes().values())
            if (node.getId().compareTo(key) > 0 && node.getId().compareTo(result) < 0)
                result = node.getId();
        return result;
    }

    private String getPrevNode(String key) {
        String result = "";
        for (Node node : database.getAllNodes().values())
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

    public Boolean saveBackup(BackupInfo backupInfo) {
        database.getBackups().put(backupInfo.getID(), backupInfo);
        return true;
    }

    public BackupInfo getBackup(String jobID, String regionID) {
        String backupID = jobID + ":" + regionID;
        return database.getBackups().get(backupID);
    }

    public Collection<Job> getAllJobs() {
        return database.getAllJobs().values();
    }
}
