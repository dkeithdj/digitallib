package com.dlib;

import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.sql.Connection;
import java.sql.Statement;
import java.util.concurrent.Flow;
import java.sql.ResultSet;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class MembersTable {

  private static String tableName = "members";

  public static void librarianTable() {

    Connection con = Utils.connectToDB();

    try {
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery("select * from " + tableName);

      String col[] = { "ID", "NAME", "PASSWORD", "EMAIL", "ADDRESS", "CITY", "CONTACT" };
      String data[][] = new String[Utils.getTableRowNum(tableName)][col.length];

      int i = 0;
      while (rs.next()) {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String passwrd = rs.getString("password");
        String email = rs.getString("email");
        String address = rs.getString("address");
        String city = rs.getString("city");
        String contact = rs.getString("contact");

        String details[] = { Integer.toString(id), name, passwrd, email, address, city, contact };

        for (int x = 0; x < details.length; x++) {
          data[i][x] = details[x];
        }
        i++;
      }

      TableModel model = new DefaultTableModel(data, col) {
        @Override
        public boolean isCellEditable(int row, int column) {
          // all cells false
          return false;
        }
      };
      JTable table = new JTable(model);
      table.getTableHeader().setReorderingAllowed(false);
      table.setShowGrid(true);
      table.setShowVerticalLines(true);
      table.setAutoCreateRowSorter(true);

      JScrollPane pane = new JScrollPane(table);

      JFrame f = new JFrame("Members");
      JPanel panel = new JPanel();

      panel.setLayout(new BorderLayout());
      panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
      panel.add(pane, BorderLayout.CENTER);
      panel.add(testAgain(), BorderLayout.SOUTH);
      f.add(panel);
      f.setSize(600, 250);
      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      f.setVisible(true);

      con.close();
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  public static Component test() {
    JButton button = new JButton("uh");
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JPanel newP = new JPanel();
        JFrame newF = new JFrame("Oh empty");
        JLabel nef2w = new JLabel("ah");
        newP.add(testAgain(), BorderLayout.CENTER);
        newF.add(newP);
        newF.add(nef2w);
        newF.setSize(300, 300);
        newF.setVisible(true);
      }
    });
    return button;
  }

  public static Component testAgain() {
      final JButton button = new JButton("arsthf");
      button.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          JFrame newF = new JFrame("Oh empty");
          JLabel nef2w = new JLabel("arst");
          newF.add(nef2w);
          newF.setSize(300, 300);
          newF.setVisible(true);
          JButton newBut = new JButton("ehe");
          newBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              JFrame newAgain = new JFrame("ara");
              JLabel ars = new JLabel("arst");
              JPanel newPan = new JPanel();
              newPan.add(button);
              newAgain.add(ars);
              newAgain.setSize(300, 300);
              newAgain.setVisible(true);
            }

          });
        }
      });
      return button;
  }
}
