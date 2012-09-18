package me.daddychurchill.CityWorld.Clipboard;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import me.daddychurchill.CityWorld.CityWorld;
import me.daddychurchill.CityWorld.WorldGenerator;
import me.daddychurchill.CityWorld.Support.Direction;
import me.daddychurchill.CityWorld.Support.RealChunk;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.CuboidClipboard.FlipDirection;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.schematic.SchematicFormat;

public class Clipboard_WorldEdit extends Clipboard {

	private BaseBlock[][][][] blocks;
	private int facingCount;
	private boolean flipableX = true;
	private boolean flipableZ = true;
//  private boolean Rotatable = false;
//	private boolean ScalableXZ = false;
//	private boolean ScalableY = false;
//	private int FloorHeightY = DataContext.FloorHeight;

	private final static String metaExtension = ".yml";
	private final static String tagGroundLevelY = "GroundLevelY";
	private final static String tagFlipableX = "FlipableX";
	private final static String tagFlipableZ = "FlipableZ";
//	private final static String tagScalableX = "ScalableX";
//	private final static String tagScalableZ = "ScalableZ";
//	private final static String tagScalableY = "ScalableY";
//	private final static String tagFloorHeightY = "FloorHeightY";
	private final static String tagOddsOfAppearance = "OddsOfAppearance";
	
	public Clipboard_WorldEdit(WorldGenerator generator, File file) throws IOException, DataException, InvalidConfigurationException {
		super(generator, file.getName());
		
		// prepare to read the meta data
		YamlConfiguration metaYaml = new YamlConfiguration();
		metaYaml.options().header("CityWorld/WorldEdit schematic configuration");
		metaYaml.options().copyDefaults(true);
		
		// add the defaults
		metaYaml.addDefault(tagGroundLevelY, groundLevelY);
		metaYaml.addDefault(tagFlipableX, flipableX);
		metaYaml.addDefault(tagFlipableZ, flipableZ);
//		metaYaml.addDefault(tagScalableX, ScalableX);
//		metaYaml.addDefault(tagScalableZ, ScalableZ);
//		metaYaml.addDefault(tagScalableY, ScalableY);
//		metaYaml.addDefault(tagFloorHeightY, FloorHeightY);
		metaYaml.addDefault(tagOddsOfAppearance, oddsOfAppearance);
		
		// start reading it
		File metaFile = new File(file.getAbsolutePath() + metaExtension);
		if (metaFile.exists()) {
			metaYaml.load(metaFile);
			groundLevelY = Math.max(0, metaYaml.getInt(tagGroundLevelY, groundLevelY));
			flipableX = metaYaml.getBoolean(tagFlipableX, flipableX);
			flipableZ = metaYaml.getBoolean(tagFlipableZ, flipableZ);
//			ScalableX = metaYaml.getBoolean(tagScalableX, ScalableX) && sizeX == 3;
//			ScalableZ = metaYaml.getBoolean(tagScalableZ, ScalableZ) && sizeZ == 3;
//			ScalableY = metaYaml.getBoolean(tagScalableY, ScalableY);
//			FloorHeightY = Math.max(2, Math.min(16, metaYaml.getInt(tagFloorHeightY, FloorHeightY)));
			oddsOfAppearance = Math.max(0.0, Math.min(1.0, metaYaml.getDouble(tagOddsOfAppearance, oddsOfAppearance)));
		}
		
		// try and save the meta data if we can
		try {
			metaYaml.save(metaFile);
		} catch (IOException e) {
			CityWorld.reportException("[Clipboard] Could not resave " + metaFile.getAbsolutePath(), e);
		}
		
		// load the actual blocks
		CuboidClipboard cuboid = SchematicFormat.getFormat(file).load(file);
		
		// how big is it?
		setSizes(cuboid.getWidth(), cuboid.getHeight(), cuboid.getLength());
		
		// allocate the blocks
		facingCount = 1;
//		if (flipableX)
//			facingCount *= 2;
//		if (flipableZ)
//			facingCount *= 2;
		
		//TODO we should allocate only facing count, then allocate the size based on what comes out of the rotation.. once I do rotation
		// allocate room
		blocks = new BaseBlock[facingCount][sizeX][sizeY][sizeZ];
		
		// copy the cubes for each direction
		copyCuboid(cuboid, 0); // normal one
//		if (flipableX) {
//			cuboid.flip(FlipDirection.WEST_EAST);
//			copyCuboid(cuboid, 1);
//			
//			// z too? if so then make two more copies
//			if (flipableZ) {
//				cuboid.flip(FlipDirection.NORTH_SOUTH);
//				copyCuboid(cuboid, 3);
//				cuboid.flip(FlipDirection.WEST_EAST);
//				copyCuboid(cuboid, 2);
//			}
//		
//		// just z
//		} else if (flipableZ) {
//			cuboid.flip(FlipDirection.NORTH_SOUTH);
//			copyCuboid(cuboid, 1);
//		}
	}
	
