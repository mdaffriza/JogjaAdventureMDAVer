package main;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Jogja Adventure");

        // Initialize AudioPlayer and play background music
        AudioPlayer backgroundMusic = new AudioPlayer("/res/audio/background_music.wav");
        backgroundMusic.play();

        GameProgressTracker tracker = new GameProgressTracker();

        StartPanel startPanel = new StartPanel(window, backgroundMusic, tracker); // Pass tracker to StartPanel
        window.add(startPanel);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}
