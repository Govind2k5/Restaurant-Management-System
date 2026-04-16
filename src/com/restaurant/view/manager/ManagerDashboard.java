package com.restaurant.view.manager;

import com.restaurant.controller.ManagerController;
import com.restaurant.model.User;
import com.restaurant.view.LoginView;
import com.restaurant.view.UITheme;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;

public class ManagerDashboard extends JFrame {

    private User user;
    private ManagerController ctrl;

    public ManagerDashboard(User user) {
        this.user = user;
        this.ctrl = new ManagerController();
        initUI();
    }

    private void initUI() {
        setTitle("Restaurant MS — Manager Dashboard");
        setSize(1140, 680);
        setMinimumSize(new Dimension(900, 580));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel root = UITheme.basePanel();

        // Live stats
        double todaySales = 0.0;
        int readyOrders = 0, totalBills = 0;
        try { todaySales  = ctrl.viewSalesReport().getTotalSales(); } catch (Exception ignored) {}
        try { readyOrders = ctrl.getReadyOrders().size();           } catch (Exception ignored) {}
        try { totalBills  = ctrl.getAllBills().size();               } catch (Exception ignored) {}

        root.add(buildSidebar(), BorderLayout.WEST);
        root.add(buildContent(todaySales, readyOrders, totalBills), BorderLayout.CENTER);

        setContentPane(root);
        setVisible(true);
    }

    // ── Content ───────────────────────────────────────────────────────────────

    private JPanel buildContent(double todaySales, int readyOrders, int totalBills) {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(UITheme.BG_DEEP);
        content.add(buildHeader(), BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(0, 16));
        center.setBackground(UITheme.BG_DEEP);
        center.setBorder(new EmptyBorder(20, 22, 20, 22));

        // Stat cards
        JPanel statsRow = new JPanel(new GridLayout(1, 3, 14, 0));
        statsRow.setBackground(UITheme.BG_DEEP);
        statsRow.add(buildStatCard("💰", String.format("$%.0f", todaySales), "Today's Revenue",  UITheme.ACCENT));
        statsRow.add(buildStatCard("🧾", String.valueOf(readyOrders),        "Orders to Bill",   UITheme.ACCENT3));
        statsRow.add(buildStatCard("📋", String.valueOf(totalBills),         "Total Bills",      UITheme.ACCENT4));
        center.add(statsRow, BorderLayout.NORTH);

        // Feature cards
        JPanel cards = new JPanel(new GridLayout(1, 2, 14, 0));
        cards.setBackground(UITheme.BG_DEEP);
        cards.add(buildFeatureCard("💰", "Billing & Invoicing",
            "Generate bills for completed orders.\n" +
            "Apply discounts and calculate tax & service charge.\n" +
            "Facade Pattern hides all billing complexity.",
            UITheme.ACCENT, () -> new BillingView(ctrl)));
        cards.add(buildFeatureCard("📊", "Sales Reports",
            "View today's revenue and order count.\n" +
            "Browse full historical order & bill records.\n" +
            "Track daily performance at a glance.",
            UITheme.ACCENT3, () -> new SalesReportView(ctrl)));
        center.add(cards, BorderLayout.CENTER);

        content.add(center, BorderLayout.CENTER);
        content.add(buildStatusBar("Manager Module  |  Facade Pattern active"), BorderLayout.SOUTH);
        return content;
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(14, 18, 30));
        header.setBorder(new EmptyBorder(18, 24, 18, 24));

        int hour = LocalTime.now().getHour();
        String greeting = hour < 12 ? "Good morning" : hour < 17 ? "Good afternoon" : "Good evening";
        JLabel title = new JLabel(greeting + ", " + user.getName());
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(UITheme.ACCENT3);

        JLabel dateLabel = new JLabel(new SimpleDateFormat("EEEE, MMMM d yyyy").format(new Date()));
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        dateLabel.setForeground(UITheme.TEXT_SEC);

