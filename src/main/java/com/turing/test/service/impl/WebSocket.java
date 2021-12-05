package com.turing.test.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turing.test.service.DialogueService;
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
@ServerEndpoint("/chat/{uid}/{chatterUid}")
@Component
@Slf4j
public class WebSocket {

    private String uid;

    private String chatterUid;

    private Session session;

    private static final Map<String,WebSocket> webSocketMap = new ConcurrentHashMap<>();

    @Autowired
    DialogueService dialogueService;

    @OnOpen
    public void openConnect(@PathParam("uid") String uid, @PathParam("chatterUid") String chatterUid, Session session) {
        this.uid = uid;
        this.chatterUid = chatterUid;
        this.session = session;
        webSocketMap.put(uid,this);
        log.info("WebSocket->openConnect: {} connected to server", uid);
    }

    @OnClose
    public void closeConnect(@PathParam("uid") String uid, Session session) {
        webSocketMap.remove(uid);
        dialogueService.endDialogue(uid);
        log.info("WebSocket->closeConnect: {} disconnected to server", uid);
    }

    @OnError
    public void errorConnect(Session session, Throwable error) {
        log.warn("WebSocket->errorConnect: {}", error.getMessage());
    }

    @OnMessage
    public void send(String message, Session session) throws IOException {
        webSocketMap.get(chatterUid).session.getBasicRemote().sendText(message);
    }

}
