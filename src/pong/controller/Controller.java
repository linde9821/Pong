package pong.controller;

public class Controller {
	private double x;
	private double y;
	private final double w = 25; // width
	private final double h = 100; // heigth
	private int deltay = 10;

	private double vel = 0;

	public Controller(double x, double y) {
		this.y = y;
		this.x = x;
	}

	public void setDeltaY(int deltay) {
		this.deltay = this.deltay - deltay;
	}

	public int getnewDeltaY() {
		if (deltay == 4) {
			return 0;
		} else
			return 1;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getW() {
		return w;
	}

	public double getH() {
		return h;
	}

	public void move(int direction) {
		if (direction == 1) {
			y -= deltay;

			if (vel > 0) {
				vel = -1;
			}

			vel -= 0.4;
		}

		if (direction == -1) {
			y += deltay;

			if (vel < 0) {
				vel = 1;
			}

			vel += 1;
		}
	}

	public void bounce(){
		vel = -vel;
	}

	public void updateVel() {
		this.y += vel;
		vel *= 0.98; // Reibung
	}

}
