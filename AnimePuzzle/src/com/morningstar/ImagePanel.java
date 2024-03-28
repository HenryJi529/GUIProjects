package com.morningstar;

import javax.swing.*;

public class ImagePanel extends JPanel {
    private ImageIcon[] images;
    private int[] state;

    public ImagePanel(ImageIcon[] images, int[] state) {
        this.images = images;
        this.state = state;

        this.initPanel();
    }

    private void initPanel() {
        this.setLayout(null);
        for (int i = 0; i < 16; i++) {
            JLabel imageLabel = new JLabel(images[state[i]]);
            int col = i % 4;
            int row = i / 4;
            imageLabel.setBounds(col * 90, row * 90, 90, 90);
            this.add(imageLabel);
        }
    }

    public void updateImage(int index) {
        JLabel imageLabel = (JLabel) this.getComponent(index);
        imageLabel.setIcon(images[state[index]]);
    }

    public void updateAllImages() {
        for (int index = 0; index < 16; index++) {
            updateImage(index);
        }
    }
}
