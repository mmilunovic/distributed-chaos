package org.example.request.job;

import com.fasterxml.jackson.core.type.TypeReference;
import com.squareup.okhttp.Request;
import org.example.request.AbstractRequest;

public class DeleteJob extends AbstractRequest<Void> {
    public DeleteJob(String url) {
        super(url);
        request = new Request.Builder()
                .url(url)
                .delete()
                .build();
        returnClass = new TypeReference<Void>() {};
    }
}
