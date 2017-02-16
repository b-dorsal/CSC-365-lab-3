package pageminroutegui;
//Brian Dorsey 2016

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import static java.lang.System.out;
import java.util.ArrayList;

public class A3GUI extends javax.swing.JFrame {

    private wikiGraph graph;

    public A3GUI(String dir) {
        graph = this.load(dir);

        initComponents();
    }

    public static wikiGraph load(String dir) {
        out.print("Loading from file...");
        wikiGraph e = null;
        try {
            FileInputStream fileIn = new FileInputStream(dir);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            e = (wikiGraph) in.readObject();
            in.close();
            fileIn.close();
        } catch (Exception c) {
            out.println("ERROR: file NOT loaded! " + c);
            System.exit(1);
            return null;
        }
        out.println("Done.");
        return e;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGo = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        tbxStartPage = new javax.swing.JTextField();
        tbxEndPage = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txaResults = new javax.swing.JTextArea();
        btnClear = new javax.swing.JButton();
        btnShowPages = new javax.swing.JButton();
        btnSpan = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Wiki Page Path");
        setResizable(false);

        btnGo.setText("Go");
        btnGo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGoActionPerformed(evt);
            }
        });

        jLabel1.setText("Start Page:");

        jLabel2.setText("End Page:");

        txaResults.setColumns(20);
        txaResults.setRows(5);
        jScrollPane1.setViewportView(txaResults);

        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        btnShowPages.setText("List Pages");
        btnShowPages.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowPagesActionPerformed(evt);
            }
        });

        btnSpan.setText("Run Span");
        btnSpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSpanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnClear)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnShowPages, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSpan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 76, Short.MAX_VALUE)
                        .addComponent(btnGo))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(tbxEndPage, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(tbxStartPage, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tbxStartPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tbxEndPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGo)
                    .addComponent(btnClear)
                    .addComponent(btnShowPages)
                    .addComponent(btnSpan))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGoActionPerformed
        String startPage = tbxStartPage.getText();
        String endPage = tbxEndPage.getText();
        System.out.println("\n\n");

        if (graph.findWikiPageByTitle(startPage) != null || graph.findWikiPageByTitle(endPage) != null) {
                wikiPage start = graph.findWikiPageByTitle(startPage);
                wikiPage end = graph.findWikiPageByTitle(endPage);

                start.tentative = -2;
                txaResults.append("\nFinding Minimum Path:\nfrom:\t" + startPage + "\nto:\t" + endPage + "\n");

                graph.path.add(start.title);
                graph.dijkstraMinPath(start, start, end);

                ArrayList<String> path = graph.path;
                for (String s : path) {
                    System.out.println(s);
                    txaResults.append("\n" + s);
                }
                graph.path.clear();
            
        }else{
            txaResults.append("\nBad Input!");
        }

    }//GEN-LAST:event_btnGoActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        txaResults.setText("");
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnShowPagesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowPagesActionPerformed
        StringBuilder str = new StringBuilder();
        for (wikiPage w : graph.wikiPages) {
            str.append(w.title + "\n");
        }
        str.append("\n" + graph.size() + " pages");
        txaResults.setText(txaResults.getText() + "\n" + str.toString());

    }//GEN-LAST:event_btnShowPagesActionPerformed

    private void btnSpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSpanActionPerformed
        int pos = 0;
        for (wikiPage p : graph.wikiPages) {
            if (graph.findSpanningTree(p) == true) {
                pos++;
            }
        }
        txaResults.append("\n" + pos + " Spanning Trees");
    }//GEN-LAST:event_btnSpanActionPerformed

    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(A3GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(A3GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(A3GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(A3GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new A3GUI("").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnGo;
    private javax.swing.JButton btnShowPages;
    private javax.swing.JButton btnSpan;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField tbxEndPage;
    private javax.swing.JTextField tbxStartPage;
    private javax.swing.JTextArea txaResults;
    // End of variables declaration//GEN-END:variables
}
