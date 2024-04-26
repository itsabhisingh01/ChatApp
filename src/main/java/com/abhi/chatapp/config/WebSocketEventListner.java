package com.abhi.chatapp.config;

import com.abhi.chatapp.chat.ChatMessage;
import com.abhi.chatapp.chat.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListner {
    private final SimpMessageSendingOperations messagingTemplate;
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event){

        // The following code is for to inform the other that who left the chat
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");

        // This loop inform the users if anyone left the chatroom
        if (username!=null){
            log.info("User Disconnected: {}", username);
            var chatMessage = ChatMessage.builder().type(MessageType.LEAVE).sender(username).build();
            messagingTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }

}
