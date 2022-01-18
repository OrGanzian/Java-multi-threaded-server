package com.bbumgames.client;

import java.io.*;
import java.net.Socket;
import java.util.Collection;
import java.util.LinkedList;

public class client {


    public static void main(String[] args) throws IOException, ClassNotFoundException {

        Socket socket = new Socket("127.0.0.1",8081);
        // socket is an abstraction of 2-way data pipe
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();

        // use decorators
        ObjectInputStream fromServer = new ObjectInputStream(inputStream);
        ObjectOutputStream toServer = new ObjectOutputStream(outputStream);

        toServer.writeObject("10+5");
        String answer =new String((String) fromServer.readObject()) ;
        System.out.println(answer);

    }


}
