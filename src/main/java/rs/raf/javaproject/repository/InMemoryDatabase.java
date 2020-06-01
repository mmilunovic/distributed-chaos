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

    private INode me = INode.parseNode(MyConfig.id());

    private InMemoryDatabase(){
        allNodes = new ArrayList<>();
        allNodes.add(me);

        allJobs = new ArrayList<>();
    }

    @Override
    public INode getInfo() {
        return me;
    }

    @Override
    public boolean remove(INode node) {
        return allNodes.remove(node);
    }

    @Override
    public boolean newNode(INode node) {
        return allNodes.add(node);
    }

    @Override
    public Collection<INode> getAllNodes() {
        return allNodes;
    }

    @Override
    public boolean remove(IJob job) {
        return allJobs.remove(job);
    }

    @Override
    public boolean addJob(IJob job) {
        return allJobs.add(job);
    }

    @Override
    public boolean addNodes(Collection<INode> nodes) {
        return allNodes.addAll(nodes);
    }

    public static InMemoryDatabase getInstance() {
        if(instance == null){
            instance = new InMemoryDatabase();
        }
        return instance;
    }


}
