package org.example.request.bootstrap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.squareup.okhttp.Request;
import org.example.model.Node;
import org.example.request.AbstractRequest;

public class HailRequest extends AbstractRequest<Node> {

    public HailRequest(String url){
        super(url);

        request = new Request.Builder()
                .url(url)
                .get()
                .build();

        returnClass = new TypeReference<Node>() {};

    }
}
