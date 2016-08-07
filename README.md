# Noise Hexagons

This project is designed to create interesting images using noise applied to a hexagon grid.

Here's an example of the output:

![The example image could not be displayed!][example_image]

[example_image]: https://github.com/daviscook477/NoiseHexagons/tree/master/examples/noise_hexagons_colorful.png "Noise Hexagons Colorful"

The project works by first constructing a grid of hexagons and keeping track of the center point of each hexagon. Then it uses some scheme to determine the color of the hexagon by looking at its center point. It then colors each hexagon according to that scheme.
Lastly, it writes the created image to a file.

5 different coloring schemes are provided, each of them uses [simplex noise](https://en.wikipedia.org/wiki/Simplex_noise) to generate variation in the color:
* Main.java is a simple greyscale coloring scheme that maps the noise to a range of [0, 255] and uses that as the RGB value of the hexagon.
* MainColorful.java uses the same principles as the greyscale version, but generates 3 channels of noise and uses them to separately specify the R, G, and B value of the color for each hexagon.
* MainDiscrete.java is also greyscale but changes it such that RGB values can only be multiples of X instead of multiples of 1.
* MainDiscreteColorful.java is the same as MainDiscrete.java but uses the 3 channels of noise like MainColorful.java
* MainInterpolate.java is very similar to Main.java but instead of interpolating between BLACK and WHITE it uses any two colors