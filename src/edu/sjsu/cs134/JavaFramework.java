
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
import java.util.Random;

import javax.sound.sampled.Clip;

public class JavaFramework {
	// Set this to true to force the game to exit.
	private static boolean shouldExit;

	// The previous frame's keyboard state.
	private static boolean kbPrevState[] = new boolean[256];

	// The current frame's keyboard state.
	private static boolean kbState[] = new boolean[256];

	// Position of the sprites.
	private static int[] spritePos = new int[] { 200, 299 };
	private static int[] enemyPos = new int[] { 400, 400 };

	// Texture for the sprites.
	private static int monkeyTex;
	private static int monkeyStandingTex;
	private static int enemyTex;
	private static int enemyStandingTex;
	private static int enemyShootTex;
	private static int gullAttackTex;
	private static int bossTex;
	private static int bossShockwaveTex;
	private static int rockTex;
	private static int monkeyProjectileTex;
	private static int snailProjectileTex;
	private static int skyTex;
	private static int groundtex;
	private static int bossSkyTexA;
	private static int bossSkyTexB;
	private static int bossGroundtex;
	private static int platformA;
	private static int platformB;
	private static int platformC;
	private static int titleScreen;
	private static int healthTex;

	// Size of the sprite.
	private static int[] spriteSize = new int[2];
	private static int[] projectileSize = new int[2];
	private static int[] bossSize = new int[2];
	private static int[] shockwaveSize = new int[2];
	private static int[] healthSize = new int[2];

	// Declare backgrounds
	private static Background backgroundMain;
	private static Background backgroundFloor;
	private static Background backgroundBossMainA;
	private static Background backgroundBossMainB;
	private static Background backgroundBossFloor;

	// Size of the tiles
	private static int[] tileSize = new int[2];
	private static int[] menuSize = new int[2];
	private static int backgroundCheck = 0;

	// Initialize Camera
	private static Camera camera = new Camera(0, 0);

	// Size of game window resolution
	private static final int xRes = 800;
	private static final int yRes = 600;

	private static int levelWidth = 30;
	private static int levelHeight = 20;

