public class wieza extends figura {
    boolean wykonal_ruch;

    public wieza(String color, boolean wykonal_ruch) {
        super(color);
        this.wykonal_ruch = wykonal_ruch;
    }

    @Override
    public boolean symuluj_ruch(pole destination, pole[][] szachownica) {
        if (destination.getFigure() instanceof krol) {
            return false;
        }
        if (destination.getFigure() != null) {
            if (destination.getFigure().getColor() == this.getColor()) {
                return false;
            }
        }
        if (destination.getSzerokosc() == this.getPole().getSzerokosc()) {
            // Sprawdzamy kolizje dla ruchu pionowego
            int startRank = Math.min(destination.getWysokosc(), this.getPole().getWysokosc()) + 1;
            int endRank = Math.max(destination.getWysokosc(), this.getPole().getWysokosc()) - 1;
            for (int rank = startRank; rank <= endRank; rank++) {
                if (szachownica[rank][destination.getSzerokosc()].getFigure() != null) {
                    return false;
                }
            }
            return true;
        } else if (destination.getWysokosc() == this.getPole().getWysokosc()) {
            // Sprawdzamy kolizje dla ruchu poziomego
            int startFile = Math.min(destination.getSzerokosc(), this.getPole().getSzerokosc()) + 1;
            int endFile = Math.max(destination.getSzerokosc(), this.getPole().getSzerokosc()) - 1;
            for (int file = startFile; file <= endFile; file++) {
                if (szachownica[destination.getWysokosc()][file].getFigure() != null) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public boolean symuluj_ruch_dla_krola(pole destination, pole[][] szachownica) {
        return false;
    }

    @Override
    public void setWykonalRuch() {
        this.wykonal_ruch = true;
    }
}
