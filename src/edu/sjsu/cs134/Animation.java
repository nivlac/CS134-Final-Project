package edu.sjsu.cs134;

import java.util.Map;

public class Animation {
	AnimationFrames[] animations;
	int currentFrame;
	private float timeRemaining;
	private boolean finished;
	
	public Animation(AnimationFrames[] f) {
		animations = f;
		currentFrame = 0;
		timeRemaining = animations[0].spriteActiveTime;
		finished = false;
	}
	
	public float getTimeRemaining() {
		return timeRemaining;
	}

	public void updateSprite(float deltaTime) {
		timeRemaining -= deltaTime;
		if (timeRemaining <= 0) {
			currentFrame++;
			if (currentFrame > animations.length - 1) {
				finished = true;
				currentFrame = 0;
			}
			timeRemaining = animations[currentFrame].spriteActiveTime;
		}
	}
	
	public int getCurrentFrame() {
		return animations[currentFrame].getSprite();
	}
	
	public void setCurrentFrame(int frame) {
		currentFrame = frame;
		timeRemaining = animations[currentFrame].spriteActiveTime;
	}
	
	public int getFrameNumber() {
		return currentFrame;
	}
	
	public void resetFrames(){
		finished = false;
		currentFrame = 0;
		timeRemaining = animations[0].spriteActiveTime;
	}
	
	public boolean isFinished(){
		return finished;
	}
}
