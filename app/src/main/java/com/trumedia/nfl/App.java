package com.trumedia.nfl;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.trumedia.core.CSVData;
import com.trumedia.core.CSVData.Line;
import com.trumedia.core.CSVReader;
import com.trumedia.core.StringUtil;

public class App {
    public static void main(String[] args) {
        if (args == null || args.length != 2)
        {
            System.err.println("Usage: App <inputCSVFile> <outputJSONFile>");
            return;
        }

        System.out.println("Hello world!");
        try {
            testReadWrite(args[0], args[1]);
        } catch (Exception e) {
            System.err.println("Something went wrong");
            e.printStackTrace();
        }
    }

    private static void testReadWrite(String inputFilename, String outputFilename) {
        CSVData plays;
        System.out.println("Reading input from " + inputFilename);
        try
        {
            plays = new CSVReader(new File(inputFilename)).readAll(true);
        }
        catch (Exception e)
        {
            System.err.println("Unable to read input CSV file: " + inputFilename + " - " + e.getMessage());
            return;
        }

        System.out.println("Parse Penalties for " + plays.getLineCount() + " plays");
        JSONArray output = new JSONArray();
        for (Line line : plays.getLines())
        {
            int gameId = line.getIntColValue("gameId", 0);
            int playId = line.getIntColValue("playId", 0);
            String playDesc = line.getColValue("playDesc");
            try
            {
                // TODO - Implement the body of PenaltyParser.parsePenalties()
                // Hint - Also requires implementing PenaltyDetail.
                List<PenaltyDetail> penalties = PenaltyParser.parsePenalties(playDesc);
                JSONObject play = new JSONObject();
                play.put("gameId", gameId);
                play.put("playId", playId);
                play.put("playDesc", playDesc);
                JSONArray penaltyArr = new JSONArray();
                play.put("penalties", penaltyArr);
                for (PenaltyDetail penaltyDetail : penalties)
                {
                    // TODO - Implement the PenaltyJSON constructor and toJSONObject() method
                    penaltyArr.put(new PenaltyJSON(penaltyDetail).toJSONObject());
                }
                output.put(play);
            }
            catch (PenaltyParsingException e)
            {
                System.err.println("[ERROR] - Unable to parse penalties for gameId=" + gameId + ", playId=" + playId + ", playDesc=\"" + playDesc + "\", err=\"" + e.getMessage() + "\"");
            }
        }

        System.out.println("Writing output to " + outputFilename);
        File outputFile = new File(outputFilename);
        try
        {
            FileOutputStream fos = new FileOutputStream(outputFile);
            fos.write(output.toString().getBytes());
            fos.close();
        }
        catch (Exception e)
        {
            System.err.println("[ERROR] - Unable to save output JSON file: " + outputFilename + " - " + e.getMessage());
            return;
        }

        System.out.println("Done");
    }
}
