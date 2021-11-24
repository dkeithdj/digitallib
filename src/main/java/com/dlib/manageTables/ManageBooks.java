package com.dlib.manageTables;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.dlib.BooksTable;
import com.dlib.Utils;

import net.miginfocom.swing.MigLayout;

public class ManageBooks {

  private static JPanel pnl;
  private static JFrame frm;

  private static JLabel bTitle, bAuthor, bGenre, bQuantity, bPubYear, BIDVal, BIDStatus, bookID;
  private static JTextField titleIn, authorIn, genreIn, quantityIn, pubYearIn, BIDIn;
  private static JButton addBook, validateBID, editBook, remBook;

  // Add Book
  public static void addBook() {

    frm = new JFrame("Add Book");
    pnl = new JPanel();
    pnl.setLayout(new MigLayout("wrap", "[][]", ""));

    bTitle = new JLabel("Title: ");
    titleIn = new JTextField("", 15);
    bAuthor = new JLabel("Author: ");
    authorIn = new JTextField("", 15);
    bGenre = new JLabel("Genre: ");
    genreIn = new JTextField("", 15);
    bQuantity = new JLabel("Quantity: ");
    quantityIn = new JTextField("", 15);
    bPubYear = new JLabel("Publish Year: ");
    pubYearIn = new JTextField("", 15);

    final JTextField[] txtInputs = { titleIn, authorIn, genreIn, quantityIn, pubYearIn };

    addBook = new JButton("Add Book!");
    addBook.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        String qTitle = titleIn.getText();
        String qAuthor = authorIn.getText();
        String qGenre = genreIn.getText();
        String qQuantity = quantityIn.getText();
        String qPubYear = pubYearIn.getText();

        Connection con = Utils.connectToDB();

        try {
          if (qTitle.trim().equals("") || qAuthor.trim().equals("") || qGenre.trim().equals("")
              || qQuantity.trim().equals("") || qPubYear.trim().equals("")) {
            JOptionPane.showMessageDialog(frm, "There's an empty field!");
          } else {

            String addQRY = "INSERT INTO books(title,author,genre,quantity,issued,publishYear) VALUES(?,?,?,?,?,?)";
            PreparedStatement pstmt = con.prepareStatement(addQRY);
            pstmt.executeUpdate("USE library");

            pstmt.setString(1, qTitle);
            pstmt.setString(2, qAuthor);
            pstmt.setString(3, qGenre);
            pstmt.setInt(4, Integer.parseInt(qQuantity));
            pstmt.setInt(5, 0);
            pstmt.setInt(6, Integer.parseInt(qPubYear));

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(frm, "Book Added!");
          }
        } catch (Exception ex) {
          JOptionPane.showMessageDialog(frm, "Unable to add book, try again");
          ex.printStackTrace();
        }
        for (int i = 0; i < txtInputs.length; i++) {
          txtInputs[i].setText("");
        }
        BooksTable.bookTable.setModel(BooksTable.showBooksTable());
      }
    });

    pnl.add(bTitle);
    pnl.add(titleIn);
    pnl.add(bAuthor);
    pnl.add(authorIn);
    pnl.add(bGenre);
    pnl.add(genreIn);
    pnl.add(bQuantity);
    pnl.add(quantityIn);
    pnl.add(bPubYear);
    pnl.add(pubYearIn);
    pnl.add(addBook, "skip, split, right");

    frm.add(pnl);
    frm.setVisible(true);
    frm.pack();
    frm.setLocationRelativeTo(null);
    frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }

  // Edit Book

  public static void editBook() {

    frm = new JFrame("Edit Book");
    pnl = new JPanel();
    pnl.setLayout(new MigLayout("wrap", "[][]", ""));

    BIDVal = new JLabel("Book ID(BID): ");
    BIDIn = new JTextField("", 15);
    BIDStatus = new JLabel("Status: ");
    bTitle = new JLabel("Title: ");
    titleIn = new JTextField("", 15);
    bAuthor = new JLabel("Author: ");
    authorIn = new JTextField("", 15);
    bGenre = new JLabel("Genre: ");
    genreIn = new JTextField("", 15);
    bQuantity = new JLabel("Quantity: ");
    quantityIn = new JTextField("", 15);
    bPubYear = new JLabel("Publish Year: ");
    pubYearIn = new JTextField("", 15);

    validateBID = new JButton("Check");
    editBook = new JButton("Edit Book!");

    final JTextField[] txtInputs = { titleIn, authorIn, genreIn, quantityIn, pubYearIn };

    for (int i = 0; i < txtInputs.length; i++) {
      txtInputs[i].setText("");
      txtInputs[i].setEnabled(false);
      editBook.setEnabled(false);
    }

    validateBID.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        Connection con = Utils.connectToDB();

        String qBID = BIDIn.getText();

        try {
          if (qBID == null || qBID.trim().equals("")) {
            for (int i = 0; i < txtInputs.length; i++) {
              txtInputs[i].setEnabled(false);
              txtInputs[i].setText("");
            }
            BIDStatus.setText("Status: Invalid");
          } else {
            String getBID = ("SELECT b_id FROM books WHERE b_id=" + qBID);
            String getBIDValues = ("SELECT * FROM books WHERE b_id=" + qBID);
            Statement pstmt = con.createStatement();
            pstmt.executeUpdate("USE library");

            ResultSet rs = pstmt.executeQuery(getBID);

            if (rs.next() == true) {
              rs = pstmt.executeQuery(getBIDValues);
              while (rs.next()) {
                for (int i = 0; i < txtInputs.length; i++) {
                  txtInputs[0].setText(rs.getString("title"));
                  txtInputs[1].setText(rs.getString("author"));
                  txtInputs[2].setText(rs.getString("genre"));
                  txtInputs[3].setText((String) rs.getString("quantity"));
                  txtInputs[4].setText(rs.getString("publishYear"));
                  txtInputs[i].setEnabled(true);
                }
              }
              BIDStatus.setText("Status: BID Exists");
              editBook.setEnabled(true);
            } else {
              for (int i = 0; i < txtInputs.length; i++) {
                txtInputs[i].setEnabled(false);
                txtInputs[i].setText("");
              }
              editBook.setEnabled(false);
              BIDIn.setText("");
              BIDStatus.setText("Status: BID not found");
            }
          }
        } catch (Exception ex) {
          JOptionPane.showMessageDialog(frm, "Input must be a number");
          ex.printStackTrace();
        }
      }
    });

    editBook.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        String qTitle = titleIn.getText();
        String qAuthor = authorIn.getText();
        String qGenre = genreIn.getText();
        String qQuantity = quantityIn.getText();
        String qPubYear = pubYearIn.getText();
        String qBIDIn = BIDIn.getText();


        Connection con = Utils.connectToDB();

        try {

          if (qTitle.trim().equals("") || qAuthor.trim().equals("") || qGenre.trim().equals("")
              || qQuantity.trim().equals("") || qPubYear.trim().equals("")) {
            JOptionPane.showMessageDialog(frm, "There's an empty field!");
          } else {

            String addQRY = "UPDATE books SET title=?, author=?, genre=?, quantity=?, publishYear=? WHERE b_id=?";
            PreparedStatement pstmt = con.prepareStatement(addQRY);
            pstmt.executeUpdate("USE library");

            pstmt.setString(1, qTitle);
            pstmt.setString(2, qAuthor);
            pstmt.setString(3, qGenre);
            pstmt.setInt(4, Integer.parseInt(qQuantity));
            pstmt.setString(5, qPubYear);
            pstmt.setInt(6, Integer.parseInt(qBIDIn));

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(frm, "Book Edited!");
          }
        } catch (Exception ex) {
          JOptionPane.showMessageDialog(frm, "Unable to edit book, try again");
          ex.printStackTrace();
        }
        for (int i = 0; i < txtInputs.length; i++) {
          txtInputs[i].setEnabled(false);
          txtInputs[i].setText("");
        }

        editBook.setEnabled(false);
        BIDIn.setText("");
        BooksTable.bookTable.setModel(BooksTable.showBooksTable());
      }
    });

    if (Utils.getTableRowNum("books") == 0) {
      BIDIn.setEnabled(false);
      validateBID.setEnabled(false);
      editBook.setEnabled(false);
      BIDStatus.setText("Status: No Books");
    }

    pnl.add(BIDVal);
    pnl.add(BIDIn);
    pnl.add(BIDStatus);
    pnl.add(validateBID, "split, right, wrap");
    pnl.add(bTitle);
    pnl.add(titleIn);
    pnl.add(bAuthor);
    pnl.add(authorIn);
    pnl.add(bGenre);
    pnl.add(genreIn);
    pnl.add(bQuantity);
    pnl.add(quantityIn);
    pnl.add(bPubYear);
    pnl.add(pubYearIn);
    pnl.add(editBook, "skip, split, right");

    frm.add(pnl);
    frm.setVisible(true);
    frm.pack();
    frm.setLocationRelativeTo(null);
    frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }

  // Remove Book
  public static void removeBook() {

    frm = new JFrame("Remove Book");
    pnl = new JPanel();
    pnl.setLayout(new MigLayout("wrap", "[][]", ""));

    bookID = new JLabel("Book ID(BID): ");
    BIDIn = new JTextField("", 15);

    remBook = new JButton("Remove Book!");
    remBook.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        String qBID = BIDIn.getText();

        Connection con = Utils.connectToDB();

        try {
          if (qBID == null || qBID.trim().equals("")) {
            JOptionPane.showMessageDialog(frm, "Input cannot be empty!");
          } else {
            String addQRY = "DELETE FROM books WHERE b_id=?";
            PreparedStatement pstmt = con.prepareStatement(addQRY);
            pstmt.executeUpdate("USE library");

            pstmt.setString(1, qBID);

            if (pstmt.executeUpdate() > 0) {
              JOptionPane.showMessageDialog(frm, "Book Removed!");
            } else {
              JOptionPane.showMessageDialog(frm, "BID doesn't exist");
            }
          }
        } catch (Exception ex) {
          JOptionPane.showMessageDialog(frm, "Unable to remove book, try again");
          ex.printStackTrace();
        }
        BIDIn.setText("");
        BooksTable.bookTable.setModel(BooksTable.showBooksTable());
      }
    });

    if (Utils.getTableRowNum("books") == 0) {
      BIDIn.setEnabled(false);
      remBook.setEnabled(false);
      BIDIn.setText("Nothing to Remove");
    }
    pnl.add(bookID);
    pnl.add(BIDIn);
    pnl.add(remBook, "skip, split, right");

    frm.add(pnl);
    frm.setVisible(true);
    frm.pack();
    frm.setLocationRelativeTo(null);
    frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }

}
