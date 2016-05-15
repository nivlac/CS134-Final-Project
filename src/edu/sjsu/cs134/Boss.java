package edu.sjsu.cs134;

public class Boss extends Character {

	private int attackMode;
	private int targetX;
	private boolean targetAcquired;
	public Boss(int x, int y, int width, int height, int tex) {
		super(x, y, width, height, tex);
		attackMode = 0;
		targetX = x;
		targetAcquired = false;
		setAcceleration(.15);
	}
	public int getAttackMode() {
		return attackMode;
	}
	public void setAttackMode(int attackMode) {
		this.attackMode = attackMode;
	}
	public int getTargetX() {
		return targetX;
	}
	public void setTargetX(int targetX) {
		this.targetX = targetX;
	}
	public boolean isTargetAcquired() {
		return targetAcquired;
	}
	public void setTargetAcquired(boolean targetAcquired) {
		this.targetAcquired = targetAcquired;
	}

}
