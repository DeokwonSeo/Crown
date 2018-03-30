package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Entity.Enemy;
import Handlers.Content;
import TileMap.TileMap;


public class Eagle extends Enemy {
	
	private BufferedImage[] idleSprites;
	
	private int tick;
	private double a;
	private double b;
	
	public Eagle(TileMap tm) {
		
		super(tm);
		
		health = maxHealth = 2;
		
		width = 40;
		height = 40;
		cwidth = 35;
		cheight = 35;
		
		damage = 1;
		moveSpeed = 5;
		
		idleSprites = Content.Eagle[0];
		
		animation.setFrames(idleSprites);
		animation.setDelay(10);
		
		tick = 0;
		a = Math.random() * 0.05 + 0.05;
		b = Math.random() * 0.051 + 0.05;
		left = false;
		facingRight = false;
	}
	
	public void update() {
		
		// check if done flinching
		if(flinching) {
			flinchCount++;
			if(flinchCount == 6) flinching = false;
		}
		
		tick++;
		x = Math.sin(a * tick) + x;
		y = Math.sin(b * tick) + y;
		
		// update animation
		animation.update();
		
	}
	
	public void draw(Graphics2D g) {
		
		if(flinching) {
			if(flinchCount == 0 || flinchCount == 2) return;
		}
		
		super.draw(g);
		
	}
	
}
