package com.agencia.viagens.sistema.swing;

import com.agencia.viagens.sistema.SistemaApplication;
import com.agencia.viagens.sistema.swing.frames.MainFrame;
import com.formdev.flatlaf.FlatDarkLaf;
import lombok.Getter;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;

public class ApplicationMain {
    @Getter
    private static ConfigurableApplicationContext springContext;

    public static void main(String[] args) {
        // Falar pro awt que eh um programa com UI
        System.setProperty("java.awt.headless", "false");

        springContext = SpringApplication.run(SistemaApplication.class, args);

        SwingUtilities.invokeLater(() -> {
            try {

                try {
                    UIManager.setLookAndFeel(new FlatDarkLaf());
                } catch (Exception ex) {
                    System.err.println("Failed to set FlatDarkLaf");
                    ex.printStackTrace();
                }

                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (springContext != null) {
                springContext.close();
            }
        }));
    }
}
