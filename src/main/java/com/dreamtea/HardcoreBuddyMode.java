package com.dreamtea;

import com.dreamtea.commands.OpSpectate;
import com.dreamtea.gamerules.GRRegistry;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.command.CommandManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HardcoreBuddyMode implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String NAMESPACE = "hc-buddy-mode";
	public static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		OpSpectate.init();
		GRRegistry.register();
		LOGGER.info("Hello Fabric world!");
	}
}
