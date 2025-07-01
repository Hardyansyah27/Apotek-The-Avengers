/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Kasir;

import database.Koneksi;
import Admin.stok_barang;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


/**
 *
 * @author Putra
 */
public class transaksi extends javax.swing.JFrame {
    Connection con;
    Statement stm;
    ResultSet rs;
    private DefaultTableModel tabmode;
    private JMenuBar menuBar; 
    private JMenu menu; 

    /**
     * Creates new form transaksi
     */
    public transaksi() {
       initComponents();
       setLocationRelativeTo(null);
       this.setSize(790, 850); 
       this.setResizable(true); 
       con = new Koneksi().connect();
       combobox();
       datatable();
      
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
    
    private void combobox() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:8080/apotek", "root", "");
            stm = con.createStatement();
            
            pilihobat.addItem("---------------------------- Pilih Obat ----------------------------");
            String sql = "select * from obat";
            rs = stm.executeQuery(sql);
            while (rs.next()) {
                pilihobat.addItem(rs.getString("nama_obat"));
            }

            pilihobat.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    updatePriceTextField();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    private void updatePriceTextField() {
        try {
            String obatterpilih = (String) pilihobat.getSelectedItem();
            String sql = "SELECT harga_beli FROM obat WHERE nama_obat = ?";
            PreparedStatement prepareStatement = con.prepareStatement(sql);
            prepareStatement.setString(1, obatterpilih);
            ResultSet resultSet = prepareStatement.executeQuery();

            if (resultSet.next()) {
                String harga_beli = resultSet.getString("harga_beli");
                harga.setText(harga_beli);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected void clear() {
        pilihobat.setSelectedIndex(0);
        harga.setText(null);
        jumlahpesanan.setText(null);
        total.setText(null);
    }

    
    protected void tambah() {
        String sqlInsertKeranjang = "INSERT INTO keranjang VALUES (?,?,?,?,?)";
        String sqlUpdateStok = "UPDATE obat SET stok = stok-? WHERE nama_obat=?";

        try {
            con.setAutoCommit(false);

            PreparedStatement statInsert = con.prepareStatement(sqlInsertKeranjang);
            statInsert.setString(1, id_transaksi.getText());

            String obatterpilih = (String) pilihobat.getSelectedItem();
            statInsert.setString(2, obatterpilih);

            double hargaobat = Double.parseDouble(harga.getText());
            int jumlahbeli = Integer.parseInt(jumlahpesanan.getText());
            double totalhargaValue = hargaobat * jumlahbeli;

            total.setText(String.valueOf(totalhargaValue));

            statInsert.setDouble(3, hargaobat);
            statInsert.setInt(4, jumlahbeli);
            statInsert.setDouble(5, totalhargaValue);

            statInsert.executeUpdate();

            PreparedStatement statUpdateStok = con.prepareStatement(sqlUpdateStok);
            statUpdateStok.setInt(1, jumlahbeli);
            statUpdateStok.setString(2, obatterpilih);

            statUpdateStok.executeUpdate();

            con.commit();
            JOptionPane.showMessageDialog(null, "Data Berhasil Ditambahkan");
            clear();
            datatable();
            jumlahtotal();

            int jumlahbaris = tb_keranjang.getRowCount();
            double totalbiaya = 0.0;
            for (int i = 0; i < jumlahbaris; i++) {
                 Object value = tb_keranjang.getValueAt(i, 4);
            if (value != null && !value.toString().isEmpty()) {
                 totalbiaya += Double.parseDouble(value.toString());
            }
            }
totalharga.setText(String.valueOf(totalbiaya));


            totalharga.setText(String.valueOf(totalbiaya));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException autoCommitException) {
                autoCommitException.printStackTrace();
            }
        }
    }
        
   protected void jumlahtotal() {
    double total = 0.0;

    for (int i = 0; i < tb_keranjang.getRowCount(); i++) {
        Object value = tb_keranjang.getValueAt(i, 4); 
        if (value != null && !value.toString().isEmpty()) {
            try {
                total += Double.parseDouble(value.toString());
            } catch (NumberFormatException e) {
                e.printStackTrace(); 
            }
        }
    }

    totalharga.setText(String.valueOf(total));
}



    
    protected void datatable() {
        Object[] Baris = new Object[]{"Kode Transaksi", "Nama Obat", "Harga", "Jumlah Pesanan", "Total Harga"};
        tabmode = new DefaultTableModel(null, Baris);
        tb_keranjang.setModel(tabmode);
        String sql = "select * from keranjang order by notransaksi asc";
        try {
            Statement stat = con.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            while (hasil.next()) {
                String notransaksi = hasil.getString("notransaksi");
                String nama_obat = hasil.getString("nama_obat");
                String hargaStr = hasil.getString("harga");
                String jumlah = hasil.getString("jumlah");
                String totalharga = hasil.getString("totalharga");

                String[] data = new String[]{notransaksi, nama_obat, hargaStr, jumlah, totalharga};
                tabmode.addRow(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    protected void hapuskeranjang() {
        try {
            con.setAutoCommit(false);

            String sqlDelete = "DELETE FROM keranjang";
            PreparedStatement statDelete = con.prepareStatement(sqlDelete);

            int rowsAffected = statDelete.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Keranjang berhasil dihapus.");
            } else {
                JOptionPane.showMessageDialog(null, "Tidak ada data di keranjang!");
            }

            con.commit();
            clear();
            datatable();
            jumlahtotal();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal menghapus keranjang");
            e.printStackTrace();
            try {
                con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException autoCommitException) {
                autoCommitException.printStackTrace();
            }
        }
    }
    
    protected void clearInvoice() {
        totalharga.setText(null);
        jumlahuang.setText(null);
        kembalian.setText(null);
    }

    
    protected void invoice() {
        String totalhargaStr = totalharga.getText();
        String uangcashStr = jumlahuang.getText();

        try {
            double totalhargaVal = Double.parseDouble(totalhargaStr);
            double uangcash = Double.parseDouble(uangcashStr);

            if (uangcash >= totalhargaVal) {
                double kembalianVal = uangcash - totalhargaVal;

                String formattedKembalian = String.format("%.2f", kembalianVal);

                kembalian.setText(formattedKembalian);

                String sql = "INSERT INTO invoice (totalharga, uangcash, kembalian) VALUES (?, ?, ?)";
                try (PreparedStatement stat = con.prepareStatement(sql)) {
                    stat.setDouble(1, totalhargaVal);
                    stat.setDouble(2, uangcash);
                    stat.setDouble(3, kembalianVal);

                    stat.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Pembayaran berhasil\nKembalian: " + formattedKembalian);
                    clearInvoice();
                    hapuskeranjang();
                    invoice inv = new invoice();
                    inv.setVisible(true);
                    dispose();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error saat mengeksekusi pernyataan SQL! " + e.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(null, "Uang pelanggan tidak mencukupi untuk pembayaran!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Masukkan angka yang valid untuk total harga dan uang cash!");
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
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        id_transaksi = new javax.swing.JTextField();
        pilihobat = new javax.swing.JComboBox();
        harga = new javax.swing.JTextField();
        jumlahpesanan = new javax.swing.JTextField();
        total = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_keranjang = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        totalharga = new javax.swing.JTextField();
        jumlahuang = new javax.swing.JTextField();
        kembalian = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Data Transaksi");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(0, 2, 751, 60);

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel2.setText("ID Transaksi");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(30, 80, 170, 30);

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel3.setText("Pilih Obat");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(30, 120, 170, 30);

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel4.setText("Harga");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(30, 160, 170, 30);

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel5.setText("Jumlah Pemesanan");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(30, 200, 170, 30);

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel6.setText("Total Harga");
        jPanel1.add(jLabel6);
        jLabel6.setBounds(30, 240, 170, 30);
        jPanel1.add(id_transaksi);
        id_transaksi.setBounds(210, 80, 319, 30);

        jPanel1.add(pilihobat);
        pilihobat.setBounds(210, 120, 319, 30);
        jPanel1.add(harga);
        harga.setBounds(210, 160, 319, 30);
        jPanel1.add(jumlahpesanan);
        jumlahpesanan.setBounds(210, 200, 319, 30);
        jPanel1.add(total);
        total.setBounds(210, 240, 319, 30);

        jButton1.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jButton1.setText("Masukan Keranjang");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);
        jButton1.setBounds(370, 290, 141, 60);

        jButton2.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jButton2.setText("Hapus Keranjang");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2);
        jButton2.setBounds(520, 290, 150, 60);

        tb_keranjang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tb_keranjang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_keranjangMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tb_keranjang);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(0, 360, 750, 97);

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel7.setText("Total Harga");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(40, 494, 160, 30);

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel8.setText("Masukkan Jumlah Uang");
        jPanel1.add(jLabel8);
        jLabel8.setBounds(40, 530, 160, 30);

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel9.setText("Uang Kembalian");
        jPanel1.add(jLabel9);
        jLabel9.setBounds(40, 570, 160, 30);
        jPanel1.add(totalharga);
        totalharga.setBounds(210, 490, 454, 30);

        jumlahuang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jumlahuangActionPerformed(evt);
            }
        });
        jPanel1.add(jumlahuang);
        jumlahuang.setBounds(210, 530, 454, 30);
        jPanel1.add(kembalian);
        kembalian.setBounds(210, 570, 454, 30);

        jButton3.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jButton3.setText("Cek Invoice");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3);
        jButton3.setBounds(409, 613, 120, 60);

        jButton4.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jButton4.setText("Reset");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton4);
        jButton4.setBounds(550, 613, 110, 60);

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/background/ChatGPT Image Jun 23, 2025, 09_58_49 PM.png"))); // NOI18N
        jPanel1.add(jLabel10);
        jLabel10.setBounds(0, 0, 800, 810);

        jMenu1.setText("Menu");

        jMenuItem2.setText("Logout");
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 750, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 699, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jumlahuangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jumlahuangActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jumlahuangActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        tambah();       
        datatable();    
        jumlahtotal();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        hapuskeranjang();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        invoice();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        clearInvoice();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void tb_keranjangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_keranjangMouseClicked
        // TODO add your handling code here:
        
        datatable();
    }//GEN-LAST:event_tb_keranjangMouseClicked

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
            java.util.logging.Logger.getLogger(transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new transaksi().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField harga;
    private javax.swing.JTextField id_transaksi;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jumlahpesanan;
    private javax.swing.JTextField jumlahuang;
    private javax.swing.JTextField kembalian;
    private javax.swing.JComboBox pilihobat;
    private javax.swing.JTable tb_keranjang;
    private javax.swing.JTextField total;
    private javax.swing.JTextField totalharga;
    // End of variables declaration//GEN-END:variables
}
