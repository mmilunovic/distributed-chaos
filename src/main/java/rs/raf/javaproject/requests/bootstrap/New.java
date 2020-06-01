package rs.raf.javaproject.requests.bootstrap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.raf.javaproject.config.MyConfig;
import rs.raf.javaproject.model.Node;
import rs.raf.javaproject.requests.ARequest;

public class New extends ARequest<Void> {



    public New(String url, Node me){
        super(url);

        try {
            var body = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(me);

            RequestBody requestBody = RequestBody.create(JSON, body);

            request = new Request.Builder()
                    .url(url)
                    .put(requestBody)
                    .build();

            returnClass =  new TypeReference<Void>() {};
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


    }



}
