package dungeon_generator;

/**
 * Tile
 * 
 * @author Michael
 *
 * Contains tile data for each tile on the map
 */
public class Tile {
	int material;						//material the area will be made out of  0 = Stone
										//                                       1 = Floor
										//										 2 = Player Spawn
										//                                       3 = Exit
	
	/**
	 * Tile
	 * 
	 * Constructor - initializes each tile as stone
	 */
	public Tile() {
		material = 0;
	}

	/**
	 * getMaterial
	 * 
	 * @return material
	 */
	public int getMaterial (){
		return material;
	}
	
	/**
	 * setMaterial
	 * @param material - allows setting of the material
	 */
	public void setMaterial (int material){
		this.material = material;
	}
}
