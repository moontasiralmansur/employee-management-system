package com.ems.view.components;

import javax.swing.*;
import java.awt.*;

// Custom Swing button with rounded corners and hover/pressed effects.
// Extends JButton and overrides paintComponent for custom drawing.
public class ModernButton extends JButton {
    private Color backgroundColor = new Color(52, 152, 219);
    private Color hoverColor = new Color(41, 128, 185);
    private Color pressedColor = new Color(31, 97, 141);

    public ModernButton(String text) {
        super(text);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(true);
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 13));
        setPreferredSize(new Dimension(120, 35));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effects
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(hoverColor);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(backgroundColor);
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                setBackground(pressedColor);
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                setBackground(hoverColor);
            }
        });

        setBackground(backgroundColor);
    }

    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
        setBackground(color);
        this.hoverColor = color.darker();
        this.pressedColor = hoverColor.darker();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

        super.paintComponent(g2);
        g2.dispose();
    }
}