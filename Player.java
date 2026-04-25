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
		g.fillRect(x+50, 600-25+5, 100, 25);
		g.fillRect(x+50+15, 600-32+5, 70, 30);
		g.fillRect(x+93, 600-45+5, 70/4, 30);
		g.fillRect(x+93, 600-35, 70/4, 30);
		g.fillRect(x+99, 600-50+5, 5, 10);
	}
}