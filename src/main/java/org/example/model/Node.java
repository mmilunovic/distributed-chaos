package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

@Data
@AllArgsConstructor
public class Node implements Comparable<Node>{

    public static final String DELIMITER = ":";

    private String address;
    private int port;

    public Node(String nodeID){
        String[] parts = nodeID.split(DELIMITER);
        this.address = parts[0];
        this.port = Integer.parseInt(parts[1]);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return port == node.port &&
                Objects.equals(address, node.address);
    }

    @Override
    public int compareTo(Node o) {
        return this.getID().compareTo(o.getID());
    }

    public String getID(){
        return  address + DELIMITER + port;
    }



}
