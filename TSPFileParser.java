import java.io.*;
import java.util.HashMap;

/**
 * Contains the method needed to parse the TSP file to obtain
 * all cities and their respective coordinates.
 *
 * @author Parth Patel, Ahmed Malik, James Calabrese
 */
public class TSPFileParser {

    /**
     * The method used to parse the given TSP file and give to the user
     * all the cities contained and their respective coordinates as a <code>HashMap</code>.
     *
     * @param filename the name of the TSP file contained in the parent directory.
     * @return <code>HashMap</code> containing all cities mapped to their respective coordinates.
     * @throws IOException
     */
    public static HashMap<Integer,double[]> parseTSPFile(String filename) throws IOException {
        // Initialize the HashMap that will map the cities to their coordinates.
        HashMap<Integer,double[]> cities = new HashMap<Integer,double[]>();

        // Create a BufferedReader to read the TSP file line by line.
        File tspFile = new File(filename);
        FileReader fileReader = new FileReader(tspFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        // Read the first name (if the file is correct, then this is just the top heading).
        String line = bufferedReader.readLine();

        // Skip through all the lines until you're at the NODE_COORD_SECTION heading.
        while (line != null && !line.equals("NODE_COORD_SECTION")) {
            line = bufferedReader.readLine();
        }

        // If the line is null or if the line doesn't equal NODE_COORD_SECTION,
        // the file is wrong or incompatible with the program.
        if(line != null && line.equals("NODE_COORD_SECTION")){
            line = bufferedReader.readLine();
        }
        else{
            throw new IOException("File is missing NODE_COORD_SECTION heading. Incompatible file format.");
        }

        // From the NODE_COORD_SECTION heading, all the way until the end of the file, we will add the cities
        // and their coordinates into the HashMap.
        while(line != null && !line.equals("EOF")){
            // tokens will be an array of size 3, with the 1st element being the city ID, the 2nd element being the city's first
            // coordinate, and the 3rd element being the city's second coordinate.
            String[] tokens = line.split(" ");
            cities.put(Integer.parseInt(tokens[0]),new double[]{Double.parseDouble(tokens[1]),Double.parseDouble(tokens[2])});
            line = bufferedReader.readLine();
        }

        // Close the file readers.
        bufferedReader.close();
        fileReader.close();

        // Return the HashMap.
        return cities;
    }
}
