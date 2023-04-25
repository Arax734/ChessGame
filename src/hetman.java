public class hetman extends figura {
    public hetman(String color) {
        super(color);
    }

    @Override
    public boolean symuluj_ruch(pole destination, pole[][] szachownica) {
        // funkcja przyjmuje pole, na ktorym sprawdzimy czy mozemy wykonac ruch
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
        if (destination.getSzerokosc() == this.getPole().getSzerokosc()) {
            // Check for collisions along the file
            int startRank = Math.min(destination.getWysokosc(), this.getPole().getWysokosc()) + 1;
            int endRank = Math.max(destination.getWysokosc(), this.getPole().getWysokosc()) - 1;
            for (int rank = startRank; rank <= endRank; rank++) {
                if (szachownica[rank][destination.getSzerokosc()].getFigure() != null) {
                    return false;
                }
            }
            return true;
        } else if (destination.getWysokosc() == this.getPole().getWysokosc()) {
            // Check for collisions along the rank
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
    public boolean symuluj_ruch_dla_krola(pole destination, pole[][] szachownica){
        return false;
    }
    public void setWykonalRuch(){
        
    }
}
