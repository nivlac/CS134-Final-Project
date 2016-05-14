package edu.sjsu.cs134;

import java.util.ArrayList;
import java.util.List;



public class Character {
	private int x;
	private int y;
	private int health;
	private double yVelocity;
	private double acceleration;
	private List<Projectile> projectiles;
	AABBCamera hitbox;
	private boolean reverse, visible, isShooting, isHit, isJumping;
	private int currentTexture;
	
	public Character(int x, int y, int width, int height, int tex) {
		this.x = x;
		this.y = y;
		this.health = 3;
		visible = true;
		projectiles = new ArrayList<Projectile>();
		hitbox = new AABBCamera(x, y, width, height);
		reverse = false;
		isShooting = false;
		isHit = false;
		acceleration = .2;
		yVelocity = 0;
		currentTexture = tex;
	}
	
	public double getyVelocity() {
		return yVelocity;
	}

	public void setyVelocity(double yVelocity) {
		this.yVelocity = yVelocity;
	}

	public boolean isJumping() {
		return isJumping;
	}

	public void setJumping(boolean isJumping) {
		this.isJumping = isJumping;
	}

	public double getAcceleration() {
		return acceleration;
	}

	public boolean isShooting() {
		return isShooting;
	}

	public void setShooting(boolean isShooting) {
		this.isShooting = isShooting;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
		hitbox.setX(x);
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
		hitbox.setY(y);
	}
	
	public List<Projectile> getProjectiles() {
		return projectiles;
	}

	public void addProjectile(Projectile p) {
		projectiles.add(p);
	}
	
	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}
	
	public AABBCamera getHitbox() {
		return hitbox;
	}
	
	public boolean getReverse() {
		return reverse;
	}

	public void setReverse(boolean bool) {
		reverse = bool;
	}
	
	public void setVisible(boolean bool){
		visible = bool;
	}
	
	public boolean getVisible() {
		return visible;
	}

	public boolean isHit() {
		return isHit;
	}

	public void setHit(boolean isHit) {
		this.isHit = isHit;
	}

	public int getCurrentTexture() {
		return currentTexture;
	}

	public void setCurrentTexture(int currentTexture) {
		this.currentTexture = currentTexture;
	}
	
	
	
}
