package com.agencia.viagens.sistema.swing.frames;

import com.agencia.viagens.sistema.entity.Cliente;
import com.agencia.viagens.sistema.entity.ClienteTipo;
import com.agencia.viagens.sistema.service.ClienteService;
import com.agencia.viagens.sistema.swing.ApplicationMain;
import com.agencia.viagens.sistema.swing.ButtonColumn;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import jakarta.validation.ValidationException;
import org.apache.commons.lang3.ArrayUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Optional;

// TODO: deletar cliente do bd
// TODO: cadastar cliente no db
// TODO: ver pacotes do cliente

public class ClienteFrame extends JFrame {
    private final JFrame parent;
    private DefaultTableModel tabelaClientesModel;
    private JTable tabelaClientes;
    private ClienteService clienteService;
    private JPanel mainPanel;

    public ClienteFrame(JFrame parent) {
        this.parent = parent;
        this.clienteService = ApplicationMain.getSpringContext().getBean(ClienteService.class);
        this.mainPanel = new JPanel();
        this.mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);

        setTitle("Clientes Cadastrados");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.NORMAL);
        setSize(1280, 720);

        setLayout(new BorderLayout());

        createMenuBar();
        createHeader();
        createClienteSearch();
        createClienteList();

        this.add(mainPanel, BorderLayout.CENTER);
    }

    private void createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout(5, 5));

        JLabel clienteLabel = new JLabel("Clientes Cadastrados");
        clienteLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JButton addButton = new JButton("Novo Cliente");
        addButton.addActionListener(e -> showClienteForm());

        headerPanel.setMaximumSize(new Dimension(
                Integer.MAX_VALUE,
                10
        ));

        headerPanel.add(clienteLabel, BorderLayout.WEST);
        headerPanel.add(addButton, BorderLayout.EAST);

        this.mainPanel.add(headerPanel);
    }

    private void showClienteForm() {
        JDialog dialogo = new JDialog(this, "Cadastrar Novo Cliente", true);
        dialogo.setSize(400, 400);
        dialogo.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel nomeLabel = new JLabel("Nome Completo:");
        JTextField nomeField = new JTextField();

        JLabel telLabel = new JLabel("Telefone:");
        JTextField telField = new JTextField();

        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        telField.putClientProperty(
                "JTextField.placeholderText", "ex: " + phoneUtil.format(phoneUtil.getExampleNumber("BR"), PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL));

        JLabel emailLabel = new JLabel("E-mail:");
        JTextField emailField = new JTextField();

        JLabel tipoLabel = new JLabel("Tipo de Cliente:");
        JComboBox<String> tipoCombo = new JComboBox<>(new String[]{"Nacional (CPF)", "Estrangeiro (Passaporte)"});

        JLabel docLabel = new JLabel("Documento:");
        JTextField docField = new JTextField();

        JButton saveButton = new JButton("Salvar");
        JButton cancelButton = new JButton("Cancelar");

        saveButton.addActionListener(e -> {
            Cliente cliente = new Cliente();
            cliente.setNome(nomeField.getText());
            cliente.setTelefone(telField.getText());
            cliente.setEmail(emailField.getText());

            ClienteTipo tipo = null;

            if (tipoLabel.getText().equals("Nacional (CPF)")) {
                tipo = ClienteTipo.NACIONAL;
                cliente.setCpf(docField.getText());
            } else {
                tipo = ClienteTipo.ESTRANGEIRO;
                cliente.setPassaporte(docField.getText());
            }

            cliente.setTipo(tipo);

            try {
                this.clienteService.salvarCliente(cliente);
            } catch (ValidationException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
                return;
            }

            atualizarTabelaClientes(null);

            dialogo.dispose();
        });

        cancelButton.addActionListener(e -> {
            dialogo.dispose();
        });

        panel.add(nomeLabel);
        panel.add(nomeField);
        panel.add(telLabel);
        panel.add(telField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(tipoLabel);
        panel.add(tipoCombo);
        panel.add(docLabel);
        panel.add(docField);
        panel.add(saveButton);
        panel.add(cancelButton);

        dialogo.add(panel);
        dialogo.setVisible(true);
    }

    private void createClienteSearch() {
        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        searchPanel.setMaximumSize(new Dimension(
                Integer.MAX_VALUE,
                20
        ));
        JTextField searchField = new JTextField();

        searchField.putClientProperty("JTextField.placeholderText", "Buscar cliente...");
        searchField.addActionListener(e -> {
            String query = searchField.getText();
            atualizarTabelaClientes(clienteService.buscar(query));
        });

        JButton buscarBtn = new JButton("Buscar");
        buscarBtn.addActionListener(e -> {
            String query = searchField.getText();
            atualizarTabelaClientes(clienteService.buscar(query));
        });

        JButton limparBuscaBtn = new JButton("Limpar Busca");
        limparBuscaBtn.addActionListener(e -> {
            atualizarTabelaClientes(null);
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        buttonPanel.add(buscarBtn);
        buttonPanel.add(limparBuscaBtn);

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(buttonPanel, BorderLayout.EAST);

        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(searchPanel);
    }

    private void createClienteList() {
        String[] colunas = {"ID", "Nome", "Telefone", "E-mail", "Documento", "Tipo", "Ver Pacotes", "Deletar"};

        int nomeIdx = ArrayUtils.indexOf(colunas, "Nome");
        int documentoIdx = ArrayUtils.indexOf(colunas, "Documento");
        int pacotesIdx = ArrayUtils.indexOf(colunas, "Ver Pacotes");
        int deletarIdx = ArrayUtils.indexOf(colunas, "Deletar");

        this.tabelaClientesModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == pacotesIdx || column == deletarIdx;
            }

            @Override
            public void moveRow(int start, int end, int to) {
            }

        };

        this.tabelaClientes = new JTable(this.tabelaClientesModel);
        this.tabelaClientes.getTableHeader().setReorderingAllowed(false);
        this.tabelaClientes.setRowHeight(40);

        Action delete = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {

                JTable table = (JTable) e.getSource();
                int modelRow = Integer.valueOf(e.getActionCommand());
                Object nome = table.getModel().getValueAt(modelRow, nomeIdx);
                Object documento = table.getModel().getValueAt(modelRow, documentoIdx);
                Window window = SwingUtilities.windowForComponent(table);

                int result = JOptionPane.showConfirmDialog(
                        window,
                        "Tem certeza que quer deletar o cliente " + nome + "(" + documento + ")?",
                        "Confirmação",
                        JOptionPane.YES_NO_OPTION);

                if (result == JOptionPane.YES_OPTION) {
                    ((DefaultTableModel) table.getModel()).removeRow(modelRow);
                    Long id = (Long) table.getModel().getValueAt(modelRow, 0);

                    Optional<Cliente> cliente = clienteService.buscarPorId(id);

                    assert cliente.isPresent();

                    clienteService.removerCliente(cliente.get());
                }
            }
        };

        Action verPacotes = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(tabelaClientes, "TOOD");

            }
        };

        new ButtonColumn(tabelaClientes, delete, deletarIdx);
        new ButtonColumn(tabelaClientes, verPacotes, pacotesIdx);

        JScrollPane scrollPane = new JScrollPane(tabelaClientes);
        this.mainPanel.add(scrollPane);

        this.tabelaClientesModel.setRowCount(0);

        atualizarTabelaClientes(null);

    }

    private void atualizarTabelaClientes(List<Cliente> clientes) {
        this.tabelaClientesModel.setRowCount(0);

        if (clientes == null)
            clientes = clienteService.buscarTodos();

        for (Cliente cliente : clientes) {
            tabelaClientesModel.addRow(new Object[]{
                    cliente.getId(),
                    cliente.getNome(),
                    cliente.getTelefone(),
                    cliente.getEmail(),
                    cliente.getTipo() == ClienteTipo.NACIONAL
                            ? cliente.getCpf()
                            : cliente.getPassaporte(),
                    cliente.getTipo() == ClienteTipo.NACIONAL
                            ? "CPF"
                            : "Passaporte",
                    "Ver Pacotes",
                    "Deletar"
            });

        }
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");

        JMenuItem voltarItem = new JMenuItem("Voltar");
        JMenuItem exitItem = new JMenuItem("Exit");

        voltarItem.addActionListener(e -> {
            this.setVisible(false);
            this.parent.setVisible(true);
        });
        fileMenu.add(voltarItem);

        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }
}
