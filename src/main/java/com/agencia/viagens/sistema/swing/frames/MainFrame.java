package com.agencia.viagens.sistema.swing.frames;


// import org.springframework.beans.factory.annotation.Autowired;
// someService = ApplicationMain.getSpringContext().getBean(SomeService.class);

import com.agencia.viagens.sistema.swing.ApplicationMain;

import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        initializeUI();
    }

    private void initializeUI() {

        setTitle("Agência de Viagens");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());


        JLabel title = new JLabel("Sistema Agência de Viagens", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JPanel buttonPanel = new JPanel(new GridLayout(2,2,20,20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton clientesBtn = new JButton("Clientes");
        clientesBtn.setFont(new Font("Arial", Font.BOLD, 24));

        JButton pacotesBtn = new JButton("Pacotes");
        pacotesBtn.setFont(new Font("Arial", Font.BOLD, 24));

        JButton servicosBtn = new JButton("Serviços");
        servicosBtn.setFont(new Font("Arial", Font.BOLD, 24));

        JButton pedidosBtn = new JButton("Pedidos");
        pedidosBtn.setFont(new Font("Arial", Font.BOLD, 24));

        buttonPanel.add(clientesBtn);
        buttonPanel.add(pacotesBtn);
        buttonPanel.add(servicosBtn);
        buttonPanel.add(pedidosBtn);

        add(title, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        createMenuBar();
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }
}