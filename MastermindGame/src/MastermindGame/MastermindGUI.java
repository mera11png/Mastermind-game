package MastermindGame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class MastermindGame {
    private List<String> secretCode;
    private final int maxAttempts = 10;
    private int attempts;
    private final String[] colors = {"Red", "Blue", "Green", "Yellow", "Orange", "Purple"};

    public MastermindGame() {
        this.secretCode = this.generateSecretCode();
        this.attempts = 0;
    }

    private List<String> generateSecretCode() {
        List<String> code = new ArrayList<>();
        List<String> colorsList = new ArrayList<>(Arrays.asList(this.colors));
        Collections.shuffle(colorsList);
        code.addAll(colorsList);
        System.out.println(code);
        return code;
    }

    public List<String> getSecretCode() {
        return this.secretCode;
    }

    public boolean isGameOver() {
        return this.attempts >= this.maxAttempts;
    }

    public String checkGuess(List<String> guess) {
        this.attempts++;
        if (guess.equals(this.secretCode)) {
            return "Correct! You win!";
        }

        int correctPositions = 0;
        int correctColors = 0;

        boolean[] secretMatched = new boolean[6];
        boolean[] guessMatched = new boolean[6];

        for (int i = 0; i < 6; i++) {
            if (guess.get(i).equals(this.secretCode.get(i))) {
                correctPositions++;
                secretMatched[i] = true;
                guessMatched[i] = true;
            }
        }
        for (int i = 0; i < 6; i++) {
            if (!guessMatched[i]) {
                for (int j = 0; j < 6; j++) {
                    if (!secretMatched[j] && guess.get(i).equals(this.secretCode.get(j))) {
                        correctColors++;
                        secretMatched[j] = true;
                        break;
                    }
                }
            }
        }

        return "Correct Positions: " + correctPositions + ", Correct Colors: " + correctColors;
    }
}
//GUI
public class MastermindGUI extends JFrame {
    private MastermindGame game;
    private JPanel colorPanel;
    private JTextArea resultArea;
    private JButton[] colorButtons;
    private List<String> currentGuess;

    public MastermindGUI() {
        this.game = new MastermindGame();
        this.currentGuess = new ArrayList<>();
        
        setTitle("Mastermind Game");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        this.colorPanel = new JPanel();
        this.colorPanel.setLayout(new FlowLayout());
        add(colorPanel, BorderLayout.NORTH);

        this.resultArea = new JTextArea();
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        add(controlPanel, BorderLayout.SOUTH);

        String[] colors = {"Red", "Blue", "Green", "Yellow", "Orange", "Purple"};
        colorButtons = new JButton[colors.length];

        for (int i = 0; i < colors.length; i++) {
            String color = colors[i];
            JButton button = new JButton(color);
            button.setBackground(getColor(color));
            button.setOpaque(true);
            button.setBorderPainted(false);
            button.addActionListener(e -> addColorToGuess(color));
            colorButtons[i] = button;
            controlPanel.add(button);
        }

        JButton submitButton = new JButton("Submit Guess");
        submitButton.addActionListener(new SubmitGuessListener());
        controlPanel.add(submitButton);
    }

    private Color getColor(String color) {
    if ("Red".equals(color)) {
        return Color.RED;
    } else if ("Blue".equals(color)) {
        return Color.BLUE;
    } else if ("Green".equals(color)) {
        return Color.GREEN;
    } else if ("Yellow".equals(color)) {
        return Color.YELLOW;
    } else if ("Orange".equals(color)) {
        return Color.ORANGE;
    } else {
        return new Color(128, 0, 139);
    }
}

    private void addColorToGuess(String color) {
        if (currentGuess.size() < 6) {
            currentGuess.add(color);
            JLabel colorLabel = new JLabel(color);
            colorLabel.setBackground(getColor(color));
            colorLabel.setOpaque(true);
            colorPanel.add(colorLabel);
            colorPanel.revalidate();
            colorPanel.repaint();
        }
    }

    private class SubmitGuessListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentGuess.size() != 6) {
                resultArea.append("You must select 6 colors before submitting.\n");
                return;
            }

            String result = game.checkGuess(currentGuess);
            resultArea.append("Guess: " + currentGuess + "\n" + result + "\n");
            currentGuess.clear();
            colorPanel.removeAll();
            colorPanel.revalidate();
            colorPanel.repaint();

            if (result.contains("Correct! You win!")) {
                JOptionPane.showMessageDialog(null, "Congratulations! You've cracked the code!");
                System.exit(0);
            } else if (game.isGameOver()) {
                JOptionPane.showMessageDialog(null, "Game Over! The correct code was: " + game.getSecretCode());
                System.exit(0);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MastermindGUI gui = new MastermindGUI();
            gui.setVisible(true);
        });
    }
}