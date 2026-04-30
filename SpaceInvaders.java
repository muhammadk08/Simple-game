import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class SpaceInvaders extends JFrame {
	public SpaceInvaders() {
		super("My Game"); // window title

		GamePanel game = new GamePanel(); // create game panel
		add(game); // put panel inside window

		pack(); // fit window to panel size

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close app on X
		setVisible(true); // show window
	}
	public static void main(String[] args) {
		new SpaceInvaders(); // start game
	}
}

class GamePanel extends JPanel implements ActionListener {

	boolean gameOver = false; // not really used anymore (state replaces it)

	int enemyDir = 1; // 1 = right, -1 = left
	int enemySpeed = 2; // how fast enemies move

	int x = 300; // player X position
	boolean[] keys; // stores which keys are pressed

	Timer timer;

	int bulletX, bulletY; // player bullet position
	boolean shooting = false; // is player currently shooting?

	int rows = 3;
	int cols = 5;

	Enemy[][] enemies; // grid of enemies
	Barrier[] barriers; // shields

	EnemyBullet enemyBullet; // enemy bullet object
	int lives; // player lives
	Image heart; // heart image

	Boss boss; // boss object
	BossBullet bossBullet; // boss bullet
	boolean bossActive; // is boss fight active?

	int state = 0;

	// 0 = menu
	// 1 = playing
	// 2 = game over
	// 3 = win
	public GamePanel() {
		heart = new ImageIcon("heart.png").getImage();

		setPreferredSize(new Dimension(800, 600));

		keys = new boolean[KeyEvent.KEY_LAST + 1];
		setFocusable(true); // By default, the JFrame has focus. This means when
		requestFocus(); // we press keys they go to it.

		addKeyListener(new KeyAdapter() {

			public void keyPressed(KeyEvent e) {

				// START GAME
				if (state == 0 && e.getKeyCode() == KeyEvent.VK_ENTER) {
					state = 1;
					resetGame();
				}

				// restart
				if ((state == 2 || state == 3) && e.getKeyCode() == KeyEvent.VK_R) {
					state = 1;
					resetGame();
				}

				// normal controls only if playing
				if (state == 1) {
					keys[e.getKeyCode()] = true;

					if (e.getKeyCode() == KeyEvent.VK_SPACE && !shooting) {
						shooting = true;
						bulletX = x + 100;
						bulletY = 600;
					}
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

		// bariers
		barriers = new Barrier[3];
		for (int i = 0; i < 3; i++) {
			barriers[i] = new Barrier(150 + i * 200, 450);
		}

		timer = new Timer(20, this);
		timer.start();
		enemyBullet = new EnemyBullet();
		boss = new Boss();
		bossBullet = new BossBullet();
	}

	public void resetGame() {

		lives = 5;
		x = 300;

		// reset enemies COMPLETELY
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				enemies[r][c].alive = true;
				enemies[r][c].x = 100 + c * 120;
				enemies[r][c].y = 50 + r * 80;
			}
		}

		enemyDir = 1; //

		// reset enemy bullet
		enemyBullet = new EnemyBullet();

		// reset boss
		boss = new Boss();
		bossBullet = new BossBullet();
		bossActive = false;
		// ------------------------
		// to go to boss

		// for (int r = 0; r < rows; r++) {
		// for (int c = 0; c < cols; c++) {
		// enemies[r][c].alive = false;
		// }
		// }
		// ------------------------
		// reset barriers
		for (int i = 0; i < barriers.length; i++) {
			barriers[i] = new Barrier(150 + i * 200, 450);
		}

		shooting = false;

		// reset keys
		keys = new boolean[KeyEvent.KEY_LAST + 1];
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		move();
		repaint();
	}

	public void move() {
		if (state != 1)
			return;
		if (gameOver)
			return;

		// player
		if (keys[KeyEvent.VK_LEFT])
			x -= 5;
		if (keys[KeyEvent.VK_RIGHT])
			x += 5;

		if (x < 0)
			x = 0;
		if (x > 800 - 200)
			x = 800 - 200;

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
			if (bulletY < 0)
				shooting = false;
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
					state = 2;
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
		// enemy randomly shoots
		if (!enemyBullet.active && Math.random() < 0.02) {

			int r = (int) (Math.random() * rows);
			int c = (int) (Math.random() * cols);

			if (enemies[r][c].alive) {
				enemyBullet.shoot(
						enemies[r][c].x + 30,
						enemies[r][c].y + 40);
			}
		}
		enemyBullet.move();
		if (enemyBullet.hitPlayer(x)) {
			lives--;

			if (lives <= 0) {
				state = 2;
			}
		}
		// enemy bullet hits barriers
		if (enemyBullet.active) {
			for (int i = 0; i < barriers.length; i++) {
				if (barriers[i].hit(enemyBullet.x, enemyBullet.y)) {
					enemyBullet.active = false;
				}
			}
		}
		if (!boss.isAlive()) {
			state = 3; // ends game when boss dies
		}
		boolean allDead = true;

		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				if (enemies[r][c].alive) {
					allDead = false;
				}
			}
		}

		if (allDead)
			bossActive = true;

		// boss
		if (bossActive && boss.isAlive()) {
			boss.move();

			if (!bossBullet.active && Math.random() < 0.03) {
				bossBullet.shoot(boss.x + 100, boss.y + 100);
			}

			bossBullet.move();
			// boss bullet hits barriers
			if (bossBullet.active) {
				for (int i = 0; i < barriers.length; i++) {
					if (barriers[i].hit(bossBullet.x, bossBullet.y)) {
						bossBullet.active = false;
					}
				}
			}

			if (bossBullet.hitPlayer(x)) {
				lives--;
				if (lives <= 0)
					state = 2;
			}

			if (shooting && boss.hit(bulletX, bulletY)) {
				shooting = false;
			}
		}
	}

