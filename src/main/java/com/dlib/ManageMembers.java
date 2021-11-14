package com.dlib;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.Bidi;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.xml.namespace.QName;

import net.miginfocom.swing.MigLayout;

public class ManageMembers {

  private static JPanel pnl;
  private static JFrame frm;

  private static JTextField fNameIn, lNameIn, addressIn, contactIn;

  public static void addMember() {

    frm = new JFrame("Add Member");
    pnl = new JPanel();
    pnl.setLayout(new MigLayout("wrap", "[][]", ""));

    JLabel fName = new JLabel("First Name: ");
    fNameIn = new JTextField("", 15);
    JLabel lName = new JLabel("Last Name: ");
    lNameIn = new JTextField("", 15);
    JLabel address = new JLabel("Address: ");
    addressIn = new JTextField("", 15);
    JLabel contact = new JLabel("Contact Number: ");
    contactIn = new JTextField("", 15);

    JButton addBook = new JButton("Add Member!");
    addBook.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        Connection con = Utils.connectToDB();

        try {
          String qfName = fNameIn.getText();
          String qlName = lNameIn.getText();
          String qAddress = addressIn.getText();
          String qContact = contactIn.getText();
          if (qfName.equals("") || qlName.equals("") || qAddress.equals("") || qContact.equals("")) {
            JOptionPane.showMessageDialog(null, "There's an empty field!");
          } else {

            String addQRY = "INSERT INTO members(firstName,lastName,address,contact) VALUES(?,?,?,?)";
            PreparedStatement pstmt = con.prepareStatement(addQRY);
            pstmt.executeUpdate("USE library");

            pstmt.setString(1, qfName);
            pstmt.setString(2, qlName);
            pstmt.setString(3, qAddress);
            pstmt.setString(4, qContact);

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Member Added!");

          }

        } catch (Exception ex) {
          ex.printStackTrace();
        }
        fNameIn.setText("");
        lNameIn.setText("");
        addressIn.setText("");
        contactIn.setText("");
        MembersTable.memTable.setModel(MembersTable.showMembersTable());
      }
    });

    pnl.add(fName);
    pnl.add(fNameIn);
    pnl.add(lName);
    pnl.add(lNameIn);
    pnl.add(address);
    pnl.add(addressIn);
    pnl.add(contact);
    pnl.add(contactIn);
    pnl.add(addBook, "skip, split, right");

    frm.add(pnl);
    frm.setSize(350, 400);
    frm.setVisible(true);
    frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }

  private static JTextField MIDIn;

  public static void removeMember() {

    frm = new JFrame("Remove Member");
    pnl = new JPanel();
    pnl.setLayout(new MigLayout("debug, wrap", "[][]", ""));

    JLabel memID = new JLabel("Member ID(MID): ");
    MIDIn = new JTextField("", 15);
    JButton remMem = new JButton("Remove Member!");
    remMem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        Connection con = Utils.connectToDB();

        try {
          String qMID = MIDIn.getText();
          if (qMID == null || qMID.equals("")) {
            JOptionPane.showMessageDialog(null, "Input cannot be empty!");
          } else {
            String addQRY = "DELETE FROM members WHERE m_id=?";
            PreparedStatement pstmt = con.prepareStatement(addQRY);
            pstmt.executeUpdate("USE library");

            pstmt.setString(1, qMID);

            if (pstmt.executeUpdate() > 0) {
              JOptionPane.showMessageDialog(null, "Member Removed!");
            } else {
              JOptionPane.showMessageDialog(null, "MID doesn't exist");
            }
          }
        } catch (Exception ex) {
          ex.printStackTrace();
        }
        MIDIn.setText("");
        MembersTable.memTable.setModel(MembersTable.showMembersTable());
      }
    });

    pnl.add(memID);
    pnl.add(MIDIn);
    pnl.add(remMem, "skip, split, right");

    frm.add(pnl);
    frm.setSize(350, 400);
    frm.setVisible(true);
    frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }

  public static void editMember() {

  }

}
