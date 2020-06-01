package rs.raf.bootstrap.repository;

import rs.raf.bootstrap.model.Node;

import java.util.ArrayList;
import java.util.Random;


public class InMemoryRepository implements IRepository{

    private ArrayList<Node> activeNodes;

    private static InMemoryRepository instance;

    private InMemoryRepository(){
        activeNodes = new ArrayList<>();
    }

    @Override
    public synchronized Node getRandomNode() {
        if(activeNodes.size() == 0){
            return new Node(null, 0);
        }else {
            Random random = new Random();
            int index = random.nextInt(activeNodes.size());
            return activeNodes.get(index);
        }
    }

    @Override
    public synchronized boolean save(Node node) {
        activeNodes.add(node);
        System.out.println(activeNodes);
        return true;
    }

    @Override
    public synchronized boolean remove(Node node) {
        return activeNodes.remove(node);
    }

    public static InMemoryRepository getInstance() {
        if(instance == null) instance = new InMemoryRepository();
        return instance;
    }
}
