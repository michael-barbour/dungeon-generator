# dungeon-generator
﻿
﻿My dungeon generator contains five classes.  
 * DungeonGenerator.java contains the main method and constructs a random map based on a default random seed.
 * DungeonPanel.java inherits from swing panels and creates a visual panel that represents the dungeon
 * Tile.java contains the tile data for each tile
 * Room.java is used by DungeonGenerator and allows the mapping from one room to the next.
 * main.java is used to create a dungeon from a static context.  Use this to actually build the dungeon.

﻿The program should run using any standard java IDE or compiler.  Compile the classes then run DungeonGenerator.  Maps contain randomly placed rooms connected by hallways.  
*         Floors will be wood sprites
*         Walls will be grey sprites
*         Player spawn is a ladder that ends on the floor you are on
*         The exit is a ladder that proceeds deeper into the Dungeon.

﻿All values that can be manipulated exist in main class including the size of each tile in pixels, the default frame size, the default map size, and the random seed.

﻿The seed for the random number generator will be printed to the terminal each time the program is run and can be hardcoded into rand when constructing DungeonGenerator object to allow the creation of the same map repeatedly.
 
﻿The Graphics are handled by the default java swing class.
