package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalTime;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Backup {

    private String jobID;
    private String regionID;
    @ToString.Exclude
    private List<Point> data;

    @JsonIgnore
    private LocalTime timestamp = LocalTime.now();

    @JsonIgnore
    public String getID() {
        return jobID + ":" + regionID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Backup backup = (Backup) o;
        return Objects.equals(jobID, backup.jobID) &&
                Objects.equals(regionID, backup.regionID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobID, regionID);
    }
}
