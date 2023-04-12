public abstract class figura { //klasa abstrakcyjna reprezentujaca figure
    String color; //kolor figury
    pole pole; //pole, na ktorym stoi figura
    boolean zbita; //okresla czy figura jest zbita
    
    public figura(String color){
        this.color = color;
    }

    public boolean symuluj_ruch(String destination){
        //funkcja przyjmuje stringa zlozonego z litery oraz cyfry, wykorzystamy to potem do odszukania
        //potrzebnego nam pola
        return true;
    }

    public void wykonaj_ruch(String destination){
        if(symuluj_ruch(destination)){ //sprawdzamy czy danych ruch jest mozliwy

        }
    }
}