package com.dlib;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class Login {
  private static JTextField userNInput;
  private static JPasswordField userPInput;

  private static JPanel pnlMain, pnlLog;
  private static JLabel userName, userPass;

  private static JButton loginButton;

  private static JFrame frm;

  private static String adminUName = "admin";
  private static String adminPass = "admin";

  public static void login() {

    frm = new JFrame("Login");
    pnlMain = new JPanel();
    pnlLog = new JPanel();
    pnlMain.setLayout(new MigLayout("fill", "", ""));
    pnlLog.setLayout(new MigLayout("wrap", "[][]", ""));

    // login panel
    userName = new JLabel("Username: ");
    userNInput = new JTextField("", 15);
    userPass = new JLabel("User Password: ");
    userPInput = new JPasswordField("", 15);
    userPInput.setEchoChar('*');

    loginButton = new JButton("Login");
    loginButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String accName = userNInput.getText();
        String accPass = String.valueOf(userPInput.getPassword());

        if (accName.equals("") || accPass.equals("")) {
          JOptionPane.showMessageDialog(null, "Name or password should not be empty!");
        } 
        else {
          if (accName.equals(adminUName) && accPass.equals(adminPass)) {
            frm.dispose();
            AdminMenu.adminPage();
          }
          else {
            JOptionPane.showMessageDialog(null, "Incorrect Username/Password");
          }
        }
      }
    });

    pnlLog.add(userName);
    pnlLog.add(userNInput);
    pnlLog.add(userPass);
    pnlLog.add(userPInput);
    pnlLog.add(loginButton, "skip, right");

    pnlMain.add(pnlLog, "top");

    frm.add(pnlMain);
    frm.pack();
    frm.setVisible(true);
    frm.setLocationRelativeTo(null);
    frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }
}
