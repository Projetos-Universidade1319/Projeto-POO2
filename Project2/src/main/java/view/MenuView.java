
package view;

import model.UsuarioModel;

public class MenuView extends javax.swing.JFrame {

    private UsuarioModel usuarioLogado;
    
    public MenuView() {
        initComponents();
        this.usuarioLogado = null;
        controlarAcesso(false); 
        setLocationRelativeTo(null);
    }
    
    public MenuView(UsuarioModel usuario) {
        initComponents();
        this.usuarioLogado = usuario;
        controlarAcesso(usuario != null); 
        if (usuario != null) {
            NomeDoUsuarioLabel.setText("Bem-vindo, " + usuario.getNome() + "!");
        }
        setLocationRelativeTo(null);
    }

    private void controlarAcesso(boolean logado) {
        CriarMenu.setEnabled(logado);
        PerfilMenu.setVisible(logado);
        
        if (logado) {
            LoginItem.setText("Sair (Logout)");
            CadastroItem.setVisible(false); 
        } else {
            LoginItem.setText("Login");
            CadastroItem.setVisible(true);
        }
    

    }
    
    
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        PrincipalPanel = new javax.swing.JPanel();
        TituloLabel = new javax.swing.JLabel();
        NomeDoUsuarioLabel = new javax.swing.JLabel();
        SobremesaButton = new javax.swing.JButton();
        AperetivoButton = new javax.swing.JButton();
        BebidaButton = new javax.swing.JButton();
        SaladaButton = new javax.swing.JButton();
        SalgadoButton = new javax.swing.JButton();
        DoceButton = new javax.swing.JButton();
        AlmocoButton = new javax.swing.JButton();
        JantaButton = new javax.swing.JButton();
        MenuBar = new javax.swing.JMenuBar();
        CriarMenu = new javax.swing.JMenu();
        ReceitaItem = new javax.swing.JMenuItem();
        EntrarMenu = new javax.swing.JMenu();
        LoginItem = new javax.swing.JMenuItem();
        CadastroItem = new javax.swing.JMenuItem();
        PerfilMenu = new javax.swing.JMenu();
        InformacoesMenuItem = new javax.swing.JMenuItem();
        ReceitasMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("TelaPrincipal");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        PrincipalPanel.setLayout(new java.awt.GridBagLayout());

        TituloLabel.setBackground(new java.awt.Color(0, 0, 0));
        TituloLabel.setDisplayedMnemonic('0');
        TituloLabel.setFont(new java.awt.Font("Segoe UI Historic", 0, 24)); // NOI18N
        TituloLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TituloLabel.setText("Livro de Receitas");
        TituloLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        TituloLabel.setAlignmentX(1.0F);
        TituloLabel.setAlignmentY(0.0F);
        TituloLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        TituloLabel.setFocusable(false);
        TituloLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 2.0;
        gridBagConstraints.insets = new java.awt.Insets(20, 0, 50, 0);
        PrincipalPanel.add(TituloLabel, gridBagConstraints);

        NomeDoUsuarioLabel.setBackground(new java.awt.Color(255, 255, 255));
        NomeDoUsuarioLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        NomeDoUsuarioLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 31;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(16, 0, 0, 70);
        PrincipalPanel.add(NomeDoUsuarioLabel, gridBagConstraints);

        SobremesaButton.setFont(new java.awt.Font("Segoe UI", 0, 9)); // NOI18N
        SobremesaButton.setText("Sobremesa");
        SobremesaButton.setPreferredSize(new java.awt.Dimension(75, 50));
        SobremesaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SobremesaButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 1.0;
        PrincipalPanel.add(SobremesaButton, gridBagConstraints);

        AperetivoButton.setFont(new java.awt.Font("Segoe UI", 0, 9)); // NOI18N
        AperetivoButton.setText("Aperetivo");
        AperetivoButton.setPreferredSize(new java.awt.Dimension(75, 50));
        AperetivoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AperetivoButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 1.0;
        PrincipalPanel.add(AperetivoButton, gridBagConstraints);

        BebidaButton.setFont(new java.awt.Font("Segoe UI", 0, 9)); // NOI18N
        BebidaButton.setText("Bebida");
        BebidaButton.setPreferredSize(new java.awt.Dimension(75, 50));
        BebidaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BebidaButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 1.0;
        PrincipalPanel.add(BebidaButton, gridBagConstraints);

        SaladaButton.setFont(new java.awt.Font("Segoe UI", 0, 9)); // NOI18N
        SaladaButton.setText("Salada");
        SaladaButton.setPreferredSize(new java.awt.Dimension(75, 50));
        SaladaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaladaButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 1.0;
        PrincipalPanel.add(SaladaButton, gridBagConstraints);

        SalgadoButton.setFont(new java.awt.Font("Segoe UI", 0, 9)); // NOI18N
        SalgadoButton.setText("Salgado");
        SalgadoButton.setPreferredSize(new java.awt.Dimension(75, 50));
        SalgadoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SalgadoButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        PrincipalPanel.add(SalgadoButton, gridBagConstraints);

        DoceButton.setFont(new java.awt.Font("Segoe UI", 0, 9)); // NOI18N
        DoceButton.setText("Doce");
        DoceButton.setPreferredSize(new java.awt.Dimension(75, 50));
        DoceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DoceButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        PrincipalPanel.add(DoceButton, gridBagConstraints);

        AlmocoButton.setFont(new java.awt.Font("Segoe UI", 0, 9)); // NOI18N
        AlmocoButton.setText("Almoço");
        AlmocoButton.setToolTipText("");
        AlmocoButton.setPreferredSize(new java.awt.Dimension(75, 50));
        AlmocoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AlmocoButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        PrincipalPanel.add(AlmocoButton, gridBagConstraints);

