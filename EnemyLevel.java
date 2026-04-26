import java.awt.*;

public class EnemyLevel {

	int rows = 3; // number of rows o
	int cols = 5; 

	Enemy[][] enemies;

	public EnemyLevel() {

		enemies = new Enemy[rows][cols];

		// fill the grid with Enemy objects
		for (int r = 0; r < rows; r++) {    
			for (int c = 0; c < cols; c++) {    
				enemies[r][c] = new Enemy(
					100 + c * 120,  // move right each column
					50 + r * 80     // move down each row
				);
			}
		}
	}

	// MOVE ALL ENEMIES
	public void move(int dir, int speed) {

		// loop through EVERY enemy in the grid
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {

				// each enemy move
				// dir = direction (1 right, -1 left)
				// speed = how fast they move
				enemies[r][c].move(dir, speed);
			}
		}

	}

	// DRAW ALL ENEMIES
	public void draw(Graphics g) {
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {

				// draw each enemy on screen
				enemies[r][c].draw(g);
			}
		}
	}

	// CHECK IF ALL ENEMIES ARE DEAD
	public boolean allDead() {

		// check every enemy
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				if (enemies[r][c].alive) return false;
			}
		}

		return true;

	}

	// CHECK IF BULLET HIT ANY ENEMY
	public boolean hit(int bx, int by) {

		// loop through all enemies
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {

				// check collision with the enemy
				if (enemies[r][c].hit(bx, by)) {
					return true;
				}
			}
		}

		// no enemy was hit
		return false;
	}
}