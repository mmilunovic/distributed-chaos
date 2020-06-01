package rs.raf.javaproject.requests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import rs.raf.javaproject.model.Node;

import java.io.IOException;

public abstract class ARequest<T>{

    protected ObjectMapper objectMapper = new ObjectMapper();
    protected Request request;
    protected String url;
    protected TypeReference<T> returnClass;

    public T execute(){
        try {
            Response response = send(request);
            return objectMapper.readValue(response.body().string(), returnClass);
        }catch (IOException e){

        }
        return null;
    }

    private static OkHttpClient defaultClient = new OkHttpClient();
    private static OkHttpClient lowBounderyClient = new OkHttpClient();
    private static OkHttpClient highBounderyClient = new OkHttpClient();

    static{
        // TODO: set timeouts for clients
    }

    protected static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    protected static Response send(Request request) throws IOException {
        return send(request, TimeoutType.DEFAULT);
    }

    protected static Response send(Request request, TimeoutType timeoutType) throws IOException {
        OkHttpClient client = null;
        switch (timeoutType){
            case LOW:
                client = lowBounderyClient;
                break;
            case HIGH:
                client = highBounderyClient;
                break;
            default:
                client = defaultClient;
                break;
        }
        return client.newCall(request).execute();
    }
}
