package edu.sjsu.cs134;

public class Seagull extends Character{

	Animation seagullMoveAnimation;
	Animation seagullDieAnimation;
	private int targetX, targetY, attackTimer;
	private boolean targetAcquired;
	
	public Seagull(int x, int y, int width, int height, int tex, Animation move, Animation die) {
		super(x, y, width, height, tex);
		seagullMoveAnimation = move;
		seagullDieAnimation = die;
		setTargetX(0);
		setTargetY(0);
		targetAcquired = false;
		attackTimer = 300;
	}
	public Animation getSeagullDieAnimation() {
		return seagullDieAnimation;
	}
	public void setSeagullDieAnimation(Animation seagullDieAnimation) {
		this.seagullDieAnimation = seagullDieAnimation;
	}
	public Animation getSeagullMoveAnimation() {
		return seagullMoveAnimation;
	}
	public void setSeagullMoveAnimation(Animation seagullMoveAnimation) {
		this.seagullMoveAnimation = seagullMoveAnimation;
	}
	public int getTargetX() {
		return targetX;
	}
	public void setTargetX(int targetX) {
		this.targetX = targetX;
	}
	public int getTargetY() {
		return targetY;
	}
	public void setTargetY(int targetY) {
		this.targetY = targetY;
	}
	public boolean isTargetAcquired() {
		return targetAcquired;
	}
	public void setTargetAcquired(boolean targetAcquired) {
		this.targetAcquired = targetAcquired;
	}
	public int getAttackTimer() {
		return attackTimer;
	}
	public void setAttackTimer(int attackTimer) {
		this.attackTimer = attackTimer;
	}

}
