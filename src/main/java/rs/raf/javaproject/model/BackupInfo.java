package rs.raf.javaproject.model;

import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
public class BackupInfo {

    private String jobID;
    private String regionID;
    private LocalTime timestamp;
    private List<Point> data;

    public String getId() {
        return jobID + ":" + regionID;
    }
}
