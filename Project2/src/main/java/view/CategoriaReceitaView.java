package view;

import controller.ReceitaController;
import dao.ReceitaIngredienteDAO;
import dao.UsuarioDAO;
import java.awt.Component;
import java.time.format.DateTimeFormatter;
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
            new String[]{"Nome", "Descrição", "Modo de preparo", "Ingredientes", "Usuário", "Data criação"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela.setModel(tableModel);
        tabela.getTableHeader().setReorderingAllowed(false);
        tabela.setRowMargin(8);
        tabela.setIntercellSpacing(new java.awt.Dimension(8, 8));
        tabela.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Aumentar bastante a largura das colunas de texto longo (sem Categoria)
        tabela.getColumnModel().getColumn(0).setPreferredWidth(150); // Nome
        tabela.getColumnModel().getColumn(1).setPreferredWidth(280); // Descrição
        tabela.getColumnModel().getColumn(2).setPreferredWidth(280); // Modo de preparo
        tabela.getColumnModel().getColumn(3).setPreferredWidth(280); // Ingredientes
        tabela.getColumnModel().getColumn(4).setPreferredWidth(140); // Usuário
        tabela.getColumnModel().getColumn(5).setPreferredWidth(150); // Data criação

        // Renderer para quebrar linha em colunas de texto longo e ajustar a altura da linha
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

                // Definir a largura do JTextArea igual à largura da coluna
                int colWidth = table.getColumnModel().getColumn(column).getWidth();
                area.setSize(colWidth, Short.MAX_VALUE);

                // Calcular a altura ideal com base no conteúdo e largura
                int prefHeight = area.getPreferredSize().height + 12; // margem extra
                if (table.getRowHeight(row) < prefHeight) {
                    table.setRowHeight(row, prefHeight);
                }
                return area;
            }
        };

        tabela.getColumnModel().getColumn(0).setCellRenderer(textAreaRenderer); // Nome
        tabela.getColumnModel().getColumn(1).setCellRenderer(textAreaRenderer); // Descrição
        tabela.getColumnModel().getColumn(2).setCellRenderer(textAreaRenderer); // Modo preparo
        tabela.getColumnModel().getColumn(3).setCellRenderer(textAreaRenderer); // Ingredientes
        tabela.getColumnModel().getColumn(4).setCellRenderer(textAreaRenderer); // Usuário
        tabela.getColumnModel().getColumn(5).setCellRenderer(textAreaRenderer); // Data criação

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
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        tableModel.setRowCount(0);
        
        if (receitas == null || receitas.isEmpty()) {
            System.out.println("Nenhuma receita encontrada para categoria: " + categoria);
            List<ReceitaModel> todasReceitas = receitaController.listarReceitasDoUsuario(usuarioLogado);
            System.out.println("Total de receitas do usuário: " + (todasReceitas != null ? todasReceitas.size() : 0));
            if (todasReceitas != null) {
                for (ReceitaModel r : todasReceitas) {
                    System.out.println("Receita: " + r.getNome() + " - Categoria: '" + r.getCategoria() + "'");
                }
            }
        }
        
        for (ReceitaModel r : receitas) {
            String ingredientesStr = "";
            try {
                List<ReceitaIngredienteModel> itens = riDao.buscarPorReceita(r.getIdReceita());
                StringBuilder sb = new StringBuilder();
                if (itens != null) {
                    for (ReceitaIngredienteModel item : itens) {
                        if (sb.length() > 0) sb.append(", ");
                        if (item.getIngrediente() != null) {
                            sb.append(item.getIngrediente().getNome())
                              .append(" (")
                              .append(item.getQuantidade())
                              .append(" ")
                              .append(item.getIngrediente().getUnidade_medida())
                              .append(")");
                        } else {
                            sb.append("Ingrediente #")
                              .append(item.getId_ingrediente())
                              .append(" (")
                              .append(item.getQuantidade())
                              .append(")");
                        }
                    }
                }
                ingredientesStr = sb.toString();
            } catch (Exception e) {
                ingredientesStr = "Erro ao carregar";
            }

            String nomeAutor = "Anônimo";
            try {
                if (r.getIdUsuario() != 0) {
                    UsuarioModel autor = usuarioDAO.buscarPorId(r.getIdUsuario());
                    if (autor != null) {
                        nomeAutor = autor.getNome();
                    }
                }
            } catch (Exception e) {
                nomeAutor = "Erro ao carregar";
            }

            String dataFormatada = "";
            if (r.getDataCriacao() != null) {
                dataFormatada = r.getDataCriacao().format(formatter);
            } else if (r.getData_criacao() != null) {
                dataFormatada = r.getData_criacao().format(formatter);
            }

            tableModel.addRow(new Object[]{
                r.getNome(),
                r.getDescricao(),
                r.getModoPreparo(),
                ingredientesStr,
                nomeAutor,
                dataFormatada
            });
        }
    }

    private void voltarMenu() {
        MenuView menu = new MenuView(usuarioLogado);
        menu.setVisible(true);
        dispose();
    }
}
