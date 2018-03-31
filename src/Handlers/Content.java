package Handlers;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Content {

	//����ȿ��
	public static BufferedImage[][] Explosion = load("/Sprites/Enemies/enemy-deadth40_41.png", 40, 40);
	//�����̹���
	public static BufferedImage[][] FootDust = load("/Sprites/Player/foot_dust.png",40,20);
	//�����̹���
	public static BufferedImage[][] AttackEffect = load("/Sprites/Player/hit.png",15,15);
	//����_������
	public static BufferedImage[][] Eagle = load("/Sprites/Enemies/eagle160_40.png", 40, 40);
	//����_������
	public static BufferedImage[][] Forg = load("/Sprites/Enemies/forg36_36.png", 36, 36);
	//����_����
	public static BufferedImage[][] Fox = load("/Sprites/Enemies/fox33_25.png", 33, 32);
	//����_��
	public static BufferedImage[][] Rat = load("/Sprites/Enemies/rat36_28.png", 36, 28);
	//����_��
	public static BufferedImage[][] Snake = load("/Sprites/Enemies/snake80_15.png", 20, 15);
	//������_�̻���
	public static BufferedImage[][] DarkEnergy = load("/Sprites/Enemies/DarkEnergy.gif", 20, 20);
	
	
	// �̹��� �迭 ���
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
			BufferedImage spritesheet = ImageIO.read(Content.class.getResourceAsStream(s));// ��ü����
				int width = spritesheet.getWidth() / w; // �׸� ����
				int height = spritesheet.getHeight() / h;// �׸� ����
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
