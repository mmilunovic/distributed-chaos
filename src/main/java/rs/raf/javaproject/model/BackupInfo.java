package rs.raf.javaproject.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BackupInfo {

    private String jobID;
    private String regionID;
    private LocalTime timestamp;
    private List<Point> data;

    public String getId() {
        return jobID + ":" + regionID;
    }
}
