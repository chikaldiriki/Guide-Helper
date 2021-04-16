package ru.hse.guidehelper.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;

import java.util.*;
import java.util.concurrent.ExecutionException;

import org.java_websocket.client.WebSocketClient;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import lombok.Getter;
import ru.hse.guidehelper.dto.ChatDTO;
import ru.hse.guidehelper.dto.MessageDTO;


@Getter
public class Client {
    private String userId;

    private StompSessionHandler sessionHandler = null;
    private StompSession session = null;

    private Map<String, StompSession.Subscription> SubscriptionByUser;

    private static class StompSessionChatHandler extends StompSessionHandlerAdapter {
    }

    private StompSession.Subscription subscribeChat(String otherUserId) {
        String chatId = null; // сходить в БД, спросить /messages/chat/{userId}/{otherUsedId}

        return session.subscribe(chatId, new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Message.class;
            }

            @Override
            public void handleFrame(StompHeaders headers,
                                    Object payload) {
                Message message = (Message) payload;
                System.err.println("send from " + message.getSenderMail() + " to " + message.getReceiverMail()
                        + " : " + message.getText());

                // отрисовать на GUI
            }
        });
    }


    public Client() throws ExecutionException, InterruptedException {
        this.userId = "sad";

        StandardWebSocketClient simpleWebSocketClient =
                new StandardWebSocketClient();
        List<Transport> transports =
                new ArrayList<>(Collections.singletonList(new WebSocketTransport(simpleWebSocketClient)));

        SockJsClient sockJsClient = new SockJsClient(transports);

        WebSocketStompClient stompClient =
                new WebSocketStompClient(sockJsClient);

        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        String urlEndpoint = "ws://192.178.3.17:8080/chat-endpoint";

        this.sessionHandler = new StompSessionChatHandler();
        this.session = stompClient.connect(urlEndpoint, sessionHandler).get();

    }


    public void enterTheChat(String otherUserId) {
        StompSession.Subscription subscription = subscribeChat(otherUserId);
        SubscriptionByUser.put(otherUserId, subscription);
    }

    public void leaveTheChat(String otherUserId) {
        if (SubscriptionByUser.containsKey(otherUserId)) {
            SubscriptionByUser.get(otherUserId).unsubscribe();
        }
    }

    public void sendMessage(String receiverMail, String text) {
        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());
        Message message = new Message()
                .setDispatchTime(ts)
                .setSenderMail(userId)
                .setReceiverMail(receiverMail)
                .setText(text);

        session.send("/topic/chat", message);

        // отрисовать на GUI
    }


    public List<MessageDTO> getAllMessages(String receiverMail) {
        // сходить в БД, спросить
        return null;
    }

    public List<ChatDTO> getAllChats() {
        // сходить в БД, спросить
        return null;
    }
}