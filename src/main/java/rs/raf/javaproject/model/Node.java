package rs.raf.javaproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import rs.raf.javaproject.config.MyConfig;

import java.util.Objects;

@Data
public class Node {

    public static final String DELIMITER = ":";

    private String ip;
    private long port;

    private String jobID;
    private String regionID;

    public Node(){ }

    public Node(String ip, long port) {
        this.ip = ip;
        this.port = port;
    }

    @JsonIgnore
    public String getID() {
        return ip + DELIMITER + port;
    }

    public String getIP() {
        return ip;
    }

    public long getPort() {
        return port;
    }

    @JsonIgnore
    public boolean isIdle() {
        return jobID == null;
    }

    @JsonIgnore
    public String getJobID() {
        return jobID;
    }

    @JsonIgnore
    public String getRegionID() {
        return regionID;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Node){
            Node other = (Node)obj;
            return this.getID().equals(other.getID());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port);
    }
}
