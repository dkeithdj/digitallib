package com.dlib;

import java.sql.Connection;
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

  // Constructor
  // @param col: database column titles
  // @param headers: JTable headers
  public TableOf(String dbName, String[] col) {
    this.dbTable = dbName;
    this.col = col;
    // this.headers = headers;
  }

  // Constructor (might remove)
  public TableOf(String dbName) {
    this.dbTable = dbName;
  }

  // setups the model for the JTable
  public DefaultTableModel setupTable() {
    try {
      con = Utils.connectToDB();
      Statement stmt = con.createStatement();
      stmt.executeUpdate("USE library");
      ResultSet rs = stmt.executeQuery("select * from " + dbTable);

      ArrayList<String> head = Utils.getTableColName(dbTable);
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
