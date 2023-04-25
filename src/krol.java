public class krol extends figura{
    boolean wykonal_ruch;
    public krol(String color, boolean wykonal_ruch){
        super(color);
        this.wykonal_ruch = wykonal_ruch;
    }
    @Override
    public boolean symuluj_ruch(pole destination, pole[][] szachownica) {
        // funkcja przyjmuje pole, na ktorym sprawdzimy czy mozemy wykonac ruch
        if (destination.getFigure() instanceof krol){
            return false;
        }
        if (destination.getFigure() != null) {
            if (destination.getFigure().getColor() == this.getColor()) {
                return false;
            }
        }
        int roznica_szerokosc = Math.abs(destination.getSzerokosc() - this.pole.getSzerokosc());
        int roznica_wysokosci = Math.abs(destination.getWysokosc() - this.pole.getWysokosc());
        if (roznica_szerokosc <= 1 && roznica_wysokosci <= 1) {
            // update the king's position
    
            // the move is valid
            return true;
        }
        return false;
    }
    public boolean symuluj_ruch_dla_krola(pole destination, pole[][] szachownica){
        return false;
    }
    @Override
    public void setWykonalRuch(){
        this.wykonal_ruch = true;
    }
}
