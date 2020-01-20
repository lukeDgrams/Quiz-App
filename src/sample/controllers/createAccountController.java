/* controls createAccount.fxml
creates new accounts in database
*/

package sample.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import sample.Main;
import sample.classes.ConnectionClass;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class createAccountController {
    public Button create;
    public TextField userNameCreate;
    public TextField passwordCreate;
    public ImageView imageOne;
    public ImageView imageTwo;
    public ImageView imageThree;
    public ImageView imageFour;
    public ImageView imageFive;
    public ImageView imageSix;
    public ImageView imageSeven;
    public ImageView imageEight;
    public RadioButton one;
    public RadioButton two;
    public RadioButton three;
    public RadioButton four;
    public RadioButton five;
    public RadioButton six;
    public RadioButton seven;
    public RadioButton eight;
    public ToggleGroup imageSelect = new ToggleGroup();
    public Stage stage;
    public Parent root;
    static ResultSet allUsers;
    public Connection connection;
    public Alert alert = new Alert(Alert.AlertType.ERROR);
    public String selected, picUrlFinal;

    /* creates connection to database */
    public void initialize(){
        setToggle();
        ConnectionClass connectionClass = new ConnectionClass();
        connection = connectionClass.getConnection();
    }

    /* verifies account names and creates account*/
    public void createAccountTwo() throws Exception{
        RadioButton selectedRadioButton = (RadioButton) imageSelect.getSelectedToggle();
        selected = selectedRadioButton.getId();
        picUrlFinal = getImage(selected);
        try{
            allUsers = connection.prepareStatement("select * from user").executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            while (allUsers.next()) {
                if (userNameCreate.getText().equals(allUsers.getString(2))) {
                    alert.setAlertType(Alert.AlertType.ERROR);
                    alert.setContentText("Username Taken");
                    alert.show();
                    return;
                }
            }
            connection.prepareStatement("INSERT INTO user (user_Name, user_Password, user_ProfilePic) VALUES('" + userNameCreate.getText() + "','" + passwordCreate.getText() + "','" + picUrlFinal + "');").executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            allUsers = connection.prepareStatement("select * from user").executeQuery();
            while (allUsers.next()) {
                if (userNameCreate.getText().equals(allUsers.getString(2))){
                    Main.userID = allUsers.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        stage = (Stage) create.getScene().getWindow();
        changeScene("../displays/profilePage.fxml", stage);

    }

    /* sets radio buttons toggle group*/
    public void setToggle(){
        one.setToggleGroup(imageSelect);
        two.setToggleGroup(imageSelect);
        three.setToggleGroup(imageSelect);
        four.setToggleGroup(imageSelect);
        five.setToggleGroup(imageSelect);
        six.setToggleGroup(imageSelect);
        seven.setToggleGroup(imageSelect);
        eight.setToggleGroup(imageSelect);
    }

    /* changes scene*/
    public void changeScene(String sceneName, Stage stageTmp)throws Exception{
        root = FXMLLoader.load(getClass().getResource(sceneName));
        stageTmp.setScene(new Scene(root, 719, 530));
        stageTmp.show();
    }

    /* for adding img path to database*/
    public String getImage(String ID){
        String picUrl = "src/profilePic1.png";
        if (ID.equals("two")){
            picUrl = "src/profilePic2.png";
        }else if(ID.equals("three")){
            picUrl = "src/profilePic3.png";
        }else if(ID.equals("four")){
            picUrl = "src/profilePic4.png";
        }else if(ID.equals("five")){
            picUrl = "src/profilePic5.jpg";
        }else if(ID.equals("six")){
            picUrl = "src/profilePic6.jpg";
        }else if(ID.equals("seven")){
            picUrl = "src/profilePic7.jpg";
        }else if(ID.equals("eight")){
            picUrl = "src/profilePic8.png";
        }
        return picUrl;
    }
}
