package model;

public class VerificaLogin implements VerificaPadrao {

    /**
     * @param caso
     * @return 
     */
    @Override 
    public boolean casoVerifica(String caso) {
        
        if (caso == null || caso.trim().isEmpty()) {
            return false;
        }

        if (!caso.contains("@") || !caso.contains(".")) {
            return false;
        }
        return true; 
    }
}
