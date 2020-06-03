package rs.raf.javaproject.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegionStatusResponse {
    private String regionID;
    private String nodeID;
    private int numberOfPoints;
}
