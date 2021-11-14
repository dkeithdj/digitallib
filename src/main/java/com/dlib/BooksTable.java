package com.dlib;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import net.miginfocom.swing.MigLayout;

public class BooksTable {
  private static String tableName = "books";

  public static JPanel booksTable() {

    Connection con = Utils.connectToDB();

    try {
      Statement stmt = con.createStatement();
      stmt.executeUpdate("USE library");
      ResultSet rs = stmt.executeQuery("select * from " + tableName);

      String col[] = { "BID", "TITLE", "AUTHOR", "GENRE", "QUANTITY", "ISSUED", "PUBLISH YEAR" };
      String data[][] = new String[Utils.getTableRowNum(tableName)][col.length];

      int i = 0;
      while (rs.next()) {
        int bid = rs.getInt("b_id");
        String title = rs.getString("title");
        String author = rs.getString("author");
        String genre = rs.getString("genre");
        int quantity = rs.getInt("quantity");
        int issued = rs.getInt("issued");
        String publishYear = rs.getString("publishYear");

        String details[] = { Integer.toString(bid), title, author, genre, Integer.toString(quantity),
            Integer.toString(issued), publishYear };

        for (int x = 0; x < details.length; x++) {
          data[i][x] = details[x];
        }
        i++;
      }

      TableModel model = new DefaultTableModel(data, col) {
        @Override
        public boolean isCellEditable(int row, int column) {
          // all cells false
          return false;
        }
      };

      // Gui layout starts here
      JTable table = new JTable(model);
      table.getTableHeader().setReorderingAllowed(false);
      table.setShowGrid(true);
      table.setShowVerticalLines(true);
      table.setAutoCreateRowSorter(true);
      table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

      JScrollPane pane = new JScrollPane(table);

      JPanel panel = new JPanel();
      JButton addBook = new JButton("Add");
      addBook.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          ManageBooks.addBook();
        }
      });
      JButton editBook = new JButton("Edit");
      editBook.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          ManageBooks.editBook();
        }
      });
      JButton remBook = new JButton("Remove");
      remBook.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          ManageBooks.removeBook();
        }
      });

      panel.setLayout(new MigLayout("insets 5 5 5 5, fill, debug", "", ""));
      panel.add(pane, "wrap, grow");
      panel.add(addBook, "split, right, top");
      panel.add(editBook, "top");
      panel.add(remBook, "top");

      con.close();
      return panel;
    } catch (Exception e) {
      System.out.println(e);
    }
    return null;

  }

}
