import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class SocketApp {

    // To run:
    // cd src/day04-workshop/src
    // javac SocketApp.java
    // For Server:
    // java SocketApp server 12345 cookie_file.txt
    // For Client:
    // java SocketApp client localhost 12345

    public static void main(String[] args) {

        // command line arguments are space separated (argnet)
        // Whatever is after the filename are argnets
        // These don't run using the play button, it requires input with the terminal

        if (args.length < 3) {
            System.out.println("""

                    Incorrect inputs, please check the following usage:
                    ================
                    > Usage: Server
                    <program> <'server'> <port> <filename>
                    ================
                    > Usage: Multithread Server
                    <program> <'thread.server'> <port> <filename>
                    ================
                    > Usage: Client
                    <program> <'client'> <host> <port>
                    ================

                    """);
            return;
        }

        String type = args[0];
        if (type.equalsIgnoreCase("server")) {
            int port = Integer.parseInt(args[1]);
            String fileName = args[2];
            startServer(port, fileName);

            // Do somrthing
        } else if (type.equalsIgnoreCase("client")) {
            String hostName = args[1];
            int port = Integer.parseInt(args[2]);
            startClient(hostName, port);

        } else if(type.equalsIgnoreCase("thread.server")) {
            int port = Integer.parseInt(args[1]);
            String fileName = args[2];
            startMultiThreadServer(port, fileName);
        }
         else {
            System.out.println("Incorrect argnets");
        }

        // Usage: Server
        // <program> <server> <port> <filename>

    }

    public static void startServer(int port, String fileName) {

        ServerSocket server;

        try {
            server = new ServerSocket(port);
            Socket socket = server.accept();

            // INPUT STRING
            DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            // OUTPUT STRING
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

            while (true) {
                String fromClient = dis.readUTF();
                if (fromClient.equalsIgnoreCase("get-cookie")) {
                    Cookie cookie = new Cookie(fileName);
                    String randomCookie = cookie.returnRandomCookie();
                    
                    // Send "cookie-text" to client
                    dos.writeUTF("cookie-text");
                    dos.writeUTF(randomCookie);
                    dos.flush();
                } else if (fromClient.equalsIgnoreCase("exit")) {
                    break;
                } else {
                    System.out.println("Invalid command");
                    dos.writeUTF("From server: Invalid command");
                    dos.flush();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void startMultiThreadServer(int port, String fileName) {

        ServerSocket server;

        try {

            server = new ServerSocket(port);
            while(true) {

                Socket socket = server.accept();
                // INPUT STRING
                DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
    
                // OUTPUT STRING
                DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

                Thread tsh = new ThreadSocketHandler(socket, dis, dos, fileName);
                tsh.start();

            }



        } catch (IOException e) {
            e.getStackTrace();
        }

    }

    // Usage: Client
    // <program> <client> <host> <port>

    public static void startClient(String host, int port) {

        try {
            Socket socket = new Socket(host, port);

            // INPUT STRING
            DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            // OUTPUT STRING
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

            // Scanner
            Scanner sc = new Scanner(System.in);
            boolean stop = false;

            while (!stop) {
                String line = sc.nextLine();
                if (line.equalsIgnoreCase("exit")) {
                    dos.writeUTF("exit");
                    dos.flush();
                    stop = true;
                    break;
                }

                if (line.equalsIgnoreCase("get-cookie")) {
                    // Send a request to the server for a cookie
                    dos.writeUTF("get-cookie");
                    dos.flush();

                    // Receiving from server
                    String fromServer = dis.readUTF();
                    //System.out.println("Response from server: " + fromServer);

                    if(fromServer.equalsIgnoreCase("cookie-text")) {
                        String randomCookie = dis.readUTF();
                        System.out.println(randomCookie);
    
    
                    }
                }

                else {
                    System.out.println("Invalid command: " + line);

                }

            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

}
