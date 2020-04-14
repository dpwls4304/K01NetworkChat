package chat7_Proj;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MultiServer {
	static ServerSocket serverSocket = null;
	static Socket socket = null;
	Map<String, PrintWriter> clientMap;
	
	public MultiServer() {
		clientMap = new HashMap<String, PrintWriter>();
		Collections.synchronizedMap(clientMap);//동기화설정, 쓰레드의 동시접근 차단
		try {
			db(clientMap);
		}
		catch(Exception e) {
			System.out.println("DB연동실패:" + e);
		}
	}
	
	public void db(Map<String, PrintWriter> clientMap)
			throws ClassNotFoundException, SQLException{
		Class.forName("oracle.jdbc.OracleDriver");
	}
	
	public void init() {
		try {
			serverSocket = new ServerSocket(9999);
			System.out.println("서버가 시작되었습니다.");
			
			while(true) {
				socket = serverSocket.accept();
				Thread mst = new MultiServerT(socket);
				mst.start();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				serverSocket.close();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		MultiServer ms = new MultiServer();
		ms.init();
	}
	
	//접속한 모든 클라이언트에게 메세지 전달하는 메소드
	public void sendtoAll(String name, String msg) {
		Iterator<String> itr = clientMap.keySet().iterator();
		
		while(itr.hasNext()) {
			try {
				PrintWriter itr_out = (PrintWriter)clientMap.get(itr.next());
				if(name.equals("")) {
					itr_out.println(msg);
				}
				else {
					itr_out.println("[" + name + "]:" + msg);
				}
			}
			catch(Exception e) {
				System.out.println("예외:" + e);
			}
		}
	}
	
	//내부클래스 생성
	class MultiServerT extends Thread {
		
		Socket socket;
		PrintWriter out = null;
		BufferedReader in = null;
		
		public MultiServerT(Socket socket) {
			this.socket = socket;
			try {
				out = new PrintWriter(this.socket.getOutputStream(),
						true);
				in = new BufferedReader(new InputStreamReader(
						this.socket.getInputStream()));
			}
			catch(Exception e) {
				System.out.println("예외발생:" + e);
			}
		}
		
		@Override
		public void run() {
			String name = "";
			String s = "";
			
			try {
				name = in.readLine();
				sendtoAll("", name + "님이 입장하셨습니다.");
				clientMap.put(name, out);
				
				System.out.println(name + "접속");
				System.out.println("현재 접속자 수는" +
				clientMap.size() + "명 입니다.");
				
				while(in!=null) {
					System.out.println(">>");
					s = in.readLine();
					if(s==null)
						break;
					
					System.out.println(name + ">>" + s);//-----------//
					sendtoAll(name, s);
				}
			}
			catch(Exception e) {
				System.out.println("예외발생:" + e);
			}
			finally {
				clientMap.remove(name);
				sendtoAll("", name + "님이 퇴장하셨습니다.");
				
				System.out.println(name + "[" +
						Thread.currentThread().getName() + "]퇴장");
				System.out.println("현재 접속자 수는" + clientMap.size()+ "명 입니다.");
				
				try {
					in.close();
					out.close();
					socket.close();
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
