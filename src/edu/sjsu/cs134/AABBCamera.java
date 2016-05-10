package edu.sjsu.cs134;

public class AABBCamera extends Camera  {
	
	private int height, width, x, y;

	public AABBCamera(int x, int y, int height, int width) {
		super(x,y);
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

}
