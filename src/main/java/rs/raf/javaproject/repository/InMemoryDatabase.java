package rs.raf.javaproject.repository;

import rs.raf.javaproject.model.IJob;
import rs.raf.javaproject.model.INode;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

public class InMemoryDatabase {

    Collection<INode> allNodes;
    Collection<IJob> allJobs;

    private static InMemoryDatabase instance;

    private InMemoryDatabase(){
        allNodes = new ArrayList<>();
        allJobs = new ArrayList<>();
    }

    public static InMemoryDatabase getInstance() {
        if(instance == null){
            instance = new InMemoryDatabase();
        }
        return instance;
    }
}
