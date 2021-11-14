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
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import net.miginfocom.swing.MigLayout;

public class IssuedBooksTable {

  private static String tableName = "issuedBooks";
  private static JFrame frm;
  public static void issuedBooksTable() {

    Connection con = Utils.connectToDB();

    try {
      Statement stmt = con.createStatement();
      stmt.executeUpdate("USE library");
      ResultSet rs = stmt.executeQuery("select * from " + tableName);

      String col[] = { "IBID", "MID", "BID", "FIRST NAME", "DATE ISSUED", "DURATION", "DATE RETURNED" };
      String data[][] = new String[Utils.getTableRowNum(tableName)][col.length];

      int i = 0;
      while (rs.next()) {
        int ibid = rs.getInt("ib_id");
        int mid = rs.getInt("m_id");
        int bid = rs.getInt("b_id");
        String firstName = rs.getString("firstName");
        String issuedDate = rs.getString("issuedDate");
        int brwPeriod = rs.getInt("borrowPeriod");
        String returnDate = rs.getString("returnDate");

        String details[] = { Integer.toString(ibid), Integer.toString(mid), Integer.toString(bid), firstName, issuedDate, Integer.toString(brwPeriod), returnDate };

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

      frm = new JFrame("Issued Books");

      // Gui layout starts here
      JTable table = new JTable(model);
      table.getTableHeader().setReorderingAllowed(false);
      table.setShowGrid(true);
      table.setShowVerticalLines(true);
      table.setAutoCreateRowSorter(true);
      table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

      JScrollPane pane = new JScrollPane(table);

      JPanel panel = new JPanel();

      JButton issueBook = new JButton("Issue Book");
      issueBook.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        }
      });
      JButton returnBook = new JButton("Return Book");
      returnBook.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        }
      });

      panel.setLayout(new MigLayout("insets 5 5 5 5, fill, debug", "", ""));
      panel.add(pane, "wrap, grow");
      panel.add(issueBook, "split, right");
      panel.add(returnBook);

      frm.add(panel);
      frm.setSize(500, 500);
      frm.setVisible(true);
      frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

      con.close();
    } catch (Exception e) {
      System.out.println(e);
    }

  }

}

