package client;
//Mikkel, Micki og Tino
//A class responsible for sending a message in every client thread every 1 min. Fulfills the IMAV requirement.
public class HeartBeat extends Thread {

    private int timer;

    public HeartBeat (int timer){

        this.timer = timer;
    }

    public void run (){
        try {
            while (true){
                Thread.sleep(timer);
                System.out.println("I am alive");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //I wasn't sure how the stop the thread entirely so part of the 'quit' method in the ClientThread class
    //sets the timer to 10000000 milliseconds which atleast stops it for a while.. :-)
    public void setTimer(int timer) {
        this.timer = timer;
    }
}
