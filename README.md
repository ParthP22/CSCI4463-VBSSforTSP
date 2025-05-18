# CSCI4463-VBSSforTSP

Implemented VBSS to solve the TSP for a given .tsp file. Final homework assignment of Stockton University's Artificial Intelligence (CSCI 4463) course.

To run on command line do: java Main [TSP filename with .tsp] [bias] [iterations]

Note: bias and iterations are optional. There are already default values set for those in the code. The default values are: bias = 10.0, iterations = 1000.


**Example**: java Main dj38.tsp 

This will run VBSS on dj38.tsp for default values of bias and iterations.


**Example**: java Main dj38.tsp " " 10000

This will run VBSS on dj38.tsp for 10000 iterations with default bias.

Note: an empty string was used to skip over the bias parameter in this one.


**Example**: java Main dj38.tsp 15

This will run VBSS on dj38.tsp for default iterations with bias = 15.0.


**Example**: java Main dj38.tsp 15 10000

This will run VBSS on dj38.tsp for 10000 iterations with bias = 15.0.
