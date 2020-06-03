package rs.raf.javaproject.requests.job;

import com.fasterxml.jackson.core.type.TypeReference;
import com.squareup.okhttp.Request;
import rs.raf.javaproject.model.Point;
import rs.raf.javaproject.requests.ARequest;
import rs.raf.javaproject.response.RegionStatusResponse;

import java.util.Collection;

public class Status extends ARequest<RegionStatusResponse> {
    public Status(String url) {
        super(url);
        request = new Request.Builder()
                .url(url)
                .get()
                .build();

        returnClass =  new TypeReference<RegionStatusResponse>() {};
    }
}
