package dev.noeb;

import net.fabricmc.api.ClientModInitializer;

public class NoebClient implements ClientModInitializer {
	public void onInitializeClient() {
		Cfg.ld();
	}
}