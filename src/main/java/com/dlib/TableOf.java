package com.dlib;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.sql.ResultSet;

import javax.swing.JTable;
import javax.swing.JTextField;
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
  private ArrayList<String> status;

  // Constructor
  // @param dbName: name of database
  // @param col: database column titles
  public TableOf(String dbName, String[] col) {
    this.dbTable = dbName;
    this.col = col;
    this.head = Utils.getTableColName(dbName);
    // this.headers = headers;
  }

  // Constructor (might remove)
  public TableOf(String dbName) {
    this(dbName, null);
  }

  public ArrayList<String> getOutput() {
    return outPut;
  }

  // jcombobox uses it to display stuff when editing

  public boolean setEdits(ArrayList<String> colName, String ID, String action) {
    this.colName = colName;
    return setEdits(null, colName, ID, action);
  }

  public boolean setEdits(ArrayList<String> jtTxt, ArrayList<String> colName, String action) {
    this.colName = colName;
    return setEdits(jtTxt, colName, id, action);
  }

  public boolean setEdits(ArrayList<String> jtTxt, ArrayList<String> colName, String ID, String action) {
    this.colName = colName;
    boolean out = false;
    this.id = ID;

    try {
      con = Utils.connectToDB();
      ResultSet rs = null;
      // // for (int i = 0; i < jtIns.size(); i++) {
      // // jtIns.get(i).setEditable(false);
      // // jtIns.get(i).setText("");
      // // }
      // }
      ArrayList<String> qryOut = new ArrayList<String>();

      String qry = formatQuery(action);
      PreparedStatement pstmt = con.prepareStatement(qry);
      System.out.println(qry);
      pstmt.executeUpdate("USE library");

      if (action.equals("select")) {
        rs = pstmt.executeQuery(qry);
        if (rs.next()) {
          for (int i = 0; i < colName.size(); i++) {
            qryOut.add(rs.getString(colName.get(i)));
            this.outPut = qryOut;
          }
        }
      } else if (action.equals("delete")) {
        pstmt.setString(1, ID);
      } else {
        for (int i = 0; i < jtTxt.size(); i++) {
          if (jtTxt.get(i).isBlank() || jtTxt == null) {
            return out;
          }
          pstmt.setString(i + 1, jtTxt.get(i));
        }
      }
      pstmt.execute();

      con.close();
      out = true;
      return out;
    } catch (Exception ex) {
      ex.printStackTrace();
      return out;
    }
  }

  // setup things when modifying a table
  public boolean setModTable(ArrayList<JTextField> jtIn, ArrayList<String> colName, String action) {
    boolean out = true;
    this.colName = colName;

    ArrayList<String> jtInStr = new ArrayList<String>();
    for (int i = 0; i < jtIn.size(); i++) {
      jtInStr.add(jtIn.get(i).getText());
    }

    try {
      Connection con = Utils.connectToDB();

      for (int i = 0; i < jtInStr.size(); i++) {
        if (jtInStr.get(i).equals("")) {
          System.out.println(jtInStr.get(i));
          out = false;
          return out;
        }
      }

      String colNameStr = formatQuery(action);
      System.out.println(colNameStr);
      // String addQRY = "INSERT INTO " + dbTable + colNameStr + " VALUES" + fmtVal;
      PreparedStatement pstmt = con.prepareStatement(colNameStr);
      pstmt.execute("USE library");

      for (int i = 0; i < jtInStr.size(); i++) {
        pstmt.setString(i + 1, jtInStr.get(i));
      }
      pstmt.execute();

      con.close();
      return out;
    } catch (Exception ex) {
      ex.printStackTrace();
      out = false;
      return out;
    }
  }

  public boolean setModTable(ArrayList<JTextField> jtIn, ArrayList<String> colName) {
    this.colName = colName;
    return setModTable(jtIn, colName, "insert");
  }

  private String formatQuery(String e) {
    // colName.add(0, head.get(0));
    // head.remove(0);
    String base = colName.toString();
    // String base = head.toString();

    if (e.equals("update")) {
      base = base.replace("[", "").replace("]", "=?").replace(" ", "").replace(",", "=?,");
      String fmt = "UPDATE " + dbTable + " SET " + base + " WHERE " + head.get(0) + "=" + id;
      return fmt;

    }
    if (e.equals("insert")) {
      base = base.replace("[", "(").replace("]", ")").replace(" ", "");
      String fmt = base.replaceAll("(\\w+)", "?");
      String qry = "INSERT INTO " + dbTable + base + " VALUES" + fmt;
      return qry;
    }
    if (e.equals("delete")) {
      String qry = "DELETE FROM " + dbTable + " WHERE " + head.get(0) + "=?";
      return qry;
    }
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

      // ArrayList<String> head = Utils.getTableColName(dbTable);

      Object[] headers = head.toArray();

      String data[][] = new String[Utils.getTableRowNum(dbTable)][col.length];

      int i = 0;
      while (rs.next()) {
        for (int x = 0; x < headers.length; x++) {
          data[i][x] = rs.getString(headers[x].toString());
        }
        i++;
      }

      DefaultTableModel model = new DefaultTableModel(data, col) {
        @Override
        public boolean isCellEditable(int row, int column) {
          // all cells false
          return false;
        }
      };

      con.close();
      return model;
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
    tbl.setShowGrid(true);
    tbl.setShowVerticalLines(true);
    tbl.setAutoCreateRowSorter(true);
    tbl.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    return tbl;
  }

}
