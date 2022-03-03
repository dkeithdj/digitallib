package com.dlib.manageTables;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.dlib.BooksTable;
import com.dlib.TableOf;
import com.dlib.Utils;

import net.miginfocom.swing.MigLayout;

// TODO: Refactor stuff
public class ManageBooks {

  private JPanel pnl;
  private JFrame frm;
  private JTable tbl;

  private JLabel bTitle, bAuthor, bGenre, bQuantity, bIssued, bPubYear, BIDVal, bookStatus, bookID;
  private JTextField titleIn, authorIn, genreIn, quantityIn, issuedIn, pubYearIn;
  private JButton addBook, editBook, remBook;
  private JComboBox<String> BIDInPick;
  public TableOf manBook = new TableOf("books", BooksTable.col);
  private ArrayList<JTextField> jtIns;
  private ArrayList<String> l;

  public ArrayList<String> bookOut;
  private ArrayList<String> bookCol;
  private ArrayList<String> out;

  public void setBookTable(JTable btbl) {
    this.tbl = btbl;
  }

  public TableOf getTableData() {
    return this.manBook;
  }

  // Add Book
  public void addBook() {
    // getInputs that will send to booksTable

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
    bIssued = new JLabel("Issued: ");
    issuedIn = new JTextField("0", 15);
    issuedIn.setEditable(false);
    bPubYear = new JLabel("Publish Year: ");
    pubYearIn = new JTextField("", 15);
    bookStatus = new JLabel("Status: ");

    // select option needs to be global
    // TODO: separate logic and stuff from gui
    // TODO: only set stuff here
    // TODO: just moved some stuff outside the actionlistener
    jtIns = new ArrayList<JTextField>();
    jtIns.add(titleIn);
    jtIns.add(authorIn);
    jtIns.add(genreIn);
    jtIns.add(quantityIn);
    jtIns.add(issuedIn);
    jtIns.add(pubYearIn);

    bookCol = Utils.getTableColName("books");
    bookCol.remove(0);

    final ArrayList<String> jtTxt = new ArrayList<String>();

    addBook = new JButton("Add Book!");
    addBook.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        for (int i = 0; i < jtIns.size(); i++) {
          jtTxt.add(jtIns.get(i).getText());
        }

        out = manBook.setEdits(jtTxt, bookCol, "insert");

        if (out != null) {
          // removes issuedIn
          jtIns.remove(4);
          for (int i = 0; i < jtIns.size(); i++) {
            jtIns.get(i).setText("");
          }
          bookStatus.setText("<html>Status: <font color=green>Success</html>");
        } else {
          bookStatus.setText("<html>Status: <font color=red>Check input fields</html>");
        }

        // refresh the table
        // tbl.setModel(manBook.setupTable());
        tbl.setModel(manBook.setupTable());
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
    pnl.add(bIssued);
    pnl.add(issuedIn);
    pnl.add(bPubYear);
    pnl.add(pubYearIn);
    pnl.add(bookStatus, "span");
    pnl.add(addBook, "skip, split, right");

    frm.add(pnl);
    frm.setVisible(true);
    frm.pack();
    frm.setLocationRelativeTo(null);
    frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }

  // Edit Book

