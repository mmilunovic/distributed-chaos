package rs.raf.javaproject.requests.node;

import com.fasterxml.jackson.core.type.TypeReference;
import com.squareup.okhttp.Request;
import rs.raf.javaproject.config.MyConfig;
import rs.raf.javaproject.requests.ARequest;
import rs.raf.javaproject.requests.TimeoutType;

public class Ping extends ARequest<Boolean> {

    public Ping(String nodeID, TimeoutType timeoutType){
        url = "http://" + MyConfig.bootstrap() + "/api/node/ping/" + nodeID;

        request = new Request.Builder()
                .url(url)
                .get()
                .build();

        returnClass =  new TypeReference<Boolean>() {};
    }
}
