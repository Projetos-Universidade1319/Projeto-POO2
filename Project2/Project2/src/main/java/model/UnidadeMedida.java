package model;

public enum UnidadeMedida {
    
    G("Grama"),
    KG("Quilograma"),
    ML("Mililitro"),
    L("Litro"),
    UNIDADE("Unidade"),
    PACOTE("Pacote"),
    LATA("Lata"),
    COLHER_CHA("Colher de Chá"),
    COLHER_SOPA("Colher de Sopa"),
    XICARA("Xícara");
    
    private final String descricao;

    UnidadeMedida(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
