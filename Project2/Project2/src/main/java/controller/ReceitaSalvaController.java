package controller;

import model.ReceitaModel;
import service.ReceitaSalvaService;
import java.util.List;

public class ReceitaSalvaController {
    
    private final ReceitaSalvaService service;

    public ReceitaSalvaController() {
        this.service = new ReceitaSalvaService();
    }

    public void alternarSalvamento(int idReceita, int idUsuario) throws Exception {
        service.alternarSalvamento(idUsuario, idReceita);
    }
    
    public List<ReceitaModel> listarSalvas(int idUsuario) throws Exception {
        return service.listarSalvas(idUsuario);
    }
}
