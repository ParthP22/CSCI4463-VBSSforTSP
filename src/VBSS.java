import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeSet;

public class VBSS {

    //Question: is bias an int or real-num???
    public static void doVBSS(String filename, String bParam) throws IOException {
        int bias = Integer.parseInt(bParam);
        HashMap<Integer,double[]> cities = TSPFileParser.parseTSPFile(filename);
        //System.out.println(cities == null?null:cities.toString());
        LinkedList<Integer> tour = new LinkedList<Integer>();
        int currCity = 1;
        tour.add(currCity);
        while(cities.size() > 1){
            //System.out.println(currCity);
            double[][] probabilities = calculateProbabilities(cities,currCity,bias);

            TreeSet<Double> probabilitiesSet = new TreeSet<Double>((a,b) -> Double.compare(a,b));
            HashMap<Double,Integer> probabilityIntervalToCity = new HashMap<>();
            double probabilitiesInterval = 0;
            for(double[] probability : probabilities){
                probabilityIntervalToCity.put(probabilitiesInterval,(int)probability[0]);
                probabilitiesSet.add(probabilitiesInterval);
                probabilitiesInterval += probability[1];
            }

            double randomNum = Math.random();

            double probability = probabilitiesSet.floor(randomNum);
            currCity = probabilityIntervalToCity.get(probability);
            tour.add(currCity);
        }

    }

    public static double[][] calculateProbabilities(HashMap<Integer,double[]> cities, int city, int bias){
        double divisor = 0;
        System.out.println(city);
        double[] cityCoords = new double[]{cities.get(city)[0],cities.get(city)[1]};

        cities.remove(city);

        double[][] heuristics = new double[cities.size()][2];

        int i = 0;
        for(Map.Entry<Integer,double[]> entry : cities.entrySet()){
            heuristics[i][0] = entry.getKey();
            heuristics[i][1] = (int)(Math.round(Math.sqrt((entry.getValue()[0] - cityCoords[0])*(entry.getValue()[0] - cityCoords[0]) +
                                                        (entry.getValue()[1] - cityCoords[1])*(entry.getValue()[1] - cityCoords[1]))));
            // Question: Do I need to worry about division by 0?
            if(heuristics[i][1] != 0){
                divisor += 1/Math.pow(heuristics[i][1],bias);
            }
        }

        double[][] probabilities = new double[cities.size()][2];

        for(i = 0; i < heuristics.length; i++){
            probabilities[i][0] = heuristics[i][0];
            probabilities[i][1] = 1/Math.pow(heuristics[i][1],2)/divisor;
        }

        return probabilities;

    }



    //Note to self: use treeset; we can use floor func from it
}
