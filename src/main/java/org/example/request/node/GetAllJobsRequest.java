package org.example.request.node;
import com.fasterxml.jackson.core.type.TypeReference;
import com.squareup.okhttp.Request;
import org.example.model.Job;
import org.example.model.Node;
import org.example.request.AbstractRequest;

import java.util.ArrayList;

public class GetAllJobsRequest extends AbstractRequest<ArrayList<Job>> {

    public GetAllJobsRequest(String url) {
        super(url);
        request = new Request.Builder()
                .url(url)
                .get()
                .build();
        returnClass = new TypeReference<ArrayList<Job>>() {};
    }
}
