/* controller for quizStats
   displays data based on user
 */
package sample.controllers;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.classes.ConnectionClass;
import sample.Main;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class adminController {
    @FXML
    public Button addAdmin;
    public Button createQuiz;
    public PieChart quizUsed;
    public ImageView profilePic;
    public Label userName;
    public Connection connection;
    public ResultSet allUsers, userQuizzes, quizAttempts;
    public Stage stage;
    public Parent root;
    public int attemptsCount;
    ObservableList<PieChart.Data> attemptData = FXCollections.observableArrayList();
/* initalizes display*/
    public void initialize(){
        ConnectionClass connectionClass = new ConnectionClass();
        connection = connectionClass.getConnection();
        try {
            allUsers = connection.prepareStatement("select * from user").executeQuery();
            while (allUsers.next()) {
                if (Main.userID == allUsers.getInt(1)){
                    File file = new File(allUsers.getString(5));
                    Image image = new Image(file.toURI().toString());
                    profilePic.setImage(image);
                    userName.setText(allUsers.getString(2));
                }
            }
            userQuizzes = connection.prepareStatement("SELECT * FROM quiz WHERE user_ID ='" + Main.userID + "'").executeQuery();

            while (userQuizzes.next()){
                attemptsCount = 0;
                quizAttempts = connection.prepareStatement("SELECT * FROM attempts WHERE quiz_ID ='" + userQuizzes.getInt(1) + "'").executeQuery();
                while(quizAttempts.next()){
                    attemptsCount++;
                }
                attemptData.add(new PieChart.Data(userQuizzes.getString(3), attemptsCount));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        quizUsed.setData(attemptData);

        attemptData.stream().forEach(pieData -> {
            pieData.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
                Bounds b1 = pieData.getNode().getBoundsInLocal();
                double newX = (b1.getWidth()) / 2 + b1.getMinX();
                double newY = (b1.getHeight()) / 2 + b1.getMinY();
                TranslateTransition tt = new TranslateTransition(
                        Duration.millis(500), pieData.getNode());
                tt.setByX(newX / 4);
                tt.setByY(newY / 4);
                tt.play();

            });
            pieData.getNode().addEventHandler(MouseEvent.MOUSE_EXITED, events -> {
                TranslateTransition tt = new TranslateTransition(
                        Duration.millis(500), pieData.getNode());
                tt.setToX(0);
                tt.setToY(0);
                tt.play();
            });
            pieData.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, events -> {
                stage = (Stage) createQuiz.getScene().getWindow();
                Main.quizID = pieData.getName().toString();
                try {
                    changeScene("../displays/deeperStats.fxml", stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
    }

    /*change scene function*/
    public void createQuiz() throws Exception {
        stage = (Stage) createQuiz.getScene().getWindow();
        changeScene("../displays/createQuiz.fxml", stage);
    }
    public void addAdmins() throws Exception {
        stage = (Stage) createQuiz.getScene().getWindow();
        changeScene("../displays/addAdmin.fxml", stage);
    }

    public void changeScene(String sceneName, Stage stageTmp)throws Exception{
        root = FXMLLoader.load(getClass().getResource(sceneName));
        stageTmp.setScene(new Scene(root, 719, 530));
        stageTmp.show();
    }
}
