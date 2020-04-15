package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName
 * @Description
 * @Author WangHaiQiang
 * @Date Created in 17:53 2020/4/14
 **/

@Slf4j
@Component
@ServerEndpoint("/hwc4.kluster.xyz/ws")
public class WebSocketTest {


    private Session session;

    private String name;

    private static ConcurrentHashMap<String, WebSocketTest> webSocketSet = new ConcurrentHashMap<>();


    @OnOpen
    public void onOpen() {
        log.info("[WebSocket] 连接成功", webSocketSet.size());
    }

    /* @OnOpen
     public void OnOpen(Session session, @PathParam(value = "name") String name) {
         this.session = session;
         this.name = name;
         // name是用来表示唯一客户端，如果需要指定发送，需要指定发送通过name来区分
         webSocketSet.put(name, this);
         log.info("[WebSocket] 连接成功，当前连接人数为：={}", webSocketSet.size());
     }

 */
    @OnClose
    public void OnClose() {
        webSocketSet.remove(this.name);
        log.info("[WebSocket] 退出成功，当前连接人数为：={}", webSocketSet.size());
    }

    @OnMessage
    public void OnMessage(String message) {
        log.info("[WebSocket] 收到消息：{}", message);
        //判断是否需要指定发送，具体规则自定义
        if (message.indexOf("TOUSER") == 0) {
            String name = message.substring(message.indexOf("TOUSER") + 6, message.indexOf(";"));
            AppointSending(name, message.substring(message.indexOf(";") + 1, message.length()));
        } else {
            GroupSending(message);
        }

    }

    public void GroupSending(String message) {
        for (String name : webSocketSet.keySet()) {
            try {
                webSocketSet.get(name).session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void AppointSending(String name, String message) {
        try {
            webSocketSet.get(name).session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
