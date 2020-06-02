package rs.raf.javaproject.requests.job;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import rs.raf.javaproject.model.Job;
import rs.raf.javaproject.requests.ARequest;

public class NewJob extends ARequest<Void> {
    public NewJob(String url, Job job) {
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
