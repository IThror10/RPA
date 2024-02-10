package com.RPA.config;

import com.RPA.service.RobotService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
    @Autowired
    private final RobotService robotService;
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(robotWebSocketHandler(), "/api/robot/connect")
                .addInterceptors(new HttpSessionHandshakeInterceptor()) //Make attributes available for socket session
                .setAllowedOrigins("*") // Default is Same-Origin only
                .withSockJS() // Enable SockJS support
                .setStreamBytesLimit(512 * 1024)
//                .setHttpMessageCacheSize(1000)
                .setDisconnectDelay(1000 * 15)
        ;
    }

    @Bean
    public RobotSocketHandler robotWebSocketHandler() {
        return new RobotSocketHandler();
    }

//    @Bean
//    public ServletServerContainerFactoryBean createWebSocketContainer() {
//        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
//        container.setMaxTextMessageBufferSize(8192);
//        container.setMaxBinaryMessageBufferSize(8192);
//        container.setMaxSessionIdleTimeout(1000l * 60 * 10);
//        return container;
//    }
}
