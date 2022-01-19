package com.bbumgames.server;

import org.springframework.stereotype.Component;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.*;

@Component
public class TcpServer implements Runnable{


    private Integer port;
    private ServerSocket serverSocket;
    private Boolean isStopRequest;
    private ScheduledExecutorService threadPool;
    List<Socket> socketsConnectedList;
    private Boolean isServerAlive;




    public TcpServer() throws IOException {
        this.port = 8081;
        isStopRequest = false;
        socketsConnectedList = new Vector<Socket>();
        isServerAlive=true;
        this.threadPool = Executors.newScheduledThreadPool(5);
        threadPool.	scheduleAtFixedRate(new Runnable() {
                                @Override
                                public void run() {
                                    for (Socket socket : socketsConnectedList) {
                                        try {
                                            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                                            outputStream.writeUTF("alive");
                                            outputStream.flush();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    System.out.println("Alive notification was sent to "+ socketsConnectedList.size()+ " sockets");
                                }
                                },
                10,10,
                TimeUnit.SECONDS);

    }

    private void startListen() {
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException("Cannot listen on port " + this.port);
        }
    }



    public void stop() {
        this.isStopRequest = true;
    }

    private Boolean isStopRequested() {
        return isStopRequest;
    }

    @Override
    public void run() {
        this.startListen();
        System.out.println("server is ON");
        while(!isStopRequested()){
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.threadPool.execute(new CalculatorHandler(clientSocket,socketsConnectedList));
        }

        this.threadPool.shutdown();
        System.out.println("Server is shutting down");

    }
}