  public void editBook() {

    frm = new JFrame("Edit Book");
    pnl = new JPanel();
    pnl.setLayout(new MigLayout("debug, wrap", "[][]", ""));

    BIDVal = new JLabel("Book ID(BID): ");
    ArrayList<String> l = manBook.getTableIDs();
    BIDInPick = new JComboBox<String>(l.toArray(new String[l.size()]));
    BIDInPick.setEditable(true);

    bookStatus = new JLabel("Status: ");
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

    // validateBID = new JButton("Check");
    editBook = new JButton("Edit Book!");

    jtIns = new ArrayList<JTextField>();
    jtIns.add(titleIn);
    jtIns.add(authorIn);
    jtIns.add(genreIn);
    jtIns.add(quantityIn);
    jtIns.add(pubYearIn);

    for (int i = 0; i < jtIns.size(); i++) {
      jtIns.get(i).setText("");
      jtIns.get(i).setEnabled(false);
    }
    editBook.setEnabled(false);

    final String qBID = (String) BIDInPick.getSelectedItem();
    final ArrayList<String> bookCol = Utils.getTableColName("books");

    bookCol.remove(0);
    bookCol.remove(4);

    out = manBook.setEdits(bookCol, qBID, "select");

    // TODO: get inputs
    // qBID,bookCol
    BIDInPick.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        // System.out.println(bookCol);

        if (out != null) {
          editBook.setEnabled(true);
          for (int i = 0; i < jtIns.size(); i++) {
            jtIns.get(i).setText((out.get(i)));
            jtIns.get(i).setEnabled(true);
          }
          bookStatus.setText("<html>Status: <font color=green>Success</html>");
        } else {
          for (int i = 0; i < jtIns.size(); i++) {
            jtIns.get(i).setEditable(false);
            jtIns.get(i).setText("");
          }
          bookStatus.setText("<html>Status: <font color=red>Invalid</html>");
        }
      }
    });

    editBook.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        // ArrayList<String> bookCol = Utils.getTableColName("books");
        // bookCol.remove(0);
        // bookCol.remove(4);

        ArrayList<String> jtTxt = new ArrayList<String>();
        for (int i = 0; i < jtIns.size(); i++) {
          jtTxt.add(jtIns.get(i).getText());
        }

        // boolean out = manBook.setModTable(jtIns, bookCol, "update");
        out = manBook.setEdits(jtTxt, bookCol, "update");

        if (out != null) {
          editBook.setEnabled(true);
          bookStatus.setText("<html>Status: <font color=green>Success</html>");
        } else {
          bookStatus.setText("<html>Status: <font color=red>Check input fields</html>");
        }

        for (int i = 0; i < jtIns.size(); i++) {
          editBook.setEnabled(false);
          jtIns.get(i).setEnabled(false);
          jtIns.get(i).setText("");
        }

        // refresh the table
        tbl.setModel(manBook.setupTable());
      }
    });

    if (Utils.getTableRowNum("books") == 0) {
      editBook.setEnabled(false);
      bookStatus.setText("Status: No Books");
    }

    pnl.add(BIDVal);
    pnl.add(BIDInPick, "grow, right");
    pnl.add(bookStatus, "span, wrap");
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
  public void removeBook() {

    frm = new JFrame("Remove Book");
    pnl = new JPanel();
    pnl.setLayout(new MigLayout("wrap", "[][]", ""));

    bookID = new JLabel("Book ID(BID): ");
    l = manBook.getTableIDs();
    BIDInPick = new JComboBox<String>(l.toArray(new String[l.size()]));
    BIDInPick.setEditable(true);
    bookStatus = new JLabel("Status: ");

    remBook = new JButton("Remove Book!");
    remBook.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String qBID = (String) BIDInPick.getSelectedItem();

        // TODO: CONTINUE FINISH TOMS
        boolean isDeleted = manBook.deleteRow(qBID);

        if (isDeleted) {
          bookStatus.setText("<html>Status: <font color=green>Success</html>");
        } else {
          bookStatus.setText("<html>Status: <font color=red>Unable to remove</html>");
        }

        // refresh the table
        tbl.setModel(manBook.setupTable());
      }
    });

    if (Utils.getTableRowNum("books") == 0) {
      remBook.setEnabled(false);
      bookStatus.setText("Status: No books");
    }
    pnl.add(bookID);
    pnl.add(BIDInPick, "grow");
    pnl.add(bookStatus, "span");
    pnl.add(remBook, "skip, split, right");

    frm.add(pnl);
    frm.setVisible(true);
    frm.pack();
    frm.setLocationRelativeTo(null);
    frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }

}
