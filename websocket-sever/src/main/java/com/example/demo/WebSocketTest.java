package com.example.demo;

import org.springframework.stereotype.Component;

import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * @ClassName
 * @Description
 * @Author WangHaiQiang
 * @Date Created in 17:53 2020/4/14
 **/

@ServerEndpoint("/webSocket")
@Component
public class WebSocketTest {

    @OnOpen
    public void connected(Session session) {
        session.getAsyncRemote().sendText("登陆成功!!!");
    }


}
