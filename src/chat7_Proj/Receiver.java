package chat7_Proj;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.net.URLDecoder;

//클라이언트가 작성한 메세지를 읽음
public class Receiver extends Thread{
	Socket socket;
	BufferedReader in = null;
	
	public Receiver(Socket socket) {
		this.socket = socket;
		try {
			in = new BufferedReader(new InputStreamReader
									(this.socket.getInputStream(), "UTF-8"));
		}
		catch(Exception e) {
			System.out.println("예외>Receiver>생성자:" + e);
		}
	}
	
	@Override
	public void run() {
		while(in!=null) {
			try {
				System.out.println("Thread Receive:" + URLDecoder.decode(in.readLine(), "UTF-8"));
			}
			catch(SocketException se) {
				System.out.println("SocketException");
				break;
			}
			catch(Exception e) {
				System.out.println("예외>Receiver>run1:" + e);
			}
		}
		try {
			in.close();
		}
		catch(Exception e) {
			System.out.println("예외>Receiver>run2:" + e);
		}
	}
}
