package net.depthscape.core.utils.hologram;

import lombok.Getter;
import lombok.Setter;
import net.depthscape.core.utils.ChatUtils;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.level.World;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

@Getter
@Setter
public class Hologram {

    /*

       These classes handle the holograms withing the gamemodes.
       They can be easily updated with the refresh() function,
       even real time.

       This class handles the packet sending and the updating of holograms.
    */


    /*
        Hologram settings. Object gets pulled from the Hologram settings.
    */

    String id, text;
    Location location;

    public Hologram(final String id, final String text, final Location location) {
        this.id = id;
        this.text = ChatUtils.format(text);
        this.location = location;
    }

    public void setText(final String text) {
        this.text = ChatUtils.format(text);
    }

    EntityArmorStand armorStand;

    public void spawn(final Player player) {

        final WorldServer craftWorld = ((CraftWorld) location.getWorld()).getHandle();
        armorStand = new EntityArmorStand(craftWorld, location.getX(), location.getY(), location.getZ());


        final String string = text.replace("{player}", player.getName());

        armorStand.b(IChatBaseComponent.a(ChatUtils.format(string)));
        armorStand.persistentInvisibility = true;
        armorStand.b(5, true);
        armorStand.n(true);
        armorStand.j(true);

        final PlayerConnection connection = ((CraftPlayer) player).getHandle().c;

        final PacketPlayOutSpawnEntity spawnPacket = new PacketPlayOutSpawnEntity(armorStand);
        connection.a(spawnPacket);

        final PacketPlayOutEntityMetadata metaPacket = new PacketPlayOutEntityMetadata(armorStand.af(), armorStand.aj().c());
        connection.a(metaPacket);

        final PacketPlayOutEntityTeleport teleportPacket = new PacketPlayOutEntityTeleport(armorStand);
        connection.a(teleportPacket);
    }

    public void refresh(final Player player) {

        final PlayerConnection connection = ((CraftPlayer) player).getHandle().c;

        final String string = text.replace("{player}", player.getName());

        final WorldServer craftWorld = ((CraftWorld) location.getWorld()).getHandle();
        armorStand.b(IChatBaseComponent.a(ChatUtils.format(string)));
//        armorStand.teleportTo(craftWorld, new Position(location.getX(), location.getY(), location.getZ()));
//        if (!craftWorld.equals(this.armorStand.dI())) {
//            try {
//                Field worldField = this.armorStand.getClass().getDeclaredField("t");
//                worldField.setAccessible(true);
//                worldField.set(this.armorStand, craftWorld);
//            } catch (NoSuchFieldException | IllegalAccessException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        this.armorStand.a(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        final PacketPlayOutEntityMetadata metaPacket = new PacketPlayOutEntityMetadata(armorStand.af(), armorStand.aj().c());
        connection.a(metaPacket);

//        final PacketPlayOutEntityTeleport teleportPacket = new PacketPlayOutEntityTeleport(armorStand);
//        connection.a(teleportPacket);
    }

    public void asPassenger(Player player, Entity vehicle) {
        final PlayerConnection connection = ((CraftPlayer) player).getHandle().c;
        final PacketPlayOutAttachEntity attachPacket = new PacketPlayOutAttachEntity(armorStand, ((CraftEntity) vehicle).getHandle());
        connection.a(attachPacket);
    }

    public void remove(final Player player) {
        final PlayerConnection connection = ((CraftPlayer) player).getHandle().c;
        connection.a(new PacketPlayOutEntityDestroy(armorStand.af()));
    }
}
