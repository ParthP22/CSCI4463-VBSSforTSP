import java.io.*;
import java.util.HashMap;

public class TSPFileParser {

    public static HashMap<Integer,double[]> parseTSPFile(String filename) throws IOException {
        HashMap<Integer,double[]> cities = new HashMap<Integer,double[]>();

        File tspFile = new File(filename);

        FileReader fileReader = new FileReader(tspFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String line = bufferedReader.readLine();

        while (line != null && !line.equals("NODE_COORD_SECTION")) {
            line = bufferedReader.readLine();
        }

        if(line.equals("NODE_COORD_SECTION")){
            line = bufferedReader.readLine();
        }



        while(line != null && !line.equals("EOF")){
            String[] tokens = line.split(" ");
            cities.put(Integer.parseInt(tokens[0]),new double[]{Double.parseDouble(tokens[1]),Double.parseDouble(tokens[2])});
            line = bufferedReader.readLine();
        }

        bufferedReader.close();
        fileReader.close();

        return cities;
    }
}
