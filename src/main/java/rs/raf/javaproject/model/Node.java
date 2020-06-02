package rs.raf.javaproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import rs.raf.javaproject.config.MyConfig;

import java.util.Objects;

@Data
public class Node implements Comparable<Node>{

    public static final String DELIMITER = ":";

    private String ip;
    private long port;
    @JsonIgnore
    @ToString.Exclude
    private Region myRegion;

    public Node(){ }

    public Node(String ip, long port) {
        this.ip = ip;
        this.port = port;
    }

    public Node(String id){
        String[] parts = id.split(DELIMITER);
        this.ip = parts[0];
        this.port = Long.parseLong(parts[1]);
    }

    @JsonIgnore
    public String getId() {
        return this.ip + DELIMITER + this.port;
    }

    @JsonIgnore
    public String getAddress() {
        return this.ip + DELIMITER + this.port;
    }

    @JsonIgnore
    public boolean isIdle() {
        return myRegion == null;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Node){
            Node other = (Node)obj;
            return this.getId().equals(other.getId());
        }

        return false;
    }


    @Override
    public int compareTo(Node o) {
        return this.getId().compareTo(o.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port);
    }
}
