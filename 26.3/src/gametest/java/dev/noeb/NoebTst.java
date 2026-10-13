package dev.noeb;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import java.lang.reflect.Method;
import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class NoebTst implements FabricClientGameTest {
	ClientGameTestContext c;

	void ck(boolean b, String s) {
		if (!b) throw new AssertionError(s);
		System.out.println("NOEB OK: " + s);
	}

	float y(Minecraft m) {
		try {
			Method d = FirstPersonHandsAndItemsRenderer.class.getDeclaredMethod("applyEatTransform", PoseStack.class, float.class, HumanoidArm.class, float.class, int.class);
			d.setAccessible(true);
			PoseStack p = new PoseStack();
			d.invoke(m.gameRenderer.firstPersonHandsAndItemsRenderer, p, 0f, HumanoidArm.RIGHT, (float) m.player.getUseItemRemainingTicks(), m.player.getMainHandItem().getUseDuration(m.player));
			return p.last().pose().m31();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	float rng(boolean on, int amt) {
		c.runOnClient(m -> {
			Cfg.c.on = on;
			Cfg.c.amt = amt;
		});
		c.getInput().holdMouse(InputConstants.MOUSE_BUTTON_RIGHT);
		float lo = 9, hi = -9;
		for (int i = 0; i < 30; i++) {
			c.waitTick();
			float[] r = c.computeOnClient(m -> new float[] { m.player.isUsingItem() ? y(m) : 0f, m.player.isUsingItem() ? m.player.getUseItemRemainingTicks() : 99 });
			if (r[1] < 3 || r[1] > 24) continue;
			lo = Math.min(lo, r[0]);
			hi = Math.max(hi, r[0]);
		}
		c.getInput().releaseMouse(InputConstants.MOUSE_BUTTON_RIGHT);
		c.waitTicks(5);
		return hi - lo;
	}
	public void runTest(ClientGameTestContext ctx) {
		c = ctx;
		try (TestSingleplayerContext s = ctx.worldBuilder().create()) {
			ctx.waitTicks(40);
			s.getServer().runCommand("give @p golden_apple");
			ctx.waitTicks(10);
			ck(ctx.computeOnClient(m -> m.player.getMainHandItem().is(Items.GOLDEN_APPLE)), "golden apple is in the main hand");
			float a = rng(true, 100);
			ck(a > 0.08f, "100% keeps the vanilla bounce, range " + a);
			float b = rng(true, 0);
			ck(b < 0.001f, "0% removes the bounce, range " + b);
			float h = rng(true, 50);
			ck(h > 0.04f && h < 0.06f, "50% halves the bounce, range " + h);
			float o = rng(false, 0);
			ck(o > 0.08f, "mod off gives the vanilla bounce, range " + o);
			Cfg.c = new Cfg();
			Cfg.c.amt = 250;
			Cfg.sv();
			ck(Cfg.c.amt == 100, "amount is clamped to 100");
			Cfg.c = new Cfg();
			Cfg.c.amt = 30;
			Cfg.sv();
			Cfg.c = new Cfg();
			Cfg.ld();
			ck(Cfg.c.amt == 30, "config reloads from the json file");
			Cfg.c = new Cfg();
			Cfg.sv();
			ck(Cfg.c.on && Cfg.c.amt == 0, "defaults are on with 0%");
			ctx.getInput().holdMouse(InputConstants.MOUSE_BUTTON_RIGHT);
			ctx.waitTicks(14);
			ctx.takeScreenshot("noeb-eat");
			ctx.getInput().releaseMouse(InputConstants.MOUSE_BUTTON_RIGHT);
			ctx.runOnClient(m -> m.gui.setScreen(CfgScr.mk(null)));
			ctx.waitTicks(10);
			ck(ctx.computeOnClient(m -> m.gui.screen().getClass().getSimpleName().equals("ClothConfigScreen")), "cloth config screen opens");
			ctx.takeScreenshot("noeb-cfg");
		}
	}
}