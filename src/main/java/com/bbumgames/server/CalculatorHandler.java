package com.bbumgames.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class CalculatorHandler implements Runnable{

    private Socket clientSocket = null;
    private String serverText   = null;

    public CalculatorHandler(Socket clientSocket, String serverText) {
        this.clientSocket = clientSocket;
        this.serverText   = serverText;
    }

    public void run() {
        try {
            InputStream input  = clientSocket.getInputStream();
            OutputStream output = clientSocket.getOutputStream();
            long time = System.currentTimeMillis();
            output.write(10);
//            output.write(("HTTP/1.1 200 OK\n\nWorkerRunnable: " +
//                    this.serverText + " - " +
//                    time +
//                    "").getBytes());
            output.close();
            input.close();
            System.out.println("Request processed: " + time);
        } catch (IOException e) {
            //report exception somewhere.
            e.printStackTrace();
        }
    }
}
