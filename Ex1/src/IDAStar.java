import java.util.Hashtable;
import java.util.Stack;

/**
 * @author Uri Hanunov
 * This class represent a IDA* algorithm 
 * Gets puzzle and return the result puzzle(the goal) with all the parameters, including path and cost
 * return null if there is no solution
 */
public class IDAStar
{
	Stack<Puzzle> stack = new Stack<Puzzle>(); //Stack for the open list
	Hashtable<String,Puzzle> frontiersHash = new Hashtable<String,Puzzle>(); //Hash table for the puzzles in open list 
	public int countNewPuzzle = 1; //counter of new puzzles
	
	public Puzzle solve(Puzzle puzzleToSolve, boolean print)
	{
		int t = puzzleToSolve.h(); //the h() of the first puzzle
		String [] moves = {"R","D","L","U"}; //all the operators that we can do by same order every time
		int round = 1; //number of round
		while (t!=Integer.MAX_VALUE)
		{
			puzzleToSolve.flag = "x"; //if it is "x" so its not marked as "out" - every new round we make it "x" again
			int minF = Integer.MAX_VALUE; //save the min f from all puzzles - for next round
			stack.add(puzzleToSolve); //add to stack
			frontiersHash.put(puzzleToSolve.madeKeyForHash(), puzzleToSolve); //add to hash of open list
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
					for(int i = 0; i < 4; i++) //for every operator
					{
						String parent = puzzle.parent(); //the previous step - we can't do it 
						if (puzzle.canMove(moves[i]) && !(parent.equals(moves[i]))) //if it is permitted moving the tile
						{
							Puzzle newPuzzle = new Puzzle(puzzle); // copy constructor
							newPuzzle.move(moves[i]); //moving the tile
							countNewPuzzle++;
							if(newPuzzle.f > t) //if the f of the new puzzle bigger than t
							{
								minF = Math.min(newPuzzle.f, minF); //check the min between f and minF for saving the lesses f
								continue; //next operator
							}
							if(frontiersHash.containsKey(newPuzzle.madeKeyForHash())) //if the new puzzle is in the open list and marked as "out" go to next operator
							{
								Puzzle temp = frontiersHash.get(newPuzzle.madeKeyForHash());
								if(temp.flag.equals("out"))
									continue; //next operator
							}
							if(frontiersHash.containsKey(newPuzzle.madeKeyForHash()) && !(frontiersHash.get(newPuzzle.madeKeyForHash()).flag.equals("out"))) //if the new puzzle is in the open list and not marked as "out"
							{
								if(frontiersHash.get(newPuzzle.madeKeyForHash()).f > newPuzzle.f) //check if the new puzzle has less f value 
								{
									//if yes - remove the old one
									Puzzle toRemove = frontiersHash.get(newPuzzle.madeKeyForHash());
									frontiersHash.remove(newPuzzle.madeKeyForHash());
									stack.remove(toRemove);
								}
								else
								{
									continue; //next operator
								}
							}
							if(newPuzzle.isSolved()) //check if this is the goal puzzle - return it if yes
							{
								if(newPuzzle.path.length() > 1)
									newPuzzle.path = newPuzzle.path.substring(0,newPuzzle.path.length()-1);
								return newPuzzle;
							}
							//adding the new puzzle to stack and to the hash
							stack.add(newPuzzle);
							frontiersHash.put(newPuzzle.madeKeyForHash(), newPuzzle);
						}
					}
				}
			}
			t = minF; //put in t the next min f for the next round
		}
		return null;
	}
}
