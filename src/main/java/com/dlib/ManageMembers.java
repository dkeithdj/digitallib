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
  private JLabel fName, lName, address, contact, MIDVal, memID;

  private JButton addMember, editMem, remMem;
  private ArrayList<JTextField> jtIns;
  private String[] membersColumn = { "MID", "FIRST NAME", "LAST NAME", "ADDRESS", "CONTACT" };
  private JTextField midIn;

  private String qMID = "";

  public ManageMembers() {
    super("members");
    super.setColNames(membersColumn);
  }

  // Add Member
  public JPanel addMember() {

    fName = new JLabel("First Name: ");
    fNameIn = new JTextField(15);
    lName = new JLabel("Last Name: ");
    lNameIn = new JTextField(15);
    address = new JLabel("Address: ");
    addressIn = new JTextField(15);
    contact = new JLabel("Contact Number: ");
    contactIn = new JTextField(15);
    status = new JLabel("Status: ");

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
          status.setText("<html>Status: <font color=green>Success</html>");
        } else {
          status.setText("<html>Status: <font color=red>Check input fields</html>");
        }

      }
    });

    frm = new JFrame("Add Member");
    pnl = new JPanel();
    pnl.setLayout(new MigLayout(" wrap", "[][][][]", ""));

    pnl.add(fName);
    pnl.add(lName);
    pnl.add(address);
    pnl.add(contact);
    pnl.add(fNameIn);
    pnl.add(lNameIn);
    pnl.add(addressIn);
    pnl.add(contactIn);
    pnl.add(status, "span 3");
    pnl.add(addMember, " split, right");

    return pnl;
  }

  // Edit Member
  public void editMember() {

    frm = new JFrame("Edit Member");
    pnl = new JPanel();
    pnl.setLayout(new MigLayout("wrap", "[][]", ""));

    MIDVal = new JLabel("Member ID(MID): ");

    midIn = new JTextField(15);

    status = new JLabel("Status: ");
    fName = new JLabel("First Name: ");
    fNameIn = new JTextField(15);
    lName = new JLabel("Last Name: ");
    lNameIn = new JTextField(15);
    address = new JLabel("Address: ");
    addressIn = new JTextField(15);
    contact = new JLabel("Contact Number: ");
    contactIn = new JTextField(15);

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
    midIn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        qMID = midIn.getText();

        if (validateID(qMID)) {

          Map<String, String> res = selectRow(qMID);

          editMem.setEnabled(true);
          int i = 0;
          for (String str : res.values()) {
            jtIns.get(i).setText(str);
            jtIns.get(i).setEditable(true);
            i++;
            status.setText("<html>Status: <font color=green>Success</html>");
          }
        } else {
          for (int i = 0; i < jtIns.size(); i++) {
            jtIns.get(i).setEditable(false);
            jtIns.get(i).setText("");
          }
          editMem.setEnabled(false);
          status.setText("<html>Status: <font color=red>Invalid</html>");
        }

      }
    });

    editMem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        qMID = midIn.getText();

        ArrayList<String> jtTxt = new ArrayList<String>();
        for (int i = 0; i < jtIns.size(); i++) {
          jtTxt.add(jtIns.get(i).getText());
        }

        boolean res = updateRow(jtTxt, qMID);

        if (res) {
          editMem.setEnabled(true);
          status.setText("<html>Status: <font color=green>Success</html>");
        } else {
          status.setText("<html>Status: <font color=red>Check input fields</html>");
        }

        for (int i = 0; i < jtIns.size(); i++) {
          editMem.setEnabled(false);
          jtIns.get(i).setEditable(false);
          jtIns.get(i).setText("");
        }

      }
    });

    if (getTableRowNum("members") == 0) {
      editMem.setEnabled(false);
      status.setText("Status: No Members");
    }

    pnl.add(MIDVal);
    pnl.add(midIn);
    pnl.add(fName);
    pnl.add(fNameIn);
    pnl.add(lName);
    pnl.add(lNameIn);
    pnl.add(address);
    pnl.add(addressIn);
    pnl.add(contact);
    pnl.add(contactIn);
    pnl.add(status, "span");
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
    midIn = new JTextField(15);
    status = new JLabel("Status: ");

    remMem = new JButton("Remove Member!");
    remMem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        qMID = midIn.getText();

        if (validateID(qMID)) {
          Map<String, String> ibData = new ManageIssBooks().selectRow(1, qMID);
          Map<String, String> memData = selectRow(qMID);
          if (ibData.isEmpty()) {
            confirm(qMID, memData.get("lastName"));

            status.setText("<html>Status: <font color=green>ID found</html>");
          } else {
            status.setText("<html>Status: <font color=red>Member still has borrowed book(s)</html>");
          }
        } else {
          status.setText("<html>Status: <font color=red>Invalid ID</html>");
        }
      }
    });

    if (getTableRowNum("members") == 0) {
      midIn.setEditable(false);
      remMem.setEnabled(false);
      status.setText("Status: No members");
    }

    pnl.add(memID);
    pnl.add(midIn);
    pnl.add(status, "span");
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

    panel = new JPanel();

    panel.setLayout(new MigLayout("fill, insets 5 5 5 5", ""));
    panel.add(addMember(), "grow, wrap");
    panel.add(showTable(), "grow");

    return panel;

  }

  public JPanel showTable() {
    l = getTableColName();
    pickFilter = new JComboBox<String>(col);

    JLabel searchL = new JLabel("Search: ");
    searchIn = new JTextField(15);
    table = new JTable();

    table.setModel(setupTable());
    table.getTableHeader().setReorderingAllowed(false);
    table.setAutoCreateRowSorter(true);
    table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

    pane = new JScrollPane(table);

    searchFilter = new JButton("Search");
    searchFilter.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        setData(searchIn.getText(), l.get(pickFilter.getSelectedIndex()));
        table.setModel(setupTable());
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

    pnl = new JPanel();
    pnl.setLayout(new MigLayout("fill, insets 5 5 5 5", "", "[][100%][]"));
    pnl.add(searchL, "split");
    pnl.add(searchIn, "growx");
    pnl.add(pickFilter);
    pnl.add(searchFilter, "wrap");
    pnl.add(pane, "wrap, grow");
    pnl.add(editBut, "split, right");
    pnl.add(remBut);

    return pnl;
  }
}
