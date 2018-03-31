package Handlers;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Content {

	//폭파효과
	public static BufferedImage[][] Explosion = load("/Sprites/Enemies/enemy-deadth40_41.png", 40, 40);
	//점프이미지
	public static BufferedImage[][] FootDust = load("/Sprites/Player/foot_dust.png",40,20);
	//공격이미지
	public static BufferedImage[][] AttackEffect = load("/Sprites/Player/hit.png",15,15);
	//몬스터_독수리
	public static BufferedImage[][] Eagle = load("/Sprites/Enemies/eagle160_40.png", 40, 40);
	//몬스터_개구리
	public static BufferedImage[][] Forg = load("/Sprites/Enemies/forg36_36.png", 36, 36);
	//몬스터_여우
	public static BufferedImage[][] Fox = load("/Sprites/Enemies/fox33_25.png", 33, 32);
	//몬스터_쥐
	public static BufferedImage[][] Rat = load("/Sprites/Enemies/rat36_28.png", 36, 28);
	//몬스터_뱀
	public static BufferedImage[][] Snake = load("/Sprites/Enemies/snake80_15.png", 20, 15);
	//개구리_미사일
	public static BufferedImage[][] DarkEnergy = load("/Sprites/Enemies/DarkEnergy.gif", 20, 20);
	
	
	// 이미지 배열 출력
	/**
	 * 
	 * @param s
	 * @param w
	 * @param h
	 * @return
	 */
	public static BufferedImage[][] load(String s, int w, int h) {
		BufferedImage[][] ret;
		try {
			BufferedImage spritesheet = ImageIO.read(Content.class.getResourceAsStream(s));// 객체제목
				int width = spritesheet.getWidth() / w; // 그림 넓이
				int height = spritesheet.getHeight() / h;// 그림 높이
				ret = new BufferedImage[height][width];
				for(int i = 0; i < height; i++) {
					for(int j = 0; j < width; j++) {
						ret[i][j] = spritesheet.getSubimage(j * w, i * h, w, h);
					}
				}
			return ret;	
		
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("Error loading graphics.");
			System.exit(0);
		}
		return null;
	}
	
}
