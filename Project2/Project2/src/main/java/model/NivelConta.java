package model;

public enum NivelConta {
    PADRAO("Padrão", 0),
    CHEF_AMADOR("Chef Amador", 100),
    CHEF_DE_COZINHA("Chef de Cozinha", 500),
    MESTRE_CUCA("Mestre Cuca", 1500); 

    private final String nomeNivel;
    private final int pontuacaoMinima;

    NivelConta(String nomeNivel, int pontuacaoMinima) {
        this.nomeNivel = nomeNivel;
        this.pontuacaoMinima = pontuacaoMinima;
    }

    public String getNomeNivel() {
        return nomeNivel;
    }

    public int getPontuacaoMinima() {
        return pontuacaoMinima;
    }
}
