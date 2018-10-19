package server;
//Mikkel, Micki og Tino

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Thread{

    private Socket s;
    private Server server;
    private DataInputStream dInput;
    private DataOutputStream dOutput;
    private boolean isRunning = true;
    private String clientName;

    public ClientHandler(Socket socket, Server server){
        this.s = socket;
        this.server = server;

    }

    //responsible for sending the client input out to every client in the client list from the serverclass.
    public void sendTextToAllClients(String text){
        for (int i = 0; i < server.getClientsConnected().size(); i++){
            ClientHandler ch = server.getClientsConnected().get(i);
            ch.sendTextToClient(text);
        }
    }

    //sends out each message to each individual client on the list (called from the method above).
    public void sendTextToClient(String text){
        try {
            dOutput.writeUTF(text);
            dOutput.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        try {
            dInput = new DataInputStream(s.getInputStream());
            dOutput = new DataOutputStream(s.getOutputStream());

            while(isRunning) {
                String textReceived = dInput.readUTF();

                //Sends a list to the client who typed 'list' with a list of all active clients on the server.
                if (textReceived.endsWith("list")){
                    ArrayList<String> names = new ArrayList<>();
                    for (ClientHandler client: server.getClientsConnected()) {
                        names.add(client.clientName);
                    }
                    sendTextToAllClients("Name of clients on the server: " + names);
                }
                //Closes down the client if the user types 'quit' and removes the client from the list of clients.
                //Fulfills the QUIT requirement and half of the LIST requirement (as it updates and tells
                //the other users everytime someone disconnects).
                if (textReceived.endsWith("quit")){
                    dOutput.writeUTF("Client " + clientName + " disconnected.");
                    dOutput.flush();
                    server.getClientsConnected().remove(this);
                    //Sends a message to all clients saying which user has disconnected.
                    sendTextToAllClients(this.clientName + " has disconnected.");

                    break;
                }

                sendTextToAllClients(textReceived);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setClientName(String clientNname) {
        this.clientName = clientNname;
    }

    public Socket getS() {
        return s;
    }
}
