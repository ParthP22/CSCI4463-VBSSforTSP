import java.io.IOException;
import java.util.*;

public class VBSS {

    public static void doVBSS(String filename, String bParam, String iterations) throws IOException {
        double bias = Double.parseDouble(bParam);

        int bestCost = Integer.MAX_VALUE;
        LinkedList<Integer> bestTour = new LinkedList<>();

        HashMap<Integer,double[]> cities = TSPFileParser.parseTSPFile(filename);
        for(int i = 0; i < Integer.parseInt(iterations); i++) {
            HashMap<Integer, double[]> currCities = new HashMap<Integer,double[]>();
            for(Map.Entry<Integer,double[]> city : cities.entrySet()){
                currCities.put(city.getKey(),city.getValue());
            }
            //System.out.println(cities == null?null:cities.toString());
            //        for(Map.Entry<Integer,double[]> entry : cities.entrySet()){
            //            System.out.println(entry.getKey()+" "+entry.getValue()[0]+" "+entry.getValue()[1]);
            //        }
            LinkedList<Integer> tour = new LinkedList<Integer>();
            int currCity = 1;
            tour.add(currCity);


            int cost = 0;
            while (currCities.size() > 1) {
                //System.out.println(currCity);
                double randomNum = Math.random();
                int[] nextCityAndCost = calculateProbabilities(currCities, currCity, bias, randomNum);
                cost += nextCityAndCost[1];
                tour.add(nextCityAndCost[0]);
                currCity = nextCityAndCost[0];
            }
            //Find cost between last city in tour and the first city, thus completing the entire tour
            cost += (int)Math.round(distance(cities.get(tour.getLast()),cities.get(tour.getFirst())));

            //Add the first city of the tour to the end to signify that a tour has been completed
            tour.add(tour.getFirst());


            if(cost < bestCost){
                bestCost = cost;
                bestTour = tour;
            }

        }


        System.out.println("Tour: ");
        for(Integer city : bestTour){
            System.out.println(city);
        }

        System.out.println("Cost: " + bestCost);
        System.out.println("Confirmed Cost: " + computeCost(bestTour,cities));

    }

    public static int[] calculateProbabilities(HashMap<Integer,double[]> cities, int city, double bias, double randomNum){
        double divisor = 0;
        //System.out.println(city);
        double[] cityCoords = new double[]{cities.get(city)[0],cities.get(city)[1]};
        //System.out.println("coords: " + cityCoords[0] + ", " + cityCoords[1]);
        cities.remove(city);

        double[][] heuristics = new double[cities.size()][2];

        int i = 0;
        for(Map.Entry<Integer,double[]> entry : cities.entrySet()){
            heuristics[i][0] = entry.getKey();
            heuristics[i][1] = distance(cityCoords,entry.getValue());
            // Question: Do I need to worry about division by 0?
            //if(heuristics[i][1] != 0){
            divisor += 1/Math.pow(heuristics[i][1],bias);
            //}
            //divisor += 1/heuristics[i][1];
            i++;
        }

        double[][] probabilities = new double[cities.size()][2];

        double probabilityInterval = 0;
        for(i = 0; i < heuristics.length; i++){
            probabilities[i][0] = heuristics[i][0];
            probabilities[i][1] = 1/Math.pow(heuristics[i][1],bias)/divisor;
            //Question: is this okay? I generated the random number before the probabilities
            //This way, I don't have to calculate ALL probability intervals before the random number.
            //I get to use the random number to find the exact probability interval that it falls under,
            //which may save a bit of time. The generating the random number and calculating the probabilities
            //are mutually independent.
            if(randomNum >= probabilityInterval && randomNum < probabilityInterval + probabilities[i][1]){
                return new int[]{(int)probabilities[i][0],(int)Math.round(distance(cityCoords,cities.get((int)probabilities[i][0])))};
            }
            probabilityInterval += probabilities[i][1];

        }

        return new int[]{(int)probabilities[probabilities.length-1][0],(int)Math.round(distance(cityCoords,cities.get((int)probabilities[probabilities.length-1][0])))};

    }

    private static double distance(double[] city1, double[] city2){
        return Math.sqrt((city1[0] - city2[0])*(city1[0] - city2[0]) +
                (city1[1] - city2[1])*(city1[1] - city2[1]));
    }

    public static int computeCost(LinkedList<Integer> tour, HashMap<Integer,double[]> cities){
        int cost = 0;
        int prev = tour.getLast();
        for(int city : tour){
            cost += (int)Math.round(distance(cities.get(prev),cities.get(city)));
            prev = city;
        }
        return cost;
    }


}
