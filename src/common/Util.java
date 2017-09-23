package common;

import java.util.Random;

import org.osbot.rs07.api.Inventory;
import org.osbot.rs07.api.model.Item;

public class Util{
	public static Random rand = new Random();
	
	public static int searchInventory(String s, Inventory inv) {
		Item[] items = inv.getItems();

		for (int i = 0; i < items.length; i++) {
			if (items[i] != null && items[i].getName().equals(s)) {
				return i;
			}
		}

		return -1;
	}
	
	public static int randomInt(int min, int max)
	{
		int n = max - min + 1;
		int i = rand.nextInt() % n;
		return min + i;
	}
}
