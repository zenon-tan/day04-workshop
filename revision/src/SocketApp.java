package revision.src;

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
        // command line arguments
        // The Program will only run with the terminal

        // We need 3 arguments after running the program:
        // <server or client> <host/port> <filename/port>
        
        if(args.length < 3) {
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

        switch(type.toLowerCase().trim()) {
                case "server":
                int port = Integer.parseInt(args[1]);
                String fileName = args[2];
                startServer(port, fileName);
                break;
            case "client":
                String hostName = args[1];
                int port2 = Integer.parseInt(args[2]);
                startClient(hostName, port2);
                break;
            case "thread.server":
                break;
            default:
                System.out.println("Incorrect argnets");
                break;
        }

    }

    public static void startServer(int port, String filename) {

        ServerSocket server;

        // Initalises a server and create input and output streams
        try {

            server = new ServerSocket(port);
            Socket socket = server.accept();

            //INPUT STREAM
            DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            //OUTPUT STREAM
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

            while(true) {
                // Read the first line
                String fromClient = dis.readUTF();
                if(fromClient.equalsIgnoreCase("get-cookie")) {
                    // Generate cookie instance and return a random cookie
                    Cookie cookie = new Cookie(filename);
                    // Send cookie-text to client
                    dos.writeUTF("cookie-text");
                    // Send the random cookie string to client
                    String randomCookie = cookie.getRandomCookie();
                    dos.writeUTF(randomCookie);

                } else if(fromClient.equalsIgnoreCase("exit")) {
                    break;
                } else {
                    System.out.println("Invalid command");
                    dos.writeUTF("From server: Invalid command");
                    dos.flush();
                }
                
            }
            dos.close();
            dis.close();
                
                
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void startClient(String host, int port) {

        try {

            Socket socket = new Socket(host, port);

            //INPUT STREAM
            DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            //OUTPUT STREAM
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

            // Get input from terminal
            Scanner sc = new Scanner(System.in);

            while(true) {
                String line = sc.nextLine();
                if(line.equalsIgnoreCase("exit")) {
                    dos.writeUTF("exit");
                    dos.flush();
                    break;

                } else if(line.equalsIgnoreCase("get-cookie")) {
                    // Send "get-cookie" to server
                    dos.writeUTF("get-cookie");
                    dos.flush();

                    // Recieve "cookie-text" and random cookie from server
                    String fromServer = dis.readUTF();

                    if(fromServer.equalsIgnoreCase("cookie-text")) {
                        String randomCookie = dis.readUTF();
                        System.out.println(randomCookie);
                    }



                } else {
                    System.out.println("Invalid command: " + line);
                    dis.close();
                    dos.close();
                }
                
            }

            
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
