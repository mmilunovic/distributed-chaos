package org.example.config;

import lombok.Data;
import org.example.model.Node;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class ServentConfig {

    @Value("${bootstrap}")
    private String bootstrap;

    @Value("${server.port}")
    private int port;

    @Value("${public.ip}")
    private String address;

    private Node servent;

}
