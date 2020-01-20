/* creates question objects so they can be inserted into table*/

package sample.classes;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class addQuestion {
    private static int ID;
    private int Id;
    private String Question;
    private String Correct;
    private String AnswerOne;
    private String AnswerTwo;
    private String AnswerThree;
    private String AnswerFour;
    private Button Remove = new Button();
    private int Code;
    public addQuestion(String quest, String right, String one, String two, String three, String four){
        ID++;
        Id = ID;
        setCorrect(right);
        setQuestion(quest);
        setAnswerOne(one);
        setAnswerTwo(two);
        setAnswerThree(three);
        setAnswerFour(four);
        setCode(1);
        Remove.setText("Remove");
        this.Remove.setId(String.valueOf(Id));
    }
    public addQuestion(String quest, String right, String one, String two){
        ID++;
        Id = ID;
        setCorrect(right);
        setQuestion(quest);
        setAnswerOne(one);
        setAnswerTwo(two);
        setCode(2);
        this.Remove.setText("Remove");
        this.Remove.setId(String.valueOf(Id));
    }
    public addQuestion(String quest, String right){
        ID++;
        Id = ID;
        setCorrect(right);
        setQuestion(quest);
        setCode(3);
        Remove.setText("Remove");
        this.Remove.setId(String.valueOf(Id));
    }

    public int getId() {
        return Id;
    }

    public String getQuestion() {
        return Question;
    }

    public String getCorrect() {
        return Correct;
    }

    public String getAnswerOne() {
        return AnswerOne;
    }

    public String getAnswerTwo() {
        return AnswerTwo;
    }

    public String getAnswerThree() {
        return AnswerThree;
    }

    public String getAnswerFour() {
        return AnswerFour;
    }

    public Button getRemove() {
        return Remove;
    }

    public int getCode() {
        return Code;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public void setCorrect(String correct) {
        this.Correct = correct;
    }

    public void setAnswerOne(String answerOne) {
        this.AnswerOne = answerOne;
    }

    public void setAnswerTwo(String answerTwo) {
        this.AnswerTwo = answerTwo;
    }

    public void setAnswerThree(String answerThree) {
        this.AnswerThree = answerThree;
    }

    public void setAnswerFour(String answerFour) {
        this.AnswerFour = answerFour;
    }

    public void setRemove(Button remove) {
        this.Remove = remove;
    }

    public void setCode(int code) {
        this.Code = code;
    }
    public void setAction(EventHandler<ActionEvent> e) {
        this.Remove.setOnAction(e);
    }
}
