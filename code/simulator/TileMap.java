package simulator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class TileMap {

	BufferedImage[] tiles;
	private int tilewidth;
	private int tileheight;
	private int mapheight;
	private int mapwidth;
	private BufferedImage tilemap;

	public TileMap(String filepath, int tw, int th) {
		this.tileheight = th;
		this.tilewidth = tw;

		try {
			tilemap = ImageIO.read(new File(filepath));

			if (tilemap == null) {
				//System.out.println("BAD FILE");
			}

			mapheight = tilemap.getHeight();
			mapwidth = tilemap.getWidth();

			//System.out.println(" tilemap laden gelukt!");

		} catch (IOException e) {
			e.printStackTrace();
		}

		tiles = new BufferedImage[mapheight * mapwidth + 1];
		int i = 1;
		for (int y = 0; y < mapheight / 32; y++) {
			for (int x = 0; x < mapwidth / 32; x++) {
				tiles[i] = tilemap.getSubimage(x * tilewidth, y * tileheight, tilewidth, tileheight);
				i++;
			}
		}

	}

	public BufferedImage[] getTiles() {
		return tiles;
	}

	public int getTileHeight() {
		return tileheight;
	}

	public int getTileWidth() {
		return tilewidth;
	}

	public int getmapheight() {
		return mapheight;
	}

}
