public class skoczek extends figura {
    public skoczek(String color) {
        super(color);
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
        if (destination.getWysokosc() == this.getPole().getWysokosc() + 2
                && (destination.getSzerokosc() == this.getPole().getSzerokosc() - 1
                        || destination.getSzerokosc() == this.getPole().getSzerokosc() + 1)) {
            return true;
        }
        if (destination.getWysokosc() == this.getPole().getWysokosc() - 2
                && (destination.getSzerokosc() == this.getPole().getSzerokosc() - 1
                        || destination.getSzerokosc() == this.getPole().getSzerokosc() + 1)) {
            return true;
        }
        if (destination.getSzerokosc() == this.getPole().getSzerokosc() + 2
                && (destination.getWysokosc() == this.getPole().getWysokosc() - 1
                        || destination.getWysokosc() == this.getPole().getWysokosc() + 1)) {
            return true;
        }
        if (destination.getSzerokosc() == this.getPole().getSzerokosc() - 2
                && (destination.getWysokosc() == this.getPole().getWysokosc() - 1
                        || destination.getWysokosc() == this.getPole().getWysokosc() + 1)) {
            return true;
        }
        return false;
    }

    public boolean symuluj_ruch_dla_krola(pole destination, pole[][] szachownica) {
        return false;
    }

    public void setWykonalRuch() {

    }
}