	public static void main(String[] args) {
		GLProfile gl2Profile;
		boolean bossMode = false;
		boolean bossInit = false;
		boolean titleMode = true;

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
		window.setTitle("Final Project");
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

		enemyTex = glTexImageTGAFile(gl, "assets/snail/snailstand.tga", spriteSize);
		enemyStandingTex = glTexImageTGAFile(gl, "assets/snail/snailstand.tga", spriteSize);
		enemyShootTex = glTexImageTGAFile(gl, "assets/snail/snailshoot.tga", spriteSize);

		bossTex = glTexImageTGAFile(gl, "assets/boss/bossstand.tga", bossSize);
		gullAttackTex = glTexImageTGAFile(gl, "assets/seagull/gullattack.tga", spriteSize);
		

		monkeyProjectileTex = glTexImageTGAFile(gl, "assets/projectiles/projectile_3.tga", projectileSize);
		snailProjectileTex = glTexImageTGAFile(gl, "assets/projectiles/enemy_projectile.tga", projectileSize);
		bossShockwaveTex = glTexImageTGAFile(gl, "assets/projectiles/shockwave.tga", shockwaveSize);
		rockTex = glTexImageTGAFile(gl, "assets/projectiles/rock.tga", spriteSize);

		/**
		 * Putting textures into an Animation Frames array. These arrays are
		 * then put into an animation object which is called when wanting to
		 * retrieve animations.
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

		AnimationFrames[] monkeyPunch = {
				new AnimationFrames(glTexImageTGAFile(gl, "assets/monkey/punch1.tga", spriteSize), (float) 200),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/monkey/punch2.tga", spriteSize), (float) 180),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/monkey/punch3.tga", spriteSize), (float) 110),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/monkey/punch4.tga", spriteSize), (float) 90) };

		AnimationFrames[] monkeyHurt = {
				new AnimationFrames(glTexImageTGAFile(gl, "assets/monkey/hurt1.tga", spriteSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/monkey/hurt2.tga", spriteSize), (float) 140),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/monkey/hurt3.tga", spriteSize), (float) 110) };
		
		AnimationFrames[] monkeyCelebration = {
				new AnimationFrames(glTexImageTGAFile(gl, "assets/monkey/cel1.tga", spriteSize), (float) 150),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/monkey/cel2.tga", spriteSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/monkey/cel3.tga", spriteSize), (float) 150),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/monkey/cel4.tga", spriteSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/monkey/cel5.tga", spriteSize), (float) 500),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/monkey/cel6.tga", spriteSize), (float) 150),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/monkey/cel7.tga", spriteSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/monkey/cel8.tga", spriteSize), (float) 150),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/monkey/cel9.tga", spriteSize), (float) 500),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/monkey/cel10.tga", spriteSize), (float) 1000)};
		
		AnimationFrames[] monkeyDie = {
				new AnimationFrames(glTexImageTGAFile(gl, "assets/monkey/die3.tga", spriteSize), (float) 140),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/monkey/die4.tga", spriteSize), (float) 140),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/monkey/die5.tga", spriteSize), (float) 140),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/monkey/die6.tga", spriteSize), (float) 200),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/monkey/die7.tga", spriteSize), (float) 130),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/monkey/die8.tga", spriteSize), (float) 130),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/monkey/die9.tga", spriteSize), (float) 130),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/monkey/die10.tga", spriteSize), (float) 130),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/monkey/die11.tga", spriteSize), (float) 700)};

		AnimationFrames[] snailMove = {
				new AnimationFrames(glTexImageTGAFile(gl, "assets/snail/snailrun1.tga", spriteSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/snail/snailrun2.tga", spriteSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/snail/snailrun3.tga", spriteSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/snail/snailrun4.tga", spriteSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/snail/snailrun5.tga", spriteSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/snail/snailrun6.tga", spriteSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/snail/snailrun7.tga", spriteSize), (float) 100) };

		AnimationFrames[] snailDie = {
				new AnimationFrames(glTexImageTGAFile(gl, "assets/snail/snaildie1.tga", spriteSize), (float) 90),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/snail/snaildie2.tga", spriteSize), (float) 90),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/snail/snaildie3.tga", spriteSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/snail/snaildie4.tga", spriteSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/snail/snaildie5.tga", spriteSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/snail/snaildie6.tga", spriteSize), (float) 90),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/snail/snaildie7.tga", spriteSize), (float) 1500) };

		AnimationFrames[] gullMove = {
				new AnimationFrames(glTexImageTGAFile(gl, "assets/seagull/gullfly1.tga", spriteSize), (float) 120),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/seagull/gullfly2.tga", spriteSize), (float) 120),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/seagull/gullfly3.tga", spriteSize), (float) 120),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/seagull/gullfly4.tga", spriteSize), (float) 120),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/seagull/gullfly5.tga", spriteSize), (float) 120),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/seagull/gullfly6.tga", spriteSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/seagull/gullfly7.tga", spriteSize), (float) 50),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/seagull/gullfly8.tga", spriteSize), (float) 50),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/seagull/gullfly9.tga", spriteSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/seagull/gullfly10.tga", spriteSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/seagull/gullfly11.tga", spriteSize), (float) 100) };

		AnimationFrames[] gullDie = {
				new AnimationFrames(glTexImageTGAFile(gl, "assets/seagull/gulldie1.tga", spriteSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/seagull/gulldie2.tga", spriteSize), (float) 90),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/seagull/gulldie3.tga", spriteSize), (float) 3000),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/seagull/gulldie4.tga", spriteSize), (float) 200) };
		
		AnimationFrames[] moleBury = {
				new AnimationFrames(glTexImageTGAFile(gl, "assets/mole/molebury1.tga", spriteSize), (float) 80),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/mole/molebury2.tga", spriteSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/mole/molebury3.tga", spriteSize), (float) 1000)};
		
		AnimationFrames[] molePopup = {
				new AnimationFrames(glTexImageTGAFile(gl, "assets/mole/molepopup1.tga", spriteSize), (float) 1000),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/mole/molepopup2.tga", spriteSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/mole/molepopup3.tga", spriteSize), (float) 90)};
		
		AnimationFrames[] moleShock = {
				new AnimationFrames(glTexImageTGAFile(gl, "assets/mole/moleshock1.tga", spriteSize), (float) 150),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/mole/moleshock2.tga", spriteSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/mole/moleshock3.tga", spriteSize), (float) 150),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/mole/moleshock4.tga", spriteSize), (float) 90),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/mole/moleshock5.tga", spriteSize), (float) 150),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/mole/moleshock6.tga", spriteSize), (float) 150),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/mole/moleshock7.tga", spriteSize), (float) 90),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/mole/moleshock8.tga", spriteSize), (float) 90),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/mole/moleshock9.tga", spriteSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/mole/moleshock10.tga", spriteSize), (float) 80),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/mole/moleshock11.tga", spriteSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/mole/molestand1.tga", spriteSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/mole/molestand2.tga", spriteSize), (float) 120),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/mole/molestand3.tga", spriteSize), (float) 120),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/mole/molestand4.tga", spriteSize), (float) 120),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/mole/molestand5.tga", spriteSize), (float) 120)};
		
		AnimationFrames[] moleDie = {
				new AnimationFrames(glTexImageTGAFile(gl, "assets/mole/moledie1.tga", spriteSize), (float) 160),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/mole/moledie2.tga", spriteSize), (float) 150),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/mole/moledie3.tga", spriteSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/mole/moledie4.tga", spriteSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/mole/moledie5.tga", spriteSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/mole/moledie6.tga", spriteSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/mole/moledie7.tga", spriteSize), (float) 130)};

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
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bosswalk13.tga", bossSize), (float) 100) };

		AnimationFrames[] bossJump = {
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bossjump1.tga", bossSize), (float) 50),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bossjump2.tga", bossSize), (float) 70),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bossjump3.tga", bossSize), (float) 80),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bossjump4.tga", bossSize), (float) 90),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bossjump5.tga", bossSize), (float) 400),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bossjump6.tga", bossSize), (float) 1500),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bossjump7.tga", bossSize), (float) 200),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bossjump8.tga", bossSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bossjump9.tga", bossSize), (float) 100) };

		AnimationFrames[] bossTaunt = {
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bosstaunt1.tga", bossSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bosstaunt2.tga", bossSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bosstaunt3.tga", bossSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bosstaunt4.tga", bossSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bosstaunt5.tga", bossSize), (float) 800) };

		AnimationFrames[] bossPunch = {
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bosspunch1.tga", bossSize), (float) 100),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bosspunch2.tga", bossSize), (float) 130),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bosspunch3.tga", bossSize), (float) 150),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bosspunch4.tga", bossSize), (float) 120),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bosspunch5.tga", bossSize), (float) 150) };

		AnimationFrames[] bossStomp = {
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bossstomp1.tga", bossSize), (float) 150),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bossstomp2.tga", bossSize), (float) 110),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bossstomp3.tga", bossSize), (float) 150),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bossstomp4.tga", bossSize), (float) 110) };

		AnimationFrames[] bossDeath = {
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bossdie1.tga", bossSize), (float) 500),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bossdie2.tga", bossSize), (float) 200),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bossdie3.tga", bossSize), (float) 200),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bossdie4.tga", bossSize), (float) 200),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bossdie5.tga", bossSize), (float) 200),
				new AnimationFrames(glTexImageTGAFile(gl, "assets/boss/bossdie6.tga", bossSize), (float) 500) };

		// Animations
		Animation runAnimation = new Animation(running);
		Animation monkeyJumpAnimation = new Animation(monkeyJump);
		Animation monkeyShootAnimation = new Animation(monkeyShoot);
		Animation monkeyPunchAnimation = new Animation(monkeyPunch);
		Animation monkeyHurtAnimation = new Animation(monkeyHurt);
		Animation monkeyDieAnimation = new Animation(monkeyDie);
		Animation monkeyCelebrationAnimation = new Animation(monkeyCelebration);
		Animation bossWalkAnimation = new Animation(bossWalk);
		Animation bossJumpAnimation = new Animation(bossJump);
		Animation bossTauntAnimation = new Animation(bossTaunt);
		Animation bossPunchAnimation = new Animation(bossPunch);
		Animation bossStompAnimation = new Animation(bossStomp);
		Animation bossDeathAnimation = new Animation(bossDeath);

		// Initialize all of the background textures
		skyTex = glTexImageTGAFile(gl, "assets/backgrounds/tileSky.tga", tileSize);
		groundtex = glTexImageTGAFile(gl, "assets/backgrounds/tileGround.tga", tileSize);
		bossSkyTexA = glTexImageTGAFile(gl, "assets/backgrounds/cavewall1.tga", tileSize);
		bossSkyTexB = glTexImageTGAFile(gl, "assets/backgrounds/cavewall2.tga", tileSize);
		bossGroundtex = glTexImageTGAFile(gl, "assets/backgrounds/cavefloor.tga", tileSize);
		titleScreen = glTexImageTGAFile(gl, "assets/backgrounds/titlecard.tga", menuSize);
		platformA = glTexImageTGAFile(gl, "assets/platforms/platformleftend.tga", tileSize);
		platformB = glTexImageTGAFile(gl, "assets/platforms/platform-middle.tga", tileSize);
		platformC = glTexImageTGAFile(gl, "assets/platforms/platformrightend.tga", tileSize);
		healthTex = glTexImageTGAFile(gl, "assets/misc/health1.tga", healthSize);
		

		backgroundMain = new Background(skyTex, true, 0, 180, 30, 20);
		backgroundFloor = new Background(groundtex, false, 150, 180, 30, 20);

		backgroundBossMainA = new Background(bossSkyTexA, true, 0, 50, 10, 10);
		backgroundBossMainB = new Background(bossSkyTexB, true, 0, 50, 10, 10);
		backgroundBossFloor = new Background(bossGroundtex, false, 50, 60, 10, 10);

		// Initialize characters in the game
		Character monkey = new Character(spritePos[0], spritePos[1], spriteSize[0], spriteSize[1], monkeyTex);
		monkey.setBoxXoffset(30);
		monkey.getHitbox().setWidth(50);
		monkey.setHealth(5);
		Boss boss = new Boss(800, 299, bossSize[0], bossSize[1], bossTex);

		ArrayList<Platform> platforms = new ArrayList<Platform>();
		platforms.add(new Platform(500, 200, 7));
		platforms.add(new Platform(500, 350, 7));
		platforms.add(new Platform(1800, 250, 3));
		platforms.add(new Platform(2000, 350, 4));
		platforms.add(new Platform(2200, 250, 4));

		// Create a bounding box camera for both the monkey and camera
		AABBCamera spriteAABB = new AABBCamera(spritePos[0], spritePos[1], spriteSize[0], spriteSize[1]);
		AABBCamera cameraAABB = new AABBCamera(camera.getX(), camera.getY(), xRes, yRes);

		// Frames
		long lastFrame;
		long currentFrame = System.nanoTime();

		int lastPhysicsFrameMs = 0;
		long curFrameMs;
		int physicsDeltaMs = 10;

		Random rand = new Random();

		// Timer for standing for when to have standing texture
		int standCount = 0;
		int bossSlamCount = 0;
		int rockTimer = 0;
		int rockCount = 0;
		// Previous positions of the monkey so it can't move through objects in
		// a collision
		int monkeyPreviousX = monkey.getX();
		int monkeyPreviousY = monkey.getY();
		int bossPreviousY = boss.getY();
		
		boolean gameOver = false;

		// Arraylist of enemies since there will be multiple
		ArrayList<Character> enemies = new ArrayList<Character>();
		enemies.add(new Snail(enemyPos[0] + 200, enemyPos[1], spriteSize[0], spriteSize[1], enemyTex,
				new Animation(snailMove), new Animation(snailDie)));
		enemies.add(new Snail(enemyPos[0] + 1000, enemyPos[1], spriteSize[0], spriteSize[1], enemyTex,
				new Animation(snailMove), new Animation(snailDie)));
		enemies.add(new Snail(enemyPos[0] + 1500, enemyPos[1], spriteSize[0], spriteSize[1], enemyTex,
				new Animation(snailMove), new Animation(snailDie)));
		enemies.add(new Seagull(800, 200, spriteSize[0], spriteSize[1], gullMove[0].getSprite(),
				new Animation(gullMove), new Animation(gullDie)));
		enemies.add(new Seagull(1000, 300, spriteSize[0], spriteSize[1], gullMove[0].getSprite(),
				new Animation(gullMove), new Animation(gullDie)));
		enemies.add(new Seagull(1500, 100, spriteSize[0], spriteSize[1], gullMove[0].getSprite(),
				new Animation(gullMove), new Animation(gullDie)));
		enemies.add(new Seagull(2000, 400, spriteSize[0], spriteSize[1], gullMove[0].getSprite(),
				new Animation(gullMove), new Animation(gullDie)));
		enemies.add(new Mole(400, 400, spriteSize[0], spriteSize[1], molePopup[0].getSprite(),
				new Animation(moleBury), new Animation(molePopup), new Animation(moleShock), new Animation(moleDie)));

		//Sound
		Sound soundMain = Sound.loadFromFile("assets/sound/Sound_Main.wav");
        Sound soundBoss = Sound.loadFromFile("assets/sound/Sound_Boss.wav");
        
        //Load empty
    	Clip bgClip = soundMain.playLooping();
    	Clip bossClip = null; 
    	
        boolean bgPlaying = false;
        
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
			
			//Play bg sounds
			if(bossMode == true){
				if(!bgPlaying){	
					bossClip = soundBoss.playLooping();
					//bossClip.start();
					bgPlaying = true;
				}
			}

			
			currentFrame = System.nanoTime();
			curFrameMs = currentFrame / 1000000;
			long delta = (currentFrame - lastFrame) / 1000000;
			
			if (bossInit == true) {
				if(!monkeyCelebrationAnimation.isFinished()){
					monkey.setCelebrating(true);
					monkey.setCurrentTexture(monkeyCelebrationAnimation.getCurrentFrame());
					monkeyCelebrationAnimation.updateSprite(delta);
				}
				else{
				monkey.setCelebrating(false);
				monkeyCelebrationAnimation.resetFrames();
				levelWidth = backgroundBossMainA.getWidth();
				levelHeight = backgroundBossMainA.getHeight();
				backgroundFloor = backgroundBossFloor;
				monkey.setX(200);
				monkey.setY(350);
				boss = new Boss(800, 299, bossSize[0], bossSize[1], bossTex);
				monkey.setHealth(5);
				enemies.add(boss);
				boss.setX(800);
				boss.setY(299);
				bossPreviousY = 299;
				camera.setX(0);
				camera.setY(0);
				platforms.clear();
				bossInit = false;
				bgClip.setMicrosecondPosition(0);
				bgClip.stop();
				bgPlaying = false;
				bossMode = true;
				}
			}
			System.out.println(boss.getX());
			if(gameOver == true){
				enemies.clear();
				//bossClip is null if never reached the boss
				if(bossClip != null){
					bossClip.setMicrosecondPosition(0);
					bossClip.stop();
				}
				
				//Reset the song
				bgClip.setMicrosecondPosition(0);
				bgClip.stop();
				bgClip = soundMain.playLooping();
				enemies.add(new Snail(enemyPos[0] + 200, enemyPos[1], spriteSize[0], spriteSize[1], enemyTex,
						new Animation(snailMove), new Animation(snailDie)));
				enemies.add(new Snail(enemyPos[0] + 1000, enemyPos[1], spriteSize[0], spriteSize[1], enemyTex,
						new Animation(snailMove), new Animation(snailDie)));
				enemies.add(new Snail(enemyPos[0] + 1500, enemyPos[1], spriteSize[0], spriteSize[1], enemyTex,
						new Animation(snailMove), new Animation(snailDie)));
				enemies.add(new Seagull(800, 200, spriteSize[0], spriteSize[1], gullMove[0].getSprite(),
						new Animation(gullMove), new Animation(gullDie)));
				enemies.add(new Seagull(1000, 300, spriteSize[0], spriteSize[1], gullMove[0].getSprite(),
						new Animation(gullMove), new Animation(gullDie)));
				enemies.add(new Seagull(1500, 100, spriteSize[0], spriteSize[1], gullMove[0].getSprite(),
						new Animation(gullMove), new Animation(gullDie)));
				enemies.add(new Seagull(2000, 400, spriteSize[0], spriteSize[1], gullMove[0].getSprite(),
						new Animation(gullMove), new Animation(gullDie)));
				enemies.add(new Mole(400, 400, spriteSize[0], spriteSize[1], molePopup[0].getSprite(),
						new Animation(moleBury), new Animation(molePopup), new Animation(moleShock), new Animation(moleDie)));
				
				
				camera.setX(0);
				camera.setY(0);
				monkey.setDead(false);
				monkey = new Character(spritePos[0], spritePos[1], spriteSize[0], spriteSize[1], monkeyTex);
				monkey.setBoxXoffset(30);
				monkey.getHitbox().setWidth(50);
				monkey.setHealth(5);
				platforms.clear();
				platforms.add(new Platform(500, 200, 7));
				platforms.add(new Platform(500, 350, 7));
				platforms.add(new Platform(1800, 250, 3));
				platforms.add(new Platform(2000, 350, 4));
				platforms.add(new Platform(2200, 250, 4));
				levelWidth = backgroundMain.getWidth();
				levelHeight = backgroundMain.getHeight();
				backgroundFloor = new Background(groundtex, false, 150, 180, 30, 20);
				monkeyPreviousY = monkey.getY();
				bossMode = false;
				titleMode = true;
			}
			/**
			 * Code for having gravity affect the monkey. For some reason it
			 * works best in this spot right before the physics loop. Will play
			 * jumping animation if monkey is jumping.
			 */
			if (monkey.isJumping()) {
				monkeyJumpAnimation.updateSprite(delta);
				monkey.setCurrentTexture(monkeyJumpAnimation.getCurrentFrame());

				monkey.setY((int) (monkey.getY() + monkey.getyVelocity()));
				monkey.setyVelocity(monkey.getyVelocity() + monkey.getAcceleration());
			} else {

				monkey.setY((int) (monkey.getY() + monkey.getyVelocity()));
				monkey.setyVelocity(monkey.getyVelocity() + monkey.getAcceleration());
			}
			boss.setY((int) (boss.getY() + boss.getyVelocity()));
			boss.setyVelocity(boss.getyVelocity() + boss.getAcceleration());

			// The physics loop
			do {
				/**
				 * Check to see if a projectile of the monkey's is hitting an
				 * enemy If it is, we remove the projectile and subtract health
				 * from the enemy.
				 */
				for (int i = 0; i < monkey.getProjectiles().size(); i++) {
					ArrayList<Projectile> mainCharProj = (ArrayList<Projectile>) monkey.getProjectiles();
					Projectile p = mainCharProj.get(i);
					p.update();
					AABBCamera projectile = mainCharProj.get(i).getCollisionBox();
					for (Character c : enemies) {
						if (AABBIntersect(projectile, c.getHitbox()) && !(i > mainCharProj.size() - 1)) {
							mainCharProj.get(i).setVisible(false);
							mainCharProj.remove(i);
							c.setHealth(c.getHealth() - 1);
							c.setHit(!c.isHit());
							if (c.getHealth() <= 0) {
								c.setDead(true);
							}
						}
					}
				}

				for (Character e : enemies) {
					if (monkey.isPunching() && (monkeyPunchAnimation.getFrameNumber() == 1
							|| monkeyPunchAnimation.getFrameNumber() == 2)) {
						if (AABBIntersect(e.getHitbox(), monkey.getHitbox())) {
							if (!e.isHit()) {
								e.setHealth(e.getHealth() - 1);
								e.setHit(!e.isHit());
								if (e.getHealth() <= 0) {
									e.setDead(true);
								}
							}
						}
					}
				}
				/**
				 * Similar to the loop above but for enemy projectiles. If the
				 * monkey is hit, the monkey's is hit property is triggered.
				 */
				for (Character c : enemies) {
					for (int i = 0; i < c.getProjectiles().size(); i++) {
						ArrayList<Projectile> enemyProjectiles = (ArrayList<Projectile>) c.getProjectiles();
						Projectile p = enemyProjectiles.get(i);
						p.update();
						AABBCamera projectile = enemyProjectiles.get(i).getCollisionBox();
						if (AABBIntersect(projectile, monkey.getHitbox())) {
							if (!p.isRock()) {
								enemyProjectiles.get(i).setVisible(false);
								enemyProjectiles.remove(i);
							}
							if (!monkey.isInvincible()) {
								monkey.setHealth(monkey.getHealth() - 1);
								if(monkey.getHealth() < 1){
									monkey.setDead(true);
								}
								monkey.setHit(!monkey.isHit());
							}
						}
					}
					if (c instanceof Boss) {
						if (((Boss) c).isPunching() && (bossPunchAnimation.getFrameNumber() == 2
								|| bossPunchAnimation.getFrameNumber() == 3) && !monkey.isInvincible()) {
							if (AABBIntersect(c.getHitbox(), monkey.getHitbox())) {
								if (!monkey.isInvincible()) {
									monkey.setHealth(monkey.getHealth() - 1);
									boss.setPunching(false);
									if(monkey.getHealth() < 1){
										monkey.setDead(true);
									}
									monkey.setHit(!monkey.isHit());
								}
							}
						}
						if (boss.isJumping()) {
							if (AABBIntersect(c.getHitbox(), monkey.getHitbox())) {
								if (!monkey.isInvincible() && !monkey.isHit()) {
									monkey.setHealth(monkey.getHealth() - 1);
									if(monkey.getHealth() < 1){
										monkey.setDead(true);
									}
									monkey.setHit(!monkey.isHit());
								}
							}
						}
					}
					
					if (c instanceof Seagull) {
						if (((Seagull) c).isPunching()){
							if (AABBIntersect(c.getHitbox(), monkey.getHitbox())) {
								if (!monkey.isInvincible()) {
									((Seagull) c).setPunching(false);
									monkey.setHealth(monkey.getHealth() - 1);
									if(monkey.getHealth() < 1){
										monkey.setDead(true);
									}
									monkey.setHit(!monkey.isHit());
								}
							}
						}
					}
					if (c instanceof Mole) {
						if (((Mole) c).isPunching()){
							if (AABBIntersect(c.getHitbox(), monkey.getHitbox())) {
								if (!monkey.isInvincible()) {
									monkey.setHealth(monkey.getHealth() - 1);
									if(monkey.getHealth() < 1){
										monkey.setDead(true);
									}
									monkey.setHit(!monkey.isHit());
								}
							}
						}
					}
				}

				// Code to determine how many tiles are in our current screen
				int startX = camera.getX() / tileSize[0];
				int endX = (camera.getX() + xRes) / tileSize[0];
				int startY = camera.getY() / tileSize[0];
				int endY = (camera.getY() + yRes) / tileSize[1];

				/**
				 * Checks for collisions with the monkey and the floor tile. We
				 * add one to endX to account for tiles on the edge not seen
				 * from our previous initialization. If the monkey is colliding,
				 * we stop the monkey from jumping, set his velocity to 0, and
				 * return him to his previous position.
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
								// monkey.setX(monkeyPreviousX);
								monkey.setY(monkeyPreviousY);
							}
							boolean collBoss = AABBIntersect(boss.getHitbox(), tileAABB);
							if (collBoss) {
								boss.setJumping(false);
								boss.setyVelocity(0);
								bossJumpAnimation.resetFrames();
								boss.setY(bossPreviousY);
							}
						}
					}
				}

				for (Platform platform : platforms) {
					AABBCamera monkeyBox;
					if (monkey.isJumping()) {
						monkeyBox = new AABBCamera(monkey.getHitbox().getX(), monkey.getHitbox().getY(),
								monkey.getHitbox().getHeight() - 20, monkey.getHitbox().getWidth());
						if (AABBIntersect(monkeyBox, platform.getCollisionBox()) && monkey.getyVelocity() > 0) {
							if (monkeyPreviousY + 80 - platform.getY() < 2) {
								monkey.setJumping(false);
								monkeyJumpAnimation.resetFrames();
								monkey.setyVelocity(0);
								monkey.setY(monkeyPreviousY - 20);
							}
						}
					} else {
						monkeyBox = monkey.getHitbox();
						if (AABBIntersect(monkey.getHitbox(), platform.getCollisionBox()) && !kbState[KeyEvent.VK_S]) {
							if (monkeyPreviousY + 100 - platform.getY() < 2) {
								monkey.setJumping(false);
								monkeyJumpAnimation.resetFrames();
								monkey.setyVelocity(0);
								monkey.setY(monkeyPreviousY);
							}
						}
					}

				}

				lastPhysicsFrameMs += physicsDeltaMs;
			} while (lastPhysicsFrameMs + physicsDeltaMs < curFrameMs);

			// Set the monkey previous coordinates after physics have run.
			monkeyPreviousX = monkey.getX();
			monkeyPreviousY = monkey.getY();
			bossPreviousY = boss.getY();
			System.out.println(runAnimation.getFrameNumber());
			// Enemy Movement
			for (Character e : enemies) {
				if (e instanceof Snail) {
					((Snail) e).setShootTimer(((Snail) e).getShootTimer() + 1);
					/**
					 * Snail movement. Tracks the monkey's position
					 */
					if (e.isDead()) {
						e.setInvincible(true);
						((Snail) e).getSnailDieAnimation().updateSprite(delta);
						e.setCurrentTexture(((Snail) e).getSnailDieAnimation().getCurrentFrame());
						if (((Snail) e).getSnailDieAnimation().isFinished()) {
							e.setVisible(false);
						}
					} else if (Math.abs(monkey.getX() - e.getX()) < 500) {
						if (e.getX() < monkey.getX() && !e.isShooting()) {
							e.setX(e.getX() + 1);
							e.setReverse(true);
							((Snail) e).getSnailMoveAnimation().updateSprite(delta);
							e.setCurrentTexture(((Snail) e).getSnailMoveAnimation().getCurrentFrame());
						} else if (e.getX() > monkey.getX() && !e.isShooting()) {
							e.setX(e.getX() - 1);
							e.setReverse(false);
							((Snail) e).getSnailMoveAnimation().updateSprite(delta);
							e.setCurrentTexture(((Snail) e).getSnailMoveAnimation().getCurrentFrame());
						} else if (!e.isShooting()) {
							e.setCurrentTexture(enemyStandingTex);
							((Snail) e).getSnailMoveAnimation().resetFrames();
						}
						if (((Snail) e).getShootTimer() == 100 && e.getVisible()) {
							e.addProjectile(new Projectile(e.getX() + 50, e.getY() + 30, projectileSize[0],
									projectileSize[1], !e.getReverse()));
							e.setShooting(true);
							e.setCurrentTexture(enemyShootTex);
						}
						if (((Snail) e).getShootTimer() == 130 && e.getVisible()) {
							e.setShooting(false);
							e.setCurrentTexture(enemyStandingTex);
							((Snail) e).setShootTimer(0);
						}
					} else {
						e.setShooting(false);
						((Snail) e).setShootTimer(0);
					}

				} else if (e instanceof Seagull) {
					if (e.isDead()) {
						e.setPunching(false);
						e.setInvincible(true);
						e.setY(e.getY() + 3);
						e.setCurrentTexture(((Seagull) e).getSeagullDieAnimation().getCurrentFrame());
						((Seagull) e).getSeagullDieAnimation().updateSprite(delta);
						if (((Seagull) e).getSeagullDieAnimation().isFinished()) {
							e.setVisible(false);
						}
					} else if (Math.abs(monkey.getX() - e.getX()) > 200 && Math.abs(monkey.getX() - e.getX()) < 500  && !((Seagull) e).isTargetAcquired()) {
						((Seagull) e).setAttackTimer(((Seagull) e).getAttackTimer() + 1);
						if (e.getX() <= monkey.getX() && !e.isShooting()) {
							e.setX(e.getX() + 1);
							e.setReverse(true);
							((Seagull) e).getSeagullMoveAnimation().updateSprite(delta);
							e.setCurrentTexture(((Seagull) e).getSeagullMoveAnimation().getCurrentFrame());
						} else if (e.getX() > monkey.getX()) {
							e.setX(e.getX() - 1);
							e.setReverse(false);
							((Seagull) e).getSeagullMoveAnimation().updateSprite(delta);
							e.setCurrentTexture(((Seagull) e).getSeagullMoveAnimation().getCurrentFrame());
						}
					} else if (Math.abs(monkey.getX() - e.getX()) <= 200 && !((Seagull) e).isTargetAcquired() && ((Seagull) e).getAttackTimer() > 100) {
						((Seagull) e).setTargetX(monkey.getX());
						((Seagull) e).setTargetY(monkey.getY());
						while (((Seagull) e).getTargetX() % 2 != 0) {
							((Seagull) e).setTargetX(((Seagull) e).getTargetX() + 1);
						}
						while (((Seagull) e).getTargetY() % 2 != 0) {
							((Seagull) e).setTargetY(((Seagull) e).getTargetY() + 1);
						}
						while (e.getX() % 2 != 0) {
							e.setX(e.getX() - 1);
						}
						while (e.getY() % 2 != 0) {
							e.setY(e.getY() - 1);
						}
						((Seagull) e).setTargetAcquired(true);
					}
					else if(((Seagull) e).isTargetAcquired()){
						System.out.println(((Seagull) e).getTargetX() + " , " + ((Seagull) e).getTargetY());
						System.out.println(e.getX() + " , " + e.getY());
						e.setCurrentTexture(gullAttackTex);
						e.setPunching(true);
						if(e.getY() == ((Seagull) e).getTargetY() && e.getX() == ((Seagull) e).getTargetX()){
							((Seagull) e).setTargetAcquired(false);
							e.setPunching(false);
							((Seagull) e).setAttackTimer(0);
						}
						else{
						if (e.getX() < ((Seagull) e).getTargetX()) {
							e.setX(e.getX() + 2);
							e.setReverse(true);
						} else if (e.getX() > ((Seagull) e).getTargetX()) {
							e.setX(e.getX() - 2);
							e.setReverse(false);
						}
						if (e.getY() > ((Seagull) e).getTargetY()) {
							e.setY(e.getY() - 2);
						} else if (e.getY() < ((Seagull) e).getTargetY()) {
							e.setY(e.getY() + 2);
						}
						}
					}
					else{
						((Seagull) e).getSeagullMoveAnimation().updateSprite(delta);
						e.setCurrentTexture(((Seagull) e).getSeagullMoveAnimation().getCurrentFrame());
					
					}
				}
				
				else if (e instanceof Mole) {
					((Mole) e).setPopupTimer(((Mole) e).getPopupTimer() + 1);
					if (e.isDead()) {
						e.setInvincible(true);
						e.setCurrentTexture(((Mole) e).getMoleDieAnimation().getCurrentFrame());
						((Mole) e).getMoleDieAnimation().updateSprite(delta);
						if (((Mole) e).getMoleDieAnimation().isFinished()) {
							e.setVisible(false);
						}
					}
						else if(((Mole) e).getPopupTimer() > 50){
					if(!((Mole) e).isTargetAcquired()){
					e.setX(monkey.getX());
					e.setInvincible(true);
					((Mole) e).setTargetAcquired(true);
					}
					else{
						if(!((Mole) e).getMolePopupAnimation().isFinished()){
						if(((Mole) e).getMolePopupAnimation().getFrameNumber() == 1){
							e.setPunching(true);
						}
						((Mole) e).getMolePopupAnimation().updateSprite(delta);
						e.setCurrentTexture(((Mole) e).getMolePopupAnimation().getCurrentFrame());
						if(((Mole) e).getMolePopupAnimation().isFinished()){
							e.setInvincible(false);
							e.setPunching(false);
						}
						}
						else if(!((Mole) e).getMoleShockAnimation().isFinished()){
							
							((Mole) e).getMoleShockAnimation().updateSprite(delta);
							e.setCurrentTexture(((Mole) e).getMoleShockAnimation().getCurrentFrame());
							}
						else if(!((Mole) e).getMoleBuryAnimation().isFinished()){
							e.setInvincible(true);
							((Mole) e).getMoleBuryAnimation().updateSprite(delta);
							e.setCurrentTexture(((Mole) e).getMoleBuryAnimation().getCurrentFrame());
							}
						else{
							((Mole) e).getMolePopupAnimation().resetFrames();
							((Mole) e).getMoleBuryAnimation().resetFrames();
							((Mole) e).getMoleShockAnimation().resetFrames();
							((Mole) e).setPopupTimer(0);
							e.setCurrentTexture(molePopup[0].getSprite());
							((Mole) e).setTargetAcquired(false);
							e.setInvincible(true);
						}
					}
				}
					}
				
			}

