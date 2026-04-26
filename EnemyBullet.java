import java.awt.*;

public class EnemyBullet {

	int x, y;
	boolean active = false;

	// fire
	public void shoot(int startX, int startY) {

		// only shoot if there is NOT already a bullet active
		if (!active) {
			active = true; // turn bullet ON
			// set starting position 
			x = startX;
			y = startY;
		}
	}

	// MOVE BULLET EVERY FRAME
	public void move() {

		// only move if bullet is active
		if (active) {
			y += 10;

			// if bullet goes off screen  remoce it
			if (y > 600) {
				active = false;
			}
		}

	}

	// DRAW BULLET
	public void draw(Graphics g) {

		// only draw if active (visible)
		if (active) {

			g.setColor(Color.YELLOW);
			g.fillRect(x, y, 5, 10);
		}
	}

	// CHECK IF BULLET HIT PLAYER
	public boolean hitPlayer(int px) {
		if (active &&
		    x > px &&
		    x < px + 200 &&
		    y > 580 && 
		    y < 600) {

			active = false; // remove bullet after hit
			return true;    // player got hit
		}

		return false; // no hit
	}
}