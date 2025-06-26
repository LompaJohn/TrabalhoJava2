package com.agencia.viagens.sistema.swing.frames;

import com.agencia.viagens.sistema.entity.Cliente;
import com.agencia.viagens.sistema.entity.ClienteTipo;
import com.agencia.viagens.sistema.entity.Pacote;
import com.agencia.viagens.sistema.entity.Pedido;
import com.agencia.viagens.sistema.entity.PedidoServico;
import com.agencia.viagens.sistema.entity.Servico;
import com.agencia.viagens.sistema.service.ClienteService;
import com.agencia.viagens.sistema.service.PacoteService;
import com.agencia.viagens.sistema.service.PedidoService;
import com.agencia.viagens.sistema.service.PedidoServicoService;
import com.agencia.viagens.sistema.service.ServicoService;
import com.agencia.viagens.sistema.swing.ApplicationMain;
import com.agencia.viagens.sistema.swing.ButtonColumn;
import com.agencia.viagens.sistema.swing.EntityAutocomplete;
import com.formdev.flatlaf.FlatDarkLaf;
import com.github.lgooddatepicker.components.CalendarPanel;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.optionalusertools.DateVetoPolicy;

import jakarta.validation.ValidationException;
import org.apache.commons.lang3.ArrayUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.HierarchyEvent;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

record ServicoCount(Servico servico, long count) {
}

public class PedidosFrame extends JFrame {
    private final JFrame parent;
    private DefaultTableModel tabelaPedidosModel;
    private JTable tabelaPedidos;
    private PedidoService pedidosService;
    private ServicoService servicoService;
    private ClienteService clienteService;
    private PacoteService pacoteService;
    private PedidoServicoService pedidoServicoService;
    private JPanel mainPanel;

    public PedidosFrame(JFrame parent) {
        this.parent = parent;

        this.pedidosService = ApplicationMain.getSpringContext().getBean(PedidoService.class);
        this.pacoteService = ApplicationMain.getSpringContext().getBean(PacoteService.class);
        this.servicoService = ApplicationMain.getSpringContext().getBean(ServicoService.class);
        this.pedidoServicoService = ApplicationMain.getSpringContext().getBean(PedidoServicoService.class);
        this.clienteService = ApplicationMain.getSpringContext().getBean(ClienteService.class);

        this.mainPanel = new JPanel();
        this.mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);

