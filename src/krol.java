public class krol extends figura {
    private boolean wykonal_ruch;

    public krol(String color, boolean wykonal_ruch) {
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
        int roznica_szerokosc = Math.abs(destination.getSzerokosc() - this.pole.getSzerokosc());
        int roznica_wysokosci = Math.abs(destination.getWysokosc() - this.pole.getWysokosc());
        
        // Sprawdzamy warunek roszady
        if (roznica_szerokosc == 2 && roznica_wysokosci == 0) {
            // Sprawdzamy czy wieza znajduje się w odpowiedniej pozycji
            if (destination.getSzerokosc() > this.pole.getSzerokosc()) {
                // Krótka roszada
                if (szachownica[this.pole.getWysokosc()][7].getFigure() instanceof wieza 
                        && szachownica[this.pole.getWysokosc()][7].getFigure().getColor() == this.getColor()
                        && !((wieza) szachownica[this.pole.getWysokosc()][7].getFigure()).wykonal_ruch && !this.wykonal_ruch){
                    // Sprawdzamy czy pola miedzy figurami sa puste
                    for (int i = this.pole.getSzerokosc() + 1; i < 7; i++) {
                        if (szachownica[this.pole.getWysokosc()][i].getFigure() != null) {
                            return false;
                        }
                        if(szachownica[this.pole.getWysokosc()][i].czy_pole_jest_atakowane(szachownica,this.getColor())){
                            return false;
                        }
                        if(this.czy_krol_jest_atakowany(szachownica)){
                            return false;
                        }
                    }
                    return true;
                }
            } else {
                // Długa roszada
                if (szachownica[this.pole.getWysokosc()][0].getFigure() instanceof wieza 
                        && szachownica[this.pole.getWysokosc()][0].getFigure().getColor() == this.getColor()
                        && !((wieza) szachownica[this.pole.getWysokosc()][0].getFigure()).wykonal_ruch && !this.wykonal_ruch) {
                    // Sprawdzamy czy pola miedzy figurami sa puste
                    for (int i = this.pole.getSzerokosc() - 1; i > 0; i--) {
                        if (szachownica[this.pole.getWysokosc()][i].getFigure() != null) {
                            return false;
                        }
                        if(szachownica[this.pole.getWysokosc()][i].czy_pole_jest_atakowane(szachownica,this.getColor())){
                            return false;
                        }
                        if(this.czy_krol_jest_atakowany(szachownica)){
                            return false;
                        }
                    }
                    return true;
                }
            }
        } else if (roznica_szerokosc <= 1 && roznica_wysokosci <= 1) {
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
