import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SpaceInvaders extends JFrame {

	public SpaceInvaders() {
		super("My Game");

		GamePanel game = new GamePanel();
		add(game);

		pack();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public static void main(String[] args) {
		new SpaceInvaders();
	}
}

class GamePanel extends JPanel implements ActionListener {

	boolean gameOver = false;

	int enemyDir = 1;
	int enemySpeed = 2;

	int x = 300;
	boolean[] keys;

	Timer timer;

	int bulletX, bulletY;
	boolean shooting = false;

	int rows = 3;
	int cols = 5;

	Enemy[][] enemies;
	Barrier[] barriers;

	public GamePanel() {

		setPreferredSize(new Dimension(800, 600));

		keys = new boolean[KeyEvent.KEY_LAST + 1];
		setFocusable(true);
		requestFocus();

		addKeyListener(new KeyAdapter() {

			public void keyPressed(KeyEvent e) {
				keys[e.getKeyCode()] = true;

				if (e.getKeyCode() == KeyEvent.VK_SPACE && !shooting) {
					shooting = true;
					bulletX = x + 100;
					bulletY = 600;
				}
			}

			public void keyReleased(KeyEvent e) {
				keys[e.getKeyCode()] = false;
			}
		});

		// enemies
		enemies = new Enemy[rows][cols];
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				enemies[r][c] = new Enemy(100 + c * 120, 50 + r * 80);
			}
		}

		// barriers
		barriers = new Barrier[3];
		for (int i = 0; i < 3; i++) {
			barriers[i] = new Barrier(150 + i * 200, 450);
		}

		timer = new Timer(20, this);
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		move();
		repaint();
	}

	public void move() {

		if (gameOver) return;

		// player
		if (keys[KeyEvent.VK_LEFT]) x -= 5;
		if (keys[KeyEvent.VK_RIGHT]) x += 5;

		if (x < 0) x = 0;
		if (x > 800 - 200) x = 800 - 200;

		// enemies move
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				enemies[r][c].move(enemyDir, enemySpeed);
			}
		}

		// border check
		boolean hitEdge = false;

		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				if (enemies[r][c].alive) {
					if (enemies[r][c].x <= 0 || enemies[r][c].x + 60 >= 800) {
						hitEdge = true;
					}
				}
			}
		}

		if (hitEdge) {
			enemyDir *= -1;

			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					enemies[r][c].y += 20;
				}
			}
		}

		// bullet
		if (shooting) {
			bulletY -= 10;
			if (bulletY < 0) shooting = false;
		}

		// hit enemies
		if (shooting) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					if (enemies[r][c].hit(bulletX, bulletY)) {
						shooting = false;
					}
				}
			}
		}

		// lose condition
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				if (enemies[r][c].alive &&
				    enemies[r][c].y + 40 >= 600 - 50) {
					gameOver = true;
				}
			}
		}

		// hit barriers
		if (shooting) {
			for (int i = 0; i < barriers.length; i++) {
				if (barriers[i].hit(bulletX, bulletY)) {
					shooting = false;
				}
			}
		}
	}

	@Override
	public void paint(Graphics g) {

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 800, 600);



		// enemies
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				enemies[r][c].draw(g);
			}
		}

		// barriers
		for (int i = 0; i < barriers.length; i++) {
			barriers[i].draw(g);
		}

		// game over
		if (gameOver) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", Font.BOLD, 40));
			g.drawString("GAME OVER", 250, 300);
		}

		// bullet
		if (shooting) {
			g.setColor(Color.WHITE);
			g.fillRect(bulletX, bulletY, 5, 10);
		}
		// player
		g.setColor(Color.GREEN);
		g.fillRect(x+50, 600-25+5, 100, 25);
		g.fillRect(x+50+15, 600-32+5, 70, 30);
		g.fillRect(x+93, 600-45+5, 70/4, 30);
		g.fillRect(x+93, 600-45, 70/4, 30);
		g.fillRect(x+99, 600-50+5, 5, 10);
	}
}