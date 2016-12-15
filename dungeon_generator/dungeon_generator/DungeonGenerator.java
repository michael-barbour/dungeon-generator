package dungeon_generator;

import dungeon_generator.Tile.Material;
import java.util.ArrayList;
import java.util.Random;

/**
 * 
 * @author Michael Barbour
 * 
 * Dungeon Generator
 * 
 * DungeonGenerator creates a dungeon consisting of rooms and hallways
 * 			The application is by default 510x510 pixels and generates a 16x16 tile map.  
 * 			Each tile has a color that represents what characteristics it would have
 * 						Floor tiles are brown
 * 						Wall tiles are gray or dark gray
 * 						The player spawn is green
 * 						The dungeon exit is black
 * 
 * Each dungeon is randomly generated and a seed can be manually put into the init function.
 *
 */

public class DungeonGenerator {
	long seed;
	Random rand = new Random(); 				// Create random number generator (default random seed)
	
	static final int default_max_tile_x = 15;
	static final int default_max_tile_y = 15;
	
	int max_tile_x;						// Default 15x15 board (can be overridden by constructor);
	int max_tile_y;
	
	int min_room_size = 2;						// Room size in tiles (default 2 - 6)
	int max_room_size = 6;
	int max_failures = 50;						// Number of failed attempts to create a new room before completing dungeon.
			
	Tile tile_map[][];							// Creates a tile_map of size max_tile_x by max_tile_y
	
	ArrayList<Room> rooms = new ArrayList<Room>();					// Creates an ArrayList for all successfully placed rooms

	/**
	 * DungeonGenerator 
	 * 
	 * @param tile_width - Number of tiles in the X direction
	 * @param tile_height - Number of desired tiles in the Y direction
	 * 
	 * Initializes variables that determine how many tiles the map will contain (max_tile_x by max_tile_y)
	 * 		then initializes tile_map to be stone.
	 */
	public DungeonGenerator(int max_tile_x, int max_tile_y){
		this.max_tile_x = max_tile_x;
		this.max_tile_y = max_tile_y;
		
		tile_map = new Tile[max_tile_x][max_tile_y];
		
		for (int i = 0; i < max_tile_y; i++){
			for(int j = 0; j < max_tile_x; j++){
				tile_map[j][i] = new Tile();
			}
		}
	}
	
	/**
	 * DungeonGenerator
	 * 
	 * Uses default max height and width (15x15 tiles)
	 */
	public DungeonGenerator(){
		this(default_max_tile_x, default_max_tile_y);
	}

	/**
	 * 	start
	 * 
	 *  Creates map by changing wall tiles to floor tiles.  Places player spawn and dungeon exit.
	 * 
	 */	
    public void start() {
    	generateRooms();
		generateHalls();
		placeSpawn();
		placeExit();
    }
    
    /**
     * 	placeSpawn
     * 
     * 	Function places the player spawn in the first room on room_order
     * 
     */
    private void placeSpawn() {
		tile_map[ rooms.get(0).getX() ][ rooms.get(0).getY() ].setMaterial(Material.PLAYER_SPAWN);
	}
    
    /**
     * 	placeExit
     * 
     * 	Function places dungeon exit in the final room of the dungeon.
     * 
     */
	private void placeExit() {
		tile_map[ rooms.get(rooms.size() - 1).getX() ][ rooms.get(rooms.size() - 1).getY() ].setMaterial(Material.EXIT);
	}
	
	/**
	 * generateRooms()
	 * 
	 * Pre: Max_failures must be set (int > 0). 
	 * Post:  All rooms are built
	 * 
	 * Attempts to randomly construct rooms until failed attempts to create = max attempts allowed.  Calls buildRoom to actually construct the room.
	 */
	public void generateRooms(){
		int failed_attempts = 0;											// Tracks number of attempts to create a new room before aborting
		Room currentRoom;													// Current room location
		
		while (failed_attempts < max_failures){
			currentRoom = new Room(rand.nextInt(max_tile_x), rand.nextInt(max_tile_y));
																			// Pick starting location for room.
			
			if (buildRoom(currentRoom)){									// Attempts to construct a room at starting point. returns False if room fails to be constructed
				failed_attempts = 0;
				rooms.add(currentRoom);
			}
			else{
				failed_attempts++;
			}
		}		
	}
	
	/**
	 * buildRoom
	 * 
	 * @param starting_point - point around which the function attempts to build a room
	 * @return	true - Room was constructed;  false - Room could not be constructed
	 * 
	 * Function attempts to build a room at given starting point, Fails if the room is too close to another room or if room is out of bounds.
	 */
	public boolean buildRoom(Room currentRoom) {
		int x = rand.nextInt(max_room_size - min_room_size) + min_room_size;			// Determines width of new room
		int y = rand.nextInt(max_room_size - min_room_size) + min_room_size;			// Determines height of new room
		int left = rand.nextInt(x);														// Determines tiles left of room starting point
		int up = rand.nextInt(y);														// Determines tiles above room starting point
				
		for (int i = up+1; i > up - (y+1); i--){										// Check that all planned room spaces are valid
			for(int j = left+1; j > left - (x+1); j--){ 
				if ((currentRoom.getX() + j) >= max_tile_x ||
						(currentRoom.getX() + j) < 0 ||
						(currentRoom.getY() + i) >= max_tile_y || 
						(currentRoom.getY() + i) < 0 ||
						tile_map[currentRoom.getX() + j][currentRoom.getY() + i].getMaterial() != Material.STONE){
					return false;
				}
			}
		}
				
		for (int i = up; i > up - y; i--){		// Changes materials to floor for all spaces within room domain
			for(int j = left; j > left - x; j--){ 
				tile_map[currentRoom.getX() + j][currentRoom.getY() + i].setMaterial(Material.FLOOR);					
			}
		}
		return true;
	}
	
