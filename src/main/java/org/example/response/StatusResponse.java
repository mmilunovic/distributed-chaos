package org.example.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusResponse {

    private String jobID;
    private String regionID;
    private String nodeID;
    private int numberofPoints;
}
