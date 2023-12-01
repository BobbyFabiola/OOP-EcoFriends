package main;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame window = new JFrame();
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setResizable(false);
            window.setTitle("EcoFriends");

            TitleScreen titleScreen = new TitleScreen(window);
            window.add(titleScreen);

            window.pack();

            int dynamicWidth = titleScreen.tileSize * titleScreen.maxScreenCol;
            int dynamicHeight = titleScreen.tileSize * titleScreen.maxScreenRow;

            window.setSize(dynamicWidth, dynamicHeight);
            window.setLocationRelativeTo(null);
            window.setVisible(true);
        });
    }

}
