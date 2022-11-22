package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

  private Connection con;
  private Statement stmt;

  public DBConnection() {
    try {
      String url =
        "jdbc:mysql://localhost:3306/?characterEncoding=UTF-8&serverTimezone=UTC";
      String user = "개인 mysql id";
      String passwd = "개인 mysql 비밀번호";

      con = DriverManager.getConnection(url, user, passwd);
      System.out.println("DB연결 성공");

      stmt = con.createStatement();
      System.out.println("Statement객체 생성 성공");
    } catch (SQLException e) {
      System.out.println("DB연결 실패");
      System.out.print("사유 : " + e.getMessage());
    }
  }

  public ResultSet selectData(String query) {
    try {
      ResultSet result = stmt.executeQuery(query);
      return result;
    } catch (SQLException e) {
      System.out.println("DB 쿼리 실행 실패");
      System.out.print("사유 : " + e.getMessage());
      return null;
    }
  }

  public int insertUpdateDeleteData(String query) {
    try {
      int result = stmt.executeUpdate(query);
      // String sql =
      //   "INSERT INTO `simple_calendar`.`user` (`user_id`, `username`, `password`, `email`) VALUES (3, '굳굳굳', '@wldls014', 'abc2@naver.com');";
      System.out.println("쿼리문 실행 결과: " + result);
      // stmt.close();
      // con.close();
      return result;
    } catch (SQLException e) {
      System.out.println("DB 쿼리 실행 실패");
      System.out.print("사유 : " + e.getMessage());
      return -1;
    }
  }

  public static void main(String[] args) {
    new DBConnection();
  }
}
