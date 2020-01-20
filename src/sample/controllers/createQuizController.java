package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.Main;
import sample.classes.ConnectionClass;
import sample.classes.addQuestion;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

public class createQuizController {
    @FXML
    public Text quizTitle;
    public TextField quizName;
    public TextArea question;
    public ToggleButton tf;
    public ToggleButton shortAnswer;
    public ToggleButton multChoice;
    public Button quizStats;
    public Button addAdmin;
    public Button createQuiz;
    public Button createQuestion;
    public ImageView profilePic;
    public Label userName;


    //tf
    public VBox tfCorrect;
    public Label tfAnswer;
    public RadioButton t;
    public RadioButton f;
    public ToggleGroup tfGroup = new ToggleGroup();

    //short Answer
    public TextArea shortCorrectArea;
    public Label shortLbl;

    //multiple choice
    public VBox choiceLabels;
    public VBox multChoices;
    public VBox correct;
    public Label choices;
    public Label multAnswer;
    public TextField A;
    public TextField B;
    public TextField C;
    public TextField D;
    public RadioButton first;
    public RadioButton second;
    public RadioButton third;
    public RadioButton forth;
    public ToggleGroup multGroup = new ToggleGroup();

    public Connection connection;
    public Stage stage;
    public Parent root;
    public ResultSet allUsers, allQuizzes, allQuestions;
    public String questionType = "none";
    public Alert alert = new Alert(Alert.AlertType.ERROR);
    public ObservableList<addQuestion> questionList = FXCollections.observableArrayList();
    public TableView<addQuestion> questionTable = new TableView<addQuestion>();
    public TableColumn<addQuestion, Button> clmRemoveButton;
    public TableColumn<addQuestion, Integer> clmID;
    public TableColumn<addQuestion, String> clmQuestion;
    public TableColumn<addQuestion, String> clmAnswer;

    /*sets display */
    public void initialize(){
        quizTitle.textProperty().bind(quizName.textProperty());
        clearScreen();
        setToggleGroups();
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
    }

    /* sets display when true false is selected */
    public void onTF(){
        clearScreen();
        tfCorrect.setVisible(true);
        tfAnswer.setVisible(true);
        questionType = "tf";
    }

    /* sets display when multiple choice is selected */
    public void onMult(){
        clearScreen();
        choiceLabels.setVisible(true);
        multChoices.setVisible(true);
        correct.setVisible(true);
        choices.setVisible(true);
        multAnswer.setVisible(true);
        questionType = "mult";
    }

    /* sets display when short answer is selected */
    public void onShort(){
        clearScreen();
        shortCorrectArea.setVisible(true);
        shortLbl.setVisible(true);
        questionType = "short";
    }

    /* adds question temp question list*/
    public void onCreateQuestion(){
        String tmpQuestion = question.getText();
        String tmpCorrect, AnswerOne, AnswerTwo, AnswerThree, AnswerFour;
        if(validCheck()) {
            if (questionType.equals("tf")) {
                RadioButton selectedRadioButton = (RadioButton) tfGroup.getSelectedToggle();
                tmpCorrect = selectedRadioButton.getText();
                questionList.add(new addQuestion(tmpQuestion, tmpCorrect, "True", "False"));
            }
            else if (questionType.equals("mult")) {
                RadioButton selectedRadioButton = (RadioButton) multGroup.getSelectedToggle();
                tmpCorrect = selectedRadioButton.getText();
                AnswerOne = A.getText();
                AnswerTwo = B.getText();
                AnswerThree = C.getText();
                AnswerFour = D.getText();
                if(tmpCorrect.equals("A")){
                    tmpCorrect = AnswerOne;
                }
                else if(tmpCorrect.equals("B")){
                    tmpCorrect = AnswerTwo;
                }
                else if(tmpCorrect.equals("C")){
                    tmpCorrect = AnswerThree;
                }
                else if(tmpCorrect.equals("D")){
                    tmpCorrect = AnswerFour;
                }
                questionList.add(new addQuestion(tmpQuestion, tmpCorrect, AnswerOne, AnswerTwo, AnswerThree, AnswerFour));

            }
            else {
                tmpCorrect = shortCorrectArea.getText();
                questionList.add(new addQuestion(tmpQuestion, tmpCorrect));
            }
            setOnAction();
            addTableData();
            clearControls();
        }
    }

    /* sets all controls invisible*/
    public void clearScreen(){
        tfCorrect.setVisible(false);
        tfAnswer.setVisible(false);

        shortCorrectArea.setVisible(false);
        shortLbl.setVisible(false);

        choiceLabels.setVisible(false);
        multChoices.setVisible(false);
        correct.setVisible(false);
        choices.setVisible(false);
        multAnswer.setVisible(false);
    }

    /* sets toggle groups*/
    public void setToggleGroups(){
        first.setToggleGroup(multGroup);
        second.setToggleGroup(multGroup);
        third.setToggleGroup(multGroup);
        forth.setToggleGroup(multGroup);

        t.setToggleGroup(tfGroup);
        f.setToggleGroup(tfGroup);
    }

