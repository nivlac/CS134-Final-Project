package edu.sjsu.cs134;

public class Snail extends Character{

	private int shootTimer;
	private Animation snailMoveAnimation;
	private Animation snailDieAnimation;
	public Snail(int x, int y, int width, int height, int tex, Animation move, Animation die) {
		super(x, y, width, height, tex);
		this.setShootTimer(0);
		snailMoveAnimation = move;
		snailDieAnimation = die;
	}
	public int getShootTimer() {
		return shootTimer;
	}
	public void setShootTimer(int shootTimer) {
		this.shootTimer = shootTimer;
	}
	public Animation getSnailMoveAnimation() {
		return snailMoveAnimation;
	}
	public void setSnailMoveAnimation(Animation snailMoveAnimation) {
		this.snailMoveAnimation = snailMoveAnimation;
	}
	public Animation getSnailDieAnimation() {
		return snailDieAnimation;
	}
	public void setSnailDieAnimation(Animation snailDieAnimation) {
		this.snailDieAnimation = snailDieAnimation;
	}

}
