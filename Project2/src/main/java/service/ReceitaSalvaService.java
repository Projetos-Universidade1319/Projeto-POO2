package service;

import dao.ReceitaSalvaDAO;
import model.ReceitaModel;
import java.sql.SQLException;
import java.util.List;

public class ReceitaSalvaService {
    
    private final ReceitaSalvaDAO dao;

    public ReceitaSalvaService() {
        this.dao = new ReceitaSalvaDAO();
    }

    /**
     * @return 
     */
    public boolean alternarSalvamento(int idUsuario, int idReceita) throws Exception {
        try {
            if (dao.estaSalva(idUsuario, idReceita)) {
                dao.remover(idUsuario, idReceita);
                System.out.println("✅ Receita removida dos favoritos.");
                return false;
            } else {
                dao.salvar(idUsuario, idReceita);
                System.out.println("✅ Receita salva nos favoritos!");
                return true;
            }
        } catch (SQLException e) {
            throw new Exception("Erro de persistência ao salvar/remover a receita.", e);
        }
    }
    
    public List<ReceitaModel> listarSalvas(int idUsuario) throws Exception {
         try {
            return dao.buscarReceitasSalvas(idUsuario);
        } catch (SQLException e) {
            throw new Exception("Erro ao listar receitas salvas.", e);
        }
    }
}
