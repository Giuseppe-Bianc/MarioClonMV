package org.dersbian.jade;

import java.awt.event.KeyEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LevelEditorScene extends Scene {
	private static final Logger LOGGER = LogManager.getLogger(LevelEditorScene.class);

	private boolean changingScene = false;
	private float timeToChangeScene = 2.0f;

	public LevelEditorScene() {
		LOGGER.info("Inside level editor scene");
	}

	@Override
	public void update(float dt) {

		if (!changingScene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE)) {
			changingScene = true;
		}

		if (changingScene && timeToChangeScene > 0) {
			timeToChangeScene -= dt;
			Window.get().r -= dt * 5.0f;
			Window.get().g -= dt * 5.0f;
			Window.get().b -= dt * 5.0f;
		}
		else if (changingScene) {
			Window.changeScene(1);
		}
	}
}