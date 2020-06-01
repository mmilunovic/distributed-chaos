package rs.raf.javaproject.model;

public interface INode {

    String DELIMITER = ":";

    String getID();

    String getIP();

    long getPort();

    boolean isIdle();

    String getJobID();

    String getRegionID();

    static INode parseNode(String id){

        String[] parts = id.split(DELIMITER);

        String ip = parts[0];
        long port = Long.parseLong(parts[1]);

        return new Node(ip, port);
    }

}
