package rs.raf.javaproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface INode {

    String DELIMITER = ":";

    @JsonIgnore
    String getID();

    String getIP();

    long getPort();

    @JsonIgnore
    boolean isIdle();

    @JsonIgnore
    String getJobID();

    @JsonIgnore
    String getRegionID();

    static INode parseNode(String id){

        String[] parts = id.split(DELIMITER);

        String ip = parts[0];
        long port = Long.parseLong(parts[1]);

        return new Node(ip, port);
    }

}
