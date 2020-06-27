/**
 * @author Uri Hanunov
 * This class represent a puzzle object
 * The puzzle keepers some elements of his state such as path, cost and more
 * it has some function that we use to continue to next puzzles 
 */
public class Puzzle implements Comparable<Puzzle>
{
	public final Tile[][] puzzle; //the state of this puzzle
	public Tile[][] correctPuzzle; //the state of goal puzzle
	public String path = ""; //save the path to this state
	public int costAll = 0; //save the cost to this state
	public int zeroJ, zeroI; //show the place of the "_" (0)
	public int f; //for the heuristic function - h+g
	public String flag; //tell us if the node is "out" or not

	//constructors
	public Puzzle(Tile[][] puzzle, Tile[][] correctPuzzle)
	{
		this.puzzle = puzzle;
		this.correctPuzzle = correctPuzzle;
		findZeroTile(); //find zeroJ and zeroI
		f = this.costAll + h();
		flag = "x"; // x means no out
	}
	
	//Depth copy constructor
	public Puzzle(Puzzle newPuzzle)
	{
		puzzle = new Tile [newPuzzle.puzzle.length][newPuzzle.puzzle[0].length];
		for (int i = 0; i < puzzle.length; i++)
		{
			for (int j = 0; j < puzzle[i].length; j++)
			{
				puzzle[i][j] = new Tile(newPuzzle.puzzle[i][j]);
			}
		}
		correctPuzzle = newPuzzle.correctPuzzle;
		zeroJ = newPuzzle.zeroJ;
		zeroI = newPuzzle.zeroI;
		path = newPuzzle.path;
		costAll = newPuzzle.costAll;
		f = this.costAll + h();
		flag = "x"; 
	}


	/*
	 * check if this is the goal puzzle
	 */
	public boolean isSolved()
	{
		for (int i = 0; i < puzzle.length; i++)
		{
			for (int j = 0; j < puzzle[i].length; j++)
			{
				if (puzzle[i][j].num != correctPuzzle[i][j].num) //if one number is not in his place return false
				{
					return false;
				}
			}
		}
		return true;
	}


	/*
	 * check if we can doing this move
	 * gets the direction move
	 */
	public boolean canMove(String direction)
	{
		if(direction.equals("U"))
		{
			if (zeroI != 0) //not in the first line
			{
				if(!puzzle[zeroI-1][zeroJ].color.equals("Black")) //if not black color we can move it
					return true;
			}
		}
		if(direction.equals("D"))
		{
			if (zeroI != puzzle.length - 1) //not in the last line
			{
				if(!puzzle[zeroI+1][zeroJ].color.equals("Black")) //if not black color we can move it
					return true;
			}
		}
		if(direction.equals("L")) 
		{
			if (zeroJ != 0) //not in the first column
			{
				if(!puzzle[zeroI][zeroJ-1].color.equals("Black")) //if not black color we can move it
					return true;
			}
		}
		if(direction.equals("R")) 
		{
			if (zeroJ != puzzle[zeroI].length - 1) //not in the last column
			{
				if(!puzzle[zeroI][zeroJ+1].color.equals("Black")) //if not black color we can move it
					return true;
			}
		}
		return false;
	}

	/*
	 * moving the tile by the direction
	 * gets the direction move
	 */
	public void move(String direction)
	{
		if(direction.equals("U"))
		{
			swap(zeroI, zeroJ, (zeroI - 1), zeroJ);
			path += String.valueOf(puzzle[zeroI+1][zeroJ].num) + "D-"; //add to path this move
			costAll += puzzle[zeroI+1][zeroJ].cost; //add to cost the moving cost
			f = this.costAll + h(); //the new f
		}
		if(direction.equals("D"))
		{
			swap(zeroI, zeroJ, (zeroI + 1), zeroJ);
			path += String.valueOf(puzzle[zeroI-1][zeroJ].num) + "U-"; //add to path this move
			costAll += puzzle[zeroI-1][zeroJ].cost; //add to cost the moving cost
			f = this.costAll + h(); //the new f
		}
		if(direction.equals("L"))
		{
			swap(zeroI, zeroJ, zeroI, (zeroJ - 1));
			path += String.valueOf(puzzle[zeroI][zeroJ+1].num) + "R-"; //add to path this move
			costAll += puzzle[zeroI][zeroJ+1].cost; //add to cost the moving cost
			f = this.costAll + h(); //the new f
		}
		if(direction.equals("R"))
		{
			swap(zeroI, zeroJ, zeroI, (zeroJ + 1));
			path += String.valueOf(puzzle[zeroI][zeroJ-1].num) + "L-"; //add to path this move
			costAll += puzzle[zeroI][zeroJ-1].cost; //add to cost the moving cost
			f = this.costAll + h(); //the new f
		}
	}


	/*
	 * swap between 2 tiles
	 * gets the locations of the tiles and swap them
	 */
	private void swap(int i1, int j1, int i2, int j2)
	{
		Tile previous = puzzle[i1][j1];
		puzzle[i1][j1] = puzzle[i2][j2];
		puzzle[i2][j2] = previous;
		zeroI = i2; //the new zeroI
		zeroJ = j2; //the new zeroJ
	}

	/*
	 * found the empty tile and update zeroI and zeroJ
	 */
	private void findZeroTile()
	{
		for(int i = 0; i < puzzle.length; i++)
		{
			for(int j = 0; j < puzzle[i].length; j++)
			{
				if (puzzle[i][j].num == 0)
				{
					zeroI = i;
					zeroJ = j;
				}
			}
		}
	}
	
	@Override
	/*
	 * prints the puzzle
	 */
	public String toString()
	{
		String s="";
		for(int i = 0; i < puzzle.length; i++)
		{
			for (int j = 0; j < puzzle[i].length; j++)
			{
				s = s + puzzle[i][j].num + " ";
			}
			s = s + "\n";
		}
		return s;
	}
	
	/*
	 * make a string from the puzzle's numbers for being a key in hash table
	 */
	public String madeKeyForHash()
	{
		String s = "";
		for(int i = 0 ; i<puzzle.length ; i++)
		{
			for(int j = 0 ; j<puzzle[i].length ; j++)
			{
				s += puzzle[i][j].num;
			}
		}
		return s;
	}
	
	/*
	 * find the last step that we did
	 */
	public String parent()
	{
		String parent = "x"; //random - this string is no matter
		if(!this.path.equals(""))
			parent = this.path.substring(this.path.length()-2,this.path.length()-1);
		return parent;
	}
	
	/*
	 * the heuristic function
	 */
	public int h()
	{
		int h=0; //the cost to the goal (around)
		for(int i = 0 ; i<puzzle.length ; i++)
		{
			for(int j = 0 ; j<puzzle[i].length ; j++)
			{
				int target = puzzle[i][j].num;
				for(int k = 0 ; k< puzzle.length ; k++)
					for(int l = 0 ; l< puzzle[i].length ; l++)
						if(correctPuzzle[k][l].num==target && puzzle[i][j].num!=0) //the place that the target needs to be
							//the distance in lines and columns from the target place to the place that it needs to be * the cost of moving tile
                            h += (Math.abs(k-i) + Math.abs(l-j))*puzzle[i][j].cost;
			}
		}
		return h;
	}
	
	@Override
	/*
	 * compare function for the PriorityQueue
	 * compare by f value
	 */
	public int compareTo(Puzzle o)
	{
		if (this.f < o.f)
          return -1;
		if (this.f > o.f)
          return 1;
		return 0;
	}
}