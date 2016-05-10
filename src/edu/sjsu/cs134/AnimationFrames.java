package edu.sjsu.cs134;

public class AnimationFrames {
	int sprite;
	float spriteActiveTime;
	
	public AnimationFrames(int sprite, float spriteActiveTime) {
		this.sprite = sprite;
		this.spriteActiveTime = spriteActiveTime;
	}

	public int getSprite() {
		return sprite;
	}

	public void setSprite(int sprite) {
		this.sprite = sprite;
	}

	public float getSpriteActiveTime() {
		return spriteActiveTime;
	}

	public void setSpriteActiveTime(float spriteActiveTime) {
		this.spriteActiveTime = spriteActiveTime;
	}
}
