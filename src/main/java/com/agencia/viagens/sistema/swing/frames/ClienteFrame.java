package com.agencia.viagens.sistema.swing.frames;

import com.agencia.viagens.sistema.entity.Cliente;
import com.agencia.viagens.sistema.entity.ClienteTipo;
import com.agencia.viagens.sistema.entity.Pacote;
import com.agencia.viagens.sistema.entity.Pedido;
import com.agencia.viagens.sistema.service.ClienteService;
import com.agencia.viagens.sistema.service.PedidoService;
import com.agencia.viagens.sistema.swing.ApplicationMain;
import com.agencia.viagens.sistema.swing.ButtonColumn;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import jakarta.validation.ValidationException;
import org.apache.commons.lang3.ArrayUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ClienteFrame extends JFrame {
    private final JFrame parent;
    private DefaultTableModel tabelaClientesModel;
    private JTable tabelaClientes;
    private ClienteService clienteService;
    private PedidoService pedidoService;
    private JPanel mainPanel;

    public ClienteFrame(JFrame parent) {
        this.parent = parent;

        this.clienteService = ApplicationMain.getSpringContext().getBean(ClienteService.class);
        this.pedidoService = ApplicationMain.getSpringContext().getBean(PedidoService.class);

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
                10));

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
                "JTextField.placeholderText", "ex: " + phoneUtil.format(phoneUtil.getExampleNumber("BR"),
                        PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL));

        JLabel emailLabel = new JLabel("E-mail:");
        JTextField emailField = new JTextField();

        JLabel tipoLabel = new JLabel("Tipo de Cliente:");

        String comboCPF = "Nacional (CPF)";
        String comboPassaporte = "Estrangeiro (Passaporte)";
        JComboBox<String> tipoCombo = new JComboBox<>(new String[] { comboCPF, comboPassaporte });

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

            if (tipoCombo.getSelectedItem().equals(comboCPF)) {
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
                20));
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
            searchField.setText("");
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
        String[] colunas = { "ID", "Nome", "Telefone", "E-mail", "Documento", "Tipo", "Ver Pacotes", "Deletar" };

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
                        "Tem certeza que quer deletar o cliente \"" + nome + "\" (" + documento + ")?",
                        "Confirmação",
                        JOptionPane.YES_NO_OPTION);

                if (result == JOptionPane.YES_OPTION) {
                    Long id = (Long) table.getModel().getValueAt(modelRow, 0);

                    try {
                        clienteService.removerPorId(id);
                        ((DefaultTableModel) table.getModel()).removeRow(modelRow);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(window, "Cliente não pode ser deletado!", "ERRO", JOptionPane.ERROR_MESSAGE);
                    }
                }

            }
        };

        Action verPacotes = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {

                JTable table = (JTable) e.getSource();
                int modelRow = Integer.valueOf(e.getActionCommand());
                Long id = (Long) table.getModel().getValueAt(modelRow, 0);

                Optional<Cliente> cliente = clienteService.buscarPorId(id);

                assert cliente.isPresent();

                Set<Pacote> pacotes = pedidoService.buscarPorClienteId(cliente.get().getId()).stream()
                        .map(Pedido::getPacote).collect(Collectors.toSet());

                if (pacotes.isEmpty()) {
                    JOptionPane.showMessageDialog(tabelaClientes, "Nenhum pacote encontrado!");
                    return;
                }

                StringBuilder sb = new StringBuilder();

                for (Pacote pacote : pacotes) {
                    sb.append("\u2022 [");
                    sb.append(pacote.getId());
                    sb.append("] ");
                    sb.append(pacote.getNome());
                    sb.append("\n");
                }

                JTextArea txtAreaPacotes = new JTextArea(sb.toString());
                txtAreaPacotes.setEditable(false);

                JOptionPane.showMessageDialog(tabelaClientes, txtAreaPacotes);

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
            tabelaClientesModel.addRow(new Object[] {
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
