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

  public static String[] col = { "BID", "TITLE", "AUTHOR", "GENRE", "QUANTITY", "ISSUED", "PUBLISH YEAR" };
  // public static String[] headers = { "b_id", "title", "author", "genre",
  // "quantity", "issued", "publishYear" };

  private static JPanel panel;
  private static JScrollPane pane;
  private static JButton addBook, editBook, remBook;

  private static TableOf bookTbl = new TableOf("books", col);
  public static JTable bookTable = bookTbl.getTable();

  private static ManageBooks manBooks = new ManageBooks();

  // can add this in a superclass
  public TableOf getTableData() {
    return bookTbl;
  }

  public static JPanel booksTable() {

    // bookTbl = new TableOf("books", col);
    // bookTable = bookTbl.getTable();
    manBooks.setBookTable(bookTable);

    // Gui layout starts here
    pane = new JScrollPane(bookTable);

    panel = new JPanel();

    addBook = new JButton("Add");
    addBook.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        manBooks.addBook();
      }
    });
    editBook = new JButton("Edit");
    editBook.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        manBooks.editBook();
      }
    });
    remBook = new JButton("Remove");
    remBook.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        manBooks.removeBook();
      }
    });

    panel.setLayout(new MigLayout("wrap, fill, insets 0 0 0 0", "", "[100%][]"));
    panel.add(pane, "grow");
    panel.add(addBook, "split, right");
    panel.add(editBook);
    panel.add(remBook);

    return panel;
  }

}
