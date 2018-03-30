package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Handlers.Content;
import TileMap.TileMap;

public class FootDust extends MapObject {
	private boolean remove;
	static BufferedImage[] sprites;
	
	public FootDust(TileMap tm, double x, double y) {
		super(tm);
		// TODO Auto-generated constructor stub
		this.x = x;
		this.y = y;
		width =40;
		height =20;
		
		
		

		sprites = Content.FootDust[0];
		animation.setFrames(sprites);
		animation.setDelay(3);
		
		
	}
	public boolean isFacingRight() { return facingRight; }
	
	public void update() {
		
		// check if done flinching
		
		animation.update();
		if(animation.hasPlayedOnce()) {
			remove = true;
		}

		
	}
	public boolean shouldRemove() { return remove; }
	
	public void draw(Graphics2D g) {
		
		setMapPosition();
		super.draw(g);
				
	}
	
}
