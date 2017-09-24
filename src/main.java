import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import controller.AbsorptionController;
import controller.OverloadController;
import controller.PrayerController;
import common.Constant.State;

import java.awt.*;

@ScriptManifest(author = "You", info = "My first script", name = "nmz-bot", version = 0, logo = "")
public class main extends Script {
	private boolean prayOpen, doingSomething;
	private OverloadController olController;
	private AbsorptionController absController;
	private PrayerController prayController;

	@Override
	public void onStart() {
		doingSomething = false;
		prayOpen = false;
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
			prayController.prayerFlick(getPrayer(), getMouse());
		} catch (InterruptedException e) {
			log("uhoh");
			e.printStackTrace();
		}
	}

	private State getState() {
		if (!doingSomething && !olController.isEmpty() && checkOverloadTimer()) {
			doingSomething = true;
			return State.PREPARE_OVERLOAD;
		} else if (!doingSomething && !absController.isEmpty() && checkAbsorpLevel()) {
			doingSomething = true;
			return State.GUZZLE_ABSORP;
		} else if (!doingSomething && checkPrayerTimer()) {
			doingSomething = true;
			return State.PRAYER_FLICKING;
		} else
			return State.IDLE;
	}

	// TODO: remove checkOverloadTimer and checkAbsorpLevel, these methods
	// should not be in main. For now it's fine.
	private boolean checkOverloadTimer() {
		long t = olController.getTimer();
		log("Time until next overload reup: " + ((t - System.currentTimeMillis()) / 1000));
		if (System.currentTimeMillis() >= t)
			return true;
		else
			return false;
	}

	// cur variable only exists for testing purposes. remove after testing
	private boolean checkAbsorpLevel() {
		int cur = absController.updateAbsorptionLevel();
		log("current Absorption level: " + cur + " current Absorp thresh: " + absController.getThresh());
		return absController.isLow();
	}

	private boolean checkPrayerTimer() {
		long t = prayController.getTimer();
		log("Time until next prayer flick: " + ((t - System.currentTimeMillis()) / 1000));
		if (System.currentTimeMillis() >= t) {
			prayController.maxOutTimer();
			return true;
		}
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
			doingSomething = false;
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
			doingSomething = false;
			break;
		case PRAYER_FLICKING:
			if (!prayOpen) {
				prayOpen = true;
				getPrayer().open();
			}
			prayController.prayerFlick(getPrayer(), getMouse());
			doingSomething = false;
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