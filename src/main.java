import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import controller.AbsorptionController;
import controller.OverloadController;
import controller.PrayerController;
import common.Constant;
import common.Constant.State;
import common.Util;

import java.awt.*;

@ScriptManifest(author = "You", info = "My first script", name = "nmz-bot 1.1", version = 0, logo = "")
public class main extends Script {
	private boolean running;
	private boolean prayOpen, busy;
	private State curState;
	private OverloadController olController;
	private AbsorptionController absController;
	private PrayerController prayController;

	private int rockcakeCount;

	@Override
	public void onStart() {
		running = true;
		busy = false;
		prayOpen = false;

		rockcakeCount = 0;

		olController = new OverloadController();
		absController = new AbsorptionController(getWidgets());
		prayController = new PrayerController();

		try {
			prepareTab(true);
			olController.reupOverload(getInventory(), getMouse());
			absController.reupAbsorp(getInventory(), getMouse());
			prepareTab(false);
			prayController.prayerFlick(getPrayer(), getMouse());
		} catch (InterruptedException e) {
			log("uhoh");
			e.printStackTrace();
		}
	}

	private State getState() {
		if (running && !busy) {
			if (isFullHealth()) {
				running = false;
				return State.IDLE;
			}
			else if (!olController.isEmpty() && checkOverloadTimer())
				return State.PREPARE_OVERLOAD;
			else if (!absController.isEmpty() && checkAbsorpLevel())
				return State.GUZZLE_ABSORP;
			else if (checkPrayerTimer())
				return State.PRAYER_FLICKING;
			else if (is2HP()) {
				return State.GUZZLE_ROCKCAKE;
			} else
				return State.IDLE;
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

	private boolean isFullHealth() {
		if (getSkills().getDynamic(Skill.HITPOINTS) == getSkills().getStatic(Skill.HITPOINTS))
			return true;
		return false;
	}

	private boolean is2HP() {
		log("rockcakeCount:  " + rockcakeCount);
		if (getSkills().getDynamic(Skill.HITPOINTS) == 2)
			return true;
		return false;
	}

	private void guzzleRockcake() {
		prepareTab(true);
		int slot = Util.searchInventory(Constant.ROCKCAKE, getInventory());
		if (slot != -1) {
			// guzzle rock cake
			getInventory().interact(slot, "Guzzle");
		}
	}

	// if parameter == true, open inventory, else open prayer
	private void prepareTab(boolean s) {
		if (s) {
			if (prayOpen) {
				prayOpen = false;
				getTabs().open(Tab.INVENTORY);
			}
		} else {
			if (!prayOpen) {
				prayOpen = true;
				getPrayer().open();
			}
		}
	}

	@Override
	public int onLoop() throws InterruptedException {
		curState = getState();

		if (!curState.equals(State.IDLE))
			busy = true;

		switch (curState) {
		case PREPARE_OVERLOAD:
			prepareTab(true);
			olController.reupOverload(getInventory(), getMouse());
			break;
		case IDLE:
			// doSomething();
			if (!running) {
				sleep(random(2000, 4000));
				stop();
			}
			break;
		case GUZZLE_ABSORP:
			prepareTab(true);
			absController.reupAbsorp(getInventory(), getMouse());
			break;
		case PRAYER_FLICKING:
			prepareTab(false);
			prayController.prayerFlick(getPrayer(), getMouse());
			break;
		case GUZZLE_ROCKCAKE:
			prepareTab(true);
			rockcakeCount++;
			guzzleRockcake();
			break;
		default:
			break;
		}

		busy = false;
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