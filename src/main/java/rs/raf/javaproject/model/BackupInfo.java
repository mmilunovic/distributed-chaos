package rs.raf.javaproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private List<Point> data;
    @JsonIgnore
    private LocalTime timestamp = LocalTime.now();

    @JsonIgnore
    public String getID() {
        return jobID + ":" + regionID;
    }
}
