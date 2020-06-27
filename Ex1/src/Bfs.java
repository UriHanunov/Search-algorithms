import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Uri Hanunov
 * This class represent a BFS algorithm 
 * Gets puzzle and return the result puzzle(the goal) with all the parameters, including path and cost
 * return null if there is no solution
 */
public class Bfs
{
	private final Queue<Puzzle> frontiers = new LinkedList<>(); //Queue for the open list
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
			closedlistHash.put(puzzle.madeKeyForHash(), puzzle); //add the puzzle to close Hash
			for(int i = 0; i < 4; i++) //for every operator
			{
				String parent = puzzle.parent(); //the previous step - we can't do it 
				if (puzzle.canMove(moves[i]) && !(parent.equals(moves[i]))) //if it is permitted moving the tile
				{
					Puzzle newPuzzle = new Puzzle(puzzle); // copy constructor
					newPuzzle.move(moves[i]); //moving the tile
					countNewPuzzle++;
					//if the new puzzle is in the close list or in the open list - continue to the next operator
					if(closedlistHash.containsKey(newPuzzle.madeKeyForHash()) || frontiersHash.containsKey(newPuzzle.madeKeyForHash()))
					{
						continue;
					}
					else
					{
						if(newPuzzle.isSolved()) //check if this is the goal puzzle - return it if yes
						{
							if(newPuzzle.path.length() > 1)
								newPuzzle.path = newPuzzle.path.substring(0,newPuzzle.path.length()-1); //removing the last "-"
							return newPuzzle;
						}
						frontiers.add(newPuzzle); //add to open list
						frontiersHash.put(newPuzzle.madeKeyForHash(), newPuzzle); //add to hash of open list
					}
				}
			}
		}
		return null;
	}
}

