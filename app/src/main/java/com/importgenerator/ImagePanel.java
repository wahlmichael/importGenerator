package com.importgenerator;

import javax.swing.JComponent;
import java.awt.Image;
import java.awt.Graphics;

public class ImagePanel extends JComponent {
  private Image image;

  public ImagePanel(Image image) {
    this.image = image;
  }

  @Override
  protected void paintComponent(Graphics graphics) {
    super.paintComponent(graphics);
    graphics.drawImage(image, 0, 0, this);
  }
}
