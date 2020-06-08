package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Node implements Comparable<Node>{

    public static final String DELIMITER = ":";

    private String ip;
    private int port;

    public Node(String nodeID){
        String[] parts = nodeID.split(DELIMITER);
        this.ip = parts[0];
        this.port = Integer.parseInt(parts[1]);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return port == node.port &&
                Objects.equals(ip, node.ip);
    }

    @Override
    public int compareTo(Node o) {
        return this.getID().compareTo(o.getID());
    }

    @JsonIgnore
    public String getID(){
        return  ip + DELIMITER + port;
    }



}
