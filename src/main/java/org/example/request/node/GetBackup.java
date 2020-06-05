package org.example.request.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import org.example.model.Backup;
import org.example.model.Node;
import org.example.request.AbstractRequest;


public class GetBackup extends AbstractRequest<Backup> {
    public GetBackup(String url, Node finalDestination) {
        super(url);
        try {
            var body = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(finalDestination);

            RequestBody requestBody = RequestBody.create(JSON, body);

            request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            returnClass =  new TypeReference<Backup>() {};
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
