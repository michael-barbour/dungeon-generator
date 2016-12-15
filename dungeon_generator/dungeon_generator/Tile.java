package dungeon_generator;

import java.awt.Color;
import java.net.URL;
import java.nio.file.Path;

/**
 * Tile
 * 
 * @author Michael Barbour
 *
 * Contains tile data for each tile on the map
 */
public class Tile {
	
	/**
	 * Material
	 * 
	 * Enum object contains floor sprites and a URL that can be used to find the sprite associated with that floor type.
	 * @author Michael
	 *
	 */
	public enum Material {												// enum type Material used to store tile colors/type
		STONE ("/StoneTiles.png"),
		FLOOR ("/FloorTiles.png"),
		PLAYER_SPAWN ("/SpawnTiles.png"),
		EXIT ("/ExitTiles.png");
		
		private URL url;
		
		Material (String s){
			url = getClass().getResource(s);
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
	
	/** 
	 * getMaterialURL
	 * 
	 * Used for paint methods, location of sprite.
	 * @return url of material's sprite
	 */
	public URL getMaterialURL(){
		return material.url;
	}
}
