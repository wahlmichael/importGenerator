package com.importgenerator;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JButton;

public class StyledButton extends JButton {
  public StyledButton(String text, Color baseColor, Color hoverColor, Color textColor) {
    super(text);
    setBackground(baseColor);
    setForeground(textColor);
    setFocusPainted(false);
    setBorderPainted(false);
    setOpaque(true);
    setFont(new Font("Verdana", Font.BOLD, 14));
    setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    setContentAreaFilled(false);
    setUI(new javax.swing.plaf.basic.BasicButtonUI());
    addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        setBackground(hoverColor);
      }

      public void mouseExited(java.awt.event.MouseEvent evt) {
        setBackground(baseColor);
      }
    });
  }

  @Override
  protected void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D) g.create();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setColor(getBackground());
    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
    super.paintComponent(g);
    g2.dispose();
  }
}
