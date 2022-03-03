package com.dlib.manageTables;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.dlib.MembersTable;
import com.dlib.TableOf;
import com.dlib.Utils;

import net.miginfocom.swing.MigLayout;

public class ManageMembers {

  private JPanel pnl;
  private JFrame frm;

  private JTextField fNameIn, lNameIn, addressIn, contactIn;
  private JLabel fName, lName, address, contact, MIDVal, memStatus, memID;
  private JComboBox<String> MIDInPick;

  private JButton addMember, editMem, remMem;
  private MembersTable mTable = new MembersTable();
  private TableOf manMem = mTable.getTableData();
  private ArrayList<String> l;
  private ArrayList<JTextField> jtIns;
  // private JTable upMemTable = mTable.getTbl();
  private JTable tbl;

  private ArrayList<String> out;

  public void setMemTable(JTable btbl) {
    this.tbl = btbl;
  }

  // private void refTable() {
  // mTable.getTbl().setModel(manMem.setupTable());
  // System.out.println("refreshed");
  // }

  // Add Member
  public void addMember() {

    frm = new JFrame("Add Member");
    pnl = new JPanel();
    pnl.setLayout(new MigLayout("debug, wrap", "[][]", ""));

    fName = new JLabel("First Name: ");
    fNameIn = new JTextField("", 15);
    lName = new JLabel("Last Name: ");
    lNameIn = new JTextField("", 15);
    address = new JLabel("Address: ");
    addressIn = new JTextField("", 15);
    contact = new JLabel("Contact Number: ");
    contactIn = new JTextField("", 15);
    memStatus = new JLabel("Status: ");

    addMember = new JButton("Add Member!");
    addMember.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jtIns = new ArrayList<JTextField>();
        jtIns.add(fNameIn);
        jtIns.add(lNameIn);
        jtIns.add(addressIn);
        jtIns.add(contactIn);

        ArrayList<String> memCol = Utils.getTableColName("members");
        memCol.remove(0);

        ArrayList<String> jtTxt = new ArrayList<String>();
        for (int i = 0; i < jtIns.size(); i++) {
          jtTxt.add(jtIns.get(i).getText());
        }

        out = manMem.setEdits(jtTxt, memCol, "insert");

        if (out != null) {
          for (int i = 0; i < jtIns.size(); i++) {
            jtIns.get(i).setText("");
          }
          memStatus.setText("<html>Status: <font color=green>Success</html>");
        } else {
          memStatus.setText("<html>Status: <font color=red>Check input fields</html>");
        }

        // refresh table
        // new MembersTable().memTable.setModel(manMem.setupTable());
        // manMem.getTable().setModel(manMem.setupTable());
        tbl.setModel(manMem.setupTable());
        // mTable.refreshTable();
        // refTable();
        // upMemTable.setModel(manMem.setupTable());
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
    pnl.add(memStatus, "span");
    pnl.add(addMember, "skip, split, right");

    frm.add(pnl);
    frm.setVisible(true);
    frm.pack();
    frm.setLocationRelativeTo(null);
    frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }

