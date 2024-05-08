import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class QuizApplication extends JFrame {
    private ArrayList<Question> questions;
    private int currentQuestionIndex;
    private int correctAnswers;
    private String playerName;

    private JLabel questionLabel;
    private JRadioButton[] answerOptions;
    private JButton nextButton;
    private JButton startButton;
    private JTextField nameField;
    private JButton saveButton;
    private JButton deleteButton;

    public QuizApplication() {
        setTitle("Knowledge");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel startPanel = new JPanel();
        startPanel.setLayout(new FlowLayout());
        JLabel nameLabel = new JLabel("fill your name:");
        nameField = new JTextField(20);
        startButton = new JButton("Start game");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerName = nameField.getText();
                startNewGame();
            }
        });
        startPanel.add(nameLabel);
        startPanel.add(nameField);
        startPanel.add(startButton);
        add(startPanel, BorderLayout.NORTH);

        JPanel quizPanel = new JPanel();
        quizPanel.setLayout(new BorderLayout());

        questionLabel = new JLabel();
        quizPanel.add(questionLabel, BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(0, 1));
        answerOptions = new JRadioButton[4];
        ButtonGroup buttonGroup = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            answerOptions[i] = new JRadioButton();
            optionsPanel.add(answerOptions[i]);
            buttonGroup.add(answerOptions[i]);
        }
        quizPanel.add(optionsPanel, BorderLayout.CENTER);

        nextButton = new JButton("Next");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processNext();
            }
        });
        quizPanel.add(nextButton, BorderLayout.SOUTH);

        add(quizPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        saveButton = new JButton("Save Score");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveScore();
            }
        });
        deleteButton = new JButton("Delete Score");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteScore();
            }
        });
        controlPanel.add(saveButton);
        controlPanel.add(deleteButton);
        add(controlPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);

        loadQuestionsFromFile();
    }

    private void loadQuestionsFromFile() {
        questions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/questions.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 5) {
                    String question = parts[0];
                    String correctAnswer = parts[1];
                    String distractor1 = parts[2];
                    String distractor2 = parts[3];
                    String distractor3 = parts[4];
                    Question q = new Question(question, correctAnswer, distractor1, distractor2, distractor3);
                    questions.add(q);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startNewGame() {
        loadQuestionsFromFile(); // Charger les questions avant de commencer le jeu
        Collections.shuffle(questions);
        currentQuestionIndex = 0;
        correctAnswers = 0;
        showQuestion();
        startButton.setEnabled(false);
        nameField.setEditable(false);
        saveButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    private void showQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Question question = questions.get(currentQuestionIndex);
            questionLabel.setText(question.getQuestion());

            ArrayList<String> options = new ArrayList<>();
            options.add(question.getCorrectAnswer());
            options.add(question.getDistractor1());
            options.add(question.getDistractor2());
            options.add(question.getDistractor3());
            Collections.shuffle(options);

            for (int i = 0; i < 4; i++) {
                answerOptions[i].setText(options.get(i));
                answerOptions[i].setSelected(false);
            }
        } else {
            // No more questions, end the game
            JOptionPane.showMessageDialog(this, "Game Over! Your score: " + correctAnswers);
            saveButton.setEnabled(true);
            deleteButton.setEnabled(true);
            startButton.setEnabled(true);
            nameField.setEditable(true);
        }
    }

    private void processNext(){
        Question question = questions.get(currentQuestionIndex);
        for (int i = 0; i < 4; i++) {
            if (answerOptions[i].isSelected() && answerOptions[i].getText().equals(question.getCorrectAnswer())) {
                correctAnswers++;
                break;
            }
        }
        currentQuestionIndex++;
        showQuestion();
    }

    private void saveScore() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/scores.txt", true))) {
            writer.write(playerName + ";" + correctAnswers);
            writer.newLine();
            writer.flush();
            JOptionPane.showMessageDialog(this, "Score saved successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to save score.");
        }
    }

    private void deleteScore() {
        String confirmationMessage = "Are you sure you want to delete your score?";
        int confirmationResult = JOptionPane.showConfirmDialog(this, confirmationMessage, "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirmationResult == JOptionPane.YES_OPTION) {
            try {
                File inputFile = new File("src/scores.txt");
                File tempFile = new File("src/temp.txt");

                BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

                String lineToRemove = playerName + ";" + correctAnswers;
                String currentLine;

                while ((currentLine = reader.readLine()) != null) {
                    String trimmedLine = currentLine.trim();
                    if (!trimmedLine.equals(lineToRemove)) {
                        writer.write(currentLine);
                        writer.newLine();
                    }
                }
                writer.close();
                reader.close();

                if (inputFile.delete() && tempFile.renameTo(inputFile)) {
                    JOptionPane.showMessageDialog(this, "Score deleted successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete score.");
                }
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to delete score.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new QuizApplication().setVisible(true);
            }
        });
    }
}

class Question {
    private String question;
    private String correctAnswer;
    private String distractor1;
    private String distractor2;
    private String distractor3;

    public Question(String question, String correctAnswer, String distractor1, String distractor2, String distractor3) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.distractor1 = distractor1;
        this.distractor2 = distractor2;
        this.distractor3 = distractor3;
    }

    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getDistractor1() {
        return distractor1;
    }

    public String getDistractor2() {
        return distractor2;
    }

    public String getDistractor3() {
        return distractor3;
    }
}