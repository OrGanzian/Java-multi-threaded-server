package com.bbumgames.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Client is able to connect a server by ip and port (TCP).
 * Client interact with user with Console UI.
 * Client is able to enter math calculations ( + , - , * , /) and get the result.
 * By enter 'exit', the client close safely the connection with the server.
 *
 * Sending messages to the server by the following protocol->
 * sending byte number (message type identifier), and then a UTF string, for example:
 * calculation message : 1 .... "2+8"
 * exit message        : 2 .... "exit"
 *
 * receiving messages from the client by the following protocol ->
 *  UTF string
 *  Server is alive message  :  "alive"
 *  calculation response message             :  "10"
 */
public class Client {

    private final Socket socket;
    private final Scanner input;
    private final DataOutputStream dataOutputStream;
    private final DataInputStream dataInputStream;
    private String calculation;
    private Boolean isServerAlive;


    public Client(String ip, Integer port) throws IOException {
        this.input = new Scanner(System.in);
        this.calculation = "";
        this.isServerAlive = true;
        this.socket = new Socket(ip, port);
        this.dataInputStream = new DataInputStream(socket.getInputStream());
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
    }

    /**
     * @param type
     * type=1: send calculation to server.
     * type=1: send exit to server. (stop the session)
     * @throws IOException
     */
    public void sendRequest(int type) throws IOException {
        if (type == 2) {
            dataOutputStream.writeByte(2);
            dataOutputStream.flush();
        } else {
            dataOutputStream.writeByte(1);
            dataOutputStream.writeUTF(this.calculation);
            dataOutputStream.flush();
        }
    }

    public void askAndGetCalculationFromUser() {
        System.out.print("Enter calculation or 'exit' to stop:");
        this.calculation = input.next();
    }

    /**
     * @return one UTF string from the input stream
     * @throws IOException
     */
    public String getResponse() throws IOException {
        return dataInputStream.readUTF();
    }

    public void close() throws IOException {
        dataInputStream.close();
        dataOutputStream.close();
        this.socket.close();
    }

    /**
     * Running client's calculations until the server is not alive OR 'exit' by the user.
     * @throws IOException
     * @throws ClassNotFoundException
     */
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
                    String response = getResponse();
                    if (response.equals("alive")) {//After sending the request,response will be in the input stream.However,"alive" messages may also appear.
                        this.isServerAlive = true;
                        continue;
                    }
                    System.out.println(response);
                    break;
                }
            }
        }
    }

}



