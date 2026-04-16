package com.restaurant.view.admin;

import com.restaurant.controller.AdminController;
import com.restaurant.model.Employee;
import com.restaurant.view.UITheme;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EmployeeManagementView extends JFrame {
    private AdminController ctrl;
    private JTable table;
    private DefaultTableModel model;
    private JTextField nameField, usernameField, passwordField, salaryField;
    private JComboBox<String> roleCombo;

    public EmployeeManagementView(AdminController ctrl) {
        this.ctrl = ctrl; initUI(); load();
    }

    private void initUI() {
        setTitle("Employee Management");
        setSize(980, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = UITheme.basePanel();
        root.add(UITheme.headerBar("👥  Employee Management","Onboard staff with login credentials", UITheme.ACCENT3), BorderLayout.NORTH);

        String[] cols = {"ID","Name","Role","Salary"};
        model = new DefaultTableModel(cols,0){ public boolean isCellEditable(int r,int c){return false;} };
        table = new JTable(model);
        UITheme.styleTable(table, UITheme.ACCENT3);

        JPanel left = new JPanel(new BorderLayout(0,8));
        left.setBackground(UITheme.BG_DEEP);
        left.setBorder(new EmptyBorder(14,14,14,8));
        left.add(UITheme.styledScroll(table), BorderLayout.CENTER);

        JPanel form = buildForm();
        root.add(left, BorderLayout.CENTER);
        root.add(form, BorderLayout.EAST);
        setContentPane(root);
        setVisible(true);
    }

    private JPanel buildForm() {
        JPanel w = new JPanel(new BorderLayout());
        w.setBackground(UITheme.BG_DEEP);
        w.setBorder(new EmptyBorder(14,8,14,14));
        w.setPreferredSize(new Dimension(280,0));

        JPanel card = UITheme.cardPanel();
        card.setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL; g.weightx=1; g.gridx=0;
        g.insets = new Insets(4,0,4,0);

        g.gridy=0; g.insets=new Insets(0,0,14,0);
        card.add(UITheme.sectionLabel("New Employee",UITheme.ACCENT3),g);
        g.insets=new Insets(4,0,4,0);

        nameField     = UITheme.styledField();
        usernameField = UITheme.styledField();
        passwordField = UITheme.styledField();
        salaryField   = UITheme.styledField();
        roleCombo     = UITheme.styledCombo(new String[]{"WAITER","CHEF","MANAGER"});

        addRow(card,g,1,"Full Name",nameField);
        addRow(card,g,3,"Username",usernameField);
        addRow(card,g,5,"Password",passwordField);
        addRow(card,g,7,"Salary ($)",salaryField);
        addRow(card,g,9,"Role",roleCombo);

        JPanel btns = new JPanel(new GridLayout(1,2,8,0));
        btns.setBackground(UITheme.BG_CARD);
        btns.setBorder(new EmptyBorder(14,0,0,0));

        JButton addBtn = UITheme.primaryButton("＋ Add Employee", new Color(50,160,90));
        JButton remBtn = UITheme.primaryButton("✕ Remove Selected", UITheme.DANGER);
        addBtn.addActionListener(e -> handleAdd());
        remBtn.addActionListener(e -> handleRemove());
        btns.add(addBtn); btns.add(remBtn);
        g.gridy=11; card.add(btns,g);

        w.add(card, BorderLayout.NORTH);
        return w;
    }

    private void addRow(JPanel p, GridBagConstraints g, int row, String label, JComponent field) {
        g.gridy=row; JLabel l=new JLabel(label); l.setFont(new Font("Segoe UI",Font.BOLD,10)); l.setForeground(UITheme.TEXT_SEC); p.add(l,g);
        g.gridy=row+1; p.add(field,g);
    }

    private void load() {
        model.setRowCount(0);
        for (Employee e : ctrl.getAllEmployees())
            model.addRow(new Object[]{e.getEmployeeId(),e.getName(),e.getRole(),String.format("$%.2f",e.getSalary())});
    }

    private void handleAdd() {
        try {
            String name=nameField.getText().trim(), user=usernameField.getText().trim(),
                   pass=passwordField.getText().trim();
            double sal=Double.parseDouble(salaryField.getText().trim());
            String role=(String)roleCombo.getSelectedItem();
            if(name.isEmpty()||user.isEmpty()||pass.isEmpty()){err("All fields required.");return;}
            if(ctrl.addEmployee(name,role,sal,user,pass)){load();clearForm();ok("Employee added!");}
            else err("Failed. Username may exist.");
        }catch(NumberFormatException ex){err("Valid salary required.");}
    }

    private void handleRemove() {
        int row=table.getSelectedRow();
        if(row<0){err("Select an employee.");return;}
        int id=(int)model.getValueAt(row,0);
        if(JOptionPane.showConfirmDialog(this,"Remove employee?","Confirm",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
            if(ctrl.removeEmployee(id)) load(); else err("Remove failed.");
    }

    private void clearForm(){nameField.setText("");usernameField.setText("");passwordField.setText("");salaryField.setText("");}
    private void err(String m){JOptionPane.showMessageDialog(this,m,"Error",JOptionPane.ERROR_MESSAGE);}
    private void ok(String m){JOptionPane.showMessageDialog(this,m,"Success",JOptionPane.INFORMATION_MESSAGE);}
}
