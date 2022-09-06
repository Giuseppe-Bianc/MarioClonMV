package org.dersbian.jade;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class LevelScene extends Scene {
	private static final Logger LOGGER = LogManager.getLogger(LevelScene.class);
	public LevelScene() {
		LOGGER.info("Inside level scene");
		Window.get().r = 1;
		Window.get().g = 1;
		Window.get().b = 1;
	}

	@Override
	public void update(float dt) {

	}
}
