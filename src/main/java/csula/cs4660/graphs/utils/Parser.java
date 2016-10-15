package csula.cs4660.graphs.utils;

import csula.cs4660.games.models.Tile;
import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;
import csula.cs4660.graphs.representations.AdjacencyList;
import csula.cs4660.graphs.representations.AdjacencyMatrix;
import csula.cs4660.graphs.representations.Representation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

/**
 * A quick parser class to read different format of files
 */
public class Parser {
	static List<Tile> tiles = new ArrayList<Tile>();
    public static Graph readRectangularGridFile(Representation.STRATEGY graphRepresentation, File file) {
        Graph graph = new Graph(Representation.of(graphRepresentation));
        AdjacencyList adjacencyList;
        AdjacencyMatrix adjacencyMatrix;
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
	        				tiles.add(new Tile(row,column,eachTile));
	        				column += 1;
        				}
        			}
        			row += 1;
        		}
        	}
        	scanner.close();
        }
        catch(FileNotFoundException fe){
        	System.out.println("Sorry! File not found.");
        }
        if(graphRepresentation.equals("ADJACENCY_LIST")){
       	 	adjacencyList = new AdjacencyList();
        }
        if(graphRepresentation.equals("ADJACENCY_MATRIX")){
        	adjacencyMatrix = new AdjacencyMatrix(tiles.size());
        }
        List<Character> direction = new ArrayList<Character>();
        List<Edge> allEdges = new ArrayList<Edge>();
        direction.add('N'); direction.add('E'); direction.add('S'); direction.add('W');
        for(Tile eachTile:tiles){
        	for(Character dir:direction){
        		Tile tile = getTileInDirection(eachTile,dir);
        		if(tile!=null){
        			Node from = new Node(eachTile.getX()+"-"+eachTile.getY()+"-"+eachTile.getType());
        			Node to = new Node(tile.getX()+"-"+tile.getY()+"-"+tile.getType());
        			graph.addNode(from);
        			graph.addNode(to);
        			Edge e = new Edge(from,to,1);
        			if(graph.addEdge(e)){
        				allEdges.add(e);
        			}
        		}
        	}
        }
        System.out.println("done");
        // TODO: implement the rectangular file read and add node with edges to graph
        return graph;
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
		// TODO Auto-generated method stub
		for(Tile tile:tiles){
			if(tile.getX()==x && tile.getY()==y){
				return tile;
			}
		}
		return null;
	}

	public static String converEdgesToAction(Collection<Edge> edges) {
        // TODO: convert a list of edges to a list of action
        return "";
    }
}
