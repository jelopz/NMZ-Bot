package controller;

import org.osbot.rs07.api.Inventory;
import org.osbot.rs07.api.Mouse;
import org.osbot.rs07.script.MethodProvider;

import common.Constant;
import common.Util;

public class OverloadController {
	private boolean overloadEmpty;
	private long overloadTimer;

	public OverloadController() {
		overloadEmpty = false;
	}

	public boolean isEmpty() {
		return overloadEmpty;
	}

	public void setEmpty() {
		overloadEmpty = true;
	}

	public long getTimer() {
		return overloadTimer;
	}

	public void setTimer(long l) {
		overloadTimer = l;
	}

	public void reupOverload(Inventory inv, Mouse m) throws InterruptedException {

		// move mouse to overload potion position
		boolean success = false;
		int slot;

		for (int i = 0; i < 4 && success == false; i++) {
			if (!success) {
				slot = Util.searchInventory(Constant.OVERLOAD_NAMES[i], inv);
				if (slot != -1) {
					success = true;
					m.move(inv.getMouseDestination(slot));
					overloadClicker(slot, i, inv, m);
				}
			}
		}

		if (!success)
			overloadEmpty = true;
		else // determine next time to start this function
			overloadTimer = ((long) (System.currentTimeMillis() + Util.randomInt(4 * 60 + 52, 4 * 60 + 58) * 1000));
	}

	private void overloadClicker(int slot, int i, Inventory inv, Mouse m) throws InterruptedException {
		boolean reUpped = false;
		long extraTime = 0;

		for (long currentTime = System.currentTimeMillis(); currentTime < extraTime
				|| reUpped == false; currentTime = System.currentTimeMillis()) {
			if (reUpped) {
			} else if (inv.getItems()[slot] != null
					&& !inv.getItems()[slot].getName().equals(Constant.OVERLOAD_NAMES[i])) {
				reUpped = true;
				extraTime = System.currentTimeMillis() + (Util.randomInt(1, 3) * 1000);
			} else if (inv.getItems()[slot] == null) {
				reUpped = true;
				extraTime = System.currentTimeMillis() + (Util.randomInt(1, 3) * 1000);
			}
			m.click(false);
			MethodProvider.sleep(Util.randomInt(150, 165));
		}
	}
}
