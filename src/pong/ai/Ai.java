package pong.ai;

import pong.ball.Ball;
import pong.controller.Controller;

public class Ai {
	private final int easyD = 0;
	private final int normalD = 1;
	private final int hardD = 2;

	private double yBPos = 0;
	private double xBPos = 0;
	private double yCPosMin, yCPosMax = 0;
	private int yVel = 0;

	private int difficulty = hardD;

	private long lastDic = 0;

	public void makeDecision(final Ball aiBall, final Controller aiController) {
		yBPos = aiBall.getCY();
		xBPos = aiBall.getCX();
		yCPosMin = aiController.getY() - aiController.getH() / 2;
		yCPosMax = aiController.getY() + aiController.getH() / 2;

		if (Math.abs(aiController.getX() - xBPos) <= 800) {
			switch (difficulty) {
			case (easyD):
				yVel = easy();
				break;
			case (normalD):
				yVel = normal();
				break;
			case (hardD):
				yVel = hard(aiController);
				break;
			}
		}

		if (System.currentTimeMillis() - lastDic > 16.6667) {
			aiController.move(yVel);
			lastDic = System.currentTimeMillis();
		}
	}

	private int easy() {
		return 0;
	}

	private int normal() {
		return 0;
	}

	private int hard(final Controller aiController) {
		if (yBPos < yCPosMin) {
			yVel = 1;
		} else if (yBPos < yCPosMax) { // urgent
			yVel = 1;
			aiController.move(1);
		} else if (yBPos > yCPosMin) {
			yVel = -1;
		} else if (yBPos > yCPosMax) { // urgent
			yVel = -1;
			aiController.move(-1);
		} else {
			yVel = 0;
		}

		return yVel;
	}
}
