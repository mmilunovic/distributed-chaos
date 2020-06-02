package rs.raf.bootstrap.repository;

import org.springframework.stereotype.Component;
import rs.raf.bootstrap.model.Node;

import java.util.ArrayList;
import java.util.Random;


@Component
public class Database{

    private ArrayList<Node> activeNodes;

    private Database(){
        activeNodes = new ArrayList<>();
    }
    public synchronized Node getRandomNode() {
        if(activeNodes.size() == 0){
            return new Node(null, 0);
        }else {
            Random random = new Random();
            int index = random.nextInt(activeNodes.size());
            return activeNodes.get(index);
        }
    }

    public synchronized boolean save(Node node) {
        System.out.println(node + " joined!");
        return activeNodes.add(node);
    }

    public synchronized boolean remove(Node node) {
        System.out.println(node + " left!");
        return activeNodes.remove(node);
    }

}
