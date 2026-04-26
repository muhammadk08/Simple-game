import java.awt.*;

public class EnemyLevel {

	int rows = 3;
	int cols = 5;

	Enemy[][] enemies;

	public EnemyLevel() {
		enemies = new Enemy[rows][cols];

		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				enemies[r][c] = new Enemy(100 + c * 120, 50 + r * 80);
			}
		}
	}

	public void move(int dir, int speed) {
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				enemies[r][c].move(dir, speed);
			}
		}
	}

	public void draw(Graphics g) {
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				enemies[r][c].draw(g);
			}
		}
	}

	public boolean allDead() {
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				if (enemies[r][c].alive) return false;
			}
		}
		return true;
	}

	public boolean hit(int bx, int by) {
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				if (enemies[r][c].hit(bx, by)) {
					return true;
				}
			}
		}
		return false;
	}
}