package com.morningstar;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Random;

public class App extends JFrame {
    // 数据成员变量
    private final double random;
    private final ImageIcon[] images;
    private int[] state;
    private int blankIndex;

    // 组件成员变量
    private ImagePanel imagePanel;
    private JButton upButton;
    private JButton downButton;
    private JButton leftButton;
    private JButton rightButton;
    private JButton helpButton;

    public App(double random) {
        this.random = random;

        this.images = loadImages();

        // 初始化数据
        initState();
        randomizedState();

        // 绘制界面
        this.initFrame();
        this.addComponents();
        this.setVisible(true);
    }

    private ImageIcon[] loadImages() {
        ImageIcon[] images = new ImageIcon[17];
        for (int i = 0; i < images.length; i++) {
            images[i] = new ImageIcon(String.format("AnimePuzzle/images/%d.png", i));
        }
        return images;
    }

    private int locateBlank() {
        for (int i = 0; i < 16; i++) {
            if (state[i] == 0) {
                return i;
            }
        }
        return -1;
    }

    private void initState() {
        state = new int[16];
        // 生成原始图片顺序
        for (int i = 0; i < 16; i++) {
            state[i] = i + 1;
        }
        Random r = new Random();
        // 替换某张图片为空图
        int removedIndex = r.nextInt(16) + 1;
        state[removedIndex - 1] = 0;
    }

    private void randomizedState() {
        Random r = new Random();
        // 打乱图片顺序
        for (int i = 0; i < (int) (random * 16); i++) {
            int j = r.nextInt(16);
            int temp = state[i];
            state[i] = state[j];
            state[j] = temp;
        }

        this.blankIndex = locateBlank();
    }

    private boolean isSuccess() {
        for (int i = 0; i < 16; i++) {
            if (state[i] != 0 && state[i] != i + 1) {
                return false;
            }
        }
        return true;
    }

    private int findMissingImageIndex() {
        int missingImageIndex = 1;
        HashSet<Integer> set = new HashSet<>();
        for (int i = 0; i < 16; i++) {
            set.add(state[i]);
        }
        for (int imageIndex = 1; imageIndex <= 16; imageIndex++) {
            if (!set.contains(imageIndex)) {
                missingImageIndex = imageIndex;
                break;
            }
        }
        return missingImageIndex;
    }

    private void initFrame() {
        this.setSize(960, 530);
        this.setTitle("动漫美女拼图");
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(null);
    }

    private void addComponents() {
        // 添加标题
        JLabel titleLabel = new JLabel(new ImageIcon("AnimePuzzle/images/title.png"));
        titleLabel.setBounds(354, 27, 232, 57);
        this.add(titleLabel);

        // 添加图片面板
        imagePanel = new ImagePanel(images, state);
        imagePanel.setBounds(150, 114, 360, 360);
        this.add(imagePanel);

        // 添加参照图
        JLabel referenceLabel = new JLabel(new ImageIcon("AnimePuzzle/images/reference.png"));
        referenceLabel.setBounds(574, 114, 122, 121);
        this.add(referenceLabel);

        // 添加操作按钮
        upButton = new JButton(new ImageIcon("AnimePuzzle/images/up.png"));
        upButton.setBounds(732, 265, 57, 57);
        upButton.addActionListener(actionEvent -> {
            swapImages(blankIndex % 4, blankIndex / 4 - 1);
            if (isSuccess()) {
                disableActionButtons();
                showCongratulationDialog();
            }
        });
        this.add(upButton);
        downButton = new JButton(new ImageIcon("AnimePuzzle/images/down.png"));
        downButton.setBounds(732, 347, 57, 57);
        downButton.addActionListener(actionEvent -> {
            swapImages(blankIndex % 4, blankIndex / 4 + 1);
            if (isSuccess()) {
                disableActionButtons();
                showCongratulationDialog();
            }
        });
        this.add(downButton);
        leftButton = new JButton(new ImageIcon("AnimePuzzle/images/left.png"));
        leftButton.setBounds(650, 347, 57, 57);
        leftButton.addActionListener(actionEvent -> {
            swapImages(blankIndex % 4 - 1, blankIndex / 4);
            if (isSuccess()) {
                disableActionButtons();
                showCongratulationDialog();
            }

        });
        this.add(leftButton);
        rightButton = new JButton(new ImageIcon("AnimePuzzle/images/right.png"));
        rightButton.setBounds(813, 347, 57, 57);
        rightButton.addActionListener(actionEvent -> {
            swapImages(blankIndex % 4 + 1, blankIndex / 4);
            if (isSuccess()) {
                disableActionButtons();
                showCongratulationDialog();
            }
        });
        this.add(rightButton);
        helpButton = new JButton(new ImageIcon("AnimePuzzle/images/help.png"));
        helpButton.setBounds(626, 444, 108, 45);
        helpButton.addActionListener(actionEvent -> help());
        this.add(helpButton);
        JButton resetButton = new JButton(new ImageIcon("AnimePuzzle/images/reset.png"));
        resetButton.setBounds(786, 444, 108, 45);
        resetButton.addActionListener(actionEvent -> reset());
        this.add(resetButton);

        // 添加背景图
        ImageIcon backgroundImage = new ImageIcon("AnimePuzzle/images/background.png");
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setBounds(0, 0, 960, 530);
        this.add(backgroundLabel);
    }

    private void swapImages(int col, int row) {
        if (col == -1 || col == 4 || row == -1 || row == 4) {
            System.out.println("无法向该方向继续移动");
            return;
        }
        int exchangeIndex = col + row * 4;

        // 更新状态
        state[blankIndex] = state[exchangeIndex];
        state[exchangeIndex] = 0;

        // 更新图片
        this.imagePanel.updateImage(exchangeIndex);
        this.imagePanel.updateImage(blankIndex);

        // 更新空图片索引
        blankIndex = exchangeIndex;
    }

    private void disableActionButtons() {
        upButton.setEnabled(false);
        downButton.setEnabled(false);
        leftButton.setEnabled(false);
        rightButton.setEnabled(false);
        helpButton.setEnabled(false);
    }

    private void enableActionButtons() {
        upButton.setEnabled(true);
        downButton.setEnabled(true);
        leftButton.setEnabled(true);
        rightButton.setEnabled(true);
        helpButton.setEnabled(true);
    }

    private void showCongratulationDialog() {
        JOptionPane.showMessageDialog(this, "Good Job!!");
    }

    private void reset() {
        enableActionButtons();
        randomizedState();
        this.imagePanel.updateAllImages();
    }

    private void help() {
        // 复原状态
        for (int i = 0; i < 16; i++) {
            state[i] = i + 1;
        }
        state[findMissingImageIndex() - 1] = 0;
        this.imagePanel.updateAllImages();

        // 关闭操作按钮
        disableActionButtons();
    }


}

