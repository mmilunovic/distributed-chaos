package rs.raf.javaproject.requests.node;

import com.fasterxml.jackson.core.type.TypeReference;
import com.squareup.okhttp.Request;
import rs.raf.javaproject.config.MyConfig;
import rs.raf.javaproject.requests.ARequest;

public class Ping extends ARequest<Boolean> {

    public Ping(String url, int seconds){
        super(url);

        request = new Request.Builder()
                .url(url)
                .get()
                .build();

        setTimeout(seconds);

        returnClass =  new TypeReference<Boolean>() {};
    }
}
