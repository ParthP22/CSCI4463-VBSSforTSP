import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * The main method of the program in which VBSS is ran,
 * and it is expected to be run through the command line.
 * 
 * @author Parth Patel. Ahmed Malik, James Calabrese
 */
public class Main {
    public static void main(String[] args) throws IOException {
        //args[0] = TSP filename
        //args[1] = b-parameter value for VBSS
        //args[2] = number of total iterations of VBSS

        VBSS.doVBSS(args[0],args[1],args[2]);
    }
}
