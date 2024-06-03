package edu.cis;
import acm.graphics.*;

public class DarkRoomAlgorithms implements DarkRoomAlgorithmsInterface {

	public GImage rotateLeft(GImage source) {
		int[][] allPixels = source.getPixelArray();
		int rowsNum = allPixels.length;
		int colNum = allPixels[0].length;

		int[][] newPixels = new int[colNum][rowsNum];

		for (int i = 0; i < rowsNum; i++) {
			for (int j = 0; j < colNum; j++) {
				newPixels[j][rowsNum - i - 1] = allPixels[i][j];
			}
		}

		GImage newImage = new GImage(newPixels);
		return newImage;
	}


	public GImage rotateRight(GImage source) {
			int[][] allPixels = source.getPixelArray();
			int rowsNum = allPixels.length;
			int colNum = allPixels[0].length;

			int[][] newPixels = new int[colNum][rowsNum];

			for (int i = 0; i < rowsNum; i++) {
				for (int j = 0; j < colNum; j++) {
					newPixels[j][rowsNum - i - 1] = allPixels[i][j];
				}
			}

			GImage newImage = new GImage(newPixels);
			return newImage;
		}


		public GImage flipHorizontal(GImage source) {
			int[][] allPixels = source.getPixelArray();
			int rowsNum = allPixels.length;
			int colNum = allPixels[0].length;

			int[][] newPixels = new int[rowsNum][colNum];

			for (int i = 0; i < rowsNum; i++) {
				for (int j = 0; j < colNum; j++) {
					newPixels[i][j] = allPixels[i][colNum - j - 1];
				}
			}

			GImage newImage = new GImage(newPixels);
			return newImage;
		}

	public GImage negative(GImage source) {
		int[][] allPixels = source.getPixelArray();
		int rowsNum = allPixels.length;
		int colNum = allPixels[0].length;

		int[][] newPixels = new int[rowsNum][colNum];

		for (int i = 0; i < rowsNum; i++) {
			for (int j = 0; j < colNum; j++) {
				int red = 255 - GImage.getRed(allPixels[i][j]);
				int green = 255 - GImage.getGreen(allPixels[i][j]);
				int blue = 255 - GImage.getBlue(allPixels[i][j]);
				newPixels[i][j] = GImage.createRGBPixel(red, green, blue);
			}
		}

		GImage newImage = new GImage(newPixels);
		return newImage;
	}


	public GImage greenScreen(GImage source) {
		int[][] allPixels = source.getPixelArray();
		int rowsNum = allPixels.length;
		int colNum = allPixels[0].length;

		int[][] newPixels = new int[rowsNum][colNum];

		for (int i = 0; i < rowsNum; i++) {
			for (int j = 0; j < colNum; j++) {
				int pixel = allPixels[i][j];
				int red = GImage.getRed(pixel);
				int green = GImage.getGreen(pixel);
				int blue = GImage.getBlue(pixel);

				if (green >= 2 * Math.max(red, blue)) {
					newPixels[i][j] = GImage.createRGBPixel(red, green, blue, 0);
				} else {
					newPixels[i][j] = pixel;
				}
			}
		}

	GImage newImage = new GImage(newPixels);
    return newImage;
}


		public GImage blur(GImage source) {
			int[][] allPixels = source.getPixelArray();
			int rowsNum = allPixels.length;
			int colNum = allPixels[0].length;

			int[][] newPixels = new int[rowsNum][colNum];

			for (int i = 0; i < rowsNum; i++) {
				for (int j = 0; j < colNum; j++) {
					int redSum = 0;
					int greenSum = 0;
					int blueSum = 0;
					int count = 0;

					for (int dx = -1; dx <= 1; dx++) {
						for (int dy = -1; dy <= 1; dy++) {
							int row = i + dx;
							int col = j + dy;

							if (row >= 0 && row < rowsNum && col >= 0 && col < colNum) {
								int pixel = allPixels[row][col];
								redSum += GImage.getRed(pixel);
								greenSum += GImage.getGreen(pixel);
								blueSum += GImage.getBlue(pixel);
								count++;
							}
						}
					}

					int avgRed = redSum / count;
					int avgGreen = greenSum / count;
					int avgBlue = blueSum / count;

					newPixels[i][j] = GImage.createRGBPixel(avgRed, avgGreen, avgBlue);
				}
			}

			GImage newImage = new GImage(newPixels);
			return newImage;
		}

	public GImage crop(GImage source, int cropX, int cropY, int cropWidth, int cropHeight) {
		int[][] allPixels = source.getPixelArray();
		int[][] newPixels = new int[cropHeight][cropWidth];

		for (int i = 0; i < cropHeight; i++) {
			for (int j = 0; j < cropWidth; j++) {
				newPixels[i][j] = allPixels[cropY + i][cropX + j];
			}
		}

		GImage croppedImage = new GImage(newPixels);
		return croppedImage;
	}

	public GImage equalize(GImage source) {
			int[] luminosityHistogram = computeLuminosityHistogram(source);
			int[] cumulativeHistogram = computeCumulativeHistogram(luminosityHistogram);
			int[][] pixels = source.getPixelArray();
			int[][] modifiedPixels = modifyPixels(pixels, cumulativeHistogram);
			return new GImage(modifiedPixels);
		}

// Compute the luminosity histogram for the source image
		private int[] computeLuminosityHistogram(GImage source) {
			int[] histogram = new int[256];
			int[][] pixels = source.getPixelArray();

			for (int[] row : pixels) {
				for (int pixel : row) {
					int red = GImage.getRed(pixel);
					int green = GImage.getGreen(pixel);
					int blue = GImage.getBlue(pixel);
					int luminosity = computeLuminosity(red, green, blue);
					histogram[luminosity]++;
				}
			}

			return histogram;
		}

// Compute the cumulative luminosity histogram
		private int[] computeCumulativeHistogram(int[] histogram) {
			int[] cumulativeHistogram = new int[256];
			cumulativeHistogram[0] = histogram[0];

			for (int i = 1; i < histogram.length; i++) {
				cumulativeHistogram[i] = cumulativeHistogram[i - 1] + histogram[i];
			}

			return cumulativeHistogram;
		}

// Modify each pixel to increase contrast using histogram equalization
		private int[][] modifyPixels(int[][] pixels, int[] cumulativeHistogram) {
			int[][] modifiedPixels = new int[pixels.length][pixels[0].length];

			for (int i = 0; i < pixels.length; i++) {
				for (int j = 0; j < pixels[i].length; j++) {
					int pixel = pixels[i][j];
					int red = GImage.getRed(pixel);
					int green = GImage.getGreen(pixel);
					int blue = GImage.getBlue(pixel);
					int luminosity = computeLuminosity(red, green, blue);
					int newLuminosity = (int) (cumulativeHistogram[luminosity] / (double) (pixels.length * pixels[i].length) * 255);
					int modifiedPixel = GImage.createRGBPixel(newLuminosity, newLuminosity, newLuminosity);
					modifiedPixels[i][j] = modifiedPixel;
				}
			}

			return modifiedPixels;
		}

// Helper method to compute luminosity from RGB values
public int computeLuminosity(int red, int green, int blue){
			return (int) (0.299 * red + 0.587 * green + 0.114 * blue);
		}
	}

