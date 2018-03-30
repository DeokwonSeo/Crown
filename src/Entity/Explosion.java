package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Handlers.Content;
import TileMap.TileMap;

public class Explosion extends MapObject {
	
	private BufferedImage[] sprites;
	
	private boolean remove;
	
	public Explosion(TileMap tm, int x, int y) {
		
		super(tm);
		
		this.x = x;
		this.y = y;
		
		width = 30;
		height = 30;
		

		sprites = Content.Explosion[0];
		
		animation.setFrames(sprites);
		animation.setDelay(6);

	}
	public void update() {
		animation.update();
		if(animation.hasPlayedOnce()) {
			remove = true;
		}
	}
	public boolean shouldRemove() { return remove; }
	
	public void draw(Graphics2D g) {
		setMapPosition();
			g.drawImage(
				animation.getImage(),
				 (int) (x + xmap - width / 2),
				 (int)(y + ymap - height / 2),
				null
			);
		
	}
	
}