	/**
	 * generateHalls()
	 * 
	 * generateHalls uses a greedy algorithm to connect all of the rooms in the dungeon to each other.  
	 * 		While the algorithm does not provide the shortest path,
	 * 		it does allow the overlapping of hallways for a more labyrinth-style of dungeon.
	 * 
	 * 		Function calls createHalls()
	 */
	public void generateHalls() {
		for (int i = 0; i < rooms.size() - 1; i++){								// Finds nearest room and draws a path to it. 
			int best = distance(rooms.get(i), rooms.get(i+1));
			for(int j = i + 2; j < rooms.size(); j++){
				int current = distance(rooms.get(i), rooms.get(j));
				if(current < best){
					best = current;
					Room temp = rooms.get(i+1);
					rooms.set(i+1, rooms.get(j));
					rooms.set(j, temp);
				}
			}
		}
		createHalls();													// create a hall between each room in the list order
	}

	/**
	 * createHalls()
	 * 
	 * creates halls between each room and the one following it in the list.
	 */
	public void createHalls(){
		for(int i = 0; i < rooms.size() - 1; i++){
			drawHall(rooms.get(i), rooms.get(i+1));
		}
	}

	/**
	 * 	drawHall
	 * 	@param room1 - Starting room
	 * 	@param room2 - Ending room
	 * 
	 * 	Draws a line left and then either up or down to connect rooms via hallways.
	 */
	public void drawHall(Room room1, Room room2) {
		int x1, x2, y1, y2;
				
		if (room1.getX() < room2.getX()){			// set x1 and y1 to correspond to the right most point
			x1 = room1.getX();
			x2 = room2.getX();
			y1 = room1.getY();
			y2 = room2.getY();
		}
		else{
			x1 = room2.getX();
			x2 = room1.getX();
			y1 = room2.getY();
			y2 = room1.getY();
		}
		
		
		
		while(x1 <= x2){									//Draw left from x1 to x2
			tile_map[x1][y1].setMaterial(Material.FLOOR);
			x1++;
		}
		if (y1 < y2){										//Draw down from y1 to y2 if y1 is above y2	(y1 value is lower)
			while (y1 <= y2){
				tile_map[x2][y1].setMaterial(Material.FLOOR);
				y1++;
			}
		}
		else{
			while (y1 >= y2){								//Draw up from y1 to y2 if y1 is below y2 (y1 value is higher)
				tile_map[x2][y1].setMaterial(Material.FLOOR);
				y1--;
			}
		}
	}

	/**
	 * 	distance
	 * 
	 * 	@param room1 - Starting Room
	 * 	@param room2 - Ending Room
	 * 	@return integer value equal to the distance
	 * 
	 * 	Calculates the number of tiles required to create a hallway from room1 to room2
	 */
	public int distance(Room room1, Room room2) {
		return Math.abs(room1.getX() - room2.getX()) + Math.abs(room1.getY() - room2.getY());
	}
	
	/**
	 * getSeed
	 * @return seed - long value corresponding to a specific dungeon configuration
	 */
	public long getSeed(){
		return seed;
	}
	
	/**
	 * setSeed
	 * 
	 * @param seed - long value, typically a random number, that corresponds to a specific map layout.
	 * 
	 * Allows the setting of a specific Random Number Generator seed.  Default seed is Random.
	 */
	public void setSeed(long seed){
		rand.setSeed(seed);
	}
	
	/**
	 * setMinRoomSize
	 * @param min_room_size - int value < max_room_size
	 * 
	 * sets the minimum room size allowed to be randomly generated
	 */
	public void setMinRoomSize(int min_room_size){
		this.min_room_size = min_room_size;
	}
	
	/**
	 * setMaxRoomSize
	 * 
	 * @param max_room_size - int value > min_room_size
	 * 
	 * sets the maximum room size allowed to be randomly generated
	 */
	public void setMaxRoomSize(int max_room_size){
		this.max_room_size = max_room_size;
	}
	
	/**
	 * setMaxFailures
	 * 
	 * @param max_failures - int > 0
	 * 
	 * set the number of attempts a room may be fail to be placed before proceeding to drawing hallways
	 */
	public void setMaxFailures(int max_failures){
		this.max_failures = max_failures;
	}
	
	/**
	 * getTileMap()
	 * 
	 * @return a 2d array consisting of tiles that contains the material data
	 * 
	 * This tile_map should be handed to DungeonPanel or further manipulated by another class to allow the addition of treasure or monsters
	 */
	public Tile[][] getTileMap(){
		return tile_map;
	}
}
