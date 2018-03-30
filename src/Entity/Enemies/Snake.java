package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.sun.javafx.geom.Rectangle;
import com.sun.prism.paint.Color;

import Entity.Enemy;
import Entity.Player;
import Handlers.Content;
import Main.GamePanel;
import TileMap.TileMap;

public class Snake extends Enemy {
	
	private BufferedImage[] sprites;
	private boolean active;
	
	public Snake(TileMap tm) {
		
		super(tm);
		
		
		health = maxHealth = 1;
		
		width = 20;
		height = 15;
		cwidth = 18;
		cheight = 15;		
		damage = 1;
		moveSpeed = 1.0;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;

		
		sprites = Content.Snake[0];
		
		animation.setFrames(sprites);
		animation.setDelay(5);
		
		left = true;
		facingRight = false;
		
		
		
	}
	
	private void getNextPosition() {
		if(left) dx = -moveSpeed;
		else if(right) dx = moveSpeed;
		else dx = 0;
		if(falling) {
			dy += fallSpeed;
			if(dy > maxFallSpeed) dy = maxFallSpeed;
		}
		
	}
	
	public void update() {
		
		
		
		// check if done flinching
		if(flinching) {
			flinchCount++;
			if(flinchCount == 6) flinching = false;
		}
		
		getNextPosition();
		checkTileMapCollision();
		calculateCorners(x, ydest + 1);
		if(!bottomLeft) {
			left = false;
			right = facingRight = true;
		}
		if(!bottomRight) {
			left = true;
			right = facingRight = false;
		}
		setPosition(xtemp, ytemp);
		
		if(dx == 0) {
			left = !left;
			right = !right;
			facingRight = !facingRight;
		}
		
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
