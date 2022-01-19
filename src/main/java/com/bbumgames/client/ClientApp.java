package com.bbumgames.client;

import java.io.IOException;

/**
 * client start point
 */
public class ClientApp {


    public static void main(String[] args)  {

        try {
            Client client = new Client("127.00.00.1", 8081);
            client.startClient();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
}
