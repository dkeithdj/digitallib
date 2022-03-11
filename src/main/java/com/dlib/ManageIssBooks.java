package com.dlib;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class ManageIssBooks extends TableOf {

  private JLabel ibIBID, ibMID, ibBID, ibLName, ibBTitle, ibBrwDate, ibDuration, ibRetDate, overdued, status;
  private JTextField midIn, bidIn, lNameIn, bTitleIn, brwDateIn, durationIn, retDateIn, overduedIn;
  private JButton retBook, issBook, remIssBook;
  private TableOf manMem = new ManageMembers();
  private static TableOf manBook = new ManageBooks();
  private ArrayList<String> l;

  private ArrayList<JTextField> jtIns;
  private ArrayList<String> jtTxts;
  public String issueBooksColumn[] = { "IBID", "MID", "BID", "LAST NAME", "BOOK TITLE", "DATE ISSUED", "DURATION",
      "DATE RETURNED", "OVERDUED" };
  private JTextField ibidIn;

  public ManageIssBooks() {
    super("issuedBooks");
    super.setColNames(issueBooksColumn);
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

    ibMID = new JLabel("Member ID(MID): ");
    midIn = new JTextField(15);
    ibBID = new JLabel("Book ID(BID): ");
    bidIn = new JTextField(15);
    ibDuration = new JLabel("Days to borrow: ");
    durationIn = new JTextField(15);
    ibBrwDate = new JLabel("Borrow Date: ");
    brwDateIn = new JTextField(15);
    brwDateIn.setEditable(false);
    status = new JLabel("Status: ");

    brwDateIn.setText(getDateToday());

    issBook = new JButton("Issue Book!");
    issBook.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        String sqMID = midIn.getText();
        String sqBID = bidIn.getText();
        String sqDuration = durationIn.getText();

        if (validateID(sqBID) && validateID(sqMID)) {

          Map<String, String> upMem = manMem.selectRow(sqMID);
          Map<String, String> upBook = manBook.selectRow(sqBID);

          jtTxts = new ArrayList<String>();
          jtTxts.add(sqMID);
          jtTxts.add(sqBID);
          jtTxts.add(upMem.get("lastName"));
          jtTxts.add(upBook.get("title"));
          jtTxts.add(getDateToday());
          jtTxts.add(sqDuration);
          jtTxts.add("-");
          jtTxts.add("-");

          int quantCount = Integer.parseInt(upBook.get("quantity"));
          int issueCount = Integer.parseInt(upBook.get("issued"));

          if (quantCount != 0) {

            insertRow(jtTxts);
            quantCount -= 1;
            issueCount += 1;
            upBook.replace("quantity", "" + quantCount);
            upBook.replace("issued", "" + issueCount);

            ArrayList<String> valL = convertToList(upBook);

            manBook.updateRow(valL, sqBID);
          }

          midIn.setText("");
          bidIn.setText("");
          durationIn.setText("");

          status.setText("<html>Status: <font color=green>Book Issued</html>");
        } else {
          status.setText("<html>Status: <font color=red> Check ID values</html>");
        }

      }
    });

    frm = new JFrame("Issue Book");
    pnl = new JPanel();

    pnl.setLayout(new MigLayout("wrap", "[][]", ""));

    pnl.add(ibMID);
    pnl.add(ibBID);
    pnl.add(midIn);
    pnl.add(bidIn);
    pnl.add(ibDuration);
    pnl.add(ibBrwDate);
    pnl.add(durationIn);
    pnl.add(brwDateIn);
    pnl.add(status, "span");
    pnl.add(issBook, "skip,split, right");
    pnl.setBorder(BorderFactory.createTitledBorder("Issue Books"));

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
    ibidIn = new JTextField(15);

    ibMID = new JLabel("Member ID(MID): ");
    midIn = new JTextField(15);
    ibBID = new JLabel("Book ID(BID): ");
    bidIn = new JTextField(15);
    ibLName = new JLabel("Last name: ");
    lNameIn = new JTextField(15);
    ibBTitle = new JLabel("Book title: ");
    bTitleIn = new JTextField(15);
    ibBrwDate = new JLabel("Borrow Date: ");
    brwDateIn = new JTextField(15);
    ibDuration = new JLabel("Days to borrow: ");
    durationIn = new JTextField(15);
    ibRetDate = new JLabel("Return Date: ");
    retDateIn = new JTextField(15);
    overdued = new JLabel("Overdued: ");
    overduedIn = new JTextField(15);
    status = new JLabel("Status: ");

    retBook = new JButton("Return Book!");

    jtIns = new ArrayList<JTextField>();
    jtIns.add(midIn);
    jtIns.add(bidIn);
    jtIns.add(lNameIn);
    jtIns.add(bTitleIn);
    jtIns.add(brwDateIn);
    jtIns.add(durationIn);
    jtIns.add(retDateIn);
    jtIns.add(overduedIn);

    retDateIn.setText(getDateToday());

    for (int i = 0; i < jtIns.size(); i++) {
      jtIns.get(i).setEditable(false);
    }
    retBook.setEnabled(false);

    ibidIn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String qIBID = ibidIn.getText();

        if (validateID(qIBID)) {

          Map<String, String> hashOut = selectRow(qIBID);

          String isOverdued = isOverdue(Integer.parseInt(hashOut.get("borrowPeriod")), hashOut.get("issuedDate"),
              getDateToday());

          hashOut.replace("returnDate", getDateToday());
          hashOut.replace("overdued", isOverdued);
          System.out.println(hashOut);

          retBook.setEnabled(true);
          int i = 0;
          for (String str : hashOut.values()) {
            jtIns.get(i).setText(str);
            i++;
          }
          status.setText("<html>Status: <font color=green>ID found</html>");

        } else {
          for (int i = 0; i < jtIns.size(); i++) {
            jtIns.get(i).setEditable(false);
            jtIns.get(i).setText("");
          }
          retBook.setEnabled(false);
          status.setText("<html>Status: <font color=red>Invalid</html>");
        }
      }
    });

    retBook.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        String qIBID = ibidIn.getText();

        ArrayList<String> jtTxt = new ArrayList<String>();
        for (int i = 0; i < jtIns.size(); i++) {
          jtTxt.add(jtIns.get(i).getText());
        }

        Map<String, String> ibRes = selectRow(qIBID);
        Map<String, String> bookRes = manBook.selectRow(bidIn.getText());

        int quantCount = Integer.parseInt(bookRes.get("quantity"));
        int issueCount = Integer.parseInt(bookRes.get("issued"));

        if (ibRes.get("overdued").equals("-")) {
          updateRow(jtTxt, qIBID);
          quantCount += 1;
          issueCount -= 1;
          bookRes.replace("quantity", "" + quantCount);
          bookRes.replace("issued", "" + issueCount);

          ArrayList<String> valL = convertToList(bookRes);

          manBook.updateRow(valL, bidIn.getText());

          status.setText("<html>Status: <font color=green>Book returned</html>");
        } else {
          status.setText("<html>Status: <font color=red>Book already returned</html>");
        }

        for (int i = 0; i < jtIns.size(); i++) {
          jtIns.get(i).setEditable(false);
          jtIns.get(i).setText("");
        }
        retBook.setEnabled(false);

      }
    });

    if (getTableRowNum("issuedBooks") == 0) {
      retBook.setEnabled(false);
      retDateIn.setText("");
      status.setText("Status: Nothing to return");
    }

    pnl.add(ibIBID);
    pnl.add(ibidIn);
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
    pnl.add(overdued);
    pnl.add(overduedIn);
    pnl.add(status, "span");
    pnl.add(retBook, "skip, split, right");

    frm.add(pnl);
    frm.setVisible(true);
    frm.pack();
    frm.setLocationRelativeTo(null);
    frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }

  public void remIssBook() {

    ibIBID = new JLabel("Issued Book IBID(IBID): ");
    ibidIn = new JTextField(15);

    status = new JLabel("Status: ");

    remIssBook = new JButton("Remove Issued Book!");
    remIssBook.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        String qIBID = ibidIn.getText();

        if (validateID(qIBID)) {

          Map<String, String> ibData = selectRow(qIBID);

          if (!ibData.get("overdued").equals("-")) {
            confirm(qIBID, ibData.get("returnDate"));
            status.setText("<html>Status: <font color=green>ID found</html>");
          } else {
            status.setText("<html>Status: <font color=red>Book not yet returned</html>");
          }
          ibidIn.setText("");
        } else {
          status.setText("<html>Status: <font color=red>Invalid ID</html>");
        }

      }
    });
    if (getTableRowNum("issuedBooks") == 0) {
      ibidIn.setEnabled(false);
      remIssBook.setEnabled(false);
      status.setText("Status: No Issued Books");
    }

    frm = new JFrame("Remove Book");
    pnl = new JPanel();
    pnl.setLayout(new MigLayout("wrap", "[][]", ""));

    pnl.add(ibIBID);
    pnl.add(ibidIn);

    pnl.add(status, "span");
    pnl.add(remIssBook, "skip, split, right");

    frm.add(pnl);
    frm.setVisible(true);
    frm.pack();
    frm.setLocationRelativeTo(null);
    frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }

  public void showTable() {
    l = getTableColName();
    pickFilter = new JComboBox<String>(col);

    JLabel searchL = new JLabel("Search: ");
    final JTextField jtt = new JTextField(15);
    table = new JTable();

    table.setModel(setupTable());
    table.getTableHeader().setReorderingAllowed(false);
    table.setAutoCreateRowSorter(true);
    table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

    pane = new JScrollPane(table);

    searchFilter = new JButton("Search");
    searchFilter.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        setData(jtt.getText(), l.get(pickFilter.getSelectedIndex()));
        table.setModel(setupTable());
      }
    });
    remBut = new JButton("Remove");
    remBut.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        remIssBook();
      }
    });

    frm = new JFrame("Issue Books Table");
    pnl = new JPanel();
    pnl.setLayout(new MigLayout("fill, insets 5 5 5 5", "", "[][100%][]"));
    pnl.add(searchL, "split");
    pnl.add(jtt, "growx");
    pnl.add(pickFilter);
    pnl.add(searchFilter, "wrap");
    pnl.add(pane, "wrap, grow");
    pnl.add(remBut, "right");

    frm.add(pnl);
    frm.setVisible(true);
    frm.setSize(550, 500);
    frm.setLocationRelativeTo(null);
    frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }

  @Override
  public JPanel getPanel() {
    System.out.println("starting with db " + dbTable);

    panel = new JPanel();

    addBut = new JButton("Issue");
    addBut.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        issueBook();
      }
    });
    editBut = new JButton("Return");
    editBut.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        returnBook();
      }
    });
    showTbl = new JButton("Show Table");
    showTbl.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        showTable();
      }
    });
    panel.setLayout(new MigLayout("fill, insets 0", ""));
    panel.add(addBut, "split, right");
    panel.add(editBut);
    panel.add(showTbl);
    panel.setBackground(Color.decode("#FCF3E4"));

    return panel;

  }
}
