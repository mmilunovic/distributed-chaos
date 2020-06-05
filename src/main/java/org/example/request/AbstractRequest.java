package org.example.request;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public abstract class AbstractRequest<T> {

    protected static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    protected static final long DEFAULT_READ_TIMEOUT = 10; // 10 sekundi


    private final OkHttpClient client = new OkHttpClient();

    protected ObjectMapper objectMapper = new ObjectMapper();
    protected Request request;
    protected String url;
    protected TypeReference<T> returnClass;

    public AbstractRequest(String url){
        this.url = url;
        try {
            Thread.sleep(300+(new Random()).nextInt(500));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void setTimeout(int seconds){
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
