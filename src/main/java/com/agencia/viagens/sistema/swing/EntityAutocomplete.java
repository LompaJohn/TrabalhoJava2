package com.agencia.viagens.sistema.swing;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.function.Function;

public class EntityAutocomplete<T> extends JPanel {
    private final JTextField textField;
    private final JList<String> suggestionList;
    private final DefaultListModel<String> listModel;
    private final JPopupMenu popupMenu;
    private final Function<String, List<T>> searchFunction;
    private final Function<T, String> displayConverter;
    private List<T> currentResults;
    private T selectedEntity = null;

    public EntityAutocomplete(Function<String, List<T>> searchFunction,
            Function<T, String> displayConverter,
            int width) {
        this.searchFunction = searchFunction;
        this.displayConverter = displayConverter;

        setLayout(new BorderLayout());

        textField = new JTextField(width);
        add(textField, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        suggestionList = new JList<>(listModel);
        suggestionList.setVisibleRowCount(5);

        popupMenu = new JPopupMenu();
        popupMenu.add(new JScrollPane(suggestionList));

        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateSuggestions();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateSuggestions();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateSuggestions();
            }
        });

        suggestionList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectItem();
            }
        });

        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (popupMenu.isVisible()) {
                        selectItem();
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN && popupMenu.isVisible()) {
                    suggestionList.requestFocus();
                    if (suggestionList.getModel().getSize() > 0) {
                        suggestionList.setSelectedIndex(0);
                    }
                }
            }
        });
    }

    private void updateSuggestions() {
        String text = textField.getText();
        if (text.length() < 2) {
            popupMenu.setVisible(false);
            return;
        }

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                currentResults = searchFunction.apply(text);
                return null;
            }

            @Override
            protected void done() {
                listModel.clear();
                if (currentResults.isEmpty()) {
                    popupMenu.setVisible(false);
                    return;
                }

                for (T result : currentResults) {
                    listModel.addElement(displayConverter.apply(result));
                }

                popupMenu.setFocusable(false);
                suggestionList.setFocusable(false);

                if (!popupMenu.isVisible()) {
                    popupMenu.show(textField, 0, textField.getHeight());
                }

                textField.requestFocusInWindow();

                Dimension preferredSize = suggestionList.getPreferredSize();
                int width = Math.max(textField.getWidth(), preferredSize.width);
                int height = Math.min(200, preferredSize.height);
                suggestionList.setPreferredSize(new Dimension(width, height));
                popupMenu.pack();
            }
        }.execute();
    }

    private void selectItem() {
        int selectedIndex = suggestionList.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < currentResults.size()) {
            selectedEntity = currentResults.get(selectedIndex);
            textField.setText(displayConverter.apply(selectedEntity));
            popupMenu.setVisible(false);
        }
    }

    public T getSelectedEntity() {
        return selectedEntity;
    }

    public void setSelectedEntity(T entity) {
        this.selectedEntity = entity;
        if (entity != null) {
            textField.setText(displayConverter.apply(entity));
        } else {
            textField.setText("");
        }
    }

    public void addActionListener(ActionListener listener) {
        textField.addActionListener(listener);
    }
}
