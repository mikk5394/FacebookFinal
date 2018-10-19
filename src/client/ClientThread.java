package client;
//Mikkel, Micki og Tino

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientThread extends Thread {

    private Socket s;
    private DataInputStream dInput;
    private DataOutputStream dOutput;
    private boolean isRunning = true;
    private HeartBeat heartBeat;

    public ClientThread(Socket socket, Client client, HeartBeat heartBeat){

        this.s = socket;
    }

    public void sendTextToServer(String text){
        try{
            dOutput.writeUTF(text);
            dOutput.flush();
        } catch (IOException e){
            e.printStackTrace();
            closeEverything();
        }
    }

    public void run(){

        try {
            dInput = new DataInputStream(s.getInputStream());
            dOutput = new DataOutputStream(s.getOutputStream());
            while (isRunning) {
                try {
                    String reply = dInput.readUTF();

                    //closes down the thread if 'quit' is typed by the client and sets the heartbeat timer to a high
                    //amount.
                    if (reply.endsWith("quit")){
                        heartBeat.setTimer(10000000);
                        closeEverything();
                        break;
                    }

                    System.out.println(reply);
                } catch (IOException e) {
                    e.printStackTrace();
                    closeEverything();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            closeEverything();
        }
    }

    //a simple method that closes down everything if called.
    public void closeEverything (){
        try {
            dInput.close();
            dOutput.close();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
