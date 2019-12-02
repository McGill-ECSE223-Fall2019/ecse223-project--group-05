package ca.mcgill.ecse223.quoridor.controller;

import java.util.ArrayList;

//class Edge{
//    public int node1;
//    public int node2;
//
//    Edge(int node1, int node2){
//        this.node1 = node1;
//        this.node2 = node2;
//    }
//}

public class Graph {
//    private ArrayList<Edge> edges = new ArrayList<Edge>();
    private ArrayList<ArrayList<Integer>> nodes = new ArrayList<ArrayList<Integer>>();

    public Graph(){
        //create the nodes;
        for (int i=0; i<81; i++) {
            //making 0, but leaving it empty
            nodes.add(new ArrayList<Integer>());
            if ((i - 9) >= 0) {
                //add the top node
                nodes.get(i).add(i - 9);
            }
            if (i % 9 != 8) {
                //add the right node
                nodes.get(i).add(i + 1);
            }
            if (i % 9 != 0) {
                //add the left node
                nodes.get(i).add(i - 1);
            }
            if ((i + 9) < 81) {
                //add the bottom node
                nodes.get(i).add(i + 9);
            }

        }
    }

    public static void main(String[] args) {
        Graph graph = new Graph();
        for (int i=0; i<81; i++){
            System.out.println(i + "" + graph.nodes.get(i));
        }
        graph.removeEdge(0,9);
        for (int i=0; i<81; i++){
            System.out.println(i + "" + graph.nodes.get(i));
        }
    }

    public ArrayList<ArrayList<Integer>> getNodes(){
        return nodes;
    }

    public Boolean removeEdge(int node1, int node2){
        Boolean removed = false;
        for (int i=0; i<getNodes().get(node1).size(); i++){
            if (getNodes().get(node1).get(i) == node2){
                getNodes().get(node1).remove(i);
                removed = true;
            }
        }
        for (int i=0; i<getNodes().get(node2).size(); i++){
            if (getNodes().get(node2).get(i) == node1){
                getNodes().get(node2).remove(i);
            }
        }
        return removed;
    }
//    public ArrayList<Edge> getEdges() {
//        return edges;
//    }
//    public int getNbEdges() {
//        return edges.size();
//    }
//    public void removeEdge(Edge edge){
//        nodes.get(edge.node1);
//    }
}
