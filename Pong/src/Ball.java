
public class Ball {
	private double cx;
	private double cy; // c=center
	private double radius = 20;
	private int directionx;
	private int directiony;
	private int velocity;

	public Ball(double x, double y) {
		this.cx = x;
		this.cy = y;
		directionx = setdirectionx();
		directiony = setdirectiony();
		velocity = 5;
	}

	public void setVel(int velocity) {
		this.velocity = velocity;
	}

	public int getVel() {
		return velocity;
	}

	public double getCX() {
		return cx;
	}

	public double getCY() {
		return cy;
	}

	public double getRADIUS() {
		return radius;
	}

	public int setdirectionx() {
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

	public int setdirectiony() {
		double dy = (Math.random() * 20); // deltay
		if (dy >= 10) {
			return 10; // --> für Positive
		}
		if (dy <= 10) {
			return -10;// --> für negative
		} else
			return -1;
	}

	public void setDirectionx(int directionx) {
		this.directionx = directionx * (-1);
	}

	public void setDirectiony(int directiony) {
		this.directiony = directiony * (-1);
	}

	public int getDirectionx() {
		return directionx;
	}

	public int getDirectiony() {
		return directiony;
	}

	public double movecx(int direction) {
		if (direction == 10) {
			return cx += velocity;
		}
		if (direction == -10) {
			return cx -= velocity;
		} else
			return this.cx;
	}

	public double movecy(int direction) {
		if (direction == 10) {
			return cy -= velocity;
		}
		if (direction == -10) {
			return cy += velocity;
		} else
			return cy;
	}

	public void decreaseRadius() {
		radius = radius - 2.5;
	}

	public void reset() {
		cx = 640;
		cy = 360;
	}
}