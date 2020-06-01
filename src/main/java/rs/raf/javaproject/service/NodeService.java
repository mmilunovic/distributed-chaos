package rs.raf.javaproject.service;

import rs.raf.javaproject.model.INode;
import rs.raf.javaproject.repository.IRepository;
import rs.raf.javaproject.repository.InMemoryDatabase;

import java.util.Collection;

public class NodeService {


    private InMemoryDatabase repository;

    public  NodeService(){
        repository = InMemoryDatabase.getInstance();
    }

    public INode info(INode node){
        return null;
    }

    public Collection<INode> allNodes(){
        return null;
    }

    public boolean ping(String nodeID){
        return true;
    }

    public void quit(){
    }

    public void left(String nodeID){
        // TODO: Posalji sledbeniku poruku koristeci /api/node/left{nodeID}
        // TODO: Obavesti bootstrap da je cvor nesta sa /api/bootstrap/left
    }

    public void newNode(String nodeID){
        // TODO: Prosledjujemo ovu istu poruku sledbeniku pomocu /api/node/new/{nodeID}
        // TODO: Radimo rekonstrukciju svog posla
    }
}
