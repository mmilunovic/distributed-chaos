package rs.raf.javaproject.repository;


import org.springframework.stereotype.Repository;
import rs.raf.javaproject.model.INode;

import java.util.Collection;

@Repository
public interface IRepository {

    INode getInfo();

    Collection<INode> getAllNodes();




}
