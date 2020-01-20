/* controls takeQuiz.fxml
   displays and records answers for quiz
 */

package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.Main;
import sample.classes.ConnectionClass;
import sample.classes.addQuestion;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class quizController {
    @FXML
    public RadioButton A;
    public RadioButton B;
    public RadioButton C;
    public RadioButton D;
    public Text txtA;
    public Text txtB;
    public Text txtC;
    public Text txtD;
    public Button next;
    public Button prev;
    public Label question;
    public ImageView profilePic;
    public Label userName;
    public TextArea shortAnswer;
    public ToggleGroup answerGroup = new ToggleGroup();

    public Connection connection;
    public ResultSet allUsers, allQuestions, multAnswers;
    public ObservableList<addQuestion> questionListUser = FXCollections.observableArrayList();
    public Stage stage;
    public Parent root;
    public String one, two, three, four;
    public int count = 1;
    public int countTwo = 1;
    public Boolean[] correctList = new Boolean[25];
    public String choice;

    /* sets up display */
    public void initialize(){
        ConnectionClass connectionClass = new ConnectionClass();
        connection = connectionClass.getConnection();
        try {
            allUsers = connection.prepareStatement("select * from user").executeQuery();
            while (allUsers.next()) {
                if (Main.userID == allUsers.getInt(1)) {
                    File file = new File(allUsers.getString(5));
                    Image image = new Image(file.toURI().toString());
                    profilePic.setImage(image);
                    userName.setText(allUsers.getString(2));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        A.setToggleGroup(answerGroup);
        B.setToggleGroup(answerGroup);
        C.setToggleGroup(answerGroup);
        D.setToggleGroup(answerGroup);

        getQuestions();
        display();
    }

    /* gets question code and sets display based on code*/
    public void display(){
        countTwo = 1;
        questionListUser.forEach((addQuestion questionTmp) -> {
            if(count == countTwo) {
                addQuestion tmp = questionTmp;
                if (tmp.getCode() == 1) {
                    mult(tmp);
                } else if (tmp.getCode() == 2) {
                    tf(tmp);
                } else {
                    shortAns(tmp);
                }
            }
            countTwo++;
        });
    }

    /* gets quiz questions*/
    public void getQuestions(){

        try{
            allQuestions = connection.prepareStatement("select * from questions where quiz_ID ='"+ Main.userQuizID + "'").executeQuery();
            while(allQuestions.next()){
                //code = allQuestions.getInt(4);
                if(allQuestions.getInt(4) == 1){
                    System.out.println("checkOne");
                    multAnswers = connection.prepareStatement("select * from type_mult where question_ID ='"+ allQuestions.getInt(1)+ "'").executeQuery();
                    while(multAnswers.next()){
                        one = multAnswers.getString(2);
                        two = multAnswers.getString(3);
                        three = multAnswers.getString(4);
                        four = multAnswers.getString(5);
                    }
                    questionListUser.add(new addQuestion(allQuestions.getString(3), allQuestions.getString(5), one, two, three, four));
                }
                else if( allQuestions.getInt(4) == 2){
                    questionListUser.add(new addQuestion(allQuestions.getString(3), allQuestions.getString(5), "True", "False"));

                }
                else{
                    questionListUser.add(new addQuestion(allQuestions.getString(3), allQuestions.getString(5)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*sets display for true false question*/
    public void tf(addQuestion tmp){
        A.setVisible(true);
        B.setVisible(true);
        C.setVisible(false);
        D.setVisible(false);
        txtA.setVisible(false);
        txtB.setVisible(false);
        txtC.setVisible(false);
        txtD.setVisible(false);
        shortAnswer.setVisible(false);
        A.setText("True");
        B.setText("False");
        question.setText(tmp.getQuestion());
        answerGroup.selectToggle(null);

    }

    /*sets display for short answer question*/
    public void shortAns(addQuestion tmp){
        A.setVisible(false);
        B.setVisible(false);
        C.setVisible(false);
        D.setVisible(false);
        txtA.setVisible(false);
        txtB.setVisible(false);
        txtC.setVisible(false);
        txtD.setVisible(false);
        shortAnswer.setVisible(true);
        question.setText(tmp.getQuestion());
        answerGroup.selectToggle(null);
    }

    /* sets display for multiple choice question*/
    public void mult(addQuestion tmp){
        System.out.println("check five");
        A.setVisible(true);
        B.setVisible(true);
        C.setVisible(true);
        D.setVisible(true);
        txtA.setVisible(true);
        txtB.setVisible(true);
        txtC.setVisible(true);
        txtD.setVisible(true);
        shortAnswer.setVisible(false);
        question.setText(tmp.getQuestion());
        A.setText("A");
        B.setText("B");
        C.setText("C");
        D.setText("D");
        txtA.setText(tmp.getAnswerOne());
        txtB.setText(tmp.getAnswerTwo());
        txtC.setText(tmp.getAnswerThree());
        txtD.setText(tmp.getAnswerFour());
        answerGroup.selectToggle(null);

    }

    /* next question button*/
    public void onNext() throws Exception {
        if(count == questionListUser.size()){
            getRight();
            for(int i = 0; i < questionListUser.size(); i++){
                System.out.println(correctList[i]);
            }
            getScore();
            connection.prepareStatement("INSERT INTO attempts (user_ID, quiz_ID, quiz_Score) VALUES('" + Main.userID + "','" + Main.userQuizID + "','" + Main.score + "')").executeUpdate();
            stage = (Stage) question.getScene().getWindow();
            changeScene("../displays/results.fxml", stage);
        }
        else{
            getRight();
            count += 1;
            display();

        }
    }

    /*previous question button*/
    public void onPrev(){
        if(count != 1){
            count -= 1;
            display();
        }
    }

    /* gets the correct answer*/
    public void getRight(){
        countTwo = 1;
        questionListUser.forEach((addQuestion questionTmp) -> {
            if (count == countTwo) {
                addQuestion tmp = questionTmp;
                if(tmp.getCode() == 1) {
                    RadioButton tmpButton = (RadioButton) answerGroup.getSelectedToggle();
                    if (tmpButton.getText().equals("A")) {
                        choice = tmp.getAnswerOne();
                    } else if (tmpButton.getText().equals("B")) {
                        choice = tmp.getAnswerTwo();
                    } else if (tmpButton.getText().equals("C")) {
                        choice = tmp.getAnswerThree();
                    } else if (tmpButton.getText().equals("D")) {
                        choice = tmp.getAnswerFour();
                    } else {
                        choice = "none";
                    }
                    if (tmp.getCorrect().equals(choice)) {
                        correctList[count - 1] = true;
                    } else {
                        correctList[count - 1] = false;
                    }
                }
                else if(tmp.getCode() == 2){
                    RadioButton tmpButton = (RadioButton) answerGroup.getSelectedToggle();
                    if (tmpButton.getText().equals("True")) {
                        choice = tmp.getAnswerOne();
                    }
                    else if (tmpButton.getText().equals("False")) {
                        choice = tmp.getAnswerTwo();
                    }
                    else{
                        choice = "none";
                    }
                    if (tmp.getCorrect().equals(choice)) {
                        correctList[count - 1] = true;
                    } else {
                        correctList[count - 1] = false;
                    }

                }
                else{
                    if (tmp.getCorrect().equals(shortAnswer.getText())) {
                        correctList[count - 1] = true;
                    } else {
                        correctList[count - 1] = false;
                    }
                }
                System.out.println(correctList[count - 1]);

            }
            countTwo++;
        });
    }

    /*gets the score of quiz*/
    public void getScore(){
        int tmpScore = 0;
        for(int i = 0; i < questionListUser.size(); i++){
            if(correctList[i] == true){
                tmpScore++;
            }
        }
        Main.score = tmpScore;
    }

    public void changeScene(String sceneName, Stage stageTmp)throws Exception{
        root = FXMLLoader.load(getClass().getResource(sceneName));
        stageTmp.setScene(new Scene(root, 719, 530));
        stageTmp.show();
    }
}
