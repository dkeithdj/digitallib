package com.dlib;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import net.miginfocom.swing.MigLayout;

public class AdminMenu {

  private JFrame frm;
  private JPanel mPnl;

  private JButton manageDB;

  private ImageIcon img;
  private Image scaleImg;

  public void adminPage() {
    frm = new JFrame("Admin");
    JTabbedPane tab = new JTabbedPane();

    // gets the image and scales it appropriately
    img = new ImageIcon("digitalLibSys.png");
    scaleImg = img.getImage().getScaledInstance(1280, 720, Image.SCALE_SMOOTH);
    img = new ImageIcon(scaleImg);
    final JLabel imgLabel = new JLabel(img);
    imgLabel.setVisible(true);

    // Panel that overrides methods in order for the background image be
    // displayed
    mPnl = new JPanel(new MigLayout("fill, insets 5 5 5 5", "[][]", "[90%][][]")) {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(scaleImg, 0, 0, getWidth(), getHeight(), this);
      }

      @Override
      public Dimension getPreferredSize() {
        return new Dimension(900, 500);
      }
    };

    manageDB = new JButton("Manage Database");
    manageDB.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        new ManageDB().manDB();
      }
    });

    JLabel aa = new JLabel("<html> </html>");

    // Checks if database has already setup
    if (new Utils().hasDatabaseSetup()) {
      // creating TableOf objects to access the getPanel method
      TableOf booksTable = new ManageBooks();
      TableOf membersTable = new ManageMembers();
      TableOf issueBooksTable = new ManageIssBooks();

      mPnl.add(aa, "wrap,spanx 2");
      mPnl.add(issueBooksTable.getPanel(), "wrap,align center");
      mPnl.add(manageDB, "skip,spanx 2,right");

      tab.add(mPnl, "Issue Books");
      tab.add(booksTable.getPanel(), "Books");
      tab.add(membersTable.getPanel(), "Members");

      frm.add(tab);
      frm.setSize(900, 500);
      frm.setVisible(true);
      frm.setLocationRelativeTo(null);
      frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    } else {
      new InitDB().redirectToAdPage(); // initializes the database if the program started for the first time
    }
  }
}
