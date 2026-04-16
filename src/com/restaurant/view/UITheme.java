package com.restaurant.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.geom.*;

/**
 * GLOBAL UI THEME - Shared styling constants and helpers for all views.
 * Dark restaurant theme: deep navy + gold accents.
 */
public class UITheme {

    // ── Palette ──────────────────────────────────────────────────
    public static final Color BG_DEEP    = new Color(13,  17,  28);   // darkest bg
    public static final Color BG_CARD    = new Color(22,  28,  45);   // card/panel bg
    public static final Color BG_FIELD   = new Color(30,  38,  58);   // input fields
    public static final Color BG_ROW_ALT = new Color(18,  24,  38);   // table alt row
    public static final Color ACCENT     = new Color(212, 175,  55);   // gold
    public static final Color ACCENT2    = new Color(108, 200, 138);   // green
    public static final Color ACCENT3    = new Color( 99, 155, 255);   // blue
    public static final Color ACCENT4    = new Color(240, 120,  60);   // orange
    public static final Color DANGER     = new Color(220,  70,  70);   // red
    public static final Color TEXT_PRI   = new Color(230, 230, 240);
    public static final Color TEXT_SEC   = new Color(140, 145, 165);
    public static final Color BORDER     = new Color( 45,  55,  80);

    // ── Fonts ─────────────────────────────────────────────────────
    public static final Font FONT_TITLE  = new Font("Segoe UI", Font.BOLD,  22);
    public static final Font FONT_HEAD   = new Font("Segoe UI", Font.BOLD,  15);
    public static final Font FONT_LABEL  = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL  = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_MONO   = new Font("Courier New", Font.PLAIN, 12);
    public static final Font FONT_BTN    = new Font("Segoe UI", Font.BOLD,  12);

    // ── Helpers ───────────────────────────────────────────────────

    public static JButton primaryButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(FONT_BTN);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(8, 18, 8, 18));
        return b;
    }

    public static JTextField styledField() {
        JTextField f = new JTextField();
        f.setBackground(BG_FIELD);
        f.setForeground(TEXT_PRI);
        f.setCaretColor(ACCENT);
        f.setFont(FONT_LABEL);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            new EmptyBorder(7, 10, 7, 10)
        ));
        return f;
    }

    public static JPasswordField styledPassword() {
        JPasswordField f = new JPasswordField();
        f.setBackground(BG_FIELD);
        f.setForeground(TEXT_PRI);
        f.setCaretColor(ACCENT);
        f.setFont(FONT_LABEL);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            new EmptyBorder(7, 10, 7, 10)
        ));
        return f;
    }

    public static JComboBox<String> styledCombo(String[] items) {
        JComboBox<String> c = new JComboBox<>(items);
        c.setBackground(BG_FIELD);
        c.setForeground(TEXT_PRI);
        c.setFont(FONT_LABEL);
        c.setBorder(BorderFactory.createLineBorder(BORDER, 1));
        ((JComponent)c.getRenderer()).setOpaque(true);
        return c;
    }

    public static JLabel sectionLabel(String text, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_HEAD);
        l.setForeground(color);
        return l;
    }

    public static JPanel cardPanel() {
        JPanel p = new JPanel();
        p.setBackground(BG_CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            new EmptyBorder(16, 16, 16, 16)
        ));
        return p;
    }

    public static void styleTable(JTable t, Color headerAccent) {
        t.setBackground(BG_CARD);
        t.setForeground(TEXT_PRI);
        t.setFont(FONT_LABEL);
        t.setRowHeight(30);
        t.setGridColor(BORDER);
        t.setShowVerticalLines(false);
        t.setIntercellSpacing(new Dimension(0, 1));
        t.setSelectionBackground(new Color(60, 80, 130));
        t.setSelectionForeground(Color.WHITE);
        t.getTableHeader().setBackground(BG_DEEP);
        t.getTableHeader().setForeground(headerAccent);
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        t.getTableHeader().setBorder(BorderFactory.createMatteBorder(0,0,2,0, headerAccent));
        // Alternating row renderer
        t.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                if (isSelected) {
                    setBackground(new Color(60, 80, 130));
                    setForeground(Color.WHITE);
                } else {
                    setBackground(row % 2 == 0 ? BG_CARD : BG_ROW_ALT);
                    setForeground(TEXT_PRI);
                }
                setBorder(new EmptyBorder(0, 10, 0, 10));
                setFont(FONT_LABEL);
                return this;
            }
        });
    }

    public static JScrollPane styledScroll(JTable t) {
        JScrollPane sp = new JScrollPane(t);
        sp.setBackground(BG_CARD);
        sp.getViewport().setBackground(BG_CARD);
        sp.setBorder(BorderFactory.createLineBorder(BORDER, 1));
        return sp;
    }

    /** Stat card: big number + label below */
    public static JPanel statCard(String value, String label, Color accent) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setBackground(BG_CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accent, 1),
            new EmptyBorder(18, 22, 18, 22)
        ));
        JLabel valLbl = new JLabel(value, SwingConstants.CENTER);
        valLbl.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valLbl.setForeground(accent);
        JLabel nameLbl = new JLabel(label, SwingConstants.CENTER);
        nameLbl.setFont(FONT_SMALL);
        nameLbl.setForeground(TEXT_SEC);
        p.add(valLbl, BorderLayout.CENTER);
        p.add(nameLbl, BorderLayout.SOUTH);
        return p;
    }

    /** Header bar with title on left, subtitle on right */
    public static JPanel headerBar(String title, String subtitle, Color accent) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG_DEEP);
        p.setBorder(new EmptyBorder(14, 20, 14, 20));
        JLabel t = new JLabel(title);
        t.setFont(FONT_TITLE);
        t.setForeground(accent);
        JLabel s = new JLabel(subtitle);
        s.setFont(FONT_SMALL);
        s.setForeground(TEXT_SEC);
        p.add(t, BorderLayout.WEST);
        p.add(s, BorderLayout.EAST);
        return p;
    }

    /** Full-window base panel */
    public static JPanel basePanel() {
        JPanel p = new JPanel(new BorderLayout(0, 0));
        p.setBackground(BG_DEEP);
        return p;
    }

    public static void applyGlobalDefaults() {
        UIManager.put("Panel.background",         BG_DEEP);
        UIManager.put("OptionPane.background",     BG_CARD);
        UIManager.put("OptionPane.messageForeground", TEXT_PRI);
        UIManager.put("OptionPane.buttonFont",     FONT_BTN);
        UIManager.put("Button.background",         BG_FIELD);
        UIManager.put("Button.foreground",         TEXT_PRI);
        UIManager.put("Button.font",               FONT_BTN);
        UIManager.put("Label.foreground",          TEXT_PRI);
        UIManager.put("Label.font",                FONT_LABEL);
        UIManager.put("TextField.background",      BG_FIELD);
        UIManager.put("TextField.foreground",      TEXT_PRI);
        UIManager.put("TextField.caretForeground", ACCENT);
        UIManager.put("ComboBox.background",       BG_FIELD);
        UIManager.put("ComboBox.foreground",       TEXT_PRI);
        UIManager.put("ScrollBar.thumb",           BORDER);
        UIManager.put("ScrollBar.track",           BG_DEEP);
        UIManager.put("TabbedPane.background",     BG_DEEP);
        UIManager.put("TabbedPane.foreground",     TEXT_PRI);
        UIManager.put("TabbedPane.selected",       BG_CARD);
        UIManager.put("TabbedPane.focus",          BG_CARD);
    }
}
