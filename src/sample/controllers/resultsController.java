/* controls results.fxml
   displays score of quiz
 */

package sample.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import sample.Main;
import sample.classes.ConnectionClass;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class resultsController {
    @FXML
    public Label userName;
    public ImageView profilePic;
    public Label score;
    public Button back;

    public Stage stage;
    public Parent root;
    public Connection connection;
    public ResultSet allUsers;

    /* sets display*/
    public void initialize() {
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

        } catch (
                SQLException e) {
            e.printStackTrace();
        }
        score.setText(String.valueOf(Main.score));
    }

    /*changes scene*/
    public void onBack() throws Exception {
        stage = (Stage) score.getScene().getWindow();
        changeScene("../displays/profilePage.fxml", stage);
    }

    public void changeScene(String sceneName, Stage stageTmp)throws Exception{
        root = FXMLLoader.load(getClass().getResource(sceneName));
        stageTmp.setScene(new Scene(root, 719, 530));
        stageTmp.show();
    }
}