	private void copyCuboid(CuboidClipboard cuboid, int facing) {
	    for (int x = 0; x < sizeX; x++)
	        for (int y = 0; y < sizeY; y++)
	          for (int z = 0; z < sizeZ; z++)
	        	  blocks[facing][x][y][z] = cuboid.getPoint(new Vector(x, y, z));
	}
	
	private EditSession getEditSession(WorldGenerator generator) {
		return new EditSession(new BukkitWorld(generator.getWorld()), blockCount);
	}
	
	private int getFacingIndex(Direction.Facing facing) {
		return 0;
//		switch (facing) {
//		case NORTH:
//			return 0;
//		case SOUTH:
//			return Math.min(facingCount, 1);
//		case WEST:
//			return Math.min(facingCount, 2);
//		default: // case EAST:
//			return Math.min(facingCount, 3);
//		}
	}
	
	@Override
	public void paste(WorldGenerator generator, RealChunk chunk, Direction.Facing facing, int blockX, int blockY, int blockZ) {
		Vector at = new Vector(blockX, blockY, blockZ);
		try {
			EditSession editSession = getEditSession(generator);
			//editSession.setFastMode(true);
			place(editSession, getFacingIndex(facing), at, true);
		} catch (Exception e) {
			CityWorld.reportException("[WorldEdit] Place schematic " + name + " at " + at + " failed", e);
		}
	}

	
	@Override
	public void paste(WorldGenerator generator, RealChunk chunk, Direction.Facing facing, 
			int blockX, int blockY, int blockZ,
			int x1, int x2, int y1, int y2, int z1, int z2) {
		
//		CityWorld.reportMessage("Partial paste: origin = " + at + " min = " + min + " max = " + max);
		
		try {
			int iFacing = getFacingIndex(facing);
			EditSession editSession = getEditSession(generator);
			//editSession.setFastMode(true);
			for (int x = x1; x < x2; x++)
				for (int y = y1; y < y2; y++)
					for (int z = z1; z < z2; z++) {
//						CityWorld.reportMessage("facing = " + iFacing + 
//								" x = " + x +
//								" y = " + y + 
//								" z = " + z);
						if (blocks[iFacing][x][y][z].isAir()) {
							continue;
						}
						editSession.setBlock(new Vector(x, y, z).add(blockX, blockY, blockZ), 
								blocks[iFacing][x][y][z]);
					}
		} catch (Exception e) {
			e.printStackTrace();
			CityWorld.reportException("[WorldEdit] Partial place schematic " + name + " failed", e);
		}
	}

	
//	@Override
//	public void paste(WorldGenerator generator, RealChunk chunk, Direction.Facing facing, 
//			int blockX, int blockY, int blockZ,
//			int x1, int x2, int y1, int y2, int z1, int z2) {
//		Vector at = new Vector(blockX, blockY, blockZ);
//		Vector min = new Vector(x1, y1, z1);
//		Vector max = new Vector(x2, y2, z2);
//		
//		CityWorld.reportMessage("Partial paste: origin = " + at + " min = " + min + " max = " + max);
//		
//		try {
//			EditSession editSession = getEditSession(generator);
//			//editSession.setFastMode(true);
//			place(editSession, getFacingIndex(facing), at, true, min, max);
//		} catch (Exception e) {
//			CityWorld.reportException("[WorldEdit] Partial place schematic " + name + " at " + at + " failed", e);
//		}
//	}
//
	//TODO Pilfered from WorldEdit's CuboidClipboard... I need to remove this once the other Place function is used
	private void place(EditSession editSession, int facing, Vector pos, boolean noAir)
			throws MaxChangedBlocksException {
		for (int x = 0; x < sizeX; x++)
			for (int y = 0; y < sizeY; y++)
				for (int z = 0; z < sizeZ; z++) {
					if ((noAir) && (blocks[facing][x][y][z].isAir())) {
						continue;
					}
					editSession.setBlock(new Vector(x, y, z).add(pos),
							blocks[facing][x][y][z]);
				}
	}

	//TODO if WorldEdit ever gets this functionality I need to remove the modified code
	private void place(EditSession editSession, int facing, Vector pos, boolean noAir,
			Vector min, Vector max) throws MaxChangedBlocksException {
		int x1 = Math.max(min.getBlockX(), 0);
		int x2 = Math.min(max.getBlockX(), sizeX);
		int y1 = Math.max(min.getBlockY(), 0);
		int y2 = Math.min(max.getBlockY(), sizeY);
		int z1 = Math.max(min.getBlockZ(), 0);
		int z2 = Math.min(max.getBlockZ(), sizeZ);
		for (int x = x1; x < x2; x++)
			for (int y = y1; y < y2; y++)
				for (int z = z1; z < z2; z++) {
					if ((noAir) && (blocks[facing][x][y][z].isAir())) {
						continue;
					}
					editSession.setBlock(new Vector(x, y, z).add(pos), blocks[facing][x][y][z]);
				}
	}	
}