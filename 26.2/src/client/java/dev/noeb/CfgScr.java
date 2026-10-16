package dev.noeb;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class CfgScr {
	static Component t(String s) {
		return Component.literal(s);
	}

	public static Screen mk(Screen p) {
		ConfigBuilder b = ConfigBuilder.create().setParentScreen(p).setTitle(t("No Eat Bounce")).setSavingRunnable(Cfg::sv);
		ConfigCategory cc = b.getOrCreateCategory(t("Settings"));
		ConfigEntryBuilder e = b.entryBuilder();
		Cfg c = Cfg.c;
		cc.addEntry(e.startBooleanToggle(t("Mod On"), c.on).setDefaultValue(true).setTooltip(t("Turn this off to get the normal vanilla eating animation back.")).setSaveConsumer(v -> c.on = v).build());
		cc.addEntry(e.startTextDescription(t("When you eat food or use a potion, the item in your hand bobs up and down. This setting controls how far it bobs. The item still moves to your mouth.")).build());
		cc.addEntry(e.startIntSlider(t("Bounce Height"), c.amt, 0, 100).setDefaultValue(0).setTextGetter(v -> t(v + "%")).setTooltip(t("0% removes the up and down bounce completely. 100% is the normal vanilla bounce. Anything in between is a smaller bounce.")).setSaveConsumer(v -> c.amt = v).build());
		return b.build();
	}
}