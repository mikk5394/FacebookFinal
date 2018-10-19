package client;
//Mikkel, Micki og Tino

import server.Server;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private ClientThread ct;
    private final int PORT = 9995;
    private String clientName;

    public static void main(String[] args){

        new Client();
    }

    public Client(){
        try {
            //Makes sure there can be no duplicate usernames, fulfills the J_ER requirement
            System.out.println("Type a username:");
            Scanner sc = new Scanner(System.in);
            String username;
            while (true) {
                String input = sc.nextLine();
                username = input;
                // Username can be max 12 chars long and can only contain letters, digits, ‘-‘ and ‘_’ allowed.
                if (Server.getClientNames().contains(username)) {
                    System.out.println("Name is taken, try something else.");
                } else if ((!username.matches("^[a-zA-Z\\d-_]{0,12}$"))){
                    System.out.println("Username is max 12 chars long, only letters, digits, ‘-‘ and ‘_’ allowed.");
                    System.out.println("Type a new username:");
                } else {
                    this.clientName = username;
                    break;
                }
            }
            System.out.println("Welcome " + username + ".");

            Socket socket = new Socket("localhost",PORT);
            HeartBeat hb = new HeartBeat(60000);
            ct = new ClientThread(socket,this, hb);

            hb.start();
            ct.start();

            String port = Integer.toString(PORT);
            ct.sendTextToServer("JOIN " + username + ", " + "localhost:" + port);

            waitForInput();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //calls a method in the ClientThread class which is responsible for sending out the input from the client
    public void waitForInput() {

        Scanner clientInput = new Scanner(System.in);
        //Making an infinite loop to make it possible for the user to keep sending input
        do {
                String input = clientInput.nextLine();

                ct.sendTextToServer(clientName + ": " + input);

        } while (true);
    }
}