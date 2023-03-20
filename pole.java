public class pole {
    String color; //kolor pola
    char szerokosc; //litera odpowiadajaca poziomej pozycji
    int wysokosc; //cyfra odpowiadajaca pionowej pozycji
    figura figura; //ewentualna przypisana figura

    public pole(char szerokosc, int wysokosc, String color){
        this.szerokosc = szerokosc;
        this.wysokosc = wysokosc;
        this.color = color;
    }
}
