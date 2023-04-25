import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class ChessGui extends JFrame {
    private final JPanel chessBoard;
    private final JButton[][] squares;
    private final int WIDTH = 70; // szerokość pola
    private final int HEIGHT = 70; // wysokość pola
    private int clickedRow = -1;
    private int clickedCol = -1;

    public ChessGui() {
        setTitle("Szachy");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        Color[][] originalColors = new Color[8][8];

        // Stwórz etykiety poziome
        JPanel horizontalLabels = new JPanel(new GridLayout(1, 8));

        // Stwórz szachownicę
        szachownica sz = new szachownica();
        pole[][] szachownica = sz.getSzachownica();

        // Stwórz etykiety pola na planszy
        chessBoard = new JPanel(new GridLayout(9, 9)) {
            // Efekt gradientu
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                GradientPaint gradientPaint = new GradientPaint(
                        0, 0, new Color(134, 134, 134),
                        getWidth(), getHeight(), new Color(226, 226, 226));
                g2d.setPaint(gradientPaint);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };

        squares = new JButton[8][8];
        for (int row = 0; row < 8; row++) {
            // Stwórz etykietę pionową
            JLabel verticalLabel = new JLabel(String.valueOf(8 - row), JLabel.CENTER);
            verticalLabel.setFont(new Font("Century Gothic Bold", Font.PLAIN, 18));
            chessBoard.add(verticalLabel);
            for (int col = 0; col < 8; col++) {
                squares[row][col] = new JButton();
                chessBoard.add(squares[row][col]);

                // Ustaw rozmiar pola
                squares[row][col].setPreferredSize(new Dimension(WIDTH, HEIGHT));

                // Ustaw kolor pola
                if ((row + col) % 2 == 0) {
                    squares[row][col].setBackground(Color.LIGHT_GRAY);

                } else {
                    squares[row][col].setBackground(Color.DARK_GRAY);
                }

                // Usuń obramowanie przycisku
                squares[row][col].setFocusPainted(false);
                squares[row][col].setBorder(BorderFactory.createEmptyBorder());

                // Dodaj ActionListener do każdego przycisku
                final int finalRow = row;
                final int finalCol = col;
                squares[row][col].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Jeśli pole, które nacisnęliśmy jest puste
                        if (clickedRow == -1 && clickedCol == -1) {
                            if (sz.getFigure(finalCol, finalRow) == null) {
                                System.out.println("Puste pole");
                                return;
                            }
                            // Żaden pionek nie jest wybrany, wybierz naciśnięty pionek
                            clickedRow = finalRow;
                            clickedCol = finalCol;
                            squares[clickedRow][clickedCol].setBackground(Color.GREEN);
                            // Get the possible moves for the selected piece
                            figura to_check = sz.getFigure(clickedCol, clickedRow);
                            ArrayList<pole> possibleMoves = new ArrayList<>();
                            for (int row = 0; row < 8; row++) {
                                for (int col = 0; col < 8; col++) {
                                    // Simulate a move to the current square
                                    if (to_check.symuluj_ruch(sz.getPole(col, row), szachownica)) {
                                        possibleMoves.add(sz.getPole(col, row));
                                    }
                                }
                            }
                            for (int i = 0; i < 8; i++) {
                                for (int j = 0; j < 8; j++) {
                                    originalColors[i][j] = squares[i][j].getBackground();
                                }
                            }
                            // Loop through the possible moves and set the background color of the
                            // corresponding squares to orange
                            for (pole pole : possibleMoves) {
                                int row = pole.getWysokosc();
                                int col = pole.getSzerokosc();
                                if (sz.getFigure(col, row) != null && sz.getFigure(col, row).getColor() != to_check.getColor()) {
                                    // square is occupied by enemy, set background color to red
                                    squares[row][col].setBackground(Color.RED);
                                } else {
                                    // square is empty or occupied by own piece, set background color to orange
                                    if(szachownica[row][col].getColor() == "czarny"){
                                        squares[row][col].setBackground(new Color(255,165,0));
                                    }else{
                                        squares[row][col].setBackground(Color.ORANGE);
                                    }          
                                }
                            }
                        } else {
                            // Pionek jest już wybrany, próbujemy go przenieść do klikniętego pola
                            // Jeśli klikamy na pole, które wcześniej nacisnęliśmy
                            if (clickedRow == finalRow && clickedCol == finalCol) {
                                for (int i = 0; i < 8; i++) {
                                    for (int j = 0; j < 8; j++) {
                                        squares[i][j].setBackground(originalColors[i][j]);
                                    }
                                } 
                                if (szachownica[clickedCol][clickedRow].getColor() == "czarny") {
                                    squares[clickedRow][clickedCol].setBackground(Color.DARK_GRAY);
                                } else {
                                    squares[clickedRow][clickedCol].setBackground(Color.LIGHT_GRAY);
                                }
                                
                                clickedRow = -1;
                                clickedCol = -1;                                   
                                return;
                            }
                            // Poniżej warunek ruchu do zrobienia
                            figura to_check = sz.getFigure(clickedCol, clickedRow);
                            if (to_check.symuluj_ruch(sz.getPole(finalCol, finalRow), szachownica)) {
                                if (to_check.wykonaj_ruch(sz.getPole(finalCol, finalRow), szachownica)) {
                                    // Wykonano ruch poprawnie, aktualizujemy dane
                                    for (int i = 0; i < 8; i++) {
                                        for (int j = 0; j < 8; j++) {
                                            squares[i][j].setBackground(originalColors[i][j]);
                                        }
                                    } 
                                    squares[finalRow][finalCol].setIcon(squares[clickedRow][clickedCol].getIcon());
                                    squares[clickedRow][clickedCol].setIcon(null);
                                    if (szachownica[clickedCol][clickedRow].getColor() == "czarny") {
                                        squares[clickedRow][clickedCol].setBackground(Color.DARK_GRAY);
                                    } else {
                                        squares[clickedRow][clickedCol].setBackground(Color.LIGHT_GRAY);
                                    }
                                } else {
                                    for (int i = 0; i < 8; i++) {
                                        for (int j = 0; j < 8; j++) {
                                            squares[i][j].setBackground(originalColors[i][j]);
                                        }
                                    } 
                                    if (szachownica[clickedCol][clickedRow].getColor() == "czarny") {
                                        squares[clickedRow][clickedCol].setBackground(Color.DARK_GRAY);
                                    } else {
                                        squares[clickedRow][clickedCol].setBackground(Color.LIGHT_GRAY);
                                    }
                                    clickedRow = -1;
                                    clickedCol = -1;
                                    return;
                                }

                            } else {
                                for (int i = 0; i < 8; i++) {
                                    for (int j = 0; j < 8; j++) {
                                        squares[i][j].setBackground(originalColors[i][j]);
                                    }
                                } 
                                if (szachownica[clickedCol][clickedRow].getColor() == "czarny") {
                                    squares[clickedRow][clickedCol].setBackground(Color.DARK_GRAY);
                                } else {
                                    squares[clickedRow][clickedCol].setBackground(Color.LIGHT_GRAY);
                                }
                                clickedRow = -1;
                                clickedCol = -1;
                                
                                return;
                            }
                            // Czyścimy pozycję naciśniętego pionka
                            clickedRow = -1;
                            clickedCol = -1; 
                        }
                    }
                });
            }
        }

        // Dodaj etykiety poziome do planszy
        chessBoard.add(new JLabel());
        for (int i = 0; i < 8; i++) {
            JLabel label = new JLabel(String.valueOf((char) ('A' + i)), JLabel.CENTER);
            label.setFont(new Font("Century Gothic Bold", Font.PLAIN, 18));
            chessBoard.add(label);
        }

        // Wstawianie pionow
        // Czarne

        ImageIcon originalImage = new ImageIcon("images/pawn-black.png");
        Image scaledImage = originalImage.getImage().getScaledInstance(WIDTH - 10, HEIGHT - 10, Image.SCALE_SMOOTH);

        for (int j = 0; j < 8; j++) {
            squares[1][j].setIcon(new ImageIcon(scaledImage));
        }

        // Biale

        ImageIcon originalImage2 = new ImageIcon("images/pawn-white.png");
        Image scaledImage2 = originalImage2.getImage().getScaledInstance(WIDTH - 10, HEIGHT - 10, Image.SCALE_SMOOTH);

        for (int j = 0; j < 8; j++) {
            squares[6][j].setIcon(new ImageIcon(scaledImage2));
        }

        // Wstawianie wiezy
        // Czarne

        ImageIcon originalImage3 = new ImageIcon("images/rook-black.png");
        Image scaledImage3 = originalImage3.getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);

        squares[0][0].setIcon(new ImageIcon(scaledImage3));
        squares[0][7].setIcon(new ImageIcon(scaledImage3));

        // Biale

        ImageIcon originalImage4 = new ImageIcon("images/rook-white.png");
        Image scaledImage4 = originalImage4.getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);

        squares[7][0].setIcon(new ImageIcon(scaledImage4));
        squares[7][7].setIcon(new ImageIcon(scaledImage4));

        // Wstawianie skoczkow
        // Czarne

        ImageIcon originalImage5 = new ImageIcon("images/knight-black.png");
        Image scaledImage5 = originalImage5.getImage().getScaledInstance(WIDTH - 10, HEIGHT - 10, Image.SCALE_SMOOTH);

        squares[0][1].setIcon(new ImageIcon(scaledImage5));
        squares[0][6].setIcon(new ImageIcon(scaledImage5));

        // Biale

        ImageIcon originalImage6 = new ImageIcon("images/knight-white.png");
        Image scaledImage6 = originalImage6.getImage().getScaledInstance(WIDTH - 10, HEIGHT - 10, Image.SCALE_SMOOTH);

        squares[7][1].setIcon(new ImageIcon(scaledImage6));
        squares[7][6].setIcon(new ImageIcon(scaledImage6));

        // Wstawianie goncow
        // Czarne

        ImageIcon originalImage7 = new ImageIcon("images/bishop-black.png");
        Image scaledImage7 = originalImage7.getImage().getScaledInstance(WIDTH - 10, HEIGHT - 10, Image.SCALE_SMOOTH);

        squares[0][2].setIcon(new ImageIcon(scaledImage7));
        squares[0][5].setIcon(new ImageIcon(scaledImage7));

        // Biale

        ImageIcon originalImage8 = new ImageIcon("images/bishop-white.png");
        Image scaledImage8 = originalImage8.getImage().getScaledInstance(WIDTH - 10, HEIGHT - 10, Image.SCALE_SMOOTH);

        squares[7][2].setIcon(new ImageIcon(scaledImage8));
        squares[7][5].setIcon(new ImageIcon(scaledImage8));

        // Wstawianie hetmana i krola
        // Czarne

        ImageIcon originalImage9 = new ImageIcon("images/queen-black.png");
        Image scaledImage9 = originalImage9.getImage().getScaledInstance(WIDTH - 10, HEIGHT - 10, Image.SCALE_SMOOTH);
        ImageIcon originalImage10 = new ImageIcon("images/king-black.png");
        Image scaledImage10 = originalImage10.getImage().getScaledInstance(WIDTH - 10, HEIGHT - 10, Image.SCALE_SMOOTH);

        squares[0][3].setIcon(new ImageIcon(scaledImage9));
        squares[0][4].setIcon(new ImageIcon(scaledImage10));

        // Biale

        ImageIcon originalImage11 = new ImageIcon("images/queen-white.png");
        Image scaledImage11 = originalImage11.getImage().getScaledInstance(WIDTH - 10, HEIGHT - 10, Image.SCALE_SMOOTH);
        ImageIcon originalImage12 = new ImageIcon("images/king-white.png");
        Image scaledImage12 = originalImage12.getImage().getScaledInstance(WIDTH - 10, HEIGHT - 10, Image.SCALE_SMOOTH);

        squares[7][3].setIcon(new ImageIcon(scaledImage11));
        squares[7][4].setIcon(new ImageIcon(scaledImage12));

        // Dodaj etykiety i planszę do ramki
        add(horizontalLabels, BorderLayout.NORTH);
        add(chessBoard, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

    }
}
