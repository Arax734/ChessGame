public class goniec extends figura{
    String kolor_pola; //wartosc okreslajaca na jakim kolorze pola porusza sie figura
    public goniec(String color, String kolor_pola){
        super(color);
        this.kolor_pola = kolor_pola;
    }
    @Override
    public boolean symuluj_ruch(pole destination, pole[][] szachownica) {
        if (destination.getFigure() instanceof krol){
            return false;
        }
        // Check if the destination square is occupied by a piece of the same color
        if (destination.getFigure() != null && destination.getFigure().getColor() == this.getColor()) {
            return false;
        }
        // Check if the move is diagonal
        int roznica_szerokosc = Math.abs(destination.getSzerokosc() - this.pole.getSzerokosc());
        int roznica_wysokosci = Math.abs(destination.getWysokosc() - this.pole.getWysokosc());
        if (roznica_szerokosc != roznica_wysokosci) {
            return false;
        }
        // Check for collisions along the diagonal path
        int fileDirection = destination.getSzerokosc() > this.pole.getSzerokosc() ? 1 : -1;
        int rankDirection = destination.getWysokosc() > this.pole.getWysokosc() ? 1 : -1;
        int file = this.pole.getSzerokosc() + fileDirection;
        int rank = this.pole.getWysokosc() + rankDirection;
        while (file != destination.getSzerokosc() && rank != destination.getWysokosc()) {
            if (szachownica[rank][file].getFigure() != null) {
                return false;
            }
            file += fileDirection;
            rank += rankDirection;
        }
        // The move is valid, update the position of the bishop
        return true;
    }
    public boolean symuluj_ruch_dla_krola(pole destination, pole[][] szachownica){
        return false;
    }
    public void setWykonalRuch(){

    }
}
