package com.restaurant.view.chef;

import com.restaurant.controller.ChefController;
import com.restaurant.model.Inventory;
import com.restaurant.view.UITheme;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class InventoryView extends JFrame {
    private ChefController ctrl;
    private JTable tbl;
    private DefaultTableModel model;
    private JTextField nameField, qtyField;

    public InventoryView(ChefController ctrl) {
        this.ctrl=ctrl; initUI(); load();
    }

    private void initUI() {
        setTitle("Inventory Tracking");
        setSize(860,560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root=UITheme.basePanel();
        root.add(UITheme.headerBar("📦  Inventory Tracking","Manage stock — mark items out of stock",UITheme.ACCENT2),BorderLayout.NORTH);

        String[] cols={"ID","Item Name","Quantity","Status"};
        model=new DefaultTableModel(cols,0){public boolean isCellEditable(int r,int c){return false;}};
        tbl=new JTable(model);
        UITheme.styleTable(tbl,UITheme.ACCENT2);

        // Color by quantity
        tbl.getColumnModel().getColumn(3).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer(){
            public Component getTableCellRendererComponent(JTable t,Object v,boolean sel,boolean foc,int r,int c){
                super.getTableCellRendererComponent(t,v,sel,foc,r,c);
                int qty=0; try{qty=(Integer)model.getValueAt(r,2);}catch(Exception ignored){}
                if(!sel){
                    if(qty==0){setBackground(new Color(50,20,20));setForeground(new Color(255,100,100));}
                    else if(qty<5){setBackground(new Color(50,35,10));setForeground(new Color(255,185,80));}
                    else{setBackground(r%2==0?UITheme.BG_CARD:UITheme.BG_ROW_ALT);setForeground(UITheme.ACCENT2);}
                }
                setBorder(new EmptyBorder(0,12,0,12));
                return this;
            }
        });

        JPanel center=new JPanel(new BorderLayout(0,8));
        center.setBackground(UITheme.BG_DEEP);
        center.setBorder(new EmptyBorder(14,14,14,8));
        center.add(UITheme.styledScroll(tbl),BorderLayout.CENTER);

        JPanel form=buildForm();
        root.add(center,BorderLayout.CENTER);
        root.add(form,BorderLayout.EAST);
        setContentPane(root);

        tbl.getSelectionModel().addListSelectionListener(e->{
            if(!e.getValueIsAdjusting()){
                int r=tbl.getSelectedRow();
                if(r>=0){nameField.setText((String)model.getValueAt(r,1));qtyField.setText(model.getValueAt(r,2).toString());}
            }
        });
        setVisible(true);
    }

    private JPanel buildForm() {
        JPanel w=new JPanel(new BorderLayout()); w.setBackground(UITheme.BG_DEEP); w.setBorder(new EmptyBorder(14,8,14,14)); w.setPreferredSize(new Dimension(260,0));
        JPanel card=UITheme.cardPanel(); card.setLayout(new GridBagLayout());
        GridBagConstraints g=new GridBagConstraints(); g.fill=GridBagConstraints.HORIZONTAL; g.weightx=1; g.gridx=0; g.insets=new Insets(5,0,5,0);
        g.gridy=0; g.insets=new Insets(0,0,14,0); card.add(UITheme.sectionLabel("Inventory Item",UITheme.ACCENT2),g); g.insets=new Insets(4,0,4,0);
        nameField=UITheme.styledField(); qtyField=UITheme.styledField();
        row(card,g,1,"Item Name",nameField); row(card,g,3,"Quantity",qtyField);

        JPanel btns=new JPanel(new GridLayout(2,1,0,6)); btns.setBackground(UITheme.BG_CARD); btns.setBorder(new EmptyBorder(14,0,0,0));
        JButton addBtn=UITheme.primaryButton("＋ Add Item", new Color(50,160,90));
        JButton updBtn=UITheme.primaryButton("✎ Update Stock",UITheme.ACCENT3);
        addBtn.addActionListener(e->handleAdd()); updBtn.addActionListener(e->handleUpdate());
        btns.add(addBtn); btns.add(updBtn);
        g.gridy=5; card.add(btns,g);
        w.add(card,BorderLayout.NORTH); return w;
    }

    private void row(JPanel p,GridBagConstraints g,int row,String label,JComponent field){
        g.gridy=row; JLabel l=new JLabel(label); l.setFont(new Font("Segoe UI",Font.BOLD,10)); l.setForeground(UITheme.TEXT_SEC); p.add(l,g);
        g.gridy=row+1; p.add(field,g);
    }

    private void load(){
        model.setRowCount(0);
        for(Inventory i:ctrl.getAllInventory())
            model.addRow(new Object[]{i.getInventoryId(),i.getItemName(),i.getQuantity(),i.getQuantity()==0?"OUT OF STOCK":i.getQuantity()<5?"LOW STOCK":"OK"});
    }

    private void handleAdd(){
        try{
            String name=nameField.getText().trim(); int qty=Integer.parseInt(qtyField.getText().trim());
            if(name.isEmpty()){err("Name required.");return;}
            if(ctrl.addInventoryItem(name,qty)>0){load();nameField.setText("");qtyField.setText("");}
            else err("Failed.");
        }catch(NumberFormatException e){err("Valid quantity required.");}
    }

    private void handleUpdate(){
        int r=tbl.getSelectedRow();
        if(r<0){err("Select an item.");return;}
        try{
            int id=(int)model.getValueAt(r,0), qty=Integer.parseInt(qtyField.getText().trim());
            if(ctrl.updateInventoryStock(id,qty)) load();
            else err("Update failed.");
        }catch(NumberFormatException e){err("Valid quantity required.");}
    }

    private void err(String m){JOptionPane.showMessageDialog(this,m,"Error",JOptionPane.ERROR_MESSAGE);}
}
