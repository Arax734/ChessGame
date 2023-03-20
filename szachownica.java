public class szachownica {
    pole[][] szachownica;

    public szachownica() {
        //tworzenie szachownicy
        szachownica = new pole[8][8];
        
        //nadajemy biale i czarne pola
        boolean isBlack = false;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (isBlack) {
                    szachownica[row][col] = new pole((char)('a' + col), row + 1, "czarny");
                } else {
                    szachownica[row][col] = new pole((char)('a' + col), row + 1, "bialy");
                }
                isBlack = !isBlack;
            }
            isBlack = !isBlack;
        }
        
        //umieszczamy figury na planszy - polom przypisujemy figury, a figurom pola, aby pozniejszy dostep byl prostszy
        szachownica[0][0].figura = new wieza("bialy",false);
        szachownica[0][1].figura = new skoczek("bialy");
        szachownica[0][2].figura = new goniec("bialy","czarny");
        szachownica[0][3].figura = new hetman("bialy");
        szachownica[0][4].figura = new krol("bialy",false);
        szachownica[0][5].figura = new goniec("bialy","bialy");
        szachownica[0][6].figura = new skoczek("bialy");
        szachownica[0][7].figura = new wieza("bialy",false);

        szachownica[1][0].figura = new pion("bialy",false);
        szachownica[1][1].figura = new pion("bialy",false);
        szachownica[1][2].figura = new pion("bialy",false);
        szachownica[1][3].figura = new pion("bialy",false);
        szachownica[1][4].figura = new pion("bialy",false);
        szachownica[1][5].figura = new pion("bialy",false);
        szachownica[1][6].figura = new pion("bialy",false);
        szachownica[1][7].figura = new pion("bialy",false);
        
        szachownica[7][0].figura = new wieza("czarny",false);
        szachownica[7][1].figura = new skoczek("czarny");
        szachownica[7][2].figura = new goniec("czarny","bialy");
        szachownica[7][3].figura = new hetman("czarny");
        szachownica[7][4].figura = new krol("czarny",false);
        szachownica[7][5].figura = new goniec("czarny","czarny");
        szachownica[7][6].figura = new skoczek("czarny");
        szachownica[7][7].figura = new wieza("czarny",false);

        szachownica[6][0].figura = new pion("czarny",false);
        szachownica[6][1].figura = new pion("czarny",false);
        szachownica[6][2].figura = new pion("czarny",false);
        szachownica[6][3].figura = new pion("czarny",false);
        szachownica[6][4].figura = new pion("czarny",false);
        szachownica[6][5].figura = new pion("czarny",false);
        szachownica[6][6].figura = new pion("czarny",false);
        szachownica[6][7].figura = new pion("czarny",false);

        for (int i=0; i<2; i++){
            for (int j=0;j<8;j++){
                szachownica[i][j].figura.pole = szachownica[i][j];
            }
        }
        for (int i=6; i<8; i++){
            for (int j=0;j<8;j++){
                szachownica[i][j].figura.pole = szachownica[i][j];
            }
        }
        
    }

    //funkcja na wydobycie obiektu pole wedlug podanych wspolrzednych
    public pole getPole(char col, int row) {
        int colIndex = col - 'a';
        int rowIndex = row - 1;
        return szachownica[rowIndex][colIndex];
    }
    
    //funkcja na ustawienie figury na polu
    public void placeFigure(char col, int row, figura figura) {
        int colIndex = col - 'a';
        int rowIndex = row - 1;
        szachownica[rowIndex][colIndex].figura = figura;

    }
    
    //funkcja na zdjecie figury z pola
    public void removeFigure(char col, int row) {
        int colIndex = col - 'a';
        int rowIndex = row - 1;
        szachownica[rowIndex][colIndex].figura = (null);
    }
}
