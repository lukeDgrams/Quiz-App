/* creates quiz objects to be inserted into table*/

package sample.classes;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class quizOption {
    private String QuizName;
    private String QuizCreator;
    private int QuestionCount, ID;
    private Button Take = new Button();
    public quizOption(String name, String creator, int count, int Id){
        QuizName = name;
        QuizCreator = creator;
        QuestionCount = count;
        ID = Id;
        this.Take.setText("Take");
        this.Take.setId(name);
    }

    public String getQuizName() {
        return QuizName;
    }

    public void setQuizName(String quizName) {
        QuizName = quizName;
    }

    public String getQuizCreator() {
        return QuizCreator;
    }

    public void setQuizCreator(String quizCreator) {
        QuizCreator = quizCreator;
    }

    public int getQuestionCount() {
        return QuestionCount;
    }

    public void setQuestionCount(int questionCount) {
        QuestionCount = questionCount;
    }

    public Button getTake() {
        return Take;
    }
    public int getID(){
        return ID;
    }


    public void setTake(Button take) {
        Take = take;
    }
    public void setAction(EventHandler<ActionEvent> e) {
        this.Take.setOnAction(e);
    }
}
