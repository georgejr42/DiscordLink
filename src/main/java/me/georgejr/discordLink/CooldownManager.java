package me.georgejr.discordLink;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {

    //get main class
    private static final DiscordLink main = DiscordLink.getPlugin(DiscordLink.class);

    //create hashmap for cooldowns
    public final Map<UUID, Long> cooldowns = new HashMap<>();

    public void setCooldown(UUID player, long time){
        if(time < 1) {
            cooldowns.remove(player);
        } else {
            cooldowns.put(player, time);
        }
    }
}
