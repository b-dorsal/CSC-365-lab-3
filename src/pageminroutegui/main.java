package pageminroutegui;
//Brian Dorsey 2016

import java.io.IOException;

public class main {
    //Program loads Wikipedia pages from web, loads them into graph, and saves graph to serialized file.
    //Loads graph from file for speed, or from web.
    //UI allows for finding of shortest path between 2 pages by dijkstra's algorithm.
    public static void main(String[] args) throws IOException {
        graphLoader loading = new graphLoader();
        loading.show();
    }
}
