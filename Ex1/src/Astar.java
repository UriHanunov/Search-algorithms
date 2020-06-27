import java.util.Hashtable;
import java.util.PriorityQueue;

/**
 * @author Uri Hanunov
 * This class represent a A* algorithm 
 * Gets puzzle and return the result puzzle(the goal) with all the parameters, including path and cost
 * return null if there is no solution
 */
public class Astar
{
	PriorityQueue<Puzzle> frontiers = new PriorityQueue<>(); //Queue for the open list priority by f value
	Hashtable<String,Puzzle> frontiersHash = new Hashtable<String,Puzzle>(); //Hash table for the puzzles in open list
	Hashtable<String,Puzzle> closedlistHash = new Hashtable<String,Puzzle>(); //Hash table for the puzzles in close list
	public int countNewPuzzle = 1; //counter of new puzzles
	
	public Puzzle solve(Puzzle puzzleToSolve, boolean print)
	{
		frontiers.add(puzzleToSolve);
		String [] moves = {"R","D","L","U"}; //all the operators that we can do by same order every time
		int round = 1; //number of round
		while (!frontiers.isEmpty())
		{
			if(print) //if we get "with open" - print the open list
			{
				System.out.println("round number: " + round);
				System.out.println(frontiers);
				round++;
			}
			Puzzle puzzle = frontiers.poll(); //take the first puzzle in the Queue
			frontiersHash.remove(puzzle.madeKeyForHash()); //remove from Hash
			if(puzzle.isSolved())  //check if this is the goal puzzle - return it if yes
			{
				if(puzzle.path.length() > 1)
					puzzle.path = puzzle.path.substring(0,puzzle.path.length()-1); //removing the last "-"
				return puzzle;
			}
			closedlistHash.put(puzzle.madeKeyForHash(), puzzle); //add to hash of closed list
			for(int i = 0; i < 4; i++) //for every operator
			{
				String parent = puzzle.parent(); //the previous step - we can't do it 
				if (puzzle.canMove(moves[i]) && !(parent.equals(moves[i]))) //if it is permitted moving the tile
				{
					Puzzle newPuzzle = new Puzzle(puzzle); // copy constructor
					newPuzzle.move(moves[i]); //moving the tile
					countNewPuzzle++;
					//if the new puzzle is no in closed list or in the open list - add it to the Queue and to the hash of open list
					if(!(closedlistHash.containsKey(newPuzzle.madeKeyForHash()) || frontiersHash.containsKey(newPuzzle.madeKeyForHash())))
					{
						frontiers.add(newPuzzle);
						frontiersHash.put(newPuzzle.madeKeyForHash(), newPuzzle);
					}
					else if(frontiersHash.containsKey(newPuzzle.madeKeyForHash())) //if the new puzzle is in the open list
					{
						if(newPuzzle.costAll < frontiersHash.get(newPuzzle.madeKeyForHash()).costAll) //check if the cost of this puzzle is less than the older puzzle that we found
						{
							//if yes so remove the old one and add the new one
							frontiers.remove(frontiersHash.get(newPuzzle.madeKeyForHash()));
							frontiersHash.remove(newPuzzle.madeKeyForHash());
							frontiers.add(newPuzzle);
							frontiersHash.put(newPuzzle.madeKeyForHash(), newPuzzle);
						}
					}
				}
			}
		}
		return null;
	}
}
