package com.dlib;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Utils {

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
    con = Utils.connectToDB();

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

  // Function that gets the column titles from the database
  public static ArrayList<String> getTableColName(String tableName) {
    int colCount = 0;
    ArrayList<String> col = new ArrayList<String>();

    try {
      con = Utils.connectToDB();
      Statement stmt = con.createStatement();
      stmt.executeUpdate("USE library");

      ResultSet rs = stmt.executeQuery("select * from " + tableName);
      ResultSetMetaData mtd = rs.getMetaData();

      colCount = mtd.getColumnCount();

      for (int i = 1; i <= colCount; i++) {

        col.add(mtd.getColumnName(i));

      }
      // return col;
    } catch (Exception e) {
      System.out.println(e);
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
          return hasDB;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return hasDB;
  }

  // checks if a specific id inside a database table exists
  public static boolean doesIdExist(String tableName, String qry) {
    boolean status = false;
    con = connectToDB();
    try {
      Statement stmt = con.createStatement();
      stmt.executeUpdate("USE library");

      ResultSet rs = stmt.executeQuery(qry);
      if (rs.next() == true) {
        status = true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return status;
  }

  // updates data of issued books
  public static void IBUpdate(String sqBID, String sqIBID, String dateToday, JFrame frm) {

    con = Utils.connectToDB();
    try {
      Statement stmt = con.createStatement();
      stmt.executeUpdate("USE library");

      ResultSet rs = stmt
          .executeQuery("SELECT borrowPeriod,issuedDate,returnDate FROM issuedBooks where ib_id=" + sqIBID);

      if (rs.next()) {
        int duration = rs.getInt("borrowPeriod");
        String brwDateValue = rs.getString("issuedDate");
        String retDateValue = rs.getString("returnDate");
        if (retDateValue.equals("-")) {
          rs = stmt.executeQuery("SELECT quantity,issued FROM books where b_id=" + sqBID);

          if (rs.next()) {
            int quantCount = rs.getInt("quantity");
            int issueCount = rs.getInt("issued");
            if (issueCount != 0) {
              quantCount += 1;
              issueCount -= 1;
              stmt.executeUpdate(
                  "UPDATE books SET quantity=" + quantCount + ",issued=" + issueCount + " WHERE b_id=" + sqBID);
              stmt.executeUpdate("UPDATE issuedBooks SET returnDate='" + dateToday + "' WHERE ib_id=" + sqIBID);
            }
          } else {
            JOptionPane.showMessageDialog(frm, "Out of stock");
          }
        } else {
          JOptionPane.showMessageDialog(frm, "Book already returned");
        }
        String overdued = isOverdue(sqIBID, duration, brwDateValue, dateToday);
        stmt.executeUpdate("UPDATE issuedBooks SET overdued='" + overdued + "' WHERE ib_id=" + sqIBID);

      }

    } catch (Exception ex) {
      JOptionPane.showMessageDialog(frm, "Unable to issue book, try again");
      ex.printStackTrace();
    }
  }

  // checks if issued books is overdued
  public static String isOverdue(String ibid, int duration, String borrowDate, String returnDate) {
    LocalDate brwDate = LocalDate.parse(borrowDate, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    LocalDate retDate = LocalDate.parse(returnDate, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    Duration diff = Duration.between(brwDate.atStartOfDay(), retDate.atStartOfDay());
    int period = (int) diff.toDays();

    if (period <= duration) {
      return "NO";
    } else {
      return "YES";
    }
  }

}
