package com.dlib;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.dlib.manageTables.ManageBooks;

import net.miginfocom.swing.MigLayout;

public class BooksTable {

  public static JTable bookTable;

  public static String[] col = { "BID", "TITLE", "AUTHOR", "GENRE", "QUANTITY", "ISSUED", "PUBLISH YEAR" };
  // public static String[] headers = { "b_id", "title", "author", "genre",
  // "quantity", "issued", "publishYear" };

  private static JPanel panel;
  private static JScrollPane pane;
  private static JButton addBook, editBook, remBook;

  public static JPanel booksTable() {

    TableOf bTbl = new TableOf("books", col);
    bookTable = bTbl.getTable();

    // Gui layout starts here
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

  // public static DefaultTableModel showBooksTable() {

  // Connection con = Utils.connectToDB();

  // try {
  // Statement stmt = con.createStatement();
  // stmt.executeUpdate("USE library");
  // ResultSet rs = stmt.executeQuery("select * from " + tableName);

  // String data[][] = new String[Utils.getTableRowNum(tableName)][col.length];

  // // TODO: format to for-loop
  // // temporary
  // // String[] cols = { "b_id", "title", "author", "genre", "quantity",
  // "issued",
  // // "publishYear" };

  // int i = 0;
  // while (rs.next()) {
  // for (int x = 0; x < headers.length; x++) {
  // data[i][x] = rs.getString(headers[x]);
  // }
  // i++;
  // }

  // DefaultTableModel model = new DefaultTableModel(data, col) {
  // @Override
  // public boolean isCellEditable(int row, int column) {
  // // all cells false
  // return false;
  // }
  // };

  // return model;
  // } catch (Exception e) {
  // System.out.println(e);
  // }
  // return null;
  // }
}
