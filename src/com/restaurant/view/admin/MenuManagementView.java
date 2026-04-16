package com.restaurant.view.admin;

import com.restaurant.controller.AdminController;
import com.restaurant.model.MenuItem;
import com.restaurant.view.UITheme;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MenuManagementView extends JFrame {

    private AdminController ctrl;
    private JTable table;
    private DefaultTableModel model;
    private JTextField nameField, priceField;
    private JComboBox<String> typeCombo;
    private JCheckBox availBox;
    private JLabel countLabel;

    public MenuManagementView(AdminController ctrl) {
        this.ctrl = ctrl;
        initUI();
        loadItems();
    }

    private void initUI() {
        setTitle("Menu Management");
        setSize(980, 640);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = UITheme.basePanel();
        root.add(UITheme.headerBar("🍴  Menu Management",
                "Factory Pattern: creates FoodItem / BeverageItem", UITheme.ACCENT), BorderLayout.NORTH);

        // ── Table area ──
        String[] cols = {"ID", "Name", "Price", "Type", "Available"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        UITheme.styleTable(table, UITheme.ACCENT);
        JScrollPane sp = UITheme.styledScroll(table);

        countLabel = new JLabel("0 items");
        countLabel.setFont(UITheme.FONT_SMALL);
        countLabel.setForeground(UITheme.TEXT_SEC);

        JPanel tablePanel = new JPanel(new BorderLayout(0, 8));
        tablePanel.setBackground(UITheme.BG_DEEP);
        tablePanel.setBorder(new EmptyBorder(14, 14, 14, 8));
        tablePanel.add(sp, BorderLayout.CENTER);
        tablePanel.add(countLabel, BorderLayout.SOUTH);

        // ── Right form panel ──
        JPanel form = buildForm();

        root.add(tablePanel, BorderLayout.CENTER);
        root.add(form, BorderLayout.EAST);
        setContentPane(root);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) populateFromRow();
        });
        setVisible(true);
    }

    private JPanel buildForm() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(UITheme.BG_DEEP);
        wrapper.setBorder(new EmptyBorder(14, 8, 14, 14));
        wrapper.setPreferredSize(new Dimension(270, 0));

        JPanel card = UITheme.cardPanel();
        card.setLayout(new GridBagLayout());

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1; g.gridx = 0;
        g.insets = new Insets(5, 0, 5, 0);

        JLabel title = UITheme.sectionLabel("Item Details", UITheme.ACCENT);
        g.gridy = 0; g.insets = new Insets(0, 0, 14, 0);
        card.add(title, g);

        g.insets = new Insets(4, 0, 4, 0);

        nameField  = UITheme.styledField();
        priceField = UITheme.styledField();
        typeCombo  = UITheme.styledCombo(new String[]{"FOOD","BEVERAGE"});
        availBox   = new JCheckBox("Available", true);
        availBox.setBackground(UITheme.BG_CARD);
        availBox.setForeground(UITheme.TEXT_PRI);
        availBox.setFont(UITheme.FONT_LABEL);

        addRow(card, g, 1, "Item Name", nameField);
        addRow(card, g, 3, "Price ($)", priceField);
        addRow(card, g, 5, "Type", typeCombo);
        g.gridy = 7; card.add(availBox, g);

        // Button strip
        JPanel btns = new JPanel(new GridLayout(2, 2, 6, 6));
        btns.setBackground(UITheme.BG_CARD);
        btns.setBorder(new EmptyBorder(14, 0, 0, 0));

        JButton addBtn    = UITheme.primaryButton("＋ Add",    new Color(50,160,90));
        JButton updateBtn = UITheme.primaryButton("✎ Update",  UITheme.ACCENT3);
        JButton deleteBtn = UITheme.primaryButton("✕ Delete",  UITheme.DANGER);
        JButton clearBtn  = UITheme.primaryButton("↺ Clear",   new Color(80,90,120));

        addBtn.addActionListener(e    -> handleAdd());
        updateBtn.addActionListener(e -> handleUpdate());
        deleteBtn.addActionListener(e -> handleDelete());
        clearBtn.addActionListener(e  -> clearForm());

        btns.add(addBtn); btns.add(updateBtn);
        btns.add(deleteBtn); btns.add(clearBtn);

        g.gridy = 8; card.add(btns, g);

        wrapper.add(card, BorderLayout.NORTH);
        return wrapper;
    }

    private void addRow(JPanel p, GridBagConstraints g, int row, String label, JComponent field) {
        g.gridy = row;
        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.BOLD, 10));
        l.setForeground(UITheme.TEXT_SEC);
        p.add(l, g);
        g.gridy = row + 1;
        p.add(field, g);
    }

    private void loadItems() {
        model.setRowCount(0);
        List<MenuItem> items = ctrl.getAllMenuItems();
        for (MenuItem i : items) {
            model.addRow(new Object[]{
                i.getItemId(), i.getName(),
                String.format("$%.2f", i.getPrice()),
                i.getItemType(), i.isAvailable() ? "✓ Yes" : "✗ No"
            });
        }
        countLabel.setText(items.size() + " items");
    }

    private void handleAdd() {
        try {
            String name = nameField.getText().trim();
            double price = Double.parseDouble(priceField.getText().trim());
            if (name.isEmpty()) { err("Name is required."); return; }
            boolean ok = ctrl.addMenuItem(name, price, availBox.isSelected(),
                    (String)typeCombo.getSelectedItem(), 1);
            if (ok) { loadItems(); clearForm(); ok("Item added!"); }
            else err("Failed to add item.");
        } catch (NumberFormatException ex) { err("Enter a valid price."); }
    }

    private void handleUpdate() {
        int row = table.getSelectedRow();
        if (row < 0) { err("Select an item first."); return; }
        try {
            int id = (int)model.getValueAt(row, 0);
            boolean ok = ctrl.updateMenuItem(id, nameField.getText().trim(),
                    Double.parseDouble(priceField.getText().trim()),
                    availBox.isSelected(), (String)typeCombo.getSelectedItem(), 1);
            if (ok) { loadItems(); ok("Updated!"); }
            else err("Update failed.");
        } catch (NumberFormatException ex) { err("Enter a valid price."); }
    }

    private void handleDelete() {
        int row = table.getSelectedRow();
        if (row < 0) { err("Select an item."); return; }
        int id = (int)model.getValueAt(row, 0);
        if (JOptionPane.showConfirmDialog(this,"Delete this item?","Confirm",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (ctrl.deleteMenuItem(id)) { loadItems(); clearForm(); }
            else err("Delete failed.");
        }
    }

    private void populateFromRow() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        nameField.setText((String)model.getValueAt(row,1));
        priceField.setText(model.getValueAt(row,2).toString().replace("$",""));
        typeCombo.setSelectedItem(model.getValueAt(row,3));
        availBox.setSelected("✓ Yes".equals(model.getValueAt(row,4)));
    }

    private void clearForm() {
        nameField.setText(""); priceField.setText("");
        typeCombo.setSelectedIndex(0); availBox.setSelected(true);
        table.clearSelection();
    }

    private void err(String msg) { JOptionPane.showMessageDialog(this,msg,"Error",JOptionPane.ERROR_MESSAGE); }
    private void ok(String msg)  { JOptionPane.showMessageDialog(this,msg,"Success",JOptionPane.INFORMATION_MESSAGE); }
}
