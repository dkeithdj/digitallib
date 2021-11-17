package com.dlib.manageTables;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.dlib.MembersTable;
import com.dlib.Utils;

import net.miginfocom.swing.MigLayout;

public class ManageMembers {

  private static JPanel pnl;
  private static JFrame frm;

  private static JTextField fNameIn, lNameIn, addressIn, contactIn, MIDIn;
  private static JLabel fName, lName, address, contact, MIDVal, MIDStatus, memID;

  private static JButton addMember, validateMID, editMem, remMem;

  // Add Member
  public static void addMember() {

    frm = new JFrame("Add Member");
    pnl = new JPanel();
    pnl.setLayout(new MigLayout("wrap", "[][]", ""));

    fName = new JLabel("First Name: ");
    fNameIn = new JTextField("", 15);
    lName = new JLabel("Last Name: ");
    lNameIn = new JTextField("", 15);
    address = new JLabel("Address: ");
    addressIn = new JTextField("", 15);
    contact = new JLabel("Contact Number: ");
    contactIn = new JTextField("", 15);

    addMember = new JButton("Add Member!");
    addMember.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        String qfName = fNameIn.getText();
        String qlName = lNameIn.getText();
        String qAddress = addressIn.getText();
        String qContact = contactIn.getText();

        Connection con = Utils.connectToDB();

        try {
          if (qfName.trim().equals("") || qlName.trim().equals("") || qAddress.trim().equals("")
              || qContact.trim().equals("")) {
            JOptionPane.showMessageDialog(frm, "There's an empty field!");
          } else {

            String addQRY = "INSERT INTO members(firstName,lastName,address,contact) VALUES(?,?,?,?)";
            PreparedStatement pstmt = con.prepareStatement(addQRY);
            pstmt.executeUpdate("USE library");

            pstmt.setString(1, qfName);
            pstmt.setString(2, qlName);
            pstmt.setString(3, qAddress);
            pstmt.setString(4, qContact);

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(frm, "Member Added!");
          }
        } catch (Exception ex) {
          JOptionPane.showMessageDialog(frm, "Unable to add member, try again");
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
    pnl.add(addMember, "skip, split, right");

    frm.add(pnl);
    frm.setVisible(true);
    frm.pack();
    frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }

  // Edit Member
  public static void editMember() {

    frm = new JFrame("Edit Member");
    pnl = new JPanel();
    pnl.setLayout(new MigLayout("wrap", "[][]", ""));

    MIDVal = new JLabel("Member ID(MID): ");
    MIDIn = new JTextField("", 15);
    MIDStatus = new JLabel("Status: ");
    fName = new JLabel("First Name: ");
    fNameIn = new JTextField("", 15);
    lName = new JLabel("Last Name: ");
    lNameIn = new JTextField("", 15);
    address = new JLabel("Address: ");
    addressIn = new JTextField("", 15);
    contact = new JLabel("Contact Number: ");
    contactIn = new JTextField("", 15);

    validateMID = new JButton("Check");
    editMem = new JButton("Edit Member!");

    final JTextField[] txtInputs = { fNameIn, lNameIn, addressIn, contactIn };

    for (int i = 0; i < txtInputs.length; i++) {
      txtInputs[i].setText("");
      txtInputs[i].setEnabled(false);
      editMem.setEnabled(false);
    }
    validateMID.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        Connection con = Utils.connectToDB();

        String qMID = MIDIn.getText();

        try {
          if (qMID == null || qMID.trim().equals("")) {
            for (int i = 0; i < txtInputs.length; i++) {
              txtInputs[i].setEnabled(false);
              txtInputs[i].setText("");
            }
            MIDStatus.setText("Status: Invalid");
          } else {
            String getMID = ("SELECT m_id FROM members WHERE m_id=" + qMID);
            String getMIDValues = ("SELECT * FROM members WHERE m_id=" + qMID);
            Statement pstmt = con.createStatement();
            pstmt.executeUpdate("USE library");

            ResultSet rs = pstmt.executeQuery(getMID);

            if (rs.next() == true) {
              rs = pstmt.executeQuery(getMIDValues);
              while (rs.next()) {
                for (int i = 0; i < txtInputs.length; i++) {
                  txtInputs[0].setText(rs.getString("firstName"));
                  txtInputs[1].setText(rs.getString("lastName"));
                  txtInputs[2].setText(rs.getString("address"));
                  txtInputs[3].setText(rs.getString("contact"));
                  txtInputs[i].setEnabled(true);
                }
              }
              MIDStatus.setText("Status: MID Exists");
              editMem.setEnabled(true);
            } else {
              for (int i = 0; i < txtInputs.length; i++) {
                txtInputs[i].setEnabled(false);
                txtInputs[i].setText("");
              }
              editMem.setEnabled(false);
              MIDIn.setText("");
              MIDStatus.setText("Status: MID not found");
            }
          }
        } catch (Exception ex) {
          JOptionPane.showMessageDialog(frm, "Input must be a number");
          ex.printStackTrace();
        }
      }
    });

    editMem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        String qfName = fNameIn.getText();
        String qlName = lNameIn.getText();
        String qAddress = addressIn.getText();
        String qContact = contactIn.getText();
        String qMID = MIDIn.getText();

        Connection con = Utils.connectToDB();

        try {

          if (qfName.trim().equals("") || qlName.trim().equals("") || qAddress.trim().equals("")
              || qContact.trim().equals("")) {
            JOptionPane.showMessageDialog(frm, "There's an empty field!");
          } else {

            String addQRY = "UPDATE members SET firstName=?, lastName=?, address=?,contact=? WHERE m_id=?";
            PreparedStatement pstmt = con.prepareStatement(addQRY);
            pstmt.executeUpdate("USE library");

            pstmt.setString(1, qfName);
            pstmt.setString(2, qlName);
            pstmt.setString(3, qAddress);
            pstmt.setString(4, qContact);
            pstmt.setString(5, qMID);

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(frm, "Member Edited!");
          }
        } catch (Exception ex) {
          JOptionPane.showMessageDialog(frm, "Unable to edit member, try again");
          ex.printStackTrace();
        }
        for (int i = 0; i < txtInputs.length; i++) {
          txtInputs[i].setEnabled(false);
          txtInputs[i].setText("");
        }

        editMem.setEnabled(false);
        MIDIn.setText("");
        MembersTable.memTable.setModel(MembersTable.showMembersTable());
      }
    });

    if (Utils.getTableRowNum("members") == 0) {
      MIDIn.setEnabled(false);
      validateMID.setEnabled(false);
      editMem.setEnabled(false);
      MIDStatus.setText("Status: No Members");
    }

    pnl.add(MIDVal);
    pnl.add(MIDIn);
    pnl.add(MIDStatus);
    pnl.add(validateMID, "split, right, wrap");
    pnl.add(fName);
    pnl.add(fNameIn);
    pnl.add(lName);
    pnl.add(lNameIn);
    pnl.add(address);
    pnl.add(addressIn);
    pnl.add(contact);
    pnl.add(contactIn);
    pnl.add(editMem, "skip, split, right");

    frm.add(pnl);
    frm.setVisible(true);
    frm.pack();
    frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }

  // Remove Member
  public static void removeMember() {

    frm = new JFrame("Remove Member");
    pnl = new JPanel();
    pnl.setLayout(new MigLayout("wrap", "[][]", ""));

    memID = new JLabel("Member ID(MID): ");
    MIDIn = new JTextField("", 15);

    remMem = new JButton("Remove Member!");
    remMem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        String qMID = MIDIn.getText();

        Connection con = Utils.connectToDB();

        try {
          if (qMID == null || qMID.trim().equals("")) {
            JOptionPane.showMessageDialog(frm, "Input cannot be empty!");
          } else {
            String addQRY = "DELETE FROM members WHERE m_id=?";
            PreparedStatement pstmt = con.prepareStatement(addQRY);
            pstmt.executeUpdate("USE library");

            pstmt.setString(1, qMID);

            if (pstmt.executeUpdate() > 0) {
              JOptionPane.showMessageDialog(frm, "Member Removed!");
            } else {
              JOptionPane.showMessageDialog(frm, "MID doesn't exist");
            }
          }
        } catch (Exception ex) {
          JOptionPane.showMessageDialog(frm, "Unable to remove member, try again");
          ex.printStackTrace();
        }
        MIDIn.setText("");
        MembersTable.memTable.setModel(MembersTable.showMembersTable());
      }
    });

    if (Utils.getTableRowNum("members") == 0) {
      MIDIn.setEnabled(false);
      remMem.setEnabled(false);
      MIDIn.setText("Nothing to Remove");
    }

    pnl.add(memID);
    pnl.add(MIDIn);
    pnl.add(remMem, "skip, split, right");

    frm.add(pnl);
    frm.setVisible(true);
    frm.pack();
    frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }

}
