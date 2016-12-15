package dungeon_generator;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

public class main {

	/**
	 *  main()
	 * 
	 * 	Creates an instance of myDungeon from a static context.
	 */	
	public static void main(String[] args){
		int tile_width = 32, tile_height = 32;						// Set desired height/width of tiles	
		int tiles_x = 20, tiles_y = 20;
		int resolution_x = tiles_x * tile_width, resolution_y = tiles_y * tile_height;		// Set resolution for eventual DungeonPanel

		Random x = new Random();
		long seed = x.nextLong();									// Create Random world seed	
		
        JFrame dungeon_frame = createDungeonFrame(resolution_x, resolution_y);
        JPanel loading_panel = createLoadingPanel(dungeon_frame);
        
        loading_panel.setBounds(0, 0, resolution_x, resolution_y);
        loading_panel.setVisible(true);
		dungeon_frame.setVisible(true);
                
        DungeonGenerator myDungeon = generateDungeon(tiles_x, tiles_y, seed);
       	DungeonPanel myDungeonPanel = new DungeonPanel(myDungeon.getTileMap(), tile_width, tile_height);
       																// Create a panel from tile_map, draw tiles of inputed size
		
		myDungeonPanel.setBounds(0, 0, resolution_x, resolution_y);
		myDungeonPanel.setVisible(true);
		loading_panel.setVisible(false);
		dungeon_frame.add(myDungeonPanel);
	}
	
	/**createDungeonFrame 
	 * 
	 * Creates a dungeon frame that will be house the generated dungeon.
	 * 
	 * @param resolution_x - Number of pixels wide the area inside of the frame will be
	 * @param resolution_y - Number of pixels high the area inside of the frame will be
	 * @return a frame of size (x + 16, y + 38) to make up for frame boarders
	 */
	private static JFrame createDungeonFrame(int resolution_x, int resolution_y) {
		JFrame dungeon_frame = new JFrame("Dungeon");						// Create frame
		dungeon_frame.setSize(resolution_x+16, resolution_y+38);			// Fit frame to eventual resolution (hard-coded border size)
        dungeon_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        return dungeon_frame;
	}
	
	/**createLoadingPanel
	 * 
	 * Adds a loading screen to dungeon frame while the map is being generated.
	 * 
	 * @param dungeon_frame - Frame that will eventually host the dungeon
	 * @return panel with loading information.
	 */
	private static JPanel createLoadingPanel(JFrame dungeon_frame) {
		JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        JLabel label = new JLabel();
        dungeon_frame.add(panel);
        panel.add(label);
        label.setText("Loading Map");
        
        return panel;
	}
	
	/**generateDungeon
	 * 
	 * Generates a dungeon using DungeonGenerator. Dungeon will be a set of rooms connected by hallways
	 * 
	 * @param tiles_x - number of tiles horizontally
	 * @param tiles_y - number of tiles vertically
	 * @param seed	- seed for dungeon generation to recreate previously built dungeons.
	 * @return a generatedDungeon of size x by y with rooms, walls, a player spawn and exit
	 */
	private static DungeonGenerator generateDungeon(int tiles_x, int tiles_y, long seed) {
		DungeonGenerator myDungeon = new DungeonGenerator(tiles_x, tiles_y);
		// Create a blank dungeon that will fill the frame provided

		myDungeon.setSeed(seed);
		System.out.format("World Seed Number %dL\n", seed);
		
		myDungeon.start();											// Populate map with rooms and doors
		
		return myDungeon;

	}

}
