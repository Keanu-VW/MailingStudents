package MailingStudents.view;

import MailingStudents.Main;
import MailingStudents.model.MailingStudentsModel;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MailingStudentsPresenter {

    private MailingStudentsView view;
    private MailingStudentsModel model;
    private List<Button> buttonList = new ArrayList<>();

    public MailingStudentsPresenter(MailingStudentsModel model, MailingStudentsView view) {
        this.model = model;
        this.view = view;
        this.addEventHandlers();
        this.updateView();
    }

    private void addEventHandlers() {
        // Get CSV file and load it into table, Also creates the buttons on mailingList tab. Put's them all in the buttonList
        view.confirmCSV.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Get CSV file and load it into table, Also creates the buttons on mailingList tab. Put's them all in the buttonList
                ObservableList<MailingStudentsModel.ScoreRow> scoreTable = model.createScoreTable(model.filePath);
                view.overViewTable.getItems().clear();
                buttonList.clear();
                view.overViewTable.setItems(scoreTable);
                model.studentScoring();
                Button allMailButton = new Button("Create mail file");
                allMailButton.setMaxWidth(Double.MAX_VALUE);
                buttonList.add(allMailButton);
                for (List<String> student : model.studentScoring) {
                    Button mailButton = new Button(student.get(0) + " | " + student.get(1) + " | " + student.get(2));
                    mailButton.setId(student.get(0));
                    mailButton.setMaxWidth(Double.MAX_VALUE);
                    buttonList.add(mailButton);
                }
                List<String> mailList = new ArrayList<>();
                for (Button mailButton : buttonList) {
                    view.mailingListRoot.getChildren().add(mailButton);
                    if (mailButton != allMailButton) {
                        String mailUrl = model.createMail(mailButton.getText());
                        mailList.add(mailUrl);
                        mailButton.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                model.teacherSetting = view.teacherName.getText();
                                model.meetingLinkSetting = view.teacherName.getText();
                                try {
                                    Desktop.getDesktop().browse(new URL(mailUrl).toURI());
                                } catch (IOException | URISyntaxException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
                    }
                }
                allMailButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        try {
                            File emailFile = new File("EmailList.txt");
                            FileWriter emailWriter = new FileWriter(emailFile);
                            for (String email : mailList) {
                                emailWriter.write(email + "\n");
                            }
                            emailWriter.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        });
        view.selectFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                File file = model.fileChooser.showOpenDialog(Main.primaryStage);
                model.filePath = file.getAbsolutePath();
                view.dragAndDropText.setText(model.filePath);
            }
        });
        view.dragAndDrop.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
                if (dragEvent.getGestureSource() != view.dragAndDropText && dragEvent.getDragboard().hasFiles()) {
                    dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
            }
        });
        view.dragAndDrop.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
                Dragboard db = dragEvent.getDragboard();
                List dragAndDropFile = db.getFiles();
                if (db.hasFiles()) {
                    view.dragAndDropText.setText(dragAndDropFile.get(0).toString());
                }
                model.filePath = dragAndDropFile.get(0).toString();
            }
        });
    }

    private void updateView() {
        view.teacherName.setText(model.teacherSetting);
        view.meetingLink.setText(model.meetingLinkSetting);
    }

    public void addWindowEventHandlers() {
    }

}
