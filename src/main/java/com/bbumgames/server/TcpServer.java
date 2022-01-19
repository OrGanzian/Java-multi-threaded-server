package com.bbumgames.server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.*;

/**
 * A TCP server that can accept simple mathematical calculations and return their results.
 * The server opens a listening Server-socket and listen for clientSocket.
 * The server can support multiple clients simultaneously, thanks to a threadPool.
 * After clientSocket connection the socket is submitting to the threadPool to process in other thread.
 * The server holds socketsConnectedList that maintain all the clients available at the moment.
 * Every minute the server sends a message to all the clients that he is alive( UTF string : "alive").
 * The use of ScheduledExecutorService let run a task after requested time just by passing a Runnable object.
 *
 */
public class TcpServer implements Runnable {

    private static volatile TcpServer instance;
    private static final Object lock = new Object();
    private final Integer port;
    private final List<Socket> socketsConnectedList;
    private final ScheduledExecutorService threadPool;
    private Boolean isServerAlive;
    private ServerSocket serverSocket;
    private final Thread thread;

    private TcpServer() throws IOException {
        this.port = 8081;
        this.socketsConnectedList = new Vector<Socket>();
        this.isServerAlive = true;
        this.threadPool = Executors.newScheduledThreadPool(5);
        this.thread=new Thread(this);
        this.thread.start();
    }

    public static TcpServer getInstance() {
        if (instance == null) {
            synchronized (lock) {
                try {
                    instance = new TcpServer();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        return instance;
    }

    private void setAliveRunnable() {
        threadPool.scheduleAtFixedRate(new Runnable() {
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
                System.out.println("Alive notification was sent to " + socketsConnectedList.size() + " sockets");
            }
        }, 10, 10, TimeUnit.SECONDS);
    }

    /**
     * Listening to clients and submits all the income
     * clients into threadPool to achieve multithreaded server
     */
    @Override
    public void run() {
        this.setAliveRunnable();
        this.startListen();
        while (isServerAlive()) {
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.threadPool.execute(new CalculatorHandler(clientSocket, socketsConnectedList));
        }
        this.threadPool.shutdown();
        System.out.println("Server is shutting down");
    }

    public synchronized void stop() {
        this.isServerAlive = false;
    }

    public synchronized Boolean isServerAlive() {
        return isServerAlive;
    }

    private void startListen() {
        try {
            this.serverSocket = new ServerSocket(port);
            System.out.println("server is ON");
        } catch (IOException e) {
            throw new RuntimeException("Cannot listen on port " + this.port);
        }
    }
}
