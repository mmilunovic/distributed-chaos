package rs.raf.javaproject.requests.bootstrap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import rs.raf.javaproject.config.MyConfig;
import rs.raf.javaproject.model.Node;
import rs.raf.javaproject.requests.ARequest;

public class Left extends ARequest<Void> {

    public Left(String url, Node node){
        super(url);

       try {
           var body = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);

           RequestBody requestBody = RequestBody.create(JSON, body);

           request = new Request.Builder()
                   .url(url)
                   .get()
                   .build();
           returnClass = new TypeReference<Void>() {};

       }catch (JsonProcessingException e) {
           e.printStackTrace();
       }

    }
}
