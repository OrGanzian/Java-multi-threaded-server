package com.bbumgames.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private final Socket socket;
    private final Scanner input ;
    private String calculation;
    private final DataOutputStream dataOutputStream;
    private final DataInputStream dataInputStream;
    private Boolean isServerAlive;


    public Client(String ip, Integer port) throws IOException {
        this.input=new Scanner(System.in);
        this.calculation = "";
        this.isServerAlive=true;
        this.socket=new Socket(ip,port);
        this.dataInputStream =new DataInputStream(socket.getInputStream());
        this.dataOutputStream =new DataOutputStream(socket.getOutputStream());
    }

    public void sendRequest(int type) throws IOException {
        if (type==2) {
            dataOutputStream.writeByte(2);
            dataOutputStream.flush();
        }else{
            dataOutputStream.writeByte(1);
            dataOutputStream.writeUTF(this.calculation);
            dataOutputStream.flush();
        }
    }

    public void askAndGetCalculationFromUser() {
        System.out.print("Enter calculation or 'Exit' to stop:");
        this.calculation= input.next();
    }

    public String getResponse() throws IOException {
        return dataInputStream.readUTF();
    }


    public void close() throws IOException {
        dataInputStream.close();
        dataOutputStream.close();
        this.socket.close();
    }

    public void startClient() throws IOException, ClassNotFoundException {

        while (this.isServerAlive) {
            askAndGetCalculationFromUser();
            if (this.calculation.equals("exit")) {
                sendRequest(2);
                System.out.println("Thanks for using calculator !");
                close();
                break;
            } else {
                sendRequest(1);
                while (true) {
                    String response= getResponse();
                    if (response.equals("alive")) {
                        this.isServerAlive=true;
                        continue;
                    }
                    System.out.println(response);
                    break;
                }



            }
        }
    }

}



