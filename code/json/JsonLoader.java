package json;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.FileReader;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

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
	ArrayList<Sprite> sprites = new ArrayList<Sprite>();
	int x = 0;
	int y = 0;

	float scale = 1;
	private BufferedImage mapimage;
	AffineTransform transform = new AffineTransform();

	private ArrayList<TileLayer> layerslist = new ArrayList<>();
	private TileMap map;
	private int height;
	private int width;

	public ContentPane() {
		for (int i = 0; i < 25; i++) {
			Point2D positie = new Point2D.Double(Math.random() * 400, Math.random() * 200);
			sprites.add(new Sprite(positie));
		}
		new Timer(1, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (Sprite b : sprites) {
					b.update(sprites);
				}
				repaint();
			}
		}).start();
		// filepath to jsonfile
		crunchJson("EndMap.json", "Tiled2.png");
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
			mapimage = new BufferedImage(width * map.getTileWidth(), height * map.getTileHeight(),
					BufferedImage.TYPE_INT_ARGB);
			drawMapImage();
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
		transform = new AffineTransform();
		transform.scale(scale, scale);
		transform.translate(x, y);

		g2d.setTransform(transform);
		
		g2d.drawImage(mapimage, new AffineTransform(), this);
		
		for (Sprite b : sprites) {

			b.draw(g2d);
		}

		g2d.setTransform(oldtransform);
	}

	int oldX = -1;
	int oldY = -1;

	public BufferedImage drawMapImage() {
		Graphics2D g = (Graphics2D) mapimage.getGraphics();

		for (TileLayer layer : layerslist) {
			g.drawImage(layer.getLayerImage(), 0, 0, null);
		}

		return mapimage;
	}

	public void mouseDragged(MouseEvent e) {

		x += -1 * ((oldX - e.getX()) / scale);
		y += -1 * ((oldY - e.getY()) / scale);
		oldX = e.getX();
		oldY = e.getY();
		repaint();
	}

	class zoomMap implements MouseWheelListener {
		float maxScale = 1.20f;
		float minScale = 0.90f;

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {

			double delta = -(0.05f * e.getPreciseWheelRotation());

			scale += delta;
			if (scale <= minScale) {
				scale = minScale;
			} else if (scale >= maxScale) {
				scale = maxScale;
			}
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
