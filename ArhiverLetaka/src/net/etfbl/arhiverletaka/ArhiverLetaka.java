/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.etfbl.arhiverletaka;

import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Office
 */
public class ArhiverLetaka {

    /**
     * @param args the command line arguments
     */
    public static final int TCP_PORT = 9000;
    public static void main(String[] args) {
            try {
            int clientCounter = 0;
           
            ServerSocket ss = new ServerSocket(TCP_PORT);
            System.out.println("Server running...");
            while (true) {
                
                Socket sock = ss.accept();

                System.out.println("Client accepted: "
                        + (++clientCounter));
               
                ServerThread st = new ServerThread(sock, clientCounter);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
}
