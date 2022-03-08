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
  private JTextField userNInput;
  private JPasswordField userPInput;

  private JPanel pnlMain, pnlLog;
  private JLabel userName, userPass;

  private JButton loginButton;

  private JFrame frm;

  private final String adminUName = "admin";
  private final String adminPass = "admin";

  public void login() {

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
        } else {
          if (accName.equals(adminUName) && accPass.equals(adminPass)) {
            frm.dispose();
            new AdminMenu().adminPage();
          } else {
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
