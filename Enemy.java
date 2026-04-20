import java.awt.*;
import javax.swing.*;

public class Enemy {

	int x, y;
	boolean alive = true;

	Image[] imgs = new Image[2];
	int frame = 0;

	public Enemy(int x, int y) {
		this.x = x;
		this.y = y;

    imgs[0] = new ImageIcon("enemy0.png").getImage();
    imgs[1] = new ImageIcon("enemy1.png").getImage();
	}

	public void move(int dir, int speed) {
		x += dir * speed;

		frame++;
		if (frame >= 10) {
			frame = 0;
		}
	}

	public void draw(Graphics g) {
		if (alive) {

			int index = frame <5? 0 : 1;

			g.drawImage(imgs[index], x, y, 60, 40, null);
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