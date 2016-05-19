package edu.sjsu.cs134;

public class Platform {
	private int x;
	private int y;
	private int length;
	private AABBCamera collisionBox;
	
	public Platform(int x, int y, int length) {
		this.setX(x);
		this.setY(y);
		this.length = length;
		this.collisionBox = new AABBCamera(x, y, 50, (length * 100) - 20);
	}

	public AABBCamera getCollisionBox() {
		return collisionBox;
	}

	public void setCollisionBox(AABBCamera collisionBox) {
		this.collisionBox = collisionBox;
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

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
}
