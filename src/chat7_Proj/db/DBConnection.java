package chat7_Proj.db;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public class DBConnection {
	
	Connection con;
	Statement psmt;
	Map<String, PrintWriter> clientMap;
	
	public DBConnection(Map<String, PrintWriter> clientMap) {
		this.clientMap=clientMap;
	}
	
	private void execute() {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			String url = "jdbc:oracle:thin://@localhost:1521:orcl";
			String userid = "kosmo";
			String userpw = "1234";
			Connection con = DriverManager.getConnection(url,
					userid, userpw);
			
			System.out.println("오라클 DB 연결성공");
		}
		catch(Exception e) {
			System.out.println("예외발생>>생성자:" + e);
		}
		
		try {
			String sql = "INSERT INTO chating_tb VALUES"
						+ "(?, ?, ?, ?)";
			psmt = con.prepareStatement(sql);
			
			//map으로 불러온 name하고 대화내용 인덱스 지정해주는 방법?
			
			
			int affected = psmt.executeUpdate(sql);
			System.out.println(affected + "행이 입력되었습니다.");
		}
		catch(SQLException e) {
			System.out.println("예외발생>>execute:" + e);
		}
		finally {
			close();
		}
	}
	
	private void close() {
		try {
			if(psmt!=null) psmt.close();
			if(con!=null) con.close();
			System.out.println("DB자원반납 완료");
		}
		catch(SQLException e) {
			System.out.println("예외발생>>close:" + e);
		}
	}

	public static void main(String[] args) {
		try {
			
		}
		catch(Exception e) {
			System.out.println("실행실패:" + e);
		}
	}
}
