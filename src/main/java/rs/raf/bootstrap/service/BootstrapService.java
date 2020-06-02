package rs.raf.bootstrap.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.raf.bootstrap.model.Node;
import rs.raf.bootstrap.repository.Database;

@Service
public class BootstrapService {

    @Autowired
    private Database database;

    public boolean save(Node node){
        return database.save(node);
    }

    public boolean remove(Node node){
        return database.remove(node);
    }

    public Node getConnection(){
        return database.getRandomNode();
    }

}
