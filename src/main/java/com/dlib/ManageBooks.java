package com.dlib;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class ManageBooks extends TableOf {

  private JPanel pnl;
  private JFrame frm;

  private JLabel bTitle, bAuthor, bGenre, bQuantity, bIssued, bPubYear, BIDVal, bookStatus, bookID;
  private JTextField titleIn, authorIn, genreIn, quantityIn, issuedIn, pubYearIn;
  private JButton addBook, editBook, remBook;
  private JComboBox<String> BIDInPick;
  private ArrayList<JTextField> jtIns;

  private String[] booksColumn = { "BID", "TITLE", "AUTHOR", "GENRE", "QUANTITY", "ISSUED", "PUBLISH YEAR" };

  public ManageBooks() {
    super("books");
    super.setColNames(booksColumn);
  }

  // Add Book
  public void addBook() {

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

    jtIns = new ArrayList<JTextField>();
    jtIns.add(titleIn);
    jtIns.add(authorIn);
    jtIns.add(genreIn);
    jtIns.add(quantityIn);
    jtIns.add(issuedIn);
    jtIns.add(pubYearIn);

    final ArrayList<String> jtTxt = new ArrayList<String>();

    addBook = new JButton("Add Book!");
    addBook.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        for (int i = 0; i < jtIns.size(); i++) {
          jtTxt.add(jtIns.get(i).getText());
        }

        boolean res = insertRow(jtTxt);

        if (res) {
          for (int i = 0; i < jtIns.size(); i++) {
            jtIns.get(i).setText("");
          }
          bookStatus.setText("<html>Status: <font color=green>Success</html>");
        } else {
          bookStatus.setText("<html>Status: <font color=red>Check input fields</html>");
        }

        issuedIn.setText("0");

        // refresh the table
        bookJTbl.setModel(setupTable());
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
    ArrayList<String> l = getTableIDs();
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

    editBook = new JButton("Edit Book!");

    jtIns = new ArrayList<JTextField>();
    jtIns.add(titleIn);
    jtIns.add(authorIn);
    jtIns.add(genreIn);
    jtIns.add(quantityIn);
    jtIns.add(pubYearIn);

    for (int i = 0; i < jtIns.size(); i++) {
      jtIns.get(i).setText("");
      jtIns.get(i).setEditable(false);
    }
    editBook.setEnabled(false);

    BIDInPick.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        String qBID = (String) BIDInPick.getSelectedItem();
        Map<String, String> res = selectRow(qBID);
        res.remove("issued");

        if (!res.isEmpty()) {
          editBook.setEnabled(true);
          int i = 0;
          for (String str : res.values()) {
            jtIns.get(i).setText(str);
            jtIns.get(i).setEditable(true);
            i++;
          }
          bookStatus.setText("<html>Status: <font color=green>Success</html>");
        } else {
          for (int i = 0; i < jtIns.size(); i++) {
            jtIns.get(i).setEditable(false);
            jtIns.get(i).setText("");
          }
          editBook.setEnabled(false);
          bookStatus.setText("<html>Status: <font color=red>Invalid</html>");
        }
      }
    });

    editBook.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        String qBID = (String) BIDInPick.getSelectedItem();

        ArrayList<String> jtTxt = new ArrayList<String>();
        for (int i = 0; i < jtIns.size(); i++) {
          jtTxt.add(jtIns.get(i).getText());
        }

        boolean res = updateRow(jtTxt, qBID);

        if (res) {
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
        bookJTbl.setModel(setupTable());
      }
    });

    if (getTableRowNum("books") == 0) {
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
    l = getTableIDs();
    BIDInPick = new JComboBox<String>(l.toArray(new String[l.size()]));
    BIDInPick.setEditable(true);
    bookStatus = new JLabel("Status: ");

    remBook = new JButton("Remove Book!");
    remBook.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String qBID = (String) BIDInPick.getSelectedItem();

        Map<String, String> output = selectRow(qBID);

        int issueCount = Integer.parseInt(output.get("issued"));

        if (issueCount == 0) {
          boolean isDeleted = deleteRow(qBID);
          if (isDeleted) {
            bookStatus.setText("<html>Status: <font color=green>Success</html>");
          } else {
            bookStatus.setText("<html>Status: <font color=red>Unable to remove</html>");
          }
        } else {
          bookStatus.setText("<html>Status: <font color=red>Book is still borrowed</html>");
        }

        // refresh the table
        bookJTbl.setModel(setupTable());
      }
    });

    if (getTableRowNum("books") == 0) {
      BIDInPick.setEnabled(false);
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

  @Override
  public JPanel getPanel() {
    System.out.println("starting with db " + dbTable);
    bookJTbl = new JTable();

    bookJTbl.setModel(setupTable());
    bookJTbl.getTableHeader().setReorderingAllowed(false);
    bookJTbl.setAutoCreateRowSorter(true);
    bookJTbl.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

    pane = new JScrollPane(bookJTbl);

    panel = new JPanel();

    addBut = new JButton("Add");
    addBut.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        addBook();
      }
    });
    editBut = new JButton("Edit");
    editBut.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        editBook();
      }
    });
    remBut = new JButton("Remove");
    remBut.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        removeBook();
      }
    });
    panel.setLayout(new MigLayout("wrap, fill, insets 0 0 0 0", "", "[100%][]"));
    panel.add(pane, "grow");
    panel.add(addBut, "split, right");
    panel.add(editBut);
    panel.add(remBut);

    return panel;

  }
}