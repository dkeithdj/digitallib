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

  public static JTable memTable;
  // private static String tableName = "members";
  private static JPanel panel;

  public static String[] col = { "MID", "FIRST NAME", "LAST NAME", "ADDRESS", "CONTACT" };
  // public static String[] headers = { "m_id", "firstName", "lastName",
  // "address", "contact" };

  private static JScrollPane pane;
  private static JButton addMember, editMember, remMember;

  public static JPanel membersTable() {

    TableOf bTbl = new TableOf("members", col);

    memTable = bTbl.getTable();

    // Gui layout starts here
    pane = new JScrollPane(memTable);

    panel = new JPanel();
    addMember = new JButton("Add");
    addMember.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ManageMembers.addMember();
      }
    });
    editMember = new JButton("Edit");
    editMember.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ManageMembers.editMember();
      }
    });
    remMember = new JButton("Remove");
    remMember.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ManageMembers.removeMember();
      }
    });

    panel.setLayout(new MigLayout("insets 0 0 0 0, fill", "", "[100%][]"));
    panel.add(pane, "wrap, grow");
    panel.add(addMember, "shrink, split, right");
    panel.add(editMember);
    panel.add(remMember);

    return panel;
  }

  // public static DefaultTableModel showMembersTable() {

  // String col[] = { "MID", "FIRST NAME", "LAST NAME", "ADDRESS", "CONTACT" };
  // String data[][] = new String[Utils.getTableRowNum(tableName)][col.length];

  // Connection con = Utils.connectToDB();
  // try {
  // Statement stmt = con.createStatement();
  // stmt.executeUpdate("USE library");
  // ResultSet rs = stmt.executeQuery("select * from " + tableName);

  // int i = 0;
  // while (rs.next()) {
  // int mid = rs.getInt("m_id");
  // String firstName = rs.getString("firstName");
  // String lastName = rs.getString("lastName");
  // String address = rs.getString("address");
  // String contact = rs.getString("contact");

  // String details[] = { Integer.toString(mid), firstName, lastName, address,
  // contact };

  // for (int x = 0; x < details.length; x++) {
  // data[i][x] = details[x];
  // }
  // i++;
  // }
  // DefaultTableModel model = new DefaultTableModel(data, col) {
  // @Override
  // public boolean isCellEditable(int row, int column) {
  // return false;
  // }
  // };
  // return model;
  // } catch (Exception ex) {
  // System.out.println(ex);
  // }
  // return null;
  // }

}
