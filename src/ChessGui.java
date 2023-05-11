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
    private String recentPlayer = "bialy";

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
            private Color currentColor = new Color(200, 200, 200);
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                GradientPaint gradientPaint = new GradientPaint(
                        0, 0, currentColor,
                        getWidth(), getHeight(), new Color(33, 33, 33));
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

                originalColors[row][col] = squares[row][col].getBackground();

                // Usuń obramowanie przycisku
                squares[row][col].setFocusPainted(false);
                squares[row][col].setBorder(BorderFactory.createLineBorder(new Color(33, 33, 33), 2));

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
                            // Jeśli nie jest nasza tura
                            if (sz.getFigure(finalCol, finalRow).getColor() != recentPlayer) {
                                System.out.println("Teraz jest ruch przeciwnika");
                                return;
                            }
                            // Żaden pionek nie jest wybrany, wybierz naciśnięty pionek
                            clickedRow = finalRow;
                            clickedCol = finalCol;
                            squares[clickedRow][clickedCol].setBackground(Color.GREEN);
                            // Sprawdzamy możliwe ruchy dla wybranego pionka
                            figura to_check = sz.getFigure(clickedCol, clickedRow);
                            ArrayList<pole> possibleMoves = new ArrayList<>();
                            for (int row = 0; row < 8; row++) {
                                for (int col = 0; col < 8; col++) {
                                    // Symulujemy ruchy dla wybranego pionka
                                    if (to_check.symuluj_ruch(sz.getPole(col, row), szachownica)) {
                                        pole aktualne = to_check.getPole();
                                        pole docelowe = sz.getPole(col, row);
                                        if (sz.getPole(col, row).getFigure() != null) {
                                            figura tymczasowa = docelowe.getFigure();
                                            aktualne.setFigure(null);
                                            docelowe.setFigure(to_check);
                                            to_check.setPole(docelowe);
                                            if (to_check.czy_krol_jest_atakowany(szachownica)) {
                                                aktualne.setFigure(to_check);
                                                docelowe.setFigure(tymczasowa);
                                                to_check.setPole(aktualne);
                                            } else {
                                                aktualne.setFigure(to_check);
                                                docelowe.setFigure(tymczasowa);
                                                to_check.setPole(aktualne);
                                                possibleMoves.add(sz.getPole(col, row));
                                            }
                                        } else {
                                            aktualne.setFigure(null);
                                            docelowe.setFigure(to_check);
                                            to_check.setPole(docelowe);
                                            if (to_check.czy_krol_jest_atakowany(szachownica)) {
                                                aktualne.setFigure(to_check);
                                                docelowe.setFigure(null);
                                                to_check.setPole(aktualne);
                                            } else {
                                                aktualne.setFigure(to_check);
                                                docelowe.setFigure(null);
                                                to_check.setPole(aktualne);
                                                possibleMoves.add(sz.getPole(col, row));
                                            }
                                        }
                                    }
                                }
                            }
                            for (pole pole : possibleMoves) {
                                int row = pole.getWysokosc();
                                int col = pole.getSzerokosc();
                                if (sz.getFigure(col, row) != null
                                        && sz.getFigure(col, row).getColor() != to_check.getColor()) {
                                    // Zamiana koloru pola na czerwony, jeśli pole zajmowane jest przez przeciwnika
                                    squares[row][col].setBackground(Color.RED);
                                } else {
                                    // Zamiana koloru pól na pomarańczowe, jeśli ruch jest możliwy
                                    if (szachownica[row][col].getColor() == "czarny") {
                                        squares[row][col].setBackground(new Color(255, 165, 0));
                                    } else {
                                        squares[row][col].setBackground(Color.ORANGE);
                                    }
                                }
                                if (sz.getFigure(col, row) == null) {
                                    if (to_check instanceof pion) {
                                        if (to_check.getColor() == "bialy") {
                                            if (sz.getFigure(col, row + 1) != null) {
                                                if (sz.getFigure(col, row + 1) instanceof pion && sz
                                                        .getFigure(col, row + 1).getColor() != to_check.getColor()) {
                                                    squares[row][col].setBackground(Color.RED);
                                                }
                                            }
                                        } else {
                                            if (sz.getFigure(col, row - 1) != null) {
                                                if (sz.getFigure(col, row - 1) instanceof pion && sz
                                                        .getFigure(col, row - 1).getColor() != to_check.getColor()) {
                                                    squares[row][col].setBackground(Color.RED);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            // Jeśli klikamy na pole, które wcześniej nacisnęliśmy
                            if (clickedRow == finalRow && clickedCol == finalCol) {
                                for (int i = 0; i < 8; i++) {
                                    for (int j = 0; j < 8; j++) {
                                        squares[i][j].setBackground(originalColors[i][j]);
                                    }
                                }
                                figura to_check = sz.getFigure(clickedCol, clickedRow);
                                pole opposite = to_check.getOppositeKingSquare(szachownica);
                                if (opposite.getFigure().czy_krol_jest_atakowany(szachownica)) {
                                    int kingrow = opposite.getWysokosc();
                                    int kingcol = opposite.getSzerokosc();
                                    squares[kingrow][kingcol].setBackground(Color.RED);
                                }

                                pole ourking = to_check.getKingSquare(szachownica);
                                if (ourking.getFigure().czy_krol_jest_atakowany(szachownica)) {
                                    int kingrow = ourking.getWysokosc();
                                    int kingcol = ourking.getSzerokosc();
                                    squares[kingrow][kingcol].setBackground(Color.RED);
                                }

                                clickedRow = -1;
                                clickedCol = -1;
                                return;
                            }
                            // Pionek jest już wybrany, próbujemy go przenieść do klikniętego pola
                            figura to_check = sz.getFigure(clickedCol, clickedRow);
                            if (to_check.symuluj_ruch(sz.getPole(finalCol, finalRow), szachownica)) {
                                if (sz.getPole(finalCol, finalRow).getFigure() != null) {
                                    figura do_zbicia = sz.getPole(finalCol, finalRow).getFigure();
                                    if (do_zbicia.getColor() == "bialy") {
                                        boolean czy_pion = false;
                                        if (do_zbicia instanceof pion) {
                                            czy_pion = true;
                                        }
                                        if (czy_pion == false) {
                                            int index = sz.check_empty_slot(sz.zbite_biale);
                                            sz.zbite_biale[index] = do_zbicia;
                                        }
                                    } else {
                                        boolean czy_pion = false;
                                        if (do_zbicia instanceof pion) {
                                            czy_pion = true;
                                        }
                                        if (czy_pion == false) {
                                            int index = sz.check_empty_slot(sz.zbite_czarne);
                                            sz.zbite_czarne[index] = do_zbicia;
                                        }
                                    }
                                }
                                if (to_check.wykonaj_ruch(sz.getPole(finalCol, finalRow), szachownica)) {
                                    // Wykonano ruch poprawnie, aktualizujemy dane
                                    for (int i = 0; i < 8; i++) {
                                        for (int j = 0; j < 8; j++) {
                                            squares[i][j].setBackground(originalColors[i][j]);
                                        }
                                    }
                                    to_check.clearPassant(szachownica);
                                    if (to_check instanceof krol) {
                                        int roznica_szerokosc = Math.abs(clickedCol - finalCol);
                                        int roznica_wysokosci = Math.abs(clickedRow - finalRow);
                                        if (roznica_szerokosc == 2 && roznica_wysokosci == 0) {
                                            if (finalCol > clickedCol) {
                                                // Krótka roszada
                                                Icon wieza = squares[finalRow][7].getIcon();
                                                squares[finalRow][5].setIcon(wieza);
                                                squares[finalRow][7].setIcon(null);
                                            } else {
                                                // Długa roszada
                                                Icon wieza = squares[finalRow][0].getIcon();
                                                squares[finalRow][3].setIcon(wieza);
                                                squares[finalRow][0].setIcon(null);
                                            }
                                        }
                                    }
                                    pole ourking = to_check.getKingSquare(szachownica);
                                    if (ourking.getFigure().czy_krol_jest_atakowany(szachownica) == false) {
                                        int kingrow = ourking.getWysokosc();
                                        int kingcol = ourking.getSzerokosc();
                                        squares[kingrow][kingcol].setBackground(originalColors[kingrow][kingcol]);
                                    }
                                    pole opposite = to_check.getOppositeKingSquare(szachownica);
                                    if (opposite.getFigure().czy_krol_jest_atakowany(szachownica)) {
                                        int kingrow = opposite.getWysokosc();
                                        int kingcol = opposite.getSzerokosc();
                                        squares[kingrow][kingcol].setBackground(Color.RED);
                                    }
                                    squares[finalRow][finalCol].setIcon(squares[clickedRow][clickedCol].getIcon());
                                    squares[clickedRow][clickedCol].setIcon(null);

                                    // Sprawdzamy czy pion doszedl do konca szachownicy
                                    if (to_check instanceof pion) {
                                        if (to_check.color == "bialy") {
                                            if (finalRow == 0) {
                                                int count = 0;
                                                for (int x = 0; x < 15; x++) {
                                                    if (sz.zbite_biale[x] != null) {
                                                        count++;
                                                    }
                                                }

                                                int slot = 0;
                                                String[] killedFigures = new String[count];
                                                for (int x = 0; x < 15; x++) {
                                                    if (sz.zbite_biale[x] != null) {
                                                        killedFigures[slot] = sz.zbite_biale[x].getClass()
                                                                .getSimpleName();
                                                        slot++;
                                                    }
                                                }

                                                ResurrectFiguresWindow window = new ResurrectFiguresWindow(
                                                        killedFigures, "bialy");
                                                window.setVisible(true);

                                                // Przycisk "Wskrześ"
                                                JButton resurrectButton = new JButton("Wskrześ");
                                                resurrectButton.addActionListener(new ActionListener() {
                                                    public void actionPerformed(ActionEvent e) {
                                                        int selectedFigureIndex = window.getSelectedFigureIndex();
                                                        if (selectedFigureIndex != -1) {
                                                            // Wskrzeszanie wybranej figury
                                                            figura do_wskrzeszenia = sz.zbite_biale[selectedFigureIndex];
                                                            sz.zbite_biale[selectedFigureIndex] = (null);

                                                            figura[] newKilledFigures = new figura[sz.zbite_biale.length];
                                                            int j = 0;
                                                            for (int i = 0; i < sz.zbite_biale.length; i++) {
                                                                if (sz.zbite_biale[i] != null) {
                                                                    newKilledFigures[j++] = sz.zbite_biale[i];
                                                                }
                                                            }
                                                            sz.zbite_biale = newKilledFigures;
                                                            do_wskrzeszenia.pole = sz.getPole(finalCol, finalRow);
                                                            sz.getPole(finalCol, finalRow).setFigure(do_wskrzeszenia);
                                                            if (do_wskrzeszenia instanceof wieza) {
                                                                ImageIcon originalImage4 = new ImageIcon(
                                                                        "images/rook-white.png");
                                                                Image scaledImage4 = originalImage4.getImage()
                                                                        .getScaledInstance(WIDTH, HEIGHT,
                                                                                Image.SCALE_SMOOTH);

                                                                squares[finalRow][finalCol]
                                                                        .setIcon(new ImageIcon(scaledImage4));
                                                            }
                                                            if (do_wskrzeszenia instanceof goniec) {
                                                                ImageIcon originalImage4 = new ImageIcon(
                                                                        "images/bishop-white.png");
                                                                Image scaledImage4 = originalImage4.getImage()
                                                                        .getScaledInstance(WIDTH - 10, HEIGHT - 10,
                                                                                Image.SCALE_SMOOTH);

                                                                squares[finalRow][finalCol]
                                                                        .setIcon(new ImageIcon(scaledImage4));
                                                            }
                                                            if (do_wskrzeszenia instanceof skoczek) {
                                                                ImageIcon originalImage4 = new ImageIcon(
                                                                        "images/knight-white.png");
                                                                Image scaledImage4 = originalImage4.getImage()
                                                                        .getScaledInstance(WIDTH - 10, HEIGHT - 10,
                                                                                Image.SCALE_SMOOTH);

                                                                squares[finalRow][finalCol]
                                                                        .setIcon(new ImageIcon(scaledImage4));
                                                            }
                                                            if (do_wskrzeszenia instanceof hetman) {
                                                                ImageIcon originalImage4 = new ImageIcon(
                                                                        "images/queen-white.png");
                                                                Image scaledImage4 = originalImage4.getImage()
                                                                        .getScaledInstance(WIDTH - 10, HEIGHT - 10,
                                                                                Image.SCALE_SMOOTH);

                                                                squares[finalRow][finalCol]
                                                                        .setIcon(new ImageIcon(scaledImage4));
                                                            }
                                                            window.dispose();
                                                            pole ourking = to_check.getKingSquare(szachownica);
                                                            if (ourking.getFigure()
                                                                    .czy_krol_jest_atakowany(szachownica) == false) {
                                                                int kingrow = ourking.getWysokosc();
                                                                int kingcol = ourking.getSzerokosc();
                                                                squares[kingrow][kingcol].setBackground(
                                                                        originalColors[kingrow][kingcol]);
                                                            }
                                                            pole opposite = to_check.getOppositeKingSquare(szachownica);
                                                            if (opposite.getFigure()
                                                                    .czy_krol_jest_atakowany(szachownica)) {
                                                                int kingrow = opposite.getWysokosc();
                                                                int kingcol = opposite.getSzerokosc();
                                                                squares[kingrow][kingcol].setBackground(Color.RED);
                                                            }
                                                            if (do_wskrzeszenia.szach_mat(szachownica)) {
                                                                Icon icon = new ImageIcon("images/winner.png"); // Replace
                                                                                                                // with
                                                                                                                // the
                                                                                                                // path
                                                                                                                // to
                                                                                                                // your
                                                                Image scaledImage = ((ImageIcon) icon).getImage()
                                                                        .getScaledInstance(45, 45,
                                                                                Image.SCALE_DEFAULT);
                                                                Icon scaledIcon = new ImageIcon(scaledImage);

                                                                String title = "Szach-mat";
                                                                String message = "Gratulacje, gracz "
                                                                        + to_check.getColor() + " wygrał grę!";
                                                                Object[] options = { "OK", "Restart" };
                                                                int defaultOption = 0;

                                                                int choice = JOptionPane.showOptionDialog(null, message,
                                                                        title,
                                                                        JOptionPane.DEFAULT_OPTION,
                                                                        JOptionPane.INFORMATION_MESSAGE, scaledIcon,
                                                                        options,
                                                                        options[defaultOption]);
                                                                if (choice == 1) {
                                                                    dispose(); // Usuwamy aktualne GUI
                                                                    ChessGui newGUI = new ChessGui(); // Tworzymy nowe
                                                                                                      // GUI
                                                                    newGUI.setVisible(true); // Wyświetlamy nowe GUI
                                                                }
                                                            }
                                                            if ((do_wskrzeszenia.stalemate(szachownica, "bialy")
                                                                    || do_wskrzeszenia.stalemate(szachownica, "czarny"))
                                                                    && !do_wskrzeszenia.szach_mat(szachownica)) {
                                                                Icon icon = new ImageIcon("images/winner.png"); // Replace
                                                                                                                // with
                                                                                                                // the
                                                                                                                // path
                                                                                                                // to
                                                                                                                // your
                                                                Image scaledImage = ((ImageIcon) icon).getImage()
                                                                        .getScaledInstance(45, 45,
                                                                                Image.SCALE_DEFAULT);
                                                                Icon scaledIcon = new ImageIcon(scaledImage);

                                                                String title = "Szach-pat";
                                                                String message = "Nie ma żadnych możliwych ruchów - Remis";
                                                                Object[] options = { "OK", "Restart" };
                                                                int defaultOption = 0;

                                                                int choice = JOptionPane.showOptionDialog(null, message,
                                                                        title,
                                                                        JOptionPane.DEFAULT_OPTION,
                                                                        JOptionPane.INFORMATION_MESSAGE, scaledIcon,
                                                                        options,
                                                                        options[defaultOption]);
                                                                if (choice == 1) {
                                                                    dispose(); // Usuwamy aktualne GUI
                                                                    ChessGui newGUI = new ChessGui(); // Tworzymy nowe
                                                                                                      // GUI
                                                                    newGUI.setVisible(true); // Wyświetlamy nowe GUI
                                                                }
                                                            }

                                                        }
                                                    }
                                                });
                                                window.add(resurrectButton, BorderLayout.SOUTH);

                                            }
                                        }
                                        if (to_check.color == "czarny") {
                                            if (finalRow == 7) {
                                                int count = 0;
                                                for (int x = 0; x < 15; x++) {
                                                    if (sz.zbite_czarne[x] != null) {
                                                        count++;
                                                    }
                                                }

                                                int slot = 0;
                                                String[] killedFigures = new String[count];
                                                for (int x = 0; x < 15; x++) {
                                                    if (sz.zbite_czarne[x] != null) {
                                                        killedFigures[slot] = sz.zbite_czarne[x].getClass()
                                                                .getSimpleName();
                                                        slot++;
                                                    }
                                                }

                                                ResurrectFiguresWindow window = new ResurrectFiguresWindow(
                                                        killedFigures, "czarny");
                                                window.setVisible(true);

                                                // Przycisk "Wskrześ"
                                                JButton resurrectButton = new JButton("Wskrześ");
                                                resurrectButton.addActionListener(new ActionListener() {
                                                    public void actionPerformed(ActionEvent e) {
                                                        int selectedFigureIndex = window.getSelectedFigureIndex();
                                                        if (selectedFigureIndex != -1) {
                                                            // Wskrzeszanie wybranej figury
                                                            figura do_wskrzeszenia = sz.zbite_czarne[selectedFigureIndex];
                                                            sz.zbite_czarne[selectedFigureIndex] = (null);

                                                            figura[] newKilledFigures = new figura[sz.zbite_czarne.length];
                                                            int j = 0;
                                                            for (int i = 0; i < sz.zbite_czarne.length; i++) {
                                                                if (sz.zbite_czarne[i] != null) {
                                                                    newKilledFigures[j++] = sz.zbite_czarne[i];
                                                                }
                                                            }
                                                            sz.zbite_czarne = newKilledFigures;
                                                            do_wskrzeszenia.pole = sz.getPole(finalCol, finalRow);
                                                            sz.getPole(finalCol, finalRow).setFigure(do_wskrzeszenia);
                                                            if (do_wskrzeszenia instanceof wieza) {
                                                                ImageIcon originalImage4 = new ImageIcon(
                                                                        "images/rook-black.png");
                                                                Image scaledImage4 = originalImage4.getImage()
                                                                        .getScaledInstance(WIDTH, HEIGHT,
                                                                                Image.SCALE_SMOOTH);

                                                                squares[finalRow][finalCol]
                                                                        .setIcon(new ImageIcon(scaledImage4));
                                                            }
                                                            if (do_wskrzeszenia instanceof goniec) {
                                                                ImageIcon originalImage4 = new ImageIcon(
                                                                        "images/bishop-black.png");
                                                                Image scaledImage4 = originalImage4.getImage()
                                                                        .getScaledInstance(WIDTH - 10, HEIGHT - 10,
                                                                                Image.SCALE_SMOOTH);

                                                                squares[finalRow][finalCol]
                                                                        .setIcon(new ImageIcon(scaledImage4));
                                                            }
                                                            if (do_wskrzeszenia instanceof skoczek) {
                                                                ImageIcon originalImage4 = new ImageIcon(
                                                                        "images/knight-black.png");
                                                                Image scaledImage4 = originalImage4.getImage()
                                                                        .getScaledInstance(WIDTH - 10, HEIGHT - 10,
                                                                                Image.SCALE_SMOOTH);

                                                                squares[finalRow][finalCol]
                                                                        .setIcon(new ImageIcon(scaledImage4));
                                                            }
                                                            if (do_wskrzeszenia instanceof hetman) {
                                                                ImageIcon originalImage4 = new ImageIcon(
                                                                        "images/queen-black.png");
                                                                Image scaledImage4 = originalImage4.getImage()
                                                                        .getScaledInstance(WIDTH - 10, HEIGHT - 10,
                                                                                Image.SCALE_SMOOTH);

                                                                squares[finalRow][finalCol]
                                                                        .setIcon(new ImageIcon(scaledImage4));
                                                            }
                                                            window.dispose();
                                                            pole ourking = to_check.getKingSquare(szachownica);
                                                            if (ourking.getFigure()
                                                                    .czy_krol_jest_atakowany(szachownica) == false) {
                                                                int kingrow = ourking.getWysokosc();
                                                                int kingcol = ourking.getSzerokosc();
                                                                squares[kingrow][kingcol].setBackground(
                                                                        originalColors[kingrow][kingcol]);
                                                            }
                                                            pole opposite = to_check.getOppositeKingSquare(szachownica);
                                                            if (opposite.getFigure()
                                                                    .czy_krol_jest_atakowany(szachownica)) {
                                                                int kingrow = opposite.getWysokosc();
                                                                int kingcol = opposite.getSzerokosc();
                                                                squares[kingrow][kingcol].setBackground(Color.RED);
                                                            }
                                                            if (do_wskrzeszenia.szach_mat(szachownica)) {
                                                                Icon icon = new ImageIcon("images/winner.png");
                                                                Image scaledImage = ((ImageIcon) icon).getImage()
                                                                        .getScaledInstance(45, 45,
                                                                                Image.SCALE_DEFAULT);
                                                                Icon scaledIcon = new ImageIcon(scaledImage);

                                                                String title = "Szach-mat";
                                                                String message = "Gratulacje, gracz "
                                                                        + to_check.getColor() + " wygrał grę!";
                                                                Object[] options = { "OK", "Restart" };
                                                                int defaultOption = 0;

                                                                int choice = JOptionPane.showOptionDialog(null, message,
                                                                        title,
                                                                        JOptionPane.DEFAULT_OPTION,
                                                                        JOptionPane.INFORMATION_MESSAGE, scaledIcon,
                                                                        options,
                                                                        options[defaultOption]);
                                                                if (choice == 1) {
                                                                    dispose(); // Usuwamy aktualne GUI
                                                                    ChessGui newGUI = new ChessGui(); // Tworzymy nowe
                                                                                                      // GUI
                                                                    newGUI.setVisible(true); // Wyświetlamy nowe GUI
                                                                }
                                                            }
                                                            if ((do_wskrzeszenia.stalemate(szachownica, "bialy")
                                                                    || do_wskrzeszenia.stalemate(szachownica, "czarny"))
                                                                    && !do_wskrzeszenia.szach_mat(szachownica)) {
                                                                Icon icon = new ImageIcon("images/winner.png"); // Replace
                                                                                                                // with
                                                                                                                // the
                                                                                                                // path
                                                                                                                // to
                                                                                                                // your
                                                                Image scaledImage = ((ImageIcon) icon).getImage()
                                                                        .getScaledInstance(45, 45,
                                                                                Image.SCALE_DEFAULT);
                                                                Icon scaledIcon = new ImageIcon(scaledImage);

                                                                String title = "Szach-pat";
                                                                String message = "Nie ma żadnych możliwych ruchów - Remis";
                                                                Object[] options = { "OK", "Restart" };
                                                                int defaultOption = 0;

                                                                int choice = JOptionPane.showOptionDialog(null, message,
                                                                        title,
                                                                        JOptionPane.DEFAULT_OPTION,
                                                                        JOptionPane.INFORMATION_MESSAGE, scaledIcon,
                                                                        options,
                                                                        options[defaultOption]);
                                                                if (choice == 1) {
                                                                    dispose(); // Usuwamy aktualne GUI
                                                                    ChessGui newGUI = new ChessGui(); // Tworzymy nowe
                                                                                                      // GUI
                                                                    newGUI.setVisible(true); // Wyświetlamy nowe GUI
                                                                }
                                                            }
                                                        }
                                                    }
                                                });
                                                window.add(resurrectButton, BorderLayout.SOUTH);
                                            }
                                        }
                                    }
                                    if (to_check.szach_mat(szachownica)) {
                                        Icon icon = new ImageIcon("images/winner.png"); // Replace with the path to your
                                        Image scaledImage = ((ImageIcon) icon).getImage().getScaledInstance(45, 45,
                                                Image.SCALE_DEFAULT);
                                        Icon scaledIcon = new ImageIcon(scaledImage);

                                        String title = "Szach-mat";
                                        String message = "Gratulacje, gracz " + to_check.getColor() + " wygrał grę!";
                                        Object[] options = { "OK", "Restart" };
                                        int defaultOption = 0;

                                        int choice = JOptionPane.showOptionDialog(null, message, title,
                                                JOptionPane.DEFAULT_OPTION,
                                                JOptionPane.INFORMATION_MESSAGE, scaledIcon, options,
                                                options[defaultOption]);
                                        if (choice == 1) {
                                            dispose(); // Usuwamy aktualne GUI
                                            ChessGui newGUI = new ChessGui(); // Tworzymy nowe GUI
                                            newGUI.setVisible(true); // Wyświetlamy nowe GUI
                                        }
                                    }
                                    if ((to_check.stalemate(szachownica, "bialy")
                                            || to_check.stalemate(szachownica, "czarny"))
                                            && !to_check.szach_mat(szachownica)) {
                                        Icon icon = new ImageIcon("images/winner.png"); // Replace with the path to your
                                        Image scaledImage = ((ImageIcon) icon).getImage().getScaledInstance(45, 45,
                                                Image.SCALE_DEFAULT);
                                        Icon scaledIcon = new ImageIcon(scaledImage);

                                        String title = "Szach-pat";
                                        String message = "Nie ma żadnych możliwych ruchów - Remis";
                                        Object[] options = { "OK", "Restart" };
                                        int defaultOption = 0;

                                        int choice = JOptionPane.showOptionDialog(null, message, title,
                                                JOptionPane.DEFAULT_OPTION,
                                                JOptionPane.INFORMATION_MESSAGE, scaledIcon, options,
                                                options[defaultOption]);
                                        if (choice == 1) {
                                            dispose(); // Usuwamy aktualne GUI
                                            ChessGui newGUI = new ChessGui(); // Tworzymy nowe GUI
                                            newGUI.setVisible(true); // Wyświetlamy nowe GUI
                                        }
                                    }
                                    if (recentPlayer == "bialy") {
                                        recentPlayer = "czarny";
                                    } else {
                                        recentPlayer = "bialy";
                                    }
                                    this.clearfield(szachownica);
                                } else {
                                    for (int i = 0; i < 8; i++) {
                                        for (int j = 0; j < 8; j++) {
                                            squares[i][j].setBackground(originalColors[i][j]);
                                        }
                                    }
                                    Icon icon = new ImageIcon("images/battle.png");
                                    Image scaledImage = ((ImageIcon) icon).getImage().getScaledInstance(40, 40,
                                            Image.SCALE_DEFAULT);
                                    Icon scaledIcon = new ImageIcon(scaledImage);

                                    String title = "Ruch niemożliwy";
                                    String message = "Król jest atakowany!";
                                    Object[] options = { "OK" };
                                    int defaultOption = 0;

                                    JLabel messageLabel = new JLabel(message);
                                    messageLabel.setHorizontalAlignment(JLabel.CENTER);
                                    JPanel messagePanel = new JPanel(new BorderLayout());
                                    messagePanel.add(messageLabel, BorderLayout.CENTER);
                                    JOptionPane.showOptionDialog(null, messagePanel, title, JOptionPane.DEFAULT_OPTION,
                                            JOptionPane.INFORMATION_MESSAGE, scaledIcon, options,
                                            options[defaultOption]);

                                    pole ourking = to_check.getKingSquare(szachownica);
                                    if (ourking.getFigure().czy_krol_jest_atakowany(szachownica)) {
                                        int kingrow = ourking.getWysokosc();
                                        int kingcol = ourking.getSzerokosc();
                                        squares[kingrow][kingcol].setBackground(Color.RED);
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
                                pole opposite = to_check.getOppositeKingSquare(szachownica);
                                if (opposite.getFigure().czy_krol_jest_atakowany(szachownica)) {
                                    int kingrow = opposite.getWysokosc();
                                    int kingcol = opposite.getSzerokosc();
                                    squares[kingrow][kingcol].setBackground(Color.RED);
                                }

                                pole ourking = to_check.getKingSquare(szachownica);
                                if (ourking.getFigure().czy_krol_jest_atakowany(szachownica)) {
                                    int kingrow = ourking.getWysokosc();
                                    int kingcol = ourking.getSzerokosc();
                                    squares[kingrow][kingcol].setBackground(Color.RED);
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

                    public void clearfield(pole[][] szachownica) {
                        for (int x = 0; x < 8; x++) {
                            for (int y = 0; y < 8; y++) {
                                if (szachownica[x][y].getFigure() == null) {
                                    squares[x][y].setIcon(null);
                                }
                            }
                        }
                    }
                });
            }
        }

        // Dodaj etykiety poziome do planszy
        // Create the button
        JButton settings = new JButton();
        settings.setSize(70, 70);
        settings.setOpaque(false);
        settings.setContentAreaFilled(false);
        settings.setBorderPainted(false);
        ImageIcon original = new ImageIcon("images/settings.png");
        Image scaled = original.getImage().getScaledInstance(WIDTH - 35, HEIGHT - 35, Image.SCALE_SMOOTH);
        settings.setIcon(new ImageIcon(scaled));
        settings.setFocusPainted(false);

        settings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame newWindow = new JFrame();
                newWindow.setTitle("Ustawienia");
                newWindow.setSize(280, 420);
                newWindow.setVisible(true);
                newWindow.setLocationRelativeTo(null);
                newWindow.setResizable(false);

                JLabel settingsLabel = new JLabel();
                ImageIcon original2 = new ImageIcon("images/settings.png");
                Image scaled2 = original2.getImage().getScaledInstance(WIDTH + 20, HEIGHT + 20, Image.SCALE_SMOOTH);
                settingsLabel.setIcon(new ImageIcon(scaled2));

                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new GridLayout(0, 1, 80, 15));
                JButton button1 = new JButton("Zmiana motywu");
                JButton button2 = new JButton("Restart");
                JButton button3 = new JButton("Zakończ grę");
                button1.setFocusPainted(false);
                button2.setFocusPainted(false);
                button3.setFocusPainted(false);
                button1.setBorderPainted(false);
                button2.setBorderPainted(false);
                button3.setBorderPainted(false);
                button1.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        for (int row = 0; row < 8; row++) {
                            for (int col = 0; col < 8; col++) {
                                if ((row + col) % 2 == 0) {
                                    if(squares[row][col].getBackground() == Color.LIGHT_GRAY){
                                        squares[row][col].setBackground(new Color(250, 202, 138));
                                        originalColors[row][col] = squares[row][col].getBackground();
                                    }else{
                                        squares[row][col].setBackground(Color.LIGHT_GRAY);
                                        originalColors[row][col] = squares[row][col].getBackground();
                                    }
                                } else {
                                    if(squares[row][col].getBackground() == Color.DARK_GRAY){
                                        squares[row][col].setBackground(new Color(127, 75, 36));
                                        originalColors[row][col] = squares[row][col].getBackground();
                                    }else{
                                        squares[row][col].setBackground(Color.DARK_GRAY);
                                        originalColors[row][col] = squares[row][col].getBackground();
                                    }
                                }
                            }
                        }
                        newWindow.dispose();
                    }
                });
                button2.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                        newWindow.dispose();
                        dispose();
                        ChessGui newGUI = new ChessGui();
                        newGUI.setVisible(true);
                    }
                });
                button3.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                        newWindow.dispose();
                        dispose();
                    }
                });
                buttonPanel.add(button1);
                buttonPanel.add(button2);
                buttonPanel.add(button3);

                // Create a new JLabel for the text
                JLabel bottomLabel = new JLabel("Autor: Kacper Fryt");

                // Create a new GridBagLayout and set it as the layout manager for the new
                // window
                GridBagLayout layout = new GridBagLayout();
                newWindow.setLayout(layout);

                int topMargin = 20;

                GridBagConstraints gbcLabel = new GridBagConstraints();
                gbcLabel.gridx = 0;
                gbcLabel.gridy = 0;
                gbcLabel.anchor = GridBagConstraints.CENTER;
                gbcLabel.insets = new Insets(topMargin, 0, 0, 0);

                newWindow.add(settingsLabel, gbcLabel);

                GridBagConstraints gbcPanel = new GridBagConstraints();
                gbcPanel.gridx = 0;
                gbcPanel.gridy = 1;
                gbcPanel.anchor = GridBagConstraints.CENTER;
                gbcPanel.insets = new Insets(10, 0, 10, 0);

                newWindow.add(buttonPanel, gbcPanel);

                // Create a new GridBagConstraints object and set its properties for the bottom
                // label
                GridBagConstraints gbcBottom = new GridBagConstraints();
                gbcBottom.gridx = 0;
                gbcBottom.gridy = 2;
                gbcBottom.anchor = GridBagConstraints.PAGE_END;
                gbcBottom.insets = new Insets(10, 0, 20, 0);

                // Add the bottom label to the new window with the specified constraints
                newWindow.add(bottomLabel, gbcBottom);

            }
        });
        chessBoard.add(settings);

        for (int i = 0; i < 8; i++) {
            JLabel label = new JLabel(String.valueOf((char) ('A' + i)), JLabel.CENTER);
            label.setFont(new Font("Century Gothic Bold", Font.PLAIN, 18));
            label.setForeground(Color.LIGHT_GRAY);
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
