package rs.raf.javaproject.service;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.raf.javaproject.config.MyConfig;
import rs.raf.javaproject.model.Node;
import rs.raf.javaproject.requests.ARequest;
import rs.raf.javaproject.requests.bootstrap.Hail;
import rs.raf.javaproject.requests.bootstrap.Left;
import rs.raf.javaproject.requests.bootstrap.New;
import rs.raf.javaproject.requests.node.AllNodes;
import rs.raf.javaproject.requests.node.NotifyNewNode;
import rs.raf.javaproject.requests.node.Ping;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

@Component
public class MessageService {

    @Autowired
    private MyConfig config;

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
        System.out.println(receiver + " - " +newNode);
        return "http://" + receiver
                .getId() + "/api/node/new/" +newNode
                .getId();
    }

    // TODO: Slanje poruka mora biti asinhrono

    public synchronized Node sendBootstrapHail(){
        Hail hail = new Hail(getBootstrapHailUrl());
        return hail.execute();
    }

    public synchronized void sendBootstrapLeft(){
        Left left = new Left(getBootstrapLeftUrl(), config.getMe());
        left.execute();
    }

    public synchronized void sendBootstrapNew(){
        New n = new New(getBootstrapNewUrl(), config.getMe());
        n.execute();
    }

    public synchronized Collection<Node> sendGetAllNodes(Node node){
        AllNodes allNodes = new AllNodes(getAllNodesUrl(node));
        return allNodes.execute();
    }

    public synchronized Boolean sendPing(Node posrednik, Node destinacija, Integer timeout){
        Ping ping = new Ping(getPingNodesUrl(posrednik, destinacija), timeout);
        return ping.execute();
    }

    public synchronized void sendNewNode(Node receiver, Node newNode){
        NotifyNewNode notifyNewNode = new NotifyNewNode(getNewNodeUrl(receiver, newNode));
        System.out.println(config.getMe() + " is sending to " + notifyNewNode);
        notifyNewNode.execute();
    }




}
