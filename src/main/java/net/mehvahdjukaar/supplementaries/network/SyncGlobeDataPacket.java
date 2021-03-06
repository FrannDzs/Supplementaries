package net.mehvahdjukaar.supplementaries.network;

import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.world.data.GlobeData;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;


public class SyncGlobeDataPacket {
    public GlobeData data;
    public SyncGlobeDataPacket(PacketBuffer buffer) {
        this.data = new GlobeData() ;
        this.data.load(buffer.readNbt());
    }

    public SyncGlobeDataPacket(GlobeData data) {
        this.data = data;
    }

    public static void buffer(SyncGlobeDataPacket message, PacketBuffer buffer) {
        buffer.writeNbt(message.data.save(new CompoundNBT()));
    }

    public static void handler(SyncGlobeDataPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (!context.getDirection().getReceptionSide().isServer()) {
                //assigns data to client
                GlobeData.clientSide = message.data;
                Supplementaries.LOGGER.info("Synced Globe data");
            }
        });
        context.setPacketHandled(true);
    }
}