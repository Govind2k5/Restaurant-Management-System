package com.restaurant.view.manager;

import com.restaurant.controller.ManagerController;
import com.restaurant.model.Bill;
import com.restaurant.model.Order;
import com.restaurant.model.SalesReport;
import com.restaurant.view.UITheme;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SalesReportView extends JFrame {
    private ManagerController ctrl;
    private JTable ordersTable, billsTable;
    private DefaultTableModel ordersModel, billsModel;
    private JLabel revenueLbl, countLbl;

    public SalesReportView(ManagerController ctrl) {
        this.ctrl=ctrl; initUI(); load();
    }

    private void initUI() {
        setTitle("Sales Reports");
        setSize(1060,660);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root=UITheme.basePanel();
        root.add(UITheme.headerBar("📊  Sales Reports","Daily revenue summary & full order history",UITheme.ACCENT3),BorderLayout.NORTH);

        // Stat cards row
        JPanel statsRow=new JPanel(new GridLayout(1,3,14,0));
        statsRow.setBackground(UITheme.BG_DEEP);
        statsRow.setBorder(new EmptyBorder(14,14,8,14));

        revenueLbl=new JLabel("$0.00",SwingConstants.CENTER);
        revenueLbl.setFont(new Font("Segoe UI",Font.BOLD,26));
        revenueLbl.setForeground(UITheme.ACCENT2);

        countLbl=new JLabel("0",SwingConstants.CENTER);
        countLbl.setFont(new Font("Segoe UI",Font.BOLD,26));
        countLbl.setForeground(UITheme.ACCENT3);

        statsRow.add(buildStatCard(revenueLbl,"Today's Revenue",UITheme.ACCENT2));
        statsRow.add(buildStatCard(countLbl,"Orders Today",UITheme.ACCENT3));
        statsRow.add(buildRefreshCard());

        // Tabs
        JTabbedPane tabs=new JTabbedPane();
        tabs.setBackground(UITheme.BG_DEEP); tabs.setForeground(UITheme.TEXT_PRI);
        tabs.setFont(UITheme.FONT_HEAD);
        tabs.setBorder(new EmptyBorder(0,14,14,14));

        // Tab 1
        String[] oc={"Order ID","Table","Waiter","Status","Total","Time"};
        ordersModel=new DefaultTableModel(oc,0){public boolean isCellEditable(int r,int c){return false;}};
        ordersTable=new JTable(ordersModel);
        UITheme.styleTable(ordersTable,UITheme.ACCENT3);
        tabs.addTab("  Today's Orders  ", UITheme.styledScroll(ordersTable));

        // Tab 2
        String[] bc={"Bill ID","Order ID","Subtotal","Discount","Tax","Service","Final"};
        billsModel=new DefaultTableModel(bc,0){public boolean isCellEditable(int r,int c){return false;}};
        billsTable=new JTable(billsModel);
        UITheme.styleTable(billsTable,UITheme.ACCENT);
        tabs.addTab("  All Bills  ", UITheme.styledScroll(billsTable));

        root.add(statsRow,BorderLayout.NORTH);
        root.add(tabs,BorderLayout.CENTER);
        setContentPane(root);
        setVisible(true);
    }

    private JPanel buildStatCard(JLabel valueLabel, String title, Color accent){
        JPanel p=new JPanel(new BorderLayout(0,6)){
            protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UITheme.BG_CARD); g2.fillRoundRect(0,0,getWidth(),getHeight(),12,12);
                g2.setColor(accent); g2.setStroke(new BasicStroke(1.5f)); g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,12,12);
                g2.dispose();
            }
        };
        p.setOpaque(false); p.setBorder(new EmptyBorder(16,18,16,18));
        JLabel tl=new JLabel(title,SwingConstants.CENTER); tl.setFont(UITheme.FONT_SMALL); tl.setForeground(UITheme.TEXT_SEC);
        p.add(valueLabel,BorderLayout.CENTER); p.add(tl,BorderLayout.SOUTH);
        return p;
    }

    private JPanel buildRefreshCard(){
        JPanel p=new JPanel(new BorderLayout(0,6)){
            protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UITheme.BG_CARD); g2.fillRoundRect(0,0,getWidth(),getHeight(),12,12);
                g2.setColor(UITheme.BORDER); g2.setStroke(new BasicStroke(1f)); g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,12,12);
                g2.dispose();
            }
        };
        p.setOpaque(false); p.setBorder(new EmptyBorder(16,18,16,18));
        JButton btn=UITheme.primaryButton("⟳  Refresh Report",new Color(70,80,110));
        btn.addActionListener(e->load());
        JLabel tl=new JLabel("Click to reload data",SwingConstants.CENTER); tl.setFont(UITheme.FONT_SMALL); tl.setForeground(UITheme.TEXT_SEC);
        p.add(btn,BorderLayout.CENTER); p.add(tl,BorderLayout.SOUTH);
        return p;
    }

    private void load(){
        SalesReport rep=ctrl.viewSalesReport();
        revenueLbl.setText(String.format("$%.2f",rep.getTotalSales()));
        ordersModel.setRowCount(0);
        List<Order> orders=ctrl.getTodayOrders();
        countLbl.setText(String.valueOf(orders.size()));
        for(Order o:orders)
            ordersModel.addRow(new Object[]{o.getOrderId(),"Table "+o.getTableId(),o.getWaiterId(),o.getStatus(),String.format("$%.2f",o.getTotalAmount()),o.getOrderTime()});
        billsModel.setRowCount(0);
        for(Bill b:ctrl.getAllBills())
            billsModel.addRow(new Object[]{b.getBillId(),b.getOrderId(),
                String.format("$%.2f",b.getTotalAmount()),String.format("$%.2f",b.getDiscount()),
                String.format("$%.2f",b.getTax()),String.format("$%.2f",b.getServiceCharge()),
                String.format("$%.2f",b.getFinalAmount())});
    }
}
