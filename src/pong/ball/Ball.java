package pong.ball;

public class Ball {
	private double cX; // center x
	private double cY; // center y
	private double radius = 20;
	private int directionX;
	private int directionY;
	private double velocity;

	public Ball(double x, double y) {
		this.cX = x;
		this.cY = y;
		directionX = setDirectionX();
		directionY = setDirectionY();
		velocity = 5;
	}

	public void setVel(double velocity) {
		this.velocity = velocity;
	}

	public double getVel() {
		return velocity;
	}

	public double getCX() {
		return cX;
	}

	public double getCY() {
		return cY;
	}

	public double getRadius() {
		return radius;
	}

	public int setDirectionX() {
		double dx = (Math.random() * 20);
		// double dy = (Math.random()*20);
		if (dx >= 10) {
			return 10; // --> für Positive
		}
		if (dx <= 10) {
			return -10;// --> für negative
		} else
			return -1;
	}

	public int setDirectionY() {
		double dy = (Math.random() * 20); // deltay
		if (dy >= 10) {
			return 10; // --> für Positive
		}
		if (dy <= 10) {
			return -10;// --> für negative
		} else
			return -1;
	}

	public void setDirectionX(int directionX) {
		this.directionX = directionX * (-1);
	}

	public void setDirectionY(int directionY) {
		this.directionY = directionY * (-1);
	}

	public int getDirectionX() {
		return directionX;
	}

	public int getDirectionY() {
		return directionY;
	}

	public double moveCX(int direction) {
		if (direction == 10) {
			return cX += velocity;
		}
		if (direction == -10) {
			return cX -= velocity;
		} else
			return this.cX;
	}

	public double moveCY(int direction) {
		if (direction == 10) {
			return cY -= velocity;
		}
		if (direction == -10) {
			return cY += velocity;
		} else
			return cY;
	}

	public void decreaseRadius() {
		radius = radius - 2.5;
	}

	public void reset() {
		cX = 640;
		cY = 360;
		velocity = 5;
		directionX *= -1;
	}
}