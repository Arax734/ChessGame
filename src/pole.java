public class pole {
    String color; // kolor pola
    int szerokosc; // cyfra odpowiadajaca poziomej pozycji
    int wysokosc; // cyfra odpowiadajaca pionowej pozycji
    figura figura; // ewentualna przypisana figura

    public pole(int szerokosc, int wysokosc, String color) {
        this.szerokosc = szerokosc;
        this.wysokosc = wysokosc;
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public int getWysokosc() {
        return wysokosc;
    }

    public int getSzerokosc() {
        return szerokosc;
    }

    public figura getFigure() {
        return figura;
    }

    public void setFigure(figura figura) {
        this.figura = figura;
    }

    public boolean czy_pole_jest_atakowane(pole[][] szachownica, String color){
        // Sprawdzamy czy pionek przeciwnika może ruszyć się na pole
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                pole currentSquare = szachownica[rank][file];
                figura currentPiece = currentSquare.getFigure();
                if (currentPiece != null && currentPiece.getColor() != color) {
                    // Symulujemy ruch dla pozycji pola
                    if (currentPiece instanceof pion) {
                        if (currentPiece.symuluj_ruch_dla_krola(this, szachownica)) {
                            return true;
                        }
                    } else if (currentPiece.symuluj_ruch(this, szachownica)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
