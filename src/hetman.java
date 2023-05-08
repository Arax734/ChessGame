public class hetman extends figura {
    public hetman(String color) {
        super(color);
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
        int roznica_szerokosc = Math.abs(destination.getSzerokosc() - this.pole.getSzerokosc());
        int roznica_wysokosci = Math.abs(destination.getWysokosc() - this.pole.getWysokosc());
        if (roznica_szerokosc == roznica_wysokosci) {
            // Sprawdzamy kolizje dla ukośnego ruchu
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

    public void setWykonalRuch() {

    }
}
