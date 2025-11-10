package service;

import dao.IngredienteDAO;
import model.IngredienteModel;
import java.sql.SQLException;
import java.util.List;

public class IngredienteService {
    
    private final IngredienteDAO ingredienteDAO;
    
    public IngredienteService() {
        this.ingredienteDAO = new IngredienteDAO(); 
    }

    /**
     * * @param novoIngrediente
     * @return 
     * @throws Exception 
     */
    public boolean salvarIngrediente(IngredienteModel novoIngrediente) throws Exception {
        
        if (novoIngrediente.getNome() == null || novoIngrediente.getNome().trim().length() < 2) {
            throw new IllegalArgumentException("O nome do ingrediente é muito curto.");
        }
        
        try {
            IngredienteModel existente = ingredienteDAO.buscarPorNome(novoIngrediente.getNome());
            
            if (existente != null) {
                System.out.println("Ingrediente '" + novoIngrediente.getNome() + "' já cadastrado. Não é necessária nova inserção.");
                novoIngrediente.setId_ingrediente(existente.getId_ingrediente());
                return true; 
            }
            return ingredienteDAO.salvarNovoIngrediente(novoIngrediente);

        } catch (SQLException e) {
            System.err.println("Erro no Service ao processar ingrediente: " + e.getMessage());
            throw new Exception("Falha ao salvar o ingrediente no sistema.", e); 
        }
    }
    
    /**
     * @return 
     * @throws SQLException
     */
    public List<IngredienteModel> buscarTodos() throws SQLException {
        return ingredienteDAO.buscarTodosIngredientes();
    }
}
