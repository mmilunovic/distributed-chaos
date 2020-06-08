package org.example.request.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import org.example.model.Backup;
import org.example.request.AbstractRequest;

public class SaveBackupRequest extends AbstractRequest<Void> {
    public SaveBackupRequest(String url, Backup backup) {
        super(url);

        try {
            var body = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(backup);

            RequestBody requestBody = RequestBody.create(JSON, body);

            request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            returnClass =  new TypeReference<Void>() {};

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }
}
