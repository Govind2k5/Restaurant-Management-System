package com.restaurant.view.manager;

import com.restaurant.controller.ManagerController;
import com.restaurant.model.Bill;
import com.restaurant.model.Order;
import com.restaurant.model.Receipt;
import com.restaurant.view.UITheme;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BillingView extends JFrame {
    private ManagerController ctrl;
    private JTable ordersTable;
    private DefaultTableModel ordersModel;
    private JTextField orderIdField, discountField;
    private JTextArea receiptArea;
    private JButton generateBtn, printReceiptBtn;
    private Bill lastBill;

    public BillingView(ManagerController ctrl) {
        this.ctrl=ctrl; initUI(); load();
    }

    private void initUI() {
        setTitle("Billing & Invoicing");
        setSize(1060,660);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root=UITheme.basePanel();
        root.add(UITheme.headerBar("💰  Billing & Invoicing","Facade Pattern — one call handles tax, service charge & discounts",UITheme.ACCENT),BorderLayout.NORTH);

        // Left: ready orders
        JPanel left=new JPanel(new BorderLayout(0,8));
        left.setBackground(UITheme.BG_DEEP);
        left.setBorder(new EmptyBorder(14,14,14,8));

        JLabel lbl=UITheme.sectionLabel("Ready Orders",UITheme.ACCENT2);
        lbl.setBorder(new EmptyBorder(0,0,8,0));

        String[] cols={"Order ID","Table","Total","Status","Time"};
        ordersModel=new DefaultTableModel(cols,0){public boolean isCellEditable(int r,int c){return false;}};
        ordersTable=new JTable(ordersModel);
        UITheme.styleTable(ordersTable,UITheme.ACCENT2);

        JButton refreshBtn=UITheme.primaryButton("⟳  Refresh",new Color(70,80,110));
        refreshBtn.addActionListener(e->load());

        left.add(lbl,BorderLayout.NORTH);
        left.add(UITheme.styledScroll(ordersTable),BorderLayout.CENTER);
        left.add(refreshBtn,BorderLayout.SOUTH);

        // Right: form + receipt
        JPanel right=new JPanel(new BorderLayout(0,12));
        right.setBackground(UITheme.BG_DEEP);
        right.setBorder(new EmptyBorder(14,8,14,14));
        right.setPreferredSize(new Dimension(360,0));

        JPanel formCard=UITheme.cardPanel();
        formCard.setLayout(new GridBagLayout());
        GridBagConstraints g=new GridBagConstraints(); g.fill=GridBagConstraints.HORIZONTAL; g.weightx=1; g.gridx=0; g.insets=new Insets(5,0,5,0);

        g.gridy=0; g.insets=new Insets(0,0,14,0);
        formCard.add(UITheme.sectionLabel("Generate Bill",UITheme.ACCENT),g);
        g.insets=new Insets(4,0,4,0);
        orderIdField=UITheme.styledField(); discountField=UITheme.styledField(); discountField.setText("0");
        addRow(formCard,g,1,"Order ID",orderIdField);
        addRow(formCard,g,3,"Discount (%)",discountField);

        JPanel btnRow=new JPanel(new GridLayout(1,2,8,0)); btnRow.setBackground(UITheme.BG_CARD); btnRow.setBorder(new EmptyBorder(12,0,0,0));
        generateBtn    =UITheme.primaryButton("⚡ Generate Bill",UITheme.ACCENT);
        printReceiptBtn=UITheme.primaryButton("🖨 Print Receipt",UITheme.ACCENT2);
        printReceiptBtn.setEnabled(false);
        generateBtn.addActionListener(e->handleGenerate());
        printReceiptBtn.addActionListener(e->handlePrint());
        btnRow.add(generateBtn); btnRow.add(printReceiptBtn);
        g.gridy=5; formCard.add(btnRow,g);

        // Receipt area
        receiptArea=new JTextArea();
        receiptArea.setEditable(false);
        receiptArea.setBackground(new Color(10,20,15));
        receiptArea.setForeground(new Color(100,255,150));
        receiptArea.setFont(new Font("Courier New",Font.PLAIN,13));
        receiptArea.setCaretColor(new Color(100,255,150));
        receiptArea.setBorder(new EmptyBorder(14,14,14,14));
        receiptArea.setText("Bill receipt will appear here...\n\nSelect a READY order and click\n'Generate Bill' to proceed.");
        JScrollPane rsp=new JScrollPane(receiptArea);
        rsp.setBorder(BorderFactory.createLineBorder(new Color(30,80,50),1));

        right.add(formCard,BorderLayout.NORTH);
        right.add(rsp,BorderLayout.CENTER);

        JSplitPane split=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,left,right);
        split.setDividerLocation(520); split.setBackground(UITheme.BG_DEEP); split.setBorder(null);
        root.add(split,BorderLayout.CENTER);
        setContentPane(root);

        ordersTable.getSelectionModel().addListSelectionListener(e->{
            if(!e.getValueIsAdjusting()){
                int r=ordersTable.getSelectedRow();
                if(r>=0) orderIdField.setText(ordersModel.getValueAt(r,0).toString());
            }
        });
        setVisible(true);
    }

    private void load(){
        ordersModel.setRowCount(0);
        for(Order o:ctrl.getReadyOrders())
            ordersModel.addRow(new Object[]{o.getOrderId(),"Table "+o.getTableId(),String.format("$%.2f",o.getTotalAmount()),o.getStatus(),o.getOrderTime()});
    }

    private void handleGenerate(){
        try{
            int id=Integer.parseInt(orderIdField.getText().trim());
            double disc=Double.parseDouble(discountField.getText().trim())/100.0;
            lastBill=ctrl.generateBill(id,disc);
            if(lastBill==null){err("Order not found or already billed.");return;}
            receiptArea.setText(buildReceipt(lastBill,id,disc));
            printReceiptBtn.setEnabled(true); load();
        }catch(NumberFormatException ex){err("Valid Order ID and Discount required.");}
    }

    private void handlePrint(){
        if(lastBill==null) return;
        Receipt r=ctrl.printReceipt(lastBill);
        if(r!=null) JOptionPane.showMessageDialog(this,"✓ Receipt #"+r.getReceiptId()+" saved.\nDate: "+r.getDate(),"Receipt Saved",JOptionPane.INFORMATION_MESSAGE);
    }

    private String buildReceipt(Bill b,int orderId,double discPct){
        return  "╔══════════════════════════╗\n"+
                "║    RESTAURANT RECEIPT    ║\n"+
                "╠══════════════════════════╣\n"+
                String.format("║  Bill ID  : %-13d║%n", b.getBillId())+
                String.format("║  Order ID : %-13d║%n", orderId)+
                "╠══════════════════════════╣\n"+
                String.format("║  Subtotal : $%-12.2f║%n", b.getTotalAmount())+
                String.format("║  Discount : -$%-11.2f║%n", b.getDiscount())+
                String.format("║  Tax(8%%)  : +$%-11.2f║%n", b.getTax())+
                String.format("║  Service  : +$%-11.2f║%n", b.getServiceCharge())+
                "╠══════════════════════════╣\n"+
                String.format("║  TOTAL    : $%-12.2f║%n", b.getFinalAmount())+
                "╠══════════════════════════╣\n"+
                "║  Thank you for dining!   ║\n"+
                "╚══════════════════════════╝\n";
    }

    private void addRow(JPanel p,GridBagConstraints g,int row,String label,JComponent field){
        g.gridy=row; JLabel l=new JLabel(label); l.setFont(new Font("Segoe UI",Font.BOLD,10)); l.setForeground(UITheme.TEXT_SEC); p.add(l,g);
        g.gridy=row+1; p.add(field,g);
    }
    private void err(String m){JOptionPane.showMessageDialog(this,m,"Error",JOptionPane.ERROR_MESSAGE);}
}
