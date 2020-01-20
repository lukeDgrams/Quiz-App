/* controls deeperStats.fxml
   displays specific data based on quiz selected
 */

package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
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
import sample.classes.attemptTable;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class deeperController {
    @FXML
    public Button back;
    public Label title;
    public Label userName;
    public ImageView profilePic;
    public TableView<attemptTable> tableOne;
    public TableColumn<attemptTable, Integer> ID;
    public TableColumn<attemptTable, String> User;
    public TableColumn<attemptTable, Float> Score;
    public ResultSet allUsers, userQuizzes, allQuestions;
    public Connection connection;
    public Stage stage;
    public Parent root;

    public CategoryAxis yAxis = new CategoryAxis();
    public NumberAxis xAxis = new NumberAxis();
    public BarChart<Number,String> graph = new BarChart<Number,String>(xAxis,yAxis);

    public int tmpID, tmpAttID, tmpUserID;
    public int countA, countB, countC, countD, countF;
    public int questionCount = 0;
    public String tmpUser;
    public Float tmpScore;
    ObservableList<attemptTable> attList = FXCollections.observableArrayList();

    /* sets display */
    public void initialize() {
        ConnectionClass connectionClass = new ConnectionClass();
        connection = connectionClass.getConnection();
        title.setText(Main.quizID);
        countA = countB = countC = countD = countF = 0;
        getQuestionCount();
        setCustomThings();
        setTableOne();
        addDataGraph();

    }

    /* back button function*/
    public void onBack()throws Exception{
        stage = (Stage) back.getScene().getWindow();
        changeScene("../displays/quizStats.fxml", stage);
    }
    public void changeScene(String sceneName, Stage stageTmp)throws Exception{
        root = FXMLLoader.load(getClass().getResource(sceneName));
        stageTmp.setScene(new Scene(root, 719, 530));
        stageTmp.show();
    }

    /* sets profile pic and user name*/
    public void setCustomThings(){
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

    /* sets quiz score table */
    public void setTableOne(){
        try{
            userQuizzes = connection.prepareStatement("SELECT * FROM attempts WHERE quiz_ID ='" + tmpID + "'").executeQuery();
            allUsers = connection.prepareStatement("select * from user").executeQuery();
            while (userQuizzes.next()) {
                tmpAttID = userQuizzes.getInt(1);
                tmpUserID = userQuizzes.getInt(2);
                tmpScore = userQuizzes.getFloat(4);
                while(allUsers.next()){
                    if(tmpUserID == allUsers.getInt(1)){
                        tmpUser = allUsers.getString(2);
                    }
                }
                attList.add(new attemptTable(tmpAttID, tmpUser, tmpScore));
                getGrade(tmpScore);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        User.setCellValueFactory(new PropertyValueFactory<>("UserName"));
        Score.setCellValueFactory(new PropertyValueFactory<>("Score"));
        tableOne.setItems(attList);


    }

    /* gets question count for quiz */
    public void getQuestionCount(){
        try {
            userQuizzes = connection.prepareStatement("SELECT * FROM quiz WHERE user_ID ='" + Main.userID + "'").executeQuery();
            while (userQuizzes.next()) {
                if (Main.quizID.equals(userQuizzes.getString(3))) {
                    tmpID = userQuizzes.getInt(1);
                }
            }
            allQuestions = connection.prepareStatement("SELECT * FROM questions WHERE quiz_ID ='" + tmpID + "'").executeQuery();
            while(allQuestions.next()){
                questionCount++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /* calculates grades for quiz attempts */
    public void getGrade(float x) {
        x = x / questionCount;
        if (x >= 0.90){
            countA++;
        }else if(x >= 0.80){
            countB++;
        }else if(x >= 0.70){
            countC++;
        }else if(x >= 0.60){
            countD++;
        }else{
            countF++;
        }
    }

    /* adds scores to bar graph*/
    public void addDataGraph(){
        XYChart.Series series = new XYChart.Series();
        series.getData().add(new XYChart.Data("F", countF));
        series.getData().add(new XYChart.Data("D", countD));
        series.getData().add(new XYChart.Data("C", countC));
        series.getData().add(new XYChart.Data("B", countB));
        series.getData().add(new XYChart.Data("A", countA));
        graph.getData().add(series);
        Node n = graph.lookup(".data0.chart-bar");
        n.setStyle("-fx-bar-fill: red");
        n = graph.lookup(".data1.chart-bar");
        n.setStyle("-fx-bar-fill: orange");
        n = graph.lookup(".data2.chart-bar");
        n.setStyle("-fx-bar-fill: yellow");
        n = graph.lookup(".data3.chart-bar");
        n.setStyle("-fx-bar-fill: blue");
        n = graph.lookup(".data4.chart-bar");
        n.setStyle("-fx-bar-fill: green");
    }
}
