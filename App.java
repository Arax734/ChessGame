public class App {
    public static void main(String[] args){
        szachownica s1 = new szachownica();
        pole tymczasowe = s1.getPole('a', 8);
        System.out.print(tymczasowe.figura.color);
    }
}
