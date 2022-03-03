package com.dlib;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.dlib.manageTables.ManageMembers;

import net.miginfocom.swing.MigLayout;

public class MembersTable {

  // private String tableName = "members";
  private static JPanel panel;

  public static String[] col = { "MID", "FIRST NAME", "LAST NAME", "ADDRESS", "CONTACT" };
  // public String[] headers = { "m_id", "firstName", "lastName",
  // "address", "contact" };

  private static JScrollPane pane;
  private static JButton addMember, editMember, remMember;

  private static TableOf memTbl = new TableOf("members", col);
  public static JTable memTable = memTbl.getTable();

  private static ManageMembers manMem = new ManageMembers();

  // can add this in a superclass
  public TableOf getTableData() {
    return memTbl;
  }

  public static JPanel membersTable() {

    manMem.setMemTable(memTable);
    // memTable = memTbl.getTable();

    // Gui layout starts here
    pane = new JScrollPane(memTable);

    panel = new JPanel();
    addMember = new JButton("Add");
    addMember.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        manMem.addMember();
      }
    });
    editMember = new JButton("Edit");
    editMember.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        manMem.editMember();
      }
    });
    remMember = new JButton("Remove");
    remMember.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        manMem.removeMember();
      }
    });

    panel.setLayout(new MigLayout("insets 0 0 0 0, fill", "", "[100%][]"));
    panel.add(pane, "wrap, grow");
    panel.add(addMember, "shrink, split, right");
    panel.add(editMember);
    panel.add(remMember);

    return panel;
  }

}
