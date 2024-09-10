package com.superworldsun.superslegend.registries;

import com.superworldsun.superslegend.SupersLegendMain;
import com.superworldsun.superslegend.songs.*;

import com.superworldsun.superslegend.songs.songs.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class OcarinaSongInit {
    public static final DeferredRegister<OcarinaSong> OCARINA_SONGS = DeferredRegister.create(new ResourceLocation(SupersLegendMain.MOD_ID, "ocarina_songs"), SupersLegendMain.MOD_ID);

    private static final List<RegistryObject<OcarinaSong>> ALL_SONGS = new ArrayList<>();
    public static final Supplier<IForgeRegistry<OcarinaSong>> REGISTRY = OCARINA_SONGS.makeRegistry(RegistryBuilder::new);
//    public static final RegistryObject<OcarinaSong> ZELDAS_LULLABY = registerSong("zeldas_lullaby", ZeldasLullaby::new);
//    public static final RegistryObject<OcarinaSong> SONG_OF_HEALING = registerSong("song_of_healing", SongOfHealing::new);
    /*1*/ public static final RegistryObject<OcarinaSong> ZELDAS_LULLABY = registerSong("zeldas_lullaby", ZeldasLullaby::new);
    /*2*/ public static final RegistryObject<OcarinaSong> EPONAS_SONG = registerSong("eponas_song", EponasSong::new);
    /*3*/ public static final RegistryObject<OcarinaSong> SARIAS_SONG = registerSong("sarias_song", SariasSong::new);
    /*4*/ public static final RegistryObject<OcarinaSong> SUNS_SONG = registerSong("suns_song", SunsSong::new);
    /*5*/ public static final RegistryObject<OcarinaSong> SONG_OF_TIME = registerSong("song_of_time", SongOfTime::new);
    /*6*/ public static final RegistryObject<OcarinaSong> SONG_OF_DOUBLE_TIME = registerSong("song_of_double_time", SongOfDoubleTime::new);
    /*7*/ public static final RegistryObject<OcarinaSong> INVERTED_SONG_OF_TIME = registerSong("inverted_song_of_time", InvertedSongOfTime::new);

    /*8*/ public static final RegistryObject<OcarinaSong> SONG_OF_STORMS = registerSong("song_of_storms", SongOfStorms::new);
    /*9*/ public static final RegistryObject<OcarinaSong> MINUET_OF_FOREST = registerSong("minuet_of_forest", MinuetOfForest::new);
    /*10*/ public static final RegistryObject<OcarinaSong> BOLERO_OF_FIRE = registerSong("bolero_of_fire", BoleroOfFire::new);
    /*11*/ public static final RegistryObject<OcarinaSong> SERENADE_OF_WATER = registerSong("serenade_of_water", SerenadeOfWater::new);
    /*12*/ public static final RegistryObject<OcarinaSong> REQUIEM_OF_SPIRIT = registerSong("requiem_of_spirit", RequiemOfSpirit::new);
    /*13*/ public static final RegistryObject<OcarinaSong> NOCTURNE_OF_SHADOW = registerSong("nocturne_of_shadow", NocturneOfShadow::new);
    /*14*/ public static final RegistryObject<OcarinaSong> PRELUDE_OF_LIGHT = registerSong("prelude_of_light", PreludeOfLight::new);

    /*15*/ public static final RegistryObject<OcarinaSong> SONG_OF_HEALING = registerSong("song_of_healing", SongOfHealing::new);
    /*16*/ public static final RegistryObject<OcarinaSong> SONG_OF_SOARING = registerSong("song_of_soaring", SongOfSoaring::new);
    /*17*/ public static final RegistryObject<OcarinaSong> SONATA_OF_AWAKENING = registerSong("sonata_of_awakening", SonataOfAwakening::new);
    /*18*/ public static final RegistryObject<OcarinaSong> GORON_LULLABY = registerSong("goron_lullaby", GoronLullaby::new);
    /*19*/ public static final RegistryObject<OcarinaSong> NEW_WAVE_BOSSA_NOVA = registerSong("new_wave_bossa_nova", NewWaveBossaNova::new);
    /*20*/ public static final RegistryObject<OcarinaSong> ELEGY_OF_EMPTYNESS = registerSong("elegy_of_emptyness", ElegyOfEmptyness::new);
    /*21*/ public static final RegistryObject<OcarinaSong> OATH_TO_ORDER = registerSong("oath_to_order", OathToOrder::new);

    private static RegistryObject<OcarinaSong> registerSong(String name, Supplier<OcarinaSong> song) {
        RegistryObject<OcarinaSong> registryObject = OCARINA_SONGS.register(name, () -> {
            OcarinaSong ocarinaSong = song.get();
            ocarinaSong.setRegistryName(new ResourceLocation(SupersLegendMain.MOD_ID, name));
            return ocarinaSong;
        });
        ALL_SONGS.add(registryObject);
        return registryObject;
    }

    public static void register(IEventBus modEventBus) {
        OCARINA_SONGS.register(modEventBus);
    }

    public static Iterable<Supplier<OcarinaSong>> getAllSongSuppliers() {
        return () -> ALL_SONGS.stream()
                .map(registryObject -> (Supplier<OcarinaSong>) registryObject)
                .iterator();
    }
}
