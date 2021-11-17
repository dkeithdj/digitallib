package com.dlib;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

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

  public static boolean doesIdExist(String tableName, String qry, JFrame frm) {
    boolean status = false;
    Connection con = connectToDB();
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

  public static void IBUpdate(String sqBID, String sqIBID, String dateToday, JFrame frm) {

    Connection con = Utils.connectToDB();
    try {
      Statement stmt = con.createStatement();
      stmt.executeUpdate("USE library");

      ResultSet rs = stmt.executeQuery("SELECT returnDate FROM issuedBooks where ib_id="+sqIBID);

      if (rs.next() == true) {
        String retDateValue = rs.getString("returnDate");
        if (retDateValue.equals("-")) {
          rs = stmt.executeQuery("SELECT quantity,issued FROM books where b_id=" + sqBID);

          if (rs.next() == true) {
            int quantCount = rs.getInt("quantity");
            int issueCount = rs.getInt("issued");
            if (issueCount != 0) {
              quantCount+=1;
              issueCount-=1;
              stmt.executeUpdate("UPDATE books SET quantity="+quantCount+",issued="+issueCount+" WHERE b_id="+sqBID);
              stmt.executeUpdate("UPDATE issuedBooks SET returnDate='"+dateToday+"' WHERE ib_id="+sqIBID);
            }
          }else {
            JOptionPane.showMessageDialog(frm, "Out of stock");
          }
        } else {
          JOptionPane.showMessageDialog(frm, "Book already returned");
        }
      }

    } catch (Exception ex) {
      JOptionPane.showMessageDialog(frm, "Unable to issue book, try again");
      ex.printStackTrace();
    }
  }
}
