package MailingStudents.view;

import MailingStudents.model.MailingStudentsModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import static javafx.scene.control.TabPane.TabClosingPolicy.UNAVAILABLE;

public class MailingStudentsView extends BorderPane {

    /* TabPane is the biggest element with 3 tabs: OpenCSV, Overview, Mailing List.
      TabPane
          OpenCSV - Tab
                  DragAndDrop - Vbox
                      DragAndDropText - Label
                  Confirm - Button
                  SelectFile - Button
          Overview - Tab
                Table
                Confirm Edit Button
                Reset All Button
                (Undo Button)
          Mailing List - Tab
                Scrolling Pane
                        Infinite amount of buttons that each have a student. When pressed a mail will be sent
     */
    TabPane tabPane;
    Tab opencsvTab, overviewTab, mailListTab;
    VBox dragAndDrop;
    HBox openCSVButtons;
    Button selectFile;
    Button confirmCSV;
    VBox openCSVRoot;
    BorderPane overViewRoot;
    TableView overViewTable;
    Label dragAndDropText;
    VBox mailingListRoot;
    Label lblTeacherName, lblMeetingLink;
    public TextField teacherName;
    public TextField meetingLink;
    HBox mailingSettings;


    public MailingStudentsView() {
        this.initialiseNodes();
        this.layoutNodes();
    }

    private void initialiseNodes() {
// TabPane -----------------------------------------------------------------------------------
        this.tabPane = new TabPane();
        this.opencsvTab = new Tab("Open CSV");
        this.overviewTab = new Tab("Overview");
        this.mailListTab = new Tab("Mailing List");

// OpenCSV -----------------------------------------------------------------------------------
        this.dragAndDrop = new VBox();
        this.openCSVButtons = new HBox();
        this.selectFile = new Button("Select File");
        this.confirmCSV = new Button("Confirm");
        this.openCSVRoot = new VBox();
        this.dragAndDropText = new Label("Drag and Drop CSV file here!");

// OverviewTab -------------------------------------------------------------------------------
        this.overViewRoot = new BorderPane();
        this.overViewTable = new TableView<MailingStudentsModel.ScoreRow>();
        TableColumn<MailingStudentsModel.ScoreRow, String> emailColumn = new TableColumn<>("Email");
        TableColumn<MailingStudentsModel.ScoreRow, String> voornaamColumn = new TableColumn<>("Voornaam");
        TableColumn<MailingStudentsModel.ScoreRow, String> naamColumn = new TableColumn<>("Naam");
        TableColumn<MailingStudentsModel.ScoreRow, String> rapportonderdeelColumn = new TableColumn<>("Rapport onderdeel");
        TableColumn<MailingStudentsModel.ScoreRow, String> deelgroepcodeColumn = new TableColumn<>("Deel Groep Code");
        TableColumn<MailingStudentsModel.ScoreRow, String> puntColumn = new TableColumn<>("Punt");
        TableColumn<MailingStudentsModel.ScoreRow, String> puntcodeafkColumn = new TableColumn<>("Punt code");
        TableColumn<MailingStudentsModel.ScoreRow, String> exkansomschrColumn = new TableColumn<>("Examen kans omschrijving");
        TableColumn<MailingStudentsModel.ScoreRow, String> kansaandeelColumn = new TableColumn<>("Kans aandeel");
        TableColumn<MailingStudentsModel.ScoreRow, String> experiodeomscgrColumn = new TableColumn<>("Examen periode omschrijving");

        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        voornaamColumn.setCellValueFactory(new PropertyValueFactory<>("voornaam"));
        naamColumn.setCellValueFactory(new PropertyValueFactory<>("naam"));
        rapportonderdeelColumn.setCellValueFactory(new PropertyValueFactory<>("rapportonderdeel"));
        deelgroepcodeColumn.setCellValueFactory(new PropertyValueFactory<>("deelgroepcode"));
        puntColumn.setCellValueFactory(new PropertyValueFactory<>("punt"));
        puntcodeafkColumn.setCellValueFactory(new PropertyValueFactory<>("puntcodeafk"));
        exkansomschrColumn.setCellValueFactory(new PropertyValueFactory<>("exkansomschr"));
        kansaandeelColumn.setCellValueFactory(new PropertyValueFactory<>("kansaandeel"));
        experiodeomscgrColumn.setCellValueFactory(new PropertyValueFactory<>("experiodeomschr"));

        overViewTable.getColumns().addAll(emailColumn, voornaamColumn, naamColumn,
                rapportonderdeelColumn, deelgroepcodeColumn, puntColumn, puntcodeafkColumn,
                exkansomschrColumn, kansaandeelColumn, experiodeomscgrColumn);

// Mailing List Tab -------------------------------------------------------------------------------
        this.lblMeetingLink = new Label( "Teacher:");
        this.lblTeacherName = new Label("Meeting Link:");
        this.meetingLink = new TextField();
        this.teacherName = new TextField();
        this.mailingSettings = new HBox();
        this.mailingListRoot = new VBox();
    }

    private void layoutNodes() {
// OpenCSV -----------------------------------------------------------------------------------
        dragAndDrop.setMinSize(400, 200);
        dragAndDrop.setId("dragAndDrop");
        dragAndDrop.setAlignment(Pos.CENTER);
        dragAndDrop.getChildren().add(dragAndDropText);

        openCSVButtons.getChildren().addAll(selectFile,confirmCSV);
        openCSVButtons.setSpacing(10);
        openCSVButtons.setAlignment(Pos.BOTTOM_CENTER);

        openCSVRoot.getChildren().addAll(dragAndDrop, openCSVButtons);
        openCSVRoot.setSpacing(20);

        VBox.setVgrow(dragAndDrop, Priority.ALWAYS);
        opencsvTab.setContent(openCSVRoot);

        openCSVRoot.setPadding(new Insets(50, 50, 50, 50));

// OverviewTab -------------------------------------------------------------------------------
        overViewRoot.setPadding(new Insets(10, 10, 10, 10));
        overViewRoot.setCenter(overViewTable);

        overviewTab.setContent(overViewRoot);

// Mailing List Tab -------------------------------------------------------------------------------
        mailingListRoot.setPadding(new Insets(20, 20, 20, 20));
        mailingListRoot.setSpacing(10);
        mailingSettings.getChildren().addAll(lblTeacherName, teacherName, lblMeetingLink, meetingLink);
        mailingSettings.setSpacing(20);
        mailingListRoot.getChildren().addAll(mailingSettings);
        mailListTab.setContent(mailingListRoot);

// TabPane -----------------------------------------------------------------------------------
        tabPane.getTabs().add(opencsvTab);
        tabPane.getTabs().add(overviewTab);
        tabPane.getTabs().add(mailListTab);
        tabPane.setTabClosingPolicy(UNAVAILABLE);
        tabPane.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        this.setCenter(tabPane);
        this.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);

    }
}