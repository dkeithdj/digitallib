package com.dlib;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.*;

import net.miginfocom.swing.MigLayout;

public class TableOf extends Utils {
  protected JPanel pnl;
  protected JFrame frm;

  protected JButton searchFilter, showTbl;
  protected JLabel confirmAct, status;
  protected JButton confButton;
  protected JTextField confirmActIn;
  protected JComboBox<String> pickFilter;
  protected JTextField searchIn;

  protected String dbTable, id;
  protected String[] col;
  protected String[][] data;
  private ArrayList<String> head;

  // Manage start
  protected JPanel panel;
  protected JScrollPane pane;
  protected JButton addBut, editBut, remBut;
  // Manage end

  protected JTable table;

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
    return this.selectRow(0, ID);
  }

  public Map<String, String> selectRow(int index, String ID) {
    try {
      con = connectToDB();

      Map<String, String> qryOutPut = new LinkedHashMap<String, String>();

      String qry = "SELECT * FROM " + dbTable + " WHERE " + head.get(index) + "=" + ID;

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

  // validates an ID input if it exists in the database
  protected boolean validateID(String ID) {
    try {
      con = connectToDB();
      String qry = "SELECT " + head.get(0) + " FROM " + dbTable + " WHERE " + head.get(0) + "=" + ID;
      PreparedStatement pstmt = con.prepareStatement(qry);
      pstmt.executeUpdate("USE library");
      ResultSet rs = pstmt.executeQuery(qry);

      return (rs.next()) ? true : false;

    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  // converts a Map to an arraylist
  protected ArrayList<String> convertToList(Map<String, String> hashOut) {
    Collection<String> val = hashOut.values();
    ArrayList<String> valL = new ArrayList<String>(val);
    return valL;
  }

  // sets data to be used by jtable
  protected String[][] setData(String srch, String category) {

    try {
      con = connectToDB();
      Statement stmt = con.createStatement();
      stmt.executeUpdate("USE library");

      // This query selects rows that match the input
      String qry1 = "SELECT * FROM " + dbTable + " WHERE " + category + " LIKE '" + srch + "%'";
      // gets the count of of the results on qry1 to be used as initial row value in
      // the data[][]
      ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + "(" + qry1 + ")" + "AS totalCount");
      rs.next();
      int colCount = rs.getInt(1);

      data = new String[colCount][col.length];

      Statement nstmt = con.createStatement();
      rs = nstmt.executeQuery(qry1);

      int i = 0;
      while (rs.next()) {
        for (int x = 0; x < head.size(); x++) {
          data[i][x] = rs.getString(head.get(x));
        }
        i++;
      }

      con.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
    return data;

  }

  // setups the model for the JTable
  public DefaultTableModel setupTable() {
    try {

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

  // confirms removal of a row by typing in the desired input
  protected void confirm(final String id, final String conf) {

    confirmAct = new JLabel("<html>Please type <u>" + conf + "</u> to confirm</html>");
    confirmActIn = new JTextField(15);
    status = new JLabel("");
    confButton = new JButton("Confirm Remove");
    confButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (conf.equals(confirmActIn.getText())) {
          deleteRow(id);
          frm.dispose();
        } else {
          status.setText("<html><font color=red>Input does not match</html>");
          confirmActIn.setText("");
        }
      }
    });

    frm = new JFrame("Confirmation");
    pnl = new JPanel();
    pnl.setLayout(new MigLayout("fill,wrap", "", "[][][]"));

    pnl.add(confirmAct, "span, center");
    pnl.add(confirmActIn, "span, growx");
    pnl.add(status, "span");
    pnl.add(confButton, "skip, split, right");

    frm.add(pnl);
    frm.setVisible(true);
    frm.setSize(300, 150);
    frm.setLocationRelativeTo(null);
    frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
      ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName);
      rs.next();
      return rs.getInt(1);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 0;
  }

  // Function that gets the column titles from the database
  public ArrayList<String> getTableColName() {
    return this.getTableColName(dbTable);
  }

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

}
