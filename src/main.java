import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import controller.AbsorptionController;
import controller.OverloadController;
import controller.PrayerController;
import common.Constant.State;

import java.awt.*;

@ScriptManifest(author = "You", info = "My first script", name = "overload eater", version = 0, logo = "")
public class main extends Script {
	private boolean prayOpen;
	OverloadController olController;
	AbsorptionController absController;
	PrayerController prayController;

	@Override
	public void onStart() {
		olController = new OverloadController();
		absController = new AbsorptionController(getWidgets());
		prayController = new PrayerController();

		try {
			if (prayOpen) {
				prayOpen = false;
				getTabs().open(Tab.INVENTORY);
			}
			olController.reupOverload(getInventory(), getMouse());
			absController.reupAbsorp(getInventory(), getMouse());

			if (!prayOpen) {
				prayOpen = true;
				getPrayer().open();
			}
			prayController.prayerFlick(getPrayer());
		} catch (InterruptedException e) {
			log("uhoh");
			e.printStackTrace();
		}
	}

	private State getState() {
		if (checkOverloadTimer() && !olController.isEmpty())
			return State.PREPARE_OVERLOAD;
		else if (checkAbsorpLevel() && !absController.isEmpty())
			return State.GUZZLE_ABSORP;
		else if (checkPrayerTimer())
			return State.PRAYER_FLICKING;
		else
			return State.IDLE;
	}

	// TODO: remove checkOverloadTimer and checkAbsorpLevel, these methods
	// should not be in main. For now it's fine.
	private boolean checkOverloadTimer() {
		if (System.currentTimeMillis() >= olController.getTimer())
			return true;
		else
			return false;
	}

	// cur variable only exists for testing purposes. remove after testing
	private boolean checkAbsorpLevel() {
		int cur = absController.updateAbsorptionLevel();
		log("current Absorption level:  " + cur);
		return absController.isLow();
	}

	private boolean checkPrayerTimer() {
		if (System.currentTimeMillis() >= prayController.getTimer())
			return true;
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
			if (prayOpen) {
				prayOpen = false;
				getTabs().open(Tab.INVENTORY);
			}
			absController.reupAbsorp(getInventory(), getMouse());
			break;
		case PRAYER_FLICKING:
			if (!prayOpen) {
				prayOpen = true;
				getPrayer().open();
			}
			prayController.prayerFlick(getPrayer());
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