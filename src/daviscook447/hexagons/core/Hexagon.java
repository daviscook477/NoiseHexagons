package daviscook447.hexagons.core;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;

public class Hexagon {

	private static final float ROOT_3_OVER_3 = (float) Math.sqrt(3) / 3.0f;
	private static final float ROOT_3_OVER_6 = ROOT_3_OVER_3 * 0.5f;
	private static final float ONE_HALF = 0.5f;
	private static final float ZERO = 0.0f;
	
	private static final int DIMENSIONS_IN_2D = 2;
	private static final int POINTS_IN_HEXAGON = 7; // 6 + 1 for cycling
	
	// this array holds the template for a hexagon's six points
	private static final float[][] HEXAGON_POINT_TEMPLATE = {
			{ROOT_3_OVER_6, ROOT_3_OVER_3, ROOT_3_OVER_6, -ROOT_3_OVER_6, -ROOT_3_OVER_3, -ROOT_3_OVER_6, ROOT_3_OVER_6},
			{ONE_HALF, ZERO, -ONE_HALF, -ONE_HALF, ZERO, ONE_HALF, ONE_HALF}
	};
	
	// creates a hexagon using the template
	private static final float[][] createHexagonFromCenter(float x, float y, float radius) {
		float[][] modifiedCoordinates = new float[DIMENSIONS_IN_2D][POINTS_IN_HEXAGON];
		for (int i = 0; i < modifiedCoordinates.length; i++) {
			for (int j = 0; j < modifiedCoordinates[i].length; j++) {
				// basically modify the template by scaling it according to the radius and offsetting it by (x,y)
				modifiedCoordinates[i][j] = HEXAGON_POINT_TEMPLATE[i][j]*radius+(i==0?x:y);
			}
		}
		return modifiedCoordinates;
	}
	
	public static final Color DEFAULT_HEXAGON_COLOR = Color.BLACK;
	
	private float x, y, radius;
	private Color color;
	
	public Hexagon(float x, float y, float radius) {
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.color = DEFAULT_HEXAGON_COLOR;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public void updateColor(Color color) {
		this.color = color;
	}
	
	// draws to an image using a path made from the hexagon's points
	public void drawToBuffer(BufferedImage image, boolean drawOutline) {
		Graphics2D g2D = image.createGraphics();
		g2D.setColor(color);
		g2D.setStroke(new BasicStroke(1));
		//System.out.println("Coloring with " + color);
		float[][] hexPoints = createHexagonFromCenter(x, y, radius);
		Path2D path = new Path2D.Float();
		for (int i = 0; i < hexPoints[0].length; i++) {
			if (i == 0) {
				path.moveTo(hexPoints[0][i], hexPoints[1][i]);
			} else {
				path.lineTo(hexPoints[0][i], hexPoints[1][i]);
			}
		}
		g2D.fill(path);
		// draw an outline if desired
		if (drawOutline) {
			g2D.setColor(DEFAULT_HEXAGON_COLOR);
			g2D.setStroke(new BasicStroke(2));
			g2D.draw(path);
		}
	}
	
}
