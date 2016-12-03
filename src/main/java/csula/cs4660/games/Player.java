package csula.cs4660.games;
import java.util.*;
import java.util.Map.Entry;

import csula.cs4660.games.models.MiniMaxState;

import java.io.*;
import java.math.*;


class Player {
	static int floodFillCount = 0;
	static Node rootNode = null;
	static Integer Alpha = Integer.MIN_VALUE;
	static Integer Beta = Integer.MAX_VALUE;
	static Map <XY,Tile> tileMap = new HashMap<XY,Tile>();
	static int P;
    private long startTime;
    static final int rows = 20;
    static final int columns = 30;
    public static void main(String args[]) {
    	  Scanner in = new Scanner(System.in);
        String currentMove = "", previousMove = "";
    	long startTime = System.currentTimeMillis();
    	//code
        int board[][] = new int[rows][columns];
        Graph graphs = parseIntoTiles(board);
        long endTime = System.currentTimeMillis();
    	System.err.println("Took "+(endTime - startTime) + " ms");
    	List<XY> opponentsLocations = new ArrayList<XY>();
        while (true) {
            int N = in.nextInt(); // total number of players (2 to 4).
            P = in.nextInt(); // your player number (0 to 3).
            int currentRow =0;
            int currentColumn = 0;
            for (int i = 0; i < N; i++) {
                int X0 = in.nextInt(); // starting X coordinate of lightcycle (or -1)
                int Y0 = in.nextInt(); // starting Y coordinate of lightcycle (or -1)
                int X1 = in.nextInt(); // starting X coordinate of lightcycle (can be the same as X0 if you play before this player)
                int Y1 = in.nextInt(); // starting Y coordinate of lightcycle (can be the same as Y0 if you play before this player)
                board[Y1][X1] = i+1;
                Optional<Node> beforeUpdatedNode =  graphs.getNode(new Node<Tile>(new Tile(Y1,X1,"0")));
                Node<Tile> converted = ((Node<Tile>) beforeUpdatedNode.get());
                converted.setData(new Tile(Y1,X1,(i+1)+""));
                
              //((MiniMaxState) graph.getNode(source).get().getData()).setValue(((MiniMaxState)bestValue.getData()).getValue());
                if(i == P){ 
                    System.err.println(X0+"-"+Y0+"-"+X1+"-"+Y1);
                    currentRow = Y1;
                    currentColumn = X1;
                }
                else{
                	opponentsLocations.add(new XY(Y1, X1));
                }
            }
            Graph graphMinMax = new Graph();
            graphMinMax = buildGraph(board,currentColumn,currentRow,opponentsLocations);
            String initialState = currentRow+"+"+currentColumn;
        	for(XY eachOpponentLocation : opponentsLocations){
        	initialState += "#" + eachOpponentLocation.getX() + "+" + eachOpponentLocation.getY();
        	}
            Node best = getBestMove(graphMinMax, new Node<MiniMaxState>(new MiniMaxState(board, 0 , initialState)), 4, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
            //debugBoard(board);
            currentMove = avoidBlock(board, currentRow+"+"+currentColumn,previousMove);
//            int rowInitial = Integer.parseInt(initialState.split("#")[P].split("\\+")[0]);
//            int columnInitial = Integer.parseInt(initialState.split("#")[P].split("\\+")[1]);
//            int row = Integer.parseInt(((MiniMaxState) best.getData()).getRecentMoves().split("#")[P].split("\\+")[0]);
//            int column = Integer.parseInt(((MiniMaxState) best.getData()).getRecentMoves().split("#")[P].split("\\+")[1]);
//            currentMove = getMove(rowInitial,columnInitial,row,column);
            System.out.println(currentMove);
            previousMove = currentMove;
            opponentsLocations =  new ArrayList<XY>();
             // A single line with UP, DOWN, LEFT or RIGHT
        }
    }
    private static String avoidBlock(int[][] board, String currentXY,String previousMove) {
	// TODO Auto-generated method stub
    	HashMap<String , String > possibleMoves = getPossibleMoves(board,currentXY);
    	if(possibleMoves.containsKey(previousMove)){
    	return previousMove;
    	}
    	for(String eachPossibleMove: possibleMoves.keySet()){
    	return eachPossibleMove;
    	}
    	return "Right";
	}
    
    private static HashMap<String,String> getPossibleMoves(int[][] board, String currentXY) {
        HashMap<String,String> possibleMoves = new HashMap<String, String>();
    	String XY[] = currentXY.split("\\+");
    	int row = Integer.parseInt(XY[0]);
    	int column = Integer.parseInt(XY[1]);
    	
    	if(row + 1 < rows && board[row +1][column] < 1){
    	possibleMoves.put("DOWN",(row+1)+"+"+column);
		}
		if((row -1 < rows && row -1 >= 0) && board[row  - 1][column] < 1){
		possibleMoves.put("UP",(row-1)+"+"+column);
		}
		if(column + 1 < columns && board[row][column + 1] < 1){
		possibleMoves.put("RIGHT",row+"+"+(column+1));
		}
		if((column - 1 < columns && column - 1 >= 0) && board[row][column - 1] < 1){
		possibleMoves.put("LEFT",row+"+"+(column-1));
		}
    	return possibleMoves;
    }
    private static String getMove(int rowInitial, int columnInitial, int row, int column) {
    	System.err.println(rowInitial + "-"+columnInitial + "-" + row + "-" + column);
	if(row < rowInitial ){
	return "UP";
	}
	else if(row > rowInitial ){
	return "DOWN";
	}
	else if(column < columnInitial ){
	return "LEFT";
	}
	else{
	return "RIGHT";
	}
	
	}
	public static int[][] deepCopyIntMatrix(int[][] input) {
	    if (input == null)
	        return null;
	    int[][] result = new int[input.length][];
	    for (int r = 0; r < input.length; r++) {
	        result[r] = input[r].clone();
	    }
	    return result;
	}
    private static Graph buildGraph(int[][] board, int currentColumn, int currentRow,
	List<XY> opponentsLocations) {
    	Graph graphMinMax = new Graph();
    	String initialState = currentRow+"+"+currentColumn;
    	for(XY eachOpponentLocation : opponentsLocations){
    	initialState += "#" + eachOpponentLocation.getX() + "+" + eachOpponentLocation.getY();
    	}
    	
    	long startTime = System.currentTimeMillis();
    	
    	
    	Node<MiniMaxState> start = new Node<MiniMaxState>(new MiniMaxState(board,0,initialState));
    	Queue<Node<MiniMaxState>> frontier = new LinkedList<Node<MiniMaxState>>();
        frontier.add(start);
    	

        TreeMap<String, String> possibleMoves = new TreeMap<String, String>();
        Node<MiniMaxState> lastNode = start;
        int level = 3;
    	while(level > 0 && !frontier.isEmpty()){
    	Node<MiniMaxState> currentNode = frontier.poll();
    	if(currentNode.equals(lastNode)){
    	lastNode = null;
    	level -=1;
    	}
    	graphMinMax.addNode(currentNode);
    	possibleMoves = generatePossibleMove(currentNode);
	        graphMinMax = addPossibleStates(possibleMoves, graphMinMax,currentNode);

	        int count = 0;
	        
    	for(Node<MiniMaxState> eachNode: graphMinMax.neighbors(currentNode)){
    	frontier.add(eachNode);
    	if(count == graphMinMax.neighbors(currentNode).size() - 1){
    	if(lastNode == null){
    	lastNode = eachNode;
    	}
    	}
    	        count += 1;
    	}
    	}
    	
    	
    	
    	long endTime = System.currentTimeMillis();
    	System.err.println("Took this much for graph build"+(endTime - startTime) + " ms");
        
        
        
	return graphMinMax;
	}

	private static TreeMap<String, String> generatePossibleMove(Node<MiniMaxState> start) {
	// TODO Auto-generated method stub
	String players[] = start.getData().getMoves().split("#");
	TreeMap<String, String> possibleMoves = new TreeMap<String,String>();
	int i = 0;
        for(String eachPlayer : players){
        	String x = eachPlayer.split("\\+")[0];
        	String y = eachPlayer.split("\\+")[1];
        	if(i == 0){
        	 possibleMoves = getPossibleMovesTreeMap(start.getData().getState(), x +"+"+ y );
        	}else{
        	possibleMoves = findPossibleCombinations(possibleMoves,getPossibleMovesTreeMap(start.getData().getState(),x +"+"+ y));
        	}
        	i++;
        }
	return possibleMoves;
	}
	private static TreeMap<String,String> getPossibleMovesTreeMap(int[][] board, String currentXY) {
        TreeMap<String,String> possibleMoves = new TreeMap<String, String>();
    	String XY[] = currentXY.split("\\+");
    	int row = Integer.parseInt(XY[0]);
    	int column = Integer.parseInt(XY[1]);
    	
    	if(row + 1 < rows && board[row +1][column] < 1){
    		possibleMoves.put("DOWN",(row+1)+"+"+column);
		}
		if((row -1 < rows && row -1 >= 0) && board[row  - 1][column] < 1){
			possibleMoves.put("UP",(row-1)+"+"+column);
		}
		if(column + 1 < columns && board[row][column + 1] < 1){
			possibleMoves.put("RIGHT",row+"+"+(column+1));
		}
		if((column - 1 < columns && column - 1 >= 0) && board[row][column - 1] < 1){
			possibleMoves.put("LEFT",row+"+"+(column-1));
		}
		
		return possibleMoves;
    }
	private static Graph addPossibleStates(TreeMap<String, String> possibleMoves, Graph graphMinMax,
	Node<MiniMaxState> start) {
	int boardCopy[][];
	for(Map.Entry<String, String> entry1 :  possibleMoves.entrySet()){
        	String playerMoves[]  = entry1.getValue().split("#");
        	 boardCopy = deepCopyIntMatrix(start.getData().getState());
        	for(int i = 1; i <= playerMoves.length ; i++ ){
        	int x = Integer.parseInt(playerMoves[i-1].split("\\+")[0]);
        	int y = Integer.parseInt(playerMoves[i-1].split("\\+")[1]);
        	boardCopy[x][y] = i;
        	}
        	Node<MiniMaxState> child = new Node<MiniMaxState>(new MiniMaxState(boardCopy, 0,entry1.getValue()));
        	graphMinMax.addNode(child);
        	graphMinMax.addEdge(new Edge(start,child,1));
        }
	return graphMinMax;
	}

	private static TreeMap<String, String> findPossibleCombinations(TreeMap<String, String> possibleMoves1,
	TreeMap<String, String> possibleMoves2) {
    	TreeMap<String,String> result = new TreeMap<String, String>();
    	for(Map.Entry<String, String> entry1 : possibleMoves1.entrySet()){
    	for(Map.Entry<String, String> entry2 : possibleMoves2.entrySet()){
    	String key = entry1.getKey() + "#"+entry2.getKey();
    	String value = entry1.getValue() + "#" + entry2.getValue();
    	result.put(key, value);
        	}
    	}
    	
	return result;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Node getBestMove(Graph graph, Node<MiniMaxState> source, Integer depth, Integer alpha, Integer beta, Boolean max) {
	        // TODO: implement your alpha beta pruning algorithm here

	    	if(rootNode == null){
	    	rootNode = source;
	    	}
	    	Node bestValue;
	        if (depth == 0 || graph.neighbors(source).size() == 0) {
//	            evaluvateState(source);
//	            int value = floodFillCount;
//	            source.getData().setValue(value);
	        	return source; // return a number
	        }

	        if (max) {
	            bestValue =new Node<>(new MiniMaxState(null, Integer.MIN_VALUE,"")); // negative infinite
	            for (Node eachNode: graph.neighbors(source)) {
	                Node value = getBestMove(graph,eachNode, depth - 1, alpha, beta, !max);
	                if(alpha < ((MiniMaxState)value.getData()).getValue() ){
	       	 alpha = ((MiniMaxState)value.getData()).getValue();
	       	if(beta < alpha){
	       	bestValue = compareNodesMin(bestValue, value);
	       	break;
	       	}
	       	 	}
	                bestValue = compareNodesMax(bestValue, value);
	            }
	            if( !(((MiniMaxState) source.getData()).getMoves()).equals(((MiniMaxState) rootNode.getData()).getMoves())){
	            bestValue = new Node(new MiniMaxState( ((MiniMaxState) source.getData()).getState() , ((MiniMaxState)bestValue.getData()).getValue(), ((MiniMaxState) source.getData()).getMoves() ));
	            }
	            ((MiniMaxState) graph.getNode(source).get().getData()).setValue(((MiniMaxState)bestValue.getData()).getValue());
	            return bestValue;
	        } else {
	        	bestValue =new Node<>(new MiniMaxState(null, Integer.MAX_VALUE,"")); // positive infinite
	        	 for (Node eachNode: graph.neighbors(source)) {
	        	 Node value = getBestMove(graph,eachNode, depth - 1, alpha, beta, !max);
	        	 if(beta > ((MiniMaxState)value.getData()).getValue() ){
	        	 beta = ((MiniMaxState)value.getData()).getValue();
	        	 if(beta < alpha){
	        	 bestValue = compareNodesMin(bestValue, value);
	        	 break;
	        	 }
	        	 }
	                bestValue = compareNodesMin(bestValue, value);
	            }
	        	 if( !(((MiniMaxState) source.getData()).getMoves()).equals(((MiniMaxState) rootNode.getData()).getMoves())){
	 	            bestValue = new Node(new MiniMaxState( ((MiniMaxState) source.getData()).getState() , ((MiniMaxState)bestValue.getData()).getValue(), ((MiniMaxState) source.getData()).getMoves() ));
	 	            }
	        	  ((MiniMaxState) graph.getNode(source).get().getData()).setValue(((MiniMaxState)bestValue.getData()).getValue());
	            return bestValue;
	        }
	       
	    }
	private static Node compareNodesMin(Node<MiniMaxState> bestValue, Node<MiniMaxState> value) {
		if(((MiniMaxState) bestValue.getData()).getValue() < value.getData().getValue()){
		return bestValue;
		}
		return value;
		}

		private static Node<MiniMaxState> compareNodesMax(Node<MiniMaxState> bestValue, Node<MiniMaxState> value) {
		if(( bestValue.getData()).getValue() > value.getData().getValue()){
		return bestValue;
		}
		return value;
		}
	class MiniMaxState {
		private int[][] state;
		private String moves;
		private int value;
		
		public MiniMaxState(int[][] state, int value, String moves) {
			this.state = state;
			this.moves = moves;
			this.value = value;
		}
		public int[][] getState() {
			return state;
		}
		public void setState(int[][] state) {
			this.state = state;
		}
		public String getMoves() {
			return moves;
		}
		public void setMoves(String moves) {
			this.moves = moves;
		}
		public int getValue() {
			return value;
		}
		public void setValue(int value) {
			this.value = value;
		}
		@Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (!(o instanceof MiniMaxState)) return false;

	        MiniMaxState that = (MiniMaxState) o;

	        return Arrays.deepEquals(getState(), that.getState());
	    }
	    
	    @Override
	    public int hashCode() {
	    	return Arrays.deepHashCode(getState());
	    }
		
		


	}


class Graph {
	private Map<Node<Integer>, Collection<Edge>> adjacencyList = new HashMap<Node<Integer>,Collection<Edge>>();
    Node<Integer> fromNode;
	Node<Integer> toNode;
	Edge fromEdge;
	Edge toEdge;
	public boolean adjacent(Node x, Node y) {
    	Iterator entries = adjacencyList.entrySet().iterator();
    	while(entries.hasNext())
    	{
    		Entry thisEntry = (Entry)entries.next();
    		Object key = thisEntry.getKey();
    		Object value = thisEntry.getValue();
    		Collection<Edge> edge = (Collection<Edge>)value;
    		Node currentNode = (Node)key;
    		for(Edge e:edge)
    		{
    			if((x.equals(e.getFrom()) && y.equals(e.getTo())) ||
    					(x.equals(e.getTo()) && y.equals(e.getFrom())))
    			{
    			 	return true;
    			}
    		}
    	}
        return false;
    }
    public List<Node> neighbors(Node x) {
    	Collection<Edge> edges = adjacencyList.get(x);
    	List<Node> nodes = new ArrayList<Node>();
    	if(edges!=null)
    	{
	    	for(Edge edge:edges)
	     	{
	    		nodes.add(edge.getTo());
	    	}
    	}
        return nodes;
    }
    public boolean addNode(Node x) {
    	if(adjacencyList.get(x)==null)
    	{
    		adjacencyList.put(x, null);
    		return true;
    	}
        return false;
    }
    public boolean addEdge(Edge x) {
    	Node from = x.getFrom();
    	Node to = x.getTo();
    	boolean present = false;
    	Collection<Edge> edges = new ArrayList<Edge>();
    	if(adjacencyList.containsKey(from)){
    		if(adjacencyList.get(from)!=null){
    			edges = adjacencyList.get(from);
    		}
    		if(edges != null){
		    	for(Edge edge : edges)
		    	{
		    		if(edge.getFrom().equals(from) && edge.getTo().equals(to))
		    			present = true;
		    	}
    		}
    	}
    	if(present)
    		return false;
    	else
    	{
    		edges.add(x);
    		adjacencyList.put(x.getFrom(), edges);
    		return true;
    	}  
    }

    public int distance(Node from, Node to) {
    	Collection<Edge> allEdges = adjacencyList.get(from);
    	for(Edge e:allEdges){
    		if(e.getTo().equals(to)){
    			return e.getValue();
    		}
    	}
        return 0;
    }

    public Optional<Node> getNode(Node node) {
    	Iterator iterator = adjacencyList.keySet().iterator();
        Optional<Node> result = Optional.empty();
        while (iterator.hasNext()) {
            Node next = (Node)iterator.next();
            if (next.equals(node)) {
                result = Optional.of(next);
            }
        }
        return result;
    }
}


class Edge {
    private Node from;
    private Node to;
    private int value;

    public Edge(Node from, Node to, int value) {
        this.from = from;
        this.to = to;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Node getFrom() {
        return from;
    }

    public void setFrom(Node from) {
        this.from = from;
    }

    public Node getTo() {
        return to;
    }

    public void setTo(Node to) {
        this.to = to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Edge)) return false;

        Edge edge = (Edge) o;

        if (getValue() != edge.getValue()) return false;
        if (getFrom() != null ? !getFrom().equals(edge.getFrom()) : edge.getFrom() != null)
            return false;
        return !(getTo() != null ? !getTo().equals(edge.getTo()) : edge.getTo() != null);

    }
}
class Tile {
    private final int x;
    private final int y;
    private final String type;

    public Tile(int x, int y, String type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getType() {
        return type;
    }
}
class Node<T> {
    private T data;

    public Node(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

	public void setData(T data) {
		this.data = data;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + getOuterType().hashCode();
		result = prime * result + ((data == null) ? 0 : data.hashCode());
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
		Node other = (Node) obj;
		if (!getOuterType().equals(other.getOuterType()))
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		return true;
	}

	private Player getOuterType() {
		return Player.this;
	}
    
}
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

