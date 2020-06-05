package rs.raf.javaproject.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;
import rs.raf.javaproject.JavaProjectApplication;
import rs.raf.javaproject.model.*;
import rs.raf.javaproject.model.Point;
import rs.raf.javaproject.repository.Database;
import rs.raf.javaproject.schedule.JobExecution;

import java.awt.*;
import java.time.LocalTime;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class NodeService {

    @Autowired
    @Getter
    private Database database;

    @Autowired
    private SuccessorTable successorTable;
    @Autowired
    private PredecessorTable predecessorTable;

    private JobExecution jobExecution;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ConfigurableApplicationContext context;


    public Node info(){
        return database.getInfo();
    }

    public Collection<Node> allNodes(){
        return database.getAllNodes().values();
    }

    public boolean ping(String nodeID){
        if(database.getInfo().getAddress().equals(nodeID)){
            return true;
        }else{
            Boolean pingResult = messageService.sendPing(database.getAllNodes().get(nodeID), database.getAllNodes().get(nodeID), 1);

            if(pingResult == null){
                return false;
            }
            return pingResult;

        }
    }

    public void quit(){
        synchronized (database.getInfo()) {
            messageService.broadcastLeaveMessage(this.database.getInfo());
            this.jobExecution.getExit().set(true);
            SpringApplication.exit(context,() -> 0);
        }

    }

    public void left(String nodeID){
        // TODO: Obavesti bootstrap da je cvor nesta sa /api/bootstrap/left - fix, ovo mozda nije potrebno
        // TODO: Treba preuzeti backup-e ukoliko je to potrebno
        Node nodeThatLeft = database.getAllNodes().get(nodeID);

        if(nodeThatLeft != null) {
            synchronized (database.getInfo()) {
                database.getAllNodes().remove(nodeID);

                successorTable.reconstructTable();
                predecessorTable.reconstructTable();
            }

            messageService.broadcastLeaveMessage(nodeThatLeft);

            restructure();
        }

    }

    public void newNode(String nodeID){

        if(!database.getAllNodes().containsKey(nodeID)){
            Node newNode = new Node(nodeID);

            synchronized (database.getInfo()) {
                database.getAllNodes().put(newNode.getId(), newNode);

                /// BITNO: NE MENJATI REDOSLED
                successorTable.reconstructTable();
                predecessorTable.reconstructTable();
            }
            messageService.sendNewNode(newNode);

            restructure();
        }
    }

    private void putBackup(BackupInfo backupInfo) {
        database.getBackups().put(backupInfo.getID(), backupInfo);
    }

    public void restructure() {
        synchronized (database.getInfo()) {
            if (this.jobExecution == null) {
                this.jobExecution = new JobExecution(this.database, database.getRegion(), new AtomicBoolean((false)), new AtomicBoolean(false));
                Thread t = new Thread(jobExecution);
                t.start();
            }
            this.jobExecution.getPause().set(true);

            List<Point> data = this.database.getData();
            // AKo je radio neki region, onda rezultat sacuva kao backup
            if (database.getInfo().getMyRegion() != null) {
                BackupInfo backupInfo = new BackupInfo();
                backupInfo.setData(new ArrayList<Point>(data));
                backupInfo.setTimestamp(LocalTime.now());
                backupInfo.setJobID(database.getInfo().getMyRegion().getJob().getId());
                backupInfo.setRegionID(database.getInfo().getMyRegion().getFullID());
                this.putBackup(backupInfo);
            }
            Region oldRegion = this.generateRegions();

            data.clear();

            if (database.getRegion() != null) {
                if (oldRegion != null)
                    data.addAll(this.getBackupFromNode(oldRegion));
                if (database.getData().size() == 0)
                    database.setTracepoint(new Point(database.getRegion().getJob().getWidth() / 2.0, database.getRegion().getJob().getHeight() / 2.0));
                else
                    database.setTracepoint(database.getData().get(database.getData().size() - 1));

                this.jobExecution.setRegion(database.getRegion());
                this.jobExecution.getPause().set(false);
            }
        }
    }


    private Region generateRegions() {
        Region oldRegion = null;

        String myRegion = "-";
        database.setRegion(null);

        if (database.getAllJobs().size() == 0)
            return null;

        //start index and number of nodes on one job
        int nodeIDIndexStart = 0;
        int nodeIDsSize = database.getAllNodes().size()/ database.getAllJobs().size();
        if (database.getAllNodes().size() % database.getAllJobs().size() > 0) {
            nodeIDsSize++;
        }

        int jobIndex = 0;
        Iterator<String> nodeIterator = database.getAllNodes().keySet().iterator();
        for (String jobID : database.getAllJobs().keySet()) {

            Job job = database.getAllJobs().get(jobID);
            Map<String, Region> regions = job.getRegions();
            Map<String, Region> oldRegions = new HashMap<>(regions);
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
            for (String regionID : regionIDs) {
                String nodeID = nodeIterator.next();
                Region region = RegionUtil.getRegionFromID(job, regionID);
                region.setNode(database.getAllNodes().get(nodeID));
                if (nodeID.equals(database.getInfo().getId())) {
                    myRegion = regionID;

                    if (myRegion.length() == 0)
                        oldRegion = oldRegions.get("");
                    else if(!oldRegions.containsKey(myRegion.substring(0,1)))
                        oldRegion = oldRegions.get("");
                    else
                        oldRegion = RegionUtil.getRegionFromID(oldRegions.get(myRegion.substring(0,1)),(myRegion.substring(1)));

                    database.getInfo().setMyRegion(region);
                    database.getInfo().setMyRegion(region);
                    database.setRegion(region);
                }

                if (this.database.getInfo().getPort() == 1000)
                    System.out.println("Job: " + job + " Region: " + regionID + " ChordID: " + nodeID);
            }


            //set new index and size
            nodeIDsSize = database.getAllNodes().size()/ database.getAllJobs().size();
            if (database.getAllNodes().size() % database.getAllJobs().size() > jobIndex)
                nodeIDsSize++;
            nodeIDIndexStart = nodeIDIndexStart + nodeIDsSize;
        }
        System.out.println("Node: " + database.getInfo() + " My Region: " + myRegion);
        if (oldRegion != null)
            System.out.println(oldRegion);
        return oldRegion;
    }

    private String getKeyFromJobAndRegion(String job, String region) {
        return job+":"+region;
    }

    //job:regionID
    private Set<Point> getBackupFromNode(Region region) {
        Map<String, String> originalNodeIDs = RegionUtil.getAllSubregionNodeAndJobIDs(region);
        HashSet<Point> points = new HashSet<>();
        for (Map.Entry<String, String> originalNodeID : originalNodeIDs.entrySet()) {
            BackupInfo info = getBackup(originalNodeID.getKey(), region.getJob().getId(), originalNodeID.getValue());
            if (info != null)
                points.addAll(info.getData());

            String nextNodeID = getNextNode(originalNodeID.getKey());
            info = getBackup(nextNodeID, region.getJob().getId(), originalNodeID.getValue());

            if (info != null)
                points.addAll(info.getData());

            String prevNodeID = getPrevNode(originalNodeID.getKey());
            info = getBackup(prevNodeID, region.getJob().getId(), originalNodeID.getValue());

            if (info != null)
                points.addAll(info.getData());

        }


        if (!region.getFullID().equals(database.getRegion().getFullID())) {
            int len = database.getRegion().getStartingPoints().size();
            int[] xPoints = new int[len];
            int[] yPoints = new int[len];
            Point[] pointsArr = new Point[len];

            for (int i = 0; i < len; i++) {
                Point p = database.getRegion().getStartingPoints().get(i);
                xPoints[i] = p.getX().intValue();
                yPoints[i] = p.getY().intValue();
            }
            Polygon poly = new Polygon(xPoints, yPoints, len);

            HashSet<Point> pomocniPoints = new HashSet<>(points);
            for (Point p : pomocniPoints)
                if (!poly.contains(p.getX(), p.getY()))
                    points.remove(p);
            if (points.size() == 0 && pomocniPoints.size() > 10) {
                System.out.println("Something's wrong with backup");
            }
        }
        return points;
    }


    private String getNextNode(String key) {
        String result = database.getAllNodes().ceilingKey(key);
        if (result == null)
            result = database.getAllNodes().firstKey();
        return result;
    }

    private String getPrevNode(String key) {
        String result = database.getAllNodes().floorKey(key);
        if (result == null)
            result = database.getAllNodes().lastKey();
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

    public void saveBackup(BackupInfo backupInfo) {
        synchronized (database.getInfo()) {
            database.getBackups().put(backupInfo.getID(), backupInfo);
        }
    }

    public BackupInfo getBackup(String nodeID, String jobID, String regionID) {
        if (!database.getAllNodes().containsKey(nodeID))
            return null;

        if(database.getInfo().getId().equals(nodeID)) {
            return database.getBackups().get(getKeyFromJobAndRegion(jobID, regionID));
        }else{
            return messageService.sendGetData(nodeID,jobID,regionID);
        }

    }

    public Collection<Job> getAllJobs() {
        return database.getAllJobs().values();
    }
}
