package rs.raf.javaproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.raf.javaproject.model.Node;
import rs.raf.javaproject.repository.Database;

import java.util.Collection;

@Service
public class NodeService {

    @Autowired
    private Database repository;

    @Autowired
    private MessageService messageService;


    public Node info(Node node){
        return repository.getInfo();
    }

    public Collection<Node> allNodes(){
        return repository.getAllNodes();
    }

    public boolean ping(String nodeID){
        if(repository.getInfo().getID().equals(nodeID)){
            return true;
        }else{
            // TODO: Posaljem kome treba?
            System.out.println("Nisam ja, saljem dalje");
        }
        return true;
    }

    public void quit(){
    }

    public void left(String nodeID){
        // TODO: Posalji sledbeniku poruku koristeci /api/node/left{nodeID}
        // TODO: Obavesti bootstrap da je cvor nesta sa /api/bootstrap/left
    }

    public void newNode(String nodeID){

        Node newNode = new Node(nodeID);

        System.out.println(repository.getInfo() + " prima poruku o cvoru " + newNode);
        repository.addNode(newNode);

        // Ako ja dobijem poruku da sam se ja ukljucio u mrezu, to znaci da su svi obavesteni
        if(repository.getInfo().equals(newNode)){
            return;
        }


        messageService.sendNewNode(repository.getPredecessor(), newNode);

        // TODO: Radimo rekonstrukciju svog posla
    }

}
