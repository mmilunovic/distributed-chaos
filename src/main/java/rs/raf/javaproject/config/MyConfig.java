package rs.raf.javaproject.config;


import jdk.jfr.DataAmount;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import rs.raf.javaproject.model.Node;

@Data
@Component
public class MyConfig {

    @Value("${bootstrap}")
    private String bootstrap;

    @Value("${server.port}")
    private Long port;

    @Value("${public.ip}")
    private String ip;

    private Node me;

    public String getID(){
        return this.getIp() + Node.DELIMITER + this.getPort();
    }



}
