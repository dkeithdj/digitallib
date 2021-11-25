package com.dlib;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.dlib.manageTables.ManageBooks;

import net.miginfocom.swing.MigLayout;

public class BooksTable {

  public static JTable bookTable;

  private static JPanel panel;
  private static String tableName = "books";

  private static JScrollPane pane;

  private static JButton addBook, editBook, remBook;

  public static JPanel booksTable() {

    // Gui layout starts here
    bookTable = new JTable();
    bookTable.setModel(showBooksTable());
    bookTable.getTableHeader().setReorderingAllowed(false);
    bookTable.setShowGrid(true);
    bookTable.setShowVerticalLines(true);
    bookTable.setAutoCreateRowSorter(true);
    bookTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

    pane = new JScrollPane(bookTable);

    panel = new JPanel();
    addBook = new JButton("Add");
    addBook.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ManageBooks.addBook();
      }
    });
    editBook = new JButton("Edit");
    editBook.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ManageBooks.editBook();
      }
    });
    remBook = new JButton("Remove");
    remBook.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ManageBooks.removeBook();
      }
    });

    panel.setLayout(new MigLayout("wrap, fill, insets 0 0 0 0", "", "[100%][]"));
    panel.add(pane, "grow");
    panel.add(addBook, "split, right");
    panel.add(editBook);
    panel.add(remBook);

    return panel;
  }

  public static DefaultTableModel showBooksTable() {

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
}
