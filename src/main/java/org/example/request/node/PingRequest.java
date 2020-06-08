package org.example.request.node;

import com.fasterxml.jackson.core.type.TypeReference;
import com.squareup.okhttp.Request;
import org.example.request.AbstractRequest;

public class PingRequest extends AbstractRequest<Boolean> {
    public PingRequest(String url, int timeout) {
        super(url);

        request = new Request.Builder()
                .url(url)
                .get()
                .build();

        setTimeout(timeout);

        returnClass =  new TypeReference<Boolean>() {};
    }
}
