package com.dlib.manageTables;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.dlib.BooksTable;
import com.dlib.IssuedBooksTable;
import com.dlib.Utils;

import net.miginfocom.swing.MigLayout;

public class ManageIssBooks {

  private static JPanel pnl;
  private static JFrame frm;

  private static JLabel ibIBID, ibMID, ibBID, ibLName, ibBTitle, ibBrwDate, ibDuration, ibRetDate, issueStatus;
  private static JTextField ibidIn, midIn, bidIn, lNameIn, bTitleIn, brwDateIn, durationIn, retDateIn;
  private static JButton retBook, issBook, validateIBID, remIssBook;

  public static void issueBook() {

    frm = new JFrame("Issue Book");
    pnl = new JPanel();

    pnl.setLayout(new MigLayout("wrap", "[][]", ""));

    ibMID = new JLabel("Member ID(MID): ");
    midIn = new JTextField("", 15);
    ibBID = new JLabel("Book ID(BID): ");
    bidIn = new JTextField("", 15);
    ibDuration = new JLabel("Days to borrow: ");
    durationIn = new JTextField("", 15);
    ibBrwDate = new JLabel("Borrow Date: ");
    issueStatus = new JLabel("Status: ");

    DateTimeFormatter brwDate = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    LocalDate today = LocalDate.now();
    final String dateToday = today.format(brwDate);
    ibBrwDate.setText("Borrow Date: " + dateToday);

    issBook = new JButton("Issue Book!");
    issBook.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        String sqMID = midIn.getText();
        String sqBID = bidIn.getText();
        String sqDuration = durationIn.getText();

        String checkMID = ("SELECT m_id FROM members WHERE m_id=" + sqMID);
        String checkBID = ("SELECT b_id FROM books WHERE b_id=" + sqBID);

        if (sqMID.trim().equals("") || sqBID.trim().equals("") || sqDuration.trim().equals("")) {
          JOptionPane.showMessageDialog(frm, "There's an empty field!");
        } else {

          boolean midExists = Utils.doesIdExist("members", checkMID);
          boolean bidExists = Utils.doesIdExist("books", checkBID);

          if ((midExists && bidExists) == true) {

            String qry1 = "INSERT INTO issuedBooks(m_id,b_id,brwrLName,bookTitle,issuedDate,borrowPeriod,returnDate)";
            String qry2a = ("(SELECT m_id FROM members WHERE m_id="+sqMID+")");
            String qry2b = ("(SELECT b_id FROM books WHERE b_id="+sqBID+")");
            String qry2c = ("(SELECT lastName FROM members WHERE m_id="+sqMID+")");
            String qry2d = ("(SELECT title FROM books WHERE b_id="+sqBID+")");
            String qry2e = ("('"+dateToday+"')");
            String qry2f = ("("+sqDuration+")");
            String qry2g = ("('-')");

            String qry0 = String.format("%s VALUES(%s,%s,%s,%s,%s,%s,%s)", qry1,qry2a,qry2b,qry2c,qry2d,qry2e,qry2f,qry2g);

            Connection con = Utils.connectToDB();
            try {
              Statement stmt = con.createStatement();
              stmt.executeUpdate("USE library");

              ResultSet rs = stmt.executeQuery("SELECT quantity,issued FROM books where b_id=" + sqBID);

              if (rs.next() == true) {
                int quantCount = rs.getInt("quantity");
                int issueCount = rs.getInt("issued");
                if (quantCount != 0) {
                  stmt.executeUpdate(qry0);
                  quantCount-=1;
                  issueCount+=1;
                  stmt.executeUpdate("UPDATE books SET quantity="+quantCount+",issued="+issueCount+" WHERE b_id="+sqBID);
                  issueStatus.setText("Status: Successfully issued book");
                }
                else {
                  JOptionPane.showMessageDialog(frm, "Out of stock");
                }
              }
            } catch (Exception ex) {
              JOptionPane.showMessageDialog(frm, "Unable to issue book, try again");
              ex.printStackTrace();
            }

            issueStatus.setText("Status: MID and BID found");
          } else {
            issueStatus.setText("Status: MID or BID not found");
            midIn.setText("");
            bidIn.setText("");
          }
        }
        BooksTable.bookTable.setModel(BooksTable.showBooksTable());
        IssuedBooksTable.issTable.setModel(IssuedBooksTable.showIssBooksTable());
      }
    });

    pnl.add(ibMID);
    pnl.add(midIn);
    pnl.add(ibBID);
    pnl.add(bidIn);
    pnl.add(ibDuration);
    pnl.add(durationIn);
    pnl.add(ibBrwDate);
    pnl.add(issueStatus, "skip");
    pnl.add(issBook, "split, right");

    frm.add(pnl);
    frm.setVisible(true);
    frm.pack();
    frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

  }

  public static void returnBook() {

    frm = new JFrame("Return Book");
    pnl = new JPanel();

    pnl.setLayout(new MigLayout("wrap", "[][]", ""));

    ibIBID = new JLabel("Issue Book ID(IBID): ");
    ibidIn = new JTextField("", 15);
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

    final JTextField[] txtInputs = {midIn,bidIn,lNameIn,bTitleIn,brwDateIn,durationIn,retDateIn};

    DateTimeFormatter brwDate = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    LocalDate today = LocalDate.now();
    final String dateToday = today.format(brwDate);
    retDateIn.setText(dateToday);

    for (int i = 0; i < txtInputs.length; i++) {
      txtInputs[i].setEditable(false);
    }
    validateIBID = new JButton("Check");
    validateIBID.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        Connection con = Utils.connectToDB();

        String qIBID = ibidIn.getText();

        if (qIBID == null || qIBID.trim().equals("")) {
          for (int i = 0; i < txtInputs.length; i++) {
            txtInputs[i].setEditable(false);
            txtInputs[i].setText("");
          }
          issueStatus.setText("Status: Invalid");
        } else {
          String checkIBID = ("SELECT ib_id FROM issuedBooks WHERE ib_id=" + qIBID);
          String getIBIDValues = ("SELECT * FROM issuedBooks WHERE ib_id=" + qIBID);

          try {
            Statement pstmt = con.createStatement();
            pstmt.executeUpdate("USE library");

            boolean midExists = Utils.doesIdExist("issuedBooks", checkIBID);

            if (midExists == true) {
              ResultSet rs = pstmt.executeQuery(getIBIDValues);
              while (rs.next()) {
                for (int i = 0; i < txtInputs.length; i++) {
                  txtInputs[0].setText(rs.getString("m_id"));
                  txtInputs[1].setText(rs.getString("b_id"));
                  txtInputs[2].setText(rs.getString("brwrLName"));
                  txtInputs[3].setText(rs.getString("bookTitle"));
                  txtInputs[4].setText(rs.getString("issuedDate"));
                  txtInputs[5].setText((String) rs.getString("borrowPeriod"));
                  txtInputs[6].setText(dateToday);
                  txtInputs[i].setEditable(false);
                }
              }
              issueStatus.setText("Status: IBID Exists");
              retBook.setEnabled(true);
            } else {
              for (int i = 0; i < txtInputs.length; i++) {
                txtInputs[i].setEditable(false);
                txtInputs[i].setText("");
              }
              retBook.setEnabled(false);
              ibidIn.setText("");
              issueStatus.setText("Status: IBID not found");
            }
          } catch (Exception ex) {
            JOptionPane.showMessageDialog(frm, "Input must be a number");
            ex.printStackTrace();
          }
        }
      }
    });

    retBook = new JButton("Return Book!");
    retBook.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        String sqIBID = ibidIn.getText();
        String sqBID = bidIn.getText();

        String checkIBID = ("SELECT ib_id FROM issuedBooks WHERE ib_id=" + sqIBID);

        if (sqIBID == null || sqIBID.trim().equals("")) {
          JOptionPane.showMessageDialog(frm, "There's an empty field!");
        } else {

          boolean ibidExists = Utils.doesIdExist("issuedBooks", checkIBID);

          if (ibidExists == true) {
            Utils.IBUpdate(sqBID, sqIBID, dateToday, frm);

            issueStatus.setText("Status: Returned");
          } else {
            issueStatus.setText("Status: IBID not found");
            ibidIn.setText("");
          }
        }
        BooksTable.bookTable.setModel(BooksTable.showBooksTable());
        IssuedBooksTable.issTable.setModel(IssuedBooksTable.showIssBooksTable());
      }
    });

    if (Utils.getTableRowNum("issuedBooks") == 0) {
      ibidIn.setEnabled(false);
      validateIBID.setEnabled(false);
      retBook.setEnabled(false);
      retDateIn.setText("");
      issueStatus.setText("Status: Nothing to return");
    }

    pnl.add(ibIBID);
    pnl.add(ibidIn);
    pnl.add(issueStatus);
    pnl.add(validateIBID, "split, right, wrap");
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
    frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }
  public static void remIssBook() {

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
            String delQry = ("DELETE FROM issuedBooks WHERE ib_id="+qIBID);

            Statement pstmt = con.createStatement();
            pstmt.executeUpdate("USE library");

            ResultSet rs = pstmt.executeQuery("SELECT returnDate FROM issuedBooks where ib_id="+qIBID);

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
        IssuedBooksTable.issTable.setModel(IssuedBooksTable.showIssBooksTable());
      }
    });
    if (Utils.getTableRowNum("issuedBooks") == 0) {
      ibidIn.setEnabled(false);
      ibidIn.setText("Nothing to remove");
      remIssBook.setEnabled(false);
    }

    pnl.add(ibIBID);
    pnl.add(ibidIn);
    pnl.add(remIssBook, "skip, split, right");

    frm.add(pnl);
    frm.setVisible(true);
    frm.pack();
    frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }
}
