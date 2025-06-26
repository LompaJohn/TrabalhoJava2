package com.agencia.viagens.sistema.swing.frames;

import com.agencia.viagens.sistema.entity.Servico;
import com.agencia.viagens.sistema.service.ServicoService;
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

public class ServicosFrame extends JFrame {
    private final JFrame parent;
    private DefaultTableModel tabelaServicosModel;
    private JTable tabelaServicos;
    private ServicoService servicoService;
    private JPanel mainPanel;

    public ServicosFrame(JFrame parent) {
        this.parent = parent;

        this.servicoService = ApplicationMain.getSpringContext().getBean(ServicoService.class);

        this.mainPanel = new JPanel();
        this.mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);

        setTitle("Serviços Cadastrados");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.NORMAL);
        setSize(1280, 720);

        setLayout(new BorderLayout());

        createMenuBar();
        createHeader();
        createServicosSearch();
        createServicoList();

        this.add(mainPanel, BorderLayout.CENTER);
    }

    private void createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout(5, 5));

        JLabel servicoLabel = new JLabel("Serviços Cadastrados");
        servicoLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JButton addButton = new JButton("Novo Serviço");
        addButton.addActionListener(e -> showServicoForm());

        headerPanel.setMaximumSize(new Dimension(
                Integer.MAX_VALUE,
                10));

        headerPanel.add(servicoLabel, BorderLayout.WEST);
        headerPanel.add(addButton, BorderLayout.EAST);

        this.mainPanel.add(headerPanel);
    }

    private void showServicoForm() {
        JDialog dialogo = new JDialog(this, "Cadastrar Novo Serviço", true);
        dialogo.setSize(400, 400);
        dialogo.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel nomeLabel = new JLabel("Nome:");
        JTextField nomeField = new JTextField();

        JLabel descricaoLabel = new JLabel("Descrição:");
        JTextField descricaoField = new JTextField();

        JLabel precoLabel = new JLabel("Preço:");
        JTextField precoField = new JTextField();

        JButton saveButton = new JButton("Salvar");
        JButton cancelButton = new JButton("Cancelar");

        saveButton.addActionListener(e -> {
            Servico servico = new Servico();
            servico.setNome(nomeField.getText());
            servico.setDescricao(descricaoField.getText());

            try {
                servico.setPreco(BigDecimal.valueOf(Double.parseDouble(precoField.getText())));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Preço invalido!", "ERRO", JOptionPane.ERROR_MESSAGE);
                return;
            }

            servico.setAtivo(true);

            try {
                servicoService.salvarServico(servico);
            } catch (ValidationException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
                return;
            }

            atualizarTabelaServicos(null);

            dialogo.dispose();
        });

        cancelButton.addActionListener(e -> {
            dialogo.dispose();
        });

        panel.add(nomeLabel);
        panel.add(nomeField);
        panel.add(descricaoLabel);
        panel.add(descricaoField);
        panel.add(precoLabel);
        panel.add(precoField);
        panel.add(saveButton);
        panel.add(cancelButton);

        dialogo.add(panel);
        dialogo.setVisible(true);
    }

    private void createServicosSearch() {
        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        searchPanel.setMaximumSize(new Dimension(
                Integer.MAX_VALUE,
                20));
        JTextField searchField = new JTextField();

        searchField.putClientProperty("JTextField.placeholderText", "Buscar serviço...");
        searchField.addActionListener(e -> {
            String query = searchField.getText();
            atualizarTabelaServicos(servicoService.buscar(query));
        });

        JButton buscarBtn = new JButton("Buscar");
        buscarBtn.addActionListener(e -> {
            String query = searchField.getText();
            atualizarTabelaServicos(servicoService.buscar(query));
        });

        JButton limparBuscaBtn = new JButton("Limpar Busca");
        limparBuscaBtn.addActionListener(e -> {
            searchField.setText("");
            atualizarTabelaServicos(null);
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        buttonPanel.add(buscarBtn);
        buttonPanel.add(limparBuscaBtn);

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(buttonPanel, BorderLayout.EAST);

        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(searchPanel);
    }

    private void createServicoList() {
        // ção
        String[] colunas = { "ID", "Nome", "Descrição", "Preço", "Deletar" };

        int nomeIdx = ArrayUtils.indexOf(colunas, "Nome");
        int deletarIdx = ArrayUtils.indexOf(colunas, "Deletar");

        this.tabelaServicosModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == deletarIdx;
            }

            @Override
            public void moveRow(int start, int end, int to) {
            }

        };

        this.tabelaServicos = new JTable(this.tabelaServicosModel);
        this.tabelaServicos.getTableHeader().setReorderingAllowed(false);
        this.tabelaServicos.setRowHeight(40);

        Action delete = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {

                JTable table = (JTable) e.getSource();
                int modelRow = Integer.valueOf(e.getActionCommand());
                Object nome = table.getModel().getValueAt(modelRow, nomeIdx);
                Window window = SwingUtilities.windowForComponent(table);

                int result = JOptionPane.showConfirmDialog(
                        window,
                        "Tem certeza que quer deletar o Serviço \"" + nome + "\"?",
                        "Confirmação",
                        JOptionPane.YES_NO_OPTION);

                if (result == JOptionPane.YES_OPTION) {
                    Long id = (Long) table.getModel().getValueAt(modelRow, 0);

                    try {
                        servicoService.removerPorId(id);
                        ((DefaultTableModel) table.getModel()).removeRow(modelRow);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(window, "Serviço não pode ser deletado!", "ERRO", JOptionPane.ERROR_MESSAGE);
                    }

                }

            }
        };

        new ButtonColumn(tabelaServicos, delete, deletarIdx);

        JScrollPane scrollPane = new JScrollPane(tabelaServicos);
        this.mainPanel.add(scrollPane);

        this.tabelaServicosModel.setRowCount(0);

        atualizarTabelaServicos(null);

    }

    private void atualizarTabelaServicos(List<Servico> servicos) {
        this.tabelaServicosModel.setRowCount(0);

        if (servicos == null)
            servicos = servicoService.buscarTodos();

        for (Servico servico : servicos) {
            tabelaServicosModel.addRow(new Object[] {
                    servico.getId(),
                    servico.getNome(),
                    servico.getDescricao(),
                    servico.getPreco(),
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
