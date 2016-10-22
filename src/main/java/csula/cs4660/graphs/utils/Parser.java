package csula.cs4660.graphs.utils;

import csula.cs4660.games.models.Tile;
import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;
import csula.cs4660.graphs.representations.Representation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A quick parser class to read different format of files
 */

class XY{
	final int x;
	final int y;
	public XY(int x, int y){
		this.x = x;
		this.y = y;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		XY other = (XY) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	
}
public class Parser {
	static HashMap<XY,Tile> tiles = new HashMap<XY,Tile>(); 
    public static Graph readRectangularGridFile(Representation.STRATEGY graphRepresentation, File file) {
        Graph graph = new Graph(Representation.of(graphRepresentation));
        BufferedReader buffer;
    	List<String> lines = new ArrayList<String>();
    	try{
    		buffer = new BufferedReader(new FileReader(file));
    		String line;
    		while((line=buffer.readLine())!=null){
    			lines.add(line.substring(1, line.length()-1));
    		}
    		lines.remove(0);
    		lines.remove(lines.size()-1);
    	}catch(FileNotFoundException fe){
    		System.out.println("File not found.");
    	}catch(IOException io){
    		System.out.println("could not find IO");
    	}
    	for(int i = 0;i<lines.size();i++){
    		int pointer = 0;
    		for(int j = 0;j<lines.get(i).length()/2;j++){
    			String eachTile = lines.get(i).substring(pointer, pointer+2);
    			pointer+=2;
    			tiles.put(new XY(j, i), new Tile(j,i,eachTile));
    			graph.addNode(new Node<Tile>(new Tile(j,i,eachTile)));
    		}
    	}
    	for(Map.Entry<XY, Tile> eachTile:tiles.entrySet()){
    		Tile north = getTileInDirection(eachTile.getValue(), 'N');
    		Tile east = getTileInDirection(eachTile.getValue(), 'E');
    		Tile west = getTileInDirection(eachTile.getValue(), 'W');
    		Tile south = getTileInDirection(eachTile.getValue(), 'S');
    		
    		createEdge(graph,eachTile.getValue(),north);
    		createEdge(graph,eachTile.getValue(),east);
    		createEdge(graph,eachTile.getValue(),west);
    		createEdge(graph,eachTile.getValue(),south);
    	}
        return graph;
    }

    
    private static void createEdge(Graph graph,Tile eachTile ,Tile tile) {
		// TODO Auto-generated method stub
    	if(tile!=null){
			graph.addEdge(new Edge(new Node<Tile>(eachTile),new Node<Tile>(tile),1));
		}
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
		XY xy = new XY(x,y);
		return tiles.get(xy);
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
        return direction;
    }
}
