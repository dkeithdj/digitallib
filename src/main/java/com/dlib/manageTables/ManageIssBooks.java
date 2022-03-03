package com.dlib.manageTables;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.dlib.BooksTable;
import com.dlib.IssuedBooksTable;
import com.dlib.MembersTable;
import com.dlib.TableOf;
import com.dlib.Utils;

import net.miginfocom.swing.MigLayout;

public class ManageIssBooks {

  private JPanel pnl;
  private JFrame frm;
  private JTable tbl, btbl;

  private JLabel ibIBID, ibMID, ibBID, ibLName, ibBTitle, ibBrwDate, ibDuration, ibRetDate, issueStatus;
  private JTextField ibidIn, midIn, bidIn, lNameIn, bTitleIn, brwDateIn, durationIn, retDateIn;
  private JButton retBook, issBook, validateIBID, remIssBook;
  private JComboBox<String> midInPick, bidInPick, ibidInPick;
  // private TableOf manMem = new TableOf("members", MembersTable.col);
  private TableOf manMem = new MembersTable().getTableData();
  // private TableOf manBook = new TableOf("books", BooksTable.col);
  private TableOf manBook = new ManageBooks().getTableData();
  private TableOf manIssBook = new TableOf("issuedBooks", IssuedBooksTable.col);
  private ArrayList<String> l;
  private ArrayList<String> out;

  private ArrayList<String> memCol, bookCol, issBookCol;

  private ArrayList<String> upMem, upBook;

  private ArrayList<JTextField> jtIns;
  private ArrayList<String> jtTxts;

  private String bookId;

  public ManageIssBooks() {
  }

  public void setBookTable(JTable btbl) {
    this.btbl = btbl;
  }

  public void setIssBookTable(JTable tbl) {
    this.tbl = tbl;
  }

  private void setOutputValBooks(ArrayList<String> val) {
    this.upBook = val;
  }

  private ArrayList<String> getOutputValBooks() {
    return this.upBook;
  }

  private ArrayList<String> getOutputValMembers() {
    return this.upMem;
  }

  private ArrayList<String> getOutputValIssBooks() {
    return this.out;
  }

  private String getDateToday() {
    DateTimeFormatter brwDate = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    LocalDate today = LocalDate.now();
    String dateToday = today.format(brwDate);
    return dateToday;
  }

  private ArrayList<String> getColumnNames(String tableName) {
    ArrayList<String> colName = Utils.getTableColName(tableName);
    // colName.remove(0);

    return colName;
  }

