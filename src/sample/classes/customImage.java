
package sample.classes;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

import java.sql.Connection;

public class customImage {
    private ImageView ImageV;
    private String UserName;
    private Button Action = new Button();
    private int adminType;
    public Connection connection;
    public sample.controllers.addAdminController addAdminController;
    public customImage(ImageView x, String y, Boolean z) {
        ImageV = x;
        ImageV.setFitHeight(50);
        ImageV.setFitWidth(50);
        ImageV.preserveRatioProperty();
        UserName = y;
        adminType = !(z) ? 1 : 0;
        if (z) {
            this.Action.setText("Remove");
        } else {
            this.Action.setText("Add");
        }

        this.Action.setId(UserName);

    }


    public void setImageV(ImageView x) {
        ImageV = x;
    }

    public ImageView getImageV() {
        return ImageV;
    }

    public void setUserName(String y) {
        UserName = y;
    }

    public String getUserName() {
        return UserName;
    }

    public Button getAction() {
        return Action;
    }
    public int getAdminType(){
        return adminType;
    }
    public void setActionEvent(EventHandler<ActionEvent> e){
        this.Action.setOnAction(e);
    }

}
