package json;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JsonLoader {

	public static void main(String[] args) {
		new JsonLoader();
	}

	public JsonLoader() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new ContentPane());
		frame.setVisible(true);
		frame.setSize(1000, 1000);
	}

}

class ContentPane extends JPanel {
	/**
	 * 
	 */

	int x = 0;
	int y = 0;

	float scale = 1;

	AffineTransform transform = new AffineTransform();

	private ArrayList<TileLayer> layerslist = new ArrayList<>();
	private TileMap map;
	private int height;
	private int width;
	private static final long serialVersionUID = -6211040993419149907L;

	public ContentPane() {
		// filepath to jsonfile
		crunchJson("C:\\Users\\kevin\\Desktop\\tilemap.json", "C:\\Users\\kevin\\Downloads\\tileset\\ground_tiles.png");
		// setSize(new Dimension(1000, 2000));
		// setMinimumSize(new Dimension(500, 500));
		addMouseMotionListener(new dragMap());
		addMouseWheelListener(new zoomMap());

		try {
			ImageIO.write(layerslist.get(0).getLayerImage(), "png", new File("tmp.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void crunchJson(String filepath, String tilemappath) {
		JSONParser parser = new JSONParser();

		String mappath = tilemappath;
		try {

			// tilemap maken door een tilemap object aan te maken.
			map = new TileMap(mappath, 32, 32);

			// jsonfile parsen
			Object file = parser.parse(new FileReader(filepath));
			JSONObject json = (JSONObject) file;
			JSONArray layers = (JSONArray) json.get("layers");
			height = ((Long) json.get("height")).intValue();
			width = ((Long) json.get("width")).intValue();

			// jsonfile uitlezen op layers en toevoegen als tilelayer in
			// tilelayer arraylist.
			for (int i = 0; i < layers.size(); i++) {
				JSONObject currentlayer = (JSONObject) layers.get(i);
				TileLayer e = new TileLayer((JSONArray) currentlayer.get("data"), map, height, width);
				layerslist.add(e);
			}
			// tileheight en width bepalen door uit de json te halen.

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		// g2d.drawImage(layerslist.get(0).getLayerImage(), 0, 0, this);
		g2d.setClip(null);
		transform.translate(x, y);
		transform.scale(scale, scale);
		g2d.setTransform(transform);
		g2d.drawImage(layerslist.get(0).getLayerImage(), transform, this);
	}

	class dragMap extends MouseMotionAdapter {
		int oldX = 0;
		int oldY = 0;

		public void mouseDragged(MouseEvent e) {
			System.out.println("translate x " + x + " y " + y);
			x = oldX - e.getX();
			y = oldY - e.getY();
			oldX = e.getX();
			oldY = e.getY();
			System.out.println("te transleren x " + x + " y " + y);
			repaint();
			System.out.println("done");
		}
	}

	class zoomMap implements MouseWheelListener {
		float maxScale = 1.20f;
		float minScale = 0.90f;

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			System.out.println("zoom");

			double delta = -(0.05f * e.getPreciseWheelRotation());

			scale += delta;
			if (scale <= minScale) {
				scale = minScale;
			} else if (scale >= maxScale) {
				scale = maxScale;
			}
			System.out.println(scale);
			repaint();
		}
	}

}
