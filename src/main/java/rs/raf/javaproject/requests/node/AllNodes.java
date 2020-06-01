package rs.raf.javaproject.requests.node;

import com.fasterxml.jackson.core.type.TypeReference;
import com.squareup.okhttp.Request;
import rs.raf.javaproject.model.Node;
import rs.raf.javaproject.requests.ARequest;

import java.util.ArrayList;
import java.util.Collection;

public class AllNodes extends ARequest<Collection<Node>> {

    public AllNodes(Node node){
        url = "http://" + node.getID() + "/api/node/allNodes";
        request = new Request.Builder()
                .url(url)
                .get()
                .build();
        returnClass = new TypeReference<Collection<Node>>() {};
    }


}
