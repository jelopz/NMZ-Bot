import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import controller.overloadController;
import common.Constant.State;

import java.awt.*;

@ScriptManifest(author = "You", info = "My first script", name = "overload eater", version = 0, logo = "")
public class main extends Script {
	private boolean prayOpen;
	overloadController olController;

	@Override
	public void onStart() {
		olController = new overloadController();

		try {
			if (prayOpen) {
				prayOpen = false;
				getTabs().open(Tab.INVENTORY);
			}
			olController.reupOverload(getInventory(), getMouse());
		} catch (InterruptedException e) {
			log("uhoh");
			e.printStackTrace();
		}
	}

	private State getState() {
		if (checkOverloadTimer() && !olController.isEmpty())
			return State.PREPARE_OVERLOAD;
		else
			return State.IDLE;
	}

	private boolean checkOverloadTimer() {
		if (System.currentTimeMillis() >= olController.getTimer())
			return true;
		else
			return false;
	}

	@Override
	public int onLoop() throws InterruptedException {
		switch (getState()) {
		case PREPARE_OVERLOAD:
			if (prayOpen) {
				prayOpen = false;
				getTabs().open(Tab.INVENTORY);
			}
			olController.reupOverload(getInventory(), getMouse());
			break;
		case IDLE:
			// doSomething();
			break;
		case GUZZLE_ABSORP:
			break;
		case PRAYER_FLICKING:
			break;
		default:
			break;
		}
		return random(200, 300);
	}

	@Override
	public void onExit() {
		log("cya");
	}

	@Override
	public void onPaint(Graphics2D g) {

	}

}