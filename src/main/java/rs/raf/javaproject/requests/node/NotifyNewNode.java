package rs.raf.javaproject.requests.node;

import com.fasterxml.jackson.core.type.TypeReference;
import com.squareup.okhttp.Request;
import rs.raf.javaproject.model.Node;
import rs.raf.javaproject.requests.ARequest;

import java.util.ArrayList;

public class NotifyNewNode extends ARequest<Void> {

    public NotifyNewNode(String url) {
        super(url);

        request = new Request.Builder()
                .url(url)
                .get()
                .build();

        returnClass = new TypeReference<Void>() {};
    }
}
