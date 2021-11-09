package com.dlib;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;


public class Login {
  public static void login() {
    JFrame frm = new JFrame("Login");
    JPanel pnl = new JPanel();
    pnl.setLayout(new MigLayout("","",""));

    JLabel userName = new JLabel("User Name: ");
    JTextField userNInput = new JTextField("", 15);
    JLabel userPass = new JLabel("User Password: ");
    final JPasswordField userPInput = new JPasswordField("", 15);
    userPInput.setEchoChar('*');

    JButton loginButton = new JButton("Login");
    JButton regButton = new JButton("Register");
    JLabel messPrompt = new JLabel("Wrong username or password, try again");

    pnl.add(userName);
    pnl.add(userNInput, "wrap");
    pnl.add(userPass);
    pnl.add(userPInput, "wrap");
    pnl.add(regButton, "span, split, right");
    pnl.add(loginButton);

    frm.add(pnl);
    frm.setSize(300, 150);
    frm.setVisible(true);
    frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
}
