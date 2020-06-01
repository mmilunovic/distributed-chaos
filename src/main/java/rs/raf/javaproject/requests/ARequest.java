package rs.raf.javaproject.requests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.springframework.beans.factory.annotation.Autowired;
import rs.raf.javaproject.config.MyConfig;
import rs.raf.javaproject.model.Node;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public abstract class ARequest<T>{

    protected static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    protected static final long DEFAULT_READ_TIMEOUT = 10; // 10 sekundi

    protected ObjectMapper objectMapper = new ObjectMapper();
    protected Request request;
    protected String url;
    protected TypeReference<T> returnClass;

    private final OkHttpClient client = new OkHttpClient();

    public ARequest(String url){
        this.url = url;
    }

    public void setTimeout(int seconds){
        client.setReadTimeout(seconds, TimeUnit.SECONDS);
    }

    public T execute(){
        try {
            Response response = client
                    .newCall(request)
                    .execute();
            return objectMapper.readValue(response.body().string(), returnClass);
        }catch (IOException e){

        }finally {
            client.setReadTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS);
        }
        return null;
    }

    @Override
    public String toString() {
        return url;
    }
}
