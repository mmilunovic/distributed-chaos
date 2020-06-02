package rs.raf.javaproject.repository;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import rs.raf.javaproject.config.MyConfig;
import rs.raf.javaproject.model.*;

import java.util.List;
import java.util.Map;


@Component
@Data
@RequiredArgsConstructor
public class Database {

    @Autowired
    MyConfig config;

    private final Map<String, Node> allNodes;
    private final Map<String, Job> allJobs;
    private final Map<String, BackupInfo> backups;
    private List<Point> data;
    private Point tracepoint;
    private Map<String, String> fractalMap;
    private Node predecessor;
    private Node successor;
    private Region region;

    public Node getInfo() {
        return config.getMe();
    }

}
