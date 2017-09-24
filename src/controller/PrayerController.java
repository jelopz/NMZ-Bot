package controller;

import org.osbot.rs07.api.Prayer;
import org.osbot.rs07.api.ui.PrayerButton;
import org.osbot.rs07.script.MethodProvider;

import common.Util;

public class PrayerController {
	private long prayerTimer;

	public PrayerController() {
		super();
	}

	public long getTimer() {
		return prayerTimer;
	}

	public void prayerFlick(Prayer p) throws InterruptedException {
		p.set(PrayerButton.RAPID_HEAL, true);
		// time between double click
		MethodProvider.sleep(Util.randomInt(240, 270));
		p.set(PrayerButton.RAPID_HEAL, false);
		prayerTimer = System.currentTimeMillis() + (long) (Util.randomInt(35, 54) * 1000);
	}
}
