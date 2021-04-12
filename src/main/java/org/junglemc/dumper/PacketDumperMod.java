package org.junglemc.dumper;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.ModInitializer;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PacketDumperMod implements ModInitializer {

    private static PackerDumper INSTANCE;

    @Override
    public void onInitialize() {
        INSTANCE = new PackerDumper();
    }

    public static PackerDumper instance() {
        if (INSTANCE == null) {
            throw new NullPointerException("instance is null");
        }
        return INSTANCE;
    }

    public static class PackerDumper {
        public void dump(Direction direction, Packet<?> packet) {
            System.out.println(direction.name() + " -> " + packet.getClass().getSimpleName());

            Path dir = Paths.get("/home/ella/packets/server", direction.name());
            if (!Files.exists(dir)) {
                try {
                    Files.createDirectories(dir);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            Path packetFile = Paths.get(dir.toString(), packet.getClass().getSimpleName() + ".bin");
            if (!Files.exists(packetFile)) {
                try {
                    ByteBuf byteBuf = Unpooled.buffer();

                    PacketByteBuf buf = new PacketByteBuf(byteBuf);
                    packet.write(buf);

                    byte[] out = new byte[byteBuf.readableBytes()];
                    byteBuf.readBytes(out);
                    byteBuf.release();

                    Files.write(packetFile, out);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public enum Direction {
        RX, TX
    }
}
