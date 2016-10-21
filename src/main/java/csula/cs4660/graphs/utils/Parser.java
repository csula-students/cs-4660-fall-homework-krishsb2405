package csula.cs4660.graphs.utils;

import csula.cs4660.games.models.Tile;
import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;
import csula.cs4660.graphs.representations.Representation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * A quick parser class to read different format of files
 */
public class Parser {
	static HashMap<String,Tile> tiles = new HashMap<String,Tile>(); 
	static List<Tile> tilesList = new ArrayList<Tile>();
	
    public static Graph readRectangularGridFile(Representation.STRATEGY graphRepresentation, File file) {
    	Collection<Edge> edges = new ArrayList<Edge>();
        Graph graph = new Graph(Representation.of(graphRepresentation));
        try{
        	Scanner scanner = new Scanner(file);
        	int row=0,column = 0;
        	while(scanner.hasNextLine()){
        		String nextLine = scanner.nextLine();
        		if(!nextLine.contains("+"))
        		{
        			column = 0;
        			for(int i=0;i<nextLine.length()-1; i ++){
        				if(nextLine.charAt(i) != '|'){
        					
	        				String eachTile = ""+nextLine.charAt(i)+""+nextLine.charAt(i+1);
	        				i += 1;
	        				Tile t = new Tile(column,row,eachTile);
	        				tilesList.add(t);
	        				tiles.put(column+""+row,t);
	        				column += 1;
        				}
        			}
        			row += 1;
        		}
        	}
        	
        	for(Tile eachTile:tilesList){
        		Tile north = getTileInDirection(eachTile, 'N');
        		Tile east = getTileInDirection(eachTile, 'E');
        		Tile west = getTileInDirection(eachTile, 'W');
        		Tile south = getTileInDirection(eachTile, 'S');
        		
        		createNodesAndEdge(graph,eachTile,north,edges);
        		createNodesAndEdge(graph,eachTile,east,edges);
        		createNodesAndEdge(graph,eachTile,west,edges);
        		createNodesAndEdge(graph,eachTile,south,edges);
        		
        	}
        	scanner.close();
        }
        catch(FileNotFoundException fe){
        	System.out.println("Sorry! File not found.");
        }
        System.out.println("done "+file.getAbsolutePath());
        return graph;
    }

    
    private static void createNodesAndEdge(Graph graph,Tile eachTile ,Tile tile, Collection<Edge> edges) {
		// TODO Auto-generated method stub
    	if(tile!=null){
			Node<Tile> from = new Node<Tile>(eachTile);
			Node<Tile> to = new Node<Tile>(tile);
			graph.addNode(from);
			graph.addNode(to);
			createEdge(graph,from,to,edges);
		}
	}

	private static void createEdge(Graph graph, Node<Tile> from, Node<Tile> to, Collection<Edge> edges) {
		// TODO Auto-generated method stub
		Edge e = new Edge(from,to,1);
		if(graph.addEdge(e)){
			edges.add(e);
		}
		System.out.println(edges.size());
	}


	private static Tile getTileInDirection(Tile eachTile,Character direction) {
		// TODO Auto-generated method stub
    	Tile tile = null;
    	switch(direction){
    	case 'N':
    		tile = getTile(eachTile.getX(),eachTile.getY()-1);
    		break;
    	case 'E':
    		tile = getTile(eachTile.getX()+1,eachTile.getY());
    		break;
    	case 'S':
    		tile = getTile(eachTile.getX(),eachTile.getY()+1);
    		break;
    	case 'W':
    		tile = getTile(eachTile.getX()-1,eachTile.getY());
    		break;
    	}
		return tile;
	}

	private static Tile getTile(int x, int y) {
		String key = ""+x+""+y;
		return tiles.get(key);
	}

	public static String converEdgesToAction(Collection<Edge> edges) {
        // TODO: convert a list of edges to a list of action
		String direction = "";
        for(Edge eachEdge : edges){
        	Node<Tile> from = eachEdge.getFrom();
        	Node<Tile> to = eachEdge.getTo();
        	if(from.getData().getX() < to.getData().getX()){
        		direction += "E";
        	}else if(from.getData().getX() > to.getData().getX()){
        		direction += "W";
        	}else if(from.getData().getY() < to.getData().getY()){
        		direction += "S";
        	}else{
        		direction += "N";
        	}
        }
        System.out.println(direction);
        return direction;
    }
}