			/**
			 * 
			 * BOSS AI CODE
			 * 
			 * 
			 */

			if (bossMode == true) {
				if (boss.getAttackMode() == 0) {
					boss.setTargetX(400);
					boss.setTargetAcquired(true);
					if (boss.getTargetX() != boss.getX()) {
						if (boss.getTargetX() < boss.getX()) {
							boss.setReverse(false);
							boss.setX(boss.getX() - 2);
						} else if (boss.getTargetX() > boss.getX()) {
							boss.setReverse(true);
							boss.setX(boss.getX() + 2);
						}
						bossWalkAnimation.updateSprite(delta);
						boss.setCurrentTexture(bossWalkAnimation.getCurrentFrame());
					} else {
						boss.setTargetAcquired(false);
						boss.setAttackMode(1);
						bossWalkAnimation.resetFrames();
					}
				} else if (boss.getAttackMode() == 1) {
					if (bossTauntAnimation.isFinished()) {
						if (boss.isDead() && !bossDeathAnimation.isFinished()) {
							boss.setInvincible(true);
							boss.setDead(true);
							boss.setCurrentTexture(bossDeathAnimation.getCurrentFrame());
							bossDeathAnimation.updateSprite(delta);
						} else if (!boss.isDead()) {
							boss.setTargetAcquired(false);
							boss.setJumping(false);
							bossTauntAnimation.resetFrames();
							bossJumpAnimation.resetFrames();
							bossPunchAnimation.resetFrames();
							boss.setAttackMode(rand.nextInt(3) + 2);
						}
						else if(boss.isDead() && bossDeathAnimation.isFinished()){
							monkey.setCelebrating(true);
							monkey.setCurrentTexture(monkeyCelebrationAnimation.getCurrentFrame());
							monkeyCelebrationAnimation.updateSprite(delta);
							if(monkeyCelebrationAnimation.isFinished()){
								monkey.setCelebrating(false);
								gameOver = true;
							}
						}
					} else {
						boss.setCurrentTexture(bossTauntAnimation.getCurrentFrame());
						bossTauntAnimation.updateSprite(delta);
					}
				} else if (boss.getAttackMode() == 2) {
					if (!boss.isTargetAcquired() && !boss.isJumping()) {
						boss.setTargetX(monkey.getX());
						while (boss.getTargetX() % 4 != 0) {
							boss.setTargetX(boss.getTargetX() + 1);
						}
						boss.setTargetAcquired(true);
						boss.setyVelocity(-8);
						boss.setJumping(true);
						boss.setCurrentTexture(bossJumpAnimation.getCurrentFrame());
					} else {
						if (boss.getTargetX() < boss.getX() && boss.isJumping()) {
							boss.setReverse(false);
							boss.setX(boss.getX() - 4);
							bossJumpAnimation.updateSprite(delta);
							boss.setCurrentTexture(bossJumpAnimation.getCurrentFrame());
						} else if (boss.getTargetX() > boss.getX() && boss.isJumping()) {
							boss.setReverse(true);
							boss.setX(boss.getX() + 4);
							bossJumpAnimation.updateSprite(delta);
							boss.setCurrentTexture(bossJumpAnimation.getCurrentFrame());
						} else if (!boss.isJumping()) {
							if (!boss.isShooting()) {
								boss.setShooting(true);
								boss.addProjectile(new Projectile(boss.getX() + 25, boss.getY() + 125, shockwaveSize[0],
										shockwaveSize[1], true));
								boss.addProjectile(new Projectile(boss.getX() + 175, boss.getY() + 125,
										shockwaveSize[0], shockwaveSize[1], false));
							}
							bossSlamCount++;
							bossJumpAnimation.setCurrentFrame(7);
							boss.setCurrentTexture(bossJumpAnimation.getCurrentFrame());
							if (bossSlamCount > 100) {
								boss.setShooting(false);
								bossSlamCount = 0;
								boss.setTargetAcquired(false);
								boss.setAttackMode(1);
							}
						}
					}
				} else if (boss.getAttackMode() == 3) {
					if (!boss.isTargetAcquired()) {
						boss.setTargetX(monkey.getX());
						while (boss.getTargetX() % 4 != 0) {
							boss.setTargetX(boss.getTargetX() + 1);
						}
						boss.setTargetAcquired(true);
					} else {
						if (boss.getTargetX() < boss.getX()) {
							boss.setReverse(false);
							boss.setX(boss.getX() - 4);
							bossWalkAnimation.updateSprite(delta);
							boss.setCurrentTexture(bossWalkAnimation.getCurrentFrame());
						} else if (boss.getTargetX() > boss.getX()) {
							boss.setReverse(true);
							boss.setX(boss.getX() + 4);
							bossWalkAnimation.updateSprite(delta);
							boss.setCurrentTexture(bossWalkAnimation.getCurrentFrame());
						} else {
							if (!boss.isPunching()) {
								boss.setPunching(true);
							}
							bossPunchAnimation.updateSprite(delta);
							boss.setCurrentTexture(bossPunchAnimation.getCurrentFrame());
							if (bossPunchAnimation.isFinished()) {
								boss.setAttackMode(1);
								boss.setPunching(false);
								boss.setTargetAcquired(false);
							}
						}
					}
				} else if (boss.getAttackMode() == 4) {
					bossStompAnimation.updateSprite(delta);
					boss.setCurrentTexture(bossStompAnimation.getCurrentFrame());
					rockTimer++;
					if (rockTimer == 40) {
						Projectile rock = new Projectile((rand.nextInt(600) + 200), -101, 100, 100, false);
						rock.setRock(true);
						rock.setSpeed(5);
						boss.addProjectile(rock);
						rockTimer = 0;
						rockCount++;
					}
					if (rockCount == 10) {
						boss.setAttackMode(1);
					}
				}
			}
			/**
			 * Code for dealing with monkey shooting.
			 */

