package daviscook447.hexagons.core;

public class HexagonGridGenerator {

	public HexagonGridGenerator() {
		
	}
	
	private static final float ROOT_3_OVER_2 = (float) Math.sqrt(3.0) * 0.5f;
	private static final float ONE_HALF = 0.5f;
	
	public static final float distance(float x1, float y1, float x2, float y2) {
		float dx = x1-x2;
		float dy = y1-y2;
		return (float) Math.sqrt(dx*dx+dy*dy);
	}
	
	// generates a grid of hexagons to cover the whole height and width of the image
	public Hexagon[][] generateGrid(int widthPX, int heightPX, float radiusPX) {
		int cols = (int) Math.ceil(widthPX / (radiusPX*ROOT_3_OVER_2))+1; // plus one to avoid artifacts at the edge of the grid
		int rows = (int) Math.ceil(heightPX / (radiusPX*ROOT_3_OVER_2))+1; // plus one to avoid artifacts at the edge of the grid
		Hexagon[][] hexGrid = new Hexagon[cols][rows];
		
		for (int i = 0; i < hexGrid.length; i++) {
			for (int j = 0; j < hexGrid[i].length; j++) {
				float x = i*radiusPX*ROOT_3_OVER_2; // x is spaced out farther because of a hexagon's geometry
				float y = j*radiusPX;
				if (i%2==0) {
					y += radiusPX*ONE_HALF; // offset every other row of hexagons in the vertical direction
				}
				hexGrid[i][j] = new Hexagon(x, y, radiusPX);
			}
		}
		return hexGrid;
	}
	
}
