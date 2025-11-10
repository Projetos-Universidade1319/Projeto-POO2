package controller;

import model.UsuarioModel;
import model.NivelConta; 
import service.UsuarioService;
import java.util.InputMismatchException;

public class UsuarioController {
    
    private final UsuarioService usuarioService; 

    public UsuarioController() {
        this.usuarioService = new UsuarioService();
    }
    
    /**
     * @param nome 
     * @param email 
     * @param senha 
     * @return 
     */
    public String registrarNovoUsuario(String nome, String email, String senha) {
        
        if (nome == null || nome.trim().length() < 3 || senha == null || senha.length() < 6 || !email.contains("@")) {
            return "ERRO: Dados de cadastro inválidos. Verifique nome (min 3), email e senha (min 6).";
        }
        
        try {

            UsuarioModel novoUsuario = new UsuarioModel();
            novoUsuario.setNome(nome);
            novoUsuario.setEmail(email);
            novoUsuario.setSenha(senha); 
            novoUsuario.setNivelConta(NivelConta.PADRAO); 
            novoUsuario.setPontuacao(0); 
            
            boolean sucesso = usuarioService.cadastrar(novoUsuario);

            if (sucesso) {
                return "SUCESSO: Usuário " + nome + " cadastrado.";
            } else {
                return "FALHA: O sistema não conseguiu salvar seu cadastro.";
            }

        } catch (IllegalArgumentException e) {
             return "ERRO DE NEGÓCIO: " + e.getMessage();
        } catch (Exception e) {
            System.err.println("Erro interno ao registrar usuário: " + e.getMessage());
            return "ERRO INTERNO: Falha no sistema. Tente novamente.";
        }
    }
    
    /**
     * @param email
     * @param senha 
     * @return
     */
    public UsuarioModel autenticarUsuario(String email, String senha) {
        if (email == null || senha == null) {
            System.out.println("Login falhou: Email e senha são obrigatórios.");
            return null;
        }
        
        try {
            UsuarioModel usuarioLogado = usuarioService.login(email, senha);
            
            if (usuarioLogado == null) {
                System.out.println("Login falhou: Credenciais inválidas.");
            }
            return usuarioLogado;
            
        } catch (Exception e) {
            System.err.println("Erro durante a autenticação: " + e.getMessage());
            return null;
        }
    }

    /**
     * @param idUsuario 
     * @return
     */
    public String deletarConta(int idUsuario) {
        
        if (idUsuario <= 0) {
            return "ERRO: ID de usuário inválido.";
        }

        try {
            boolean sucesso = usuarioService.removerUsuario(idUsuario);
            
            if (sucesso) {
                return "SUCESSO: Conta deletada permanentemente.";
            } else {
                return "FALHA: Não foi possível deletar a conta (Usuário não encontrado).";
            }
            
        } catch (SecurityException e) {
            System.err.println("Tentativa de exclusão sem permissão: " + e.getMessage());
            return "ERRO: Permissão negada para esta operação.";
        } catch (Exception e) {
            System.err.println("Erro ao deletar conta (Controller): " + e.getMessage());
            return "ERRO INTERNO: Falha no sistema ao processar a exclusão.";
        }
    }
}
