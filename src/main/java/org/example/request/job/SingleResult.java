package org.example.request.job;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import org.example.model.Job;
import org.example.model.Point;
import org.example.request.AbstractRequest;

import java.util.Collection;

public class SingleResult extends AbstractRequest<Collection<Point>> {
    public SingleResult(String url, Job requestedJob) {
        super(url);

        try {
            var body = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestedJob);

            RequestBody requestBody = RequestBody.create(JSON, body);

            request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            returnClass =  new TypeReference<Collection<Point>>() {};
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
