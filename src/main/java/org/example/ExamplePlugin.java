package org.example;

import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.plugin.Plugin;

public class ExamplePlugin extends Plugin {
	
	@Override
	public void onLoad() {
		
		//logger
		this.getLogger().info("killeffect plugin loaded!");
		
		//creating and registering a new module
		final ExampleModule exampleModule = new ExampleModule();
		RusherHackAPI.getModuleManager().registerFeature(exampleModule);
	}
	
	@Override
	public void onUnload() {
		this.getLogger().info("killeffect plugin unloaded!");
	}
	
}