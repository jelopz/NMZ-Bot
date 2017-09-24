package controller;

import org.osbot.rs07.api.Inventory;
import org.osbot.rs07.api.Mouse;
import org.osbot.rs07.api.Widgets;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.script.MethodProvider;

import common.Constant;
import common.Util;

public class AbsorptionController {
	private boolean absorpEmpty;
	RS2Widget absorpWidget;
	private int curAbsorp, absThresh;

	public AbsorptionController(Widgets w) {
		absorpEmpty = false;
		absorpWidget = w.get(202, 1, 9);
		absThresh = Util.randomInt(350, 450);
	}

	public boolean isEmpty() {
		return absorpEmpty;
	}

	public int updateAbsorptionLevel() {
		curAbsorp = Integer.parseInt(absorpWidget.getMessage());
		return curAbsorp;
	}

	public boolean isLow() {
		if (curAbsorp <= absThresh)
			return true;
		else
			return false;
	}

	public void reupAbsorp(Inventory inv, Mouse m) throws InterruptedException {
		// if (prayOpen) {
		// prayOpen = false;
		// getTabs().open(Tab.INVENTORY);
		// }
		int slot = Util.searchInventory(Constant.ABSORP_NAMES[0], inv);
		// move mouse to spot and then click
		if (slot != -1) {
			m.move(inv.getMouseDestination(slot));
			absorpClicker(slot, inv, m);
			absThresh = Util.randomInt(350, 450);
		} else {
			absorpEmpty = true;
		}
	}

	public static int searchInventory(String s, Inventory inv) {
		Item[] items = inv.getItems();

		for (int i = 0; i < items.length; i++) {
			if (items[i] != null && items[i].getName().equals(s)) {
				return i;
			}
		}

		return -1;
	}

	// change while loop to for loop > refer to overloadcontroller
	private void absorpClicker(int slot, Inventory inv, Mouse m) throws InterruptedException {
		boolean reUpped = false;
		boolean chugging = false;
		long extraTime = 0;

		for (long currentTime = System.currentTimeMillis(); currentTime < extraTime
				|| reUpped == false; currentTime = System.currentTimeMillis()) {
			if (reUpped) {
				if (System.currentTimeMillis() >= extraTime)
					break;
			} else if (extraTime == 0) {
				for (int i = 0; i < Constant.ABSORP_NAMES.length; i++) {
					if (inv.getItems()[slot].getName().equals(Constant.ABSORP_NAMES[i])) {
						chugging = true;
					}
				}
				if (!chugging) {
					reUpped = true;
					extraTime = System.currentTimeMillis() + (Util.randomInt(1, 3) * 1000);
				} else
					chugging = false;
			}
			m.click(false);
			MethodProvider.sleep(Util.randomInt(150, 165));
		}
	}
}
