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
        //args[1] = b-parameter value for VBSS (default value = 10.0)
        //args[2] = number of total iterations of VBSS (default value = 1000)

        // In case the b-parameter value or number of total iterations aren't
        // specified, then we'll just leave them as empty strings.
        String[] arguments = {"","",""};
        for(int i = 0; i < args.length; i++){
            arguments[i] = args[i];
        }

        VBSS.doVBSS(arguments[0],arguments[1],arguments[2]);
    }
}
