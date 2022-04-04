package es.ucm.fdi.ici.c1920.practica3.grupo01;

import pacman.game.Game;

public class Quadrant
{
	public static final int MAX_X_COORD = 104;
	public static final int MAX_Y_COORD = 116;
	public static final int BLINKY = 0;
	public static final int PINKY = 1;
	public static final int INKY = 2;
	public static final int SUE = 3;
	public static final int PACMAN = 4; 
	
	private int x; 
    private int y;  
    private int nodeIndex; 
    
    public Quadrant(int x, int y, int nodeIndex) 
    {
    	this.x = x;
    	this.y = y;
    	this.nodeIndex = nodeIndex;
    }
    
    public static Quadrant[] setQuadrants(int size, Game game, int levelCount) 
    {
    	Quadrant[] q = new Quadrant[size];
    	/* This would be ideal, but there may not be a node with this coordinates in the maze, so we have to decide the center nodes by hand
    	q[BLINKY] = new Quadrant(MAX_X_COORD / 4, MAX_Y_COORD / 4, searchNode(MAX_X_COORD / 4, MAX_Y_COORD / 4, game));
    	q[PINKY] = new Quadrant((MAX_X_COORD * 3) / 4, MAX_Y_COORD / 4, searchNode((MAX_X_COORD * 3) / 4, MAX_Y_COORD / 4, game));
    	q[INKY] = new Quadrant(MAX_X_COORD / 4, (MAX_Y_COORD * 3) / 4, searchNode(MAX_X_COORD / 4, (MAX_Y_COORD * 3) / 4, game));
    	q[SUE] = new Quadrant((MAX_X_COORD * 3) / 4, (MAX_Y_COORD * 3) / 4, searchNode((MAX_X_COORD * 3) / 4, (MAX_Y_COORD * 3) / 4, game));
    	q[PACMAN] = new Quadrant(-1, -1, -1);
    	*/
    	switch(levelCount)
    	{
    	case 0:
	    	q[BLINKY] = new Quadrant(24, 28, searchNode(24, 28, game));
	    	q[PINKY] = new Quadrant(84, 28, searchNode(84, 28, game));
	    	q[INKY] = new Quadrant(24, 92, searchNode(24, 92, game));
	    	q[SUE] = new Quadrant(84, 92, searchNode(84, 92, game));
	    	break;
    	case 1:
	    	q[BLINKY] = new Quadrant(26, 28, searchNode(26, 28, game));
	    	q[PINKY] = new Quadrant(78, 28, searchNode(78, 28, game));
	    	q[INKY] = new Quadrant(24, 92, searchNode(24, 92, game));
	    	q[SUE] = new Quadrant(84, 92, searchNode(84, 92, game));
	    	break;
    	case 2:
	    	q[BLINKY] = new Quadrant(28, 24, searchNode(28, 24, game));
	    	q[PINKY] = new Quadrant(80, 24, searchNode(80, 24, game));
	    	q[INKY] = new Quadrant(24, 92, searchNode(24, 92, game));
	    	q[SUE] = new Quadrant(84, 92, searchNode(84, 92, game));
	    	break;
    	case 3:
	    	q[BLINKY] = new Quadrant(24, 20, searchNode(24, 20, game));
	    	q[PINKY] = new Quadrant(84, 20, searchNode(84, 20, game));
	    	q[INKY] = new Quadrant(24, 92, searchNode(24, 92, game));
	    	q[SUE] = new Quadrant(84, 92, searchNode(84, 92, game));
	    	break;
    	}
    	q[PACMAN] = new Quadrant(-1, -1, -1);
    	
    	return q;
    }
    
    public static int searchNode(int x, int y, Game game)
    {   	
		int node = -1;
    	for(int i = 0; i < game.getNumberOfNodes() && node == -1; ++i) 
		{
			if(game.getCurrentMaze().graph[i].x == x && game.getCurrentMaze().graph[i].y == y)
				node = game.getCurrentMaze().graph[i].nodeIndex;
		}
    	
    	return node;
    }
    
    public int getX()
    {
    	return x;
    }
    
    public int getY()
    {
    	return y;
    }
    
    public int getNodeIndex()
    {
    	return nodeIndex;
    }
    
    public void setX(int x)
    {
    	this.x = x;
    }
    
    public void setY(int y)
    {
    	this.y = y;
    }
    
    public void setNodeIndex(int nodeIndex)
    {
    	this.nodeIndex = nodeIndex;
    }
};
