package Controller;

// QuizController.java
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import Model.Question;
import View.QuizView;

import javax.swing.*;

public class QuizController implements ActionListener {
    private QuizView view;
    private ArrayList<Question> questions;
    private int currentQuestionIndex;
    private int correctAnswers;
    private String playerName;

    public QuizController(QuizView view) {
        this.view = view;
        this.view.getStartButton().addActionListener(this);
        this.view.getNextButton().addActionListener(this);
        this.view.getSaveButton().addActionListener(this);
        this.view.getDeleteButton().addActionListener(this);
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void loadQuestionsFromFile() {
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

    public void startNewGame() {
        loadQuestionsFromFile();
        Collections.shuffle(questions);
        currentQuestionIndex = 0;
        correctAnswers = 0;
        view.showQuestion();
        view.getStartButton().setEnabled(false);
        view.getNameField().setEditable(false);
        view.getSaveButton().setEnabled(false);
        view.getDeleteButton().setEnabled(false);
        view.getNextButton().setEnabled(true);
    }

    public void processNext() {
        Question question = questions.get(currentQuestionIndex);
        boolean isAnswerCorrect = false;

        for (int i = 0; i < 4; i++) {
            if (view.getAnswerOptions()[i].isSelected() && view.getAnswerOptions()[i].getText().equals(question.getCorrectAnswer())) {
                correctAnswers++;
                isAnswerCorrect = true;
                break;
            }
        }

        currentQuestionIndex++;

        if (!isAnswerCorrect) {
            JOptionPane.showMessageDialog(view, "Wrong answer! Game over.");
            view.getStartButton().setEnabled(true);
            view.getNameField().setEditable(true);
            view.getSaveButton().setEnabled(true);
            view.getDeleteButton().setEnabled(true);
            view.getNextButton().setEnabled(false);
        } else {
            if (currentQuestionIndex >= 10) {
                JOptionPane.showMessageDialog(view, "Quiz completed! You answered " + correctAnswers + " questions correctly.");
                view.getStartButton().setEnabled(true);
                view.getNameField().setEditable(true);
                view.getSaveButton().setEnabled(true);
                view.getDeleteButton().setEnabled(true);
                view.getNextButton().setEnabled(false);
            } else {
                view.showQuestion();
            }
        }
    }

    public void saveScore() {
        playerName = view.getNameField().getText();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/scores.txt", true))) {
            writer.write(playerName + ";" + correctAnswers);
            writer.newLine();
            writer.flush();
            JOptionPane.showMessageDialog(view, "Score saved successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Failed to save score.");
        }
    }

    public void deleteScore() {
        String confirmationMessage = "Are you sure you want to delete your score?";
        int confirmationResult = JOptionPane.showConfirmDialog(view, confirmationMessage, "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirmationResult == JOptionPane.YES_OPTION) {
            // Delete score logic
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.getStartButton()) {
            startNewGame();
        } else if (e.getSource() == view.getNextButton()) {
            processNext();
        } else if (e.getSource() == view.getSaveButton()) {
            saveScore();
        } else if (e.getSource() == view.getDeleteButton()) {
            deleteScore();
        }
    }

    public static void main(String[] args) {
        QuizView view = new QuizView();
        QuizController controller = new QuizController(view);
        view.setController(controller);
        view.setVisible(true);
    }
}