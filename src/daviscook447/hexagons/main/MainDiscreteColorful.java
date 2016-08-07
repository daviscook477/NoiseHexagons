package daviscook447.hexagons.main;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import daviscook447.hexagons.core.NoiseGenerator;
import daviscook447.hexagons.core.Hexagon;
import daviscook447.hexagons.core.HexagonGridGenerator;

// Makes the each color channel more discrete, as in, it can only take on X different values instead of 255 e.g. only 0, 32, 64, 96, etc...
public class MainDiscreteColorful {

	public static final int DEFAULT_WIDTHPX = 1920; // width of rendered image
	public static final int DEFAULT_HEIGHTPX = 1080; // height of rendered image
	public static final float DEFAULT_RADIUSPX = 50.0f; // just changes the size of the hexagon
	public static final float NOISE_SCALE = 0.0013f; // smaller values make the region more similar in color
	
	public static final String IMAGE_FORMAT = "png";
	public static final String DEFAULT_OUT_FILE_PATH = "output/perlin_noise_hexagons_colorful_discrete." + IMAGE_FORMAT;
	private static final int DISCRETENESS = 8; // only looks significantly different towards 64-128
	
	private static final float ROOT_3_OVER_2 = (float) Math.sqrt(3.0) * 0.5f;
	
	private static final int SEED_0 = 109761, SEED_1 = 541, SEED_2 = -7896; // the seeds for the noise generators
	
	public static int scaleToColor(float value) {
		float scale = 0.6f; // higher values make everything more grey scaled
		// technically the noise is in the range -1 to 1 but values close to -1 or 1 are very unlikely - therefore
		// we use a slightly different scaling than just (value+1)/2*255
		int valueScaled = (int) ((value+scale)/(2*scale)*255);
		valueScaled = (int) (valueScaled / (float) DISCRETENESS) * DISCRETENESS;
		// cap noise values to the valid range for colors
		if (valueScaled>255) {
			valueScaled=255;
		}
		if (valueScaled<0) {
			valueScaled=0;
		}
		return valueScaled;
	}
	
	// convert a noise value to a color
	public static Color floatToColor(float red, float green, float blue) {
		return new Color(scaleToColor(red), scaleToColor(green), scaleToColor(blue)); // Many colors
	}
	
	// apply noise to the hexagon grid to make it colored interestingly
	public static void applyNoiseToHexagonGrid(NoiseGenerator[] pngs, Hexagon[][] hexGrid) {
		for (int i = 0; i < hexGrid.length; i++) {
			for (int j = 0; j < hexGrid[i].length; j++) {
				Hexagon currentHex = hexGrid[i][j];
				// get a noise value using octave noise that is scaled by an adjustable coefficient
				// the reason x is modified by ROOT_3_OVER_2 is because the x spacing of the hexagons is more than the y spacing by that factor (it's geometry)
				float redValue = pngs[0].octaveNoiseAt(currentHex.getX()/(ROOT_3_OVER_2)*NOISE_SCALE,currentHex.getY()*NOISE_SCALE,2);
				float greenValue = pngs[1].octaveNoiseAt(currentHex.getX()/(ROOT_3_OVER_2)*NOISE_SCALE,currentHex.getY()*NOISE_SCALE,2);
				float blueValue = pngs[2].octaveNoiseAt(currentHex.getX()/(ROOT_3_OVER_2)*NOISE_SCALE,currentHex.getY()*NOISE_SCALE,2);
				// update the hexes color by converting the noise to a color
				currentHex.updateColor(floatToColor(redValue,greenValue,blueValue));
			}
		}
	}
	
	// create a new BufferedImage
	public static BufferedImage createNewBufferedImage(int width, int height) {
		return new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	}
	
	// write the hex grid to a buffered image
	public static void writeHexagonGridToBufferedImage(Hexagon[][] hexGrid, BufferedImage image) {
		for (int i = 0; i < hexGrid.length; i++) {
			for (int j = 0; j < hexGrid[i].length; j++) {
				// draw each individual hexagon onto the image
				hexGrid[i][j].drawToBuffer(image, true);
			}
		}
	}
	
	// write the image to the output file
	public static void writeBufferedImageToFile(BufferedImage image, File file) throws IOException {
		// use ImageIO to write it
		ImageIO.write(image, IMAGE_FORMAT, file);
	}
	
	// main program handles command line arguments, generating the hex grids, coloring them, and saving the image
	public static void main(String[] args) {
		// command line args and defaults
		int widthPX = DEFAULT_WIDTHPX,
				heightPX = DEFAULT_HEIGHTPX;
		float radiusPX = DEFAULT_RADIUSPX;
		String outFilePath = DEFAULT_OUT_FILE_PATH;
		if (args.length>=1) {
			widthPX = Integer.parseInt(args[0]);
		}
		if (args.length>=2) {
			heightPX = Integer.parseInt(args[1]);
		}
		if (args.length>=3) {
			radiusPX = Float.parseFloat(args[2]);
		}
		if (args.length>=4) {
			outFilePath = args[3];
		}
		NoiseGenerator[] pngs = new NoiseGenerator[3];
		pngs[0] = new NoiseGenerator(SEED_0);
		pngs[1] = new NoiseGenerator(SEED_1);
		pngs[2] = new NoiseGenerator(SEED_2);
		HexagonGridGenerator hgg = new HexagonGridGenerator();
		// make a hex grid
		Hexagon[][] hexGrid = hgg.generateGrid(widthPX, heightPX, radiusPX);
		// add some noise to it to change its colors
		applyNoiseToHexagonGrid(pngs, hexGrid);
		// make an image
		BufferedImage image = createNewBufferedImage(widthPX, heightPX);
		// draw the hex grid on the image
		writeHexagonGridToBufferedImage(hexGrid, image);
		// save the image as a file
		File outFile = new File(outFilePath);
		try {
			writeBufferedImageToFile(image, outFile);
		} catch (IOException e) {
			System.out.println("Could not write to the output file! An IOException occurred!");
			e.printStackTrace();
		}
	}
	
}
