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
            System.out.println("Thanks for using calculator");

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
}
