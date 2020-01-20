/* controller for addAdmin.fxml
allows admins to add/remove admins from database*/

package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import sample.classes.ConnectionClass;
import sample.Main;
import sample.classes.customImage;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


public class addAdminController {
    public ImageView profilePic;
    public Label userName;
    public Button createQuiz;
    public Button quizStats;
    public TableView<customImage> adminTable = new TableView<customImage>();
    public TableColumn<customImage, ImageView> userPic;
    public TableColumn<customImage, String> tableUserName;
    public TableColumn<customImage, Button> actionButton;

    public ObservableList<customImage> imageList = FXCollections.observableArrayList();
    public ObservableList<customImage> imList = FXCollections.observableArrayList();

    public Connection connection;
    public Stage stage;
    public Parent root;
    public ResultSet allUsers;
/*initializes display*/
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        addTableData();
    }

/*adds users to table*/
    public void addTableData(){
        try {
            allUsers = connection.prepareStatement("select * from user").executeQuery();
            while (allUsers.next()) {
                imageList.add(new customImage(new ImageView(new Image(new File(allUsers.getString(5)).toURI().toString())), allUsers.getString(2), allUsers.getBoolean(4)));
            }
            imageList.forEach(image -> {
                image.setActionEvent(event);
                imList.add(image);
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
        userPic.setCellValueFactory(new PropertyValueFactory<customImage, ImageView>("ImageV"));
        tableUserName.setCellValueFactory(new PropertyValueFactory<customImage, String>("UserName"));
        actionButton.setCellValueFactory(new PropertyValueFactory<customImage, Button>("Action"));
        adminTable.setItems(imageList);
    }

/*functions for change scene buttons*/
    public void createQuiz() throws Exception {
        stage = (Stage) createQuiz.getScene().getWindow();
        changeScene("../displays/createQuiz.fxml", stage);
    }
    public void quizStats() throws Exception {
        stage = (Stage) createQuiz.getScene().getWindow();
        changeScene("../displays/quizStats.fxml", stage);
    }
    public void changeScene(String sceneName, Stage stageTmp)throws Exception{
        root = FXMLLoader.load(getClass().getResource(sceneName));
        stageTmp.setScene(new Scene(root, 719, 530));
        stageTmp.show();
    }
    /*end of change scene functions*/

    /*eventhandler for display table*/
    EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e) {
            String buttonID = ((Button)e.getSource()).getId();
            imageList.forEach(image -> {
                customImage tmpImage = image;
                if(tmpImage.getUserName().equals(buttonID)){
                    changeAdminType(tmpImage.getUserName(), tmpImage.getAdminType());
                }
            });
            addTableData();
        }
    };

    /*function to add/ remove admin status*/
    public void changeAdminType(String UserName, int adminType) {

        try {
            ConnectionClass connectionClass = new ConnectionClass();
            connection = connectionClass.getConnection();
            connection.prepareStatement("update user set user_Admin ='" + adminType + "'where user_Name ='" + UserName + "'").executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //adminTable.getItems().clear();
        imageList = FXCollections.observableArrayList();
        imList.clear();
    }
}
