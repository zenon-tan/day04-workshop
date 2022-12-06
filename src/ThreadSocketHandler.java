import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ThreadSocketHandler extends Thread {

    private DataInputStream dis;
    private DataOutputStream dos;
    private Socket s;
    private String fileName;

    // Create a ThreadSocketHandler class to handle Multithread Server connections
    /*
     * The class is a subclass of Thread class and we will override the run method and replace it with
     * whatever we need
     */

    public ThreadSocketHandler(Socket s, DataInputStream dis, DataOutputStream dos, String fileName) {
        this.dis = dis;
        this.s = s;
        this.dos = dos;
        this.fileName = fileName;
    }

    // All 

    @Override
    public void run() {

        // Handle socket communication here
        // Identical setup as a single server connection

        while(true) {

            try {

                String fromClient = dis.readUTF();
                if(fromClient.equalsIgnoreCase("exit")) {
                    break;
                }

                System.out.println("LOG: Message from client: " + fromClient);

                if(fromClient.equalsIgnoreCase("get-cookie")) {
                    Cookie cookie = new Cookie(fileName);
                    String randomCookie = cookie.returnRandomCookie();

                    dos.writeUTF("cookie-text");
                    dos.writeUTF(randomCookie + " from Multithread: " + Thread.currentThread().getName() );
                    dos.flush();

                } else {
                System.out.println("Invalid command");
                    dos.writeUTF("From server: Invalid command");
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
}
