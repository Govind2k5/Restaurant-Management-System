package com.restaurant.view;

import com.restaurant.dao.UserDAO;
import com.restaurant.model.User;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class LoginView extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel statusLabel;
    private UserDAO userDAO;

    public LoginView() {
        this.userDAO = new UserDAO();
        UITheme.applyGlobalDefaults();
        initUI();
    }

    private void initUI() {
        setTitle("Restaurant Management System — Staff Portal");
        setSize(480, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Root panel with gradient background
        JPanel root = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(8, 11, 20), 0, getHeight(), new Color(15, 20, 36));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // decorative glow top-right
                g2.setColor(new Color(212, 175, 55, 25));
                g2.fillOval(getWidth() - 200, -100, 320, 320);
                // bottom-left glow
                g2.setColor(new Color(99, 155, 255, 15));
                g2.fillOval(-80, getHeight() - 220, 300, 300);
                g2.dispose();
            }
        };
        root.setOpaque(false);

        // Center card
        JPanel card = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(20, 26, 44, 240));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(new Color(45, 55, 80));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                // gold accent line on top
                g2.setColor(UITheme.ACCENT);
                g2.setStroke(new BasicStroke(2.5f));
                g2.drawLine(40, 0, getWidth() - 40, 0);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(38, 48, 36, 48));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 0, 6, 0);

        // Icon
        JLabel icon = new JLabel("🍽", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 50));
        gbc.gridy = 0; gbc.insets = new Insets(0, 0, 6, 0);
        card.add(icon, gbc);

        // App title
        JLabel appTitle = new JLabel("Restaurant MS", SwingConstants.CENTER);
        appTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        appTitle.setForeground(UITheme.ACCENT);
        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 2, 0);
        card.add(appTitle, gbc);

        // Subtitle
        JLabel sub = new JLabel("Management System", SwingConstants.CENTER);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(UITheme.TEXT_SEC);
        gbc.gridy = 2; gbc.insets = new Insets(0, 0, 6, 0);
        card.add(sub, gbc);

        // Staff Portal badge
        JPanel badge = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        badge.setOpaque(false);
        JLabel badgeLbl = new JLabel("  STAFF PORTAL  ");
        badgeLbl.setFont(new Font("Segoe UI", Font.BOLD, 9));
        badgeLbl.setForeground(new Color(99, 155, 255));
        badgeLbl.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(99, 155, 255, 120), 1),
            new EmptyBorder(3, 6, 3, 6)
        ));
        badge.add(badgeLbl);
        gbc.gridy = 3; gbc.insets = new Insets(0, 0, 24, 0);
        card.add(badge, gbc);

        // Divider
        JSeparator sep = new JSeparator();
        sep.setForeground(UITheme.BORDER);
        gbc.gridy = 4; gbc.insets = new Insets(0, 0, 22, 0);
        card.add(sep, gbc);

        // Username
        gbc.gridy = 5; gbc.insets = new Insets(0, 0, 5, 0);
        card.add(makeLabel("USERNAME"), gbc);
        usernameField = UITheme.styledField();
        usernameField.setPreferredSize(new Dimension(340, 42));
        gbc.gridy = 6; gbc.insets = new Insets(0, 0, 16, 0);
        card.add(usernameField, gbc);

        // Password
        gbc.gridy = 7; gbc.insets = new Insets(0, 0, 5, 0);
        card.add(makeLabel("PASSWORD"), gbc);
        passwordField = UITheme.styledPassword();
        passwordField.setPreferredSize(new Dimension(340, 42));
        gbc.gridy = 8; gbc.insets = new Insets(0, 0, 26, 0);
        card.add(passwordField, gbc);

        // Login button
        JButton loginBtn = new JButton("SIGN IN") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color base = UITheme.ACCENT;
                Color bg = getModel().isPressed() ? base.darker()
                         : getModel().isRollover() ? base.brighter()
                         : base;
                GradientPaint gp = new GradientPaint(0, 0, bg.brighter(), 0, getHeight(), bg);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        loginBtn.setForeground(new Color(18, 18, 28));
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginBtn.setPreferredSize(new Dimension(340, 46));
        loginBtn.setContentAreaFilled(false);
        loginBtn.setBorderPainted(false);
        loginBtn.setFocusPainted(false);
        loginBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.gridy = 9; gbc.insets = new Insets(0, 0, 12, 0);
        card.add(loginBtn, gbc);

        // Status label
        statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setFont(UITheme.FONT_SMALL);
        statusLabel.setForeground(UITheme.DANGER);
        gbc.gridy = 10; gbc.insets = new Insets(0, 0, 0, 0);
        card.add(statusLabel, gbc);

        root.add(card, BorderLayout.CENTER);
        root.setBorder(new EmptyBorder(48, 44, 48, 44));

        // Bottom footer bar
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(0, 44, 14, 44));
        JLabel footerLbl = new JLabel("Accounts are created by the system administrator", SwingConstants.CENTER);
        footerLbl.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        footerLbl.setForeground(new Color(65, 75, 100));
        footer.add(footerLbl, BorderLayout.CENTER);
        root.add(footer, BorderLayout.SOUTH);

        setContentPane(root);
        loginBtn.addActionListener(e -> handleLogin());
        passwordField.addActionListener(e -> handleLogin());
        usernameField.addActionListener(e -> passwordField.requestFocus());

        setVisible(true);
    }

    private JLabel makeLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 10));
        l.setForeground(UITheme.TEXT_SEC);
        return l;
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please enter your username and password.");
            return;
        }
        statusLabel.setText("");
        User user = userDAO.login(username, password);
        if (user == null) {
            statusLabel.setText("Invalid credentials. Please try again.");
            passwordField.setText("");
            shake(this);
            return;
        }
        dispose();
        SwingUtilities.invokeLater(() -> openDashboard(user));
    }

    private void openDashboard(User user) {
        switch (user.getRole()) {
            case "ADMIN":   new com.restaurant.view.admin.AdminDashboard(user);     break;
            case "WAITER":  new com.restaurant.view.waiter.WaiterDashboard(user);   break;
            case "CHEF":    new com.restaurant.view.chef.ChefDashboard(user);       break;
            case "MANAGER": new com.restaurant.view.manager.ManagerDashboard(user); break;
            default: JOptionPane.showMessageDialog(null, "Unknown role: " + user.getRole());
        }
    }

    private void shake(JFrame frame) {
        final int[] moves = {-8, 8, -6, 6, -4, 4, -2, 2, 0};
        final int[] idx = {0};
        Timer t = new Timer(30, null);
        Point orig = frame.getLocation();
        t.addActionListener(e -> {
            frame.setLocation(orig.x + moves[idx[0]], orig.y);
            if (++idx[0] >= moves.length) { t.stop(); frame.setLocation(orig); }
        });
        t.start();
    }
}