  // checks if issued books is overdued
  public static String isOverdue(int duration, String borrowDate, String returnDate) {
    LocalDate brwDate = LocalDate.parse(borrowDate, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    LocalDate retDate = LocalDate.parse(returnDate, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    Duration diff = Duration.between(brwDate.atStartOfDay(), retDate.atStartOfDay());
    int period = (int) diff.toDays();

    if (period <= duration) {
      return "NO";
    } else {
      return "YES";
    }
  }

  public void issueBook() {

    frm = new JFrame("Issue Book");
    pnl = new JPanel();

    pnl.setLayout(new MigLayout("wrap", "[][]", ""));

    ibMID = new JLabel("Member ID(MID): ");
    l = manMem.getTableIDs();
    midInPick = new JComboBox<String>(l.toArray(new String[l.size()]));
    midInPick.setEditable(true);

    midIn = new JTextField("", 15);

    ibBID = new JLabel("Book ID(BID): ");
    l = manBook.getTableIDs();
    bidInPick = new JComboBox<String>(l.toArray(new String[l.size()]));
    bidInPick.setEditable(true);

    bidIn = new JTextField("", 15);
    ibDuration = new JLabel("Days to borrow: ");
    durationIn = new JTextField("", 15);
    ibBrwDate = new JLabel("Borrow Date: ");
    issueStatus = new JLabel("Status: ");

    final String dateToday = getDateToday();
    ibBrwDate.setText("Borrow Date: " + dateToday);

    // get this from manageBooks and ManageMembers
    memCol = getColumnNames("members");
    memCol.remove(0);
    bookCol = getColumnNames("books");
    bookCol.remove(0);
    issBookCol = getColumnNames("issuedBooks");
    issBookCol.remove(0);

    // setOutputValBooks(manBook.setEdits(bookCol, sqBID, "select"));

    // mBooks.manBook.getOutput();
    // TODO: fix this

    // manIssBook.setEdits(issbookCol, "select");
    // might be a handful but this just converts the string output to an integer

    issBook = new JButton("Issue Book!");
    issBook.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String sqMID = (String) midInPick.getSelectedItem();
        String sqBID = (String) bidInPick.getSelectedItem();
        String sqDuration = durationIn.getText();

        upMem = manMem.setEdits(memCol, sqMID, "select");
        upBook = manBook.setEdits(bookCol, sqBID, "select");

        // setOutputValBooks(upBook);
        // System.out.println("hays " + upBook);
        int quantCount = Integer.parseInt(upBook.get(3));
        int issueCount = Integer.parseInt(upBook.get(4));

        jtTxts = new ArrayList<String>();
        jtTxts.add(sqMID);
        jtTxts.add(sqBID);
        jtTxts.add(upMem.get(1));
        jtTxts.add(upBook.get(0));
        jtTxts.add(dateToday);
        jtTxts.add(sqDuration);
        jtTxts.add("-");
        jtTxts.add("-");
        // add stuff
        System.out.println(jtTxts);

        if (quantCount != 0) {
          // needs title,quantity,issued
          // inserts stuff here
          manIssBook.setEdits(jtTxts, issBookCol, "insert");
          quantCount -= 1;
          issueCount += 1;
          // upBook.remove(0);
          upBook.set(3, "" + quantCount);
          upBook.set(4, "" + issueCount);
          manBook.setEdits(upBook, bookCol, "update");

          issueStatus.setText("<html>Status: <font color=green>Success</html>");
        } else {
          issueStatus.setText("<html>Status: <font color=red>Out of Stock</html>");
        }

        /*
         * check select quantity and issued then insert then update quantity and issued
         * 
         */

        durationIn.setText("");

        // refresh the table
        BooksTable.bookTable.setModel(manBook.setupTable());
        tbl.setModel(manIssBook.setupTable());
      }

    });

    pnl.add(ibMID);
    pnl.add(midInPick, "grow");
    pnl.add(ibBID);
    pnl.add(bidInPick, "grow");
    pnl.add(ibDuration);
    pnl.add(durationIn);
    pnl.add(ibBrwDate);
    pnl.add(issueStatus, "skip");
    pnl.add(issBook, "split, right");

