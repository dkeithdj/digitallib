package com.dlib;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class AdminMenu {

  private static JFrame frm;

  public static void adminPage() {
    frm = new JFrame("Admin");

    JPanel mPnl = new JPanel(new MigLayout("debug, fill, insets 0 0 0 0", "[60%]0[40%]", ""));
    JPanel rPnl = new JPanel(new MigLayout("debug, fill, insets 0 0 0 0, wrap, center", "[][]", "[90%][10%]"));

    JButton showIB = new JButton("Show Issued Books");
    showIB.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        IssuedBooksTable.issuedBooksTable();
      }
    });
    JButton backReset = new JButton("Backup and Reset");
    backReset.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
      }
    });

    if (Utils.hasDatabaseSetup() == true) {
      rPnl.add(MembersTable.membersTable(), "grow, span");
      rPnl.add(showIB);
      rPnl.add(backReset);
      mPnl.add(BooksTable.booksTable(), "grow");
      mPnl.add(rPnl, "grow, top");

      frm.add(mPnl);
      frm.setSize(1000, 500);
      frm.setVisible(true);
      frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    } else {
      InitDB.initializeDB();
    }
  }
}
