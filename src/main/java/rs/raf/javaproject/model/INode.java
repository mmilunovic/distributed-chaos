package rs.raf.javaproject.model;

public interface INode {

    String getID();

    String getIP();

    long getPort();

    boolean isIdle();

    String getJobID();

    String getRegionID();

}
