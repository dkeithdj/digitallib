package com.dlib;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class AdminMenu {

  private static JFrame frm;
  private static JPanel mPnl;

  private static JButton showIB, manageDB;

  public static void adminPage() {
    frm = new JFrame("Admin");

    mPnl = new JPanel(new MigLayout("fill, insets 5 5 5 5", "[60%][40%]", "[100%][]"));

    showIB = new JButton("Show Issued Books");
    showIB.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        IssuedBooksTable.issuedBooksTable();
      }
    });
    manageDB = new JButton("Manage Database");
    manageDB.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ManageDB.manDB();
      }
    });

    if (Utils.hasDatabaseSetup() == true) {
      mPnl.add(BooksTable.booksTable(), "spany 2, grow");
      mPnl.add(MembersTable.membersTable(), "wrap, grow");
      mPnl.add(showIB, "split, right");
      mPnl.add(manageDB);

      frm.add(mPnl);
      frm.setSize(1000, 500);
      frm.setVisible(true);
      frm.setLocationRelativeTo(null);
      frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    } else {
      InitDB.redirectToAdPage();
    }
  }
}
