package com.anatevka.dungeongen;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        Objects.requireNonNull(getCommand("gen")).setExecutor(new GenerateCommand());
        Objects.requireNonNull(getCommand("setRegion")).setExecutor(new SetRegionCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
