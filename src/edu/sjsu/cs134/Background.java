package edu.sjsu.cs134;


public class Background {
	private int width;
	private int height;
	private Tile[] background;

	public Background(int image, boolean collision, int start, int end, int width, int height) {
		this.width = width;
		this.height = height;
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