package com.restaurant.view.waiter;

import com.restaurant.controller.WaiterController;
import com.restaurant.model.Table;
import com.restaurant.view.UITheme;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class TableReservationView extends JFrame {
    private WaiterController ctrl;
    private JTable tbl;
    private DefaultTableModel model;
    private JLabel availLabel;

    public TableReservationView(WaiterController ctrl) {
        this.ctrl=ctrl; initUI(); load();
    }

    private void initUI() {
        setTitle("Table Reservations");
        setSize(640,480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = UITheme.basePanel();
        root.add(UITheme.headerBar("🪑  Table Reservations","Toggle availability of dining tables", UITheme.ACCENT3), BorderLayout.NORTH);

        String[] cols={"Table ID","Capacity","Status"};
        model=new DefaultTableModel(cols,0){public boolean isCellEditable(int r,int c){return false;}};
        tbl=new JTable(model);
        tbl.setRowHeight(36);
        tbl.setBackground(UITheme.BG_CARD); tbl.setForeground(UITheme.TEXT_PRI);
        tbl.setFont(UITheme.FONT_LABEL); tbl.setGridColor(UITheme.BORDER);
        tbl.setShowVerticalLines(false);
        tbl.getTableHeader().setBackground(UITheme.BG_DEEP);
        tbl.getTableHeader().setForeground(UITheme.ACCENT3);
        tbl.getTableHeader().setFont(new Font("Segoe UI",Font.BOLD,12));
        tbl.getTableHeader().setBorder(BorderFactory.createMatteBorder(0,0,2,0,UITheme.ACCENT3));

        tbl.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
            public Component getTableCellRendererComponent(JTable t,Object v,boolean sel,boolean foc,int r,int c){
                super.getTableCellRendererComponent(t,v,sel,foc,r,c);
                String status=(String)model.getValueAt(r,2);
                if(sel){ setBackground(new Color(60,80,130)); setForeground(Color.WHITE); }
                else if("RESERVED".equals(status)){ setBackground(new Color(50,20,20)); setForeground(new Color(255,130,130)); }
                else { setBackground(r%2==0?UITheme.BG_CARD:UITheme.BG_ROW_ALT); setForeground(UITheme.ACCENT2); }
                setBorder(new EmptyBorder(0,12,0,12));
                setFont(UITheme.FONT_LABEL);
                return this;
            }
        });

        JPanel center = new JPanel(new BorderLayout(0,10));
        center.setBackground(UITheme.BG_DEEP);
        center.setBorder(new EmptyBorder(14,14,14,14));

        availLabel = new JLabel("");
        availLabel.setFont(UITheme.FONT_SMALL); availLabel.setForeground(UITheme.TEXT_SEC);

        center.add(UITheme.styledScroll(tbl),BorderLayout.CENTER);
        center.add(availLabel,BorderLayout.SOUTH);

        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.CENTER,14,12));
        btnBar.setBackground(new Color(18,24,38));
        btnBar.setBorder(BorderFactory.createMatteBorder(1,0,0,0,UITheme.BORDER));

        JButton reserveBtn = UITheme.primaryButton("🔒  Mark Reserved", new Color(200,120,40));
        JButton releaseBtn = UITheme.primaryButton("🔓  Mark Available", UITheme.ACCENT2);
        JButton refreshBtn = UITheme.primaryButton("⟳  Refresh",        new Color(70,80,110));

        reserveBtn.addActionListener(e->handleToggle(true));
        releaseBtn.addActionListener(e->handleToggle(false));
        refreshBtn.addActionListener(e->load());

        btnBar.add(reserveBtn); btnBar.add(releaseBtn); btnBar.add(refreshBtn);

        root.add(center, BorderLayout.CENTER);
        root.add(btnBar, BorderLayout.SOUTH);
        setContentPane(root);
        setVisible(true);
    }

    private void load() {
        model.setRowCount(0);
        List<Table> tables=ctrl.getAllTables();
        long avail=tables.stream().filter(t->!t.isReserved()).count();
        availLabel.setText(avail+" available, "+(tables.size()-avail)+" reserved");
        for(Table t:tables)
            model.addRow(new Object[]{t.getTableId(),t.getCapacity(),t.isReserved()?"RESERVED":"AVAILABLE"});
    }

    private void handleToggle(boolean reserve) {
        int r=tbl.getSelectedRow();
        if(r<0){JOptionPane.showMessageDialog(this,"Select a table.","Error",JOptionPane.ERROR_MESSAGE);return;}
        int id=(int)model.getValueAt(r,0);
        boolean ok=reserve?ctrl.reserveTable(id):ctrl.releaseTable(id);
        if(ok) load();
        else JOptionPane.showMessageDialog(this,"Failed.","Error",JOptionPane.ERROR_MESSAGE);
    }
}
