package Main;

import javax.swing.JFrame;


@SuppressWarnings("serial")
public class Game extends JFrame {

	public Game()  {
		// TODO 자동 생성된 생성자 스텁	
		// 제목
		super("Crown");
		
		this.setContentPane(new GamePanel());
		// 창 조절 금지
		this.setResizable(false);
		// 위치
		this.setLocation(600, 200);
		// 크기
		pack(); //this.setSize(400, 300);
		// 보여주기
		this.setVisible(true);
		// 종료코드
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		// TODO 자동 생성된 메소드 스텁
		new Game();
	}
}
