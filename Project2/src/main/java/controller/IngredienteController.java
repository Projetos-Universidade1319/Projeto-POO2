package controller;

import model.IngredienteModel;
import model.UnidadeMedida;
import service.IngredienteService; 
import java.util.List;
import java.util.Scanner;

public class IngredienteController {
    
    private final IngredienteService ingredienteService;

    public IngredienteController() {
        this.ingredienteService = new IngredienteService();
    }

    public List<IngredienteModel> buscarTodosIngredientes() throws Exception {
        return ingredienteService.buscarTodos();
    }
    
    public IngredienteModel buscarOuCriarIngrediente(String nome, Scanner scanner) throws Exception {
        
        IngredienteModel existente = ingredienteService.buscarPorNome(nome);

        if (existente != null) {
            System.out.println("-> Ingrediente encontrado na base: " + nome);
            return existente;
        }

        System.out.println("⚠️ Ingrediente '" + nome + "' não encontrado. Iniciando cadastro...");
        
        System.out.println("Selecione a unidade de medida para '" + nome + "':");
        UnidadeMedida[] unidades = UnidadeMedida.values();
        for (int i = 0; i < unidades.length; i++) {
            System.out.println((i + 1) + ". " + unidades[i].name());
        }
        System.out.print("Escolha o número da unidade: ");

        int opcaoUnidade;
        if (scanner.hasNextInt()) {
            opcaoUnidade = scanner.nextInt();
            scanner.nextLine();
        } else {
            scanner.nextLine();
            System.out.println("❌ Opção inválida. Cadastro cancelado.");
            return null;
        }

        UnidadeMedida unidadeSelecionada = null;
        if (opcaoUnidade > 0 && opcaoUnidade <= unidades.length) {
            unidadeSelecionada = unidades[opcaoUnidade - 1]; 
        } else {
             System.out.println("❌ Número fora do intervalo. Cadastro cancelado.");
             return null;
        }
        
        IngredienteModel novoIngrediente = new IngredienteModel();
        novoIngrediente.setNome(nome);
        novoIngrediente.setUnidade_medida(unidadeSelecionada.name());
        
        boolean sucesso = ingredienteService.salvarNovo(novoIngrediente); 
        
        if (sucesso) {
            System.out.println("✅ Ingrediente '" + nome + "' cadastrado com sucesso! Unidade: " + unidadeSelecionada.name());
            return novoIngrediente;
        } else {
            System.out.println("❌ Falha ao salvar novo ingrediente no banco de dados.");
            return null;
        }
    }
    
    public String registrarNovoIngrediente(String nome, String unidadeStr) {
        if (nome == null || nome.trim().isEmpty() || unidadeStr == null || unidadeStr.trim().isEmpty()) {
            return "ERRO: Nome e unidade são obrigatórios.";
        }

        try {
            UnidadeMedida unidadeSelecionada = UnidadeMedida.valueOf(unidadeStr.toUpperCase().trim());

            IngredienteModel novoIngrediente = new IngredienteModel();
            novoIngrediente.setNome(nome);
            novoIngrediente.setUnidade_medida(unidadeSelecionada.name());
            
            boolean sucesso = ingredienteService.salvarNovo(novoIngrediente);

            if (sucesso) {
                return "SUCESSO: Ingrediente '" + nome + "' registrado.";
            } else {
                return "FALHA: Não foi possível registrar o ingrediente (Erro DAO).";
            }
        } catch (IllegalArgumentException e) {
            return "ERRO: Unidade de medida inválida. Verifique o Enum UnidadeMedida.";
        } catch (Exception e) {
            System.err.println("Erro interno ao registrar ingrediente: " + e.getMessage());
            return "ERRO INTERNO: Falha no sistema.";
        }
    }
}