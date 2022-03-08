package com.dlib;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;

public class Utils {

  // generally, the process of interacting with the database are as follows:

  // Connection con=Utils.connectToDB(); <-- this initializes a connection to the
  // database

  // Statement is to create a statement shown below
  // Statement stmt = con.createStatement();

  // executeUpdate gets a string that commands the database to either select,
  // insert, delete as well as other commands (SQL - Structured Query Language)
  // NOTE: capitalization is just for readability
  // stmt.executeUpdate("USE library"); <-- can be "SELECT m_id from members" or
  // "INSERT INTO firstName='Raiden' WHERE m_id=3"

  // ResultSet fetches the data from the database to be used in the code
  // ResultSet rs = stmt.executeQuery("select count(*) from " +
  // tableName);

  // rs.next();

  // usually SELECT commands gets a result from the database
  // whereas INSERT and DELETE sets the database based on your inputs
  // SELECT --> in
  // INSERT, DELETE --> out

  private static Connection con;

  // Initializes connection to MySQL
  public static Connection connectToDB() {
    try {
      String DBurl = "jdbc:mysql://localhost:3306/mysql";
      String DBuser = "root";
      String DBpassword = "root";

      Class.forName("com.mysql.cj.jdbc.Driver");
      con = DriverManager.getConnection(DBurl, DBuser, DBpassword);

      return con;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  // Function that gets the number of rows in the table
  public static int getTableRowNum(String tableName) {

    try {
      con = Utils.connectToDB();
      Statement stmt = con.createStatement();
      stmt.executeUpdate("USE library");
      ResultSet rs = stmt.executeQuery("select count(*) from " + tableName);
      rs.next();
      return rs.getInt(1);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 0;
  }

  // Function that gets the column titles from the database
  public static ArrayList<String> getTableColName(String tableName) {
    int colCount = 0;
    ArrayList<String> col = new ArrayList<String>();

    try {
      con = Utils.connectToDB();
      Statement stmt = con.createStatement();
      stmt.executeUpdate("USE library");

      ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
      ResultSetMetaData mtd = rs.getMetaData();

      colCount = mtd.getColumnCount();

      for (int i = 1; i <= colCount; i++) {

        col.add(mtd.getColumnName(i));

      }
      // return col;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return col;
  }

  // checks if a database exist
  public static boolean hasDatabaseSetup() {
    con = Utils.connectToDB();
    boolean hasDB = false;
    try {

      ResultSet rSet = con.getMetaData().getCatalogs();

      while (rSet.next()) {
        String DBName = rSet.getString(1);
        if (DBName.equals("library")) {
          hasDB = true;
        }
      }
      return hasDB;
    } catch (Exception e) {
      e.printStackTrace();
      return hasDB;
    }
  }
}
