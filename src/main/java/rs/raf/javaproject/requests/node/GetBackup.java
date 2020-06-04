package rs.raf.javaproject.requests.node;

import com.fasterxml.jackson.core.type.TypeReference;
import com.squareup.okhttp.Request;
import rs.raf.javaproject.model.Point;
import rs.raf.javaproject.requests.ARequest;

import java.util.Collection;

public class GetBackup extends ARequest<Collection<Point>> {

    public GetBackup(String url) {
        super(url);

        request = new Request.Builder()
                .url(url)
                .get()
                .build();

        returnClass = new TypeReference<Collection<Point>>() {};
    }
}

