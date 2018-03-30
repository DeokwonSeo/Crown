package Entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import TileMap.TileMap;


public class Player extends MapObject {
	
	// references
	private ArrayList<Enemy> enemies;
	private ArrayList<FootDust> footDust;
	private ArrayList<AttackEffect> attackEffect;
	// player stuff
	private int lives;
	private int health;
	private int maxHealth;
	private int damage;
	private int chargeDamage;
	private boolean knockback;
	private boolean flinching;
	private long flinchCount;
	private int score;
	private boolean doubleJump;
	private boolean alreadyDoubleJump;
	private double doubleJumpStart;

	private long time;
	
	// actions
	private boolean dashing;
	private boolean attacking;
	private boolean upattacking;
	private boolean charging;
	private int chargingTick;
	private boolean teleporting;
	
	// animations
	private ArrayList<BufferedImage[]> sprites;
	private final int[] NUMFRAMES = {
	//대기,걷기,어택,점프,떨어짐,대쉬,죽음,피격,이동,상어택
		 2,   4,   4,   2,     1,   6,   3,   3,   4,    4// 행동에 따른 프레임 개수
	};
	private final int[] FRAMEWIDTHS = { // player 사진 넓이 크기
		20,  20,  55,  20,    20,  20,  20,  20,  15,   20
	};
	
	private final int[] FRAMEHEIGHTS = { // player 사진 높이 크기
	    25,  25,  25,  25,    25,  25,  25,  25,  25,   45
	};
	
	private final int[] SPRITEDELAYS = {
		 10,  3,   3,  10,     5,   1,  15,   3,   15,    3
	};
	
	
	private Rectangle ar;
	private Rectangle aur;
	private Rectangle cr;
	
	// animation actions
	private static final int IDLE = 0;		
	private static final int WALKING = 1;
	private static final int ATTACKING  = 2;
	private static final int JUMPING = 3;	
	private static final int FALLING = 4;
	private static final int CHARGING = 5;
	private static final int DEAD = 6;
	private static final int KNOCKBACK = 7;
	private static final int TELEPORTING = 8;
	private static final int UPATTACKING = 9;
	
	// emotes
	private BufferedImage confused;
	private BufferedImage surprised;
	public static final int NONE = 0;
	public static final int CONFUSED = 1;
	public static final int SURPRISED = 2;
	private int emote = NONE;

	
	public Player(TileMap tm) {
		
		super(tm);
		
		ar = new Rectangle(0, 0, 0, 0);
		ar.width = 20;
		ar.height =45;
		aur = new Rectangle((int)x - 15, (int)y - 45, 30, 30);
		cr = new Rectangle(0, 0, 0, 0);
		cr.width = 20;
		cr.height = 20;
		
		width = 20;//왼쪽을 바라볼때 넓이
		height = 28;//왼쪽을 바라볼때 높이
		cwidth = 15;
		cheight = 24;
		
		moveSpeed = 1.6;
		maxSpeed = 1.6;
		stopSpeed = 1.6;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;
		doubleJumpStart = -3;
		
		damage = 2;
		
		facingRight = true;
		
		
		lives = 3;
		health = maxHealth = 3;
		footDust = new ArrayList<FootDust>();
		attackEffect = new ArrayList<AttackEffect>();
		// load sprites
		try {
			
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/Sprites/Player/PlayerSprites.png"
				)
			);
			
			int count = 0;
			sprites = new ArrayList<BufferedImage[]>();
			for(int i = 0; i < NUMFRAMES.length; i++) {
				BufferedImage[] bi = new BufferedImage[NUMFRAMES[i]];
				for(int j = 0; j < NUMFRAMES[i]; j++) {
					bi[j] = spritesheet.getSubimage(
						j * FRAMEWIDTHS[i],
						count,
						FRAMEWIDTHS[i],
						FRAMEHEIGHTS[i]
					);
				}
				sprites.add(bi);
				count += FRAMEHEIGHTS[i];
				
			}
			
