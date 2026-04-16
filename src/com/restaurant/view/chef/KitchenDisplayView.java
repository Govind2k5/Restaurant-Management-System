package com.restaurant.view.chef;

import com.restaurant.controller.ChefController;
import com.restaurant.model.Order;
import com.restaurant.view.UITheme;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class KitchenDisplayView extends JFrame {
    private ChefController ctrl;
    private JTable ordersTable;
    private DefaultTableModel model;
    private JLabel pendingLbl, inProgressLbl, readyLbl;

    public KitchenDisplayView(ChefController ctrl) {
        this.ctrl=ctrl; initUI(); refreshOrders();
        // Auto-refresh every 10 seconds
        new Timer(10000, e->refreshOrders()).start();
    }

    private void initUI() {
        setTitle("Kitchen Display System");
        setSize(960,620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root=UITheme.basePanel();
        root.add(UITheme.headerBar("🍳  Kitchen Display System","Observer Pattern — auto-refreshes when Waiter places orders",UITheme.ACCENT4),BorderLayout.NORTH);

        // Stat row
        JPanel stats=new JPanel(new GridLayout(1,3,12,0));
        stats.setBackground(UITheme.BG_DEEP);
        stats.setBorder(new EmptyBorder(14,14,8,14));
        pendingLbl    = statCard("0","PENDING",    new Color(220,120,40));
        inProgressLbl = statCard("0","IN PROGRESS",UITheme.ACCENT3);
        readyLbl      = statCard("0","READY",      UITheme.ACCENT2);
        stats.add(pendingLbl.getParent()); stats.add(inProgressLbl.getParent()); stats.add(readyLbl.getParent());

        // Table
        String[] cols={"Order ID","Table","Waiter","Status","Time","Total"};
        model=new DefaultTableModel(cols,0){public boolean isCellEditable(int r,int c){return false;}};
        ordersTable=new JTable(model);
        ordersTable.setRowHeight(34);
        ordersTable.setBackground(UITheme.BG_CARD); ordersTable.setForeground(UITheme.TEXT_PRI);
        ordersTable.setFont(UITheme.FONT_LABEL); ordersTable.setGridColor(UITheme.BORDER);
        ordersTable.setShowVerticalLines(false);
        ordersTable.getTableHeader().setBackground(UITheme.BG_DEEP);
        ordersTable.getTableHeader().setForeground(UITheme.ACCENT4);
        ordersTable.getTableHeader().setFont(new Font("Segoe UI",Font.BOLD,12));
        ordersTable.getTableHeader().setBorder(BorderFactory.createMatteBorder(0,0,2,0,UITheme.ACCENT4));
        ordersTable.setSelectionBackground(new Color(60,80,130));

        ordersTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
            public Component getTableCellRendererComponent(JTable t,Object v,boolean sel,boolean foc,int r,int c){
                super.getTableCellRendererComponent(t,v,sel,foc,r,c);
                if(sel){ setBackground(new Color(60,80,130)); setForeground(Color.WHITE); }
                else {
                    String status=(String)model.getValueAt(r,3);
                    switch(status){
                        case "PENDING":     setBackground(new Color(45,25,10)); setForeground(new Color(255,170,80)); break;
                        case "IN_PROGRESS": setBackground(new Color(10,25,50)); setForeground(new Color(100,160,255)); break;
                        case "READY":       setBackground(new Color(10,40,20)); setForeground(new Color(80,220,120)); break;
                        default: setBackground(r%2==0?UITheme.BG_CARD:UITheme.BG_ROW_ALT); setForeground(UITheme.TEXT_PRI);
                    }
                }
                setBorder(new EmptyBorder(0,12,0,12));
                setFont(UITheme.FONT_LABEL);
                return this;
            }
        });

        JScrollPane sp=UITheme.styledScroll(ordersTable);

        // Legend
        JPanel legend=new JPanel(new FlowLayout(FlowLayout.LEFT,22,8));
        legend.setBackground(new Color(18,24,38));
        legend.setBorder(BorderFactory.createMatteBorder(1,0,0,0,UITheme.BORDER));
        legend.add(dot(new Color(255,170,80),"PENDING — waiting to be processed"));
        legend.add(dot(new Color(100,160,255),"IN PROGRESS — currently cooking"));
        legend.add(dot(new Color(80,220,120),"READY — awaiting waiter pickup"));

        // Action buttons
        JPanel btns=new JPanel(new FlowLayout(FlowLayout.CENTER,14,10));
        btns.setBackground(new Color(18,24,38));

        JButton startBtn  = UITheme.primaryButton("▶  Start Processing", UITheme.ACCENT3);
        JButton readyBtn  = UITheme.primaryButton("✓  Mark as READY",    UITheme.ACCENT2);
        JButton refreshBtn= UITheme.primaryButton("⟳  Refresh",           new Color(70,80,110));

        startBtn.setPreferredSize(new Dimension(180,38));
        readyBtn.setPreferredSize(new Dimension(180,38));
        startBtn.addActionListener(e->handleStart());
        readyBtn.addActionListener(e->handleReady());
        refreshBtn.addActionListener(e->refreshOrders());

        btns.add(startBtn); btns.add(readyBtn); btns.add(refreshBtn);

        JPanel south=new JPanel(new BorderLayout());
        south.setBackground(new Color(18,24,38));
        south.add(legend,BorderLayout.NORTH); south.add(btns,BorderLayout.CENTER);

        JPanel center=new JPanel(new BorderLayout(0,0));
        center.setBackground(UITheme.BG_DEEP);
        center.setBorder(new EmptyBorder(0,14,0,14));
        center.add(sp,BorderLayout.CENTER);

        root.add(stats,BorderLayout.NORTH);
        root.add(center,BorderLayout.CENTER);
        root.add(south,BorderLayout.SOUTH);
        setContentPane(root);
        setVisible(true);
    }

    public void refreshOrders() {
        SwingUtilities.invokeLater(()->{
            model.setRowCount(0);
            List<Order> orders=ctrl.getActiveOrders();
            long p=orders.stream().filter(o->"PENDING".equals(o.getStatus())).count();
            long ip=orders.stream().filter(o->"IN_PROGRESS".equals(o.getStatus())).count();
            long r=orders.stream().filter(o->"READY".equals(o.getStatus())).count();
            pendingLbl.setText(String.valueOf(p));
            inProgressLbl.setText(String.valueOf(ip));
            readyLbl.setText(String.valueOf(r));
            for(Order o:orders)
                model.addRow(new Object[]{o.getOrderId(),"Table "+o.getTableId(),o.getWaiterId(),o.getStatus(),o.getOrderTime(),String.format("$%.2f",o.getTotalAmount())});
        });
    }

    private JLabel statCard(String val, String label, Color accent) {
        JPanel p=new JPanel(new BorderLayout(0,4)){
            protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UITheme.BG_CARD); g2.fillRoundRect(0,0,getWidth(),getHeight(),12,12);
                g2.setColor(accent); g2.setStroke(new BasicStroke(1.5f)); g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,12,12);
                g2.dispose();
            }
        };
        p.setOpaque(false); p.setBorder(new EmptyBorder(14,18,14,18));
        JLabel vl=new JLabel(val,SwingConstants.CENTER); vl.setFont(new Font("Segoe UI",Font.BOLD,32)); vl.setForeground(accent);
        JLabel nl=new JLabel(label,SwingConstants.CENTER); nl.setFont(UITheme.FONT_SMALL); nl.setForeground(UITheme.TEXT_SEC);
        p.add(vl,BorderLayout.CENTER); p.add(nl,BorderLayout.SOUTH);
        // We need to return the label to update it, but it's inside the panel, so we'll use a trick
        // Actually return vl and let the panel be added from outside
        // Since we return JLabel, caller accesses parent via vl.getParent() after it's added
        // Use a different approach: store in field
        vl.putClientProperty("panel", p);
        return vl;
    }

    // Helper to build legend dots
    private JLabel dot(Color c, String text) {
        JLabel l=new JLabel("● "+text); l.setFont(UITheme.FONT_SMALL); l.setForeground(c); return l;
    }

    private void handleStart() {
        int r=ordersTable.getSelectedRow();
        if(r<0){err("Select an order.");return;}
        int id=(int)model.getValueAt(r,0);
        if(ctrl.startProcessingOrder(id)) refreshOrders();
        else err("Could not update order.");
    }

    private void handleReady() {
        int r=ordersTable.getSelectedRow();
        if(r<0){err("Select an order.");return;}
        int id=(int)model.getValueAt(r,0);
        if(ctrl.markOrderReady(id)){
            refreshOrders();
            JOptionPane.showMessageDialog(this,"✓ Order #"+id+" is READY for service!","Order Ready",JOptionPane.INFORMATION_MESSAGE);
        } else err("Could not update order.");
    }

    private void err(String m){JOptionPane.showMessageDialog(this,m,"Error",JOptionPane.ERROR_MESSAGE);}
}
