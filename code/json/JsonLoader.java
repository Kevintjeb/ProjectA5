package json;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.io.FileReader;
import java.util.ArrayList;

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
		JPanel pane = new ContentPane();
		frame.setContentPane(pane);
		frame.setSize(1000, 1000);
		frame.setVisible(true);
	}

}

class ContentPane extends JPanel implements MouseMotionListener, MouseListener {
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

	public ContentPane() {

		// filepath to jsonfile
		crunchJson("C:\\Users\\Rick\\Desktop\\test2.json","C:\\Users\\Rick\\Downloads\\ground_tiles.png");//"C:\\Users\\kevin\\Desktop\\tilemap.json", "C:\\Users\\kevin\\Downloads\\tileset\\ground_tiles.png");
		addMouseMotionListener(this);
		addMouseListener(this);
		addMouseWheelListener(new zoomMap());
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

	public int getMapWidth() {
		return width;
	}

	public int getMapHeight() {
		return height;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform oldtransform = g2d.getTransform();
		// g2d.drawImage(layerslist.get(0).getLayerImage(), 0, 0, this);
		// g2d.setClip(null);
		transform = new AffineTransform();
		transform.scale(scale, scale);
		transform.translate(x, y);
		// transform.translate(-getWidth()/2, -getHeight()/2);
		
		g2d.setTransform(transform);
		g2d.drawImage(layerslist.get(0).getLayerImage(), transform, this);
		g2d.setTransform(oldtransform);
	}

	int oldX = -1;
	int oldY = -1;

	public void mouseDragged(MouseEvent e) {
		
		System.out.println("translate x " + x + " y " + y);
		x += -0.5 * ((oldX - e.getX())/scale);
		y += -0.5 * ((oldY - e.getY())/scale);
		oldX = e.getX();
		oldY = e.getY();
		System.out.println("te transleren x " + x + " y " + y);
		repaint();
		System.out.println("done");
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

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		oldX = e.getX();
		oldY = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
