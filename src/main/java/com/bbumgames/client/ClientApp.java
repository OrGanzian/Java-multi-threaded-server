package com.bbumgames.client;

import java.io.IOException;

public class ClientApp {


    public static void main(String[] args) {
        try {
            Client client = new Client("127.0.0.1",8081);

            while (!client.getCalculation().equals("exit")) {
                client.AskAndGetCalculationFromUser();
                client.sendRequest();
                System.out.println(client.getResponse());
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
}
