package com.bbumgames.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    ////members

    private String ip;
    private Integer port;
    private Scanner input = new Scanner(System.in);
    private StringBuilder calculation;
    private Socket socket;
    private  InputStream inputStream;
    private OutputStream outputStream;
    private ObjectInputStream fromServer;
    private  ObjectOutputStream toServer;

    //////////// methods

    public Client(String ip, Integer port) throws IOException {
        this.ip=ip;
        this.port=port;
        this.input=new Scanner(System.in);
        this.calculation = new StringBuilder();
        this.socket=new Socket(ip,port);
        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();
        this.fromServer = new ObjectInputStream(inputStream);
        this.toServer = new ObjectOutputStream(outputStream);
    }

    public void sendRequest() throws IOException {
        toServer.writeObject(this.calculation);
    }

    public void AskAndGetCalculationFromUser() {
        this.calculation.delete(0, calculation.length());
        System.out.print("Enter calculation or 'Exit' to stop:");
        this.calculation= new StringBuilder((input.next()));
    }

    public String getResponse() throws IOException, ClassNotFoundException {
        return new String((String) fromServer.readObject());
    }

    public StringBuilder getCalculation() {
        return calculation;
    }
//    public static void main(String[] args) throws IOException, ClassNotFoundException {
//
//        Socket socket = new Socket("127.0.0.1",8081);
//        // socket is an abstraction of 2-way data pipe
//        InputStream inputStream = socket.getInputStream();
//        OutputStream outputStream = socket.getOutputStream();
//
//        // use decorators
//        ObjectInputStream fromServer = new ObjectInputStream(inputStream);
//        ObjectOutputStream toServer = new ObjectOutputStream(outputStream);

//        toServer.writeObject("10+5");
//
//        String answer =new String((String) fromServer.readObject()) ;
//        System.out.println(answer);





//        while (!guess.equals(secret)) {
//            System.out.print("Enter calculation please:");
//            guess = input.next();
//
//            if (guess.equals(secret)) {
//                System.out.println("enter");
//            } else {
//                System.out.println("try again");
//            }
//        }


    }



