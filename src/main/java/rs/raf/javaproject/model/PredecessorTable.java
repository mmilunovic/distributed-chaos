package rs.raf.javaproject.model;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.raf.javaproject.repository.Database;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

@Data
@Component
public class PredecessorTable {

    @Autowired
    private Database database;

    private final ArrayList<Node> table;

    public void reconstructTable(){

        table.clear();
        ArrayList<Node> list = new ArrayList<>(database.getAllNodes().values());

        int myPos = list.indexOf(database.getInfo());

        int size = list.size();
        for (int step = 1; step < size; step *=2){

            int succPos = ((myPos - step) + size) % size;
            table.add(list.get(succPos));
        }

        System.out.println(database.getInfo().getId() + ":" + table);
    }

    public Collection<Node> broadcastingNodes(){
        ArrayList<Node> broadcastingNodes = new ArrayList<>();

        try {
            broadcastingNodes.add(table.get(0));
            broadcastingNodes.add(table.get(1));
            broadcastingNodes.add(table.get(2));
        }catch (IndexOutOfBoundsException e){

        }

        return broadcastingNodes;
    }

    public Node getDelegator(Node node){

        if(table.size() == 0) return null; // Cisto provere radi

        Node ret = null;
        TreeSet<Node> set = new TreeSet<>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return -o1.compareTo(o2);
            }
        });

        set.addAll(table);

        if(set.contains(node)){
            return node;
        }

        set.add(node);

        Node prev = null;
        int i = 0;
        for(Node tableNode: set){
            if(tableNode.equals(node) && i == 0){
                return set.last();
            }

            if(tableNode.equals(node) && i == set.size()){
                return  set.first();
            }

            if(tableNode.equals(node)){
                return prev;
            }
            prev  = tableNode;
            i++;
        }

        return ret;
    }
}
