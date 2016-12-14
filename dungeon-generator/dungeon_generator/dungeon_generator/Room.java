package dungeon_generator;


/**
 * Room
 * 
 * @author Michael Barbour
 * 
 * Used to create a room object at a specific coordinate location.  Room sizes are handled through DungeonGenerator 
 * TODO: Room sizes should be handled locally.
 *
 */
public class Room {
	int x;
	int y;
	
	/**
	 * Room
	 * 
	 * creates a room object at location x, y
	 * @param x position horizontally where room should begin
	 * @param y position vertically where room should begin
	 */
	public Room(int x, int y){
		this.x = x;
		this.y = y;
	}
	/**
	 * getX
	 * @return horizontal location
	 */
	public int getX(){
		return x;
	}
	/**
	 * getY
	 * @return vertical location
	 */
	public int getY(){
		return y;
	}
}
