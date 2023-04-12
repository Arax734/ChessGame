import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            ChessGui gui = new ChessGui();
            gui.setVisible(true);
        });
    }
}
