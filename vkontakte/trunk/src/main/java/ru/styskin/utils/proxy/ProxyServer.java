package ru.styskin.utils.proxy;

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProxyServer {
	
	private int port = 9999;
	private Thread thread;
	
	private ExecutorService executor = Executors.newCachedThreadPool();
	
	public static void main(String[] args) {
		new ProxyServer(9999).run();
	}
	
	public ProxyServer(int port) {
		this.port = port;
	}
	
	public void start() {
		thread.start();
	}	

	public void run() {
		try {
			ServerSocket socket = new ServerSocket(port);
			while(true) {
				executor.execute(new Proxy(socket.accept()));
			}
		} catch(Throwable ex) {
			ex.printStackTrace();
		}		
	}
}
