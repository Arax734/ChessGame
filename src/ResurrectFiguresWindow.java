import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ResurrectFiguresWindow extends JFrame {
    private JPanel panel;
    private JButton[] figureButtons;
    public int selectedFigureIndex;
    private final int WIDTH = 50; // szerokość pola
    private final int HEIGHT = 50; // wysokość pola

    public ResurrectFiguresWindow(String[] killedFigures, String color) {
        super("Resurrect Figures");
        // Panel z przyciskami
        panel = new JPanel();
        panel.setLayout(new GridLayout(killedFigures.length, 1));

        // Przycisk dla każdej zbitej figury
        figureButtons = new JButton[killedFigures.length];
        for (int i = 0; i < killedFigures.length; i++) {
            figureButtons[i] = new JButton();
            figureButtons[i].addActionListener(new FigureButtonListener(i));
            if(killedFigures[i] == "wieza"){
                if(color == "bialy"){
                    ImageIcon originalImage = new ImageIcon("images/rook-white.png");
                    Image scaledImage = originalImage.getImage().getScaledInstance(WIDTH, HEIGHT,Image.SCALE_SMOOTH);
                    figureButtons[i].setIcon(new ImageIcon(scaledImage));
                }else{
                    ImageIcon originalImage = new ImageIcon("images/rook-black.png");
                    Image scaledImage = originalImage.getImage().getScaledInstance(WIDTH, HEIGHT,Image.SCALE_SMOOTH);
                    figureButtons[i].setIcon(new ImageIcon(scaledImage));
                }
            }
            if(killedFigures[i] == "skoczek"){
                if(color == "bialy"){
                    ImageIcon originalImage = new ImageIcon("images/knight-white.png");
                    Image scaledImage = originalImage.getImage().getScaledInstance(WIDTH-10, HEIGHT-10,Image.SCALE_SMOOTH);
                    figureButtons[i].setIcon(new ImageIcon(scaledImage));
                }else{
                    ImageIcon originalImage = new ImageIcon("images/knight-black.png");
                    Image scaledImage = originalImage.getImage().getScaledInstance(WIDTH-10, HEIGHT-10,Image.SCALE_SMOOTH);
                    figureButtons[i].setIcon(new ImageIcon(scaledImage));
                }
            }
            if(killedFigures[i] == "goniec"){
                if(color == "bialy"){
                    ImageIcon originalImage = new ImageIcon("images/bishop-white.png");
                    Image scaledImage = originalImage.getImage().getScaledInstance(WIDTH-10, HEIGHT-10,Image.SCALE_SMOOTH);
                    figureButtons[i].setIcon(new ImageIcon(scaledImage));
                }else{
                    ImageIcon originalImage = new ImageIcon("images/bishop-black.png");
                    Image scaledImage = originalImage.getImage().getScaledInstance(WIDTH-10, HEIGHT-10,Image.SCALE_SMOOTH);
                    figureButtons[i].setIcon(new ImageIcon(scaledImage));
                }
            }
            if(killedFigures[i] == "hetman"){
                if(color == "bialy"){
                    ImageIcon originalImage = new ImageIcon("images/queen-white.png");
                    Image scaledImage = originalImage.getImage().getScaledInstance(WIDTH-10, HEIGHT-10,Image.SCALE_SMOOTH);
                    figureButtons[i].setIcon(new ImageIcon(scaledImage));
                }else{
                    ImageIcon originalImage = new ImageIcon("images/queen-black.png");
                    Image scaledImage = originalImage.getImage().getScaledInstance(WIDTH-10, HEIGHT-10,Image.SCALE_SMOOTH);
                    figureButtons[i].setIcon(new ImageIcon(scaledImage));
                }
            }
            figureButtons[i].setFocusPainted(false);
            figureButtons[i].setBorder(BorderFactory.createLineBorder(new Color(33, 33, 33), 2));
            panel.add(figureButtons[i]);
        }

        getContentPane().add(panel, BorderLayout.CENTER);

        // Domyślnie zbita figura
        selectedFigureIndex = -1;

        // Ustawienia okna
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private class FigureButtonListener implements ActionListener {
        public int index;

        public FigureButtonListener(int index) {
            this.index = index;
        }

        public void actionPerformed(ActionEvent e) {
            for (int x = 0; x < figureButtons.length; x++) {
                figureButtons[x].setBackground(null);
            }
            selectedFigureIndex = index;
            JButton selectedButton = (JButton) e.getSource();
            selectedButton.setBackground(Color.GREEN); // set the color to green
        }
    }

    public int getSelectedFigureIndex() {
        return selectedFigureIndex;
    }
}
