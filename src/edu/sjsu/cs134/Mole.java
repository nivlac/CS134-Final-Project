package edu.sjsu.cs134;

public class Mole extends Character {

	private Animation moleBuryAnimation, molePopupAnimation, moleShockAnimation, moleDieAnimation;
	private boolean targetAcquired;
	private int popupTimer;
	public Mole(int x, int y, int width, int height, int tex, Animation bury, Animation popup, Animation shock, Animation die) {
		super(x, y, width, height, tex);
		moleBuryAnimation = bury;
		molePopupAnimation = popup;
		moleShockAnimation = shock;
		moleDieAnimation = die;
		targetAcquired = false;
		this.getHitbox().setY(this.getY() + 35);
		this.getHitbox().setHeight(this.getHitbox().getHeight() - 35);
		popupTimer = 0;
	}
	public Animation getMoleBuryAnimation() {
		return moleBuryAnimation;
	}
	public void setMoleBuryAnimation(Animation moleBuryAnimation) {
		this.moleBuryAnimation = moleBuryAnimation;
	}
	public Animation getMolePopupAnimation() {
		return molePopupAnimation;
	}
	public void setMolePopupAnimation(Animation molePopupAnimation) {
		this.molePopupAnimation = molePopupAnimation;
	}
	public Animation getMoleShockAnimation() {
		return moleShockAnimation;
	}
	public void setMoleShockAnimation(Animation moleShockAnimation) {
		this.moleShockAnimation = moleShockAnimation;
	}
	public boolean isTargetAcquired() {
		return targetAcquired;
	}
	public void setTargetAcquired(boolean targetAcquired) {
		this.targetAcquired = targetAcquired;
	}
	public Animation getMoleDieAnimation() {
		return moleDieAnimation;
	}
	public void setMoleDieAnimation(Animation moleDieAnimation) {
		this.moleDieAnimation = moleDieAnimation;
	}
	public int getPopupTimer() {
		return popupTimer;
	}
	public void setPopupTimer(int popupTimer) {
		this.popupTimer = popupTimer;
	}

}
