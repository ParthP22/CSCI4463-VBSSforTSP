import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeSet;

public class VBSS {

    //Question: is bias an int or real-num???
    public static void doVBSS(String filename, String bParam) throws IOException {
        double bias = Double.parseDouble(bParam);
        HashMap<Integer,double[]> cities = TSPFileParser.parseTSPFile(filename);
        //System.out.println(cities == null?null:cities.toString());
//        for(Map.Entry<Integer,double[]> entry : cities.entrySet()){
//            System.out.println(entry.getKey()+" "+entry.getValue()[0]+" "+entry.getValue()[1]);
//        }
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
                //System.out.println("interval: " + probabilitiesInterval + ", city: " + probability[0]);
                //System.out.println("city: " + probability[0] + ", probability: " + probability[1]);
                probabilityIntervalToCity.put(probabilitiesInterval,(int)probability[0]);
                probabilitiesSet.add(probabilitiesInterval);
                probabilitiesInterval += probability[1];
            }
//            for(double probability : probabilitiesSet){
//                System.out.println(probability + "\t" + probabilityIntervalToCity.get(probability));
//            }
            double randomNum = Math.random();
            System.out.println(randomNum);
            double probability = probabilitiesSet.floor(randomNum);
            currCity = probabilityIntervalToCity.get(probability);
            tour.add(currCity);
        }
        tour.add(tour.getFirst());

        System.out.println("tour: ");
        for(Integer city : tour){
            System.out.println(city);
        }

    }

    public static double[][] calculateProbabilities(HashMap<Integer,double[]> cities, int city, double bias){
        double divisor = 0;
        //System.out.println(city);
        double[] cityCoords = new double[]{cities.get(city)[0],cities.get(city)[1]};
        //System.out.println("coords: " + cityCoords[0] + ", " + cityCoords[1]);
        cities.remove(city);

        double[][] heuristics = new double[cities.size()][2];

        int i = 0;
        for(Map.Entry<Integer,double[]> entry : cities.entrySet()){
            heuristics[i][0] = entry.getKey();
            heuristics[i][1] = (int)(Math.round(Math.sqrt((entry.getValue()[0] - cityCoords[0])*(entry.getValue()[0] - cityCoords[0]) +
                                                        (entry.getValue()[1] - cityCoords[1])*(entry.getValue()[1] - cityCoords[1]))));
            // Question: Do I need to worry about division by 0?
            //if(heuristics[i][1] != 0){
            divisor += 1/Math.pow(heuristics[i][1],bias);
            //}
            //divisor += 1/heuristics[i][1];
            i++;
        }

        double[][] probabilities = new double[cities.size()][2];

        for(i = 0; i < heuristics.length; i++){
            probabilities[i][0] = heuristics[i][0];
            probabilities[i][1] = 1/Math.pow(heuristics[i][1],bias)/divisor;
        }

        return probabilities;

    }



    //Note to self: use treeset; we can use floor func from it
}
