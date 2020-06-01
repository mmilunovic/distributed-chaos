package rs.raf.javaproject.requests.bootstrap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import rs.raf.javaproject.config.MyConfig;
import rs.raf.javaproject.model.INode;
import rs.raf.javaproject.model.Node;
import rs.raf.javaproject.requests.ARequest;

public class New extends ARequest<Void> {

    public New(){
        url = "http://"+ MyConfig.bootstrap()+"/api/bootstrap/new";

        try {
            var body = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(INode.parseNode(MyConfig.id()));

            RequestBody requestBody = RequestBody.create(JSON, body);

            request = new Request.Builder()
                    .url(url)
                    .put(requestBody)
                    .build();

            returnClass = Void.class;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


    }



}
