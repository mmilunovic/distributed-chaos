package rs.raf.javaproject.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.raf.javaproject.model.Point;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultResponse {

    private String jobID;
    private List<Point> startingPoints;
    private ArrayList<Point> data;
}
