package org.junglemc.dumper.mixin;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.DecoderHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import org.junglemc.dumper.PacketDumperMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Surrogate;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(value = DecoderHandler.class, targets = "Lnet/minecraft/network/Packet;")
public abstract class DecodeHandlerMixin {
    @Surrogate
    @Inject(method = "decode", at = @At(value = "TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list, CallbackInfo ci, PacketByteBuf packetByteBuf, int i, Packet<?> packet) {
        PacketDumperMod.instance().dump(PacketDumperMod.Direction.RX, packet);
    }
}
