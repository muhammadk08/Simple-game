import java.awt.*;

public class EnemyBullet {

	int x, y;
	boolean active = false;

	public void shoot(int startX, int startY) {
		if (!active) {
			active = true;
			x = startX;
			y = startY;
		}
	}

	public void move() {
		if (active) {
			y += 10;

			if (y > 600) {
				active = false;
			}
		}
	}

	public void draw(Graphics g) {
		if (active) {
			g.setColor(Color.YELLOW);
			g.fillRect(x, y, 5, 10);
		}
	}

	public boolean hitPlayer(int px) {
		if (active &&
		    x > px &&
		    x < px + 200 &&
		    y > 550 &&
		    y < 600) {

			active = false;
			return true;
		}
		return false;
	}
}