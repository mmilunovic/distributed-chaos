package org.example.request.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import org.example.model.Node;
import org.example.request.AbstractRequest;

public class BroadcastNewNodeRequest extends AbstractRequest<Void> {
    public BroadcastNewNodeRequest(String url, Node servent) {
        super(url);
        try {
            var body = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(servent);

            RequestBody requestBody = RequestBody.create(JSON, body);

            request = new Request.Builder()
                    .url(url)
                    .put(requestBody)
                    .build();

            returnClass =  new TypeReference<Void>() {};

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
