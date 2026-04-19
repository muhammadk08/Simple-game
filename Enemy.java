import java.awt.*;
import javax.swing.*;

public class Enemy {

	int x, y;
	boolean alive = true;

	Image img;

	public Enemy(int x, int y) {
		this.x = x;
		this.y = y;

		// try normal file load first
		img = new ImageIcon("enemy.png").getImage();

		// if that fails → try resource load
		if (img == null || img.getWidth(null) == -1) {
			java.net.URL url = getClass().getResource("enemy.png");
			if (url != null) {
				img = new ImageIcon(url).getImage();
			}
		}
	}

	public void move(int dir, int speed) {
		x += dir * speed;
	}

	public void draw(Graphics g) {
		if (alive) {
			if (img != null && img.getWidth(null) != -1) {
				g.drawImage(img, x, y, 60, 40, null);
			} else {
				// fallback (so you SEE something if image fails)
				g.setColor(Color.RED);
				g.fillRect(x, y, 60, 40);
			}
		}
	}

	public boolean hit(int bx, int by) {
		if (alive && bx > x && bx < x + 60 && by > y && by < y + 40) {
			alive = false;
			return true;
		}
		return false;
	}
}