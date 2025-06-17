import java.util.*;
import java.net.*;
import java.io.*;


public class Server implements Runnable {

    private Socket sc;
    private static HashMap<String, DataOutputStream> client = new HashMap<>(); //to map each client 
    private String userName;
    
    public Server(Socket sc){
        this.sc= sc;
    }

    @Override
    public void run(){

        try{
            DataInputStream in = new DataInputStream(sc.getInputStream());
            DataOutputStream out = new DataOutputStream(sc.getOutputStream());

            //read username
            userName = in.readUTF();
            synchronized(client){
                client.put(userName, out);

                //to broadcast the updated client list
                StringBuilder sb = new StringBuilder("USERLIST:");
                for (String user : client.keySet()) {
                    sb.append(user).append(",");
                }

                String finalList = sb.toString();
                for (DataOutputStream dos : client.values()) {
                    dos.writeUTF(finalList);
                }
            }

            System.out.println(userName + " Connected");

            //to broadcast the message
            while (true) {
                String data = in.readUTF();

                if(!data.startsWith("@")) continue;
                int index = data.indexOf(":");
                if(index == -1) continue;

                String rcvr = data.substring(1, index).trim(); //extract receiver's name
                String msg = data.substring(index + 1).trim(); //extract message

                DataOutputStream rcvrOut;
                synchronized(client){
                    rcvrOut = client.get(rcvr);
                }

                if(rcvrOut != null){
                    rcvrOut.writeUTF(userName + ":" + msg);
                }else{
                    out.writeUTF("User not found!");
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try{
            ServerSocket skt = new ServerSocket(5500);
            System.out.println("Server has started!");

            while(true){
                Socket clientsc = skt.accept();
                Server s = new Server(clientsc);
                Thread thread = new Thread(s);
                thread.start();
            }

        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
