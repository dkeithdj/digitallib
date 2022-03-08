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

public class ManageMembers extends TableOf {

  private JPanel pnl;
  private JFrame frm;

  private JTextField fNameIn, lNameIn, addressIn, contactIn;
  private JLabel fName, lName, address, contact, MIDVal, memStatus, memID;
  private JComboBox<String> MIDInPick;

  private JButton addMember, editMem, remMem;
  private ArrayList<JTextField> jtIns;
  private String[] membersColumn = { "MID", "FIRST NAME", "LAST NAME", "ADDRESS", "CONTACT" };

  public ManageMembers() {
    super("members");
    super.setColNames(membersColumn);
  }

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

    jtIns = new ArrayList<JTextField>();
    jtIns.add(fNameIn);
    jtIns.add(lNameIn);
    jtIns.add(addressIn);
    jtIns.add(contactIn);

    final ArrayList<String> jtTxt = new ArrayList<String>();

    addMember = new JButton("Add Member!");
    addMember.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        for (int i = 0; i < jtIns.size(); i++) {
          jtTxt.add(jtIns.get(i).getText());
        }
        boolean res = insertRow(jtTxt);

        if (res) {
          for (int i = 0; i < jtIns.size(); i++) {
            jtIns.get(i).setText("");
          }
          memStatus.setText("<html>Status: <font color=green>Success</html>");
        } else {
          memStatus.setText("<html>Status: <font color=red>Check input fields</html>");
        }

        // refresh table
        memJTbl.setModel(setupTable());
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

    l = getTableIDs();
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

    jtIns = new ArrayList<JTextField>();
    jtIns.add(fNameIn);
    jtIns.add(lNameIn);
    jtIns.add(addressIn);
    jtIns.add(contactIn);

    for (int i = 0; i < jtIns.size(); i++) {
      jtIns.get(i).setText("");
      jtIns.get(i).setEditable(false);
    }
    editMem.setEnabled(false);

    MIDInPick.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        String qMID = (String) MIDInPick.getSelectedItem();

        Map<String, String> res = selectRow(qMID);

        if (res != null) {
          editMem.setEnabled(true);
          int i = 0;
          for (String str : res.values()) {
            jtIns.get(i).setText(str);
            jtIns.get(i).setEditable(true);
            i++;
          }
          memStatus.setText("<html>Status: <font color=green>Success</html>");
        } else {
          for (int i = 0; i < jtIns.size(); i++) {
            jtIns.get(i).setEditable(false);
            jtIns.get(i).setText("");
          }
          editMem.setEnabled(false);
          memStatus.setText("<html>Status: <font color=red>Invalid</html>");
        }
      }
    });

    editMem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        String qMID = (String) MIDInPick.getSelectedItem();

        ArrayList<String> jtTxt = new ArrayList<String>();
        for (int i = 0; i < jtIns.size(); i++) {
          jtTxt.add(jtIns.get(i).getText());
        }

        boolean res = updateRow(jtTxt, qMID);

        if (res) {
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
        memJTbl.setModel(setupTable());
      }
    });

    if (getTableRowNum("members") == 0) {
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
    l = getTableIDs();
    MIDInPick = new JComboBox<String>(l.toArray(new String[l.size()]));
    MIDInPick.setEditable(true);
    memStatus = new JLabel("Status: ");

    remMem = new JButton("Remove Member!");
    remMem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        String qMID = (String) MIDInPick.getSelectedItem();

        boolean isDeleted = deleteRow(qMID);

        if (isDeleted) {
          memStatus.setText("<html>Status: <font color=green>Success</html>");
        } else {
          memStatus.setText("<html>Status: <font color=red>Unable to remove</html>");
        }

        // refresh table
        memJTbl.setModel(setupTable());
      }
    });

    if (getTableRowNum("members") == 0) {
      MIDInPick.setEnabled(false);
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

  @Override
  public JPanel getPanel() {
    System.out.println("starting with db " + dbTable);
    memJTbl = new JTable();

    memJTbl.setModel(setupTable());
    memJTbl.getTableHeader().setReorderingAllowed(false);
    memJTbl.setAutoCreateRowSorter(true);
    memJTbl.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

    pane = new JScrollPane(memJTbl);

    panel = new JPanel();

    addBut = new JButton("Add");
    addBut.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        addMember();
      }
    });
    editBut = new JButton("Edit");
    editBut.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        editMember();
      }
    });
    remBut = new JButton("Remove");
    remBut.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        removeMember();
      }
    });
    panel.setLayout(new MigLayout("fill, insets 0 0 0 0", "", "[100%][]"));
    panel.add(pane, "wrap, grow");
    panel.add(addBut, "shrink, split, right");
    panel.add(editBut);
    panel.add(remBut);

    return panel;

  }

}
