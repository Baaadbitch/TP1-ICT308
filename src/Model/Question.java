package Model;

// Question.java
public class Question {
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
