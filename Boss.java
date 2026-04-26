import java.awt.*;
import javax.swing.*;

public class Boss {

	int x = 300, y = 100;

	int dx = 4; // speed X
	int dy = 2; // speed Y

	int width = 200;
	int height = 120;

	int maxHealth = 20;
    int health = maxHealth;

	Image[] imgs = new Image[2];
	int frame = 0;

	public Boss() {
		imgs[0] = new ImageIcon("boss1.png").getImage();
		imgs[1] = new ImageIcon("boss2.png").getImage();
	}

	public void move() {

		// move
		x += dx;
		y += dy;

		// bounce left/right
		if (x <= 0 || x + width >= 800) {
			dx *= -1;
		}

		// bounce top / barrier zone (~400)
		if (y <= 50 || y + height >= 400) {
			dy *= -1;
		}

		// random direction change (chaotic)
		if (Math.random() < 0.02) {
			dx = (int)(Math.random() * 7) - 3;
			dy = (int)(Math.random() * 5) - 2;
		}

		// animation
		frame++;
		if (frame >= 20) frame = 0;
	}

	public void draw(Graphics g) {
		int index = frame < 10 ? 0 : 1;
		g.drawImage(imgs[index], x, y, width, height, null);
	}

	public boolean hit(int bx, int by) {
		if (bx > x && bx < x + width &&
		    by > y && by < y + height) {

			health--;
			return true;
		}
		return false;
	}

	public boolean isAlive() {
		return health > 0;
	}

	public int getCenterX() {
		return x + width / 2;
	}

	public int getBottomY() {
		return y + height;
	}
}