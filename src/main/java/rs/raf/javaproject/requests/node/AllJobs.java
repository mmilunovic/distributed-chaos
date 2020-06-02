package rs.raf.javaproject.requests.node;

import com.fasterxml.jackson.core.type.TypeReference;
import com.squareup.okhttp.Request;
import rs.raf.javaproject.model.Job;
import rs.raf.javaproject.model.Node;
import rs.raf.javaproject.requests.ARequest;

import java.util.ArrayList;
import java.util.Collection;

public class AllJobs extends ARequest<ArrayList<Job>> {

    public AllJobs(String url) {
        super(url);
        request = new Request.Builder()
                .url(url)
                .get()
                .build();
        returnClass = new TypeReference<ArrayList<Job>>() {};

    }
}
