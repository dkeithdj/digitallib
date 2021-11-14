package com.dlib;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Utils {

  // Initializes connection to MySQL
  public static Connection connectToDB() {
    try {
      String DBurl = "jdbc:mysql://localhost:3306/mysql";
      String DBuser = "root";
      String DBpassword = "root";

      Class.forName("com.mysql.cj.jdbc.Driver");
      Connection con = DriverManager.getConnection(DBurl, DBuser, DBpassword);

      return con;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  
  // Function that gets the table's number of rows
  public static int getTableRowNum(String tableName) {
    Connection con = Utils.connectToDB();

     try {
       Statement stmt = con.createStatement();
       stmt.executeUpdate("USE library");
       ResultSet rs = stmt.executeQuery("select count(*) from " + tableName);
       rs.next();
       return rs.getInt(1);
     } catch (Exception e) {
      System.out.println(e);
     }
     return 0;
  }
  public static boolean hasDatabaseSetup() {
    boolean hasDB = false;
    try {
      Connection con = Utils.connectToDB();

      ResultSet rSet = con.getMetaData().getCatalogs();

      while (rSet.next()) {
        String DBName = rSet.getString(1);
        if (DBName.equals("library")) {
          hasDB = true;
          return hasDB;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return hasDB;
  }
}
