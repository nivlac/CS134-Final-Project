package edu.sjsu.cs134;


public class Background {
	private int width;
	private int height;
	private Tile[] background;

	public Background(int image, boolean collision, int start, int end) {
		width = 30;
		height = 20;
		background = new Tile[width * height];
		for (int i = start; i < end; i++) {
			background[i] = new Tile(image, collision);
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Tile getTile(int x, int y) {
		return background[y * width + x];
	}
	
}