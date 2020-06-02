package rs.raf.javaproject.requests.job;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import rs.raf.javaproject.model.Point;
import rs.raf.javaproject.requests.ARequest;

import java.util.Collection;

public class MyResult extends ARequest<Collection<Point>> {

    public MyResult(String url) {
        super(url);
        request = new Request.Builder()
                .url(url)
                .get()
                .build();

        returnClass =  new TypeReference<Collection<Point>>() {};

    }
}
