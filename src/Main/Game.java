package Main;

import javax.swing.JFrame;


@SuppressWarnings("serial")
public class Game extends JFrame {

	public Game()  {
		// TODO �ڵ� ������ ������ ����	
		// ����
		super("Crown");
		
		this.setContentPane(new GamePanel());
		// â ���� ����
		this.setResizable(false);
		// ��ġ
		this.setLocation(600, 200);
		// ũ��
		pack(); //this.setSize(400, 300);
		// �����ֱ�
		this.setVisible(true);
		// �����ڵ�
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		// TODO �ڵ� ������ �޼ҵ� ����
		new Game();
	}
}
