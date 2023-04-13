package com.nonstopslip.gmail.universal_slabs;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UniversalSlabsMod implements ModInitializer {

	public static final String MOD_ID = "universal_slabs";
	public static final String NAME = "Universal Slabs";


	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info(NAME + " initializing...");
	}
}
