package rs.raf.bootstrap.repository;

import org.springframework.stereotype.Repository;
import rs.raf.bootstrap.model.Node;

@Repository
public interface IRepository {

    Node getRandomNode();

    boolean save(Node node);

    boolean remove(Node node);
}
