package View;

// QuizView.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import Controller.QuizController;
import Model.Question;

public class QuizView extends JFrame {
    private JLabel questionLabel;
    private JRadioButton[] answerOptions;
    private JButton startButton;
    private JButton nextButton;
    private JTextField nameField;
    private JButton saveButton;
    private JButton deleteButton;
    private QuizController controller;
    private String playerName;

    public QuizView() {
        setTitle("Knowledge");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        add(mainPanel);

        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        mainPanel.add(questionPanel);

        questionLabel = new JLabel();
        questionPanel.add(questionLabel);

        JPanel answerPanel = new JPanel();
        answerPanel.setLayout(new GridLayout(4, 1));
        mainPanel.add(answerPanel);

        answerOptions = new JRadioButton[4];
        for (int i = 0; i < 4; i++) {
            answerOptions[i] = new JRadioButton();
            answerPanel.add(answerOptions[i]);
        }

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        mainPanel.add(buttonPanel);

        startButton = new JButton("Start");
        buttonPanel.add(startButton);

        nextButton = new JButton("Next");
        nextButton.setEnabled(false);
        buttonPanel.add(nextButton);

        JPanel namePanel = new JPanel();
        namePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        mainPanel.add(namePanel);

        JLabel nameLabel = new JLabel("Player Name:");
        namePanel.add(nameLabel);

        nameField = new JTextField(20);
        namePanel.add(nameField);

        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        mainPanel.add(scorePanel);

        saveButton = new JButton("Save Score");
        saveButton.setEnabled(false);
        scorePanel.add(saveButton);

        deleteButton = new JButton("Delete Score");
        deleteButton.setEnabled(false);
        scorePanel.add(deleteButton);

        pack();
        setSize(600, 400);
        setLocationRelativeTo(null);
    }

    public JLabel getQuestionLabel() {
        return questionLabel;
    }

    public JRadioButton[] getAnswerOptions() {
        return answerOptions;
    }

    public JButton getStartButton() {
        return startButton;
    }

    public JButton getNextButton() {
        return nextButton;
    }

    public JTextField getNameField() {
        return nameField;
    }

    public JButton getSaveButton() {
        return saveButton;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }

    public void setController(QuizController controller) {
        this.controller = controller;
    }

    public void showQuestion() {
        if (controller.getCurrentQuestionIndex() < controller.getQuestions().size()) {
            Question question = controller.getQuestions().get(controller.getCurrentQuestionIndex());
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
            showFinalScore();
        }
    }

    public void showFinalScore() {
        questionLabel.setText("Quiz Completed! Your Score: " + controller.getCorrectAnswers());
        startButton.setEnabled(true);
        nameField.setEditable(true);
        nextButton.setEnabled(false);
        saveButton.setEnabled(true);
        deleteButton.setEnabled(true);
    }
}