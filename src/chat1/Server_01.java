package chat1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server_01 {

	public static void main(String[] args) {
		//1. 클라이언트의 요청을 받기 위한 준비를 한다(ServerSocket)
		ServerSocket serverSocket = null;
		Socket socket = null;
		BufferedReader in = null;//클라이언트의 요청을 읽을 리더클래스
		PrintWriter out = null;//클라이언트에게 메세지를 보낼 출력클래스
		String s = "";//클라이언트의 메세지를 담을 스트링클래스
		
		
		//클라이언트가 서버에 접속 요청을 한다.
		
		//2. 클라이언트의 요청을 받아들인다.
		try {
			serverSocket = new ServerSocket(9999);
			System.out.println("서버가 시작되었습니다.");
			
			socket = serverSocket.accept();//접속을승인하고
			System.out.println(socket.getInetAddress() + ":" +
					socket.getPort());//클라이언트의 ip와 포트번호를 출력한다.
			
			//클라이언트로부터 메세지를 받기 위한 스트림 생성
			in = new BufferedReader(new 
					InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			
			//클라이언트가 보낸 메세지를 라인 단위로 읽어옴
			s = in.readLine();
			
			System.out.println("클라이언트에서 읽어옴:" + s);
			
			//클라이언트로 Echo
			out.println("server에서 응답:" + s);
			
			//콘솔에 종료메세지 출력
			System.out.println("Good bye, See you again");
		}
		catch(Exception e) {
			System.out.print("예외발생:");
			e.printStackTrace();
		}
		finally {
			try {
				in.close();
				out.close();
				socket.close();
				serverSocket.close();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
