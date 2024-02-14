package com.RPA.config;

import com.RPA.entity.InMemoryRobot;
import com.RPA.service.RobotService;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class RobotSocketHandler extends TextWebSocketHandler {
    // Map to store WebSocket sessions mapped to unique identifiers
    private final Map<String, WebSocketSession> sessionMap = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Generate a unique identifier for this WebSocket connection
        String sessionId = UUID.randomUUID().toString();

        // Associate the session with the generated identifier
        sessionMap.put(sessionId, session);

        System.out.println("New WebSocket connection established. Session ID: " + sessionId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Handle incoming messages from clients
        System.out.println("Received message from session " + getSessionId(session) + ": " + message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // Remove the session from the map when the connection is closed
        String sessionId = getSessionId(session);
        sessionMap.remove(sessionId);
        System.out.println("WebSocket connection closed. Session ID: " + sessionId);
    }

    // Utility method to get the session ID from a WebSocket session
    private String getSessionId(WebSocketSession session) {
        for (Map.Entry<String, WebSocketSession> entry : sessionMap.entrySet()) {
            if (entry.getValue().equals(session)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
