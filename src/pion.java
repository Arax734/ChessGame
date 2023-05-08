public class pion extends figura {
    boolean wykonal_ruch;

    public pion(String color, boolean wykonal_ruch) {
        super(color);
        this.wykonal_ruch = wykonal_ruch;
    }

    // Pomocnicza metoda, którą wykorzystujemy przy sprawdzaniu czy król może ruszyć się na odpowiednie pole
    @Override
    public boolean symuluj_ruch_dla_krola(pole destination, pole[][] szachownica) {
        // sytuacja dla białych
        if (destination.getFigure() instanceof krol) {
            return false;
        }
        if (destination.getFigure() != null) {
            if (destination.getFigure().getColor() == this.getColor()) {
                return false;
            }
        }
        if (this.getColor() == "bialy") {
            if (destination.getWysokosc() == this.getPole().getWysokosc() - 1
                    && (destination.getSzerokosc() == this.getPole().getSzerokosc() - 1
                            || destination.getSzerokosc() == this.getPole().getSzerokosc() + 1)) {

                return true;

            }

        }
        // sytuacja dla czarnych
        if (this.getColor() == "czarny") {
            if (destination.getWysokosc() == this.getPole().getWysokosc() + 1
                    && (destination.getSzerokosc() == this.getPole().getSzerokosc() - 1
                            || destination.getSzerokosc() == this.getPole().getSzerokosc() + 1)) {

                return true;

            }
        }
        return false;
    }

    @Override
    public boolean symuluj_ruch(pole destination, pole[][] szachownica) {
        // sytuacja dla białych
        if (destination.getFigure() instanceof krol) {
            return false;
        }
        if (destination.getFigure() != null) {
            if (destination.getFigure().getColor() == this.getColor()) {
                return false;
            }
        }
        if (this.getColor() == "bialy") {
            if (this.wykonal_ruch == false) {
                if (destination.getWysokosc() == this.getPole().getWysokosc() - 2
                        && destination.getSzerokosc() == this.getPole().getSzerokosc()) {
                    if (destination.getFigure() == null) {
                        if (szachownica[destination.getWysokosc() + 1][destination.getSzerokosc()]
                                .getFigure() != null) {
                            return false;
                        }
                        return true;
                    }
                }
            }
            if (destination.getWysokosc() == this.getPole().getWysokosc() - 1
                    && destination.getSzerokosc() == this.getPole().getSzerokosc()) {
                if (destination.getFigure() == null) {
                    return true;
                }
            }
            if (destination.getWysokosc() == this.getPole().getWysokosc() - 1
                    && (destination.getSzerokosc() == this.getPole().getSzerokosc() - 1
                            || destination.getSzerokosc() == this.getPole().getSzerokosc() + 1)) {
                if (destination.getFigure() != null) {
                    return true;
                }
            }
            if(destination.getWysokosc() == this.getPole().getWysokosc() - 1
            && (destination.getSzerokosc() == this.getPole().getSzerokosc() - 1
                    || destination.getSzerokosc() == this.getPole().getSzerokosc() + 1)){
                        if(destination.getFigure() == null){
                            if(szachownica[destination.getWysokosc()+1][destination.getSzerokosc()].getFigure() != null){
                                figura passant = szachownica[destination.getWysokosc()+1][destination.getSzerokosc()].getFigure();
                                if(passant.getColor() != this.getColor()){
                                    if(passant instanceof pion){
                                        if(passant.getPoprzedniePole() == null){
                                            passant.setPoprzedniePole(passant.getPole());
                                        }
                                        int roznica = Math.abs(passant.getPole().getWysokosc()-passant.getPoprzedniePole().getWysokosc());
                                        if(roznica == 2 && passant.getPassant() == true){
                                            return true;
                                        }
                                    }     
                                }
                            }
                        }
                    } 
        }
        // sytuacja dla czarnych
        if (this.getColor() == "czarny") {
            if (this.wykonal_ruch == false) {
                if (destination.getWysokosc() == this.getPole().getWysokosc() + 2
                        && destination.getSzerokosc() == this.getPole().getSzerokosc()) {
                    if (destination.getFigure() == null) {
                        if (szachownica[destination.getWysokosc() - 1][destination.getSzerokosc()]
                                .getFigure() != null) {
                            return false;
                        }
                        return true;
                    }
                }
            }
            if (destination.getWysokosc() == this.getPole().getWysokosc() + 1
                    && destination.getSzerokosc() == this.getPole().getSzerokosc()) {
                if (destination.getFigure() == null) {
                    return true;
                }
            }
            if (destination.getWysokosc() == this.getPole().getWysokosc() + 1
                    && (destination.getSzerokosc() == this.getPole().getSzerokosc() - 1
                            || destination.getSzerokosc() == this.getPole().getSzerokosc() + 1)) {
                if (destination.getFigure() != null) {
                    return true;
                }
            }
            if(destination.getWysokosc() == this.getPole().getWysokosc() + 1
            && (destination.getSzerokosc() == this.getPole().getSzerokosc() - 1
                    || destination.getSzerokosc() == this.getPole().getSzerokosc() + 1)){
                        if(destination.getFigure() == null){
                            if(szachownica[destination.getWysokosc()-1][destination.getSzerokosc()].getFigure() != null){
                                figura passant = szachownica[destination.getWysokosc()-1][destination.getSzerokosc()].getFigure();
                                if(passant.getColor() != this.getColor()){
                                    if(passant instanceof pion){
                                        if(passant.getPoprzedniePole() == null){
                                            passant.setPoprzedniePole(passant.getPole());
                                        }
                                        int roznica = Math.abs(passant.getPole().getWysokosc()-passant.getPoprzedniePole().getWysokosc());
                                        if(roznica == 2 && passant.getPassant() == true){
                                            return true;
                                        }
                                    }     
                                }
                            }
                        }
                    }
        }
        return false;
    }

    @Override
    public void setWykonalRuch() {
        this.wykonal_ruch = true;
    }
}
