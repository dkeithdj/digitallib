package com.dlib;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;

import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
public class InitDB {

  public static void initializeDB() {
    try {
      Connection con = Utils.connectToDB();

      ResultSet rSet = con.getMetaData().getCatalogs();

      while (rSet.next()) {
        String DBName = rSet.getString(1);
        if (DBName.equals("library")) {
          Statement inStmt = con.createStatement();
          inStmt.executeQuery("DROP DATABASE library");
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
                  + "u_id INT NOT NULL,"
                  + "b_id INT NOT NULL,"
                  + "brwName VARCHAR(150) NOT NULL,"
                  + "issuedDate VARCHAR(50) NOT NULL,"
                  + "borrowPeriod INT NOT NULL,"
                  + "returnDate VARCHAR(50) NOT NULL)";

      String[] listQry = {qry1,qry2,qryB,qryIB,qryM};  
      for (int i = 0; i < listQry.length; i++) {
        inStmt.executeUpdate(listQry[i]);
      }
      rSet.close();
      AdminMenu.adminPage();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private static JFrame initFrm;

  public static void initializeConf() {
    initFrm = new JFrame("Initialize");

    JPanel initPnl = new JPanel(new MigLayout("fill", "", ""));
    JLabel initDesc = new JLabel("Press button to Create/Reset");
      JButton initButton = new JButton("IInitializenitialize");
      initButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          try {
            Runtime.getRuntime().exec("test.bat");
          } catch (IOException e1) {
            e1.printStackTrace();
          }
          // initFrm.dispose();
          // AdminMenu.adminPage();
          // InitDB.initializeDB();
 
            
        }
      });

      initPnl.add(initDesc, "wrap, center");
      initPnl.add(initButton, "center");
      initFrm.add(initPnl);
      initFrm.setSize(200, 100);
      initFrm.setVisible(true);
      initFrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
}
