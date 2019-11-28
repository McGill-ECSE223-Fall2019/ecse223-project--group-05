package ca.mcgill.ecse223.quoridor.controller;

import java.util.ArrayList;

class Edge{
    public int node1;
    public int node2;

    Edge(int node1, int node2){
        this.node1 = node1;
        this.node2 = node2;
    }
}

public class Graph {
    private ArrayList<Edge> edges = new ArrayList<Edge>();
    private ArrayList<Integer> nodes = new ArrayList<Integer>();

    public Graph(){
        //create the nodes;
        for (int i=1; i<82; i++){
            nodes.add(i);
        }

    }

}
