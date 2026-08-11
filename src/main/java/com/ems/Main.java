package com.ems;

import com.ems.view.LoginFrame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;

// Main entry point of the Employee Management System.
// Starts the Swing GUI by opening the login window on the Event Dispatch Thread.
public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            logger.error("Failed to set look and feel", e);
        }

        // Start the application
        SwingUtilities.invokeLater(() -> {
            logger.info("Starting Employee Management System");
            new LoginFrame().setVisible(true);
        });
    }
}