package chat7_Proj;

import java.net.Socket;
import java.util.Scanner;

public class MultiClient {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		System.out.println("이름을 입력하세요:");
		String name = scan.nextLine();
		
		//PrintWriter out = null; -- Sender가 기능을 가져가므로 쓰지 않음
		//BufferedReader in = null; -- Receiver가 기능을 가져가므로 쓰지 않음
		
		try {
			String ServerIP = "localhost";
			if(args.length > 0) {
				ServerIP = args[0];
			}
			Socket socket = new Socket(ServerIP, 9999);
			System.out.println("서버와 연결되었습니다.");
			
			//Receiver호출
			Thread receiver = new Receiver(socket);
			receiver.start();
			
			//Sender호출
			Thread sender = new Sender(socket, name);
			sender.start();
		}
		catch(Exception e) {
			System.out.println("예외발생[MultiClient]" + e);
		}
		
	}

}
