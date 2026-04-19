import java.awt.*;
import javax.swing.*;

public class Barrier {

	int x, y;
	int state = 0; // 0,1,2 then gone

	Image[] imgs = new Image[3];

	public Barrier(int x, int y) {
		this.x = x;
		this.y = y;

		imgs[0] = new ImageIcon("barrier0.png").getImage();
		imgs[1] = new ImageIcon("barrier1.png").getImage();
		imgs[2] = new ImageIcon("barrier2.png").getImage();
	}

	public void draw(Graphics g) {
		if (state < 3) {
			g.drawImage(imgs[state], x, y, 80, 40, null);
		}
	}

	public boolean hit(int bx, int by) {

		if (state >= 3) return false;

		if (bx > x && bx < x + 80 && by > y && by < y + 40) {
			state++;
			return true;
		}
		return false;
	}
}