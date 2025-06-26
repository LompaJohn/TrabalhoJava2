package com.agencia.viagens.sistema.swing.frames;

import com.agencia.viagens.sistema.entity.Cliente;
import com.agencia.viagens.sistema.entity.ClienteTipo;
import com.agencia.viagens.sistema.entity.Pacote;
import com.agencia.viagens.sistema.service.PacoteService;
import com.agencia.viagens.sistema.service.PedidoService;
import com.agencia.viagens.sistema.swing.ApplicationMain;
import com.agencia.viagens.sistema.swing.ButtonColumn;
import jakarta.validation.ValidationException;
import org.apache.commons.lang3.ArrayUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class PacotesFrame extends JFrame {
    private final JFrame parent;
    private DefaultTableModel tabelaPacotesModel;
    private JTable tabelaPacotes;
    private PacoteService pacoteService;
    private PedidoService pedidoService;
    private JPanel mainPanel;

    public PacotesFrame(JFrame parent) {
        this.parent = parent;

        this.pacoteService = ApplicationMain.getSpringContext().getBean(PacoteService.class);
        this.pedidoService = ApplicationMain.getSpringContext().getBean(PedidoService.class);

        this.mainPanel = new JPanel();
        this.mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);

        setTitle("Pacotes Cadastrados");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.NORMAL);
        setSize(1280, 720);

        setLayout(new BorderLayout());

        createMenuBar();
        createHeader();
        createPacoteSearch();
        createPacoteTable();

        this.add(mainPanel, BorderLayout.CENTER);
    }

    private void createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout(5, 5));

        JLabel pacoteLabel = new JLabel("Pacotes Cadastrados");
        pacoteLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JButton addButton = new JButton("Novo Pacote");
        addButton.addActionListener(e -> showPacoteForm());

        headerPanel.setMaximumSize(new Dimension(
                Integer.MAX_VALUE,
                10));

        headerPanel.add(pacoteLabel, BorderLayout.WEST);
        headerPanel.add(addButton, BorderLayout.EAST);

        this.mainPanel.add(headerPanel);
    }

    private void showPacoteForm() {
        JDialog dialogo = new JDialog(this, "Cadastrar Novo Pacote", true);
        dialogo.setSize(400, 500);
        dialogo.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel nomeLabel = new JLabel("Nome:");
        JTextField nomeField = new JTextField();

        JLabel descricaoLabel = new JLabel("Descrição:");
        JTextField descricaoField = new JTextField();

        JLabel destinoLabel = new JLabel("Destino:");
        JTextField destinoField = new JTextField();

        JLabel duracaoDiasLabel = new JLabel("Duração em Dias");
        JTextField duracaoDiasField = new JTextField();

        JLabel precoLabel = new JLabel("Preço:");
        JTextField precoField = new JTextField();

        JLabel tipoLabel = new JLabel("Tipo:");
        JTextField tipoField = new JTextField();

        JButton saveButton = new JButton("Salvar");
        JButton cancelButton = new JButton("Cancelar");

        saveButton.addActionListener(e -> {
            Pacote pacote = new Pacote();
            pacote.setNome(nomeField.getText());
            pacote.setDescricao(descricaoField.getText());
            pacote.setDestino(destinoField.getText());

            try {
                pacote.setDuracaoDias(Integer.parseInt(duracaoDiasField.getText()));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Duração invalida!", "ERRO", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                pacote.setPreco(BigDecimal.valueOf(Double.parseDouble(precoField.getText())));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Preço invalido!", "ERRO", JOptionPane.ERROR_MESSAGE);
                return;
            }

            pacote.setTipo(tipoField.getText());
            pacote.setAtivo(true);

            try {
                pacoteService.salvarPacote(pacote);
            } catch (ValidationException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
                return;
            }

            atualizarTabelaPacotes(null);

            dialogo.dispose();
        });

        cancelButton.addActionListener(e -> {
            dialogo.dispose();
        });

        panel.add(nomeLabel);
        panel.add(nomeField);
        panel.add(descricaoLabel);
        panel.add(descricaoField);
        panel.add(destinoLabel);
        panel.add(destinoField);
        panel.add(duracaoDiasLabel);
        panel.add(duracaoDiasField);
        panel.add(tipoLabel);
        panel.add(tipoField);
        panel.add(precoLabel);
        panel.add(precoField);
        panel.add(saveButton);
        panel.add(cancelButton);

        dialogo.add(panel);
        dialogo.setVisible(true);
    }

    private void createPacoteSearch() {
        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        searchPanel.setMaximumSize(new Dimension(
                Integer.MAX_VALUE,
                20));
        JTextField searchField = new JTextField();

        searchField.putClientProperty("JTextField.placeholderText", "Buscar pacote...");
        searchField.addActionListener(e -> {
            String query = searchField.getText();
            atualizarTabelaPacotes(pacoteService.buscar(query));
        });

        JButton buscarBtn = new JButton("Buscar");
        buscarBtn.addActionListener(e -> {
            String query = searchField.getText();
            atualizarTabelaPacotes(pacoteService.buscar(query));
        });

        JButton limparBuscaBtn = new JButton("Limpar Busca");
        limparBuscaBtn.addActionListener(e -> {
            searchField.setText("");
            atualizarTabelaPacotes(null);
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        buttonPanel.add(buscarBtn);
        buttonPanel.add(limparBuscaBtn);

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(buttonPanel, BorderLayout.EAST);

        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(searchPanel);
    }

    private void createPacoteTable() {
        // ção
        String[] colunas = { "ID", "Nome", "Tipo", "Descrição", "Duração Dias", "Preço", "Ver Clientes", "Deletar" };

        int nomeIdx = ArrayUtils.indexOf(colunas, "Nome");
        int pacotesIdx = ArrayUtils.indexOf(colunas, "Ver Clientes");
        int deletarIdx = ArrayUtils.indexOf(colunas, "Deletar");

        this.tabelaPacotesModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == pacotesIdx || column == deletarIdx;
            }

            @Override
            public void moveRow(int start, int end, int to) {
            }

        };

        this.tabelaPacotes = new JTable(this.tabelaPacotesModel);
        this.tabelaPacotes.getTableHeader().setReorderingAllowed(false);
        this.tabelaPacotes.setRowHeight(40);

        Action delete = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {

                JTable table = (JTable) e.getSource();
                int modelRow = Integer.valueOf(e.getActionCommand());
                Object nome = table.getModel().getValueAt(modelRow, nomeIdx);
                Window window = SwingUtilities.windowForComponent(table);

                int result = JOptionPane.showConfirmDialog(
                        window,
                        "Tem certeza que quer deletar o pacote \"" + nome + "\"?",
                        "Confirmação",
                        JOptionPane.YES_NO_OPTION);

                if (result == JOptionPane.YES_OPTION) {
                    Long id = (Long) table.getModel().getValueAt(modelRow, 0);

                    try {
                        pacoteService.removerPorId(id);
                        ((DefaultTableModel) table.getModel()).removeRow(modelRow);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(window, "Pacote não pode ser deletado!", "ERRO", JOptionPane.ERROR_MESSAGE);
                    }
                }

            }
        };

        Action verClientes = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {

                JTable table = (JTable) e.getSource();
                int modelRow = Integer.valueOf(e.getActionCommand());
                Long id = (Long) table.getModel().getValueAt(modelRow, 0);

                Optional<Pacote> pacote = pacoteService.buscarPorId(id);

                assert pacote.isPresent();

                Set<Cliente> clientes = pedidoService.buscarClientesPorPacoteId(pacote.get().getId());

                if (clientes.isEmpty()) {
                    JOptionPane.showMessageDialog(tabelaPacotes, "Nenhum cliente encontrado!");
                    return;
                }

                StringBuilder sb = new StringBuilder();

                for (Cliente cliente : clientes) {
                    sb.append("\u2022 [");
                    sb.append(cliente.getId());
                    sb.append("] ");
                    sb.append(cliente.getNome());
                    sb.append(" (");
                    sb.append(cliente.getTipo() == ClienteTipo.NACIONAL
                            ? cliente.getCpf()
                            : cliente.getPassaporte());
                    sb.append(")\n");
                }

                JTextArea txtAreaClientes = new JTextArea(sb.toString());
                txtAreaClientes.setEditable(false);
                JOptionPane.showMessageDialog(tabelaPacotes, txtAreaClientes);
            }
        };

        new ButtonColumn(tabelaPacotes, delete, deletarIdx);
        new ButtonColumn(tabelaPacotes, verClientes, pacotesIdx);

        JScrollPane scrollPane = new JScrollPane(tabelaPacotes);
        this.mainPanel.add(scrollPane);

        this.tabelaPacotesModel.setRowCount(0);

        atualizarTabelaPacotes(null);

    }

    private void atualizarTabelaPacotes(List<Pacote> pacotes) {
        this.tabelaPacotesModel.setRowCount(0);

        if (pacotes == null)
            pacotes = pacoteService.buscarTodos();

        for (Pacote pacote : pacotes) {
            tabelaPacotesModel.addRow(new Object[] {
                    pacote.getId(),
                    pacote.getNome(),
                    pacote.getTipo(),
                    pacote.getDescricao(),
                    pacote.getDuracaoDias(),
                    pacote.getPreco(),
                    "Ver Clientes",
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
