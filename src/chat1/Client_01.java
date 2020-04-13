package chat1;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Scanner;

public class Client_01 {

	public static void main(String[] args) {
		//서버에 접속하기 위한 준비를 한다.
		Scanner scanner = new Scanner(System.in);
		System.out.print("이름을 입력하세요:");
		String name = scanner.nextLine();
		PrintWriter out = null;//내 메세지를 서버에 보냄
		BufferedReader in = null;//서버로부터 메세지를 읽어옴.
		
		try {
			//별도의 매개변수가 없다면 접속자의 ip는 localhost로 고정
			String serverIP = "localhost";
			
			//클라이언트 실행시 매개변수가 있는 경우 아이피로 설정함
			if(args.length > 0) {
				serverIP = args[0];
			}
		}
		catch(Exception e) {
			System.out.print("예외발생:");
			e.printStackTrace();
		}
	}

}
