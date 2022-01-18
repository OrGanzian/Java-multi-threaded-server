package com.bbumgames.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class client {


    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("127.0.0.1",8081);
        // socket is an abstraction of 2-way data pipe
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        Integer answer = (Integer) inputStream.read();
        System.out.println(answer);

    }


}