        header.add(title, BorderLayout.WEST);
        header.add(dateLabel, BorderLayout.EAST);
        return header;
    }

    // ── Sidebar ───────────────────────────────────────────────────────────────

    private JPanel buildSidebar() {
        Color sidebarBg = new Color(10, 13, 22);

        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(sidebarBg);
        sidebar.setPreferredSize(new Dimension(232, 0));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, UITheme.BORDER));

        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setBackground(sidebarBg);
        top.add(buildBrand(sidebarBg));
        top.add(buildDivider());
        top.add(buildProfileCard(user, UITheme.ACCENT3, sidebarBg));
        top.add(buildDivider());

        JButton billingBtn = sidebarButton("💰  Billing & Invoicing", UITheme.ACCENT);
        JButton reportsBtn = sidebarButton("📊  Sales Reports",       UITheme.ACCENT3);
        billingBtn.addActionListener(e -> new BillingView(ctrl));
        reportsBtn.addActionListener(e -> new SalesReportView(ctrl));

        JPanel nav = new JPanel(new GridLayout(0, 1, 0, 4));
        nav.setBackground(sidebarBg);
        nav.setBorder(new EmptyBorder(10, 10, 10, 10));
        nav.add(billingBtn);
        nav.add(reportsBtn);
        top.add(nav);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(sidebarBg);
        bottom.setBorder(new EmptyBorder(8, 10, 18, 10));
        JButton logout = sidebarButton("⎋  Logout", UITheme.DANGER);
        logout.addActionListener(e -> { dispose(); new LoginView(); });
        bottom.add(logout, BorderLayout.CENTER);

        sidebar.add(top, BorderLayout.NORTH);
        sidebar.add(bottom, BorderLayout.SOUTH);
        return sidebar;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private JPanel buildBrand(Color bg) {
        JPanel brand = new JPanel(new BorderLayout(10, 0));
        brand.setBackground(bg);
        brand.setBorder(new EmptyBorder(20, 16, 18, 16));
        JLabel ico = new JLabel("🍽");
        ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        JLabel name = new JLabel("Restaurant MS");
        name.setFont(new Font("Segoe UI", Font.BOLD, 14));
        name.setForeground(UITheme.ACCENT);
        JLabel sub = new JLabel("Management System");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        sub.setForeground(UITheme.TEXT_SEC);
        JPanel txt = new JPanel(new GridLayout(2, 1, 0, 2));
        txt.setOpaque(false);
        txt.add(name); txt.add(sub);
        brand.add(ico, BorderLayout.WEST);
        brand.add(txt, BorderLayout.CENTER);
        return brand;
    }

    private JPanel buildProfileCard(User u, Color accent, Color bg) {
        JPanel p = new JPanel(new BorderLayout(12, 0));
        p.setBackground(bg);
        p.setBorder(new EmptyBorder(14, 16, 14, 16));

        String initials = getInitials(u.getName());
        JLabel avatar = new JLabel(initials, SwingConstants.CENTER) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(accent.darker().darker());
                g2.fillOval(1, 1, getWidth() - 2, getHeight() - 2);
                g2.setColor(accent);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawOval(1, 1, getWidth() - 2, getHeight() - 2);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        avatar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        avatar.setForeground(Color.WHITE);
        avatar.setPreferredSize(new Dimension(44, 44));
        avatar.setOpaque(false);

        JLabel nameLabel = new JLabel(u.getName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        nameLabel.setForeground(UITheme.TEXT_PRI);
        JLabel roleBadge = new JLabel("  " + u.getRole() + "  ");
        roleBadge.setFont(new Font("Segoe UI", Font.BOLD, 9));
        roleBadge.setForeground(accent);
        roleBadge.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accent, 1),
            new EmptyBorder(2, 4, 2, 4)
        ));
        JPanel info = new JPanel(new GridLayout(2, 1, 0, 4));
        info.setOpaque(false);
        info.add(nameLabel); info.add(roleBadge);
        p.add(avatar, BorderLayout.WEST);
        p.add(info, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildStatCard(String icon, String value, String label, Color accent) {
        JPanel p = new JPanel(new BorderLayout(12, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UITheme.BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(UITheme.BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.setColor(accent);
                g2.fillRect(0, 0, getWidth(), 3);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(18, 20, 18, 20));
        JLabel ico = new JLabel(icon);
        ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.BOLD, 34));
        val.setForeground(accent);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(UITheme.TEXT_SEC);
        JPanel txt = new JPanel(new BorderLayout(0, 4));
        txt.setOpaque(false);
        txt.add(val, BorderLayout.CENTER);
        txt.add(lbl, BorderLayout.SOUTH);
        p.add(ico, BorderLayout.WEST);
        p.add(txt, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildFeatureCard(String icon, String title, String desc, Color accent, Runnable action) {
        JPanel p = new JPanel(new BorderLayout(0, 14)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UITheme.BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.setColor(UITheme.BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                g2.setColor(accent);
                g2.fillRoundRect(0, 0, 5, getHeight(), 4, 4);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(22, 26, 22, 26));
        JPanel topRow = new JPanel(new BorderLayout(12, 0));
        topRow.setOpaque(false);
        JLabel ico = new JLabel(icon);
        ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        JLabel tl = new JLabel(title);
        tl.setFont(new Font("Segoe UI", Font.BOLD, 17));
        tl.setForeground(accent);
        topRow.add(ico, BorderLayout.WEST);
        topRow.add(tl, BorderLayout.CENTER);
        JLabel dl = new JLabel("<html>" + desc.replace("\n", "<br>") + "</html>");
        dl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dl.setForeground(UITheme.TEXT_SEC);
        JButton btn = buildOpenButton(accent);
        btn.addActionListener(e -> action.run());
        p.add(topRow, BorderLayout.NORTH);
        p.add(dl, BorderLayout.CENTER);
        p.add(btn, BorderLayout.SOUTH);
        return p;
    }

    private JButton buildOpenButton(Color accent) {
        JButton btn = new JButton("Open  →") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getModel().isRollover() ? accent : new Color(
                    Math.min(accent.getRed() / 3 + 15, 255),
                    Math.min(accent.getGreen() / 3 + 15, 255),
                    Math.min(accent.getBlue() / 3 + 15, 255));
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 22, 10, 22));
        return btn;
    }

    private JPanel buildStatusBar(String rightText) {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(new Color(10, 13, 22));
        bar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, UITheme.BORDER),
            new EmptyBorder(6, 22, 6, 22)
        ));
        JLabel left = new JLabel("● Connected  |  Restaurant Management System v1.0");
        left.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        left.setForeground(UITheme.ACCENT2);
        JLabel right = new JLabel(rightText);
        right.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        right.setForeground(UITheme.TEXT_SEC);
        bar.add(left, BorderLayout.WEST);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    private JPanel buildDivider() {
        JPanel d = new JPanel(new BorderLayout());
        d.setOpaque(false);
        d.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        JSeparator sep = new JSeparator();
        sep.setForeground(UITheme.BORDER);
        sep.setBackground(UITheme.BORDER);
        d.add(sep, BorderLayout.CENTER);
        return d;
    }

    private JButton sidebarButton(String text, Color accent) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        b.setForeground(UITheme.TEXT_PRI);
        b.setBackground(new Color(18, 23, 38));
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(10, 14, 10, 14));
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                b.setBackground(new Color(28, 36, 58));
                b.setForeground(accent);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                b.setBackground(new Color(18, 23, 38));
                b.setForeground(UITheme.TEXT_PRI);
            }
        });
        return b;
    }

    private String getInitials(String name) {
        if (name == null || name.isBlank()) return "?";
        String[] parts = name.trim().split("\\s+");
        if (parts.length == 1) return parts[0].substring(0, 1).toUpperCase();
        return (parts[0].substring(0, 1) + parts[parts.length - 1].substring(0, 1)).toUpperCase();
    }
}