			// emotes
			spritesheet = ImageIO.read(getClass().getResourceAsStream(
				"/HUD/Emotes.gif"
			));
			confused = spritesheet.getSubimage(
				0, 0, 14, 17
			);
			surprised = spritesheet.getSubimage(
				14, 0, 14, 17
			);
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		

		
		setAnimation(IDLE);
		
		
	}
	
	public void init(ArrayList<Enemy> enemies) {
		this.enemies = enemies;	
	}
	
	public int getHealth() { return health; }
	public int getMaxHealth() { return maxHealth; }
	
	public void setEmote(int i) {
		emote = i;
	}
	public void setTeleporting(boolean b) { teleporting = b; }
	
	public void setJumping(boolean b) {
		if(knockback) return;
		if(b && !jumping && falling && !alreadyDoubleJump) {
			doubleJump = true;
		}
		jumping = b;
	}
	public void setAttacking() {
		if(knockback) return;
		if(charging) return;
		if(up && !attacking) upattacking = true;
		else attacking = true;
	}
	public void setCharging() {
		if(knockback) return;
		if(!attacking && !upattacking && !charging) {
			charging = true;
			//JukeBox.play("playercharge");
			chargingTick = 0;
		}
	}
	
	public void setDead() {
		health = 0;
		stop();
	}
	
	// 게임 플레이 시간 그린 경로 HUD
	public String getTimeToString() {   
		int minutes = (int) (time / 3600);
		int seconds = (int) ((time % 3600) / 60);
		return seconds < 10 ? minutes + ":0" + seconds : minutes + ":" + seconds;
	}
	public long getTime() { return time; }
	public void setTime(long t) { time = t; }
	public void setHealth(int i) { health = i; }
	public void setLives(int i) { lives = i; }
	public void gainLife() { lives++; }
	public void loseLife() { lives--; }
	public int getLives() { return lives; }
	
	public void increaseScore(int score) {
		this.score += score; 
	}
	
	public int getScore() { return score; }
	
	public void hit(int damage) {
		if(flinching) return;
		//JukeBox.play("playerhit");
		stop();
		health -= damage;
		if(health < 0) health = 0;
		flinching = true;
		flinchCount = 0;
		if(facingRight) dx = -1;
		else dx = 1;
		dy = -3;
		knockback = true;
		falling = true;
		jumping = false;
		
	}
	
	public void reset() {
		health = maxHealth;
		facingRight = true;
		currentAction = -1;
		stop();
	}
	
	public void stop() {
		left = right = up = down = flinching = 
			dashing = jumping = attacking = upattacking = charging = false;
	}
	
	private void getNextPosition() {
		
		if(knockback) {
			dy += fallSpeed * 2;
			if(!falling) knockback = false;
			return;
		}
		
		double maxSpeed = this.maxSpeed;
		if(dashing) maxSpeed *= 1.75;
		
		// movement
		if(left) {
			dx -= moveSpeed;
			if(dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		}
		else if(right) {
			dx += moveSpeed;
			if(dx > maxSpeed) {
				dx = maxSpeed;
			}
		}
		else {
			if(dx > 0) {
				dx -= stopSpeed;
				if(dx < 0) {
					dx = 0;
				}
			}
			else if(dx < 0) {
				dx += stopSpeed;
				if(dx > 0) {
					dx = 0;
				}
			}
		}
		
		// cannot move while attacking, except in air
		if((attacking || upattacking || charging) &&
			!(jumping || falling)) {
			dx = 0;
		}
		
		// charging
		if(charging) {
			chargingTick++;
			if(facingRight)  dx = moveSpeed * (3 - chargingTick * 0.07);
			else dx = -moveSpeed * (3 - chargingTick * 0.07);
			
		}
		
		
		// jumping
		if(jumping && !falling) {
			dy = jumpStart;
			falling = true;
			footDust.add(new FootDust(tileMap, x,y+4));
		
		}
		
		if(doubleJump) {
			dy = doubleJumpStart;
			alreadyDoubleJump = true;
			doubleJump = false;
			footDust.add(new FootDust(tileMap, x,y+4));
		}
		
		if(!falling) alreadyDoubleJump = false;
		
		// falling
		if(falling) {
			dy += fallSpeed;
			if(dy < 0 && !jumping) dy += stopJumpSpeed;
			if(dy > maxFallSpeed) dy = maxFallSpeed;
		}
		
	}
	
	private void setAnimation(int i) { // i에 해당하는 액션을 수행.
		currentAction = i;
		animation.setFrames(sprites.get(currentAction));
		animation.setDelay(SPRITEDELAYS[currentAction]);
		width = FRAMEWIDTHS[currentAction];
		height = FRAMEHEIGHTS[currentAction];
	}
	
	public void update() {
		
		time++;
		// footDust
		for(int i = 0; i < footDust.size(); i++) {
			footDust.get(i).update();
			if(footDust.get(i).shouldRemove()) {
				footDust.remove(i);
				i--;
			}
		}
		// attackEffect
			for(int j = 0; j < attackEffect.size(); j++) {
				attackEffect.get(j).update();
				if(attackEffect.get(j).shouldRemove()) {
					attackEffect.remove(j);
					j--;
				}
			}
		// check teleporting
		
		
		// update position
		boolean isFalling = falling;
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		if(isFalling && !falling) {
			//JukeBox.play("playerlands");
		}
		if(dx == 0) x = (int)x;
		
		// check done flinching
		if(flinching) {
			flinchCount++;
			if(flinchCount > 120) {
				flinching = false;
			}
		}
		
		// check attack finished
		if(currentAction == ATTACKING ||
			currentAction == UPATTACKING) {
			if(animation.hasPlayedOnce()) {
				attacking = false;
				upattacking = false;
				
			}
			
			
		}
		if(currentAction == CHARGING) {
			if(animation.hasPlayed(3)) {
				charging = false;
			}
			cr.y = (int)y - 20;
			if(facingRight) cr.x = (int)x - 15;
			else cr.x = (int)x - 35;
			
		}
		
		// check enemy interaction
		for(int i = 0; i < enemies.size(); i++) {
			
			Enemy e = enemies.get(i);
			
			// check attack
			if(currentAction == ATTACKING &&
					animation.getFrame() == 1 && animation.getCount() == 0) {
				if(e.intersects(ar)) {
					e.hit(damage);
					if(facingRight)attackEffect.add(new AttackEffect(tileMap, x+12, y-8));
					else attackEffect.add(new AttackEffect(tileMap, x-22, y-8));
				}
			}
			
			// check upward attack
			if(currentAction == UPATTACKING &&
					animation.getFrame() == 1 && animation.getCount() == 0) {
				if(e.intersects(aur)) {
					e.hit(damage);
					if(facingRight)attackEffect.add(new AttackEffect(tileMap, x, y-20));
					else attackEffect.add(new AttackEffect(tileMap, x-10, y-20));
				}
			}
			
			// check charging attack
			if(currentAction == CHARGING) {
				if(animation.getCount() == 0) {
					if(e.intersects(cr)) {
						e.hit(chargeDamage);
					}
					/*if(e.intersects(this)) {
						e.hit(chargeDamage);
					}*/
				}
			}
			
			// collision with enemy 데미지 입없을때. 
				// 죽지 않았거나/ 맞지 않았거나/ 차징상태가 아닐때
			if(!e.isDead() && intersects(e) && !charging) {
				hit(e.getDamage());
			}
			
			
		}
		
		// set animation, ordered by priority
		if(teleporting) {
			if(currentAction != TELEPORTING) {
				setAnimation(TELEPORTING);
			}
		}
		else if(knockback) {
			if(currentAction != KNOCKBACK) {
				setAnimation(KNOCKBACK);
			}
		}
		else if(health == 0) {
			if(currentAction != DEAD) {
				setAnimation(DEAD);
			}
		}
		else if(upattacking) {
			if(currentAction != UPATTACKING) {
				//JukeBox.play("playerattack");
				setAnimation(UPATTACKING);
				aur.x = (int)x - 15;
				aur.y = (int)y - 50;
			}
			else {
				if(animation.getFrame() == 2 && animation.getCount() == 0) {
					
				}
			}
		}
		else if(attacking) {
			if(currentAction != ATTACKING) {
				//JukeBox.play("playerattack");
				setAnimation(ATTACKING);
				ar.y = (int)y - 6;
				if(facingRight) ar.x = (int)x + 10;
				else ar.x = (int)x - 40;
			}
			
		}
		else if(charging) {
			if(currentAction != CHARGING) {
				setAnimation(CHARGING);
			}
		}
		else if(dy < 0) {
			if(currentAction != JUMPING) {
				setAnimation(JUMPING);
				
			}
		}
		else if(dy > 0) {
			if(currentAction != FALLING) {
				setAnimation(FALLING);
			}
		}
		else if(left || right) {
			if(currentAction != WALKING) {
				setAnimation(WALKING);
			}
		}
		else if(currentAction != IDLE) {
			setAnimation(IDLE);
		}
		
		animation.update();
		
		
			
		
		
		
		// set direction
		if(!attacking && !upattacking && !charging && !knockback) {
			if(right) facingRight = true;
			if(left) facingRight = false;
		}
		
	}
	
	
	public void draw(Graphics2D g) {
		
		// draw emote
		if(emote == CONFUSED) {
			g.drawImage(confused, (int)(x + xmap - cwidth / 2), (int)(y + ymap - 40), null);
		}
		else if(emote == SURPRISED) {
			g.drawImage(surprised, (int)(x + xmap - cwidth / 2), (int)(y + ymap - 40), null);
		}
		
		// flinch
		if(flinching && !knockback) {
			if(flinchCount % 10 < 5) return;
		}
		// draw player effect
		for(int i = 0; i < footDust.size(); i++) {
		 footDust.get(i).draw(g);
		}
		for(int i = 0; i < attackEffect.size(); i++) {
		 attackEffect.get(i).draw(g);
		}
		
		super.draw(g);
		
	}


	
}