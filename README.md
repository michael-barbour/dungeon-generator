﻿# dungeon-generator
﻿
﻿My dungeon generator contains two classes.  DungeonGenerator.java contains the main method and constructs a random map based on a default random seed.  Tile.java contains the tile data and needs to be in the same project as DungeonGenerator.java.

﻿The program should run using any standard java IDE or compiler.  Compile the classes then run DungeonGenerator.  Maps contain randomly placed rooms connected by hallways.  
*         Floors will be brown
*         Walls will be checkered gray and black
*         Player spawn is green
*         The exit is black.

﻿All values to manipulate map generation can be found at the top of DungeonGenerator.java. 
*         Map Size can be manipulated by modifying map_size_width and map_size_height
*         Tile Size can be manipulted by modifying tile_width and tile_height
*         The varity of room sizes can be manipulated using min_room_size and max_room_size
*         The probability of generating rooms can be increased by increasing max_failures or decreased by decreasing max_failures

﻿The seed for the RNG will be printed to the terminal each time the program is run and can be hardcoded into rand to allow the creation of the same map repeatedly.
