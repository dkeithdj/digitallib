package com.dlib;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.dlib.manageTables.ManageIssBooks;

import net.miginfocom.swing.MigLayout;

public class IssuedBooksTable {

  public static String col[] = { "IBID", "MID", "BID", "LAST NAME", "BOOK TITLE", "DATE ISSUED", "DURATION",
      "DATE RETURNED", "OVERDUED" };

  private static JFrame frm;
  private static JPanel panel;

  public static JTable issTable;
  public static JScrollPane pane;

  public static JButton issueBook, returnBook, remIssBook;

  private static ManageIssBooks manIssBooks = new ManageIssBooks();

  public static void issuedBooksTable() {

    frm = new JFrame("Issued Books");

    TableOf issBookTbl = new TableOf("issuedBooks", col);
    issTable = issBookTbl.getTable();
    manIssBooks.setIssBookTable(issTable);

    // Gui layout starts here
    pane = new JScrollPane(issTable);
    panel = new JPanel();

    issueBook = new JButton("Issue Book");
    issueBook.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        manIssBooks.issueBook();
      }
    });
    returnBook = new JButton("Return Book");
    returnBook.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        manIssBooks.returnBook();
      }
    });
    remIssBook = new JButton("Remove Issued");
    remIssBook.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        manIssBooks.remIssBook();
        // ManageIssBooks.remIssBook();
      }
    });

    panel.setLayout(new MigLayout("insets 5 5 5 5, fill", "", "[100%][]"));
    panel.add(pane, "wrap, grow");
    panel.add(issueBook, "split, right");
    panel.add(returnBook);
    panel.add(remIssBook);

    frm.add(panel);
    frm.setVisible(true);
    frm.setSize(800, 500);
    frm.setLocationRelativeTo(null);
    frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

  }

  // public static DefaultTableModel showIssBooksTable() {

  // Connection con = Utils.connectToDB();

  // try {
  // Statement stmt = con.createStatement();
  // stmt.executeUpdate("USE library");
  // ResultSet rs = stmt.executeQuery("select * from " + tableName);

  // String col[] = { "IBID", "MID", "BID", "LAST NAME", "BOOK TITLE", "DATE
  // ISSUED", "DURATION", "DATE RETURNED",
  // "OVERDUED" };
  // String data[][] = new String[Utils.getTableRowNum(tableName)][col.length];

  // int i = 0;
  // while (rs.next()) {
  // int ibid = rs.getInt("ib_id");
  // int mid = rs.getInt("m_id");
  // int bid = rs.getInt("b_id");
  // String lastName = rs.getString("brwrLName");
  // String bookTitle = rs.getString("bookTitle");
  // String issuedDate = rs.getString("issuedDate");
  // int brwPeriod = rs.getInt("borrowPeriod");
  // String returnDate = rs.getString("returnDate");
  // String overdued = rs.getString("overdued");

  // String details[] = { Integer.toString(ibid), Integer.toString(mid),
  // Integer.toString(bid), lastName, bookTitle,
  // issuedDate, Integer.toString(brwPeriod), returnDate, overdued };

  // for (int x = 0; x < details.length; x++) {
  // data[i][x] = details[x];
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
