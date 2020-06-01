package rs.raf.javaproject.model;

import lombok.Data;

import java.util.Objects;

@Data
public class Node implements INode {

    private final String ip;
    private final long port;
    private String jobID;
    private String regionID;

    public Node(String ip, long port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public String getID() {
        return ip + DELIMITER + port;
    }

    @Override
    public String getIP() {
        return ip;
    }

    @Override
    public long getPort() {
        return port;
    }

    @Override
    public boolean isIdle() {
        return jobID == null;
    }

    @Override
    public String getJobID() {
        return jobID;
    }

    @Override
    public String getRegionID() {
        return regionID;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof INode){
            INode other = (INode)obj;
            return this.getID().equals(other.getID());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port);
    }
}
