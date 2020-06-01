package rs.raf.javaproject.repository;


import org.springframework.stereotype.Repository;
import rs.raf.javaproject.model.IJob;
import rs.raf.javaproject.model.INode;
import rs.raf.javaproject.model.Node;

import java.util.Collection;

@Repository
public interface IRepository {

    INode getInfo();

    Collection<INode> getAllNodes();

    boolean remove(INode node);

    boolean newNode(INode node);

    boolean remove(IJob job);

    boolean addJob(IJob job);

    boolean addNodes(Collection<INode> nodes);

}
