package com.dlib;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Bidi;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class ManageBooks {

  private static JPanel pnl;
  private static JFrame frm;

  public static void addBook() {

    frm = new JFrame("Add books");
    pnl = new JPanel();
    pnl.setLayout(new MigLayout("wrap", "[][]", ""));

    JLabel bTitle = new JLabel("Title: ");
    JTextField titleIn = new JTextField("", 15);
    JLabel bAuthor = new JLabel("Author: ");
    JTextField authorIn = new JTextField("", 15);
    JLabel bGenre = new JLabel("Genre: ");
    JTextField genreIn = new JTextField("", 15);
    JLabel bQuant = new JLabel("Quantity: ");
    JTextField quantIn = new JTextField("", 15);
    JLabel bPubYear = new JLabel("Published Year: ");
    JTextField pubYearIn = new JTextField("", 15);

    JButton addBook = new JButton("Add Book!");

    pnl.add(bTitle);
    pnl.add(titleIn);
    pnl.add(bAuthor);
    pnl.add(authorIn);
    pnl.add(bGenre);
    pnl.add(genreIn);
    pnl.add(bQuant);
    pnl.add(quantIn);
    pnl.add(bPubYear);
    pnl.add(pubYearIn);
    pnl.add(addBook, "skip, split, right");

    frm.add(pnl);
    frm.setSize(350, 400);
    frm.setVisible(true);
    frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }

  public static void removeBook() {

    frm = new JFrame("Remove Book");
    pnl = new JPanel();
    pnl.setLayout(new MigLayout("debug, wrap", "[][]", ""));

    JLabel bookID = new JLabel("Book ID (BID): ");
    JTextField BIDIn = new JTextField("", 15);
    JButton remBook = new JButton("Remove Book!");

    pnl.add(bookID);
    pnl.add(BIDIn);
    pnl.add(remBook, "skip, split, right");

    frm.add(pnl);
    frm.setSize(350, 400);
    frm.setVisible(true);
    frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }

  public static void editBook() {

  }

}
