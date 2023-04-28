public abstract class figura { // klasa abstrakcyjna reprezentujaca figure
    String color; // kolor figury
    pole pole; // pole, na ktorym stoi figura
    boolean zbita; // okresla czy figura jest zbita

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
        // Get the position of the king
        pole kingSquare = this.getKingSquare(szachownica);
        figura king = kingSquare.getFigure();
        kingSquare.setFigure(null); // temporarily remove the king from the square
        // Check if any opponent piece can move to the king's square
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                pole currentSquare = szachownica[rank][file];
                figura currentPiece = currentSquare.getFigure();
                if (currentPiece != null && currentPiece.getColor() != this.getColor()) {
                    // Simulate a move for the opponent piece to the king's square
                    if (currentPiece instanceof pion) {
                        if (currentPiece.symuluj_ruch_dla_krola(kingSquare, szachownica)) {
                            kingSquare.setFigure(king); // put the king back on the square
                            return true;
                        }
                    } else if (currentPiece.symuluj_ruch(kingSquare, szachownica)) {
                        kingSquare.setFigure(king); // put the king back on the square
                        return true;
                    }
                }
            }
        }
        // No opponent piece can attack the king's square
        kingSquare.setFigure(king); // put the king back on the square
        return false;
    }

    // Helper method to find the square on which the king is located
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
        // The king was not found on the board (should never happen in a legal game)
        return null;
    }

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
        // The king was not found on the board (should never happen in a legal game)
        return null;
    }

    abstract boolean symuluj_ruch(pole destination, pole[][] szachownica);

    public boolean wykonaj_ruch(pole destination, pole[][] szachownica) {
        // The move is valid, update the board
        pole start = this.getPole(); // get the starting square of the piece
        figura capturedPiece = destination.getFigure(); // store any captured piece
        start.setFigure(null); // remove the piece from the starting square
        destination.setFigure(this); // place the piece on the destination square
        this.setPole(destination); // update the position of the piece
        if (czy_krol_jest_atakowany(szachownica)) {
            // Revert changes if the king is under attack
            destination.setFigure(capturedPiece); // restore any captured piece
            start.setFigure(this); // move the piece back to its starting square
            this.setPole(start); // update the position of the piece
            return false;
        }
        if (this instanceof krol || this instanceof wieza || this instanceof pion) {
            this.setWykonalRuch();
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
        return true;
    }
}