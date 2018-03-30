package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Handlers.Content;
import TileMap.TileMap;

public class AttackEffect extends MapObject {

	private boolean remove;
	static BufferedImage[] sprites;
	private int tick;
	private double a;
	private double b;
	
	
	public AttackEffect(TileMap tm, double x, double y) {
		super(tm);
		// TODO Auto-generated constructor stub
		this.x =x;
		this.y =y;
		width =5;
		height =5;
		cwidth=5;
		cheight=5;
		sprites = Content.AttackEffect[0];
		animation.setFrames(sprites);
		animation.setDelay(2);
		facingRight=true;
		//tick = 0;
		//a = Math.random()  + 0.05;
		//b = Math.random() + 0.05;
	}
	public void update() {
		
		// check if done flinching
		
		animation.update();
		if(animation.hasPlayedOnce()) {
			remove = true;
		}
		//tick++;
		//x = Math.sin(a * tick) + x;
		//y = Math.sin(b * tick) + y;
		
		
	}
	public boolean shouldRemove() { return remove; }
	
	public void draw(Graphics2D g) {
		
		setMapPosition();
		super.draw(g);
	}
}
