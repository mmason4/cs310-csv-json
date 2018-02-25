package edu.jsu.mcis;

import java.io.*;
import java.util.*;
import com.opencsv.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Converter {
    
    /*
    
        Consider the following CSV data:
        
        "ID","Total","Assignment 1","Assignment 2","Exam 1"
        "111278","611","146","128","337"
        "111352","867","227","228","412"
        "111373","461","96","90","275"
        "111305","835","220","217","398"
        "111399","898","226","229","443"
        "111160","454","77","125","252"
        "111276","579","130","111","338"
        "111241","973","236","237","500"
        
        The corresponding JSON data would be similar to the following (tabs and other whitespace
        have been added for clarity).  Note the curly braces, square brackets, and double-quotes!
        
        {
            "colHeaders":["ID","Total","Assignment 1","Assignment 2","Exam 1"],
            "rowHeaders":["111278","111352","111373","111305","111399","111160","111276","111241"],
            "data":[[611,146,128,337],
                    [867,227,228,412],
                    [461,96,90,275],
                    [835,220,217,398],
                    [898,226,229,443],
                    [454,77,125,252],
                    [579,130,111,338],
                    [973,236,237,500]
            ]
        }
    
    */
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        
        String results = "";
        
        try {
            
            CSVReader reader = new CSVReader(new StringReader(csvString));
            List<String[]> full = reader.readAll();
            Iterator<String[]> iterator = full.iterator();
            
            JSONObject jsonObject = new JSONObject();
            
            // INSERT YOUR CODE HERE
            String[] headings = iterator.next();
            ArrayList colHeadings = new ArrayList<String>();
            for(String column: headings){
                String addQuotes = "\"" + column + "\"";
                colHeadings.add(addQuotes);            
            }
            ArrayList rowHeadings = new ArrayList<String>();
            for(int j = 1; j < full.size();++j){
                String rowAddQuotes = "\"" + full.get(j)[0] + "\"";
                rowHeadings.add(rowAddQuotes);
            }
            
            ArrayList data = new ArrayList<String>();
            for(int i = 1; i < full.size();++i){
                String[] subset = Arrays.copyOfRange(full.get(i), 1, full.get(i).length);
                data.add(Arrays.toString(subset));
                
            }
            
            
            jsonObject.put("rowHeaders", rowHeadings);
            jsonObject.put("data", data);
            jsonObject.put("colHeaders", colHeadings);
            
            results = "";
            results += "{\"rowHeaders\":" + jsonObject.get("rowHeaders").toString().replaceAll(", ", ",")+",";
            results += "\"data\":" + data.toString().replaceAll(", ", ",") + ",";
            results += "\"colHeaders\":"+ jsonObject.get("colHeaders").toString().replaceAll(", ", ",") + "}";
        }
        
        catch(IOException e) { return e.toString(); }
        
        return results.trim();
        
    }
    
    public static String jsonToCsv(String jsonString) {
        
        String results = "";
        
        try {
            
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject)parser.parse(jsonString);
            
            StringWriter writer = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(writer, ',', '"', '\n');
            
            ArrayList csvData = new ArrayList<String>();
            ArrayList colHeadings = (ArrayList<String>) jsonObject.get("colHeaders");
            ArrayList rowHeadings = (ArrayList<String>) jsonObject.get("rowHeaders");
            ArrayList data = (ArrayList<String>) jsonObject.get("data");
            String[] columns = new String[colHeadings.size()];
            for(int i = 0; i < colHeadings.size(); ++i){
                columns[i] = (String)colHeadings.get(i);
            }
            csvWriter.writeNext(columns);
            for(int i = 0; i < rowHeadings.size(); ++i){
                String[] combined = new String[colHeadings.size()];
                combined[0] = (String)rowHeadings.get(i);
                ArrayList dataList = (ArrayList)data.get(i);
                for(int j = 0; j < dataList.size(); ++j){
                    combined[j+1] = Long.toString((long) dataList.get(j));
                }
                csvWriter.writeNext(combined);
            }
            results += writer.toString();
            // INSERT YOUR CODE HERE
            
        }
        
        catch(ParseException e) { return e.toString(); }
        
        return results.trim();
        
    }
	
}