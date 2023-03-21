package MailingStudents;

import MailingStudents.model.MailingStudentsModel;
import MailingStudents.view.MailingStudentsPresenter;
import MailingStudents.view.MailingStudentsView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

public class Main extends Application {
    public static Window primaryStage;
    MailingStudentsView view = new MailingStudentsView();

    MailingStudentsModel model = new MailingStudentsModel();

    @Override
    public void start(Stage primaryStage) {
        // MailingStudentsPresenter communicates between Model and view. If model wants to change something in ui
        // it has to go through the presenter class
        MailingStudentsPresenter presenter = new MailingStudentsPresenter(model, view);
        Scene styledScene = new Scene(view);
        styledScene.getStylesheets().add("MailingStudents/view/css/Style.css");
        primaryStage.setScene(styledScene);
        presenter.addWindowEventHandlers();
        primaryStage.show();
    }
    @Override
    public void stop() {
        model.saveSettings(view.teacherName.getText(), view.meetingLink.getText());
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}