        JantaButton.setFont(new java.awt.Font("Segoe UI", 0, 9)); // NOI18N
        JantaButton.setText("Janta");
        JantaButton.setPreferredSize(new java.awt.Dimension(75, 50));
        JantaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JantaButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        PrincipalPanel.add(JantaButton, gridBagConstraints);

        getContentPane().add(PrincipalPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 0, 400, 270));

        CriarMenu.setText("Criar");

        ReceitaItem.setText("Receita");
        ReceitaItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ReceitaItemActionPerformed(evt);
            }
        });
        CriarMenu.add(ReceitaItem);

        MenuBar.add(CriarMenu);

        EntrarMenu.setText("Entrar");

        LoginItem.setText("Login");
        LoginItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LoginItemActionPerformed(evt);
            }
        });
        EntrarMenu.add(LoginItem);

        CadastroItem.setText("Cadastro");
        CadastroItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CadastroItemActionPerformed(evt);
            }
        });
        EntrarMenu.add(CadastroItem);

        PerfilMenu.setText("Perfil");

        InformacoesMenuItem.setText("Informacoes");
        InformacoesMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InformacoesMenuItemActionPerformed(evt);
            }
        });
        PerfilMenu.add(InformacoesMenuItem);

        ReceitasMenuItem.setText("Receitas");
        ReceitasMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ReceitasMenuItemActionPerformed(evt);
            }
        });
        PerfilMenu.add(ReceitasMenuItem);

        EntrarMenu.add(PerfilMenu);

        MenuBar.add(EntrarMenu);

        setJMenuBar(MenuBar);

        pack();
        setLocationRelativeTo(null);
    }

    private void ReceitaItemActionPerformed(java.awt.event.ActionEvent evt) {
        ReceitaView receitaView = new ReceitaView(this.usuarioLogado);
        receitaView.setVisible(true);
        this.dispose();
    }

    private void CadastroItemActionPerformed(java.awt.event.ActionEvent evt) {
        TelaCadastroView loginView = new TelaCadastroView();
        loginView.setVisible(true);
        this.dispose();
    }

    private void LoginItemActionPerformed(java.awt.event.ActionEvent evt) {
        if (LoginItem.getText().equals("Login")) {
            TelaLoginView loginView = new TelaLoginView();
            loginView.setVisible(true);
            this.dispose();
        } else if (LoginItem.getText().equals("Sair (Logout)")) {
            controlarAcesso(false);
            javax.swing.JOptionPane.showMessageDialog(this, "Logout realizado com sucesso.");
        }
    }

    private void SobremesaButtonActionPerformed(java.awt.event.ActionEvent evt) {
        new CategoriaReceitaView(this.usuarioLogado, "Sobremesa").setVisible(true);
        this.dispose();
    }

    private void SalgadoButtonActionPerformed(java.awt.event.ActionEvent evt) {
        new CategoriaReceitaView(this.usuarioLogado, "Salgado").setVisible(true);
        this.dispose();
    }

    private void DoceButtonActionPerformed(java.awt.event.ActionEvent evt) {
        new CategoriaReceitaView(this.usuarioLogado, "Doce").setVisible(true);
        this.dispose();
    }

    private void AlmocoButtonActionPerformed(java.awt.event.ActionEvent evt) {
        new CategoriaReceitaView(this.usuarioLogado, "Almoço").setVisible(true);
        this.dispose();
    }

    private void JantaButtonActionPerformed(java.awt.event.ActionEvent evt) {
        new CategoriaReceitaView(this.usuarioLogado, "Janta").setVisible(true);
        this.dispose();
    }

    private void AperetivoButtonActionPerformed(java.awt.event.ActionEvent evt) {
        new CategoriaReceitaView(this.usuarioLogado, "Aperitivo").setVisible(true);
        this.dispose();
    }

    private void BebidaButtonActionPerformed(java.awt.event.ActionEvent evt) {
        new CategoriaReceitaView(this.usuarioLogado, "Bebida").setVisible(true);
        this.dispose();
    }

    private void SaladaButtonActionPerformed(java.awt.event.ActionEvent evt) {
        new CategoriaReceitaView(this.usuarioLogado, "Salada").setVisible(true);
        this.dispose();
    }
    
    private void InformacoesMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        TelaInformacoes telaInformacoes = new TelaInformacoes(this.usuarioLogado);
        telaInformacoes.setVisible(true);
        this.dispose();
    }

    private void ReceitasMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        ReceitaConta tela = new ReceitaConta(this.usuarioLogado);
        tela.setVisible(true);
        this.dispose();
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MenuView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MenuView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MenuView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MenuView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(() -> {
            new MenuView().setVisible(true);
        });
    }

    private javax.swing.JButton AlmocoButton;
    private javax.swing.JButton AperetivoButton;
    private javax.swing.JButton BebidaButton;
    private javax.swing.JMenuItem CadastroItem;
    private javax.swing.JMenu CriarMenu;
    private javax.swing.JButton DoceButton;
    private javax.swing.JMenu EntrarMenu;
    private javax.swing.JMenuItem InformacoesMenuItem;
    private javax.swing.JButton JantaButton;
    private javax.swing.JMenuItem LoginItem;
    private javax.swing.JMenuBar MenuBar;
    private javax.swing.JLabel NomeDoUsuarioLabel;
    private javax.swing.JMenu PerfilMenu;
    private javax.swing.JPanel PrincipalPanel;
    private javax.swing.JMenuItem ReceitaItem;
    private javax.swing.JMenuItem ReceitasMenuItem;
    private javax.swing.JButton SaladaButton;
    private javax.swing.JButton SalgadoButton;
    private javax.swing.JButton SobremesaButton;
    private javax.swing.JLabel TituloLabel;
}
