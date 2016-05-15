
package edu.sjsu.cs134;

import com.jogamp.nativewindow.WindowClosingProtocol;
import com.jogamp.opengl.*;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.GLProfile;


import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class JavaFramework {
	// Set this to true to force the game to exit.
	private static boolean shouldExit;

	// The previous frame's keyboard state.
	private static boolean kbPrevState[] = new boolean[256];

	// The current frame's keyboard state.
	private static boolean kbState[] = new boolean[256];

	// Position of the sprites.
	private static int[] spritePos = new int[] { 100, 299 };
	private static int[] enemyPos = new int[] { 400, 300 };

	// Texture for the sprites.
	private static int monkeyTex;
	private static int monkeyStandingTex;
	private static int enemyTex;
	private static int enemyStandingTex;
	private static int enemyShootTex;
	private static int bossTex;
	private static int bossShockwaveTex;
	private static int monkeyProjectileTex;
	private static int snailProjectileTex;
	private static int skyTex;
	private static int groundtex;
	private static int bossSkyTexA;
	private static int bossSkyTexB;
	private static int bossGroundtex;
	

	// Size of the sprite.
	private static int[] spriteSize = new int[2];
	private static int[] projectileSize = new int[2];
	private static int[] bossSize = new int[2];
	private static int[] shockwaveSize = new int[2];

	//Declare backgrounds
	private static Background backgroundMain;
	private static Background backgroundFloor;
	private static Background backgroundBossMainA;
	private static Background backgroundBossMainB;
	private static Background backgroundBossFloor;
	
	//Size of the tiles
	private static int[] tileSize = new int[2];
	private static int backgroundCheck = 0;

	//Initialize Camera
	private static Camera camera = new Camera(0, 0);

	//Size of game window resolution
	private static final int xRes = 800;
	private static final int yRes = 600;
	
	private static int levelWidth = 30;
	private static int levelHeight = 20;

	public static void main(String[] args) {
		GLProfile gl2Profile;

		try {
			// Make sure we have a recent version of OpenGL
			gl2Profile = GLProfile.get(GLProfile.GL2);
		} catch (GLException ex) {
			System.out.println("OpenGL max supported version is too low.");
			System.exit(1);
			return;
		}

		// Create the window and OpenGL context.
		GLWindow window = GLWindow.create(new GLCapabilities(gl2Profile));
		window.setSize(xRes, yRes);
		window.setTitle("Stupid Monkey");
		window.setVisible(true);
		window.setDefaultCloseOperation(WindowClosingProtocol.WindowClosingMode.DISPOSE_ON_CLOSE);
		window.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent keyEvent) {
				if (keyEvent.isAutoRepeat()) {
					return;
				}
				kbState[keyEvent.getKeyCode()] = true;
			}

			@Override
			public void keyReleased(KeyEvent keyEvent) {
				if (keyEvent.isAutoRepeat()) {
					return;
				}
				kbState[keyEvent.getKeyCode()] = false;
			}
		});

		// Setup OpenGL state.
		window.getContext().makeCurrent();
		GL2 gl = window.getGL().getGL2();
		gl.glViewport(0, 0, xRes, yRes);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glOrtho(0, xRes, yRes, 0, 0, 100);
		gl.glEnable(GL2.GL_TEXTURE_2D);
		gl.glEnable(GL2.GL_BLEND);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

		// Load the texture.
		monkeyTex = glTexImageTGAFile(gl, "assets/monkey/monkey_stand.tga", spriteSize);
		monkeyStandingTex = glTexImageTGAFile(gl, "assets/monkey/monkey_stand.tga", spriteSize);

		enemyTex = glTexImageTGAFile(gl, "assets/snail/snail_stand.tga", spriteSize);
		enemyStandingTex = glTexImageTGAFile(gl, "assets/snail/snail_stand.tga", spriteSize);
		enemyShootTex = glTexImageTGAFile(gl, "assets/snail/snail_shoot.tga", spriteSize);
		
		bossTex = glTexImageTGAFile(gl, "assets/boss/bossstand.tga", bossSize);

		monkeyProjectileTex = glTexImageTGAFile(gl, "assets/projectiles/projectile_3.tga", projectileSize);
		snailProjectileTex = glTexImageTGAFile(gl, "assets/projectiles/enemy_projectile.tga", projectileSize);
		bossShockwaveTex = glTexImageTGAFile(gl, "assets/projectiles/shockwave.tga", shockwaveSize);

		/**
		 * Putting textures into an Animation Frames array. These arrays are then put into an animation object which
		 * is called when wanting to retrieve animations.
		 */
		AnimationFrames[] running = {
				new AnimationFrames(glTexImageTGAFile(gl, "assets/monkey/monkey_run1.tga", spriteSize), (float) 110),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/monkey/monkey_run2.tga", spriteSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/monkey/monkey_run3.tga", spriteSize), (float) 120),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/monkey/monkey_run4.tga", spriteSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/monkey/monkey_run5.tga", spriteSize), (float) 110),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/monkey/monkey_run6.tga", spriteSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/monkey/monkey_run7.tga", spriteSize), (float) 120),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/monkey/monkey_run8.tga", spriteSize), (float) 100) };

		AnimationFrames[] monkeyShoot = {
				new AnimationFrames(glTexImageTGAFile(gl, "assets/monkey/monkey_shoot1.tga", spriteSize), (float) 130),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/monkey/monkey_shoot2.tga", spriteSize), (float) 80),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/monkey/monkey_shoot3.tga", spriteSize), (float) 150),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/monkey/monkey_shoot4.tga", spriteSize), (float) 80) };
		
		AnimationFrames[] monkeyJump = {
				new AnimationFrames(glTexImageTGAFile(gl, "assets/monkey/monkey_jump1.tga", spriteSize), (float) 50),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/monkey/monkey_jump2.tga", spriteSize), (float) 75),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/monkey/monkey_jump3.tga", spriteSize), (float) 1500),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/monkey/monkey_jump4.tga", spriteSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/monkey/monkey_jump4.tga", spriteSize), (float) 70) };
		
		AnimationFrames[] monkeyHurt = {
				new AnimationFrames(glTexImageTGAFile(gl, "assets/monkey/hurt1.tga", spriteSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/monkey/hurt2.tga", spriteSize), (float) 140),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/monkey/hurt3.tga", spriteSize), (float) 110)};

		AnimationFrames[] snailMove = {
				new AnimationFrames(glTexImageTGAFile(gl, "assets/snail/snail_run1.tga", spriteSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/snail/snail_run2.tga", spriteSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/snail/snail_run3.tga", spriteSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/snail/snail_run4.tga", spriteSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/snail/snail_run5.tga", spriteSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/snail/snail_run6.tga", spriteSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/snail/snail_run7.tga", spriteSize), (float) 100) };
		
		AnimationFrames[] bossWalk = {
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bosswalk1.tga", bossSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bosswalk2.tga", bossSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bosswalk3.tga", bossSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bosswalk4.tga", bossSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bosswalk5.tga", bossSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bosswalk6.tga", bossSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bosswalk7.tga", bossSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bosswalk8.tga", bossSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bosswalk9.tga", bossSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bosswalk10.tga", bossSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bosswalk11.tga", bossSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bosswalk12.tga", bossSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bosswalk13.tga", bossSize), (float) 100)};
		
		AnimationFrames[] bossJump = {
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bossjump1.tga", bossSize), (float) 50),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bossjump2.tga", bossSize), (float) 70),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bossjump3.tga", bossSize), (float) 80),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bossjump4.tga", bossSize), (float) 90),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bossjump5.tga", bossSize), (float) 400),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bossjump6.tga", bossSize), (float) 1500),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bossjump7.tga", bossSize), (float) 200),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bossjump8.tga", bossSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bossjump9.tga", bossSize), (float) 100)};
		
		AnimationFrames[] bossTaunt = {
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bosstaunt1.tga", bossSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bosstaunt2.tga", bossSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bosstaunt3.tga", bossSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bosstaunt4.tga", bossSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bosstaunt5.tga", bossSize), (float) 800)};

		//Animations
		Animation runAnimation = new Animation(running);
		Animation monkeyJumpAnimation = new Animation(monkeyJump);
		Animation monkeyShootAnimation = new Animation(monkeyShoot);
		Animation monkeyHurtAnimation = new Animation(monkeyHurt);
		Animation snailMoveAnimation = new Animation(snailMove);
		Animation bossWalkAnimation = new Animation(bossWalk);
		Animation bossJumpAnimation = new Animation(bossJump);
		Animation bossTauntAnimation = new Animation(bossTaunt);


		// Initialize all of the background textures
		skyTex = glTexImageTGAFile(gl, "assets/backgrounds/tileSky.tga", tileSize);
		groundtex = glTexImageTGAFile(gl, "assets/backgrounds/tileGround.tga", tileSize);
		bossSkyTexA = glTexImageTGAFile(gl, "assets/backgrounds/cavewall1.tga", tileSize);
		bossSkyTexB = glTexImageTGAFile(gl, "assets/backgrounds/cavewall2.tga", tileSize);
		bossGroundtex = glTexImageTGAFile(gl, "assets/backgrounds/cavefloor.tga", tileSize);
		
		backgroundMain = new Background(skyTex, true, 0, 180, 30, 20);
		backgroundFloor = new Background(groundtex, false, 150, 180, 30, 20);
		
		backgroundBossMainA = new Background(bossSkyTexA, true, 0, 50, 10, 10);
		backgroundBossMainB = new Background(bossSkyTexB, true, 0, 50, 10, 10);
		backgroundBossFloor = new Background(bossGroundtex, false, 50, 60, 10, 10);
		

		

		//Initialize characters in the game
		Character monkey = new Character(spritePos[0], spritePos[1], spriteSize[0], spriteSize[1], monkeyTex);
		Character snail = new Character(enemyPos[0], enemyPos[1], spriteSize[0], spriteSize[1], enemyTex);
		Boss boss = new Boss(800, 299, bossSize[0], bossSize[1], bossTex);

		//Create a bounding box camera for both the monkey and camera
		AABBCamera spriteAABB = new AABBCamera(spritePos[0], spritePos[1], spriteSize[0], spriteSize[1]);
		AABBCamera cameraAABB = new AABBCamera(camera.getX(), camera.getY(), xRes, yRes);

		//Frames
		long lastFrame;
		long currentFrame = System.nanoTime();

		int lastPhysicsFrameMs = 0;
		long curFrameMs;
		int physicsDeltaMs = 10;
		
		//Timer for standing for when to have standing texture
		int standCount = 0;
		int bossSlamCount = 0;
		//Timer so the player cant spam shooting
		int shootTimer = 0;
		//Previous positions of the monkey so it can't move through objects in a collision
		int monkeyPreviousX = monkey.getX();
		int monkeyPreviousY = monkey.getY();
		int bossPreviousY = boss.getY();
		boolean bossMode = true;
		if(bossMode == true){
			levelWidth = backgroundBossMainA.getWidth();
			levelHeight = backgroundBossMainA.getHeight();
			backgroundFloor = backgroundBossFloor;
		}

		//Arraylist of enemies since there will be multiple
		ArrayList<Character> enemies = new ArrayList<Character>();
		enemies.add(snail);
		enemies.add(boss);
		snail.setVisible(false);
		// The game loop
		while (!shouldExit) {
			lastFrame = currentFrame;
			System.arraycopy(kbState, 0, kbPrevState, 0, kbState.length);

			// Actually, this runs the entire OS message pump.
			window.display();
			if (!window.isVisible()) {
				shouldExit = true;
				break;
			}
			
			currentFrame = System.nanoTime();
			curFrameMs = currentFrame / 1000000;
			long delta = (currentFrame - lastFrame) / 1000000;
			
			/**
			 * Code for having gravity affect the monkey. For some reason it works best in
			 * this spot right before the physics loop. Will play jumping animation if monkey is jumping.
			 */
			if(monkey.isJumping()){
				monkeyJumpAnimation.updateSprite(delta);
				monkey.setCurrentTexture(monkeyJumpAnimation.getCurrentFrame());
				monkey.setY((int)(monkey.getY() + monkey.getyVelocity()));
				monkey.setyVelocity(monkey.getyVelocity() + monkey.getAcceleration());
			}
			else{
				monkey.setY((int)(monkey.getY() + monkey.getyVelocity()));
				monkey.setyVelocity(monkey.getyVelocity() + monkey.getAcceleration());
			}
			boss.setY((int)(boss.getY() + boss.getyVelocity()));
			boss.setyVelocity(boss.getyVelocity() + boss.getAcceleration());
			
			//The physics loop
			do {
				/**
				 * Check to see if a projectile of the monkey's is hitting an enemy
				 * If it is, we remove the projectile and subtract health from the enemy.
				 */
				for (int i = 0; i < monkey.getProjectiles().size(); i++) {
					ArrayList<Projectile> mainCharProj = (ArrayList<Projectile>) monkey.getProjectiles();
					Projectile p = mainCharProj.get(i);
					p.update();
					AABBCamera projectile = mainCharProj.get(i).getCollisionBox();
					for (Character c : enemies) {
						if (AABBIntersect(projectile, c.getHitbox())) {
							mainCharProj.get(i).setVisible(false);
							mainCharProj.remove(i);
							c.setHealth(c.getHealth() - 1);
							if (c.getHealth() <= 0) {
								c.setVisible(false);
							}
						}
					}
				}
				/**
				 * Similar to the loop above but for enemy projectiles. If the monkey is hit, the monkey's is hit
				 * property is triggered.
				 */
				for (Character c : enemies) {
					for (int i = 0; i < c.getProjectiles().size(); i++) {
						ArrayList<Projectile> enemyProjectiles = (ArrayList<Projectile>) c.getProjectiles();
						Projectile p = enemyProjectiles.get(i);
						p.update();
						AABBCamera projectile = enemyProjectiles.get(i).getCollisionBox();
							if (AABBIntersect(projectile, monkey.getHitbox())) {
								enemyProjectiles.get(i).setVisible(false);
								enemyProjectiles.remove(i);
								monkey.setHit(!monkey.isHit());
							}
						}
					}
				
				//Code to determine how many tiles are in our current screen
				int startX = camera.getX() / tileSize[0];
				int endX = (camera.getX() + xRes) / tileSize[0];
				int startY = camera.getY() / tileSize[0];
				int endY = (camera.getY() + yRes) / tileSize[1];
				
				/**
				 * Checks for collisions with the monkey and the floor tile. We add one to endX to account for
				 * tiles on the edge not seen from our previous initialization. If the monkey is colliding,
				 * we stop the monkey from jumping, set his velocity to 0, and return him to his previous position.
				 */
				for (int i = startX; i < endX + 1; i++) {
					for (int j = startY; j < endY + 1; j++) {
						
						if (backgroundFloor.getTile(i, j) != null) {
							AABBCamera tileAABB = new AABBCamera(i * tileSize[0], j * tileSize[1], 100, 100);
							boolean collMonkey = AABBIntersect(monkey.getHitbox(), tileAABB);
							if (collMonkey) {
								monkey.setJumping(false);
								monkey.setyVelocity(0);
								monkeyJumpAnimation.resetFrames();
								//monkey.setX(monkeyPreviousX);
								monkey.setY(monkeyPreviousY);
							}
							boolean collBoss = AABBIntersect(boss.getHitbox(), tileAABB);
							if (collBoss) {
								boss.setJumping(false);
								boss.setyVelocity(0);
								bossJumpAnimation.resetFrames();
								//monkey.setX(monkeyPreviousX);
								boss.setY(bossPreviousY);
							}
						}
					}
				}
				
				
				lastPhysicsFrameMs += physicsDeltaMs;
			} while (lastPhysicsFrameMs + physicsDeltaMs < curFrameMs);
			
			//Set the monkey previous coordinates after physics have run.
			monkeyPreviousX = monkey.getX();
			monkeyPreviousY = monkey.getY();
			bossPreviousY = boss.getY();
			
			//If an enemy is not visible, then remove it from the enemy arraylist
			for (int i = 0; i < enemies.size(); i++) {
				if (!enemies.get(i).getVisible()) {
					enemies.remove(i);
				}
			}


			// Enemy Movement
			shootTimer++;
			/**
			 * Snail movement. Tracks the monkey's position
			 */
			if (snail.getX() < monkey.getX() && !snail.isShooting()) {
				snail.setX(snail.getX() + 1);
				snail.setReverse(true);
				snailMoveAnimation.updateSprite(delta);
				snail.setCurrentTexture(snailMoveAnimation.getCurrentFrame());
			} else if (snail.getX() > monkey.getX() && !snail.isShooting()) {
				snail.setX(snail.getX() - 1);
				snail.setReverse(false);
				snailMoveAnimation.updateSprite(delta);
				snail.setCurrentTexture(snailMoveAnimation.getCurrentFrame());
			} else if(!snail.isShooting()) {
				snail.setCurrentTexture(enemyStandingTex);
				snailMoveAnimation.resetFrames();
			}
			if(shootTimer == 100 && snail.getVisible()){
				snail.addProjectile(new Projectile(snail.getX() + 50, snail.getY() + 30, projectileSize[0],
						projectileSize[1], !snail.getReverse()));
				snail.setShooting(true);
				snail.setCurrentTexture(enemyShootTex);
			}
			if(shootTimer == 130 && snail.getVisible()){
				snail.setShooting(false);
				snail.setCurrentTexture(enemyStandingTex);
				shootTimer = 0;
			}
			
			/**
			 * 
			 * BOSS AI CODE
			 * 
			 * 
			 */
			
			if(boss.getAttackMode() == 0){
				boss.setTargetX(400);
				boss.setTargetAcquired(true);
				if(boss.getTargetX() != boss.getX()){
					if(boss.getTargetX() < boss.getX()){
						boss.setReverse(false);
						boss.setX(boss.getX() - 2);
					}
					else if(boss.getTargetX() > boss.getX()){
						boss.setReverse(true);
						boss.setX(boss.getX() + 2);
					}
					bossWalkAnimation.updateSprite(delta);
					boss.setCurrentTexture(bossWalkAnimation.getCurrentFrame());
				}
				else{
					boss.setTargetAcquired(false);
					boss.setAttackMode(1);
					bossWalkAnimation.resetFrames();
				}
			}
			else if(boss.getAttackMode() == 1){
				if(bossTauntAnimation.isFinished()){
					bossTauntAnimation.resetFrames();
					bossJumpAnimation.resetFrames();
					boss.setAttackMode(2);
				}
				boss.setCurrentTexture(bossTauntAnimation.getCurrentFrame());
				bossTauntAnimation.updateSprite(delta);
			}
			else if(boss.getAttackMode() == 2){
				if(!boss.isTargetAcquired() && !boss.isJumping() && bossJumpAnimation.getFrameNumber() == 0){
					boss.setTargetX(monkey.getX());
					while(boss.getTargetX() % 4 != 0){
						boss.setTargetX(boss.getTargetX() + 1);
					}
					boss.setTargetAcquired(true);
					boss.setyVelocity(-8);
					boss.setJumping(true);
					boss.setCurrentTexture(bossJumpAnimation.getCurrentFrame());
					System.out.println("yes");
				}
				else{
					if(boss.getTargetX() < boss.getX()){
						boss.setReverse(false);
						boss.setX(boss.getX() - 4);
						bossJumpAnimation.updateSprite(delta);
						boss.setCurrentTexture(bossJumpAnimation.getCurrentFrame());
					}
					else if(boss.getTargetX() > boss.getX()){
						boss.setReverse(true);
						boss.setX(boss.getX() + 4);
						bossJumpAnimation.updateSprite(delta);
						boss.setCurrentTexture(bossJumpAnimation.getCurrentFrame());
					}
					else if(!boss.isJumping()){
						if(!boss.isShooting()){
							boss.setShooting(true);
							boss.addProjectile(new Projectile(boss.getX() + 25, boss.getY() + 125, shockwaveSize[0],
									shockwaveSize[1], true));
							boss.addProjectile(new Projectile(boss.getX() + 175, boss.getY() + 125, shockwaveSize[0],
									shockwaveSize[1], false));
						}
						System.out.println(boss.getProjectiles().size());
						bossSlamCount++;
						bossJumpAnimation.setCurrentFrame(7);
						boss.setCurrentTexture(bossJumpAnimation.getCurrentFrame());
						if(bossSlamCount > 100){
							boss.setShooting(false);
						bossSlamCount = 0;
						boss.setTargetAcquired(false);
						boss.setAttackMode(1);
						}
					}
				}
			}
			/**
			 * Code for dealing with monkey shooting.
			 */
			
			if(monkey.isHit()){
				monkeyHurtAnimation.updateSprite(delta);
				monkey.setCurrentTexture(monkeyHurtAnimation.getCurrentFrame());
				if(monkeyHurtAnimation.isFinished()){
					monkeyHurtAnimation.resetFrames();
					monkey.setHit(false);
				}
			}
			
			if (!monkey.isShooting()) {
				if (kbState[KeyEvent.VK_SPACE] && !monkey.isHit()) {
					monkey.addProjectile(new Projectile(monkey.getX() + 20, monkey.getY() + 20, projectileSize[0],
							projectileSize[1], monkey.getReverse()));
					monkey.setShooting(true);
				}
			}
			else{
				monkeyShootAnimation.updateSprite(delta);
				monkey.setCurrentTexture(monkeyShootAnimation.getCurrentFrame());
				if(monkeyShootAnimation.isFinished()){
					monkeyShootAnimation.resetFrames();
					monkey.setShooting(false);
				}
			}
			
			
			/**
			 * If the monkey is not moving or jumping or shooting, then play the standing animation.
			 * The stand count was added for some weird animation bug
			 */
			
			if (kbState[KeyEvent.VK_D] == false && kbState[KeyEvent.VK_A] == false && !monkey.isShooting() && !monkey.isJumping() && !monkey.isHit()) {
				standCount++;
				if (standCount > 6) {
					runAnimation.resetFrames();
					monkey.setCurrentTexture(monkeyStandingTex);
					standCount = 0;
				}
			}

			
			/**
			 * Monkey movement code.
			 */
			if (kbState[KeyEvent.VK_A] && monkey.getX() > 0 && !monkey.isShooting() && !monkey.isHit()) {
				monkey.setX(monkey.getX() - 3);
				if (monkey.getX() < camera.getX() + (xRes / 6)) {
					if (camera.getX() - 3 < 0) {
						camera.setX(camera.getX());
					} else {
						camera.setX(camera.getX() - 3);
					}
				}
				monkey.setReverse(true);
				if(!monkey.isJumping()){
				runAnimation.updateSprite(delta);
				monkey.setCurrentTexture(runAnimation.getCurrentFrame());
				}
			}
			if (kbState[KeyEvent.VK_D] && monkey.getX() < levelWidth * tileSize[0] - spriteSize[0]  && !monkey.isShooting() && !monkey.isHit()) {
				monkey.setX(monkey.getX() + 3);
				if (monkey.getX() > camera.getX() + (xRes - (xRes / 3))) {
					if (camera.getX() + 3 > tileSize[0] * levelWidth - xRes) {
						camera.setX(camera.getX());
					} else {
						camera.setX(camera.getX() + 3);
					}
				}
				monkey.setReverse(false);
				if(!monkey.isJumping()){
				runAnimation.updateSprite(delta);
				monkey.setCurrentTexture(runAnimation.getCurrentFrame());
				}
			}
			
			

			/**
			 * Press W to jump. Change number in set velocity to determine how high monkey jumps.
			 */
			if (kbState[KeyEvent.VK_W] && monkey.getY() > 0 && !monkey.isJumping() && !monkey.isHit()) {
				monkey.setJumping(true);
				monkey.setyVelocity(-7);
			}
			
			
			//Does nothing as of now
			if (kbState[KeyEvent.VK_S] && monkey.getY() < backgroundMain.getHeight() * tileSize[1] - spriteSize[1]) {
				// spritePos[1] += 1;
			}

			
			//Camera controls
			if (kbState[KeyEvent.VK_LEFT]) {
				if (camera.getX() - 1 < 0) {
					camera.setX(camera.getX());
				} else {
					camera.setX(camera.getX() - 1);
				}
			}
			if (kbState[KeyEvent.VK_RIGHT]) {
				if (camera.getX() + 1 > tileSize[0] * backgroundMain.getWidth() - xRes) {
					camera.setX(camera.getX());
				} else {
					camera.setX(camera.getX() + 1);
				}
			}
			// if (kbState[KeyEvent.VK_UP]) {
			// if (camera.getY() - 1 < 0) {
			// camera.setY(camera.getY());
			// } else
			// camera.setY(camera.getY() - 1);
			// }
			// if (kbState[KeyEvent.VK_DOWN]) {
			// if (camera.getY() + 1 > tileSize[1] * background.getHeight() -
			// yRes) {
			// camera.setY(camera.getY());
			// } else
			// camera.setY(camera.getY() + 1);
			// }

			//Update bounding box for monkey and camera
			spriteAABB.setX(monkey.getX());
			spriteAABB.setY(monkey.getY());
			spriteAABB.setWidth(spriteSize[0]);
			spriteAABB.setHeight(spriteSize[1]);

			cameraAABB.setX(camera.getX());
			cameraAABB.setY(camera.getY());
			cameraAABB.setWidth(xRes);
			cameraAABB.setHeight(yRes);
			
			//Check to see what tiles are in bounds of the camera.
			backgroundCheck = backgroundInBounds(camera.getX(), camera.getY());

			gl.glClearColor(0, 0, 0, 1);
			gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

			if(!bossMode){
			//Draw main background. Last two params for glDrawSprite are for reversing image and making image red.
			for (int i = backgroundCheck; i < backgroundMain.getWidth(); i++) {
				for (int j = 0; j < backgroundMain.getHeight(); j++) {
					if (backgroundMain.getTile(i, j) != null) {
						glDrawSprite(gl, backgroundMain.getTile(i, j).getImage(), i * tileSize[0] - camera.getX(),
								j * tileSize[1] - camera.getY(), tileSize[0], tileSize[1], false, false);
					}
				}
			}
			//Draw floor
			for (int i = backgroundCheck; i < backgroundFloor.getWidth(); i++) {
				for (int j = 0; j < backgroundFloor.getHeight(); j++) {
					if (backgroundFloor.getTile(i, j) != null) {
						glDrawSprite(gl, backgroundFloor.getTile(i, j).getImage(), i * tileSize[0] - camera.getX(),
								j * tileSize[1] - camera.getY(), tileSize[0], tileSize[1], false, false);
					}
				}
			}
			}
			else{
				for (int i = backgroundCheck; i < backgroundBossMainB.getWidth(); i++) {
					for (int j = 0; j < backgroundBossMainB.getHeight(); j++) {
						if (backgroundBossMainB.getTile(i, j) != null) {
							glDrawSprite(gl, backgroundBossMainB.getTile(i, j).getImage(), i * tileSize[0] - camera.getX(),
									j * tileSize[1] - camera.getY(), tileSize[0], tileSize[1], false, false);
						}
					}
				}
				//Draw floor
				for (int i = backgroundCheck; i < backgroundBossFloor.getWidth(); i++) {
					for (int j = 0; j < backgroundBossFloor.getHeight(); j++) {
						if (backgroundBossFloor.getTile(i, j) != null) {
							glDrawSprite(gl, backgroundBossFloor.getTile(i, j).getImage(), i * tileSize[0] - camera.getX(),
									j * tileSize[1] - camera.getY(), tileSize[0], tileSize[1], false, false);
						}
					}
				}
			}
			
			//Draw all enemies inside main camera
			for (Character c : enemies) {
				if (c.getVisible()) {
					if (AABBIntersect(cameraAABB, c.getHitbox())) {
						glDrawSprite(gl, c.getCurrentTexture(), c.getX() - camera.getX(), c.getY() - camera.getY(), c.getWidth(),
								c.getHeight(), c.getReverse(), false);
					}
				}
			}
			
			//Draw monkey projectiles
			ArrayList<Projectile> monkeyProjectiles = (ArrayList<Projectile>) monkey.getProjectiles();
			for (int i = 0; i < monkeyProjectiles.size(); i++) {
				Projectile p = monkeyProjectiles.get(i);
				glDrawSprite(gl, monkeyProjectileTex, p.getX() - camera.getX(), p.getY() - camera.getY(), projectileSize[0],
						projectileSize[1], false, false);
			}
			
			//Draw snail projectiles
			ArrayList<Projectile> snailProjectiles = (ArrayList<Projectile>) snail.getProjectiles();
			for (int i = 0; i < snailProjectiles.size(); i++) {
				Projectile p = snailProjectiles.get(i);
				glDrawSprite(gl, snailProjectileTex, p.getX() - camera.getX(), p.getY() - camera.getY(), projectileSize[0],
						projectileSize[1], false, false);
			}
			
			//Draw boss projectiles
			ArrayList<Projectile> bossProjectiles = (ArrayList<Projectile>) boss.getProjectiles();
			for (int i = 0; i < bossProjectiles.size(); i++) {
				Projectile p = bossProjectiles.get(i);
				glDrawSprite(gl, bossShockwaveTex, p.getX() - camera.getX(), p.getY() - camera.getY(), shockwaveSize[0],
						shockwaveSize[1], !p.isReverse(), false);
			}
			
			//If the camera and the monkey are within each other, then draw monkey
			if (AABBIntersect(cameraAABB, spriteAABB)) {
				glDrawSprite(gl, monkey.getCurrentTexture(), monkey.getX() - camera.getX(), monkey.getY() - camera.getY(), monkey.getWidth(),
						monkey.getHeight(), monkey.getReverse(), monkey.isHit());
			}
			
			
			if (kbState[KeyEvent.VK_ESCAPE]) {
				shouldExit = true;
			}
		}
		System.exit(0);
	}

	public static boolean AABBIntersect(AABBCamera box1, AABBCamera box2) {
		// box1 to the right
		if (box1.getX() > box2.getX() + box2.getWidth()) {
			return false;
		}
		// box1 to the left
		if (box1.getX() + box1.getWidth() < box2.getX()) {
			return false;
		}
		// box1 below
		if (box1.getY() > box2.getY() + box2.getHeight()) {
			return false;
		}
		// box1 above
		if (box1.getY() + box1.getHeight() < box2.getY()) {
			return false;
		}
		return true;
	}

	public static int backgroundInBounds(int x, int y) {

		return (y / tileSize[1]) * levelHeight + (x / tileSize[0]);
	}

	// Load a file into an OpenGL texture and return that texture.
	public static int glTexImageTGAFile(GL2 gl, String filename, int[] out_size) {
		final int BPP = 4;

		DataInputStream file = null;
		try {
			// Open the file.
			file = new DataInputStream(new FileInputStream(filename));
		} catch (FileNotFoundException ex) {
			System.err.format("File: %s -- Could not open for reading.", filename);
			return 0;
		}

		try {
			// Skip first two bytes of data we don't need.
			file.skipBytes(2);

			// Read in the image type. For our purposes the image type
			// should be either a 2 or a 3.
			int imageTypeCode = file.readByte();
			if (imageTypeCode != 2 && imageTypeCode != 3) {
				file.close();
				System.err.format("File: %s -- Unsupported TGA type: %d", filename, imageTypeCode);
				return 0;
			}

			// Skip 9 bytes of data we don't need.
			file.skipBytes(9);

			int imageWidth = Short.reverseBytes(file.readShort());
			int imageHeight = Short.reverseBytes(file.readShort());
			int bitCount = file.readByte();
			file.skipBytes(1);

			// Allocate space for the image data and read it in.
			byte[] bytes = new byte[imageWidth * imageHeight * BPP];

			// Read in data.
			if (bitCount == 32) {
				for (int it = 0; it < imageWidth * imageHeight; ++it) {
					bytes[it * BPP + 0] = file.readByte();
					bytes[it * BPP + 1] = file.readByte();
					bytes[it * BPP + 2] = file.readByte();
					bytes[it * BPP + 3] = file.readByte();
				}
			} else {
				for (int it = 0; it < imageWidth * imageHeight; ++it) {
					bytes[it * BPP + 0] = file.readByte();
					bytes[it * BPP + 1] = file.readByte();
					bytes[it * BPP + 2] = file.readByte();
					bytes[it * BPP + 3] = -1;
				}
			}

			file.close();

			// Load into OpenGL
			int[] texArray = new int[1];
			gl.glGenTextures(1, texArray, 0);
			int tex = texArray[0];
			gl.glBindTexture(GL2.GL_TEXTURE_2D, tex);
			gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, GL2.GL_RGBA, imageWidth, imageHeight, 0, GL2.GL_BGRA,
					GL2.GL_UNSIGNED_BYTE, ByteBuffer.wrap(bytes));
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);

			out_size[0] = imageWidth;
			out_size[1] = imageHeight;
			return tex;
		} catch (IOException ex) {
			System.err.format("File: %s -- Unexpected end of file.", filename);
			return 0;
		}
	}

	public static void glDrawSprite(GL2 gl, int tex, int x, int y, int w, int h, boolean reverse, boolean isRed) {
		gl.glBindTexture(GL2.GL_TEXTURE_2D, tex);
		gl.glBegin(GL2.GL_QUADS);
		{
			if(isRed){
				gl.glColor3f((float)1.0, (float)0.0, (float)0.0);
			}
			else{
				gl.glColor3ub((byte) -1, (byte) -1, (byte) -1);
			}
			if (reverse) {
				gl.glTexCoord2f(1, 1);
				gl.glVertex2i(x, y);
				gl.glTexCoord2f(0, 1);
				gl.glVertex2i(x + w, y);
				gl.glTexCoord2f(0, 0);
				gl.glVertex2i(x + w, y + h);
				gl.glTexCoord2f(1, 0);
				gl.glVertex2i(x, y + h);
			} else {
				gl.glTexCoord2f(0, 1);
				gl.glVertex2i(x, y);
				gl.glTexCoord2f(1, 1);
				gl.glVertex2i(x + w, y);
				gl.glTexCoord2f(1, 0);
				gl.glVertex2i(x + w, y + h);
				gl.glTexCoord2f(0, 0);
				gl.glVertex2i(x, y + h);
			}
			

		}
		gl.glEnd();
	}
}
