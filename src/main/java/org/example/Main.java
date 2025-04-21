package org.example;

import com.cosium.matrix_communication_client.MatrixResources;
import com.cosium.matrix_communication_client.Message;
import com.cosium.matrix_communication_client.RoomResource;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        MatrixResources matrix = MatrixResources.factory()
                        .builder()
                        .https()
                        .hostname("matrix.org")
                        .defaultPort()
                        .usernamePassword("JDOE", "ONOE")
                        .build();

        RoomResource room = matrix
                .rooms()
                .byId("!ROOMID:ETC");

        room.sendMessage(Message.builder().body("Hi from bot!").formattedBody("Hi from <b>bot</b>!").build());
        System.out.print("greeting printed!");
    }
}