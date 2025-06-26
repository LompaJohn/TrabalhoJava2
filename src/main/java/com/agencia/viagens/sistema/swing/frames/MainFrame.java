package com.agencia.viagens.sistema.swing.frames;

import javax.swing.*;
import java.awt.*;

// TODO: validar o tamanho dos fields

public class MainFrame extends JFrame {
    private ClienteFrame clienteFrame;
    private PacotesFrame pacotesFrame;
    private ServicosFrame servicosFrame;
    private PedidosFrame pedidosFrame;

    public MainFrame() {
        this.clienteFrame = new ClienteFrame(this);
        this.pacotesFrame = new PacotesFrame(this);
        this.servicosFrame = new ServicosFrame(this);
        this.pedidosFrame = new PedidosFrame(this);
        initializeUI();
    }

    private void initializeUI() {

        setTitle("Agência de Viagens");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // setSize(800, 600);
        setSize(1280, 720);
        setExtendedState(JFrame.NORMAL);

        setLayout(new BorderLayout());

        JLabel title = new JLabel("Sistema Agência de Viagens", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton clientesBtn = new JButton("Clientes");
        clientesBtn.setFont(new Font("Arial", Font.BOLD, 24));
        clientesBtn.addActionListener(e -> {
            this.setVisible(false);
            clienteFrame.setVisible(true);
        });

        JButton pacotesBtn = new JButton("Pacotes");
        pacotesBtn.setFont(new Font("Arial", Font.BOLD, 24));
        pacotesBtn.addActionListener(e -> {
            this.setVisible(false);
            pacotesFrame.setVisible(true);
        });

        JButton servicosBtn = new JButton("Serviços");
        servicosBtn.setFont(new Font("Arial", Font.BOLD, 24));
        servicosBtn.addActionListener(e -> {
            this.setVisible(false);
            servicosFrame.setVisible(true);
        });

        JButton pedidosBtn = new JButton("Pedidos");
        pedidosBtn.setFont(new Font("Arial", Font.BOLD, 24));
        pedidosBtn.addActionListener(e -> {
            this.setVisible(false);
            pedidosFrame.setVisible(true);
        });

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
