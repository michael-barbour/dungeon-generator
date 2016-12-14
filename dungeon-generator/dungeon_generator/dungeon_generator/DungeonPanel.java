package dungeon_generator;



import java.awt.Graphics;
import java.awt.image.*;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Dungeon Panel
 * 
 * @author Michael Barbour
 *
 *	Allows custom paint function.  
 */
class DungeonPanel extends JPanel{
	private static final long serialVersionUID = -1633734159866132802L;
	Tile[][] tile_map;
	int tile_width;
	int tile_height;
	
	/**
	 * Dungeon Panel
	 * 
	 * @param tile_map - 2d tile array containing all of the materials used
	 * @param tile_width - width of tiles
	 * @param tile_height - height of tiles
	 * 
	 * Constructs a panel to draw a map using these variables
	 */
	public DungeonPanel(Tile[][] tile_map, int tile_width, int tile_height){
		this.tile_map = tile_map;
		this.tile_width = tile_width;
		this.tile_height = tile_height;
	}
	
	/**
	 * paint
	 * 
	 * @param g - Graphic object handled by panel.
	 * 
	 * Paints the current conditions to the panel.
	 * 			Paints all tiles in the current map with the material colors
	 */
	@Override
	public void paint(Graphics g){
		for(int i = 0; i < tile_map[0].length; i++){					//Traverse tile_map row by column and draw a rectangle to represent each tile
			for(int j = 0; j < tile_map.length; j++){
				try {
					URL url = tile_map[j][i].getMaterialURL();
			        BufferedImage image = ImageIO.read(url);
			        
			        g.drawImage(image, j*tile_width, i*tile_height, tile_width, tile_height, null);
			    } catch (IOException e){
					System.out.print(e);
				}
			}
		}
	}
}
