package com.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestDataUtil {

    /**
     * Reads CSV file from classpath and returns list of maps
     * Each map represents a row with column headers as keys
     * 
     * @param fileName name of the CSV file in test resources
     * @return List of Maps containing CSV data
     */
    public static List<Map<String, String>> readCsvFile(String fileName) {
        List<Map<String, String>> records = new ArrayList<>();
        
        try {
            InputStream input = TestDataUtil.class.getClassLoader()
                    .getResourceAsStream("testdata/" + fileName);
            
            if (input == null) {
                throw new RuntimeException("CSV file not found: " + fileName);
            }
            
            InputStreamReader reader = new InputStreamReader(input);
            CSVParser csvParser = new CSVParser(reader, 
                    CSVFormat.DEFAULT.withFirstRecordAsHeader());
            
            for (CSVRecord record : csvParser) {
                Map<String, String> rowData = record.toMap();
                records.add(rowData);
            }
            
            csvParser.close();
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to read CSV file: " + fileName, e);
        }
        
        return records;
    }

    /**
     * Gets a specific record by index from CSV
     * 
     * @param fileName name of the CSV file
     * @param index row index (0-based)
     * @return Map containing the row data
     */
    public static Map<String, String> getCsvRecord(String fileName, int index) {
        List<Map<String, String>> records = readCsvFile(fileName);
        if (index >= 0 && index < records.size()) {
            return records.get(index);
        }
        throw new RuntimeException("Record at index " + index + " not found");
    }

    /**
     * Gets first record from CSV (useful for single-row test data)
     * 
     * @param fileName name of the CSV file
     * @return Map containing first row data
     */
    public static Map<String, String> getFirstRecord(String fileName) {
        return getCsvRecord(fileName, 0);
    }
}
