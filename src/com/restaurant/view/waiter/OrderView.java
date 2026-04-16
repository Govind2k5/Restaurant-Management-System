package com.restaurant.view.waiter;

import com.restaurant.controller.WaiterController;
import com.restaurant.model.MenuItem;
import com.restaurant.model.Table;
import com.restaurant.model.User;
import com.restaurant.view.UITheme;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OrderView extends JFrame {
    private WaiterController ctrl;
    private User waiter;
    private JComboBox<String> tableCombo;
    private JTable menuTable, cartTable;
    private DefaultTableModel menuModel, cartModel;
    private JLabel totalLabel, statusLabel;
    private List<MenuItem> menuItems;
    private List<Table> tables;

    public OrderView(WaiterController ctrl, User waiter) {
        this.ctrl=ctrl; this.waiter=waiter;
        initUI(); loadData();
    }

    private void initUI() {
        setTitle("Place Order");
        setSize(1060,660);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = UITheme.basePanel();
        root.add(UITheme.headerBar("🛒  Place Order","«includes» Verify Availability | Observer notifies Chef", UITheme.ACCENT2), BorderLayout.NORTH);

        // ── TOP BAR: table selector ──
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT,14,10));
        topBar.setBackground(new Color(18,24,38));
        topBar.setBorder(BorderFactory.createMatteBorder(0,0,1,0,UITheme.BORDER));

        JLabel tl = new JLabel("Select Table:");
        tl.setFont(UITheme.FONT_LABEL); tl.setForeground(UITheme.TEXT_SEC);

        tableCombo = UITheme.styledCombo(new String[]{});
        tableCombo.setPreferredSize(new Dimension(220,32));

        JButton startBtn = UITheme.primaryButton("▶  Start Order", UITheme.ACCENT2);
        startBtn.addActionListener(e->handleStart());

        statusLabel = new JLabel("Select a table and click Start Order");
        statusLabel.setFont(UITheme.FONT_SMALL); statusLabel.setForeground(UITheme.TEXT_SEC);

        topBar.add(tl); topBar.add(tableCombo); topBar.add(startBtn);
        topBar.add(Box.createHorizontalStrut(20)); topBar.add(statusLabel);

        // ── CENTER: menu | cart ──
        JPanel center = new JPanel(new GridLayout(1,2,12,0));
        center.setBackground(UITheme.BG_DEEP);
        center.setBorder(new EmptyBorder(12,12,12,12));

        // Menu
        JPanel menuPanel = new JPanel(new BorderLayout(0,8));
        menuPanel.setBackground(UITheme.BG_DEEP);
        JLabel ml = UITheme.sectionLabel("Available Menu",UITheme.ACCENT2);
        String[] mc={"ID","Name","Price","Type"};
        menuModel=new DefaultTableModel(mc,0){public boolean isCellEditable(int r,int c){return false;}};
        menuTable=new JTable(menuModel);
        UITheme.styleTable(menuTable, UITheme.ACCENT2);
        JButton addBtn = UITheme.primaryButton("Add to Cart  →", UITheme.ACCENT2);
        addBtn.addActionListener(e->handleAdd());
        menuPanel.add(ml,BorderLayout.NORTH);
        menuPanel.add(UITheme.styledScroll(menuTable),BorderLayout.CENTER);
        menuPanel.add(addBtn,BorderLayout.SOUTH);

        // Cart
        JPanel cartPanel = new JPanel(new BorderLayout(0,8));
        cartPanel.setBackground(UITheme.BG_DEEP);
        JLabel cl = UITheme.sectionLabel("Order Cart",UITheme.ACCENT);
        String[] cc={"ID","Item Name","Price"};
        cartModel=new DefaultTableModel(cc,0){public boolean isCellEditable(int r,int c){return false;}};
        cartTable=new JTable(cartModel);
        UITheme.styleTable(cartTable, UITheme.ACCENT);

        totalLabel = new JLabel("Total:  $0.00");
        totalLabel.setFont(new Font("Segoe UI",Font.BOLD,18));
        totalLabel.setForeground(UITheme.ACCENT);

        JButton remBtn    = UITheme.primaryButton("✕  Remove", UITheme.DANGER);
        JButton placeBtn  = UITheme.primaryButton("✓  PLACE ORDER", new Color(40,160,90));
        placeBtn.setFont(new Font("Segoe UI",Font.BOLD,14));
        remBtn.addActionListener(e->handleRemove());
        placeBtn.addActionListener(e->handlePlace());

        JPanel cartBottom = new JPanel(new BorderLayout(0,6));
        cartBottom.setBackground(UITheme.BG_DEEP);
        cartBottom.add(totalLabel,BorderLayout.NORTH);
        JPanel bb=new JPanel(new GridLayout(1,2,8,0)); bb.setBackground(UITheme.BG_DEEP);
        bb.add(remBtn); bb.add(placeBtn);
        cartBottom.add(bb,BorderLayout.CENTER);

        cartPanel.add(cl,BorderLayout.NORTH);
        cartPanel.add(UITheme.styledScroll(cartTable),BorderLayout.CENTER);
        cartPanel.add(cartBottom,BorderLayout.SOUTH);

        center.add(menuPanel); center.add(cartPanel);

        root.add(topBar,BorderLayout.NORTH); // override — add after header via content
        JPanel mainArea=new JPanel(new BorderLayout());
        mainArea.setBackground(UITheme.BG_DEEP);
        mainArea.add(topBar,BorderLayout.NORTH);
        mainArea.add(center,BorderLayout.CENTER);

        root.add(mainArea,BorderLayout.CENTER);
        setContentPane(root);
        setVisible(true);
    }

    private void loadData() {
        tables=ctrl.getAllTables();
        tableCombo.removeAllItems();
        for(Table t:tables)
            tableCombo.addItem("Table "+t.getTableId()+"  (cap:"+t.getCapacity()+")"+(t.isReserved()?" [RESERVED]":""));
        menuItems=ctrl.getAvailableMenuItems();
        menuModel.setRowCount(0);
        for(MenuItem i:menuItems)
            menuModel.addRow(new Object[]{i.getItemId(),i.getName(),String.format("$%.2f",i.getPrice()),i.getItemType()});
    }

    private void handleStart() {
        int idx=tableCombo.getSelectedIndex();
        if(idx<0||tables.isEmpty()){err("No table selected.");return;}
        Table t=tables.get(idx);
        ctrl.startNewOrder(t.getTableId(),waiter.getUserId());
        cartModel.setRowCount(0); updateTotal();
        statusLabel.setText("✓ Order started — Table "+t.getTableId());
        statusLabel.setForeground(UITheme.ACCENT2);
    }

    private void handleAdd() {
        int r=menuTable.getSelectedRow();
        if(r<0){err("Select a menu item.");return;}
        if(ctrl.getCurrentOrder()==null){err("Click 'Start Order' first.");return;}
        MenuItem item=menuItems.get(r);
        ctrl.addItemToOrder(item);
        cartModel.addRow(new Object[]{item.getItemId(),item.getName(),String.format("$%.2f",item.getPrice())});
        updateTotal();
    }

    private void handleRemove() {
        int r=cartTable.getSelectedRow();
        if(r<0){err("Select a cart item.");return;}
        int id=(int)cartModel.getValueAt(r,0);
        menuItems.stream().filter(i->i.getItemId()==id).findFirst().ifPresent(ctrl::removeItemFromOrder);
        cartModel.removeRow(r); updateTotal();
    }

    private void handlePlace() {
        if(ctrl.getCurrentOrder()==null||cartModel.getRowCount()==0){err("Cart is empty.");return;}
        boolean ok=ctrl.placeOrder();
        if(ok){
            JOptionPane.showMessageDialog(this,"✓ Order placed!\nChef has been notified via Observer.","Order Placed",JOptionPane.INFORMATION_MESSAGE);
            cartModel.setRowCount(0); updateTotal();
            statusLabel.setText("Order placed — Chef notified");
        } else err("Order failed. Some items may be unavailable.");
    }

    private void updateTotal() {
        totalLabel.setText(ctrl.getCurrentOrder()!=null
            ? String.format("Total:  $%.2f",ctrl.getCurrentOrder().getTotalAmount())
            : "Total:  $0.00");
    }

    private void err(String m){JOptionPane.showMessageDialog(this,m,"Error",JOptionPane.ERROR_MESSAGE);}
}