			if (monkey.isHit()) {
				monkey.setInvincible(true);
				monkeyHurtAnimation.updateSprite(delta);
				monkey.setCurrentTexture(monkeyHurtAnimation.getCurrentFrame());
				if (monkeyHurtAnimation.isFinished()) {
					if(monkey.isDead()){
						monkeyDieAnimation.updateSprite(delta);
						monkey.setCurrentTexture(monkeyDieAnimation.getCurrentFrame());
						if(monkeyDieAnimation.isFinished()){
						monkeyDieAnimation.resetFrames();
						gameOver = true;
						}
					}
					else{
					monkeyHurtAnimation.resetFrames();
					monkey.setHit(false);
					}
				}
			}

			if (monkey.isInvincible() && !monkey.isHit()) {
				monkey.setHurtTimer(monkey.getHurtTimer() + 1);
				if (monkey.getHurtTimer() == 100) {
					monkey.setInvincible(false);
					monkey.setHurtTimer(0);
				}
			}

			for (Character enemy : enemies) {
				if (enemy.isHit()) {
					enemy.setInvincible(true);
				}
				if (enemy.isInvincible()) {
					enemy.setHurtTimer(enemy.getHurtTimer() + 1);
					if (enemy.getHurtTimer() == 30) {
						enemy.setHit(false);
						enemy.setInvincible(false);
						enemy.setHurtTimer(0);
					}
				}
			}

