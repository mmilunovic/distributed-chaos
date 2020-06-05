package org.example.request.bootstrap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import org.example.model.Node;
import org.example.request.AbstractRequest;

public class NodeLeftRequest extends AbstractRequest<Void> {
    public NodeLeftRequest(String url, Node exitingNode) {
        super(url);
        try {
            var body = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(exitingNode);

            RequestBody requestBody = RequestBody.create(JSON, body);

            request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            returnClass = new TypeReference<Void>() {};

        }catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
