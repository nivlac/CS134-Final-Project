package edu.sjsu.cs134;


public class Projectile {
	private int x;
	private int y;
	private int width, height;
	private int speed;
	private int distance;
	private double gravity;
	private boolean visible;
	private AABBCamera collisionBox;
	private boolean reverse, isRock;
	
	public Projectile(int x, int y, int width, int height, boolean isLeft) {
		this.width = width;
		this.height = height;
		reverse = isLeft;
		if(!isLeft){
			this.speed = 3;
			this.x = x;
			this.y = y;
		}
		else{
			this.speed = -3;
			this.x = x - width;
			this.y = y;
		}
		this.gravity = 1;
		this.distance = 0;
		collisionBox = new AABBCamera(x, y, width, height);
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

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}
	
	public AABBCamera getCollisionBox() {
		return collisionBox;
	}
	
	public void setVisible(boolean bool){
		visible = bool;
	}
	
	public boolean getVisible() {
		return visible;
	}

	public void update() {
		if(this.isRock()){
			y += speed;
			distance += speed;
			collisionBox.setY(y);
		}
		else{
		x += speed;
		distance += speed;
		collisionBox.setX(x);
		}
	}

	public boolean isReverse() {
		return reverse;
	}

	public void setReverse(boolean reverse) {
		this.reverse = reverse;
	}

	public boolean isRock() {
		return isRock;
	}

	public void setRock(boolean isRock) {
		this.isRock = isRock;
	}
}