			if (!monkey.isShooting()) {
				if (kbState[KeyEvent.VK_SHIFT] && !monkey.isHit()) {
					monkey.addProjectile(new Projectile(monkey.getX() + 20, monkey.getY() + 20, projectileSize[0],
							projectileSize[1], monkey.getReverse()));
					monkey.setShooting(true);
				}
			} else {
				monkeyShootAnimation.updateSprite(delta);
				monkey.setCurrentTexture(monkeyShootAnimation.getCurrentFrame());
				if (monkeyShootAnimation.isFinished()) {
					monkeyShootAnimation.resetFrames();
					monkey.setShooting(false);
				}
			}

			if (!monkey.isShooting()) {
				if (kbState[KeyEvent.VK_SPACE] && !monkey.isHit() && !monkey.isPunching()) {
					monkey.setPunching(true);
				} else if (monkey.isPunching()) {
					monkeyPunchAnimation.updateSprite(delta);
					monkey.setCurrentTexture(monkeyPunchAnimation.getCurrentFrame());
					if (monkeyPunchAnimation.isFinished()) {
						monkeyPunchAnimation.resetFrames();
						monkey.setPunching(false);
					}
				}
			}

			/**
			 * If the monkey is not moving or jumping or shooting, then play the
			 * standing animation. The stand count was added for some weird
			 * animation bug
			 */

