package com.dlib;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.dlib.manageTables.ManageIssBooks;

import net.miginfocom.swing.MigLayout;

public class IssuedBooksTable {

  private static String tableName = "issuedBooks";

  private static JFrame frm;
  private static JPanel panel;

  public static JTable issTable;
  public static JScrollPane pane;

  public static JButton issueBook, returnBook, remIssBook;

  public static void issuedBooksTable() {

    frm = new JFrame("Issued Books");
    issTable = new JTable();

    // Gui layout starts here
    issTable.setModel(IssuedBooksTable.showIssBooksTable());
    issTable.getTableHeader().setReorderingAllowed(false);
    issTable.setShowGrid(true);
    issTable.setShowVerticalLines(true);
    issTable.setAutoCreateRowSorter(true);
    issTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

    pane = new JScrollPane(issTable);

    panel = new JPanel();

    issueBook = new JButton("Issue Book");
    issueBook.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ManageIssBooks.issueBook();
      }
    });
    returnBook = new JButton("Return Book");
    returnBook.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ManageIssBooks.returnBook();
      }
    });
    remIssBook = new JButton("Remove Issued");
    remIssBook.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ManageIssBooks.remIssBook();
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

  public static DefaultTableModel showIssBooksTable() {

    Connection con = Utils.connectToDB();

    try {
      Statement stmt = con.createStatement();
      stmt.executeUpdate("USE library");
      ResultSet rs = stmt.executeQuery("select * from " + tableName);

      String col[] = { "IBID", "MID", "BID", "LAST NAME", "BOOK TITLE", "DATE ISSUED", "DURATION", "DATE RETURNED", "OVERDUED" };
      String data[][] = new String[Utils.getTableRowNum(tableName)][col.length];

      int i = 0;
      while (rs.next()) {
        int ibid = rs.getInt("ib_id");
        int mid = rs.getInt("m_id");
        int bid = rs.getInt("b_id");
        String lastName = rs.getString("brwrLName");
        String bookTitle = rs.getString("bookTitle");
        String issuedDate = rs.getString("issuedDate");
        int brwPeriod = rs.getInt("borrowPeriod");
        String returnDate = rs.getString("returnDate");
        String overdued = rs.getString("overdued");

        String details[] = { Integer.toString(ibid), Integer.toString(mid), Integer.toString(bid), lastName, bookTitle,
            issuedDate, Integer.toString(brwPeriod), returnDate, overdued };

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
