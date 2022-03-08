package com.dlib;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class ManageDB {

  private JFrame frm;
  private JPanel pnl;

  private JButton backupReset, importSQL, importConfirm;
  private JLabel filenamePrmpt, statusImport;
  private JTextField fileNameIn;
  private TableOf manBook = new TableOf("books", BooksTable.col);
  private TableOf manMem = new MembersTable().getTableData();

  public void manDB() {

    frm = new JFrame("Manage Database");
    pnl = new JPanel(new MigLayout("center", "", "[][]"));

    DateTimeFormatter brwDate = DateTimeFormatter.ofPattern("MMddyyyy-HHmm");
    LocalDateTime today = LocalDateTime.now();
    final String dateToday = today.format(brwDate);

    backupReset = new JButton("Backup and Reset");
    backupReset.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        String fileName = ("libraryDB" + dateToday + ".sql");
        String[] command = { "cmd.exe", "/c", "mysqldump.exe -uroot -proot", "library", ">", "data/" + fileName };

        int[] DBData = { Utils.getTableRowNum("members"), Utils.getTableRowNum("books"),
            Utils.getTableRowNum("issuedBooks") };
        int totalRows = 0;

        for (int i = 0; i < DBData.length; i++) {
          totalRows += DBData[i];
        }

        try {
          if (totalRows > 0) {
            Process proc = new ProcessBuilder(command).start();
            proc.waitFor();
            JOptionPane.showMessageDialog(frm,
                "Finished backed-up with filename " + fileName + "\nFile can be found inside the 'data' directory");
          } else {
            JOptionPane.showMessageDialog(frm, "Database is currently empty!");
          }
        } catch (IOException e1) {
          e1.printStackTrace();
        } catch (InterruptedException e1) {
          e1.printStackTrace();
        }

        new InitDB().initializeDB();
        // refresh tables
        BooksTable.bookTable.setModel(manBook.setupTable());
        MembersTable.memTable.setModel(manMem.setupTable());
      }
    });

    importSQL = new JButton("Import");
    importSQL.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        importOpts();

      }
    });

    pnl.add(backupReset);
    pnl.add(importSQL);

    frm.add(pnl);
    frm.setVisible(true);
    frm.setSize(300, 100);
    frm.setLocationRelativeTo(null);
    frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }

  public void importOpts() {

    frm = new JFrame("Manage Database");
    pnl = new JPanel(new MigLayout("wrap", "[][]", ""));

    filenamePrmpt = new JLabel("Filename to import");
    fileNameIn = new JTextField("", 15);
    statusImport = new JLabel("Status: ");

    importConfirm = new JButton("Import");
    importConfirm.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        String fileName = fileNameIn.getText();

        File fileN = new File("data/" + fileName + ".sql");
        String[] command = { "cmd.exe", "/c", "mysql.exe -uroot -proot", "library", "<", "data/" + fileName + ".sql" };
        if (fileN.exists()) {
          try {
            Process proc = new ProcessBuilder(command).start();
            proc.waitFor();
            statusImport.setText("Status: File imported!");
            fileNameIn.setText("");
          } catch (IOException e1) {
            e1.printStackTrace();
            JOptionPane.showMessageDialog(frm, "Something went wrong, try again");
          } catch (InterruptedException e1) {
            e1.printStackTrace();
            JOptionPane.showMessageDialog(frm, "Something went wrong, try again");
          }
        } else {
          statusImport.setText("File not found, please double check");
        }

        // refresh tables
        BooksTable.bookTable.setModel(manBook.setupTable());
        MembersTable.memTable.setModel(manMem.setupTable());
      }
    });

    pnl.add(filenamePrmpt);
    pnl.add(fileNameIn);
    pnl.add(statusImport, "wrap");
    pnl.add(importConfirm, "skip, split, right");

    frm.add(pnl);
    frm.setVisible(true);
    frm.pack();
    frm.setLocationRelativeTo(null);
    frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }

}
