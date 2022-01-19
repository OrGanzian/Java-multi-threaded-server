package com.bbumgames.server;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.net.Socket;
import java.util.List;

public class CalculatorHandler implements Runnable{



    private Socket clientSocket = null;
    private DataInputStream dataInputStream;
    private DataOutputStream outputStream;
    private List<Socket> socketsConnectedList;

    public CalculatorHandler(Socket clientSocket, List<Socket> socketsConnectedList) {
        this.socketsConnectedList=socketsConnectedList;
        this.clientSocket = clientSocket;
        try {
            this.dataInputStream= new DataInputStream(clientSocket.getInputStream());
            this.outputStream= new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String solveCalculation(String calculation){
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        try {
            return engine.eval(calculation).toString();
        } catch (ScriptException e) {
            return "calculation in not valid";
        }
    }


    public void run() {
        try {
            socketsConnectedList.add(this.clientSocket);
            boolean done = false;
            while(!done) {
                byte messageType = dataInputStream.readByte();
                switch(messageType)
                {
                    case 1: // Type A
                        String question = dataInputStream.readUTF();
                        String resultToResponse = this.solveCalculation(question);
                        System.out.println("Request: " + question + " -> " + "Response: " + resultToResponse); //works
                        outputStream.writeUTF(resultToResponse);
                        outputStream.flush(); // Send off the data
                        break;
                    case 2: // Type B
                        done = true;
                        break;
                }
            }


            // closing connection here
            socketsConnectedList.remove(this.clientSocket);
            dataInputStream.close();
            outputStream.close();
            clientSocket.close();
            System.out.println("socket connection closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
