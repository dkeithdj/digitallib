package com.dlib;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

public class ManageIssBooks extends TableOf {

  private JPanel pnl;
  private JFrame frm;

  private JLabel ibIBID, ibMID, ibBID, ibLName, ibBTitle, ibBrwDate, ibDuration, ibRetDate, overdued, issueStatus;
  private JTextField midIn, bidIn, lNameIn, bTitleIn, brwDateIn, durationIn, retDateIn, overduedIn;
  private JButton retBook, issBook, remIssBook;
  private JComboBox<String> midInPick, bidInPick, ibidInPick;
  private TableOf manMem = new ManageMembers();
  private static TableOf manBook = new ManageBooks();
  private ArrayList<String> l;

  private ArrayList<JTextField> jtIns;
  private ArrayList<String> jtTxts;
  public String issueBooksColumn[] = { "IBID", "MID", "BID", "LAST NAME", "BOOK TITLE", "DATE ISSUED", "DURATION",
      "DATE RETURNED", "OVERDUED" };

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

    ibBrwDate.setText("Borrow Date: " + getDateToday());

    issBook = new JButton("Issue Book!");
    issBook.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        String sqMID = (String) midInPick.getSelectedItem();
        String sqBID = (String) bidInPick.getSelectedItem();
        String sqDuration = durationIn.getText();

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
        // add stuff

        int quantCount = Integer.parseInt(upBook.get("quantity"));
        int issueCount = Integer.parseInt(upBook.get("issued"));

        if (!upMem.isEmpty() || !upBook.isEmpty()) {
          if (quantCount != 0) {

            insertRow(jtTxts);
            quantCount -= 1;
            issueCount += 1;
            upBook.replace("quantity", "" + quantCount);
            upBook.replace("issued", "" + issueCount);

            ArrayList<String> valL = convertToList(upBook);

            manBook.updateRow(valL, sqBID);
          }
        }

        durationIn.setText("");

        // refresh the table
        // bookJTbl.setModel(manBook.setupTable());
        // issbookJTbl.setModel(setupTable());
        refreshTable();

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
    l = getTableIDs();
    ibidInPick = new JComboBox<String>(l.toArray(new String[l.size()]));
    ibidInPick.setEditable(true);

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
    overdued = new JLabel("Overdued: ");
    overduedIn = new JTextField("", 15);
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
    jtIns.add(overduedIn);

    retDateIn.setText(getDateToday());

    for (int i = 0; i < jtIns.size(); i++) {
      jtIns.get(i).setEditable(false);
    }
    retBook.setEnabled(false);

    ibidInPick.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String qIBID = (String) ibidInPick.getSelectedItem();

        Map<String, String> hashOut = selectRow(qIBID);

        String isOverdued = isOverdue(Integer.parseInt(hashOut.get("borrowPeriod")), hashOut.get("issuedDate"),
            getDateToday());

        hashOut.replace("returnDate", getDateToday());
        hashOut.replace("overdued", isOverdued);
        System.out.println(hashOut);

        if (!hashOut.isEmpty()) {
          retBook.setEnabled(true);
          int i = 0;
          for (String str : hashOut.values()) {
            jtIns.get(i).setText(str);
            i++;
          }
          issueStatus.setText("<html>Status: <font color=green>ID found</html>");
        } else {
          issueStatus.setText("<html>Status: <font color=red>Invalid</html>");
        }
      }
    });

    retBook.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        String qIBID = (String) ibidInPick.getSelectedItem();

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

          issueStatus.setText("<html>Status: <font color=green>Book returned</html>");
        } else {
          issueStatus.setText("<html>Status: <font color=red>Book already returned</html>");
        }

        for (int i = 0; i < jtIns.size(); i++) {
          jtIns.get(i).setEditable(false);
          jtIns.get(i).setText("");
        }
        retBook.setEnabled(false);

        // refresh the table
        // ManageBooks.bookJTbl.setModel(manBook.setupTable());
        // issbookJTbl.setModel(setupTable());
        refreshTable();
      }
    });

    if (getTableRowNum("issuedBooks") == 0) {
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
    pnl.add(overdued);
    pnl.add(overduedIn);
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
    l = getTableIDs();
    ibidInPick = new JComboBox<String>(l.toArray(new String[l.size()]));
    ibidInPick.setEditable(true);

    issueStatus = new JLabel("Status: ");

    remIssBook = new JButton("Remove Issued Book!");
    remIssBook.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        String qIBID = (String) ibidInPick.getSelectedItem();

        Map<String, String> ibData = selectRow(qIBID);

        if (!ibData.get("overdued").equals("-")) {
          deleteRow(qIBID);
          if (!ibData.isEmpty()) {
            issueStatus.setText("<html>Status: <font color=green>Success</html>");
          } else {
            issueStatus.setText("<html>Status: <font color=red>Unable to remove</html>");
          }
        } else {
          issueStatus.setText("<html>Status: <font color=red>Book not yet returned</html>");
        }

        // refresh the table
        issbookJTbl.setModel(setupTable());
      }
    });
    if (getTableRowNum("issuedBooks") == 0) {
      ibidInPick.setEnabled(false);
      remIssBook.setEnabled(false);
      issueStatus.setText("Status: No Issued Books");
    }

    pnl.add(ibIBID);
    pnl.add(ibidInPick, "grow");
    pnl.add(issueStatus, "span");
    pnl.add(remIssBook, "skip, split, right");

    frm.add(pnl);
    frm.setVisible(true);
    frm.pack();
    frm.setLocationRelativeTo(null);
    frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }

  @Override
  public JPanel getPanel() {
    System.out.println("starting with db " + dbTable);
    issbookJTbl = new JTable();

    issbookJTbl.setModel(setupTable());
    issbookJTbl.getTableHeader().setReorderingAllowed(false);
    issbookJTbl.setAutoCreateRowSorter(true);
    issbookJTbl.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

    pane = new JScrollPane(issbookJTbl);

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
    remBut = new JButton("Remove");
    remBut.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        remIssBook();
      }
    });
    panel.setLayout(new MigLayout("fill, insets 5 5 5 5", "", "[100%][]"));
    panel.add(pane, "wrap, grow");
    panel.add(addBut, "split, right");
    panel.add(editBut);
    panel.add(remBut);

    return panel;

  }
}
