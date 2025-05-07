import java.io.IOException;
import java.util.*;

/**
 * This file contains the methods required to perform the Value-Biased
 * Stochastic Sampling algorithm on a given TSP file to calculate a tour
 * with minimized cost.
 *
 * @author Parth Patel, Ahmed Malik, James Calabrese
 */
public class VBSS {

    /**
     * This is the method that is called in order to perform VBSS on a
     * given TSP file. Whether or not the optimal tour is obtained is
     * dependent on chance as well as the number of iterations performed.
     *
     * @param filename the name of the TSP file that this algorithm will be performed on.
     * @param bParam the value of the bias parameter for VBSS, obtained from the command line arguments. Default value = 10.0.
     * @param solutions the number of solutions created by VBSS, obtained from the command line arguments. Default value = 1000.
     * @throws IOException
     */
    public static void doVBSS(String filename, String bParam, String solutions) throws IOException {
        // Default value of the bias parameter for VBSS
        double bias = 10.0;

        // If there was a bias parameter specified in the command line arguments,
        // then update the bias parameter here.
        if(!bParam.trim().equals("")){
            bias = Double.parseDouble(bParam);
        }

        // Default value for the number of solutions that VBSS will create.
        int iterations = 1000;

        // If there was an iterations parameter specified in the command line arguments,
        // then update the number of iterations here.
        if(!solutions.trim().equals("")){
            iterations = Integer.parseInt(solutions);
        }

        // bestCost will represent the smallest cost obtained from all the solutions created.
        // We initialize it to be positive infinity.
        int bestCost = Integer.MAX_VALUE;

        // bestTour is a LinkedList containing the tour with the smallest cost obtained from
        // the solutions created. The first element will be the starting point of the tour
        // and the last element will be the starting point of the tour also, since it comes back
        // to the start.
        LinkedList<Integer> bestTour = new LinkedList<>();

        // This will contain all of the cities from the parsed TSP file mapped to their corresponding coordinates.
        HashMap<Integer,double[]> cities = TSPFileParser.parseTSPFile(filename);

        // This loop will generate N-number of solutions using VBSS and pick the best tour.
        for(int i = 0; i < iterations; i++) {

            // Create a copy of the cities HashMap.
            HashMap<Integer, double[]> currCities = new HashMap<Integer,double[]>();
            for(Map.Entry<Integer,double[]> city : cities.entrySet()){
                currCities.put(city.getKey(),city.getValue());
            }
            // Initialize a new LinkedList for the current tour.
            LinkedList<Integer> tour = new LinkedList<Integer>();

            // Pick a random city to start from
            // Note: we don't always start with the first city in the TSP file, because
            // picking a random city will allow us to stumble upon the optimal
            // tour, since the optimal tour may not necessarily start with the
            // the first city in the TSP file.
            int currCity = (int)(Math.random() * cities.size()) + 1;

            // Add the starting point into the current tour.
            tour.add(currCity);

            // The cost of the current tour is 0.
            int cost = 0;

            // We will iterate over the remaining cities until there is only one city left.
            while (currCities.size() > 1) {
                // Generate a random probability.
                double randomNum = Math.random();

                // Determine the next city and the cost from the current city to that next city.
                int[] nextCityAndCost = calculateProbabilities(currCities, currCity, bias, randomNum);

                // Update the current cost of the tour.
                cost += nextCityAndCost[1];

                // Add the new city into the tour.
                tour.add(nextCityAndCost[0]);

                // Make the current city the new city (essentially updating the pointer).
                currCity = nextCityAndCost[0];
            }

            // Find cost between last city in tour and the first city, thus completing the entire tour.
            cost += (int)Math.round(distance(cities.get(tour.getLast()),cities.get(tour.getFirst())));

            // Add the first city of the tour to the end to signify that a tour has been completed.
            tour.add(tour.getFirst());

            // If the newly completed tour is better than the best tour we've seen, we update the best tour.
            if(cost < bestCost){
                bestCost = cost;
                bestTour = tour;
            }
        }

        // After the iterations are complete, we print the best tour in space-delimited format.
        System.out.println("Tour: ");
        for(Integer city : bestTour){
            System.out.println(city);
        }
        System.out.println("Cost: " + bestCost);
    }