	@Override
	public void paint(Graphics g) {

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 800, 600);
		// menu
		if (state == 0) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", Font.BOLD, 40));
			g.drawString("SPACE INVADERS", 180, 250);

			g.setFont(new Font("Arial", Font.PLAIN, 20));
			g.drawString("Press ENTER to Start", 260, 320);
			return;
		}
		// game over
		if (state == 2) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", Font.BOLD, 40));
			g.drawString("GAME OVER", 250, 280);

			g.setFont(new Font("Arial", Font.PLAIN, 20));
			g.drawString("Press R to Restart", 260, 330);
			return;
		}
		if (state == 3) {
			g.setColor(Color.GREEN);
			g.setFont(new Font("Arial", Font.BOLD, 40));
			g.drawString("YOU WON", 270, 280);

			g.setFont(new Font("Arial", Font.PLAIN, 20));
			g.drawString("Press R to Restart", 260, 330);
			return;
		}
		// draw lives as hearts
		for (int i = 0; i < lives; i++) {
			g.drawImage(heart, 20 + i * 40, 20, 30, 30, null);
		}
		// boss
		if (bossActive && boss.isAlive()) {
			boss.draw(g);
			bossBullet.draw(g);
		}
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

		if (bossActive && boss.isAlive()) {

			// bar background (full)
			g.setColor(Color.DARK_GRAY);
			g.fillRect(20, 100, 20, 300);

			// health amount
			int barHeight = (int) (300.0 * boss.health / boss.maxHealth);

			// green health (shrinks DOWN)
			g.setColor(Color.GREEN);
			g.fillRect(20, 100 + (300 - barHeight), 20, barHeight);
		}

		// bullet
		if (shooting) {
			g.setColor(Color.WHITE);
			g.fillRect(bulletX, bulletY, 5, 10);
		}
		enemyBullet.draw(g);
		// player
		g.setColor(Color.GREEN);
		g.fillRect(x + 50, 600 - 25 + 5, 100, 25);
		g.fillRect(x + 50 + 15, 600 - 32 + 5, 70, 30);
		g.fillRect(x + 93, 600 - 45 + 5, 70 / 4, 30);
		g.fillRect(x + 93, 600 - 35, 70 / 4, 30);
		g.fillRect(x + 99, 600 - 50 + 5, 5, 10);
	}

}