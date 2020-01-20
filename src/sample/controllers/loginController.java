package sample.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.Main;
import sample.classes.ConnectionClass;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class loginController {
    @FXML
    public Button createAccount;
    public Button login;
    public Button loginTwo;
    public Button createAccountTwo;
    public TextField userName;
    public PasswordField password;
    public boolean adminCheck;
    public Alert alert = new Alert(Alert.AlertType.ERROR);
    Parent root;
    Stage stage;
    public Connection connection;
    public ResultSet allUsers;
    public void initialize(){
        ConnectionClass connectionClass = new ConnectionClass();
        connection = connectionClass.getConnection();
    }
    public void createAccount()throws Exception{
        stage = (Stage) login.getScene().getWindow();
        changeScene("../displays/createAcount.fxml", stage);
    }
    public void createAccountTwo()throws Exception{
        stage = (Stage) loginTwo.getScene().getWindow();
        changeScene("../displays/createAcount.fxml", stage);
    }
    public void login() throws Exception{
        stage = (Stage) login.getScene().getWindow();
        changeScene("../displays/loginTwo.fxml", stage);
    }
    public void loginTwo() throws Exception {
        if(credentialsCheck(userName.getText(), password.getText()) != true){
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Wrong username or password");
            alert.show();
            return;
        }
        if( adminCheck == true) {
            stage = (Stage) loginTwo.getScene().getWindow();
            changeScene("../displays/quizStats.fxml", stage);
        }else{
            stage = (Stage) loginTwo.getScene().getWindow();
            changeScene("../displays/profilePage.fxml", stage);
        }
    }

    public void changeScene(String sceneName, Stage stageTmp)throws Exception{
        root = FXMLLoader.load(getClass().getResource(sceneName));
        stageTmp.setScene(new Scene(root, 719, 530));
        stageTmp.show();
    }
    public boolean credentialsCheck(String username, String PassWord){
        try {
            allUsers = connection.prepareStatement("select * from user").executeQuery();
            while (allUsers.next()) {
                if (userName.getText().equals(allUsers.getString(2)) && password.getText().equals(allUsers.getString(3))){
                    Main.userID = allUsers.getInt(1);
                    adminCheck = allUsers.getBoolean(4);
                    return true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }



}
