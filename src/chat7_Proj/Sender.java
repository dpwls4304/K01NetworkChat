package chat7_Proj;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

//클라이언트 창에 메세지를 출력
public class Sender extends Thread{
	Socket socket;
	PrintWriter out = null;
	String name;
	
	public Sender(Socket socket, String name) {
		this.socket = socket;
		try {
			out = new PrintWriter(this.socket.getOutputStream(), true);
			this.name=name;
		}
		catch(Exception e) {
			System.out.println("예외>Sender>생성자:" + e);
		}
	}
	
	@Override
	public void run() {
		Scanner scan = new Scanner(System.in);
		//run2
		try {
			out.println(name);
			
			//run1
			while(out != null) {
				try {
					String s = scan.nextLine();
					if(s.equals("Q") || s.equals("q")) {
						break;
					}
					else {
						out.println(s);
					}
				}
				catch(Exception e) {
					System.out.println("예외>Sender>run1:" + e);
				}
			}
			
			//Q를 입력하면 자원 닫음
			out.close();
			socket.close();
		}
		catch(Exception e) {
			System.out.println("예외>Sender>run2" + e);
		}
	}
}
