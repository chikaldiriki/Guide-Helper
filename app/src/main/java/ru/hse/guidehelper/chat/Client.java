package ru.hse.guidehelper.chat;

import java.lang.reflect.Type;
import java.sql.Timestamp;

import java.util.*;
import java.util.concurrent.ExecutionException;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.SockJsClient;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;

import org.springframework.web.socket.messaging.WebSocketStompClient;

import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.messaging.simp.stomp.StompSession;

public class Client {
    private String userId;

    private StompSessionHandler sessionHandler = null;
    private StompSession session = null;

    private Map<String, StompSession.Subscription> SubscriptionByUser;

    private class StompSessionChatHandler extends StompSessionHandlerAdapter {
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

    public Client(String userId) throws ExecutionException, InterruptedException {
        this.userId = userId;

        WebSocketClient simpleWebSocketClient =
                new StandardWebSocketClient();
        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(simpleWebSocketClient));

        SockJsClient sockJsClient = new SockJsClient(transports);

        WebSocketStompClient stompClient =
                new WebSocketStompClient(sockJsClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        String urlEndpoint = "ws://localhost:8080/chat-endpoint";

        this.sessionHandler = new StompSessionChatHandler();
        this.session = stompClient.connect(urlEndpoint, sessionHandler)
                .get();
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
                .setText(text)
                .setStatus(true);

        session.send("/topic/chat", message);

        // отрисовать на GUI
    }

    public List<Message> getAllMessages(String receiverMail) {
        // сходить в БД, спросить
        return null;
    }
}
