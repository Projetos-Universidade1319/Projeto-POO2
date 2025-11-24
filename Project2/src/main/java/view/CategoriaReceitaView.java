package view;

import controller.ReceitaController;
import dao.ReceitaIngredienteDAO;
import java.awt.Component;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import model.ReceitaModel;
import model.ReceitaIngredienteModel;
import model.UsuarioModel;

public class CategoriaReceitaView extends javax.swing.JFrame {

    private final UsuarioModel usuarioLogado;
    private final String categoria;
    private final ReceitaController receitaController;
    private DefaultTableModel tableModel;
    private JTable tabela;

    public CategoriaReceitaView(UsuarioModel usuarioLogado, String categoria) {
        this.usuarioLogado = usuarioLogado;
        this.categoria = categoria;
        this.receitaController = new ReceitaController();
        initComponents();
        carregarReceitas();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Receitas - " + categoria);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        getContentPane().setLayout(new java.awt.BorderLayout());

        tabela = new JTable();
        tableModel = new DefaultTableModel(
            new Object[][]{},
            new String[]{"Nome", "Descrição", "Categoria", "Modo de preparo", "Ingredientes", "Curtidas", "Data criação"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela.setModel(tableModel);
        tabela.getTableHeader().setReorderingAllowed(false);

        // Renderer para quebrar linha em colunas de texto longo
        TableCellRenderer textAreaRenderer = new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                JTextArea area = new JTextArea();
                area.setLineWrap(true);
                area.setWrapStyleWord(true);
                area.setText(value == null ? "" : value.toString());
                area.setEditable(false);
                area.setOpaque(true);
                if (isSelected) {
                    area.setBackground(table.getSelectionBackground());
                    area.setForeground(table.getSelectionForeground());
                } else {
                    area.setBackground(table.getBackground());
                    area.setForeground(table.getForeground());
                }
                // Ajustar altura aproximada da linha
                int prefHeight = area.getPreferredSize().height;
                if (table.getRowHeight(row) < prefHeight) {
                    table.setRowHeight(row, prefHeight);
                }
                return area;
            }
        };

        tabela.getColumnModel().getColumn(1).setCellRenderer(textAreaRenderer); // Descrição
        tabela.getColumnModel().getColumn(3).setCellRenderer(textAreaRenderer); // Modo preparo
        tabela.getColumnModel().getColumn(4).setCellRenderer(textAreaRenderer); // Ingredientes

        JScrollPane scrollPane = new JScrollPane(tabela);

        JButton voltarButton = new JButton("Voltar");
        voltarButton.addActionListener(e -> voltarMenu());

        JPanel bottomPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        bottomPanel.add(voltarButton);

        getContentPane().add(scrollPane, java.awt.BorderLayout.CENTER);
        getContentPane().add(bottomPanel, java.awt.BorderLayout.SOUTH);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                voltarMenu();
            }
        });

        pack();
    }

    private void carregarReceitas() {
        List<ReceitaModel> receitas = receitaController.buscarEClassificar("", categoria, usuarioLogado);
        ReceitaIngredienteDAO riDao = new ReceitaIngredienteDAO();
        tableModel.setRowCount(0);
        for (ReceitaModel r : receitas) {
            // Montar string de ingredientes
            String ingredientesStr = "";
            try {
                List<ReceitaIngredienteModel> itens = riDao.buscarPorReceita(r.getIdReceita());
                StringBuilder sb = new StringBuilder();
                for (ReceitaIngredienteModel item : itens) {
                    if (sb.length() > 0) sb.append(", ");
                    if (item.getIngrediente() != null) {
                        sb.append(item.getIngrediente().getNome())
                          .append(" (")
                          .append(item.getQuantidade()).append(" ")
                          .append(item.getIngrediente().getUnidade_medida())
                          .append(")");
                    } else {
                        sb.append("Ingrediente #").append(item.getId_ingrediente())
                          .append(" (").append(item.getQuantidade()).append(")");
                    }
                }
                ingredientesStr = sb.toString();
            } catch (Exception e) {
                ingredientesStr = "Erro ao carregar";
            }

            tableModel.addRow(new Object[]{
                r.getNome(),
                r.getDescricao(),
                r.getCategoria(),
                r.getModoPreparo(),
                ingredientesStr,
                r.getCurtidas(),
                r.getDataCriacao() != null ? r.getDataCriacao() : r.getData_criacao()
            });
        }
    }

    private void voltarMenu() {
        MenuView menu = new MenuView(usuarioLogado);
        menu.setVisible(true);
        dispose();
    }
}
