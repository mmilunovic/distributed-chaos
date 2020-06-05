package org.example.request.job;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import org.example.model.Job;
import org.example.request.AbstractRequest;

public class StartJobRequest extends AbstractRequest<Void> {
    public StartJobRequest(String url, Job job) {
        super(url);

        try {
            var body = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(job);

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
