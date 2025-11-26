package view;

import controller.ReceitaController;
import dao.ReceitaIngredienteDAO;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;
import javax.swing.JTextArea;
import model.ReceitaModel;
import model.ReceitaIngredienteModel;
import model.UsuarioModel;

public class ReceitaConta extends javax.swing.JFrame {

    private final UsuarioModel usuarioLogado;
    private final ReceitaController receitaController;
    private DefaultTableModel tableModel;
    private JTable tabela;
    private java.util.List<ReceitaModel> receitasUsuario;

    public ReceitaConta(UsuarioModel usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
        this.receitaController = new ReceitaController();
        initComponents();
        carregarReceitasUsuario();
        setLocationRelativeTo(null);
    }

    private void editarCelulaSelecionada() {
        int linha = tabela.getSelectedRow();
        int coluna = tabela.getSelectedColumn();

        if (linha < 0 || coluna < 0) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Selecione uma célula para editar.",
                "Aviso",
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (receitasUsuario == null || linha >= receitasUsuario.size()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Não foi possível identificar a receita selecionada.",
                "Erro",
                javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Colunas 0, 1 e 3 (Nome, Descrição, Modo de preparo) são editáveis.
        if (coluna == 2 || coluna < 0 || coluna > 3) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Esta coluna não pode ser editada diretamente.",
                "Aviso",
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        Object valorAtualObj = tableModel.getValueAt(linha, coluna);
        String valorAtual = valorAtualObj == null ? "" : valorAtualObj.toString();

        String novoValor = javax.swing.JOptionPane.showInputDialog(
            this,
            "Informe o novo valor:",
            valorAtual
        );

        if (novoValor == null) {
            // Cancelado
            return;
        }

        novoValor = novoValor.trim();
        if (novoValor.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "O valor não pode ser vazio.",
                "Aviso",
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Salvar os ingredientes antes da edição
        List<ReceitaIngredienteModel> ingredientesBackup = null;
        try {
            ReceitaIngredienteDAO riDao = new ReceitaIngredienteDAO();
            ingredientesBackup = riDao.buscarPorReceita(receitasUsuario.get(linha).getIdReceita());
            System.out.println("Backup de ingredientes: " + (ingredientesBackup != null ? ingredientesBackup.size() : 0));
        } catch (Exception e) {
            System.out.println("Erro ao fazer backup dos ingredientes: " + e.getMessage());
        }

        // Atualizar o modelo de Receita
        ReceitaModel receita = receitasUsuario.get(linha);

        switch (coluna) {
            case 0: // Nome
                receita.setNome(novoValor);
                break;
            case 1: // Descrição
                receita.setDescricao(novoValor);
                break;
            case 3: // Modo de preparo
                receita.setModoPreparo(novoValor);
                break;
            default:
                // Já filtrado acima, mas deixamos por segurança
                return;
        }

        int opcao = javax.swing.JOptionPane.showConfirmDialog(
            this,
            "Deseja salvar a alteração?",
            "Confirmar edição",
            javax.swing.JOptionPane.YES_NO_OPTION,
            javax.swing.JOptionPane.QUESTION_MESSAGE
        );

        if (opcao != javax.swing.JOptionPane.YES_OPTION) {
            return;
        }

        boolean sucesso = receitaController.editarReceita(receita, usuarioLogado.getIdUsuario());
        System.out.println("Resultado da edição: " + sucesso);
        if (sucesso) {
            boolean ingredientesPerdidos = false;
            try {
                ReceitaIngredienteDAO riDao = new ReceitaIngredienteDAO();
                List<ReceitaIngredienteModel> ingredientesAposEdicao = riDao.buscarPorReceita(receita.getIdReceita());
                System.out.println("Ingredientes após edição: " + (ingredientesAposEdicao != null ? ingredientesAposEdicao.size() : 0));
                
                if (ingredientesAposEdicao == null || ingredientesAposEdicao.isEmpty()) {
                    ingredientesPerdidos = true;
                    
                    if (ingredientesBackup != null && !ingredientesBackup.isEmpty()) {
                        try (java.sql.Connection conn = dao.ConnectionFactory.getConnection()) {
                            riDao.salvarItensReceita(receita.getIdReceita(), ingredientesBackup, conn);
                        } catch (Exception e) {
                            System.out.println("Erro ao restaurar ingredientes: " + e.getMessage());
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Erro ao verificar/restaurar ingredientes: " + e.getMessage());
            }
            
            // Atualizar a célula editada
            tableModel.setValueAt(novoValor, linha, coluna);
            
            if (ingredientesPerdidos) {
                recarregarLinhaEspecifica(linha, receita);
            }
            
            javax.swing.JOptionPane.showMessageDialog(this,
                "Alteração salva com sucesso.",
                "Sucesso",
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
        } else {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Não foi possível salvar a alteração da receita.",
                "Erro",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initComponents() {
        JScrollPane scrollPane = new JScrollPane();
        tabela = new JTable();

        tableModel = new DefaultTableModel(
            new Object [][] {},
            new String [] {"Nome", "Descrição", "Categoria", "Modo de preparo", "Ingredientes", "Data criação"}
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

        // Larguras semelhantes às de CategoriaReceitaView
        tabela.getColumnModel().getColumn(0).setPreferredWidth(150); // Nome
        tabela.getColumnModel().getColumn(1).setPreferredWidth(280); // Descrição
        tabela.getColumnModel().getColumn(2).setPreferredWidth(120); // Categoria
        tabela.getColumnModel().getColumn(3).setPreferredWidth(280); // Modo de preparo
        tabela.getColumnModel().getColumn(4).setPreferredWidth(280); // Ingredientes
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
        tabela.getColumnModel().getColumn(2).setCellRenderer(textAreaRenderer); // Categoria
        tabela.getColumnModel().getColumn(3).setCellRenderer(textAreaRenderer); // Modo preparo
        tabela.getColumnModel().getColumn(4).setCellRenderer(textAreaRenderer); // Ingredientes
        tabela.getColumnModel().getColumn(5).setCellRenderer(textAreaRenderer); // Data criação
        scrollPane.setViewportView(tabela);

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Minhas Receitas");
        getContentPane().setLayout(new java.awt.BorderLayout());
        getContentPane().add(scrollPane, java.awt.BorderLayout.CENTER);

        // Ao fechar esta tela, voltar para o MenuView
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                MenuView menu = new MenuView(usuarioLogado);
                menu.setVisible(true);
                dispose();
            }
        });

        // Menu de contexto
        javax.swing.JPopupMenu popupMenu = new javax.swing.JPopupMenu();
        javax.swing.JMenuItem publicarItem = new javax.swing.JMenuItem("Publicar");
        publicarItem.addActionListener(e -> publicarReceitaSelecionada());
        popupMenu.add(publicarItem);

        javax.swing.JMenuItem editarCelulaItem = new javax.swing.JMenuItem("Editar célula");
        editarCelulaItem.addActionListener(e -> editarCelulaSelecionada());
        popupMenu.add(editarCelulaItem);

        javax.swing.JMenuItem excluirItem = new javax.swing.JMenuItem("Excluir receita");
        excluirItem.addActionListener(e -> excluirReceitaSelecionada());
        popupMenu.add(excluirItem);

        tabela.setComponentPopupMenu(popupMenu);

        pack();
    }

    private void publicarReceitaSelecionada() {
    }

    private void carregarReceitasUsuario() {
        receitasUsuario = receitaController.listarReceitasDoUsuario(usuarioLogado);
        ReceitaIngredienteDAO riDao = new ReceitaIngredienteDAO();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        tableModel.setRowCount(0);
        for (ReceitaModel r : receitasUsuario) {
            String ingredientesStr = "";
            try {
                List<ReceitaIngredienteModel> itens = riDao.buscarPorReceita(r.getIdReceita());
                
                StringBuilder sb = new StringBuilder();
                if (itens != null) {
                    for (ReceitaIngredienteModel item : itens) {
                        if (sb.length() > 0) {
                            sb.append(", ");
                        }
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
                System.out.println("Erro ao carregar ingredientes: " + e.getMessage());
                ingredientesStr = "Erro ao carregar";
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
                r.getCategoria(),
                r.getModoPreparo(),
                ingredientesStr,
                dataFormatada
            });
        }
    }

    private void recarregarLinhaEspecifica(int linha, ReceitaModel receita) {
        try {
            ReceitaIngredienteDAO riDao = new ReceitaIngredienteDAO();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            List<ReceitaIngredienteModel> itens = riDao.buscarPorReceita(receita.getIdReceita());
            
            String ingredientesStr = "";
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
            
            String dataFormatada = "";
            if (receita.getDataCriacao() != null) {
                dataFormatada = receita.getDataCriacao().format(formatter);
            } else if (receita.getData_criacao() != null) {
                dataFormatada = receita.getData_criacao().format(formatter);
            }
            
            // Atualizar a linha inteira
            tableModel.setValueAt(receita.getNome(), linha, 0);
            tableModel.setValueAt(receita.getDescricao(), linha, 1);
            tableModel.setValueAt(receita.getCategoria(), linha, 2);
            tableModel.setValueAt(receita.getModoPreparo(), linha, 3);
            tableModel.setValueAt(ingredientesStr, linha, 4);
            tableModel.setValueAt(dataFormatada, linha, 5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void excluirReceitaSelecionada() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Selecione uma receita para excluir.",
                "Aviso",
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (receitasUsuario == null || linha >= receitasUsuario.size()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Não foi possível identificar a receita selecionada.",
                "Erro",
                javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        ReceitaModel receita = receitasUsuario.get(linha);

        int opcao = javax.swing.JOptionPane.showConfirmDialog(this,
            "Deseja realmente excluir a receita '" + receita.getNome() + "'?",
            "Confirmar exclusão",
            javax.swing.JOptionPane.YES_NO_OPTION,
            javax.swing.JOptionPane.QUESTION_MESSAGE);

        if (opcao != javax.swing.JOptionPane.YES_OPTION) {
            return;
        }

        String resultado = receitaController.excluirReceita(receita.getIdReceita(), usuarioLogado.getIdUsuario());

        javax.swing.JOptionPane.showMessageDialog(this,
            resultado,
            "Excluir receita",
            resultado.startsWith("SUCESSO") ? javax.swing.JOptionPane.INFORMATION_MESSAGE : javax.swing.JOptionPane.ERROR_MESSAGE);

        if (resultado.startsWith("SUCESSO")) {
            carregarReceitasUsuario();
        }
    }
}
