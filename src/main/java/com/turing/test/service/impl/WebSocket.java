package com.turing.test.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
* @Author Yibo Wen
* @Date 11/29/2021 11:51 PM
**/
@ServerEndpoint("/chat/{uid}/{target_uid}")
@Component
@Slf4j
public class WebSocket {

    private String uid;

    private String target_uid;

    private Session session;

    private static Map<String,WebSocket> webSocketMap = new ConcurrentHashMap<>();

    @OnOpen
    public void openConnect(@PathParam("uid") String uid, @PathParam("target_uid") String target_uid, Session session) {
        this.uid = uid;
        this.target_uid = target_uid;
        this.session = session;
        webSocketMap.put(uid,this);
        log.info("WebSocket->openConnect: {} connected to server", uid);
    }

    @OnClose
    public void closeConnect(@PathParam("uid") String uid, Session session) {
        webSocketMap.remove(uid);
        log.info("WebSocket->closeConnect: {} disconnected to server", uid);
    }

    @OnError
    public void errorConnect(Session session, Throwable error) {
        log.warn("WebSocket->errorConnect: {}", error.getMessage());
    }

    @OnMessage
    public void send(String message, Session session) throws IOException {
        webSocketMap.get(target_uid).session.getBasicRemote().sendText(message);
    }

}
