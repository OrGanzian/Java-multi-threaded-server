package com.bbumgames.server;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.net.Socket;
import java.util.List;

/**
 *    After the server submit the clients in the threadPool, the CalculatorHandler
 *    class is the responsible for serving the client's request (each client in different thread)
 *
 */
public class CalculatorHandler implements Runnable {

    private final Socket clientSocket;
    private final List<Socket> socketsConnectedList;
    private DataInputStream dataInputStream;
    private DataOutputStream outputStream;

    public CalculatorHandler(Socket clientSocket, List<Socket> socketsConnectedList) {
        this.socketsConnectedList = socketsConnectedList;
        this.clientSocket = clientSocket;
        try {
            this.dataInputStream = new DataInputStream(clientSocket.getInputStream());
            this.outputStream = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * As soon as the run method start, the client is saved in the available socketsConnectedList
     *  * receiving messages from the server by the following protocol->
     *  * sending byte number(message type identifier), and then a UTF string, for example:
     *  * calculation message : 1 .... "2+8"
     *  * exit message        : 2 .... "exit"
     *  Finally, remove the client from the socketsConnectedList and close the connection properly.
     */
    public void run() {
        try {
            socketsConnectedList.add(this.clientSocket);
            boolean done = false;
            while (!done) {   // continue solving calculations until exit message
                byte messageType = dataInputStream.readByte();
                switch (messageType) {
                    case 1: // calculation message handle
                        String question = dataInputStream.readUTF();
                        String resultToResponse = this.solveCalculation(question);
                        System.out.println("Request: " + question + " -> " + "Response: " + resultToResponse);
                        outputStream.writeUTF(resultToResponse);
                        outputStream.flush();
                        break;
                    case 2:
                        done = true; // exit message handle
                        break;
                }
            }

            // closing connection here
            socketsConnectedList.remove(this.clientSocket); //remove the client from the socketsConnectedList
            dataInputStream.close();
            outputStream.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Using ScriptEngineManager to execute scripts.
     * If string not valid, return a "calculation in not valid" message
     * @param calculation as String
     * @return result of the calculation
     */
    private String solveCalculation(String calculation) {
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        try {
            return engine.eval(calculation).toString();
        } catch (ScriptException e) {
            return "calculation in not valid";
        }
    }
}
