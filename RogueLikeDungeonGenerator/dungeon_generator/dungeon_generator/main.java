package dungeon_generator;

import java.util.Random;
import javax.swing.JFrame;

public class main {

	/**
	 *  main()
	 * 
	 * 	Creates an instance of myDungeon from a static context.
	 */	
	public static void main(String[] args){
		int resolution_x = 510, resolution_y = 510;					// Set resolution for eventual DungeonPanel
		int tile_width = 15, tile_height = 15;						// Set desired height/width of tiles	
		
		Random x = new Random();
		long seed = x.nextLong();									// Create Random world seed	
		
		JFrame frame = new JFrame();								// Create frame
		frame.setVisible(true);
		frame.setSize(resolution_x+16, resolution_y+38);			// Fit frame to eventual resolution (hard-coded border size)
				
		DungeonGenerator myDungeon = new DungeonGenerator(resolution_x/tile_width, resolution_y/tile_height);
																	// Create a blank dungeon that will fill the frame provided
		
		myDungeon.setSeed(seed);
		System.out.format("World Seed Number %dL\n", seed);
		
		myDungeon.start();											// Populate map with rooms and doors
		
		DungeonPanel myDungeonPanel = new DungeonPanel(myDungeon.getTileMap(), tile_width, tile_height);
																	// Create a panel from tile_map, draw tiles of inputed size
		
		myDungeonPanel.setBounds(0, 0, resolution_x, resolution_y);
		myDungeonPanel.setVisible(true);
		frame.add(myDungeonPanel);
	}

}
