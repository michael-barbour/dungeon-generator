package dungeon_generator;

import java.awt.Color;

/**
 * Tile
 * 
 * @author Michael
 *
 * Contains tile data for each tile on the map
 */
public class Tile {
	public enum Material {												// enum type Material used to store tile colors/type
		STONE (Color.gray),
		FLOOR (new Color(165, 93, 53)),
		PLAYER_SPAWN (Color.green),
		EXIT (Color.black);
		
		private Color color;
		
		Material (Color c){
			color = c;
		}
	}
	
	Material material;
	
	/**
	 * Tile
	 * 
	 * Constructor - initializes each tile as stone
	 */
	public Tile() {
		material = Material.STONE;
	}

	/**
	 * getMaterial
	 * 
	 * @return material
	 */
	public Material getMaterial (){
		return material;
	}
	
	/**
	 * setMaterial
	 * @param material - allows setting of the material
	 */
	public void setMaterial (Material material){
		this.material = material;
	}
	
	public Color getMaterialColor(){
		return material.color;
	}
}
