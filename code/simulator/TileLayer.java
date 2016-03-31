package simulator;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.json.simple.JSONArray;

public class TileLayer {

	private JSONArray data;
	private TileMap map;
	private int loaderheight;
	private int loaderwidth;
	private BufferedImage tilelayer;
	private boolean visible;

	public TileLayer(JSONArray jsonArray, TileMap m, int jlh, int jlw, boolean b) {
		data = jsonArray;
		map = m;
		loaderheight = jlh;
		loaderwidth = jlw;
		tilelayer = new BufferedImage(loaderheight * map.getTileWidth(), loaderwidth * map.getTileHeight(),
				BufferedImage.TYPE_INT_ARGB);
		visible = b;
		draw();
	}

	public int getTileInteger(int index) {
		return ((Long) data.get(index)).intValue();
	}

	public void draw() {
		if (visible) {
			Graphics2D g2d = (Graphics2D) tilelayer.getGraphics();

			int i = 0;

			for (int y = 0; y < loaderheight; y++) {
				for (int x = 0; x < loaderwidth; x++) {
					g2d.drawImage(map.getTiles()[(getTileInteger(i))], (x * map.getTileWidth()),
							(y * map.getTileHeight()), null);
					i++;
				}
			}
		}
	}

	public BufferedImage getLayerImage() {
		return tilelayer;
	}
}
