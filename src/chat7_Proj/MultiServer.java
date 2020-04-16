package chat7_Proj;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import chat7_Proj.db.DBInsert;

public class MultiServer {
	static ServerSocket serverSocket = null;
	static Socket socket = null;
	Map<String, PrintWriter> clientMap;
	DBInsert db;
	
	public MultiServer() {
		clientMap = new HashMap<String, PrintWriter>();
		Collections.synchronizedMap(clientMap);//동기화설정, 쓰레드의 동시접근 차단
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
					itr_out.println(URLEncoder.encode(msg, "UTF-8"));
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
				out = new PrintWriter(this.socket.getOutputStream(),true);
				in = new BufferedReader(new InputStreamReader(
										this.socket.getInputStream(), "UTF-8"));
			}
			catch(Exception e) {
				System.out.println("예외발생>>멀티서버T:" + e);
			}
		}
		
		@Override
		public void run() {
			String name = "";
			String s = "";
			
			try {
				name = in.readLine();//이름읽음
				name = URLDecoder.decode(name, "UTF-8");
				sendtoAll("", name + "님이 입장하셨습니다.");
				clientMap.put(name, out);
				
				System.out.println(name + "접속");
				System.out.println("현재 접속자 수는" +
				clientMap.size() + "명 입니다.");
				
				while(in!=null) {
					System.out.println(">>");
					s = in.readLine();//채팅내용읽음
					s = URLDecoder.decode(s, "UTF-8");
					if(s==null)
						break;
					
					System.out.println(name + ">>" + s);
					sendtoAll(name, s);
					
					db = new DBInsert(name,s);//------객체 생성, 값 삽입
					db.execute();//------------인서트문 접속------------
					
					//--------------접속자명단 /list---------------//
					if(s.equals("/list")) {
						Iterator<String> itr = clientMap.keySet().iterator();
						String sys1 = "현재 접속중인 사람은";
						String sys2 = "입니다.";
						
						System.out.println(sys1);
						out.println(sys1);
						while(itr.hasNext()) {
							String key = itr.next();
							System.out.println(key);
							out.println(key);
						}
						System.out.println(sys2);
						out.println(sys2);
					}
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
					//**DBDelete();
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
