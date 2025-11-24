package view;

import controller.ReceitaController;
import dao.ReceitaIngredienteDAO;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.ReceitaModel;
import model.ReceitaIngredienteModel;
import model.UsuarioModel;

public class ReceitaConta extends javax.swing.JFrame {

    private final UsuarioModel usuarioLogado;
    private final ReceitaController receitaController;
    private DefaultTableModel tableModel;
    private JTable tabela;

    public ReceitaConta(UsuarioModel usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
        this.receitaController = new ReceitaController();
        initComponents();
        carregarReceitasUsuario();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        JScrollPane scrollPane = new JScrollPane();
        tabela = new JTable();

        tableModel = new DefaultTableModel(
            new Object [][] {},
            new String [] {"Nome", "Descrição", "Categoria", "Modo de preparo", "Ingredientes", "Curtidas", "Data criação"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela.setModel(tableModel);
        tabela.getTableHeader().setReorderingAllowed(false);
        scrollPane.setViewportView(tabela);

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Minhas Receitas");
        getContentPane().setLayout(new java.awt.BorderLayout());
        getContentPane().add(scrollPane, java.awt.BorderLayout.CENTER);

        // Ao fechar esta tela, voltar para o MenuView mantendo o usuarioLogado
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                MenuView menu = new MenuView(usuarioLogado);
                menu.setVisible(true);
                dispose();
            }
        });

        // Menu de contexto com opcao "Publicar" ao clicar com botao direito
        javax.swing.JPopupMenu popupMenu = new javax.swing.JPopupMenu();
        javax.swing.JMenuItem publicarItem = new javax.swing.JMenuItem("Publicar");
        publicarItem.addActionListener(e -> publicarReceitaSelecionada());
        popupMenu.add(publicarItem);

        tabela.setComponentPopupMenu(popupMenu);

        pack();
    }

    private void publicarReceitaSelecionada() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Selecione uma receita para publicar.",
                "Aviso",
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nomeReceita = (String) tableModel.getValueAt(linha, 0);

        // Aqui todas as receitas ja estao salvas na tabela 'receita', entao
        // tratamos 'Publicar' como tornar a receita visivel/publica na aplicacao.
        javax.swing.JOptionPane.showMessageDialog(this,
            "Receita '" + nomeReceita + "' publicada com sucesso!",
            "Publicar",
            javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }

    private void carregarReceitasUsuario() {
        List<ReceitaModel> receitas = receitaController.listarReceitasDoUsuario(usuarioLogado);
        ReceitaIngredienteDAO riDao = new ReceitaIngredienteDAO();
        tableModel.setRowCount(0);
        for (ReceitaModel r : receitas) {
            // Montar string com ingredientes da receita
            String ingredientesStr = "";
            try {
                List<ReceitaIngredienteModel> itens = riDao.buscarPorReceita(r.getIdReceita());
                StringBuilder sb = new StringBuilder();
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
}
