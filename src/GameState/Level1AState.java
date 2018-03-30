package GameState;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

//import Audio.JukeBox;
import Entity.Enemy;
import Entity.EnemyProjectile;
import Entity.Explosion;
import Entity.HUD;
import Entity.Player;
import Entity.PlayerSave;
import Entity.Teleport;
import Entity.Title;
import Entity.Enemies.Eagle;
import Entity.Enemies.Fox;
import Entity.Enemies.Rat;
import Entity.Enemies.Snake;
import Handlers.Keys;
import Main.GamePanel;
import TileMap.Background;
import TileMap.TileMap;

public class Level1AState extends GameState {
	
	private Background sky;
	private Background clouds;
	private Background mountains;
	
	private Player player;
	private TileMap tileMap;
	private ArrayList<Enemy> enemies;
	private ArrayList<EnemyProjectile> eprojectiles;
	private ArrayList<Explosion> explosions;
	
	private HUD hud;
	private BufferedImage hageonText;
	private Title title;
	private Title subtitle;
	private Teleport teleport;
	
	// events
	private boolean blockInput = false;
	private int eventCount = 0;
	private boolean eventStart;
	private ArrayList<Rectangle> tb;
	private boolean eventFinish;
	private boolean eventDead;
	
	public Level1AState(GameStateManager gsm) {
		super(gsm);
		init();
	}
	
