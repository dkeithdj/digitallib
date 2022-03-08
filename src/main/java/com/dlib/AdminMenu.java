package com.dlib;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.dlib.manageTables.ManageBooks;
import com.dlib.manageTables.ManageIssBooks;
import com.dlib.manageTables.ManageMembers;

import net.miginfocom.swing.MigLayout;

public class AdminMenu {

  private JFrame frm;
  private JPanel mPnl;

  private JButton showIB, manageDB;

  public void adminPage() {
    frm = new JFrame("Admin");
    JTabbedPane tab = new JTabbedPane();

    mPnl = new JPanel(new MigLayout("fill, insets 5 5 5 5", "[60%][40%]", "[100%][]"));

    // showIB = new JButton("Show Issued Books");
    // showIB.addActionListener(new ActionListener() {
    // public void actionPerformed(ActionEvent e) {
    // IssuedBooksTable.issuedBooksTable();
    // }
    // });
    manageDB = new JButton("Manage Database");
    manageDB.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        new ManageDB().manDB();
      }
    });

    if (Utils.hasDatabaseSetup()) {
      TableOf booksTable = new ManageBooks();
      TableOf membersTable = new ManageMembers();
      TableOf issueBooksTable = new ManageIssBooks();
      mPnl.add(booksTable.getPanel(), "spany 2, grow");
      mPnl.add(membersTable.getPanel(), "wrap, grow");
      // mPnl.add(BooksTable.booksTable(), "spany 2, grow");
      // mPnl.add(MembersTable.membersTable(), "wrap, grow");
      mPnl.add(manageDB, "split, right");
      // mPnl.add(manageDB);

      tab.add(mPnl, "Books and Members");
      // tab.add(IssuedBooksTable.issuedBooksTable(), "Issued Books");
      tab.add(issueBooksTable.getPanel(), "Issued Books");
      frm.add(tab);
      frm.setSize(1000, 500);
      frm.setVisible(true);
      frm.setLocationRelativeTo(null);
      frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    } else {
      new InitDB().redirectToAdPage();
    }
  }
}
