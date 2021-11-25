package com.dlib;

import java.sql.Connection;
import java.sql.ResultSet;

import java.sql.Statement;

public class InitDB {

  public static void initializeDB() {
    try {
      Connection con = Utils.connectToDB();

      ResultSet rSet = con.getMetaData().getCatalogs();

      String libraryDB = "library";
      while (rSet.next()) {
        String DBName = rSet.getString(1);
        if (libraryDB.equals(DBName)) {
          Statement inStmt = con.createStatement();
          inStmt.executeUpdate("DROP DATABASE library");
        }
      }
      Statement inStmt = con.createStatement();
      String qry1 = "CREATE DATABASE library";
      String qry2 = "USE library";
      String qryM = "CREATE TABLE members("
                  + "m_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                  + "firstName VARCHAR(150) NOT NULL,"
                  + "lastName VARCHAR(150) NOT NULL,"
                  + "address VARCHAR(150) NOT NULL,"
                  + "contact VARCHAR(100) NOT NULL)";
      String qryB = "CREATE TABLE books("
                  + "b_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                  + "title VARCHAR(100) NOT NULL,"
                  + "author VARCHAR(100) NOT NULL,"
                  + "genre VARCHAR(100) NOT NULL,"
                  + "quantity INT NOT NULL,"
                  + "issued INT NOT NULL,"
                  + "publishYear VARCHAR(50) NOT NULL)";
      String qryIB = "CREATE TABLE issuedBooks("
                  + "ib_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                  + "m_id INT NOT NULL,"
                  + "b_id INT NOT NULL,"
                  + "brwrLName VARCHAR(50) NOT NULL,"
                  + "bookTitle VARCHAR(50) NOT NULL,"
                  + "issuedDate VARCHAR(50) NOT NULL,"
                  + "borrowPeriod INT NOT NULL,"
                  + "returnDate VARCHAR(50) NOT NULL,"
                  + "overdued VARCHAR(50) NOT NULL)";

      String[] listQry = {qry1,qry2,qryB,qryIB,qryM};  
      for (int i = 0; i < listQry.length; i++) {
        inStmt.executeUpdate(listQry[i]);
      }
      rSet.close();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public static void redirectToAdPage() {
    InitDB.initializeDB();
    AdminMenu.adminPage();
  }

}