    /**
     * This method will calculate the probabilities of the next city to be picked
     * based on the current city given. The heuristic used to calculate the probability
     * is the Euclidean distance heuristic rounded to the nearest integer.
     *
     * @param cities <code>HashMap</code> containing the remaining cities to picked.
     * @param city the current city that the algorithm has reached.
     * @param bias the bias parameter for VBSS.
     * @param randomNum a randomly generated number that will be used to determine which city is picked next.
     * @return <code>int[]</code> of size two, with the first element being the next city that is picked and
     * the second element being the cost from the current city to the next city.
     */
    private static int[] calculateProbabilities(HashMap<Integer,double[]> cities, int city, double bias, double randomNum){
        // The divisor will be the sum of 1/(H(C)^B) for all remaining cities C. This will be
        // what we divide the 1/(H(C)^B) value by for each city to calculate the probabilities.
        double divisor = 0;

        // The coordinates of the current city that the algorithm is at.
        double[] cityCoords = new double[]{cities.get(city)[0],cities.get(city)[1]};

        // Remove the current city from the cities HashMap so that we don't iterate over it by accident.
        cities.remove(city);

        // A matrix of size K x 2, where K is the number of remaining cities.
        // The first column will be the ID of the city and the second column will be the value of 1/(H(C)^B)
        // for the corresponding city C. Later, we will divide the values in the second column by the divisor.
        // to obtain the probability of the city getting picked.
        double[][] probabilities = new double[cities.size()][2];

        // Iterate over the remaining cities and calculate the heuristic value for each.
        int i = 0;
        for(Map.Entry<Integer,double[]> entry : cities.entrySet()){
            probabilities[i][0] = entry.getKey();
            probabilities[i][1] = 1/Math.pow((int)Math.round(distance(cityCoords,entry.getValue())),bias);
            divisor += probabilities[i][1];
            i++;
        }


        // In order to figure out which probability interval that randomNum falls into, we will use this
        // variable to keep track as we iterate over the probability values for each city.
        double probabilityInterval = 0;

        // Iterate over the remaining cities and calculate their probability values.
        for(i = 0; i < probabilities.length; i++){
            // We will divide the value 1/(H(C)^B) of the city C by the divisor calculated earlier to obtain the
            // probability of this city getting picked.
            probabilities[i][1] = probabilities[i][1]/divisor;

            // If the randomNum is in the probability interval of this city, then we will immediately
            // return this city and it's cost from the previous city to it.
            // The cost will simply be the Euclidean distance between both cities rounded to the nearest integer.
            if(randomNum >= probabilityInterval && randomNum < probabilityInterval + probabilities[i][1]){
                return new int[]{(int)probabilities[i][0],(int)Math.round(distance(cityCoords,cities.get((int)probabilities[i][0])))};
            }

            // Otherwise, we will update the probability interval and move onto the next city.
            probabilityInterval += probabilities[i][1];

        }

        // If the loop completes and we still haven't returned, that means randomNum picked the final city.
        return new int[]{(int)probabilities[probabilities.length-1][0],(int)Math.round(distance(cityCoords,cities.get((int)probabilities[probabilities.length-1][0])))};

    }

    /**
     * Computes the Euclidean distance from the coordinates of city1 to
     * the coordinates of city2.
     * @param city1 the coordinates of city 1
     * @param city2 the coordinates of city 2
     * @return a <code>double</code> value for the distance between the two cities.
     */
    private static double distance(double[] city1, double[] city2){
        return Math.sqrt((city1[0] - city2[0])*(city1[0] - city2[0]) +
                (city1[1] - city2[1])*(city1[1] - city2[1]));
    }

    /**
     * A method used to check the cost of the given tour. Used for debugging.
     * @param tour a <code>LinkedList</code> containing the given tour.
     * @param cities a <code>HashMap</code> containing all the cities and their corresponding coordinates.
     * @return an <code>int</code> value representing the cost of the tour.
     */
    private static int computeCost(LinkedList<Integer> tour, HashMap<Integer,double[]> cities){
        int cost = 0;
        int prev = tour.getLast();
        for(int city : tour){
            cost += (int)Math.round(distance(cities.get(prev),cities.get(city)));
            prev = city;
        }
        return cost;
    }


}
