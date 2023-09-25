package com.f1oating.messanger;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    private Socket socket = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;

    public Client(String address, int port){
        try{
            socket = new Socket(address, port);
            System.out.println("Соединение установлено");
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            Thread threadOut = new Thread(() -> {
                try{
                    String lineOut = "";

                    while(true){
                        lineOut = new BufferedReader(new InputStreamReader(System.in)).readLine();
                        out.writeUTF(lineOut);
                        if(lineOut.equals("Over")){
                            System.out.println("Вы завершили сессию");
                            closeTheConnection();
                            break;
                        }
                    }

                }catch(IOException e){
                    e.printStackTrace();
                }
            });

            Thread threadIn = new Thread(() -> {
                try{
                    String lineIn = "";

                    while(true){
                        lineIn = in.readUTF();
                        if(lineIn.equals("Over")){
                            System.out.println("Пользователь завершил сессию");
                            closeTheConnection();
                            break;
                        }
                        System.out.println(lineIn);
                    }
                }catch(IOException e){
                    e.printStackTrace();
                }
            });

            threadIn.start();
            threadOut.start();

        }catch(UnknownHostException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void closeTheConnection(){
        try{
            socket.close();
            in.close();
            out.close();
            System.exit(0);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String args[]){
        Client client = new Client("127.0.0.1", 2810);
    }

}