        setTitle("Pedidios Cadastrados");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.NORMAL);
        setSize(1280, 720);

        setLayout(new BorderLayout());

        createMenuBar();
        createHeader();
        createServicosSearch();
        createPedidoTable();

        this.add(mainPanel, BorderLayout.CENTER);
    }

    private void createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout(5, 5));

        JLabel pedidiosLabel = new JLabel("Pedidios Cadastrados");
        pedidiosLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JButton addButton = new JButton("Novo Pedido");
        addButton.addActionListener(e -> showPedidoForm());

        headerPanel.setMaximumSize(new Dimension(
                Integer.MAX_VALUE,
                10));

        headerPanel.add(pedidiosLabel, BorderLayout.WEST);
        headerPanel.add(addButton, BorderLayout.EAST);

        this.mainPanel.add(headerPanel);
    }

    private void showPedidoForm() {
        JDialog dialogo = new JDialog(this, "Cadastrar Novo Pedido", true);
        dialogo.setSize(400, 500);
        dialogo.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel clienteLabel = new JLabel("Cliente: ");
        EntityAutocomplete<Cliente> clienteSearch = new EntityAutocomplete<>(
                query -> clienteService.buscar(query),
                cliente -> cliente.getNome() + " ("
                        + (cliente.getTipo() == ClienteTipo.NACIONAL ? cliente.getCpf() : cliente.getPassaporte())
                        + ")",
                30);

        JLabel pacoteLabel = new JLabel("Pacote: ");
        EntityAutocomplete<Pacote> pacoteSearch = new EntityAutocomplete<>(
                query -> pacoteService.buscar(query),
                pacote -> "[" + pacote.getId() + "] " + pacote.getNome() + " R$" + pacote.getPreco(),
                30);

        DatePickerSettings settings = new DatePickerSettings() {
            @Override
            public Color getColor(DateArea area) {
                switch (area) {
                    // Backgrounds
                    case BackgroundCalendarPanelLabelsOnHover:
                        return UIManager.getColor("Component.focusColor");
                    case BackgroundClearLabel:
                        return UIManager.getColor("Button.background");
                    case BackgroundMonthAndYearMenuLabels:
                        return UIManager.getColor("Menu.background");
                    case BackgroundMonthAndYearNavigationButtons:
                        return UIManager.getColor("Button.background");
                    case BackgroundOverallCalendarPanel:
                        return UIManager.getColor("Panel.background");
                    case BackgroundTodayLabel:
                        return UIManager.getColor("Button.background");
                    case BackgroundTopLeftLabelAboveWeekNumbers:
                        return UIManager.getColor("TableHeader.background");
                    case CalendarBackgroundNormalDates:
                        return UIManager.getColor("Table.background");
                    case CalendarBackgroundSelectedDate:
                        return UIManager.getColor("List.selectionBackground");
                    case CalendarBackgroundVetoedDates:
                        return new Color(80, 50, 50);
                    case CalendarDefaultBackgroundHighlightedDates:
                        return new Color(70, 70, 100);
                    case CalendarBorderSelectedDate:
                        return UIManager.getColor("Component.focusColor");
                    case CalendarTextNormalDates:
                        return UIManager.getColor("Label.foreground");
                    case CalendarTextWeekNumbers:
                        return UIManager.getColor("Label.foreground").brighter();
                    case CalendarTextWeekdays:
                        return UIManager.getColor("Label.foreground");
                    case CalendarDefaultTextHighlightedDates:
                        return UIManager.getColor("Label.foreground").brighter();
                    case DatePickerTextDisabled:
                        return UIManager.getColor("Label.disabledForeground");
                    case DatePickerTextInvalidDate:
                        return Color.PINK;
                    case DatePickerTextValidDate:
                        return UIManager.getColor("TextField.foreground");
                    case DatePickerTextVetoedDate:
                        return new Color(255, 150, 150);
                    case TextCalendarPanelLabelsOnHover:
                        return UIManager.getColor("Label.foreground").brighter();
                    case TextClearLabel:
                        return UIManager.getColor("Button.foreground");
                    case TextMonthAndYearMenuLabels:
                        return UIManager.getColor("Menu.foreground");
                    case TextMonthAndYearNavigationButtons:
                        return UIManager.getColor("Button.foreground");
                    case TextTodayLabel:
                        return UIManager.getColor("Button.foreground");
                    case TextFieldBackgroundDisabled:
                        return UIManager.getColor("TextField.inactiveBackground");
                    case TextFieldBackgroundDisallowedEmptyDate:
                        return new Color(80, 60, 60);
                    case TextFieldBackgroundInvalidDate:
                        return new Color(80, 50, 50);
                    case TextFieldBackgroundValidDate:
                        return UIManager.getColor("TextField.background");
                    case TextFieldBackgroundVetoedDate:
                        return new Color(80, 50, 50);
                    default:
                        return super.getColor(area);
                }
            }
        };

        settings.setAllowEmptyDates(false);
        settings.setFormatForDatesCommonEra("dd/MM/yyyy");

        JLabel dataViagemLabel = new JLabel("Data da Viagem:");
        DatePicker dataViagemPicker = new DatePicker(settings);

        dataViagemPicker.getSettings().setDateRangeLimits(
                LocalDate.now(),
                null);

        dataViagemPicker.getSettings().setVetoPolicy(new DateVetoPolicy() {
            @Override
            public boolean isDateAllowed(LocalDate date) {
                return (!date.isBefore(LocalDate.now())) && !date.isEqual(LocalDate.now());

            }
        });

        JLabel servicosLabel = new JLabel("Serviços:");

        DefaultListModel<Servico> selectedItemsModel = new DefaultListModel<>();
        JList<Servico> selectedItemsList = new JList<>(selectedItemsModel);
        selectedItemsList.setCellRenderer(new ServicoListRenderer());
        selectedItemsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane listScrollPane = new JScrollPane(selectedItemsList);
        listScrollPane.setSize(new Dimension(panel.getWidth(), 50));
        listScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        EntityAutocomplete<Servico> servicosSearch = new EntityAutocomplete<>(
                query -> servicoService.buscar(query),
                servico -> servico.getNome() + " - R$" + servico.getPreco(),
                30);

        servicosSearch.addActionListener(e -> {
            Servico selected = servicosSearch.getSelectedEntity();
            if (selected != null && !selectedItemsModel.contains(selected)) {
                selectedItemsModel.addElement(selected);
                servicosSearch.setSelectedEntity(null);
                servicosSearch.requestFocusInWindow();
            }
        });

        JButton saveButton = new JButton("Salvar");
        JPanel saveBtnPanel = new JPanel(new BorderLayout());
        saveBtnPanel.add(saveButton, BorderLayout.CENTER);

        JButton cancelButton = new JButton("Cancelar");
        JPanel cancelBtnPanel = new JPanel(new BorderLayout());
        cancelBtnPanel.add(cancelButton, BorderLayout.CENTER);

        saveButton.addActionListener(e -> {
            Pedido pedido = new Pedido();

            Cliente cliente = clienteSearch.getSelectedEntity();
            if (cliente == null) {
                JOptionPane.showMessageDialog(this, "Cliente não pode ser vazio!", "ERRO", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Pacote pacote = pacoteSearch.getSelectedEntity();
            if (pacote == null) {
                JOptionPane.showMessageDialog(this, "Pacote não pode ser vazio!", "ERRO", JOptionPane.ERROR_MESSAGE);
                return;
            }

            pedido.setCliente(cliente);
            pedido.setPacote(pacote);
            pedido.setDataViagem(dataViagemPicker.getDate());
            pedido.setDataContratacao(LocalDate.now());
            pedido.setDataViagem(dataViagemPicker.getDate());
            BigDecimal total = pacote.getPreco();

            List<ServicoCount> servicos = Collections.list(selectedItemsModel.elements()).stream()
                    .collect(Collectors.groupingBy(
                            s -> s.getId(),
                            Collectors.counting()))
                    .entrySet()
                    .stream()
                    .map(entry -> new ServicoCount(servicoService.buscarPorId(entry.getKey()).orElseThrow(),
                            entry.getValue()))
                    .collect(Collectors.toList());

            pedido.setValorTotal(total);

            for (ServicoCount servicoCount : servicos) {
                total = total.add(servicoCount.servico().getPreco().multiply(new BigDecimal(servicoCount.count())));
            }

            pedidosService.salvarPedido(pedido);

            for (ServicoCount servicoCount : servicos) {
                PedidoServico pdsv = new PedidoServico();
                System.err.println("AAAAAAAAAAAAAAAAAAA: " + servicoCount.count());

                pdsv.setServico(servicoCount.servico());
                pdsv.setPedido(pedido);
                pdsv.setQuantidade(servicoCount.count());
                pdsv.setPrecoUnitario(servicoCount.servico().getPreco());
                pedidoServicoService.salvarRaw(pdsv);

            }

            atualizarTabelaPedidos(null);

            dialogo.dispose();
        });

        cancelButton.addActionListener(e -> {
            dialogo.dispose();
        });

        panel.add(clienteLabel);
        panel.add(clienteSearch);
        panel.add(pacoteLabel);
        panel.add(pacoteSearch);
        panel.add(dataViagemLabel);
        panel.add(dataViagemPicker);
        panel.add(servicosLabel);
        panel.add(servicosSearch);
        panel.add(listScrollPane);
        panel.add(Box.createVerticalStrut(5));
        panel.add(saveBtnPanel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(cancelBtnPanel);
        panel.add(Box.createVerticalStrut(5));
        dialogo.add(panel);
        dialogo.setVisible(true);
    }

    private void createServicosSearch() {
        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        searchPanel.setMaximumSize(new Dimension(
                Integer.MAX_VALUE,
                20));
        JTextField searchField = new JTextField();

        searchField.putClientProperty("JTextField.placeholderText", "Buscar pedido...");
        searchField.addActionListener(e -> {
            String query = searchField.getText();
            atualizarTabelaPedidos(pedidosService.buscar(query));
        });

        JButton buscarBtn = new JButton("Buscar");
        buscarBtn.addActionListener(e -> {
            String query = searchField.getText();
            atualizarTabelaPedidos(pedidosService.buscar(query));
        });

        JButton limparBuscaBtn = new JButton("Limpar Busca");
        limparBuscaBtn.addActionListener(e -> {
            searchField.setText("");
            atualizarTabelaPedidos(null);
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        buttonPanel.add(buscarBtn);
        buttonPanel.add(limparBuscaBtn);

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(buttonPanel, BorderLayout.EAST);

        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(searchPanel);
    }

    private void createPedidoTable() {
        // ção
        String[] colunas = { "ID", "Cliente", "Pacote", "Data Contratação", "Data Viagem", "Valor Total",
                "Ver Serviços", "Deletar" };

        int servicosIdx = ArrayUtils.indexOf(colunas, "Ver Serviços");
        int deletarIdx = ArrayUtils.indexOf(colunas, "Deletar");

        this.tabelaPedidosModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == deletarIdx || column == servicosIdx;
            }

            @Override
            public void moveRow(int start, int end, int to) {
            }

        };

        this.tabelaPedidos = new JTable(this.tabelaPedidosModel);
        this.tabelaPedidos.getTableHeader().setReorderingAllowed(false);
        this.tabelaPedidos.setRowHeight(40);

        Action delete = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {

                JTable table = (JTable) e.getSource();
                int modelRow = Integer.valueOf(e.getActionCommand());
                Long id = (Long) table.getModel().getValueAt(modelRow, 0);
                Window window = SwingUtilities.windowForComponent(table);

                int result = JOptionPane.showConfirmDialog(
                        window,
                        "Tem certeza que quer deletar o Pedido [" + id + "]?",
                        "Confirmação",
                        JOptionPane.YES_NO_OPTION);

                if (result == JOptionPane.YES_OPTION) {
                    ((DefaultTableModel) table.getModel()).removeRow(modelRow);

                    pedidosService.removerPorId(id);
                }

            }
        };

        Action mostrarServicos = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {

                JTable table = (JTable) e.getSource();
                int modelRow = Integer.valueOf(e.getActionCommand());
                Long id = (Long) table.getModel().getValueAt(modelRow, 0);
                Window window = SwingUtilities.windowForComponent(table);

                List<PedidoServico> pedidoServicos = pedidoServicoService.buscarPorPedidoId(id);

                if (pedidoServicos.isEmpty()) {
                    JOptionPane.showMessageDialog(window, "Nenhum serviço encontrado!");
                    return;
                }

                StringBuilder sb = new StringBuilder();

                for (PedidoServico servico : pedidoServicos) {
                    sb.append("\u2022 ");
                    sb.append(servico.getQuantidade());
                    sb.append("x [");
                    sb.append(servico.getServico().getId());
                    sb.append("] ");
                    sb.append(servico.getServico().getNome());
                    sb.append("\n");
                }

                JTextArea area = new JTextArea(sb.toString());
                area.setEditable(false);

                JOptionPane.showMessageDialog(window, area);

            }
        };

        new ButtonColumn(tabelaPedidos, mostrarServicos, servicosIdx);
        new ButtonColumn(tabelaPedidos, delete, deletarIdx);

        JScrollPane scrollPane = new JScrollPane(tabelaPedidos);
        this.mainPanel.add(scrollPane);

        this.tabelaPedidosModel.setRowCount(0);

        atualizarTabelaPedidos(null);

    }

    private void atualizarTabelaPedidos(List<Pedido> pedidos) {
        this.tabelaPedidosModel.setRowCount(0);

        if (pedidos == null)
            pedidos = pedidosService.buscarTodos();

        for (Pedido pedido : pedidos) {
            Cliente cliente = pedido.getCliente();
            Pacote pacote = pedido.getPacote();

            tabelaPedidosModel.addRow(new Object[] {
                    pedido.getId(),
                    cliente.getNome() + " ("
                            + (cliente.getTipo() == ClienteTipo.NACIONAL ? cliente.getCpf() : cliente.getPassaporte())
                            + ")",
                    "[" + pacote.getId() + "] " + pacote.getNome(),
                    pedido.getDataContratacao(),
                    pedido.getDataViagem(),
                    pedido.getValorTotal(),
                    "Ver Serviços",
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

    private static class ServicoListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Servico) {
                Servico servico = (Servico) value;
                setText(servico.getNome() + " - R$" + servico.getPreco());
            }
            return this;
        }
    }
}
