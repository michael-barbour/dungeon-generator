package dungeon_generator;

import java.awt.*;
import javax.swing.*;
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
	Random x = new Random();
	long seed = x.nextLong();
	Random rand = new Random(seed);				// Create random number generator (default random seed)
	
	JFrame frame;
	DungeonPanel myDungeonPanel;
	
	int map_size_width = 510;					// width and height of map in pixels (default 510x510)
	int map_size_height = 510;
	int tile_width = 15;						// tile sizes
	int tile_height = 15;
	int max_tile_x = map_size_width/tile_width;	// total number of tiles
	int max_tile_y = map_size_height/tile_height;
	
	int min_room_size = 2;						// room size in tiles (default 2 - 6)
	int max_room_size = 6;
	int max_failures = 50;						//Number of failed attempts to create a new room before completing dungeon.
			
	Tile tile_map[][] = new Tile[max_tile_x][max_tile_y];				// Creates a tile_map of size max_tile_x by max_tile_y
	ArrayList<int[]> rooms = new ArrayList<int[]>();					// Creates an ArrayList for all successfully placed rooms
	int[] room_order;													// Creates the order that the hallway generator will visit the rooms.

	
	
	/**
	 *  main()
	 * 
	 * 	Creates an instance of myDungeon from a static context.
	 * 
	 */	
	public static void main(String[] args){
		DungeonGenerator myDungeon = new DungeonGenerator();
		myDungeon.init();
		myDungeon.start();
	}
	
	public DungeonGenerator(){
	}
	
	/**
	 * init
	 * 
	 * Pre: None
	 * Post:  Creates frame and panel and initializes map to be stone.
	 */
	public void init(){	
		frame = new JFrame();
		frame.setVisible(true);
		frame.setSize(526, 548);
		
		myDungeonPanel = new DungeonPanel(tile_map, max_tile_x, max_tile_y, tile_width, tile_height);
		
		myDungeonPanel.setBounds(0, 0, 510, 510);
		myDungeonPanel.setVisible(true);
		frame.add(myDungeonPanel);

		
		for (int i = 0; i < max_tile_y; i++){
			for(int j = 0; j < max_tile_x; j++){
				tile_map[j][i] = new Tile();
			}
		}
		System.out.println("Map Seed: " + seed);
    }

	
	/**
	 * 	start()
	 * 
	 * 	Pre: Program must be initialized
	 * 	Post: Map generated with player spawn and dungeon exit
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
     * 	placeSpawn()
     * 
     * 	Pre:  Map has rooms in an order.  Calls on room_order and tile_map
     * 	Post:  Player spawn is placed in the first room
     * 
     * 	Function places the player spawn in the first room on room_order
     * 
     */
    private void placeSpawn() {
		tile_map[rooms.get(room_order[0])[0]][rooms.get(room_order[0])[1]].setMaterial(2);
	}
    
    /**
     * 	placeExit()
     * 
     * 	Pre: Map has rooms in an order.  Calls on room_order and tile_map
     * 	Post:  Dungeon Exit is placed at the end of the dungeon.
     * 
     * 	Function places dungeon exit in the final room of the dungeon.
     * 
     */
	private void placeExit() {
		tile_map[rooms.get(room_order[room_order.length-1])[0]][rooms.get(room_order[room_order.length-1])[1]].setMaterial(3);
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
		int[] starting_point;									// Current room location
		
		while (failed_attempts < max_failures){
			starting_point = new int[2];
			starting_point[0] = rand.nextInt(max_tile_x);					// Save a random location to build a room
			starting_point[1] = rand.nextInt(max_tile_y);
			
			if (buildRoom(starting_point)){									// Attempts to construct a room at starting point. returns False if room fails to be constructed
				failed_attempts = 0;
				rooms.add(starting_point);
			}
			else{
				failed_attempts++;
			}
		}		
	}
	
	/**
	 * buildRoom(int[] starting_point)
	 * 
	 * @param starting_point - point around which the function attempts to build a room
	 * @return	true - Room was constructed;  false - Room could not be constructed
	 * 
	 * Function attempts to build a room at given starting point, Fails if the room is too close to another room or if room is out of bounds.
	 */
	public boolean buildRoom(int[] starting_point) {
		int x = rand.nextInt(max_room_size - min_room_size) + min_room_size;			// Determines width of new room
		int y = rand.nextInt(max_room_size - min_room_size) + min_room_size;			// Determines height of new room
		int left = rand.nextInt(x);														// Determines tiles left of room starting point
		int up = rand.nextInt(y);														// Determines tiles above room starting point
				
		for (int i = up+1; i > up - (y+1); i--){										// Check that all planned room spaces are valid
			for(int j = left+1; j > left - (x+1); j--){ 
				if ((starting_point[0] + j) >= max_tile_x ||
						(starting_point[0] + j) < 0 ||
						(starting_point[1] + i) >= max_tile_y || 
						(starting_point[1] + i) < 0 ||
						tile_map[starting_point[0] + j][starting_point[1] + i].getMaterial() != 0){
					return false;
				}
			}
		}
				
		for (int i = up; i > up - y; i--){		// Changes materials to floor for all spaces within room domain
			for(int j = left; j > left - x; j--){ 
				tile_map[starting_point[0] + j][starting_point[1] + i].setMaterial(1);					
			}
		}
		return true;
	}
	
	/**
	 * generateHalls()
	 * 
	 * Pre: Rooms must be constructed
	 * Post: Halls connecting all rooms are constructed
	 * 
	 * generateHalls uses a greedy algorithm to connect all of the rooms in the dungeon to each other.  While the algorithm does not provide the best path,
	 * 		it allows overlapping of hallways and a more labyrinth style of hallways without being too overwhelming.
	 * 
	 * 		Function calls drawHalls()
	 */
	public void generateHalls() {
		int[] permutation = new int[rooms.size()];
		
		for(int i = 0; i < rooms.size(); i++){
			permutation[i] = i;													// Add all rooms to path
		}
		
		for (int i = 0; i < rooms.size() - 1; i++){								// Finds nearest room and draws a path to it. 
			int best = distance(rooms.get(permutation[i]), rooms.get(permutation[i+1]));
			for(int j = i + 2; j < rooms.size(); j++){
				int current = distance(rooms.get(permutation[i]), rooms.get(permutation[j]));
				if(current < best){
					best = current;
					int temp = permutation[i+1];
					permutation[i+1] = permutation[j];
					permutation[j] = temp;
				}
			}
		}
		room_order = permutation;
		drawHalls();													// Draw a hall between each room in order
		
	}

	/**
	 * drawHalls()
	 * 
	 * Pre:  room_order must be decided
	 * Post: Halls are constructed on tile_map
	 * 
	 * Draws halls between each room in room_order and the one following it.
	 */
	public void drawHalls(){
		for(int i = 0; i < room_order.length - 1; i++){
			int[][] start_end_points = {rooms.get(room_order[i]), rooms.get(room_order[i+1])};
			drawHall(start_end_points);
		}
	}

	/**
	 * 	drawHall(int[][] start_end_points)
	 * 	@param start_end_points - Contains 4 values [0][0] = x coordinate of room 1
	 * 												[0][1] = y coordinate of room 1
	 * 												[1][0] = x coordinate of room 2
	 * 												[1][1] = y coordinate of room 2
	 * 
	 * 	Draws a line right and then either up or down to connect rooms via hallways.
	 */
	public void drawHall(int[][] start_end_points) {
		int x1;
		int x2;
		int y1;
		int y2;
				
		if (start_end_points[0][0] < start_end_points[1][0]){
			x1 = start_end_points[0][0];
			x2 = start_end_points[1][0];
			y1 = start_end_points[0][1];
			y2 = start_end_points[1][1];
		}
		else{
			x1 = start_end_points[1][0];
			x2 = start_end_points[0][0];
			y1 = start_end_points[1][1];
			y2 = start_end_points[0][1];
		}
		
		
		
		while(x1 <= x2){
			tile_map[x1][y1].setMaterial(1);
			x1++;
		}
		if (y1 < y2){
			while (y1 <= y2){
				tile_map[x2][y1].setMaterial(1);
				y1++;
			}
		}
		else{
			while (y1 >= y2){
				tile_map[x2][y1].setMaterial(1);
				y1--;
			}
		}
	}

	/**
	 * 	distance(int[] room1, int[] room2)
	 * 
	 * 	@param room1 {x,y} coordinates of starting room
	 * 	@param room2 {x,y} coordinates of ending room
	 * 	@return integer value equal to the distance
	 * 
	 * 	Calculates the number of tiles required to create a hallway from room1 to room2
	 */
	public int distance(int[] room1, int[] room2) {
		return Math.abs(room1[0] - room2[0]) + Math.abs(room1[1] - room2[1]);
	}

	/**
	 *  totalDistance(int[] permutation)
	 *  
	 *  @param permutation - array of a potential room order
	 *  @return	integer value equal to distance between all rooms (excluding connecting first room to last.
	 * 
	 *  Calculates the total distance required to go between all rooms
	 */
	public int totalDistance(int[] permutation){
		int total_distance = 0;
		for(int i = 0; i < permutation.length - 1; i++){
			total_distance += distance(rooms.get(permutation[i]), rooms.get(permutation[i+1]));
		}
		
		return total_distance;
	}

	/**
	 * Dungeon Panel
	 * 
	 * @author Michael Barbour
	 *
	 *	Nested class allows custom paint function.
	 */
	class DungeonPanel extends JPanel{
		Tile[][] tile_map;
		int max_tile_x;
		int max_tile_y;
		int tile_width;
		int tile_height;
		
		/**
		 * Dungeon Panel
		 * 
		 * @param tile_map - 2d tile array containing all of the materials used
		 * @param max_tile_x - number of tiles horizontally
		 * @param max_tile_y - number of tiles vertically
		 * @param tile_width - width of tiles
		 * @param tile_height - height of tiles
		 * 
		 * Constructs a panel to draw a map using these variables
		 */
		public DungeonPanel(Tile[][] tile_map, int max_tile_x, int max_tile_y, int tile_width, int tile_height){
			this.tile_map = tile_map;
			this.max_tile_x = max_tile_x;
			this.max_tile_y = max_tile_y;
			this.tile_width = tile_width;
			this.tile_height = tile_height;
		}
		
		/**
		 * paint(Graphics g)
		 * 
		 * Pre: None
		 * Post:  Creates a panel with a map painted on it
		 * 
		 * Paints the current conditions to the panel.
		 * 			Paints all tiles in the current map with the set colors
		 * 				Floors = brown
		 * 				Spawn = green
		 * 				Exit = black
		 * 				Walls = checkered gray and dark gray
		 */
		@Override
		public void paint(Graphics g){
			for(int i = 0; i < max_tile_y; i++){
				for(int j = 0; j < max_tile_x; j++){

					if (tile_map[j][i].getMaterial() == 1)
						g.setColor(new Color(165, 93, 53));						// Floors = brown
					else if (tile_map[j][i].getMaterial() == 2)
						g.setColor(Color.green);								// Spawn = green
					else if (tile_map[j][i].getMaterial() == 3){
						g.setColor(Color.black);								// Exit = black
					}
					else														// walls = dark gray and gray checkered
						if ((i + j) % 2 == 0)
							g.setColor(Color.gray);
						else
							g.setColor(Color.darkGray);
					g.fillRect(j*tile_width, i*tile_height, tile_width, tile_height);
					//g.drawString("" + tile_map[j][i].getMaterial(), j*tile_width, i*tile_height + tile_height);				// Draws material number on tiles
					//g.drawString(j + ", " + i, j*tile_width, i*tile_height + tile_height);									// Draws coordinate pair on tiles
				}
			}
		}
	}
}
