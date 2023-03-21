import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TestFile {
    /*
        String with filepath from root
        Create Filereader with init (Filepath string, StandardCharSets.UTF_8)
        Create csvReader with init (Filereader from step above)

        reader.readNext for next line

        Put into array of arrays
        Count items in first array
        Create x amount of columns for each item, take first array and use those as column names
        Count each array
        Create x amount of rows for tableView

     */

    public static void main(String[] args) {
        List<String[]> csvFileArray = loadCSVFile();

        System.out.println(csvFileArray.size());

        for(String[] row: csvFileArray){
            for(String cell: row) {
                System.out.print(cell + "|");
            }
            System.out.println("\n------------------------------------------------------------------");
        }
    }

    public static List<String[]> loadCSVFile() {
        String fileName = "CsvTestFiles/Test.csv";
        List<String[]> csvFileArray;

        try {
            FileReader filereader = new FileReader(fileName, StandardCharsets.UTF_8);
            CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
            CSVReader csvReader = new CSVReaderBuilder(filereader).withCSVParser(parser).build();

            csvFileArray = csvReader.readAll();
            return csvFileArray;
        } catch (IOException | CsvException ex) {
            throw new RuntimeException(ex);
        }
    }



}
