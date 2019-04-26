import com.wizaord.boursycrypto.gatewaywebsocket.MyStompSessionHandler;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.Scanner;

public class StomClient {

  public static void main(String[] args) {
    WebSocketClient client = new StandardWebSocketClient();
    WebSocketStompClient stompClient = new WebSocketStompClient(client);

    stompClient.setMessageConverter(new MappingJackson2MessageConverter());

    StompSessionHandler sessionHandler = new MyStompSessionHandler();
    stompClient.connect("wss://ws-feed.pro.coinbase.com", sessionHandler);

    new Scanner(System.in).nextLine(); // Don't close immediately.
  }
}
