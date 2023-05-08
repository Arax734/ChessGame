public abstract class figura { // klasa abstrakcyjna reprezentujaca figure
    String color; // kolor figury
    pole pole; // pole, na ktorym stoi figura
    boolean zbita; // okresla czy figura jest zbita
    pole poprzednie_pole;
    boolean passant;

    public figura(String color) {
        this.color = color;
    }

    public pole getPole() {
        return pole;
    }

    public String getColor() {
        return color;
    }

    public void setPole(pole destination) {
        this.pole = destination;
    }

    abstract boolean symuluj_ruch_dla_krola(pole destination, pole[][] szachownica);

    public boolean czy_krol_jest_atakowany(pole[][] szachownica) {
        // Pobieramy pozycję króla
        pole kingSquare = this.getKingSquare(szachownica);
        figura king = kingSquare.getFigure();
        kingSquare.setFigure(null); // Chwilowo zdejmujemy króla z planszy
        // Sprawdzamy czy pionek przeciwnika może ruszyć się na pole, na którym stał król
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                pole currentSquare = szachownica[rank][file];
                figura currentPiece = currentSquare.getFigure();
                if (currentPiece != null && currentPiece.getColor() != this.getColor()) {
                    // Symulujemy ruch dla pozycji naszego króla
                    if (currentPiece instanceof pion) {
                        if (currentPiece.symuluj_ruch_dla_krola(kingSquare, szachownica)) {
                            kingSquare.setFigure(king); // Przywracamy króla na planszę
                            return true;
                        }
                    } else if (currentPiece.symuluj_ruch(kingSquare, szachownica)) {
                        kingSquare.setFigure(king); // Przywracamy króla na planszę
                        return true;
                    }
                }
            }
        }
        // Żaden pionek przeciwnika nie atakuje króla
        kingSquare.setFigure(king); // Przywracamy króla na planszę
        return false;
    }

    // Pomocnicza metoda do zlokalizowania króla
    public pole getKingSquare(pole[][] szachownica) {
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                pole currentSquare = szachownica[rank][file];
                figura currentPiece = currentSquare.getFigure();
                if (currentPiece instanceof krol && currentPiece.getColor() == this.getColor()) {
                    return currentSquare;
                }
            }
        }
        // Król nie został znaleziony (sytuacja niemożliwa dla prawidłowej gry)
        return null;
    }

    // Pomocnicza metoda do zlokalizowania króla przeciwnika
    public pole getOppositeKingSquare(pole[][] szachownica) {
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                pole currentSquare = szachownica[rank][file];
                figura currentPiece = currentSquare.getFigure();
                if (currentPiece instanceof krol && currentPiece.getColor() != this.getColor()) {
                    return currentSquare;
                }
            }
        }
        // Król nie został znaleziony (sytuacja niemożliwa dla prawidłowej gry)
        return null;
    }

    abstract boolean symuluj_ruch(pole destination, pole[][] szachownica);

    public pole getPoprzedniePole(){
        return this.poprzednie_pole;
    }

    public void setPoprzedniePole(pole poprzednie_pole){
        this.poprzednie_pole = poprzednie_pole;
    }

    public boolean getPassant(){
        return this.passant;
    }

    public void setPassant(boolean passant){
        this.passant = passant;
    }

    public void clearPassant(pole[][] szachownica){
        for(int wysokosc=0; wysokosc<8; wysokosc++){
            for(int szerokosc=0; szerokosc<8; szerokosc++){
                if(szachownica[wysokosc][szerokosc].getFigure() != null){
                    if(szachownica[wysokosc][szerokosc].getFigure() instanceof pion){
                        if(szachownica[wysokosc][szerokosc].getFigure() != this){
                            szachownica[wysokosc][szerokosc].getFigure().setPassant(false);
                        }
                    }
                }
            }
        }
    }

    public boolean wykonaj_ruch(pole destination, pole[][] szachownica) {
        if(this instanceof krol){
            int roznica_szerokosc = Math.abs(destination.getSzerokosc() - this.pole.getSzerokosc());
            int roznica_wysokosci = Math.abs(destination.getWysokosc() - this.pole.getWysokosc());
            
            if (roznica_szerokosc == 2 && roznica_wysokosci == 0) {
                figura wieza;
                if (destination.getSzerokosc() > this.pole.getSzerokosc()) {
                    // Krótka roszada
                    wieza = szachownica[this.pole.getWysokosc()][7].getFigure();
                    pole start_wieza = wieza.getPole();
                    start_wieza.setFigure(null);
                    szachownica[this.pole.getWysokosc()][destination.getSzerokosc()-1].setFigure(wieza);
                    wieza.setPole(szachownica[this.pole.getWysokosc()][destination.getSzerokosc()-1]);
                } else {
                    // Długa roszada
                    wieza = szachownica[this.pole.getWysokosc()][0].getFigure();
                    pole start_wieza = wieza.getPole();
                    start_wieza.setFigure(null);
                    szachownica[this.pole.getWysokosc()][destination.getSzerokosc()+1].setFigure(wieza);
                    wieza.setPole(szachownica[this.pole.getWysokosc()][destination.getSzerokosc()+1]);
                }
            }
        }
        pole start = this.getPole();
        if(this instanceof pion){
            this.setPoprzedniePole(start);
            if(this.getColor() == "bialy"){
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
                                        passant.getPole().setFigure(null);
                                        passant.setPole(null);
                                    }
                                    }                         
                                }
                            }
                        }
                    }
            }
            else{
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
                                            passant.getPole().setFigure(null);
                                            passant.setPole(null);
                                        }
                                    }     
                                }
                            }
                        }
                    }
            }
        }
        figura capturedPiece = destination.getFigure();
        start.setFigure(null);
        destination.setFigure(this);
        this.setPole(destination);
        if (czy_krol_jest_atakowany(szachownica)) {
            // Przywracamy konfigurację szachownicy, jeśli król jest atakowany
            destination.setFigure(capturedPiece);
            start.setFigure(this);
            this.setPole(start);
            return false;
        }
        if (this instanceof krol || this instanceof wieza || this instanceof pion) {
            this.setWykonalRuch();
        }
        if(this instanceof pion){
            int roznica = Math.abs(this.getPole().getWysokosc()-this.getPoprzedniePole().getWysokosc());
            if(roznica == 2){
                this.setPassant(true);
            }
        }
        return true;
    }

    public abstract void setWykonalRuch();

    public boolean szach_mat(pole[][] szachownica) {
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                pole currentSquare = szachownica[rank][file];
                figura currentPiece = currentSquare.getFigure();
                if (currentPiece != null && currentPiece.getColor() != this.getColor()) {
                    for (int x = 0; x < 8; x++) {
                        for (int y = 0; y < 8; y++) {
                            if (currentPiece.symuluj_ruch(szachownica[x][y], szachownica)) {
                                pole aktualne = currentPiece.getPole();
                                pole docelowe = szachownica[x][y];
                                if (szachownica[x][y].getFigure() != null) {
                                    figura tymczasowa = docelowe.getFigure();
                                    aktualne.setFigure(null);
                                    docelowe.setFigure(currentPiece);
                                    currentPiece.setPole(docelowe);
                                    if (currentPiece.czy_krol_jest_atakowany(szachownica)) {
                                        aktualne.setFigure(currentPiece);
                                        docelowe.setFigure(tymczasowa);
                                        currentPiece.setPole(aktualne);
                                    } else {
                                        aktualne.setFigure(currentPiece);
                                        docelowe.setFigure(tymczasowa);
                                        currentPiece.setPole(aktualne);
                                        return false;
                                    }
                                } else {
                                    aktualne.setFigure(null);
                                    docelowe.setFigure(currentPiece);
                                    currentPiece.setPole(docelowe);
                                    if (currentPiece.czy_krol_jest_atakowany(szachownica)) {
                                        aktualne.setFigure(currentPiece);
                                        docelowe.setFigure(null);
                                        currentPiece.setPole(aktualne);
                                    } else {
                                        aktualne.setFigure(currentPiece);
                                        docelowe.setFigure(null);
                                        currentPiece.setPole(aktualne);
                                        return false;
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
        if(getOppositeKingSquare(szachownica).getFigure().czy_krol_jest_atakowany(szachownica)){
            return true;
        }else{
            return false;
        }
        
    }
    public boolean stalemate(pole[][] szachownica, String playerColor) {
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                pole currentSquare = szachownica[rank][file];
                figura currentPiece = currentSquare.getFigure();
                if (currentPiece != null && currentPiece.getColor() == playerColor) {
                    for (int x = 0; x < 8; x++) {
                        for (int y = 0; y < 8; y++) {
                            if (currentPiece.symuluj_ruch(szachownica[x][y], szachownica)) {
                                pole aktualne = currentPiece.getPole();
                                pole docelowe = szachownica[x][y];
                                figura tymczasowa = docelowe.getFigure();
                                aktualne.setFigure(null);
                                docelowe.setFigure(currentPiece);
                                currentPiece.setPole(docelowe);
                                boolean czy_krol_jest_atakowany = currentPiece.czy_krol_jest_atakowany(szachownica);
                                aktualne.setFigure(currentPiece);
                                docelowe.setFigure(tymczasowa);
                                currentPiece.setPole(aktualne);
                                if (!czy_krol_jest_atakowany) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
    
    
}