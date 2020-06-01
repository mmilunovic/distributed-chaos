package rs.raf.bootstrap.model;

import lombok.Data;

@Data
public class Node {
    private final String ip;
    private final long port;
}
