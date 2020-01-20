/* Main file does nothing but initialize the project and save some static variables*/
package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

    public class Main extends Application {
        public static int userID;
        public static int tmpID;
        public static  int userQuizID;
    public static int tmpQuestionID;
    public static int score;
    public static String quizID;
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("displays/login.fxml"));
        Parent loginPane = loginLoader.load();
        Scene loginScene = new Scene(loginPane, 719, 530);

        primaryStage.setTitle("login");
        primaryStage.setScene(loginScene);
        primaryStage.show();


    }


    public static void main(String[] args) {
        launch(args);
    }
}
