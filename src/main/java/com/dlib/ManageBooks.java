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

  private JLabel bTitle, bAuthor, bGenre, bISBN, bQuantity, bIssued, bPubYear, BIDVal, bookID;
  private JTextField titleIn, authorIn, genreIn, iSBNIn, quantityIn, issuedIn, pubYearIn;
  private JButton addBook, editBook, remBook;
  private ArrayList<JTextField> jtIns;
  private String qBID = "";

  private String[] booksColumn = { "BID", "TITLE", "AUTHOR", "GENRE", "ISBN", "QUANTITY", "ISSUED", "PUBLISH YEAR" };
  private JTextField bidIn;

  public ManageBooks() {
    super("books");
    super.setColNames(booksColumn);
  }

  // Add Book
  public JPanel addBook() {

    bTitle = new JLabel("Title: ");
    titleIn = new JTextField(15);
    bAuthor = new JLabel("Author: ");
    authorIn = new JTextField(15);
    bGenre = new JLabel("Genre: ");
    genreIn = new JTextField(15);
    bISBN = new JLabel("ISBN: ");
    iSBNIn = new JTextField(15);
    bQuantity = new JLabel("Quantity: ");
    quantityIn = new JTextField(15);
    bIssued = new JLabel("Issued: ");
    issuedIn = new JTextField("0", 15);
    issuedIn.setEditable(false);
    bPubYear = new JLabel("Publish Year: ");
    pubYearIn = new JTextField(15);
    status = new JLabel("Status: ");

    jtIns = new ArrayList<JTextField>();
    jtIns.add(titleIn);
    jtIns.add(authorIn);
    jtIns.add(genreIn);
    jtIns.add(iSBNIn);
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
          status.setText("<html>Status: <font color=green>Success</html>");
        } else {
          status.setText("<html>Status: <font color=red>Check input fields</html>");
        }

        issuedIn.setText("0");

      }
    });

    frm = new JFrame("Add Book");
    pnl = new JPanel();
    pnl.setLayout(new MigLayout("wrap", "[][][][]", ""));

    pnl.add(bTitle);
    pnl.add(bAuthor);
    pnl.add(bGenre);
    pnl.add(bISBN);

    pnl.add(titleIn);
    pnl.add(authorIn);
    pnl.add(genreIn);
    pnl.add(iSBNIn);

    pnl.add(bQuantity);
    pnl.add(bIssued);
    pnl.add(bPubYear, "wrap");

    pnl.add(quantityIn);
    pnl.add(issuedIn);
    pnl.add(pubYearIn);

    pnl.add(status, "skip,span 3");
    pnl.add(addBook, " split, right");

    return pnl;
  }

  // Edit Book
  public void editBook() {

    frm = new JFrame("Edit Book");
    pnl = new JPanel();
    pnl.setLayout(new MigLayout("wrap", "[][]", ""));

    BIDVal = new JLabel("Book ID(BID): ");
    bidIn = new JTextField(15);

    status = new JLabel("Status: ");
    bTitle = new JLabel("Title: ");
    titleIn = new JTextField(15);
    bAuthor = new JLabel("Author: ");
    authorIn = new JTextField(15);
    bGenre = new JLabel("Genre: ");
    genreIn = new JTextField(15);
    bISBN = new JLabel("ISBN: ");
    iSBNIn = new JTextField(15);
    bQuantity = new JLabel("Quantity: ");
    quantityIn = new JTextField(15);
    bIssued = new JLabel("Issued: ");
    issuedIn = new JTextField(15);
    issuedIn.setEditable(false);
    bPubYear = new JLabel("Publish Year: ");
    pubYearIn = new JTextField(15);

    editBook = new JButton("Edit Book!");

    jtIns = new ArrayList<JTextField>();
    jtIns.add(titleIn);
    jtIns.add(authorIn);
    jtIns.add(genreIn);
    jtIns.add(iSBNIn);
    jtIns.add(quantityIn);
    jtIns.add(issuedIn);
    jtIns.add(pubYearIn);

    for (int i = 0; i < jtIns.size(); i++) {
      jtIns.get(i).setText("");
      jtIns.get(i).setEditable(false);
    }
    editBook.setEnabled(false);

    bidIn.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        qBID = bidIn.getText();
        if (validateID(qBID)) {

          Map<String, String> res = selectRow(qBID);

          editBook.setEnabled(true);
          int i = 0;
          for (String str : res.values()) {
            if (issuedIn != jtIns.get(i)) {
              jtIns.get(i).setEditable(true);
            }
            jtIns.get(i).setText(str);
            i++;
          }
          status.setText("<html>Status: <font color=green>Success</html>");
        } else {
          for (int i = 0; i < jtIns.size(); i++) {
            if (issuedIn != jtIns.get(i)) {
              jtIns.get(i).setEditable(false);
              jtIns.get(i).setText("");
            }
          }
          editBook.setEnabled(false);
          status.setText("<html>Status: <font color=red>Invalid</html>");
        }

      }
    });

    editBook.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        ArrayList<String> jtTxt = new ArrayList<String>();
        for (int i = 0; i < jtIns.size(); i++) {
          jtTxt.add(jtIns.get(i).getText());
        }

        boolean res = updateRow(jtTxt, qBID);

        if (res) {
          editBook.setEnabled(true);
          status.setText("<html>Status: <font color=green>Success</html>");
        } else {
          status.setText("<html>Status: <font color=red>Check input fields</html>");
        }

        for (int i = 0; i < jtIns.size(); i++) {
          editBook.setEnabled(false);
          jtIns.get(i).setEditable(false);
          jtIns.get(i).setText("");
        }

      }
    });

    if (getTableRowNum("books") == 0) {
      editBook.setEnabled(false);
      status.setText("Status: No Books");
    }

    pnl.add(BIDVal);
    pnl.add(bidIn);
    pnl.add(bTitle);
    pnl.add(titleIn);
    pnl.add(bAuthor);
    pnl.add(authorIn);
    pnl.add(bGenre);
    pnl.add(genreIn);
    pnl.add(bISBN);
    pnl.add(iSBNIn);
    pnl.add(bQuantity);
    pnl.add(quantityIn);
    pnl.add(bIssued);
    pnl.add(issuedIn);
    pnl.add(bPubYear);
    pnl.add(pubYearIn);
    pnl.add(status, "span");
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
    bidIn = new JTextField(15);
    status = new JLabel("Status: ");

    remBook = new JButton("Remove Book!");
    remBook.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        qBID = bidIn.getText();

        if (validateID(qBID)) {

          Map<String, String> bookData = selectRow(qBID);

          int issueCount = Integer.parseInt(bookData.get("issued"));

          if (issueCount == 0) {
            status.setText("<html>Status: <font color=green>ID found</html>");
            confirm(qBID, bookData.get("title"));
          } else {
            status.setText("<html>Status: <font color=red>Book is still borrowed</html>");
          }
        } else {
          status.setText("<html>Status: <font color=red>Invalid ID</html>");
        }

      }
    });

    if (getTableRowNum("books") == 0) {
      bidIn.setEnabled(false);
      remBook.setEnabled(false);
      status.setText("Status: No books");
    }
    pnl.add(bookID);
    pnl.add(bidIn);
    pnl.add(status, "span");
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

    panel = new JPanel();

    panel.setLayout(new MigLayout("fill, insets 5 5 5 5", ""));
    panel.add(addBook(), "grow, wrap");
    panel.add(showTable(), "grow");

    return panel;

  }

  public JPanel showTable() {
    l = getTableColName();
    pickFilter = new JComboBox<String>(col);

    JLabel searchL = new JLabel("Search: ");
    searchIn = new JTextField(15);
    table = new JTable();

    table.setModel(setupTable());
    table.getTableHeader().setReorderingAllowed(false);
    table.setAutoCreateRowSorter(true);
    table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

    pane = new JScrollPane(table);

    searchFilter = new JButton("Search");
    searchFilter.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        setData(searchIn.getText(), l.get(pickFilter.getSelectedIndex()));
        table.setModel(setupTable());
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

    pnl = new JPanel();
    pnl.setLayout(new MigLayout("fill, insets 5 5 5 5", "", "[][100%][]"));
    pnl.add(searchL, "split");
    pnl.add(searchIn, "growx");
    pnl.add(pickFilter);
    pnl.add(searchFilter, "wrap");
    pnl.add(pane, "wrap, grow");
    pnl.add(editBut, "split, right");
    pnl.add(remBut);

    return pnl;
  }
}
