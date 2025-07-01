/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Admin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import database.Koneksi;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import Kasir.transaksi;




/**
 *
 * @author Putra
 */
public class stok_barang extends javax.swing.JFrame {
   

    private Connection con;
    private DefaultTableModel tabmode;
    private JMenuBar menuBar; 
    private JMenu menu; 
    

    
    /**
     * Creates new form stok_barang
     */
    public stok_barang() {
    initComponents();
    con = new Koneksi().connect();
    setLocationRelativeTo(null);
    menuBar = new JMenuBar(); 
    menu = new JMenu("Menu"); 

JMenuItem menuItemFrom_Login = new JMenuItem("Logout");
menuItemFrom_Login.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        dispose(); 
        new Login.From_Login().setVisible(true); 
    }
});

menu.add(menuItemFrom_Login);
menuBar.add(menu);
setJMenuBar(menuBar);
    }
    
     protected void clear() {
    id.setText(null);
    nama_obat.setText(null);
    harga_beli.setText(null);
    harga_jual.setText(null); 
    stok.setText(null);
}

     
     protected void datatable() {
    Object[] Baris = {"ID", "Nama Obat", "Harga Beli", "Harga Jual", "Stok"};
    tabmode = new DefaultTableModel(null, Baris);
    tb_apotek.setModel(tabmode);

    String sql = "SELECT * FROM obat ORDER BY id ASC";
    try {
        Statement stat = con.createStatement();
        ResultSet hasil = stat.executeQuery(sql);
        while (hasil.next()) {
            String id = hasil.getString("id");
            String nama_obat = hasil.getString("nama_obat");
            String harga_beli = hasil.getString("harga_beli");
            String harga_jual = hasil.getString("harga_jual");
            String stok = hasil.getString("stok");

            String[] data = {id, nama_obat, harga_beli, harga_jual, stok};
            tabmode.addRow(data);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Gagal mengambil data: " + e);
    }
}


     
    protected void tambah() {
    String sql = "INSERT INTO obat (id, nama_obat, harga_beli, harga_jual, stok) VALUES (?, ?, ?, ?, ?)";
    try {
        PreparedStatement stat = con.prepareStatement(sql);

        
        String idText = id.getText(); 
        String namaObatText = nama_obat.getText();
        String hargaBeliText = harga_beli.getText();
        String hargaJualText = harga_jual.getText();
        String stokText = stok.getText();

        
        stat.setString(1, idText);
        stat.setString(2, namaObatText);
        stat.setString(3, hargaBeliText);
        stat.setString(4, hargaJualText);
        stat.setString(5, stokText);

        stat.executeUpdate();
        JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan");
        clear();
        datatable();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Data gagal ditambahkan: " + e.getMessage());
    }
}




     
    protected void dataterpilih() {
    int bar = tb_apotek.getSelectedRow();

    if (bar != -1) {
        String idText = tabmode.getValueAt(bar, 0).toString();
        String namaObatText = tabmode.getValueAt(bar, 1).toString();
        String hargaBeliText = tabmode.getValueAt(bar, 2).toString();
        String hargaJualText = tabmode.getValueAt(bar, 3).toString();
        String stokText = tabmode.getValueAt(bar, 4).toString();

        id.setText(idText);
        nama_obat.setText(namaObatText);
        harga_beli.setText(hargaBeliText);
        harga_jual.setText(hargaJualText);
        stok.setText(stokText);
    }
}

     
     protected void edit(){
         try{
             String sql = "update obat set nama_obat=?,harga_beli=?,harga_jual=?,stok=? where id=?";
             
             con.setAutoCommit(false);
             
             try (PreparedStatement stat = con.prepareStatement(sql)){
                 stat.setString(1, nama_obat.getText());
                 stat.setString(2, harga_beli.getText());
                 stat.setString(3, harga_jual.getText());
                 stat.setString(4, stok.getText());
                 stat.setString(5, id.getText());
                 
                 System.out.println("Nama Obat : " + nama_obat.getText());
                 System.out.println("Harga Beli : " +harga_beli.getText());
                 System.out.println("Harga Jual : " +harga_jual.getText());
                 System.out.println("Stok : " + stok.getText());
                 System.out.println("ID : " + id.getText());
                 
                 stat.executeUpdate();
                 
                 con.commit();
                 
                 JOptionPane.showMessageDialog(null, "Data berhasil diubah.");
                 clear();
                 datatable();
             }catch (SQLException e){
                 con.rollback();
                 e.printStackTrace();
                 JOptionPane.showMessageDialog(null, "Data gagal diubah : " + e.getMessage());
             }finally{
                 con.setAutoCommit(true);
             }
         }catch (SQLException e){
             JOptionPane.showMessageDialog(null, "Data gagal diubah : " + e.getMessage());
         }
    }
     
    protected void delete() {
    int delete = JOptionPane.showConfirmDialog(null,
            "Apakah Anda yakin ingin menghapus data ini?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION);
    
    if (delete == JOptionPane.YES_OPTION) {
        String sql = "DELETE FROM obat WHERE id = ?";
        try {
            PreparedStatement stat = con.prepareStatement(sql);
            stat.setString(1, id.getText());
            stat.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
            clear();
            datatable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Data gagal dihapus: " + e.getMessage());
        }
    }
}

    
    protected void cari(){
        System.out.println("Tombol cari ditekan.");
        Object[] Baris = {"ID", "Nama Obat", "Harga Beli", "Harga Jual", "Stok"};
        tabmode=new DefaultTableModel(null, Baris);
        tb_apotek.setModel(tabmode);
        String keyword = cari.getText();
        String sql = "SELECT * FROM obat WHERE id LIKE '%" + keyword + "%' OR nama_obat LIKE '%" + keyword + "%' ORDER BY id ASC";
        try{
            java.sql.Statement stat = con.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            while(hasil.next()){
                String a = hasil.getString("id");
                String b = hasil.getString("nama_obat");
                String c = hasil.getString("harga_beli");
                String d = hasil.getString("harga_jual");
                String e = hasil.getString("stok");
                
                String[] data = {a, b, c, d, e};
                tabmode.addRow(data);
            }
        }catch(Exception e){
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
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_apotek = new javax.swing.JTable();
        cari = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        id = new javax.swing.JTextField();
        nama_obat = new javax.swing.JTextField();
        harga_beli = new javax.swing.JTextField();
        harga_jual = new javax.swing.JTextField();
        stok = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel1.setText("TABEL OBAT");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(10, 11, 160, 40);

        tb_apotek.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        tb_apotek.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Nama Obat", "Harga Beli", "Harga Jual", "Stok"
            }
        ));
        tb_apotek.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_apotekMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tb_apotek);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(10, 130, 700, 90);
        jPanel1.add(cari);
        cari.setBounds(360, 60, 210, 40);

        jButton1.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jButton1.setText("Cari");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);
        jButton1.setBounds(600, 60, 100, 40);

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel2.setText("Detail Data Obat");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(10, 232, 190, 30);

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel3.setText("ID                        ");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(10, 280, 150, 30);

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel4.setText("Nama Obat          ");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(10, 320, 150, 30);

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel5.setText("Harga Beli Obat   ");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(10, 360, 150, 30);

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel6.setText("Harga Jual Obat  ");
        jPanel1.add(jLabel6);
        jLabel6.setBounds(10, 400, 150, 30);

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel7.setText("Stok Obat            ");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(10, 440, 150, 30);
        jPanel1.add(id);
        id.setBounds(170, 280, 330, 30);
        jPanel1.add(nama_obat);
        nama_obat.setBounds(170, 320, 330, 30);
        jPanel1.add(harga_beli);
        harga_beli.setBounds(170, 360, 330, 30);
        jPanel1.add(harga_jual);
        harga_jual.setBounds(170, 400, 330, 30);
        jPanel1.add(stok);
        stok.setBounds(170, 440, 330, 30);

        jButton2.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jButton2.setText("Tambah Data");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2);
        jButton2.setBounds(170, 510, 130, 50);

        jButton3.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jButton3.setText("Edit Data");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3);
        jButton3.setBounds(440, 510, 110, 50);

        jButton4.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jButton4.setText("Hapus Data");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton4);
        jButton4.setBounds(560, 510, 120, 50);

        jButton5.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jButton5.setText("Lihat Data");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton5);
        jButton5.setBounds(310, 510, 120, 50);

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/background/ChatGPT Image Jun 23, 2025, 10_18_06 PM.png"))); // NOI18N
        jPanel1.add(jLabel8);
        jLabel8.setBounds(-80, -30, 850, 820);

        jMenu1.setText("Menu");

        jMenuItem1.setText("Logout");
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 713, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        datatable();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        delete();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        edit();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        tambah();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        cari();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void tb_apotekMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_apotekMouseClicked
        // TODO add your handling code here:
        dataterpilih();
    }//GEN-LAST:event_tb_apotekMouseClicked

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
            java.util.logging.Logger.getLogger(stok_barang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(stok_barang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(stok_barang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(stok_barang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new stok_barang().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField cari;
    private javax.swing.JTextField harga_beli;
    private javax.swing.JTextField harga_jual;
    private javax.swing.JTextField id;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField nama_obat;
    private javax.swing.JTextField stok;
    private javax.swing.JTable tb_apotek;
    // End of variables declaration//GEN-END:variables


}
