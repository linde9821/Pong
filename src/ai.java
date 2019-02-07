public class ai {
	private final int easyD = 0;
	private final int normalD = 1;
	private final int hardD = 2;

	private double yBPos = 0;
	private double yCPosMin, yCPosMax = 0;
	private int yVel = 0;

	private int difficulty = hardD;

	private long lastDic = 0;

	public void makeDecision(final Ball aiBall, final Controller aiController) {
		yBPos = aiBall.getCY();
		yCPosMin = aiController.getY() - 25;
		yCPosMax = aiController.getY() + 25;

		switch (difficulty) {
		case (easyD):
			yVel = easy();
			break;
		case (normalD):
			yVel = normal();
			break;
		case (hardD):
			yVel = hard();
			break;
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

	private int hard() {
		System.out.println("yCPosMin:" + yCPosMin);
		System.out.println("yCPosMax:" + yCPosMax);

		if (yBPos < yCPosMin && yBPos < yCPosMax) {
			yVel = 1;
		} else if (yBPos > yCPosMin && yBPos > yCPosMax) {
			yVel = -1;
		} else
			yVel = 0;

		return yVel;
	}
}
