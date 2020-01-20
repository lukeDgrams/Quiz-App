/* controls profilePage.fxml
   displays quiz options for users
 */

package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import sample.Main;
import sample.classes.ConnectionClass;
import sample.classes.quizOption;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

public class userController {
    @FXML
    public Button logOut;
    public TableView<quizOption> quizTable = new TableView<quizOption>();
    public TableColumn<quizOption, Button> clmButton;
    public TableColumn<quizOption, Integer> clmCount;
    public TableColumn<quizOption, String> clmName;
    public TableColumn<quizOption, String> clmCreator;

    public ImageView profilePic;
    public Label userName;
    public Connection connection;
    public ResultSet allUsers, allQuizzes, allQuestions;
    public Stage stage;
    public Parent root;
    public String quizName, creatorName;
    public int creatorID, quizID, count;
    public ObservableList<quizOption> quizList = FXCollections.observableArrayList();

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
        getTableData();
    }

    /* gets quiz data from database*/
    public void getTableData(){

        try {
            allQuizzes = connection.prepareStatement("select * from quiz").executeQuery();
            while(allQuizzes.next()) {
                quizID = allQuizzes.getInt(1);
                quizName = allQuizzes.getString(3);
                creatorID = allQuizzes.getInt(2);
                count = 0;
                creatorName = "test";
                try{
                    allUsers = connection.prepareStatement("select * from user").executeQuery();
                    allQuestions = connection.prepareStatement("select * from questions where quiz_ID = '" + quizID + "'").executeQuery();
                    while(allUsers.next()){
                        if(allUsers.getInt(1) == creatorID){
                            creatorName = allUsers.getString(2);
                        }
                    }
                    while(allQuestions.next()){
                        count++;
                    }
                    quizList.add( new quizOption(quizName, creatorName, count, quizID));


                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        setOnAction();
        addTableData();
    }

    /* adds quiz data to table*/
    public void addTableData(){
        clmButton.setCellValueFactory(new PropertyValueFactory<>("Take"));
        clmCount.setCellValueFactory(new PropertyValueFactory<>("QuestionCount"));
        clmCreator.setCellValueFactory(new PropertyValueFactory<>("QuizCreator"));
        clmName.setCellValueFactory(new PropertyValueFactory<>("QuizName"));
        quizTable.setItems(quizList);
    }

    /* adds action event for take quiz button*/
    public void setOnAction(){
        quizList.forEach(quiz -> {
            quiz.setAction(event);
        });
    }

    /* creates action event for quiz take buttons*/
    EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e) {
            String buttonID = ((Button)e.getSource()).getId();
            for (Iterator<quizOption> iterator = quizList.iterator(); iterator.hasNext(); ) {
                quizOption tmp = iterator.next();
                if (tmp.getQuizName().equals(buttonID)) {
                    Main.userQuizID = tmp.getID();
                    try {
                        onTake();
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    };

    /* change scene buttons*/
    public void onLogOut() throws Exception {
        stage = (Stage) userName.getScene().getWindow();
        changeScene("../displays/login.fxml", stage);
    }
    public void onTake() throws Exception {
        stage = (Stage) userName.getScene().getWindow();
        changeScene("../displays/takeQuiz.fxml", stage);
    }
    public void changeScene(String sceneName, Stage stageTmp)throws Exception{
        root = FXMLLoader.load(getClass().getResource(sceneName));
        stageTmp.setScene(new Scene(root, 719, 530));
        stageTmp.show();
    }

}
