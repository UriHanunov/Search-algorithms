import java.util.Hashtable;
import java.util.PriorityQueue;
import java.util.Stack;

/**
 * @author Uri hanunov
 * This class represent a DFBnB algorithm 
 * Gets puzzle and return the result puzzle(the goal) with all the parameters, including path and cost
 * return null if there is no solution
 */
public class DFBnB
{
	Stack<Puzzle> stack = new Stack<Puzzle>(); //Stack for the open list
	Hashtable<String,Puzzle> frontiersHash = new Hashtable<String,Puzzle>(); //Hash table for the puzzles in open list 
	public int countNewPuzzle = 1; //counter of new puzzles
	
	public Puzzle solve(Puzzle puzzleToSolve, boolean print,int threshold)
	{
		stack.add(puzzleToSolve); //add to stack
		frontiersHash.put(puzzleToSolve.madeKeyForHash(), puzzleToSolve); //add to hash of open list
		Puzzle result = null; //save the correct puzzle
		int t = threshold; //the threshold
		String [] moves = {"R","D","L","U"}; //for every operator
		int round = 1; //number of round
		while (!stack.isEmpty())
		{
			if(print) //if we get "with open" - print the open list
			{
				System.out.println("round number: " + round);
				System.out.println(stack);
				round++;
			}
			Puzzle puzzle = stack.pop(); //take the top puzzle in stack
			if(puzzle.flag.equals("out")) //check if it is marked with "out"
			{
				frontiersHash.remove(puzzle.madeKeyForHash()); //if yes - remove
			}
			else
			{
				puzzle.flag = "out"; //marked as "out"
				stack.add(puzzle);
				PriorityQueue<Puzzle> N = new PriorityQueue<>(); //Queue for the 3 new puzzles priority by f value
				for(int i = 0; i < 4; i++)
				{
					String parent = puzzle.parent(); //the previous step - we can't do it 
					if (puzzle.canMove(moves[i]) && !(parent.equals(moves[i]))) //if it is permitted moving the tile
					{
						Puzzle newPuzzle = new Puzzle(puzzle); // copy constructor
						newPuzzle.move(moves[i]); //moving the tile
						countNewPuzzle++;
						N.add(newPuzzle); //add to the Queue N 
					}
				}
				for(int i = 0; i < N.size(); i++)
				{
					Puzzle newPuzzle = N.peek(); //take the first puzzle in the Queue
					if(newPuzzle.f >= t) //if f value is bigger than t - clear N, all the puzzles after are also with bigger f
					{
						N.clear();
					}
					else if(frontiersHash.containsKey(newPuzzle.madeKeyForHash())) //if the new puzzle is in the open list and marked as "out" - remove from N
					{
						Puzzle a = frontiersHash.get(newPuzzle.madeKeyForHash());
						if(a.flag.equals("out"))
							N.remove();
					}
					else if(frontiersHash.containsKey(newPuzzle.madeKeyForHash()) && !(frontiersHash.get(newPuzzle.madeKeyForHash()).flag.equals("out"))) //if the new puzzle is in the open list and not marked as "out"
					{
						if(frontiersHash.get(newPuzzle.madeKeyForHash()).f <= newPuzzle.f) //check if the new puzzle has bigger f value 
						{
							N.remove(); //if yes - remove from N
						}
						else
						{
							//if no - remove the old one
							Puzzle toRemove = frontiersHash.get(newPuzzle.madeKeyForHash());
							frontiersHash.remove(toRemove.madeKeyForHash());
							stack.remove(toRemove);
						}
					}
					else if(newPuzzle.isSolved()) //check if this is the goal puzzle - save it if yes
					{
						t = newPuzzle.f;
						if(newPuzzle.path.length() > 1)
							newPuzzle.path = newPuzzle.path.substring(0,newPuzzle.path.length()-1);
						result = newPuzzle;
						N.clear(); //clear N
					}
				}
				Stack<Puzzle> temp = new Stack<Puzzle>();
				//insert N in a reverse order to the stack and to the hash
				while(!N.isEmpty())
				{
					temp.add(N.poll());
				}
				while(!temp.isEmpty())
				{
					Puzzle newPuzzle = temp.pop();
					stack.add(newPuzzle);
					frontiersHash.put(newPuzzle.madeKeyForHash(), newPuzzle);
				}
			}
		}				
		return result;
	}
}
