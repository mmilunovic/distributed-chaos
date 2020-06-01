package rs.raf.javaproject.requests.bootstrap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.squareup.okhttp.Request;
import rs.raf.javaproject.config.MyConfig;
import rs.raf.javaproject.model.Node;
import rs.raf.javaproject.requests.ARequest;

import javax.annotation.PostConstruct;


public class Hail extends ARequest<Node> {

    public Hail(String url){
        super(url);
        request = new Request.Builder()
                .url(url)
                .get()
                .build();
        returnClass = new TypeReference<Node>() {};
    }

}
