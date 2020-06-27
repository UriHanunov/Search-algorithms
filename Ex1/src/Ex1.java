import java.io.*;
import java.text.DecimalFormat;

/**
 * @author Uri Hanunov
 * This class represent a program that run on file and doing a search algorithm on it's Colored NxM-tile puzzle
 * in the end of the algorithm we can get the goal puzzle and all the cost and path of the algorithm
 * The algorithms are : BFS, DFID, A*, IDA* and DFBnB
 * the program choose one of them by the input file and run it and make output text file of the results
 */
public class Ex1
{
	public static void main(String[] args) throws IOException
	{
		File input = new File("input.txt");
		FileWriter myWriter = new FileWriter("output.txt"); //we will write the results to this file
		BufferedWriter bw = new BufferedWriter(myWriter); //run on lines in output text
		BufferedReader br = new BufferedReader(new FileReader(input)); //run on lines in input text
		String algorithm = null; // the algorithm that we going to use it
		boolean runTime = false; // if to print the run time or not
		boolean openList = false; // if to print the open list in every step of the algorithm or not
		int lines = 0, columns = 0; //number of lines and columns of the puzzle
		String [] blacks = null, reds = null; //array of the black and red numbers
		Tile [] [] puzzle = null, goalPuzzle = null; //the matrixes
		int countLines = 1; //number of line in the input file
		long timeStart;
		long timeStop;
		String st = br.readLine(); //the string in the line
		while (st != null)
		{
			if(countLines == 1) //line 1 says which algo we are going to use
				algorithm=st;
			if(st.equals("with time")) //line 2 says if to print the run time or not
				runTime = true;
			if(st.equals("with open")) //line 3 says if to print the open list
				openList = true;
			if(countLines == 4) //line 4 says what is the puzzle size
			{
				String [] temp = st.split("x");
				lines = Integer.parseInt(temp[0]); //number of lines
				columns = Integer.parseInt(temp[1]); //number of columns
				puzzle = new Tile[lines][columns];
				goalPuzzle = new Tile[lines][columns];
			}
			if(countLines == 5) //line 5 says which numbers are Black
			{
				if(st.length()>6)
				{
					st = st.substring(7);
					blacks = st.split(","); //array of the black numbers
				}
			}
			if(countLines == 6) //line 6 says which numbers are Red
			{
				if(st.length()>4)
				{
					st = st.substring(5);
					reds = st.split(","); //array of the red numbers
				}
			}
			if(countLines == 7) //line 7 - (7+lines-1) say what are the numbers in the matrix
			{
				for(int i = 0; i<lines ; i++)
				{
					String [] temp = st.split(",");
					for(int j = 0; j<columns ; j++)
					{
						if(temp[j].equals("_"))
						{
							puzzle[i][j] = new Tile(0,"Black"); //this is the empty tile
						}
						else
						{
							boolean success = false; //check if the color is black or red - if not so put green
							if(blacks!=null)
							{
								for(int x = 0 ; x < blacks.length ; x++)
								{
									if(Integer.parseInt(temp[j]) == Integer.parseInt(blacks[x])) //check if this number is black
									{
										puzzle[i][j] = new Tile(Integer.parseInt(temp[j]),"Black");
										success = true;
										break;
									}
								}
							}
							if(reds!=null)
							{
								for(int x = 0 ; x<reds.length ; x++)
								{
									if(Integer.parseInt(temp[j]) == Integer.parseInt(reds[x])) //check if this number is red
									{
										puzzle[i][j] = new Tile(Integer.parseInt(temp[j]),"Red");
										success = true;
										break;
									}
								}
							}
							if(!success) //if the color is not black or red
								puzzle[i][j] = new Tile(Integer.parseInt(temp[j]),"Green"); //put green
						}
					}
					st = br.readLine(); //next line in the file
				}
			}
			st = br.readLine(); //next line in the file
			countLines++; //next number line
		}
		br.close();
		int number=1; //counter for numbers in goalPuzzle
		for(int i = 0; i<lines ; i++)
		{
			for(int j = 0; j<columns ; j++)
			{
				goalPuzzle[i][j] = new Tile(number, "color no matter"); //making the goal puzzle with no color
				number++;
			}
		}
		goalPuzzle[lines-1][columns-1].num = 0; //the last tile is the empty one
		Puzzle wrokOn = new Puzzle(puzzle , goalPuzzle); //the type puzzle holds the input puzzle and the goal puzzle

		//send to the right algorithm
		if(algorithm.equals("BFS"))
		{
			Bfs algo = new Bfs();
			timeStart = System.nanoTime(); // start time
			Puzzle solvedPuzzle = algo.solve(wrokOn,openList); //gets the goal puzzle with his results
			timeStop = System.nanoTime(); //end time
			if(solvedPuzzle!=null)
			{
				bw.write(solvedPuzzle.path); //the path to the goal
				bw.newLine();
				bw.write("Num: " + algo.countNewPuzzle); //numbers of created puzzles
				bw.newLine();
				bw.write("Cost: " + solvedPuzzle.costAll); //the cost to the result
				bw.newLine();
				if(runTime) //if we get "with time" so print how long time the algo takes
				{
					double time = (double) (timeStop - timeStart) / 1_000_000_000;
					bw.write(new DecimalFormat("##.###").format(time) + " seconds");
				}
			}
			else //if there is no path to the result
			{
				bw.write("no path");
				bw.newLine();
				bw.write("Num: " + algo.countNewPuzzle); //numbers of created puzzles
				bw.newLine();
			}
		}
		else if(algorithm.equals("DFID"))
		{
			Dfid algo = new Dfid();
			timeStart = System.nanoTime();
			Puzzle solvedPuzzle = algo.solve(wrokOn,openList);
			timeStop = System.nanoTime();
			if(solvedPuzzle!=null)
			{
				bw.write(solvedPuzzle.path); //the path to the goal
				bw.newLine();
				bw.write("Num: " + algo.countNewPuzzle); //numbers of created puzzles
				bw.newLine();
				bw.write("Cost: " + solvedPuzzle.costAll); //the cost to the result
				bw.newLine();
				if(runTime) //if we get "with time" so print how long time the algo takes
				{
					double time = (double) (timeStop - timeStart) / 1_000_000_000;
					bw.write(new DecimalFormat("##.###").format(time) + " seconds");
				}
			}
			else //if there is no path to the result
			{
				bw.write("no path");
				bw.newLine();
				bw.write("Num: " + algo.countNewPuzzle); //numbers of created puzzles
				bw.newLine();
			}
		}
		else if(algorithm.equals("A*"))
		{
			Astar algo = new Astar();
			timeStart = System.nanoTime();
			Puzzle solvedPuzzle = algo.solve(wrokOn,openList);
			timeStop = System.nanoTime();
			if(solvedPuzzle!=null)
			{
				bw.write(solvedPuzzle.path); //the path to the goal
				bw.newLine();
				bw.write("Num: " + algo.countNewPuzzle); //numbers of created puzzles
				bw.newLine();
				bw.write("Cost: " + solvedPuzzle.costAll); //the cost to the result
				bw.newLine();
				if(runTime) //if we get "with time" so print how long time the algo takes
				{
					double time = (double) (timeStop - timeStart) / 1_000_000_000;
					bw.write(new DecimalFormat("##.###").format(time) + " seconds");
				}
			}
			else //if there is no path to the result
			{
				bw.write("no path");
				bw.newLine();
				bw.write("Num: " + algo.countNewPuzzle); //numbers of created puzzles
				bw.newLine();
			}
		}
		else if(algorithm.equals("IDA*"))
		{
			IDAStar algo = new IDAStar();
			timeStart = System.nanoTime();
			Puzzle solvedPuzzle = algo.solve(wrokOn,openList);
			timeStop = System.nanoTime();
			if(solvedPuzzle!=null)
			{
				bw.write(solvedPuzzle.path); //the path to the goal
				bw.newLine();
				bw.write("Num: " + algo.countNewPuzzle); //numbers of created puzzles
				bw.newLine();
				bw.write("Cost: " + solvedPuzzle.costAll); //the cost to the result
				bw.newLine();
				if(runTime) //if we get "with time" so print how long time the algo takes
				{
					double time = (double) (timeStop - timeStart) / 1_000_000_000;
					bw.write(new DecimalFormat("##.###").format(time) + " seconds");
				}
			}
			else //if there is no path to the result
			{
				bw.write("no path");
				bw.newLine();
				bw.write("Num: " + algo.countNewPuzzle); //numbers of created puzzles
				bw.newLine();
			}
		}
		else if(algorithm.equals("DFBnB"))
		{
			DFBnB algo = new DFBnB();
			int numbersNotBlacks; //all the numbers in the puzzle except blacks
			if(blacks!=null)
				numbersNotBlacks = (puzzle.length*puzzle[0].length-1) - blacks.length;
			else
				numbersNotBlacks = (puzzle.length*puzzle[0].length-1);
			int threshold = 1;
			for(int i = 1 ; i<=numbersNotBlacks ; i++) //calculate numbersNotBlacks!
			{
				threshold = threshold*i;
			}
			threshold = Math.min(threshold, Integer.MAX_VALUE); //take the min between numbersNotBlacks! to Integer.MAX_VALUE for the threshold in DFBnB
			timeStart = System.nanoTime();
			Puzzle solvedPuzzle = algo.solve(wrokOn,openList,threshold);
			timeStop = System.nanoTime();
			if(solvedPuzzle!=null)
			{
				bw.write(solvedPuzzle.path); //the path to the goal
				bw.newLine();
				bw.write("Num: " + algo.countNewPuzzle); //numbers of created puzzles
				bw.newLine();
				bw.write("Cost: " + solvedPuzzle.costAll); //the cost to the result
				bw.newLine();
				if(runTime) //if we get "with time" so print how long time the algo takes
				{
					double time = (double) (timeStop - timeStart) / 1_000_000_000;
					bw.write(new DecimalFormat("##.###").format(time) + " seconds");
				}
			}
			else //if there is no path to the result
			{
				bw.write("no path");
				bw.newLine();
				bw.write("Num: " + algo.countNewPuzzle); //numbers of created puzzles
				bw.newLine();
			}
		}
		bw.close();
		myWriter.close();
	}
}