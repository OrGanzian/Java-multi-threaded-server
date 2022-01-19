package com.bbumgames.server;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

@Component
public class TcpServer implements Runnable{

    private Integer port;
    private ServerSocket serverSocket;
    private Boolean isStopRequest;
    private ScheduledExecutorService threadPool;
    private Boolean isServerAlive;



    public TcpServer() throws IOException {
        this.port = 8081;
        isStopRequest = false;
        this.threadPool = Executors.newScheduledThreadPool(5);
        threadPool.	scheduleAtFixedRate(new Runnable() {
                                @Override
                                public void run() {
                                    System.out.println("5 sec");
                                }
                                },
                0,5,
                TimeUnit.SECONDS);

    }

    private void startListen() {
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException("Cannot listen on port " + this.port);
        }
    }

//    private void startIsAliveNotification() {
//        Timer timer = new Timer();
//        TimerTask timerTask =new TimerTask() {
//            @Override
//            public void run() {
//                System.out.println("alive");
//            }
//        };
//        timer.schedule(timerTask,0,10000);
//    }

    public void stop() {
        this.isStopRequest = true;
    }

    private Boolean isStopRequested() {
        return isStopRequest;
    }

    @Override
    public void run() {
        this.startListen();
//        startIsAliveNotification();
        System.out.println("server is ON");
        while(!isStopRequested()){
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.threadPool.execute(new CalculatorHandler(clientSocket));
        }

        this.threadPool.shutdown();
        System.out.println("Server is shutting down");

    }
}