			if (kbState[KeyEvent.VK_D] == false && kbState[KeyEvent.VK_A] == false && !monkey.isShooting()
					&& !monkey.isJumping() && !monkey.isHit() && !monkey.isPunching() && !monkey.isCelebrating()) {
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
			if (kbState[KeyEvent.VK_A] && monkey.getX() > 0 && !monkey.isShooting() && !monkey.isHit() && !monkey.isCelebrating()) {
				monkey.setX(monkey.getX() - 3);
				if (monkey.getX() < camera.getX() + (xRes / 6)) {
					if (camera.getX() - 3 < 0) {
						camera.setX(camera.getX());
					} else {
						camera.setX(camera.getX() - 3);
					}
				}
				monkey.setReverse(true);
				if (!monkey.isJumping() && !monkey.isPunching()) {
					runAnimation.updateSprite(delta);
					monkey.setCurrentTexture(runAnimation.getCurrentFrame());
				}
			}
			if (kbState[KeyEvent.VK_D] && monkey.getX() < levelWidth * tileSize[0] - spriteSize[0]
					&& !monkey.isShooting() && !monkey.isHit()  && !monkey.isCelebrating()) {
				monkey.setX(monkey.getX() + 3);
				if (monkey.getX() > camera.getX() + (xRes - (xRes / 3))) {
					if (camera.getX() + 3 > tileSize[0] * levelWidth - xRes) {
						camera.setX(camera.getX());
					} else {
						camera.setX(camera.getX() + 3);
					}
				}
				monkey.setReverse(false);
				if (!monkey.isJumping() && !monkey.isPunching()) {
					runAnimation.updateSprite(delta);
					monkey.setCurrentTexture(runAnimation.getCurrentFrame());
				}
			}

			/**
			 * Press W to jump. Change number in set velocity to determine how
			 * high monkey jumps.
			 */
			if (kbState[KeyEvent.VK_W] && monkey.getY() > 0 && !monkey.isJumping() && !monkey.isHit() && !monkey.isCelebrating()) {
				monkey.setJumping(true);
				monkey.setyVelocity(-7);
			}

			// Does nothing as of now
			if (kbState[KeyEvent.VK_S] && monkey.getY() < backgroundMain.getHeight() * tileSize[1] - spriteSize[1]) {
				// monkey.setJumping(true);
			}

			// Camera controls
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

			// Update bounding box for monkey and camera
			spriteAABB.setX(monkey.getX());
			spriteAABB.setY(monkey.getY());
			spriteAABB.setWidth(spriteSize[0]);
			spriteAABB.setHeight(spriteSize[1]);

			cameraAABB.setX(camera.getX());
			cameraAABB.setY(camera.getY());
			cameraAABB.setWidth(xRes);
			cameraAABB.setHeight(yRes);

			// Check to see what tiles are in bounds of the camera.
			backgroundCheck = backgroundInBounds(camera.getX(), camera.getY());

			gl.glClearColor(0, 0, 0, 1);
			gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

			// If an enemy is not visible, then remove it from the enemy
			// arraylist
			for (int i = 0; i < enemies.size(); i++) {
				if (!enemies.get(i).getVisible()) {
					enemies.get(i).getProjectiles().clear();
					enemies.remove(i);
				}
				if(enemies.size() == 0){
					bossInit = true;
				}
			}

			if (!bossMode) {
				// Draw main background. Last two params for glDrawSprite are
				// for reversing image and making image red.
				for (int i = backgroundCheck; i < backgroundMain.getWidth(); i++) {
					for (int j = 0; j < backgroundMain.getHeight(); j++) {
						if (backgroundMain.getTile(i, j) != null) {
							glDrawSprite(gl, backgroundMain.getTile(i, j).getImage(), i * tileSize[0] - camera.getX(),
									j * tileSize[1] - camera.getY(), tileSize[0], tileSize[1], false, false);
						}
					}
				}
				// Draw floor
				for (int i = backgroundCheck; i < backgroundFloor.getWidth(); i++) {
					for (int j = 0; j < backgroundFloor.getHeight(); j++) {
						if (backgroundFloor.getTile(i, j) != null) {
							glDrawSprite(gl, backgroundFloor.getTile(i, j).getImage(), i * tileSize[0] - camera.getX(),
									j * tileSize[1] - camera.getY(), tileSize[0], tileSize[1], false, false);
						}
					}
				}
			} else {
				for (int i = backgroundCheck; i < backgroundBossMainB.getWidth(); i++) {
					for (int j = 0; j < backgroundBossMainB.getHeight(); j++) {
						if (backgroundBossMainB.getTile(i, j) != null) {
							glDrawSprite(gl, backgroundBossMainB.getTile(i, j).getImage(),
									i * tileSize[0] - camera.getX(), j * tileSize[1] - camera.getY(), tileSize[0],
									tileSize[1], false, false);
						}
					}
				}
				// Draw floor
				for (int i = backgroundCheck; i < backgroundBossFloor.getWidth(); i++) {
					for (int j = 0; j < backgroundBossFloor.getHeight(); j++) {
						if (backgroundBossFloor.getTile(i, j) != null) {
							glDrawSprite(gl, backgroundBossFloor.getTile(i, j).getImage(),
									i * tileSize[0] - camera.getX(), j * tileSize[1] - camera.getY(), tileSize[0],
									tileSize[1], false, false);
						}
					}
				}
			}

			// Draw platforms
			for (Platform platform : platforms) {
				if (AABBIntersect(cameraAABB, platform.getCollisionBox())) {
					glDrawSprite(gl, platformA, platform.getX() - camera.getX(), platform.getY() - camera.getY(),
							tileSize[0], tileSize[1], false, false);
					for (int i = 0; i < platform.getLength() - 2; i++) {
						glDrawSprite(gl, platformB, platform.getX() + (100 * (i + 1)) - camera.getX(),
								platform.getY() - camera.getY(), tileSize[0], tileSize[1], false, false);
					}

					glDrawSprite(gl, platformC, platform.getX() + (100 * (platform.getLength() - 1)) - camera.getX(),
							platform.getY() - camera.getY(), tileSize[0], tileSize[1], false, false);
				}

			}

			// Draw all enemies inside main camera
			for (Character c : enemies) {
				if (c.getVisible()) {
					if (AABBIntersect(cameraAABB, c.getHitbox())) {
						glDrawSprite(gl, c.getCurrentTexture(), c.getX() - camera.getX(), c.getY() - camera.getY(),
								c.getWidth(), c.getHeight(), c.getReverse(), c.isInvincible() && !c.isDead());
					}
				}
			}

			// Draw monkey projectiles
			ArrayList<Projectile> monkeyProjectiles = (ArrayList<Projectile>) monkey.getProjectiles();
			for (int i = 0; i < monkeyProjectiles.size(); i++) {
				Projectile p = monkeyProjectiles.get(i);
				glDrawSprite(gl, monkeyProjectileTex, p.getX() - camera.getX(), p.getY() - camera.getY(),
						projectileSize[0], projectileSize[1], false, false);
			}

			// Draw snail projectiles
			for (Character e : enemies) {
				if (e instanceof Snail) {
					ArrayList<Projectile> snailProjectiles = (ArrayList<Projectile>) e.getProjectiles();
					for (int i = 0; i < snailProjectiles.size(); i++) {
						Projectile p = snailProjectiles.get(i);
						glDrawSprite(gl, snailProjectileTex, p.getX() - camera.getX(), p.getY() - camera.getY(),
								projectileSize[0], projectileSize[1], false, false);
					}
				}
			}

			// Draw boss projectiles
			ArrayList<Projectile> bossProjectiles = (ArrayList<Projectile>) boss.getProjectiles();
			for (int i = 0; i < bossProjectiles.size(); i++) {
				Projectile p = bossProjectiles.get(i);
				if (p.isRock()) {
					glDrawSprite(gl, rockTex, p.getX() - camera.getX(), p.getY() - camera.getY(), spriteSize[0],
							spriteSize[1], !p.isReverse(), false);
				} else {
					glDrawSprite(gl, bossShockwaveTex, p.getX() - camera.getX(), p.getY() - camera.getY(),
							shockwaveSize[0], shockwaveSize[1], !p.isReverse(), false);
				}
			}

			// If the camera and the monkey are within each other, then draw
			// monkey
			if (AABBIntersect(cameraAABB, spriteAABB)) {
				glDrawSprite(gl, monkey.getCurrentTexture(), monkey.getX() - camera.getX(),
						monkey.getY() - camera.getY(), monkey.getWidth(), monkey.getHeight(), monkey.getReverse(),
						monkey.isInvincible());
			}
			
			for(int i = 0; i < monkey.getHealth(); i++){
				glDrawSprite(gl, healthTex,((monkey.getX() - camera.getX()) + (55 *i)),
						30, healthSize[0], healthSize[1], false,
						false);
			}

			if (kbState[KeyEvent.VK_ESCAPE]) {
				shouldExit = true;
			}
			
			while(titleMode){
				System.arraycopy(kbState, 0, kbPrevState, 0, kbState.length);
				glDrawSprite(gl, titleScreen, 0 - camera.getX(),
						0 - camera.getY(), menuSize[0], menuSize[1], false, false);
				
				if(kbState[KeyEvent.VK_ENTER]){
					titleMode = false;
					gameOver = false;
				}
				window.display();
				if (!window.isVisible()) {
					shouldExit = true;
					break;
				}
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
			if (isRed) {
				gl.glColor3f((float) 1.0, (float) 0.0, (float) 0.0);
			} else {
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
