package com.example.myapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.myapp.Active;

public class Client {

    //region Fields

    private Socket clientSocket;
    private int port;
    private String IPAddress;
    private String serverAnswer;
    private final ObservableList<Active> actives;
    private final ObservableList<Operation> operations;

    //endregion

    //region Class lifecycle

    public Client() {
        this.port = 0;
        this.IPAddress = "";
        this.actives = FXCollections.observableArrayList();
        this.operations = FXCollections.observableArrayList();
    }


    public Client(String IPAddress, int port) {
        this.port = port;
        this.IPAddress = IPAddress;
        this.actives = FXCollections.observableArrayList();
        this.operations = FXCollections.observableArrayList();
    }

    //endregion

    //region Properties

    public void setIPAddress(String IPAddress) {
        this.IPAddress = IPAddress;
    }


    public void setPort(int port) {
        this.port = port;
    }


    public Socket getClientSocket() {
        return clientSocket;
    }


    public String getServerAnswer() {
        return serverAnswer;
    }

    public ObservableList<Active> getActives() { return actives; }

    public ObservableList<Operation> getOperations() { return operations; }

    //endregion

    //region Public methods

    public void startConnection(String clientMessage) {
        try {
            clientSocket = new Socket(IPAddress, port);

            ObjectOutputStream serverOut = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream serverIn = new ObjectInputStream(clientSocket.getInputStream());

            serverOut.writeObject(clientMessage);

            String[] words = clientMessage.split("/");

            switch (words[0]){
                case "viewActiveUser", "viewPortfolioUser", "viewActiveAdmin" -> {
                    ArrayList<Active> received = (ArrayList<Active>) serverIn.readObject();

                    for (Active active : received) actives.add((Active) active);
                }
                case "viewOperations" -> {
                    ArrayList<Operation> received = (ArrayList<Operation>) serverIn.readObject();

                    for (Operation operation : received) operations.add(operation);
                }

                default -> serverAnswer = (String) serverIn.readObject();
            }

            clientSocket.close();

            serverOut.close();
            serverIn.close();

        } catch (Exception ex) {
            System.out.printf("Exception: %s.\n", ex.getMessage());
        }
    }
    //endregion
}