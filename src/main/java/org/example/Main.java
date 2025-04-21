package org.example;

import com.cosium.matrix_communication_client.MatrixResources;
import com.cosium.matrix_communication_client.Message;
import com.cosium.matrix_communication_client.RoomResource;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.print("Hello and welcome!");

        for (int i = 1; i <= 5; i++) {
            //TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
            // for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
            System.out.println("i = " + i);
        }

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
    }
}