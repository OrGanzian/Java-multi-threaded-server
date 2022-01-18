package com.bbumgames.server;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class TcpServer implements Runnable{

    private Integer port;
    private ServerSocket serverSocket;
    private Boolean isStopRequest;
    private ThreadPoolExecutor threadPool;


    public TcpServer() throws IOException {
        this.port = 8081;
        isStopRequest = false;
        this.threadPool = new ThreadPoolExecutor(
                3, 5, 10,
                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10));

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
        while(!isStopRequested()){
            System.out.println("server is ON");
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.threadPool.execute(new CalculatorHandler(clientSocket, "Thread Pooled Server"));
        }

        this.threadPool.shutdown();
        System.out.println("Server is shutting down");

    }
}
