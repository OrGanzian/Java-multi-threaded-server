package com.bbumgames.server;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.net.Socket;

public class CalculatorHandler implements Runnable{

    private Socket clientSocket = null;


    public CalculatorHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;

    }

    private String solveCalculation(String claculation) throws ScriptException {
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        return engine.eval(claculation).toString();
    }




    public void run() {
        try {
            InputStream input  = clientSocket.getInputStream();    // from client
            OutputStream output = clientSocket.getOutputStream();  //to client

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(output);
            ObjectInputStream objectInputStream = new ObjectInputStream(input);


            String question = "";
            while(!question.equals("exit")){
               question =  objectInputStream.readObject().toString(); //10+5 or 'exit'

                String resultToResponse = this.solveCalculation(question);
                objectOutputStream.writeObject(resultToResponse);
                System.out.println("Request: " + question + " -> " + "Response: " + resultToResponse); //works
            }



            output.close();
            input.close();
            System.out.println("socket connection closed");
        } catch (IOException | ClassNotFoundException | ScriptException e) {
            //report exception somewhere.
            e.printStackTrace();
        }
    }
}
