package org.example;

import com.cosium.matrix_communication_client.MatrixResources;
import com.cosium.matrix_communication_client.Message;
import com.cosium.matrix_communication_client.RoomResource;

public class Main {
    public static void main(String[] args) {
        MatrixResources matrix = MatrixResources.factory().builder()
                        .uri("https://matrix.org")
                        .usernamePassword("JDOE", "ONOE")
                        .build();

        RoomResource room = matrix
                .rooms()
                .byId("!ROOMID:ETC");

        room.sendMessage(Message.builder().body("Hi from bot!").formattedBody("Hi from <b>bot</b>!").build());
        System.out.println("Message sent to Matrix room");
    }
}