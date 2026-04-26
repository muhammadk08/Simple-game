import java.awt.*;

public class BossBullet {

	int x, y;
	boolean active = false;

	public void shoot(int sx, int sy) {
		if (!active) {
			x = sx;
			y = sy;
			active = true;
		}
	}

	public void move() {
		if (active) {
			y += 20;

			if (y > 600) {
				active = false;
			}
		}
	}

	public void draw(Graphics g) {
		if (active) {
			g.setColor(Color.RED);
			g.fillRect(x, y, 12, 20); // BIG bullet
		}
	}

	public boolean hitPlayer(int px) {
		if (active &&
		    x > px + 50 &&
		    x < px + 150 &&
		    y > 580 &&
		    y < 600) {

			active = false;
			return true;
		}
		return false;
	}
}