package com.dlib;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.dlib.manageTables.ManageMembers;

import net.miginfocom.swing.MigLayout;

public class MembersTable {

  public static JTable memTable;

  private static String tableName = "members";

  public static JPanel membersTable() {

    // Gui layout starts here
    memTable = new JTable();
    memTable.setModel(showMembersTable());
    memTable.getTableHeader().setReorderingAllowed(false);
    memTable.setShowGrid(true);
    memTable.setShowVerticalLines(true);
    memTable.setAutoCreateRowSorter(true);
    memTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

    JScrollPane pane = new JScrollPane(memTable);

    JPanel panel = new JPanel();
    JButton addMember = new JButton("Add");
    addMember.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ManageMembers.addMember();
      }
    });
    JButton editMember = new JButton("Edit");
    editMember.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ManageMembers.editMember();
      }
    });
    JButton remMember = new JButton("Remove");
    remMember.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ManageMembers.removeMember();
      }
    });

    panel.setLayout(new MigLayout("insets 5 5 5 5, fill", "", ""));
    panel.add(pane, "wrap, grow");
    panel.add(addMember, "split, right, top");
    panel.add(editMember, "top");
    panel.add(remMember, "top");

    return panel;
  }

  public static DefaultTableModel showMembersTable() {

    String col[] = { "MID", "FIRST NAME", "LAST NAME", "ADDRESS", "CONTACT" };
    String data[][] = new String[Utils.getTableRowNum(tableName)][col.length];

    Connection con = Utils.connectToDB();
    try {
      Statement stmt = con.createStatement();
      stmt.executeUpdate("USE library");
      ResultSet rs = stmt.executeQuery("select * from " + tableName);

      int i = 0;
      while (rs.next()) {
        int mid = rs.getInt("m_id");
        String firstName = rs.getString("firstName");
        String lastName = rs.getString("lastName");
        String address = rs.getString("address");
        String contact = rs.getString("contact");

        String details[] = { Integer.toString(mid), firstName, lastName, address, contact };

        for (int x = 0; x < details.length; x++) {
          data[i][x] = details[x];
        }
        i++;
      }
      DefaultTableModel model = new DefaultTableModel(data, col) {
        @Override
        public boolean isCellEditable(int row, int column) {
          return false;
        }
      };
      return model;
    } catch (Exception ex) {
      System.out.println(ex);
    }
    return null;
  }

}
