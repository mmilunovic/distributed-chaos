package rs.raf.javaproject.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.raf.javaproject.model.Job;
import rs.raf.javaproject.model.Region;

import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusResponse {

    private String jobID;
    private Collection<RegionStatusResponse> allJobs;
}
