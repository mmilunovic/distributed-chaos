package rs.raf.javaproject.requests.job;

import com.fasterxml.jackson.core.type.TypeReference;
import com.squareup.okhttp.Request;
import rs.raf.javaproject.model.Point;
import rs.raf.javaproject.requests.ARequest;

import java.util.Collection;

public class DeleteJob extends ARequest<Void> {

    public DeleteJob(String url) {
        super(url);
        request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        returnClass =  new TypeReference<Void>() {};

    }
}
