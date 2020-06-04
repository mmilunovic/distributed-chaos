package rs.raf.javaproject.repository;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.raf.javaproject.config.MyConfig;
import rs.raf.javaproject.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


@Component
@Data
@RequiredArgsConstructor
public class Database {

    @Autowired
    MyConfig config;

    private final TreeMap<String, Node> allNodes;
    private final Map<String, Job> allJobs;
    private ConcurrentHashMap<String, BackupInfo> backups;
    private List<Point> data;
    private Point tracepoint;
    private Region region;

    public Node getInfo() {
        return config.getMe();
    }

    public Node getSuccessor(){
        ArrayList<Node> nodes = new ArrayList<>(allNodes.values());
        int index = nodes.indexOf(config.getMe());
        int successor = ((index + 1) + nodes.size())  % nodes.size();

        return nodes.get(successor);
    }

    public Node getPredecessor(){
        ArrayList<Node> nodes = new ArrayList<>(allNodes.values());
        int index = nodes.indexOf(config.getMe());
        int predecessor = ((index - 1) + nodes.size()) % nodes.size();

        return nodes.get(predecessor);
    }

}
