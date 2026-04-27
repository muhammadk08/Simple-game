import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class Bullet {

	int x, y;
	boolean active = false;

	public void shoot(int startX) {
		if (!active) {
			active = true;
			x = startX;
			y = 600;
		}
	}

	public void move() {
		if (active) {
			y -= 10;

			if (y < 0) {
				active = false;
			}
		}
	}

	public void draw(Graphics g) {
		if (active) {

		}
	}
}