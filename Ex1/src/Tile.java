/**
 * @author Uri Hanunov
 * This class represent a tile in puzzle's matrix
 */
public class Tile
{
	public int num; //the number of tile
	public String color; //the color of the tile
	public int cost; //the cost for moving it every time
	
	//constructor
	public Tile(int num , String color)
	{
		this.num = num;
		this.color = color;
		if(color.equals("Red")) //the cost for red color
		{
			cost = 30;
		}
		else if(color.equals("Black")) //the cost for black color - we can't moving it
		{
			cost = 0;
		}
		else //the cost for green color
		{
			cost = 1;
		}
	}
	
	//copy constructor
	public Tile(Tile tile) 
	{
		this.num = tile.num;
		this.color = tile.color;
		this.cost = tile.cost;
	}
}
