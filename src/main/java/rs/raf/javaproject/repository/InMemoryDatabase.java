package rs.raf.javaproject.repository;

import rs.raf.javaproject.config.MyConfig;
import rs.raf.javaproject.model.IJob;
import rs.raf.javaproject.model.INode;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

public class InMemoryDatabase implements IRepository{

    Collection<INode> allNodes;
    Collection<IJob> allJobs;

    private static InMemoryDatabase instance;

    private INode myInfo = INode.parseNode(MyConfig.id());

    private InMemoryDatabase(){
        allNodes = new ArrayList<>();
        allNodes.add(myInfo);

        allJobs = new ArrayList<>();
    }

    @Override
    public INode getInfo() {
        return myInfo;
    }

    @Override
    public Collection<INode> getAllNodes() {
        return allNodes;
    }

    public static InMemoryDatabase getInstance() {
        if(instance == null){
            instance = new InMemoryDatabase();
        }
        return instance;
    }
}
