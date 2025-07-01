/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Admin;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import database.Koneksi;
import javax.swing.*;  
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Putra
 */
public class create_user extends javax.swing.JFrame {
    Connection con;
    Statement stm;
    ResultSet rs;
    private DefaultTableModel tabmode;
    /**
     * Creates new form create_user
     */
    public create_user() {
        initComponents();
        setLocationRelativeTo(null);
        this.setSize(550, 650); 
        this.setResizable(true);
        con = new Koneksi().connect();
        role.removeAllItems(); 
        role.addItem("-------------------- Pilih User ---------------------");
        role.addItem("Owner");
        role.addItem("Admin");
        role.addItem("Kasir");
        role.setSelectedIndex(0);
        
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem menuItemPenghasilan = new JMenuItem("Penghasilan");
        menuItemPenghasilan.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); 
                new Owner.Penghasilan().setVisible(true); 
            }
        });
        
        JMenuItem menuItemstok_barang = new JMenuItem("Stok Barang");
        menuItemstok_barang.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); 
               new stok_barang().setVisible(true); 
            }
        });
        
        JMenuItem menuItemFrom_Login = new JMenuItem("Logout");
        menuItemFrom_Login.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); 
               new Login.From_Login().setVisible(true); 
            }
        });


        menu.add(menuItemPenghasilan);
        menu.add(menuItemstok_barang);
        menu.add(menuItemFrom_Login);
        menuBar.add(menu);
        setJMenuBar(menuBar);
        
    
    }
    
    protected void dataterpilih(){
        int bar = tb_user.getSelectedRow();
        String name = tabmode.getValueAt(bar, 0).toString();
        String pass = tabmode.getValueAt(bar, 1).toString();
        String rol = tabmode.getValueAt(bar, 2).toString();
        
        username.setText(name);
        password.setText(pass);
        role.setSelectedItem(rol);
    }
    
    protected void clear(){
        username.setText(null);
        password.setText(null);
        role.setSelectedIndex(0); 
    }
    
    protected void datatable(){
        Object[] Baris = {"Username", "Password", "Role"};
        tabmode = new DefaultTableModel(null, Baris);
        tb_user.setModel(tabmode);
        String sql = "select * from users order by id asc";
        try {
            Statement stat = con.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            while (hasil.next()) {
                String username = hasil.getString("username");
                String password = hasil.getString("password");
                String role = hasil.getString("role");

                String[] data = {username, password, role};
                tabmode.addRow(data);
            }
        } catch (Exception e) {
            System.err.println("Error : " + e.getMessage());
        }
    }
    
    protected void tambah() {
        
        if (username.getText().isEmpty() || password.getPassword().length == 0) {
            JOptionPane.showMessageDialog(null, "Username dan Password wajib diisi!");
            return;
        }

        if (role.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Pilih role terlebih dahulu!");
            return;
        }

        String sql = "INSERT INTO users VALUES (null, ?, ?, ?)";

        try {
            PreparedStatement stat = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stat.setString(1, username.getText());
            stat.setString(2, new String(password.getPassword()));
            stat.setString(3, role.getSelectedItem().toString());

            stat.executeUpdate();

            ResultSet generatedKeys = stat.getGeneratedKeys();
            if (generatedKeys.next()) {
                int idBaru = generatedKeys.getInt(1);
                System.out.println("ID Baru : " + idBaru);
            }

            JOptionPane.showMessageDialog(null, "Data Berhasil Ditambahkan.");
            clear();
            datatable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Data Gagal Ditambahkan! " + e.getMessage());
        }
    }
    
    protected void delete() {
        int delete = JOptionPane.showConfirmDialog(null, "Hapus user ini?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (delete == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM users WHERE username=?";
            try {
                PreparedStatement stat = con.prepareStatement(sql);
                stat.setString(1, username.getText());
                stat.executeUpdate();
                JOptionPane.showMessageDialog(null, "Data Berhasil Dihapus.");
                clear();
                datatable();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Data Gagal Dihapus! " + e.getMessage());
            }
        }
    }
    
    protected void edit() {
        try {
            String sql = "UPDATE users SET password=?, role=? WHERE username=?";
            con.setAutoCommit(false);

            try (PreparedStatement stat = con.prepareStatement(sql)) {
                stat.setString(1, new String(password.getPassword()));
                stat.setString(2, role.getSelectedItem().toString());
                stat.setString(3, username.getText());

                stat.executeUpdate();
                con.commit();

                JOptionPane.showMessageDialog(null, "Data Berhasil Diubah.");
                clear();
                datatable();
            } catch (SQLException e) {
                con.rollback();
                JOptionPane.showMessageDialog(null, "Data Gagal Diubah! " + e.getMessage());
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Data Gagal Diubah! " + e.getMessage());
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        username = new javax.swing.JTextField();
        password = new javax.swing.JPasswordField();
        role = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_user = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Create User");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(10, 12, 500, 50);

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel2.setText("Username    :");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(30, 130, 180, 30);

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel3.setText("Password     :");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(30, 170, 180, 30);

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel4.setText("Hak Akses    : ");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(30, 210, 170, 30);
        jPanel1.add(username);
        username.setBounds(220, 130, 260, 30);
        jPanel1.add(password);
        password.setBounds(220, 170, 260, 30);

        role.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " ", " ", " " }));
        jPanel1.add(role);
        role.setBounds(220, 210, 260, 30);

        jButton1.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jButton1.setText("Create");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);
        jButton1.setBounds(395, 260, 90, 40);

        tb_user.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tb_user.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Username", "Password", "Hak Akses"
            }
        ));
        tb_user.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_userMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tb_user);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(0, 310, 520, 92);

        jButton2.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jButton2.setText("Edit");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2);
        jButton2.setBounds(280, 430, 100, 40);

        jButton3.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jButton3.setText("Hapus");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3);
        jButton3.setBounds(390, 430, 100, 40);

        jButton4.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jButton4.setText("Lihat Data");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton4);
        jButton4.setBounds(160, 430, 110, 40);

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/background/ChatGPT Image Jun 23, 2025, 09_52_29 PM.png"))); // NOI18N
        jPanel1.add(jLabel6);
        jLabel6.setBounds(-130, -30, 690, 720);

        jMenu1.setText("Menu");

        jMenuItem1.setText("Penghasilan");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem3.setText("stok_barang");
        jMenu1.add(jMenuItem3);

        jMenuItem2.setText("Logout");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 546, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 504, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        delete();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        edit();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        tambah();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void tb_userMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_userMouseClicked
        // TODO add your handling code here:
        dataterpilih();
    }//GEN-LAST:event_tb_userMouseClicked

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        datatable();
    }//GEN-LAST:event_jButton4ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(create_user.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(create_user.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(create_user.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(create_user.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new create_user().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPasswordField password;
    private javax.swing.JComboBox role;
    private javax.swing.JTable tb_user;
    private javax.swing.JTextField username;
    // End of variables declaration//GEN-END:variables
}