	public void init() {
		
		// backgrounds
		sky = new Background("/Backgrounds/sky.gif", 0);
		clouds = new Background("/Backgrounds/clouds.gif", 0.1);
		mountains = new Background("/Backgrounds/mountains.gif", 0.2);
		
		// tilemap
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/Stage1_tiles.png");
		tileMap.loadMap("/Maps/Stage1.map");
		tileMap.setPosition(20, 0);
		/*tileMap.setBounds(
			tileMap.getWidth() - 1 * tileMap.getTileSize(),
			tileMap.getHeight() - 2 * tileMap.getTileSize(),
			0, 0
		);*/
		tileMap.setTween(1);
		// teleport
		teleport = new Teleport(tileMap);
		teleport.setPosition(5054, 136);
		// player
		player = new Player(tileMap);
		player.setPosition(60, 200);
		player.setHealth(PlayerSave.getHealth());
		player.setLives(PlayerSave.getLives());
		player.setTime(PlayerSave.getTime());

		// enemies
		enemies = new ArrayList<Enemy>();
		eprojectiles = new ArrayList<EnemyProjectile>();
		populateEnemies();
		

		// init player
		player.init(enemies);
		
		// explosions
		explosions = new ArrayList<Explosion>();
		
		// hud
		hud = new HUD(player);
		
		// title and subtitle
		try {
			hageonText = ImageIO.read(
				getClass().getResourceAsStream("/HUD/HageonTemple.gif")
			);
			title = new Title(hageonText.getSubimage(0, 0, 178, 20));
			title.sety(60);
			subtitle = new Title(hageonText.getSubimage(0, 20, 82, 13));
			subtitle.sety(85);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		
		
		// start event
		eventStart = true;
		tb = new ArrayList<Rectangle>();
		eventStart();
		
		// sfx
		//JukeBox.load("/SFX/teleport.mp3", "teleport");
		//JukeBox.load("/SFX/explode.mp3", "explode");
		//JukeBox.load("/SFX/enemyhit.mp3", "enemyhit");
		
		// music
		//JukeBox.load("/Music/level1.mp3", "level1");
		//JukeBox.loop("level1", 600, JukeBox.getFrames("level1") - 2200);
		
	}
	
	private void populateEnemies() {
		enemies.clear();
		
		Fox fox;
		Eagle eg;
		Rat rat;
		Snake sk;
		
		sk = new Snake(tileMap);
		sk.setPosition(460, 2760);
		enemies.add(sk);
		sk = new Snake(tileMap);
		sk.setPosition(480, 180);
		enemies.add(sk);
		sk = new Snake(tileMap);
		sk.setPosition(710, 190);
		enemies.add(sk);		
		fox = new Fox(tileMap);
		fox.setPosition(900, 150);
		enemies.add(fox);
		fox = new Fox(tileMap);
		fox.setPosition(1000, 150);
		enemies.add(fox);
		fox = new Fox(tileMap);
		fox.setPosition(1360, 240);
		enemies.add(fox);
		eg = new Eagle(tileMap);
		eg.setPosition(2100, 50);
		enemies.add(eg);
		eg = new Eagle(tileMap);
		eg.setPosition(2860, 188);
		enemies.add(eg);
		rat = new Rat(tileMap);
		rat.setPosition(3200,150);
		enemies.add(rat);
		rat = new Rat(tileMap);
		rat.setPosition(3470,230);
		enemies.add(rat);
		eg = new Eagle(tileMap);
		eg.setPosition(3733, 240);
		enemies.add(eg);
		eg = new Eagle(tileMap);
		eg.setPosition(3868, 247);
		enemies.add(eg);
		rat = new Rat(tileMap);
		rat.setPosition(4006,120);
		enemies.add(rat);
		rat = new Rat(tileMap);
		rat.setPosition(4077,113);
		enemies.add(rat);
		sk = new Snake(tileMap);
		sk.setPosition(4153, 199);
		enemies.add(sk);
		sk = new Snake(tileMap);
		sk.setPosition(4219, 72);
		enemies.add(sk);
		eg = new Eagle(tileMap);
		eg.setPosition(4135, 66);
		enemies.add(eg);
		rat = new Rat(tileMap);
		rat.setPosition(4698,231);
		enemies.add(rat);
		eg = new Eagle(tileMap);
		eg.setPosition(4803, 129);
		enemies.add(eg);
	}
	
	public void update() {
		
		// check keys
		handleInput();
		
		// check if end of level event should start
		if(teleport.contains(player)) {
			eventFinish = blockInput = true;
		}
		
		// check if player dead event should start
		if(player.getHealth() == 0 || player.gety() > tileMap.getHeight()) {
			eventDead = blockInput = true;
		}
		
		// play events
		if(eventStart) eventStart();
		if(eventDead) eventDead();
		if(eventFinish) eventFinish();
		
		// move title and subtitle
		if(title != null) {
			title.update();
			if(title.shouldRemove()) title = null;
		}
		if(subtitle != null) {
			subtitle.update();
			if(subtitle.shouldRemove()) subtitle = null;
		}
		
		// move backgrounds
		clouds.setPosition(tileMap.getx(), tileMap.gety());
		mountains.setPosition(tileMap.getx(), tileMap.gety());
		
		// update player
		player.update();
		
		// update tilemap
		tileMap.setPosition(
			GamePanel.WIDTH / 2 - player.getx(),
			GamePanel.HEIGHT / 2 - player.gety()
		);
		tileMap.update();
		tileMap.fixBounds();
		
		// update enemies
		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			e.update();
			if(e.isDead()) {
				enemies.remove(i);
				i--;
				explosions.add(new Explosion(tileMap, e.getx(), e.gety()));
			}
		}
		
		// update enemy projectiles
		for(int i = 0; i < eprojectiles.size(); i++) {
			EnemyProjectile ep = eprojectiles.get(i);
			ep.update();
			if(ep.shouldRemove()) {
				eprojectiles.remove(i);
				i--;
			}
		}
		
		// update explosions
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).update();
			if(explosions.get(i).shouldRemove()) {
				explosions.remove(i);
				i--;
			}
		}
		
		// update teleport
		teleport.update();
		
	}
	
	public void draw(Graphics2D g) {
		
		// draw background
		sky.draw(g);
		clouds.draw(g);
		mountains.draw(g);
		
		// draw tilemap
		tileMap.draw(g);
		
		// draw enemies
		for(int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(g);
		}
		
		// draw enemy projectiles
		for(int i = 0; i < eprojectiles.size(); i++) {
			eprojectiles.get(i).draw(g);
		}
		
		// draw explosions
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).draw(g);
		}
		
		// draw player
		player.draw(g);
		
		// draw teleport
		teleport.draw(g);
		
		// draw hud
		hud.draw(g);
		
		// draw title
		if(title != null) title.draw(g);
		if(subtitle != null) subtitle.draw(g);
		
		// draw transition boxes
		g.setColor(java.awt.Color.BLACK);
		for(int i = 0; i < tb.size(); i++) {
			g.fill(tb.get(i));
		}
		
	}
	
	public void handleInput() {
		if(Keys.isPressed(Keys.ESCAPE)) gsm.setPaused(true);
		if(blockInput || player.getHealth() == 0) return;
		player.setUp(Keys.keyState[Keys.UP]);
		player.setLeft(Keys.keyState[Keys.LEFT]);
		player.setDown(Keys.keyState[Keys.DOWN]);
		player.setRight(Keys.keyState[Keys.RIGHT]);
		player.setJumping(Keys.keyState[Keys.BUTTON1]);

		if(Keys.isPressed(Keys.BUTTON3)) player.setAttacking();
		if(Keys.isPressed(Keys.BUTTON4)) player.setCharging();
	}

