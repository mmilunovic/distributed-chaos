package rs.raf.javaproject.requests.node;

import com.fasterxml.jackson.core.type.TypeReference;
import com.squareup.okhttp.Request;
import rs.raf.javaproject.requests.ARequest;

public class NodeLeft extends ARequest<Void> {

    public NodeLeft(String url) {
        super(url);

        request = new Request.Builder()
                .url(url)
                .get()
                .build();

        returnClass = new TypeReference<Void>() {};

    }
}
