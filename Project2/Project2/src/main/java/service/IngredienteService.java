package service;

import model.IngredienteModel;
import dao.IngredienteDAO;
import dao.ConnectionFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class IngredienteService {
    
    private final IngredienteDAO ingredienteDAO;

    public IngredienteService() {
        this.ingredienteDAO = new IngredienteDAO(); 
    }

    public IngredienteModel buscarPorNome(String nome) throws Exception {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do ingrediente não pode ser vazio.");
        }
        try {
            return ingredienteDAO.buscarPorNome(nome);
        } catch (SQLException e) {
            throw new Exception("Erro ao buscar ingrediente no BD.", e);
        }
    }

    public List<IngredienteModel> buscarTodos() throws Exception {
        try {
            return ingredienteDAO.buscarTodos();
        } catch (SQLException e) {
            throw new Exception("Erro ao buscar todos os ingredientes no BD.", e);
        }
    }

    public boolean salvarNovo(IngredienteModel novoIngrediente) throws Exception {
        if (novoIngrediente.getNome() == null || novoIngrediente.getUnidade_medida() == null) {
            throw new IllegalArgumentException("Dados do ingrediente incompletos.");
        }
        
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(true);
            return ingredienteDAO.salvarNovoIngrediente(novoIngrediente, conn);
        } catch (SQLException e) {
            System.err.println("Erro SQL ao salvar novo ingrediente: " + e.getMessage());
            throw new Exception("Falha ao salvar o ingrediente no banco de dados.", e);
        } finally {
            if (conn != null) conn.close();
        }
    }
}