package rs.raf.javaproject.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.raf.javaproject.repository.Database;

import java.util.ArrayList;

@Data
@Component
public class SuccessorTable {

    @Autowired
    private Database database;

    private final ArrayList<Node> table;

    public void reconstructTable(){

        table.clear();
        ArrayList<Node> list = new ArrayList<>(database.getAllNodes().values());

        int myPos = list.indexOf(database.getInfo());

        int size = list.size();
        for (int step = 1; step < size; step *=2){

            int succPos = (myPos + step) % size;
            table.add(list.get(succPos));
        }

        System.out.println(database.getInfo().getId() + ":" + table);
    }


}
