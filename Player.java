import java.awt.*;

public class Player {

	int x;
	int width = 200;
	int height = 50;

	public Player(int x) {
		this.x = x;
	}

	public void move(boolean left, boolean right) {
		if (left) x -= 5;
		if (right) x += 5;

		// keep it inside
		if (x < 0) x = 0;
		if (x > 800 - width) x = 800 - width;
	}

	public void draw(Graphics g) {
		g.setColor(Color.GREEN);
		g.fillRect(x, 600 - height, width, height);
		g.fillRect(x, 600 - height, width, height);
	}
}