package MailingStudents.model;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class MailingStudentsModel {

    public FileChooser fileChooser = new FileChooser();
    public String filePath = "C:\\Users\\Leerling\\Desktop\\Programming\\MailingStudents\\CsvTestFiles\\Test.csv";
    public ObservableList<ScoreRow> scoreTable = FXCollections.observableArrayList();
    public List<List<String>> studentScoring = new ArrayList<>();
    public String teacherSetting;
    public String meetingLinkSetting;
    public MailingStudentsModel() {
        this.loadSettings();
    }

    public void loadSettings() {
        Properties loadProps = new Properties();
        try {
            loadProps.loadFromXML(new FileInputStream("settings.xml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        teacherSetting = loadProps.getProperty("Teacher");
        meetingLinkSetting = loadProps.getProperty("MeetingLink");
    }
    public void saveSettings(String settings1, String settings2) {
        teacherSetting = settings1;
        meetingLinkSetting = settings2;
        Properties saveProps = new Properties();
        saveProps.setProperty("Teacher", teacherSetting);
        saveProps.setProperty("MeetingLink", meetingLinkSetting);
        try {
            saveProps.storeToXML(new FileOutputStream("settings.xml"), "");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    // Self-explanatory, After being called returns a List<List<String>> with al the data from the csv
    public static List<List<String>> loadCSVFile(String fileName) {
        List<List<String>> csvFileArray;

        try {
            FileReader filereader = new FileReader(fileName, StandardCharsets.UTF_8);
            CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
            CSVReader csvReader = new CSVReaderBuilder(filereader).withCSVParser(parser).build();

            List<String[]> csvList = csvReader.readAll();
            csvFileArray = csvList.stream().map(Arrays::asList).collect(Collectors.toList());
            return csvFileArray;
        } catch (IOException | CsvException ex) {
            throw new RuntimeException(ex);
        }
    }

    // Give filePath, returns an ObservableList with ScoreRow instances.
    // ScoreRows have 10 Strings with the different values of the table.
    // The returning ObservableList gets used to create the table itself. This methode just gets and cleans the data
    public ObservableList<ScoreRow> createScoreTable(String fileName) {
        List<List<String>> csvFile = loadCSVFile(fileName);
        for (int i = 1; i < csvFile.size(); i++) {
            List<String> csvFileRow = csvFile.get(i);
            List<String> rowValues = new ArrayList<>(csvFileRow);
            if (rowValues.size() == 9) {
                rowValues.add(6, "/");
            }
            ScoreRow row = new ScoreRow();
            row.email = rowValues.get(0);
            row.voornaam = rowValues.get(1);
            row.naam = rowValues.get(2);
            row.rapportonderdeel = rowValues.get(3);
            row.deelgroepcode = rowValues.get(4);
            row.punt = rowValues.get(5);
            row.puntcodeafk = rowValues.get(6);
            row.exkansomschr = rowValues.get(7);
            row.kansaandeel = rowValues.get(8);
            row.experiodeomschr = rowValues.get(9);
            scoreTable.add(row);
        }
        return scoreTable;
    }

    // Creates a List<List<String> for the studentMailing Buttons. Each inner list has 3 Strings
    //      "Mail, Name, Colour Code"
    public void studentScoring() {
        for (ScoreRow scoreRow : scoreTable) {
            List<String> student = new ArrayList<>();
            student.add(scoreRow.email);
            student.add(scoreRow.naam + " " + scoreRow.voornaam);
            if (!studentScoring.contains(student)) {
                studentScoring.add(student);
            }
        }
        for (List student : studentScoring) {
            List<Float> studentPoints = new ArrayList<>();
            for (ScoreRow scoreRow : scoreTable) {
                if (Objects.equals(scoreRow.email,student.get(0))) {
                    if (Objects.equals(scoreRow.punt, "--")) {
                        studentPoints.add((float) 0);
                    }
                    else if (!(Objects.equals(scoreRow.punt, "xx"))) {
                        studentPoints.add(Float.valueOf(scoreRow.punt));
                    }
                }
            }
            int temp = 0;
            for (float point : studentPoints) {
                if (point < 10) {
                    temp += 1;
                }
            }
            if (temp == 0) {
                student.add("green");
            }
            else if (temp == studentPoints.size()) {
                student.add("red");
            }
            else if (temp == 1){
                student.add("yellow");
            }
            else {
                student.add("orange");
            }
        }
    }


    // Creates mail for mailingStudents buttons

    // mailto:keanu@gmail.com?subject=Test&body=Hello%20World%0D%0Atest%0D%0Ateste%0D%0Atest
    public String createMail(String text){

        // Get all the info for the mail, can be optimized
        String[] temp = text.split(" \\| ");
        String email = temp[0].replace("+", "%2B");
        String naam = temp[1];
        String colour = temp[2];
        String subject = null;
        String examPeriode = null;
        StringBuilder fails = new StringBuilder(new String());

        for (ScoreRow scoreRow : scoreTable) {
            if (scoreRow.email.replace("+", "%2B").equals(email)) {
                examPeriode = scoreRow.experiodeomschr;
                if (!Objects.equals(scoreRow.punt, "xx")) {
                    if (Objects.equals(scoreRow.punt, "--")) {
                        fails.append(String.format("%s (0/20)", scoreRow.rapportonderdeel));
                    }
                    else if (Float.parseFloat(scoreRow.punt) < 10) {
                        fails.append(String.format("%s (%s/20)  ", scoreRow.rapportonderdeel, scoreRow.punt));
                    }
                }
            }
        }


        subject = URLEncoder.encode("Resultaat Examens", StandardCharsets.UTF_8).replace("+", "%20");
        String body = switch (colour) {
            case "green" -> String.format("""
                    Dag %s
                                            
                    Proficiat je bent voor alle vakken geslaagd in de examens van %s.
                    Doe zo verder
                                            
                    Met vriendelijke groeten
                    %s""", naam, examPeriode, meetingLinkSetting);
            case "yellow" -> String.format("""
                    Dag %s
                                            
                    Je bent voor de meeste vakken geslaagd in de examens van %s. Jammer dat je
                    niet slaagde voor %s. Ga zeker naar het inzagerecht van dit vak.
                    Doe zo verder
                                            
                    Met vriendelijke groeten
                    %s""", naam, examPeriode, fails, meetingLinkSetting);
            case "orange" -> String.format("""
                    Dag %s
                                            
                    Je examens van %s verliepen niet zo goed maar ook niet dramatisch. Jammer
                    dat je niet slaagde voor de vakken, %s. Graag
                    had ik hierover een gesprek met je. Boek een afspraak via deze link:
                    %s .
                    Ga alvast zeker naar het inzagerecht van deze vakken.
                    Graag tot dan.
                                            
                    Met vriendelijke groeten
                    %s""", naam, examPeriode, fails, teacherSetting, meetingLinkSetting);
            case "red" -> String.format("""
                    Dag %s
                                            
                    Uit de resultaten van je examens van %s blijkt dat je voor geen enkel vak
                    Karel de Grote Hogeschool, Katholieke Hogeschool Antwerpen vzw - Brusselstraat 45, 2018 ANTWERPEN - BTW BE-0458.402.105 3
                    slaagde. Ik raad je sterk aan om op gesprek te komen. Boek een afspraak via deze link:
                    %s
                                            
                    Met vriendelijke groeten
                    %s""", naam, examPeriode, teacherSetting, meetingLinkSetting);
            default -> "/";
        };

        String structuredBody = URLEncoder.encode(body, StandardCharsets.UTF_8).replace("+", "%20");

        // Email addresses may contain + chars
        return(String.format("mailto:%s?subject=%s&body=%s", email, subject, structuredBody));
    }


    // This is a comment
    public static class ScoreRow {
        String email;
        String voornaam;
        String naam;
        String rapportonderdeel;
        String deelgroepcode;
        String punt;
        String puntcodeafk;
        String exkansomschr;
        String kansaandeel;
        String experiodeomschr;

        public String getEmail() {
            return email;
        }

        public String getVoornaam() {
            return voornaam;
        }

        public String getNaam() {
            return naam;
        }

        public String getRapportonderdeel() {
            return rapportonderdeel;
        }

        public String getDeelgroepcode() {
            return deelgroepcode;
        }

        public String getPunt() {
            return punt;
        }

        public String getPuntcodeafk() {
            return puntcodeafk;
        }

        public String getExkansomschr() {
            return exkansomschr;
        }

        public String getKansaandeel() {
            return kansaandeel;
        }

        public String getExperiodeomschr() {
            return experiodeomschr;
        }
    }


}