    frm.add(pnl);
    frm.setVisible(true);
    frm.pack();
    frm.setLocationRelativeTo(null);
    frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

  }

  public void returnBook() {

    frm = new JFrame("Return Book");
    pnl = new JPanel();

    pnl.setLayout(new MigLayout("wrap", "[][]", ""));

    ibIBID = new JLabel("Issue Book ID(IBID): ");
    l = manIssBook.getTableIDs();
    ibidInPick = new JComboBox<String>(l.toArray(new String[l.size()]));
    ibidInPick.setEditable(true);

    // ibidIn = new JTextField("", 15);
    ibMID = new JLabel("Member ID(MID): ");
    midIn = new JTextField("", 15);
    ibBID = new JLabel("Book ID(BID): ");
    bidIn = new JTextField("", 15);
    ibLName = new JLabel("Last name: ");
    lNameIn = new JTextField("", 15);
    ibBTitle = new JLabel("Book title: ");
    bTitleIn = new JTextField("", 15);
    ibBrwDate = new JLabel("Borrow Date: ");
    brwDateIn = new JTextField("", 15);
    ibDuration = new JLabel("Days to borrow: ");
    durationIn = new JTextField("", 15);
    ibRetDate = new JLabel("Return Date: ");
    retDateIn = new JTextField("", 15);
    issueStatus = new JLabel("Status: ");

    retBook = new JButton("Return Book!");

    jtIns = new ArrayList<JTextField>();
    jtIns.add(midIn);
    jtIns.add(bidIn);
    jtIns.add(lNameIn);
    jtIns.add(bTitleIn);
    jtIns.add(brwDateIn);
    jtIns.add(durationIn);
    jtIns.add(retDateIn);

    // { midIn, bidIn, lNameIn, bTitleIn, brwDateIn, durationIn, retDateIn };

    // DateTimeFormatter brwDate = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    // LocalDate today = LocalDate.now();
    // final String dateToday = today.format(brwDate);
    retDateIn.setText(getDateToday());

    for (int i = 0; i < jtIns.size(); i++) {
      jtIns.get(i).setEditable(false);
      // txtInputs[i].setEditable(false);
    }
    retBook.setEnabled(false);

    final ArrayList<String> issBookCol = getColumnNames("issuedBooks");
    issBookCol.remove(0);
    issBookCol.remove(issBookCol.size() - 1);

    ibidInPick.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        String qIBID = (String) ibidInPick.getSelectedItem();
        // Connection con = Utils.connectToDB();
        out = manIssBook.setEdits(issBookCol, qIBID, "select");
        out.set(out.size() - 1, getDateToday());
        bookId = out.get(1);

        if (out != null) {
          retBook.setEnabled(true);
          for (int i = 0; i < jtIns.size(); i++) {
            jtIns.get(i).setText((out.get(i)));
            // jtIns.get(i).setEnabled(true);
          }
          issueStatus.setText("<html>Status: <font color=green>ID found</html>");
        } else {
          issueStatus.setText("<html>Status: <font color=red>Invalid</html>");
        }
      }
    });
    final ArrayList<String> ibCol = getColumnNames("issuedBooks");
    ibCol.remove(0);
    // issBookCol.remove(issBookCol.size() - 1);
    final ArrayList<String> bookCol = getColumnNames("books");
    bookCol.remove(0);

    jtIns.remove(jtIns.size() - 1);
    retBook.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String isOverdued = isOverdue(Integer.parseInt(durationIn.getText()), brwDateIn.getText(), retDateIn.getText());

        ArrayList<String> jtTxt = new ArrayList<String>();
        for (int i = 0; i < jtIns.size(); i++) {
          jtTxt.add(jtIns.get(i).getText());
        }

        jtTxt.add(isOverdued);

        // System.out.println(jtTxt);
        // System.out.println(isOverdued);

        ArrayList<String> bookstuff = manBook.setEdits(bookCol, bidIn.getText(), "select");
        System.out.println(bookstuff);

        int quantCount = Integer.parseInt(bookstuff.get(3));
        int issueCount = Integer.parseInt(bookstuff.get(4));

        if (issueCount != 0) {

          manIssBook.setEdits(jtTxt, ibCol, "update");
          quantCount += 1;
          issueCount -= 1;
          // upBook.remove(0);
          bookstuff.set(3, "" + quantCount);
          bookstuff.set(4, "" + issueCount);
          manBook.setEdits(bookstuff, bookCol, "update");

          issueStatus.setText("<html>Status: <font color=green>Book returned</html>");
        } else {
          issueStatus.setText("<html>Status: <font color=red>Book already returned</html>");
        }
        // TODO: put isOverdue()

        for (int i = 0; i < jtIns.size(); i++) {
          jtIns.get(i).setEditable(false);
          jtIns.get(i).setText("");
        }
        retBook.setEnabled(false);
        // String sqBID = bidIn.getText();

        // select issuedbooks and books
        // get issueDate from select
        // compare issueDate to currentDate (datToday)
        // get book quantity and issued

        // String checkIBID = ("SELECT ib_id FROM issuedBooks WHERE ib_id=" + sqIBID);

        // if (sqIBID == null || sqIBID.trim().equals("")) {
        // JOptionPane.showMessageDialog(frm, "There's an empty field!");
        // } else {

        // boolean ibidExists = Utils.doesIdExist("issuedBooks", checkIBID);

        // if (ibidExists == true) {
        // // Utils.IBUpdate(sqBID, sqIBID, dateToday, frm);

        // issueStatus.setText("Status: Returned");
        // ibidIn.setText("");
        // } else {
        // issueStatus.setText("Status: IBID not found");
        // ibidIn.setText("");
        // }
        // }

        // for (int i = 0; i < txtInputs.length; i++) {
        // txtInputs[i].setText("");
        // }

        // refresh the table
        manBook.getTable().setModel(manBook.setupTable());
        tbl.setModel(manIssBook.setupTable());
      }
    });

    if (Utils.getTableRowNum("issuedBooks") == 0) {
      // ibidIn.setEnabled(false);
      // validateIBID.setEnabled(false);
      retBook.setEnabled(false);
      retDateIn.setText("");
      issueStatus.setText("Status: Nothing to return");
    }

    pnl.add(ibIBID);
    pnl.add(ibidInPick, "grow");
    pnl.add(issueStatus, "span");
    pnl.add(ibMID);
    pnl.add(midIn);
    pnl.add(ibBID);
    pnl.add(bidIn);
    pnl.add(ibLName);
    pnl.add(lNameIn);
    pnl.add(ibBTitle);
    pnl.add(bTitleIn);
    pnl.add(ibBrwDate);
    pnl.add(brwDateIn);
    pnl.add(ibDuration);
    pnl.add(durationIn);
    pnl.add(ibRetDate);
    pnl.add(retDateIn);
    pnl.add(retBook, "skip, split, right");

    frm.add(pnl);
    frm.setVisible(true);
    frm.pack();
    frm.setLocationRelativeTo(null);
    frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }

  public void remIssBook() {

    frm = new JFrame("Remove Book");
    pnl = new JPanel();
    pnl.setLayout(new MigLayout("wrap", "[][]", ""));

    ibIBID = new JLabel("Issued Book IBID(IBID): ");
    ibidIn = new JTextField("", 15);

    remIssBook = new JButton("Remove Issued Book!");
    remIssBook.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        String qIBID = ibidIn.getText();

        Connection con = Utils.connectToDB();

        if (qIBID == null || qIBID.trim().equals("")) {
          JOptionPane.showMessageDialog(frm, "Input cannot be empty!");
        } else {
          try {
            String delQry = ("DELETE FROM issuedBooks WHERE ib_id=" + qIBID);

            Statement pstmt = con.createStatement();
            pstmt.executeUpdate("USE library");

            ResultSet rs = pstmt.executeQuery("SELECT returnDate FROM issuedBooks where ib_id=" + qIBID);

            if (rs.next() == true) {
              String retDateValue = rs.getString("returnDate");
              if (!retDateValue.equals("-")) {
                pstmt.executeUpdate(delQry);
                JOptionPane.showMessageDialog(frm, "Issue Removed!");
              } else {
                JOptionPane.showMessageDialog(frm, "Book has not been returned yet");
              }
            } else {
              JOptionPane.showMessageDialog(frm, "IBID doesn't exist");
            }

          } catch (Exception ex) {
            JOptionPane.showMessageDialog(frm, "Unable to remove issued, try again");
            ex.printStackTrace();
          }
        }
        ibidIn.setText("");
        // refresh the table
        tbl.setModel(manIssBook.setupTable());
      }
    });
    if (Utils.getTableRowNum("issuedBooks") == 0) {
      ibidIn.setEnabled(false);
      ibidIn.setText("");
      remIssBook.setEnabled(false);
    }

    pnl.add(ibIBID);
    pnl.add(ibidIn);
    pnl.add(remIssBook, "skip, split, right");

    frm.add(pnl);
    frm.setVisible(true);
    frm.pack();
    frm.setLocationRelativeTo(null);
    frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }

  // updates the quantity of the books from the booksTable when RETURNING
  // private void updateQuantity(ArrayList<String> jtIns, int quantCount, int
  // issueCount, String dateToday) {
  // select ibid then update quantities, then update dates then update ifOverdued
  // this.updateQuantity(null, quantCount, issueCount, "update", false); // <-
  // update quantities

  // get this stuff to update
  // int duration = rs.getInt("borrowPeriod");
  // String brwDateValue = rs.getString("issuedDate");
  // String retDateValue = rs.getString("returnDate");

  // int changeq = 1, changei = -1;

  // // if (borrow) {
  // // changeq = -1;
  // // changei = 1;
  // // }
  // if (quantCount != 0) {
  // // needs title,quantity,issued
  // // inserts stuff here
  // manIssBook.setEdits(jtIns, issBookCol, "update");
  // quantCount += changeq;
  // issueCount += changei;
  // // upBook.remove(0);
  // upBook.set(3, "" + quantCount);
  // upBook.set(4, "" + issueCount);
  // manBook.setEdits(upBook, bookCol, "update");

  // }
  // this.updateQuantity(jtTxts, quantCount, issueCount, "update", false);
  // }

  // updates the quantity of the books from the booksTable when BORROWING
  private void updateQuantity(ArrayList<String> colN, final ArrayList<String> jtIns, int quantCount, int issueCount,
      String action, boolean borrow) {
  }
}
