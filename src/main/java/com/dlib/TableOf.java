package com.dlib;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.ResultSet;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class TableOf {
  private String dbTable;
  private JTable tbl;
  private String[] col;
  private Connection con;
  private ArrayList<String> head;
  private ArrayList<String> colName;
  private ArrayList<String> outPut;
  private String id;
  private ArrayList<String> jtTxts;

  // Constructor
  // @param dbName: name of database
  // @param col: database column titles
  public TableOf(String dbName, String[] col) {
    this.dbTable = dbName;
    this.col = col;
    this.head = Utils.getTableColName(dbName);
    // this.headers = headers;
  }

  // Constructor for managing tables
  public TableOf(String dbName) {
    this(dbName, null);
  }

  // sets dbName to access the database table
  public void setDBName(String dbName) {
    this.dbTable = dbName;
  }
  public void getDbName() {
    System.out.println(this.dbTable);
  }

  // public ArrayList<String> getOutput() {
  // return this.outPut;
  // }

  // jcombobox uses it to display stuff when editing

  public ArrayList<String> setEdits(ArrayList<String> colName, String ID, String action) {
    this.id = ID;
    this.colName = colName;
    return this.setEdits(jtTxts, colName, ID, action);
  }

  public ArrayList<String> setEdits(ArrayList<String> jtTxt, ArrayList<String> colName) {
    this.jtTxts = jtTxt;
    this.colName = colName;
    return this.setEdits(jtTxt, colName, id, "insert");
  }

  public ArrayList<String> setEdits(ArrayList<String> jtTxt) {
    this.jtTxts = jtTxt;
    return this.setEdits(jtTxt, colName, id, "insert");
  }

  public ArrayList<String> setEdits(ArrayList<String> colName, String action) {
    this.colName = colName;
    return this.setEdits(jtTxts, colName, id, action);
  }

  public ArrayList<String> setEdits(String action) {
    return this.setEdits(jtTxts, colName, id, action);
  }

  public ArrayList<String> setEdits(ArrayList<String> jtTxt, ArrayList<String> colName, String action) {
    this.colName = colName;
    this.jtTxts = jtTxt;
    return this.setEdits(jtTxt, colName, id, action);
  }

  public ArrayList<String> setEdits(ArrayList<String> jtTxt, ArrayList<String> colName, String ID, String action) {
    this.colName = colName;
    this.id = ID;
    this.jtTxts = jtTxt;

    try {
      con = Utils.connectToDB();
      ResultSet rs = null;

      ArrayList<String> qryOut = new ArrayList<String>();

      String qry = formatQuery(action);
      PreparedStatement pstmt = con.prepareStatement(qry);
      System.out.println(qry);
      pstmt.executeUpdate("USE library");

      // outputs something
      if (action.equals("select")) {
        rs = pstmt.executeQuery(qry);
        if (rs.next()) {
          for (int i = 0; i < colName.size(); i++) {
            qryOut.add(rs.getString(colName.get(i)));
            this.outPut = qryOut;
          }
        }
        // self explanatory
      }
      // else if (action.equals("delete")) {
      // pstmt.setString(1, ID);
      // // inserts or updates stuff to the database
      // }
      else {
        for (int i = 0; i < jtTxt.size(); i++) {
          if (jtTxt.get(i).isBlank() || jtTxt == null) {
            return null;
          }
          pstmt.setString(i + 1, jtTxt.get(i));
        }
      }
      pstmt.execute();

      con.close();
      return qryOut;
    } catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }

  public boolean deleteRow(String ID) {
    try {
      con = Utils.connectToDB();

      String qry = "DELETE FROM " + dbTable + " WHERE " + head.get(0) + "=?";

      PreparedStatement pstmt = con.prepareStatement(qry);
      System.out.println(qry);
      pstmt.executeUpdate("USE library");
      pstmt.setString(1, ID);

      pstmt.executeUpdate();

      System.out.println(ID);

      return true;

    } catch (Exception e) {
      // TODO: handle exception
      e.printStackTrace();
      return false;
    }

  }

  public ArrayList<String> selectRow(String ID) {
    try {
      con = Utils.connectToDB();
      ResultSet rs = null;

      ArrayList<String> qryOut = new ArrayList<String>();

      String qry = "SELECT * FROM " + dbTable + " WHERE " + head.get(0) + "=" + ID;

      PreparedStatement pstmt = con.prepareStatement(qry);
      System.out.println(qry);
      pstmt.executeUpdate("USE library");
      rs = pstmt.executeQuery(qry);
      int i = 0;
      while (rs.next()) {
        // for (int i = 0; i < colName.size(); i++) {
        qryOut.add(rs.getString(head.get(i)));
        System.out.println(qryOut);
        i++;
        // }
      }

      // pstmt.executeUpdate();

      System.out.println(ID);

      return qryOut;
    } catch (Exception e) {
      // TODO: handle exception
      e.printStackTrace();
      return null;
    }

  }

  private String formatQuery(String e) {
    // colName.add(0, head.get(0));
    // head.remove(0);
    // String base = head.toString();

    if (e.equals("update")) {
      String base = colName.toString();
      base = base.replace("[", "").replace("]", "=?").replace(" ", "").replace(",", "=?,");
      String fmt = "UPDATE " + dbTable + " SET " + base + " WHERE " + head.get(0) + "=" + id;
      return fmt;

    }
    if (e.equals("insert")) {
      String base = colName.toString();
      base = base.replace("[", "(").replace("]", ")").replace(" ", "");
      String fmt = base.replaceAll("(\\w+)", "?");
      String qry = "INSERT INTO " + dbTable + base + " VALUES" + fmt;
      return qry;
    }
    // if (e.equals("delete")) {
    // String qry = "DELETE FROM " + dbTable + " WHERE " + head.get(0) + "=?";
    // return qry;
    // }
    if (e.equals("select")) {
      String qry = "SELECT * FROM " + dbTable + " WHERE " + head.get(0) + "=" + id;
      return qry;
    }
    return "";

  }

  public ArrayList<String> getTableIDs() {
    ArrayList<String> IDs = new ArrayList<String>();
    try {
      Connection con = Utils.connectToDB();

      String qry = "SELECT " + head.get(0) + " FROM " + dbTable;

      Statement pstmt = con.createStatement();
      pstmt.executeUpdate("USE library");

      ResultSet rs = pstmt.executeQuery(qry);

      while (rs.next()) {
        IDs.add(rs.getString(head.get(0)));
      }

      return IDs;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  // setups the model for the JTable
  public DefaultTableModel setupTable() {
    try {
      con = Utils.connectToDB();
      Statement stmt = con.createStatement();
      stmt.executeUpdate("USE library");
      ResultSet rs = stmt.executeQuery("select * from " + dbTable);

      Object[] headers = head.toArray();

      String data[][] = new String[Utils.getTableRowNum(dbTable)][col.length];

      int i = 0;
      while (rs.next()) {
        for (int x = 0; x < headers.length; x++) {
          data[i][x] = rs.getString(headers[x].toString());
        }
        i++;
      }

      con.close();

      return new DefaultTableModel(data, col) {
        @Override
        public boolean isCellEditable(int row, int column) {
          // all cells false
          return false;
        }
      };

      // return model;
    } catch (Exception e) {
      System.out.println(e);
    }
    return null;
  }

  // Configure JTable's features?
  public JTable getTable() {
    tbl = new JTable();

    tbl.setModel(setupTable());
    tbl.getTableHeader().setReorderingAllowed(false);
    tbl.setAutoCreateRowSorter(true);
    tbl.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    return tbl;
  }

}
