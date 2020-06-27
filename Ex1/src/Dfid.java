import java.util.Hashtable;

/**
 * @author Uri Hanunov
 * This class represent a DFID algorithm 
 * Gets puzzle and return the result puzzle(the goal) with all the parameters, including path and cost
 * return null if there is no solution
 */
public class Dfid
{
	Hashtable<String,Puzzle> frontiersHash; //Hash table for the puzzles in open list
	String [] result; //save the puzzle, cutoff or not and fail or not
	public int countNewPuzzle = 1; //counter of new puzzles
	int round = 1; //number of round
	
	public Puzzle solve(Puzzle puzzleToSolve, boolean print)
	{
		for(int l = 1; l<Integer.MAX_VALUE ; l++) //the depth of the algo every time
		{
			frontiersHash = new Hashtable<String,Puzzle>(); 
			result = limitDfs(puzzleToSolve, l, frontiersHash,print); //send to limitDfs function
			if(result[1].equals("no cutoff"))
			{
				if(result[2].equals("no fail")) //if there is no cutoff and no fail so this is the result
				{
					Puzzle theSolve = frontiersHash.get(result[0]); //the result
					if(theSolve.path.length()>1)
						theSolve.path = theSolve.path.substring(0,theSolve.path.length()-1);
					return theSolve;
				}
				return null; //if its fail
			}
		}
		return null;
	}
	
	public String [] limitDfs(Puzzle puzzleToSolve, int limit, Hashtable<String,Puzzle> hash,boolean print)
	{
		String [] info = new String[3]; //the result
		if(print) //if we get "with open" - print the open list
		{
			System.out.println("round number: " + round);
			System.out.println(hash);
			round++;
		}
		if(puzzleToSolve.isSolved()) //if this is the goal puzzle so no cutoff and no fail
		{
			hash.put(puzzleToSolve.madeKeyForHash(), puzzleToSolve);
			info[0] = puzzleToSolve.madeKeyForHash();
			info[1] = "no cutoff";
			info[2] = "no fail";
			return info;
		}
		else if(limit == 0) //if the limit depth is over so there is cutoff and no fail
		{
			info[0] = puzzleToSolve.madeKeyForHash();
			info[1] = "cutoff";
			info[2] = "no fail";
			return info;
		}
		else
		{
			hash.put(puzzleToSolve.madeKeyForHash(), puzzleToSolve); //add the puzzle to hash
			boolean isCutoff = false;
			String [] moves = {"R","D","L","U"}; //all the operators that we can do by same order every time
			for(int i = 0; i < 4; i++) //for every operator
			{
				String parent = puzzleToSolve.parent(); //the previous step - we can't do it 
				if (puzzleToSolve.canMove(moves[i]) && !(parent.equals(moves[i]))) //if it is permitted moving the tile
				{
					Puzzle newPuzzle = new Puzzle(puzzleToSolve); // copy constructor
					newPuzzle.move(moves[i]); //moving the tile
					countNewPuzzle++;
					if(hash.containsKey(newPuzzle.madeKeyForHash())) //if this puzzle is in the open list - continue to next operator
					{
						continue;
					}
					else
					{
						info = limitDfs(newPuzzle, limit-1,hash,print); //Recursive on the new puzzle
						if(info[1].equals("cutoff"))
							isCutoff = true;
						else if(info[2].equals("no fail")) //return the puzzle if no fail
						{
							return info;
						}
					}
				}
			}
			hash.remove(puzzleToSolve.madeKeyForHash()); //remove from hash
			if(isCutoff == true) //if there is cutoff so return the puzzle with cutoff and no fail
			{
				info[0] = puzzleToSolve.madeKeyForHash();
				info[1] = "cutoff";
				info[2] = "no fail";
				return info;
			}
			else //else - return the puzzle with no cutoff and fail
			{
				info[0] = puzzleToSolve.madeKeyForHash();
				info[1] = "no cutoff";
				info[2] = "fail";
				return info;
			}
		}
	}
}