///////////////////////////////////////////////////////
//////////////////// EVENTS
///////////////////////////////////////////////////////
	
	// reset level
	private void reset() {
		player.reset();
		player.setPosition(300, 161);
		populateEnemies();
		blockInput = true;
		eventCount = 0;
		tileMap.setShaking(false, 0);
		eventStart = true;
		eventStart();
		title = new Title(hageonText.getSubimage(0, 0, 178, 20));
		title.sety(60);
		subtitle = new Title(hageonText.getSubimage(0, 33, 91, 13));
		subtitle.sety(85);
	}
	
	// level started
	private void eventStart() {
		eventCount++;
		if(eventCount == 1) {
			tb.clear();
			tb.add(new Rectangle(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT / 2));
			tb.add(new Rectangle(0, 0, GamePanel.WIDTH / 2, GamePanel.HEIGHT));
			tb.add(new Rectangle(0, GamePanel.HEIGHT / 2, GamePanel.WIDTH, GamePanel.HEIGHT / 2));
			tb.add(new Rectangle(GamePanel.WIDTH / 2, 0, GamePanel.WIDTH / 2, GamePanel.HEIGHT));
		}
		if(eventCount > 1 && eventCount < 60) {
			tb.get(0).height -= 4;
			tb.get(1).width -= 6;
			tb.get(2).y += 4;
			tb.get(3).x += 6;
		}
		if(eventCount == 30) title.begin();
		if(eventCount == 60) {
			eventStart = blockInput = false;
			eventCount = 0;
			subtitle.begin();
			tb.clear();
		}
	}
	
	// player has died
	private void eventDead() {
		eventCount++;
		if(eventCount == 1) {
			player.setDead();
			player.stop();
		}
		if(eventCount == 60) {
			tb.clear();
			tb.add(new Rectangle(
				GamePanel.WIDTH / 2, GamePanel.HEIGHT / 2, 0, 0));
		}
		else if(eventCount > 60) {
			tb.get(0).x -= 6;
			tb.get(0).y -= 4;
			tb.get(0).width += 12;
			tb.get(0).height += 8;
		}
		if(eventCount >= 120) {
			if(player.getLives() == 0) {
				gsm.setState(GameStateManager.MENUSTATE);
			}
			else {
				eventDead = blockInput = false;
				eventCount = 0;
				player.loseLife();
				reset();
			}
		}
	}
	
	// finished level
	private void eventFinish() {
		eventCount++;
		if(eventCount == 1) {
			//JukeBox.play("teleport");
			player.setTeleporting(true);
			player.stop();
		}
		else if(eventCount == 120) {
			tb.clear();
			tb.add(new Rectangle(
				GamePanel.WIDTH / 2, GamePanel.HEIGHT / 2, 0, 0));
		}
		else if(eventCount > 120) {
			tb.get(0).x -= 6;
			tb.get(0).y -= 4;
			tb.get(0).width += 12;
			tb.get(0).height += 8;
			//JukeBox.stop("teleport");
		}
		if(eventCount == 180) {
			PlayerSave.setHealth(player.getHealth());
			PlayerSave.setLives(player.getLives());
			PlayerSave.setTime(player.getTime());
			gsm.setState(GameStateManager.LEVEL1ASTATE);
		}
		
	}

}