  // Edit Member
  public void editMember() {

    frm = new JFrame("Edit Member");
    pnl = new JPanel();
    pnl.setLayout(new MigLayout("debug,wrap", "[][]", ""));

    MIDVal = new JLabel("Member ID(MID): ");

    l = manMem.getTableIDs();
    MIDInPick = new JComboBox<String>(l.toArray(new String[l.size()]));
    MIDInPick.setEditable(true);

    memStatus = new JLabel("Status: ");
    fName = new JLabel("First Name: ");
    fNameIn = new JTextField("", 15);
    lName = new JLabel("Last Name: ");
    lNameIn = new JTextField("", 15);
    address = new JLabel("Address: ");
    addressIn = new JTextField("", 15);
    contact = new JLabel("Contact Number: ");
    contactIn = new JTextField("", 15);

    editMem = new JButton("Edit Member!");

    // TODO: change this
    final JTextField[] txtInputs = { fNameIn, lNameIn, addressIn, contactIn };

    for (int i = 0; i < txtInputs.length; i++) {
      txtInputs[i].setText("");
      txtInputs[i].setEnabled(false);
      editMem.setEnabled(false);
    }
    MIDInPick.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        // changes
        ArrayList<String> memCol = Utils.getTableColName("members");
        memCol.remove(0);

        jtIns = new ArrayList<JTextField>();
        jtIns.add(fNameIn);
        jtIns.add(lNameIn);
        jtIns.add(addressIn);
        jtIns.add(contactIn);

        String qMID = (String) MIDInPick.getSelectedItem();

        out = manMem.setEdits(memCol, qMID, "select");

        if (out != null) {
          editMem.setEnabled(true);
          for (int i = 0; i < jtIns.size(); i++) {
            jtIns.get(i).setText((out.get(i)));
            jtIns.get(i).setEnabled(true);
          }

          memStatus.setText("<html>Status: <font color=green>Success</html>");
        } else {
          for (int i = 0; i < jtIns.size(); i++) {
            jtIns.get(i).setEditable(false);
            jtIns.get(i).setText("");
            editMem.setEnabled(false);
          }
          memStatus.setText("<html>Status: <font color=red>Invalid</html>");
        }
      }
    });

    editMem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        ArrayList<String> memCol = Utils.getTableColName("members");
        memCol.remove(0);

        jtIns = new ArrayList<JTextField>();
        jtIns.add(fNameIn);
        jtIns.add(lNameIn);
        jtIns.add(addressIn);
        jtIns.add(contactIn);

        ArrayList<String> jtTxt = new ArrayList<String>();
        for (int i = 0; i < jtIns.size(); i++) {
          jtTxt.add(jtIns.get(i).getText());
        }

        out = manMem.setEdits(jtTxt, memCol, "update");

        if (out != null) {
          editMem.setEnabled(true);
          memStatus.setText("<html>Status: <font color=green>Success</html>");
        } else {
          memStatus.setText("<html>Status: <font color=red>Check input fields</html>");
        }

        for (int i = 0; i < jtIns.size(); i++) {
          editMem.setEnabled(false);
          jtIns.get(i).setEnabled(false);
          jtIns.get(i).setText("");
        }

        // refresh table
        // new MembersTable().memTable.setModel(manMem.setupTable());
        // refTable();
        // upMemTable.setModel(manMem.setupTable());
        // refTable();
        tbl.setModel(manMem.setupTable());
        // mTable.refTbl().setModel(manMem.setupTable());
      }
    });

    if (Utils.getTableRowNum("members") == 0) {
      editMem.setEnabled(false);
      memStatus.setText("Status: No Members");
    }

    pnl.add(MIDVal);
    pnl.add(MIDInPick, "grow, right");
    pnl.add(memStatus, "span");
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
    frm.setLocationRelativeTo(null);
    frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }

  // Remove Member
  public void removeMember() {

    frm = new JFrame("Remove Member");
    pnl = new JPanel();
    pnl.setLayout(new MigLayout("wrap", "[][]", ""));

    memID = new JLabel("Member ID(MID): ");
    l = manMem.getTableIDs();
    MIDInPick = new JComboBox<String>(l.toArray(new String[l.size()]));
    MIDInPick.setEditable(true);
    memStatus = new JLabel("Status: ");

    remMem = new JButton("Remove Member!");
    remMem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        String qMID = (String) MIDInPick.getSelectedItem();

        ArrayList<String> memCol = Utils.getTableColName("members");
        memCol.remove(0);

        out = manMem.setEdits(memCol, qMID, "delete");

        if (out != null) {
          memStatus.setText("<html>Status: <font color=green>Success</html>");
        } else {
          memStatus.setText("<html>Status: <font color=red>Unable to remove</html>");
        }

        // refresh table
        // MembersTable.memTable.setModel(manMem.setupTable());
        tbl.setModel(manMem.setupTable());
      }
    });

    if (Utils.getTableRowNum("members") == 0) {
      remMem.setEnabled(false);
      memStatus.setText("Status: No members");
    }

    pnl.add(memID);
    pnl.add(MIDInPick, "grow");
    pnl.add(memStatus, "span");
    pnl.add(remMem, "skip, split, right");

    frm.add(pnl);
    frm.setVisible(true);
    frm.pack();
    frm.setLocationRelativeTo(null);
    frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }

}
