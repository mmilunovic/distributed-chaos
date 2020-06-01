package rs.raf.javaproject.requests.node;

import com.fasterxml.jackson.core.type.TypeReference;
import com.squareup.okhttp.Request;
import rs.raf.javaproject.model.Node;
import rs.raf.javaproject.requests.ARequest;

import java.util.ArrayList;

public class AllNodes extends ARequest<ArrayList<Node>> {

    public AllNodes(String url){
        super(url);
        System.out.println(url);
        request = new Request.Builder()
                .url(url)
                .get()
                .build();
        returnClass = new TypeReference<ArrayList<Node>>() {};
    }


}
