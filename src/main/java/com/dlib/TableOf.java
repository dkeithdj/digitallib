package com.dlib;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.*;

public class TableOf extends Utils {
  protected String dbTable, id;
  protected String[] col;
  private ArrayList<String> head;

  // Manage start
  protected JPanel panel;
  protected JScrollPane pane;
  protected JButton addBut, editBut, remBut;
  // Manage end

  public static JTable bookJTbl, memJTbl, issbookJTbl;

  protected Map<String, String> selectOutPut;
  protected ArrayList<String> l;

  // Constructor
  // @param dbName: name of database
  // @param col: database column titles
  public TableOf(String dbName) {
    this.dbTable = dbName;
    this.head = getTableColName(dbName);
  }

  // @param sets column names for JTable
  public void setColNames(String[] col) {
    this.col = col;
  }

  public JPanel getPanel() {
    return this.getPanel();
  }

  // jtTxt will be used as values that will be added in the database
  public boolean insertRow(ArrayList<String> jtTxt) {
    try {
      con = connectToDB();

      // formats the array e.g.
      // [m_id, firstName, lastName, title] to (firstname,lastName,title)
      // (\\w+) is a regex expression that selects a word e.g first, getting
      String base = head.toString();
      base = base.replaceFirst("(\\w+),", "").replace("[", "(").replace("]", ")").replace(" ", "");
      String fmt = base.replaceAll("(\\w+)", "?");
      String qry = "INSERT INTO " + dbTable + base + " VALUES" + fmt;

      PreparedStatement pstmt = con.prepareStatement(qry);
      pstmt.executeUpdate("USE library");

      for (int i = 0; i < jtTxt.size(); i++) {
        if (jtTxt.get(i).isBlank() || jtTxt == null) {
          return false;
        } else {
          pstmt.setString(i + 1, jtTxt.get(i));
        }
      }

      pstmt.execute();

      con.close();
      return true;

    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  // Updates an existing row in the database with the specific ID
  public boolean updateRow(ArrayList<String> jtTxt, String ID) {
    try {
      con = connectToDB();

      // formats the array e.g.
      // [m_id, firstName, lastName, title] to firstname=?,lastName=?,title=?
      String base = head.toString();
      base = base.replaceFirst("(\\w+),", "").replace("[", "").replace("]", "=?").replace(" ", "").replace(",", "=?,");
      String qry = "UPDATE " + dbTable + " SET " + base + " WHERE " + head.get(0) + "=" + ID;

      PreparedStatement pstmt = con.prepareStatement(qry);
      System.out.println(qry);
      pstmt.executeUpdate("USE library");

      for (int i = 0; i < jtTxt.size(); i++) {
        if (jtTxt.get(i).isBlank() || jtTxt == null) {
          return false;
        } else {
          pstmt.setString(i + 1, jtTxt.get(i));
        }
      }
      pstmt.executeUpdate();

      con.close();
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }

  }

  // Self-explanatory, this deletes the row based on the ID selected
  public boolean deleteRow(String ID) {
    try {
      con = connectToDB();

      String qry = "DELETE FROM " + dbTable + " WHERE " + head.get(0) + "=" + ID;

      Statement pstmt = con.createStatement();

      pstmt.executeUpdate("USE library");
      pstmt.execute(qry);

      con.close();
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }

  }

  // selects the row of the selected ID
  // returns a Map in order for easy fetching of data when choosing a value
  public Map<String, String> selectRow(String ID) {
    try {
      con = connectToDB();

      Map<String, String> qryOutPut = new LinkedHashMap<String, String>();

      String qry = "SELECT * FROM " + dbTable + " WHERE " + head.get(0) + "=" + ID;

      PreparedStatement pstmt = con.prepareStatement(qry);
      System.out.println(qry);
      pstmt.executeUpdate("USE library");
      ResultSet rs = pstmt.executeQuery(qry);
      if (rs.next()) {
        for (int i = 0; i < head.size(); i++) {
          qryOutPut.put(head.get(i), rs.getString(head.get(i)));
        }
      }

      pstmt.execute();

      qryOutPut.remove(head.get(0));

      this.selectOutPut = qryOutPut;

      con.close();
      return qryOutPut;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }

  }

  // converts a Map to an arraylist
  protected ArrayList<String> convertToList(Map<String, String> hashOut) {
    Collection<String> val = hashOut.values();
    ArrayList<String> valL = new ArrayList<String>(val);
    return valL;
  }

  // Gets the table IDs (usually used in JComboBox)
  public ArrayList<String> getTableIDs() {
    ArrayList<String> IDs = new ArrayList<String>();
    try {
      con = connectToDB();

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
      con = connectToDB();
      Statement stmt = con.createStatement();
      stmt.executeUpdate("USE library");
      ResultSet rs = stmt.executeQuery("SELECT * FROM " + dbTable);

      Object[] headers = head.toArray();

      String data[][] = new String[getTableRowNum(dbTable)][col.length];

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
      e.printStackTrace();
    }
    return null;
  }

  // Function that gets the number of rows in the table
  public int getTableRowNum() {
    return this.getTableRowNum(dbTable);
  }

  public int getTableRowNum(String tableName) {

    try {
      con = connectToDB();
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
  public ArrayList<String> getTableColName(String tableName) {
    int colCount = 0;
    ArrayList<String> col = new ArrayList<String>();

    try {
      con = connectToDB();
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

  // understandable
  public String getDateToday() {
    return this.getDateToday("MM/dd/yyyy");
  }

  public String getDateToday(String pattern) {
    DateTimeFormatter dfmt = DateTimeFormatter.ofPattern(pattern);
    LocalDateTime today = LocalDateTime.now();
    String dateToday = today.format(dfmt);
    return dateToday;
  }

  // Refreshes the tables after there're changes happend such as
  // adding,editing,removing data
  // Made it static so that they can share the same value
  public void refreshTable() {
    bookJTbl.setModel(new ManageBooks().setupTable());
    memJTbl.setModel(new ManageMembers().setupTable());
    issbookJTbl.setModel(new ManageIssBooks().setupTable());
  }

}