    /*checks for valid inputs */
    public boolean validCheck(){
        if(question.getText().equals("")){
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Must fill out all fields");
            alert.show();
            return false;
        }
        else if(quizName.getText().equals("")){
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Must fill quiz name");
            alert.show();
            return false;
        }
        else if(questionType.equals("none")){
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Must choose question type");
            alert.show();
            return false;
        }
        else if(questionType.equals("tf") && tfGroup.getSelectedToggle() == null){
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Must fill out all fields");
            alert.show();
            return false;
        }
        else if(questionType.equals("mult")){
            if(multGroup.getSelectedToggle() == null){
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setContentText("Must fill out all fields");
                alert.show();
                return false;
            }
            else if(A.getText().equals("")){
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setContentText("Must fill out all fields");
                alert.show();
                return false;
            }
            else if(B.getText().equals("")){
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setContentText("Must fill out all fields");
                alert.show();
                return false;
            }
            else if(C.getText().equals("")){
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setContentText("Must fill out all fields");
                alert.show();
                return false;
            }
            else if(D.getText().equals("")){
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setContentText("Must fill out all fields");
                alert.show();
                return false;
            }
        }
        else if(questionType.equals("short") && shortCorrectArea.getText().equals("")){
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Must fill out all fields");
            alert.show();
            return false;
        }
        for (Iterator<addQuestion> iterator = questionList.iterator(); iterator.hasNext(); ) {
            addQuestion tmp = iterator.next();
            if (tmp.getQuestion().equals(question.getText())) {
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setContentText("Cannot have the same question Twice");
                alert.show();
                return false;
            }
        }
        return true;
    }

    /* adds temp questions to table*/
    public void addTableData(){
        clmRemoveButton.setCellValueFactory(new PropertyValueFactory<addQuestion, Button>("Remove"));
        clmID.setCellValueFactory(new PropertyValueFactory<addQuestion, Integer>("Id"));
        clmQuestion.setCellValueFactory(new PropertyValueFactory<addQuestion, String>("Question"));
        clmAnswer.setCellValueFactory(new PropertyValueFactory<addQuestion, String>("Correct"));
        questionTable.setItems(questionList);
    }

    /* clears inputs */
    public void clearControls(){
        question.setText("");
        tfGroup.selectToggle(null);
        multGroup.selectToggle(null);
        A.setText("");
        B.setText("");
        C.setText("");
        D.setText("");
        shortCorrectArea.setText("");
    }

    /* sets action event for question table */
    public void setOnAction(){
        questionList.forEach(question -> {
            question.setAction(event);
        });
    }

    /* validates quiz */
    public void onCreateQuiz(){
        if(questionList.size() > 0){
            if(checkQuizValid()) {
                addToQuiz();
                addToQuestions();
                resetScene();
            }
        }
        else{
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Must add question to create quiz");
            alert.show();
        }
    }
    public boolean checkQuizValid(){
        try {
            allQuizzes = connection.prepareStatement("select * from quiz where quiz_name = '" + quizName.getText() + "'").executeQuery();
            while(allQuizzes.next()){
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setContentText("Quiz Name Taken");
                alert.show();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    /*adds quiz to database */
    public void addToQuiz(){

        try {
            connection.prepareStatement("INSERT INTO quiz (user_ID, quiz_name) VALUES('" + Main.userID + "','" + quizName.getText() + "')").executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void addToQuestions(){
        try {
            allQuizzes = connection.prepareStatement("select * from quiz where quiz_name = '" + quizName.getText() + "'").executeQuery();
            while(allQuizzes.next()){
                Main.tmpID = allQuizzes.getInt(1);
            }
            questionList.forEach(question -> {
                try {
                    connection.prepareStatement("INSERT INTO questions (quiz_ID, question, type, correct) VALUES('" + Main.tmpID + "','" + question.getQuestion() + "','" + question.getCode() + "','" + question.getCorrect() + "')").executeUpdate();
                    if(question.getCode() == 1 ){
                        allQuestions = connection.prepareStatement("select * from questions where quiz_ID = '" + Main.tmpID + "' and question ='" + question.getQuestion() + "'").executeQuery();
                        while(allQuestions.next()){
                            Main.tmpQuestionID = allQuestions.getInt(1);
                        }
                        connection.prepareStatement("INSERT INTO type_mult (question_ID, answer_one, answer_two, answer_three, answer_four) VALUES('" + Main.tmpQuestionID + "','" + question.getAnswerOne() + "', '" + question.getAnswerTwo() + "', '" + question.getAnswerThree() + "', '" + question.getAnswerFour() + "')").executeUpdate();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /* resets scene after quiz is created */
    public void resetScene(){
        questionList = FXCollections.observableArrayList();
        clearControls();
        clearScreen();
        quizName.setText("");
        questionTable.setItems(questionList);
    }

    /* change scene buttons */
    public void quizStats() throws Exception {
        stage = (Stage) quizTitle.getScene().getWindow();
        changeScene("../displays/quizStats.fxml", stage);
    }
    public void addAdmin() throws Exception {
        stage = (Stage) quizTitle.getScene().getWindow();
        changeScene("../displays/addAdmin.fxml", stage);
    }
    public void changeScene(String sceneName, Stage stageTmp)throws Exception{
        root = FXMLLoader.load(getClass().getResource(sceneName));
        stageTmp.setScene(new Scene(root, 719, 530));
        stageTmp.show();
    }

    /* action event for question table */
    EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e) {
            String buttonID = ((Button)e.getSource()).getId();
            for (Iterator<addQuestion> iterator = questionList.iterator(); iterator.hasNext(); ) {
                addQuestion tmp = iterator.next();
                if (String.valueOf(tmp.getId()).equals(buttonID)) {
                    iterator.remove();
                }
            }
            addTableData();
        }
    };


}
