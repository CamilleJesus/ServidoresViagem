package util;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import controller.Controller;

public class ConexaoTCP extends Thread{
	
	private ServerSocket sskt;
	private Socket skt;
	private int porta;
	private Controller controller;
	
	public ConexaoTCP(int porta, Controller controller) {
		this.porta = porta;
		this.controller = controller;
		
	}
	
	public void run() {

        try {
            sskt = new ServerSocket(porta);
            System.out.println("criou a thread");
            while (true) {
                skt = sskt.accept();
                new AtendenteTCP(skt, this, controller).start();

            }
        } catch (IOException ex) {
            System.out.println("Deu rum em criar o socket, tentou criar de novo");
        }
    }

}