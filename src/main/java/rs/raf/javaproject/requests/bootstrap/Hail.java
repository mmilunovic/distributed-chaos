package rs.raf.javaproject.requests.bootstrap;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import rs.raf.javaproject.config.MyConfig;
import rs.raf.javaproject.model.Node;
import rs.raf.javaproject.requests.ARequest;

import java.io.IOException;

public class Hail extends ARequest<Node> {

    public Hail(){
        url = "http://"+ MyConfig.bootstrap()+"/api/bootstrap/hail";
        request = new Request.Builder()
                .url(url)
                .get()
                .build();
        returnClass = Node.class;
    }

}
