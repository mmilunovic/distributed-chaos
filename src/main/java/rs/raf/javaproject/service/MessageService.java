package rs.raf.javaproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.raf.javaproject.config.MyConfig;
import rs.raf.javaproject.handler.ExecutorPool;
import rs.raf.javaproject.model.*;

import rs.raf.javaproject.model.SuccessorTable;
import rs.raf.javaproject.requests.bootstrap.Hail;
import rs.raf.javaproject.requests.bootstrap.Left;
import rs.raf.javaproject.requests.bootstrap.New;
import rs.raf.javaproject.requests.job.JobResult;
import rs.raf.javaproject.requests.job.NewJob;
import rs.raf.javaproject.requests.job.RegionResult;
import rs.raf.javaproject.requests.job.Status;
import rs.raf.javaproject.requests.node.*;
import rs.raf.javaproject.response.RegionStatusResponse;
import rs.raf.javaproject.response.ResultResponse;
import rs.raf.javaproject.response.StatusResponse;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class MessageService {

    @Autowired
    private MyConfig config;

    @Autowired
    private SuccessorTable successorTable;

    @Autowired
    private PredecessorTable predecessorTable;

    @Autowired
    private ExecutorPool executorPool;

    private String getBootstrapHailUrl(){
        return "http://" + config.getBootstrap() + "/api/bootstrap/hail";
    }

    private String getBootstrapNewUrl(){
        return "http://" + config.getBootstrap() + "/api/bootstrap/new";
    }

    private String getBootstrapLeftUrl(){
        return "http://" + config.getBootstrap() + "/api/bootstrap/left";
    }

    private String getAllNodesUrl(Node node){
        return "http://" + node.getAddress() + "/api/node/allNodes";
    }

    private String getPingNodesUrl(Node node, Node ping){
        return "http://" + node.getAddress() + "/api/node/ping/" + ping.getAddress();
    }
    private String getNewNodeUrl(Node receiver, Node newNode){
        return "http://" + receiver.getId() + "/api/node/new/" +newNode.getId();
    }

    private String getSaveBackupUrl(Node receiver){
        return "http://" + receiver.getId() + "/api/node/backup";
    }

    private String getAllJobsUrl(Node receiver){
        return "http://" + receiver.getId() + "/api/node/allJobs";
    }

    private String getNewJobUrl(Node receiver){
        return  "http://" + receiver.getId() + "/api/jobs/start";
    }

    private String getJobWorkUrl(String receiver, String forWho, String jobID){
        return "http://" + receiver + "/api/delegate/" + forWho + "/jobs/" + jobID;
    }
    
    private String getRegionWorkUlr(String receiver, String forWho, String jobID, String regionID){
        return "http://" + receiver + "/api/delegate/" + forWho + "/jobs/" + jobID + "/" + regionID;
    }

    private String getLeftUrl(Node receiver, Node nodeThatLeft){
        return "http://" + receiver.getId() + "/api/node/left/" + nodeThatLeft.getId();
    }

    private String getStatusUrl(Node receiver, String forWho, String jobID){
        return "http://" + receiver.getId() + "/api/delegate/" + forWho + "/status" + jobID;
    }

    // TODO: Slanje poruka mora biti asinhrono


    public Node sendBootstrapHail(){
        Hail hail = new Hail(getBootstrapHailUrl());
        return hail.execute();
    }

    public void sendBootstrapLeft(Node node){

        executorPool.submit(new Runnable() {
            @Override
            public void run() {
                Left left = new Left(getBootstrapLeftUrl(), node);
                left.execute();
            }
        });
    }

    public void sendBootstrapNew(){
        executorPool.submit(new Runnable() {
            @Override
            public void run() {
                New n = new New(getBootstrapNewUrl(), config.getMe());
                n.execute();
            }
        });
    }


    public Collection<Node> sendGetAllNodes(Node node){
        AllNodes allNodes = new AllNodes(getAllNodesUrl(node));
        return allNodes.execute();
    }

    public Boolean sendPing(Node posrednik, Node destinacija, Integer timeout){
        Ping ping = new Ping(getPingNodesUrl(posrednik, destinacija), timeout);
        return ping.execute();
    }

    public void sendNewNode(Node newNode){

        for(Node node: successorTable.broadcastingNodes()){
            executorPool.submit(new Runnable() {
                @Override
                public void run() {
                    NotifyNewNode notifyNewNode = new NotifyNewNode(getNewNodeUrl(node, newNode));
                    notifyNewNode.execute();
                }
            });
        }

        for(Node node: predecessorTable.broadcastingNodes()){
            executorPool.submit(new Runnable() {
                @Override
                public void run() {
                    NotifyNewNode notifyNewNode = new NotifyNewNode(getNewNodeUrl(node, newNode));
                    notifyNewNode.execute();
                }
            });
        }
    }

    public Boolean sendSaveBackup(Node receiver, BackupInfo backupInfo){
        SaveBackup saveBackup = new SaveBackup(getSaveBackupUrl(receiver), backupInfo);
        return saveBackup.execute();
    }

    public Collection<Job> sendGetAllJobs(Node node) {
        AllJobs allJobs = new AllJobs(getAllJobsUrl(node));
        return allJobs.execute();
    }

    public void broadcastNewJob(Job job){
        for (Node node: successorTable.broadcastingNodes()){
            executorPool.submit(new Runnable() {
                @Override
                public void run() {
                    NewJob newJob = new NewJob(getNewJobUrl(node), job);
                    newJob.execute();
                }
            });
        }
    }

    public ResultResponse sendGetResult(String jobID, List<String> recipients) {
        ResultResponse resultResponse = new ResultResponse();
        resultResponse.setJobID(jobID);

        resultResponse.setData(new ArrayList<>());

        for(String nodeID: recipients){

            if(nodeID.equals(config.getMe().getId())){
                // Salje sebi ukoliko je on na tom job-u
                JobResult jobResult = new JobResult(getJobWorkUrl(nodeID, nodeID, jobID));
                resultResponse.getData().addAll(jobResult.execute());
            }else{
                // Koristimo delegate za slanje poruke
                Node delegator = successorTable.getDelegator(successorTable.getDatabase().getAllNodes().get(nodeID));

                JobResult jobResult = new JobResult(getJobWorkUrl(delegator.getId(), nodeID, jobID));                               // Ne saljemo direktno cvoru nego delegatoru
                // Ako nam je cvor u successor tabeli vratice njega
                resultResponse.getData().addAll(jobResult.execute());
            }
        }

        return resultResponse;
    }

    public ResultResponse sendGetResult(String jobID, String regionID, List<String> recipients) {
        ResultResponse resultResponse = new ResultResponse();
        resultResponse.setJobID(jobID);

        resultResponse.setData(new ArrayList<>());

        for(String nodeID: recipients){

            if(nodeID.equals(config.getMe().getId())){
                // Salje sebi ukoliko je on na tom job-u
                RegionResult regionResult = new RegionResult(getRegionWorkUlr(nodeID, nodeID, jobID, regionID));
                resultResponse.getData().addAll(regionResult.execute());
            }else{
                // Koristimo delegate za slanje poruke
                Node delegator = successorTable.getDelegator(successorTable.getDatabase().getAllNodes().get(nodeID));

                RegionResult regionResult = new RegionResult(getRegionWorkUlr(delegator.getId(), nodeID, jobID, regionID));
                System.out.println(getRegionWorkUlr(delegator.getId(), nodeID, jobID, regionID));
                resultResponse.getData().addAll(regionResult.execute());
            }
        }

        return resultResponse;
    }


    public void broadcastLeaveMessage(Node nodeThatLeft){
        for (Node node: successorTable.broadcastingNodes()){
            executorPool.submit(new Runnable() {
                @Override
                public void run() {
                    NodeLeft left = new NodeLeft(getLeftUrl(node, nodeThatLeft));
                    left.execute();
                }
            });
        }
    }

    @PreDestroy
    public void leave(){
        sendBootstrapLeft(config.getMe());
    }

    public StatusResponse sendGetStatus(String jobID, List<String> receiverIDs) {

        StatusResponse statusResponse = new StatusResponse();
        statusResponse.setJobID(jobID);

        for(String nodeID: receiverIDs){
            RegionStatusResponse regionStatusResponse = new RegionStatusResponse();

            if(nodeID.equals(config.getMe().getId())){
                regionStatusResponse.setRegionID(successorTable.getDatabase().getRegion().getFullID());
                regionStatusResponse.setNumberOfPoints(successorTable.getDatabase().getData().size());
            }else{
                Node delegator = successorTable.getDelegator(successorTable.getDatabase().getAllNodes().get(nodeID));

                Status status = new Status(getStatusUrl(delegator, nodeID, jobID));                               // Ne saljemo direktno cvoru nego delegatoru

                regionStatusResponse = status.execute();
            }
            statusResponse.getAllJobs().add(regionStatusResponse);
        }

        return statusResponse;
    }

    public RegionStatusResponse sendGetMyStatus() {
        return null;
    }
}









