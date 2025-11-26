package Teste;

import controller.ReceitaController;
import controller.UsuarioController;
import controller.IngredienteController;
import controller.AvaliacaoController;
import controller.ReceitaSalvaController;
import model.ReceitaModel;
import model.UsuarioModel;
import model.IngredienteModel;
import model.ReceitaIngredienteModel;
import model.AvaliacaoModel;
import java.util.Scanner;
import java.util.List;
import java.time.LocalDateTime;

public class AppMain {

    private static final UsuarioController usuarioController = new UsuarioController();
    private static final ReceitaController receitaController = new ReceitaController();
    private static final IngredienteController ingredienteController = new IngredienteController();
    private static final AvaliacaoController avaliacaoController = new AvaliacaoController();
    private static final ReceitaSalvaController receitaSalvaController = new ReceitaSalvaController();
    
    private static UsuarioModel usuarioLogado = null;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("====================================================");
        System.out.println("           Bem-vindo ao Chef Console App          ");
        System.out.println("====================================================");
        menuPrincipal();
    }

    // =========================================================================
    // MENU PRINCIPAL (Pré-Login)
    // =========================================================================

    private static void menuPrincipal() {
        int opcao;
        do {
            System.out.println("\n[MENU]");
            System.out.println("1. Cadastrar Novo Usuário");
            System.out.println("2. Fazer Login");
            System.out.println("3. Sair");
            System.out.print("Escolha uma opção: ");

            if (scanner.hasNextInt()) {
                opcao = scanner.nextInt();
                scanner.nextLine(); 
            } else {
                System.out.println("Opção inválida. Digite um número.");
                scanner.nextLine(); 
                opcao = 0;
            }

            try {
                switch (opcao) {
                    case 1: handleCadastro(); break;
                    case 2: handleLogin(); break;
                    case 3: System.out.println("Obrigado por usar o Chef.g! Até logo."); break;
                    default: 
                        if (opcao != 3) System.out.println("Opção não reconhecida.");
                        break;
                }
            } catch (Exception e) {
                System.err.println("Erro inesperado: " + e.getMessage());
            }

        } while (opcao != 3 && usuarioLogado == null);
        
        if (usuarioLogado != null) {
            menuLogado();
        }
    }

    private static void handleCadastro() {
        System.out.println("\n--- CADASTRO ---");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Senha (min 6 caracteres): ");
        String senha = scanner.nextLine();
        
        String resultado = usuarioController.registrarNovoUsuario(nome, email, senha);
        System.out.println(resultado);
    }

    private static void handleLogin() {
        System.out.println("\n--- LOGIN ---");
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();
        
        try {
            usuarioLogado = usuarioController.autenticarUsuario(email, senha);
            if (usuarioLogado != null) {
                System.out.println("\n🎉 Login bem-sucedido! Bem-vindo, " + usuarioLogado.getNome() + ".");
            } else {
                System.out.println("\n❌ Falha no login. Verifique email e senha.");
            }
        } catch (Exception e) {
            System.err.println("Erro durante o login: " + e.getMessage());
        }
    }

    // =========================================================================
    // MENU LOGADO (Pós-Login)
    // =========================================================================

    private static void menuLogado() {
        int opcao;
        do {
            System.out.println("\n[MENU DE AUTOR]");
            System.out.println("Usuário: " + usuarioLogado.getNome() + " | Nível: " + usuarioLogado.getNivel());
            System.out.println("1. Publicar Nova Receita");
            System.out.println("2. Buscar e Ver Receitas (Ranqueamento)");
            System.out.println("3. Curtir ou Avaliar Receita");
            System.out.println("4. Editar/Excluir Minhas Receitas");
            System.out.println("5. Salvar/Desalvar Receita"); 
            System.out.println("6. Ver Minhas Receitas Salvas"); 
            System.out.println("7. Fazer Logout");
            System.out.print("Escolha uma opção: ");

            if (scanner.hasNextInt()) {
                opcao = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println("Opção inválida. Digite um número.");
                scanner.nextLine();
                opcao = 0;
            }

            try {
                switch (opcao) {
                    case 1: handlePublicarReceita(); break;
                    case 2: handleBuscarReceitas(); break;
                    case 3: handleCurtirAvaliar(); break;
                    case 4: handleEditarExcluirReceita(); break;
                    case 5: handleAlternarSalvamentoReceita(); break;
                    case 6: handleListarReceitasSalvas(); break;
                    case 7: usuarioLogado = null; System.out.println("Logout realizado."); break;
                    default: 
                        if (opcao != 7) System.out.println("Opção não reconhecida.");
                        break;
                }
            } catch (Exception e) {
                System.err.println("Erro inesperado na operação: " + e.getMessage());
            }

        } while (opcao != 7 && usuarioLogado != null);
        
        if (usuarioLogado == null) {
            menuPrincipal(); 
        }
    }

    // =========================================================================
    // MÉTODOS DE FUNCIONALIDADE
    // =========================================================================

    private static void handlePublicarReceita() {
        System.out.println("\n--- PUBLICAR RECEITA ---");
        
        ReceitaModel novaReceita = new ReceitaModel();
        novaReceita.setIdUsuario(usuarioLogado.getIdUsuario());
        novaReceita.setDataCriacao(LocalDateTime.now());
        novaReceita.setCurtidas(0);
        novaReceita.setListaIngredientes(new java.util.ArrayList<>()); 

        System.out.print("Nome da Receita: ");
        novaReceita.setNome(scanner.nextLine());
        
        System.out.print("Descrição Curta: ");
        novaReceita.setDescricao(scanner.nextLine());
        
        System.out.print("Modo de Preparo (detalhado, min 10 caracteres): ");
        novaReceita.setModoPreparo(scanner.nextLine());
        
        System.out.print("Categoria (Ex: PRINCIPAL, SOBREMESA, BEBIDA): ");
        novaReceita.setCategoria(scanner.nextLine().toUpperCase());
        
        try {
            coletarIngredientes(novaReceita); 
            
            if (novaReceita.getListaIngredientes().isEmpty()) {
                 System.out.println("❌ Publicação cancelada: Receita deve ter pelo menos um ingrediente.");
                 return;
            }
            
            boolean sucesso = receitaController.publicarNovaReceita(novaReceita, usuarioLogado);
            if (sucesso) {
                usuarioController.verificarNivelUsuario(usuarioLogado); 
            }

        } catch (Exception e) {
            System.err.println("Falha ao processar ingredientes/publicação: " + e.getMessage());
        }
    }

    private static void coletarIngredientes(ReceitaModel receita) throws Exception { 
        System.out.println("\n--- INGREDIENTES ---");
        System.out.println("Digite o nome e a quantidade de cada ingrediente. Digite 'fim' para terminar.");
        
        String nomeIngrediente = ""; 
        
        do {
            System.out.print("Nome do Ingrediente: ");
            nomeIngrediente = scanner.nextLine().trim();

            if (nomeIngrediente.equalsIgnoreCase("fim") || nomeIngrediente.isEmpty()) {
                break; 
            }
            
            IngredienteModel ingredienteDB = ingredienteController.buscarOuCriarIngrediente(nomeIngrediente, scanner);
            
            if (ingredienteDB == null) {
                System.out.println("❌ Não foi possível encontrar/cadastrar o ingrediente. Tente outro nome.");
                continue;
            }

            System.out.print("Quantidade (" + ingredienteDB.getUnidade_medida() + "): ");
            
            if (scanner.hasNextInt()) {
                int quantidade = scanner.nextInt();
                scanner.nextLine();
                
                ReceitaIngredienteModel item = new ReceitaIngredienteModel();
                item.setId_ingrediente(ingredienteDB.getId_ingrediente());
                item.setQuantidade(quantidade);
                
                receita.getListaIngredientes().add(item);
                System.out.println("-> Adicionado: " + ingredienteDB.getNome());
            } else {
                System.out.println("❌ Entrada de quantidade inválida. Tente novamente.");
                scanner.nextLine(); 
            }

        } while (true); 
    }

    private static void handleBuscarReceitas() {
        System.out.println("\n--- BUSCAR RECEITAS ---");
        System.out.print("Termo de Busca (Ex: bolo, frango, deixe em branco para ver todas): ");
        String termo = scanner.nextLine();
        System.out.print("Categoria (Ex: SOBREMESA, PRATOPRINCIPAL, deixe em branco): ");
        String categoria = scanner.nextLine();

        List<ReceitaModel> resultados = receitaController.buscarEClassificar(termo, categoria, usuarioLogado);
            
        if (resultados.isEmpty()) {
            System.out.println("\nNenhuma receita encontrada com esses critérios.");
            return;
        }

        System.out.println("\n=== RESULTADOS RANQUEADOS (Melhores no topo) ===");
        for (int i = 0; i < resultados.size(); i++) {
            ReceitaModel r = resultados.get(i);
            System.out.printf("[%d] %s (ID: %d) | Curtidas: %d | Categoria/Nível: %s\n", 
                              i + 1, r.getNome(), r.getIdReceita(), r.getCurtidas(), r.getCategoria());
            System.out.printf("    Descrição: %s\n", r.getDescricao());
        }
    }

    private static void handleCurtirAvaliar() {
        System.out.println("\n--- CURTIR / AVALIAR ---");
        System.out.print("Digite o ID da Receita que deseja interagir: ");
        if (!scanner.hasNextInt()) {
            System.out.println("ID inválido.");
            scanner.nextLine();
            return;
        }
        int idReceita = scanner.nextInt();
        scanner.nextLine();
        
        System.out.println("Opções: (C)urtir ou (A)valiar:");
        String opcao = scanner.nextLine().trim().toUpperCase();

        try {
            if (opcao.equals("C")) {
                receitaController.curtirReceita(idReceita, usuarioLogado.getIdUsuario());
                // Chamada correta
                usuarioController.verificarNivelUsuario(usuarioLogado);
                
            } else if (opcao.equals("A")) {
                System.out.print("Seu comentário (min 5 caracteres): ");
                String comentario = scanner.nextLine();
                
                AvaliacaoModel avaliacao = new AvaliacaoModel();
                avaliacao.setId_receita(idReceita);
                avaliacao.setId_usuario(usuarioLogado.getIdUsuario());
                avaliacao.setNome(usuarioLogado.getNome());
                avaliacao.setComentario(comentario);
                avaliacao.setData_avaliacao(LocalDateTime.now());
                
                boolean sucesso = avaliacaoController.processarAvaliacao(avaliacao);
                if (sucesso) {
                    System.out.println("✅ Avaliação processada com sucesso.");
                    // Chamada correta
                    usuarioController.verificarNivelUsuario(usuarioLogado);
                } else {
                    System.out.println("❌ Falha ao processar avaliação.");
                }
            } else {
                System.out.println("Opção não reconhecida.");
            }
        } catch (Exception e) {
            System.err.println("Falha na interação: " + e.getMessage());
        }
    }
    
    private static void handleEditarExcluirReceita() {
        System.out.println("\n--- EDITAR / EXCLUIR RECEITAS ---");
        System.out.print("Digite o ID da Receita que deseja gerenciar: ");
        if (!scanner.hasNextInt()) {
            System.out.println("ID inválido.");
            scanner.nextLine();
            return;
        }
        int idReceita = scanner.nextInt();
        scanner.nextLine();
        
        System.out.println("Opções: (E)ditar ou (X)excluir:");
        String opcao = scanner.nextLine().trim().toUpperCase();

        try {
            if (opcao.equals("X")) {
                System.out.print("⚠️ CONFIRME a exclusão da Receita ID " + idReceita + " (S/N): ");
                if (scanner.nextLine().trim().toUpperCase().equals("S")) {
                    String resultado = receitaController.excluirReceita(idReceita, usuarioLogado.getIdUsuario()); 
                    System.out.println(resultado);
                } else {
                    System.out.println("Exclusão cancelada.");
                }
            } else if (opcao.equals("E")) {
                System.out.println("\n--- EDIÇÃO DA RECEITA ID: " + idReceita + " ---");
                
                ReceitaModel receitaEditada = new ReceitaModel();
                receitaEditada.setIdReceita(idReceita);
                receitaEditada.setIdUsuario(usuarioLogado.getIdUsuario()); 
                receitaEditada.setListaIngredientes(new java.util.ArrayList<>()); 
                
                System.out.print("Novo Nome da Receita: ");
                receitaEditada.setNome(scanner.nextLine());
                
                System.out.print("Nova Descrição Curta: ");
                receitaEditada.setDescricao(scanner.nextLine());
                
                System.out.print("Novo Modo de Preparo: ");
                receitaEditada.setModoPreparo(scanner.nextLine());
                
                System.out.print("Nova Categoria: ");
                receitaEditada.setCategoria(scanner.nextLine().toUpperCase());
                
                coletarIngredientes(receitaEditada); 
                
                boolean sucesso = receitaController.editarReceita(receitaEditada, usuarioLogado.getIdUsuario());
                System.out.println(sucesso ? "✅ Edição salva com sucesso!" : "❌ Falha na edição. Verifique permissão.");
                
            } else {
                System.out.println("Opção não reconhecida.");
            }
        } catch (Exception e) {
             System.err.println("Falha no gerenciamento da receita: " + e.getMessage());
        }
    }

    // =========================================================================
    // NOVOS MÉTODOS DE RECEITA SALVA
    // =========================================================================
    
    private static void handleAlternarSalvamentoReceita() {
        System.out.println("\n--- SALVAR / DESALVAR RECEITA ---");
        System.out.print("Digite o ID da Receita: ");
        if (!scanner.hasNextInt()) {
            System.out.println("ID inválido.");
            scanner.nextLine();
            return;
        }
        int idReceita = scanner.nextInt();
        scanner.nextLine();
        
        try {
            receitaSalvaController.alternarSalvamento(idReceita, usuarioLogado.getIdUsuario());
        } catch (Exception e) {
            System.err.println("Falha ao salvar/desalvar receita: " + e.getMessage());
        }
    }
    
    private static void handleListarReceitasSalvas() {
        System.out.println("\n--- MINHAS RECEITAS SALVAS ---");
        
        try {
            List<ReceitaModel> salvas = receitaSalvaController.listarSalvas(usuarioLogado.getIdUsuario());
            
            if (salvas.isEmpty()) {
                System.out.println("Você ainda não salvou nenhuma receita.");
                return;
            }

            System.out.println("\n=== RECEITAS FAVORITAS ===");
            for (ReceitaModel r : salvas) {
                System.out.printf("[ID: %d] %s\n", r.getIdReceita(), r.getNome());
                System.out.printf("    Descrição: %s\n", r.getDescricao());
            }
        } catch (Exception e) {
            System.err.println("Falha ao listar receitas salvas: " + e.getMessage());
        }
    }
}