package chat7_Proj.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBConImpl implements DBCon {
	
	public Connection con;
	public PreparedStatement psmt;
	
	public DBConImpl() {
	}
	
	public DBConImpl(String user, String pass) {
		try {
			Class.forName(ORACLE_DRIVER);
			connect(user, pass);
		}
		catch(ClassNotFoundException e) {
			System.out.print("드라이버 로딩 실패:");
			e.printStackTrace();
		}
	}

	@Override
	public void connect(String user, String pass) {
		try {
			con = DriverManager.getConnection(ORACLE_URL, user, pass);
		}
		catch(SQLException e) {
			System.out.print("데이터베이스 연결 오류:");
			e.printStackTrace();
		}
	}

	@Override
	public void execute() {
	}

	@Override
	public void close() {
		try {
			if(con!=null) con.close();
			if(psmt!=null) psmt.close();
			System.out.println("자원 반납 완료");
		}
		catch(Exception e) {
			System.out.print("자원 반납 실패:");
			e.printStackTrace();
		}
	}
}
