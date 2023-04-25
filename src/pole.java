public class pole {
    String color; //kolor pola
    int szerokosc; //cyfra odpowiadajaca poziomej pozycji
    int wysokosc; //cyfra odpowiadajaca pionowej pozycji
    figura figura; //ewentualna przypisana figura

    public pole(int szerokosc, int wysokosc, String color){
        this.szerokosc = szerokosc;
        this.wysokosc = wysokosc;
        this.color = color;
    }

    public String getColor(){
        return color;
    }

    public int getWysokosc(){
        return wysokosc;
    }

    public int getSzerokosc(){
        return szerokosc;
    }

    public figura getFigure(){
        return figura;
    }

    public void setFigure(figura figura){
        this.figura = figura;
    }
}
