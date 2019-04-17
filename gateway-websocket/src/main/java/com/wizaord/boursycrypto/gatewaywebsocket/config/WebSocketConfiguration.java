package com.wizaord.boursycrypto.gatewaywebsocket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;

@Configuration
public class WebSocketConfiguration {

    @Bean
    public WebSocketContainer gdaxWebSocketContainer() {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.setDefaultMaxTextMessageBufferSize(9999999);
        container.setDefaultMaxSessionIdleTimeout(0);
        return container;
    }

}
