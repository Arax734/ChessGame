public class goniec extends figura {
    protected String kolor_pola; // wartosc okreslajaca na jakim kolorze pola porusza sie figura

    public goniec(String color, String kolor_pola) {
        super(color);
        this.kolor_pola = kolor_pola;
    }

    @Override
    public boolean symuluj_ruch(pole destination, pole[][] szachownica) {
        if (destination.getFigure() instanceof krol) {
            return false;
        }
        // Sprawdzamy czy cel jest zajmowany przez figurę naszego koloru
        if (destination.getFigure() != null && destination.getFigure().getColor() == this.getColor()) {
            return false;
        }
        // Sprawdzamy czy ruch jest ukośny
        int roznica_szerokosc = Math.abs(destination.getSzerokosc() - this.pole.getSzerokosc());
        int roznica_wysokosci = Math.abs(destination.getWysokosc() - this.pole.getWysokosc());
        if (roznica_szerokosc != roznica_wysokosci) {
            return false;
        }
        // Sprawdzamy czy występują kolizje z innymi pionkami
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
        return true;
    }

    public boolean symuluj_ruch_dla_krola(pole destination, pole[][] szachownica) {
        return false;
    }

    public void setWykonalRuch() {

    }

    public void setPassant(boolean statement){
        
    }
}
