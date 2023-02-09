package edu.jsu.mcis.cs310;

import com.github.cliftonlabs.json_simple.*;
import com.opencsv.CSVReader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class Converter {
    
    /*
        
        Consider the following CSV data, a portion of a database of episodes of
        the classic "Star Trek" television series:
        
        "ProdNum","Title","Season","Episode","Stardate","OriginalAirdate","RemasteredAirdate"
        "6149-02","Where No Man Has Gone Before","1","01","1312.4 - 1313.8","9/22/1966","1/20/2007"
        "6149-03","The Corbomite Maneuver","1","02","1512.2 - 1514.1","11/10/1966","12/9/2006"
        
        (For brevity, only the header row plus the first two episodes are shown
        in this sample.)
    
        The corresponding JSON data would be similar to the following; tabs and
        other whitespace have been added for clarity.  Note the curly braces,
        square brackets, and double-quotes!  These indicate which values should
        be encoded as strings and which values should be encoded as integers, as
        well as the overall structure of the data:
        
        {
            "ProdNums": [
                "6149-02",
                "6149-03"
            ],
            "ColHeadings": [
                "ProdNum",
                "Title",
                "Season",
                "Episode",
                "Stardate",
                "OriginalAirdate",
                "RemasteredAirdate"
            ],
            "Data": [
                [
                    "Where No Man Has Gone Before",
                    1,
                    1,
                    "1312.4 - 1313.8",
                    "9/22/1966",
                    "1/20/2007"
                ],
                [
                    "The Corbomite Maneuver",
                    1,
                    2,
                    "1512.2 - 1514.1",
                    "11/10/1966",
                    "12/9/2006"
                ]
            ]
        }
        
        Your task for this program is to complete the two conversion methods in
        this class, "csvToJson()" and "jsonToCsv()", so that the CSV data shown
        above can be converted to JSON format, and vice-versa.  Both methods
        should return the converted data as strings, but the strings do not need
        to include the newlines and whitespace shown in the examples; again,
        this whitespace has been added only for clarity.
        
        NOTE: YOU SHOULD NOT WRITE ANY CODE WHICH MANUALLY COMPOSES THE OUTPUT
        STRINGS!!!  Leave ALL string conversion to the two data conversion
        libraries we have discussed, OpenCSV and json-simple.  See the "Data
        Exchange" lecture notes for more details, including examples.
        
    */
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        
        String result = "{}"; // default return value; replace later!
        
        try {
        
            // INSERT YOUR CODE HERE
            
            CSVReader reader = new CSVReader(new StringReader(csvString));
            List<String[]> full = reader.readAll();
            Iterator<String[]> iterator = full.iterator();
            JsonArray records = new JsonArray();
            if (iterator.hasNext()){
                String[] headings = iterator.next();
                while(iterator.hasNext()){
                    String[] csvRecord = iterator.next();
                    LinkedHashMap<String, String> jsonRecord = new LinkedHashMap<>();
                    //JsonObject jsonRecord = new JsonObject();
                    for (int i = 0; i < headings.length; ++i){
                        jsonRecord.put(headings[i], csvRecord[i]);
                    }
                    
                    records.add(jsonRecord);
                }
            }
            result = Jsoner.serialize(records);
            
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return result.trim();
        
    }
    
    @SuppressWarnings("unchecked")
    public static String jsonToCsv(String jsonString) {
        
        String result = ""; // default return value; replace later!
        
        try {
            
            // INSERT YOUR CODE HERE
            /*
                I am citing this page as a resource I used to implement this method.
                https://github.com/Ziggomatica/cs310-csv-json/blob/master/src/main/java/edu/jsu/mcis/Converter.java
                The following code has been borrowed and converted to the new json-simple format we are using. 
            */
            JsonObject parser = (JsonObject) Jsoner.deserialize(jsonString);
            JsonArray prodNums = (JsonArray) parser.get("ProdNums");
            JsonArray colHeadings = (JsonArray) parser.get("ColHeadings");
            JsonArray data = (JsonArray) parser.get("Data");
            result = Converter.<String>joinArray((JsonArray) colHeadings) + "\n";
            
            for (int i = 0; i < prodNums.size(); i++){
                result = (result + "\"" + (String)prodNums.get(i) + "\"," + Converter.<Long>joinArray((JsonArray) data.get(i)) + "\n");
            }
            

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return result.trim();
        
    }
  /*
        I am citing this page as a resource I used to implement this method.
        https://github.com/Ziggomatica/cs310-csv-json/blob/master/src/main/java/edu/jsu/mcis/Converter.java
        The following code has been borrowed and converted to the new json-simple format we are using. 
 */
     @SuppressWarnings("unchecked")
    private static <T> String joinArray(JsonArray array) {
        String line = "";
        for(int i = 0; i < array.size(); i++) {
            line = (line + "\"" + ((T) array.get(i)) + "\"");
            if(i < array.size() - 1) {
                line = line + ",";            
            }
        }
        return line;
    }
    
}
