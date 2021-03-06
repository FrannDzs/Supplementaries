package net.mehvahdjukaar.supplementaries.configs;

import net.mehvahdjukaar.supplementaries.block.util.CapturedMobs;
import net.mehvahdjukaar.supplementaries.world.data.GlobeDataGenerator;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class ClientConfigs {
    public static ForgeConfigSpec CLIENT_CONFIG;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        block.init(builder);
        particle.init(builder);
        entity.init(builder);
        general.init(builder);
        tweaks.init(builder);

        CLIENT_CONFIG = builder.build();
    }

    public static class tweaks {
        public static ForgeConfigSpec.BooleanValue COLORED_BWERING_STAND;
        private static void init(ForgeConfigSpec.Builder builder) {
            builder.comment("game tweaks")
                    .push("tweaks");
            COLORED_BWERING_STAND = builder.comment("make the brewing stand block get the correct color depending on the potions it's brewing")
                    .define("brewing_stand_colors",true);
            builder.pop();
        }
    }

    public static class general {
        public static ForgeConfigSpec.BooleanValue CONFIG_BUTTON;
        public static ForgeConfigSpec.BooleanValue TOOLTIP_HINTS;
        public static ForgeConfigSpec.BooleanValue ANTI_REPOST_WARNING;
        private static void init(ForgeConfigSpec.Builder builder) {
            builder.comment("general settings")
                    .push("general");
            CONFIG_BUTTON = builder.comment("enable Quark styled config button on main menu. Needs Configured installed to work")
                    .define("config_button",false);
            TOOLTIP_HINTS = builder.comment("show some tooltip hints to guide players through the mod")
                    .define("tooltip_hints",true);
            ANTI_REPOST_WARNING = builder.comment("tries to detect when the mod hasn't been downloaded from curseforge. " +
                    "set to false if you have manually changed the mod jar name")
                    .define("anti_reposting_warning", true);
            builder.pop();
        }
    }

    public static class block {
        public static ForgeConfigSpec.DoubleValue FIREFLY_SPAWN_CHANCE;
        public static ForgeConfigSpec.IntValue FIREFLY_SPAWN_PERIOD;

        public static ForgeConfigSpec.BooleanValue PEDESTAL_SPIN;
        public static ForgeConfigSpec.BooleanValue PEDESTAL_SWORD;
        public static ForgeConfigSpec.DoubleValue PEDESTAL_SPEED;
        public static ForgeConfigSpec.BooleanValue SHELF_TRANSLATE;
        public static ForgeConfigSpec.DoubleValue WIND_VANE_POWER_SCALING;
        public static ForgeConfigSpec.DoubleValue WIND_VANE_ANGLE_1;
        public static ForgeConfigSpec.DoubleValue WIND_VANE_ANGLE_2;
        public static ForgeConfigSpec.DoubleValue WIND_VANE_PERIOD_1;
        public static ForgeConfigSpec.DoubleValue WIND_VANE_PERIOD_2;
        public static ForgeConfigSpec.BooleanValue CLOCK_24H;
        public static ForgeConfigSpec.BooleanValue GLOBE_RANDOM;
        public static ForgeConfigSpec.ConfigValue<List<? extends List<String>>> GLOBE_COLORS;

        public static ForgeConfigSpec.DoubleValue FLAG_SEGMENT_LENGTH;
        public static ForgeConfigSpec.DoubleValue FLAG_SPEED;
        public static ForgeConfigSpec.DoubleValue FLAG_WAVELENGTH;
        public static ForgeConfigSpec.DoubleValue FLAG_AMPLITUDE;
        public static ForgeConfigSpec.DoubleValue FLAG_AMPLITUDE_INCREMENT;
        public static ForgeConfigSpec.ConfigValue<List<? extends List<String>>> CAPTURED_MOBS_PROPERTIES;


        //is there a way to store more complicated data structures inside Forge configs. For example I would like to store a list containing many lists made up of lets say a string and an int value

        private static void init(ForgeConfigSpec.Builder builder) {



            builder.comment("Tweak and change the various block animations +\n" +
                    "only cosmetic stuff here. Lots of maths to tweak every aspect of the animation. leave default if not interested \n"+
                    "remember to delete this and server configs and let it refresh every once in a while cause I might have tweaked it")

                    .push("blocks");

            builder.push("globe");
            GLOBE_RANDOM = builder.comment("enable a random globe texture for each world").define("random_world", true);

            GLOBE_COLORS = builder.comment("here you can put custom colors that will be assigned to each globe depending on its dimension:\n" +
                    "to do so you'll have to make a list of lists in the format [[id,colors...],[id,colors...],...]\n"+
                    "1: dimension id\n" +
                    "now follows 12 numbers that will be used to color all of the 17 possible 'biomes'. omitting even one will make the config invalid\n"+
                    "2: water, 3: water shaded, 4: water dark, 5: coast/taiga \n"+
                    "6: forest, 7: plains\n"+
                    "8: savanna, 9: desert\n"+
                    "10: snow, 11: ice, 12: iceberg/island\n"+
                    "13: mushroom island")
                    .defineList("globe_colors", GlobeDataGenerator.getDefaultConfig(),s->true);


            builder.pop();

            builder.push("clock_block");
            CLOCK_24H = builder.comment("display 24h time format. False for 12h format").define("24h_format", true);
            builder.pop();

            builder.push("firefly_jar");
            FIREFLY_SPAWN_PERIOD = builder.comment("particle spawn if this equation is true: time%period==0 and randomfloat>chance  where random float is a random number between 0.0 and 1.0\n"+
                    "how often they try to spawn")
                    .defineInRange("spawn_period",8, 1, 20);
            FIREFLY_SPAWN_CHANCE = builder.comment("spawn chance every period")
                    .defineInRange("spawn_chance", 0.3, 0, 1);
            builder.pop();

            builder.push("pedestal");
            PEDESTAL_SPIN = builder.comment("enable displayed item spin")
                    .define("spin",true);
            PEDESTAL_SPEED = builder.comment("spin speed")
                    .defineInRange("speed",2f,0f,100f);
            PEDESTAL_SWORD = builder.comment("enable swords to display inserted into the block")
                    .define("fancy_swords",true);
            builder.pop();

            builder.push("item_shelf");
            SHELF_TRANSLATE = builder.comment("translate down displayed 3d blocks so that they are touching the shelf. they will not be centered vertically this way")
                    .define("supported_blocks", true);
            builder.pop();

            builder.push("wind_vane");
            WIND_VANE_POWER_SCALING = builder.comment("wind vane swings according to this equation: \n+" +
                    "angle(time) = max_angle_1*sin(2pi*time*pow/period_1) + max_angle_2*sin(wpi*time*pow/period_2) \n+" +
                    "where pow=max(1,redstone_power*power_scaling)\n"+
                    "how much frequency changes depending on power. 2 means it spins twice as fast each power level (2* for rain, 4* for thunder)\n+" +
                    "increase to have more distinct indication when weather changes")
                    .defineInRange("power_scaling", 3.0, 1.0, 100.0);
            WIND_VANE_ANGLE_1 = builder.comment("amplitude (maximum angle) of first sine wave")
                    .defineInRange("max_angle_1", 30.0, 0, 360);
            WIND_VANE_ANGLE_2 = builder.defineInRange("max_angle_2", 10.0, 0, 360);
            WIND_VANE_PERIOD_1 = builder.comment("base period in ticks at 0 power of first sine wave")
                    .defineInRange("period_1", 450.0, 0.0, 2000.0);
            WIND_VANE_PERIOD_2 = builder.comment("this should be kept period_1/3 for a symmetric animation")
                    .defineInRange("period_2", 150.0, 0.0, 2000.0);
            builder.pop();

            builder.push("flag");
            FLAG_SPEED = builder.comment("I don't remember what these are, I'll adde proper descriptions later. names may be a bit random and non descriptive")
                    .defineInRange("speed", 1d, 0, 10);
            FLAG_WAVELENGTH = builder.defineInRange("wavelenght", 15d, 0.001, 100);
            FLAG_AMPLITUDE = builder.defineInRange("amplitude", 1d, 0d, 100d);
            FLAG_AMPLITUDE_INCREMENT = builder.comment("how much the wave amplitude increases each pixel")
                    .defineInRange("amplitude_increment", 0.3d, 0, 10);
            builder.pop();
            //TODO: add more(hourGlass, sawying blocks...)


            builder.push("captured_mobs");

            CAPTURED_MOBS_PROPERTIES = builder.comment("following here is a list of mob ids and 4 parameters that determine how they are displayed in jars and cages:\n"
                    +"1&2: these are the added height and width that will be added to the mob actual hitbox to determine its scale inside a cage or jar \n"+
                    "you can increase them so this 'adjusted hitbox' will match the actual mob shape, in other words increase the to make the mob smaller\n"+
                    "3: this one determines if the mob should emit light\n"+
                    "4: the last parameter determines the animation type. It can be set to the following values:\n" +
                    "-'air' to make it stand in mid air like a flying animal (note that such mobs are set to this value by default)\n" +
                    "-'land' to force it to stand on the ground even if it is a flying animal\n" +
                    "-'floating' to to make it stand in mid air and wobble up and down\n" +
                    "-any number >0 to make it render as a 2d fish. The ordinal of the texture that will be shown will be the number\n" +
                    "-0 or any other values will be ignored and treated as default")
                    .defineList("rendering_parameters", CapturedMobs.DEFAULT_CONFIG, s->true);

            builder.pop();

            builder.pop();
        }
    }



    public static class particle {
        public static ForgeConfigSpec.IntValue FIREFLY_PAR_MAXAGE;
        public static ForgeConfigSpec.DoubleValue FIREFLY_PAR_SCALE;
        private static void init(ForgeConfigSpec.Builder builder) {
            builder.comment("particle parameters")
                    .push("particles");
            builder.comment("firefly jar particle")
                    .push("firefly_glow");
            FIREFLY_PAR_SCALE = builder.comment("scale multiplier")
                    .defineInRange("scale", 0.075, 0,1);
            FIREFLY_PAR_MAXAGE = builder.comment("max age. Note that actual max age with be this + a random number between 0 and 10")
                    .defineInRange("max_age", 40, 1,256);
            builder.pop();

            builder.pop();
        }
    }

    public static class entity {
        public static ForgeConfigSpec.DoubleValue FIREFLY_SCALE;
        public static ForgeConfigSpec.DoubleValue FIREFLY_INTENSITY;
        public static ForgeConfigSpec.DoubleValue FIREFLY_EXPONENT;
        private static void init(ForgeConfigSpec.Builder builder) {
            builder.comment("entities parameters")
                    .push("entities");
            builder.push("firefly");
            FIREFLY_SCALE = builder.comment("glow animation uses following euation:\n"+
                    "alpha = scale = {max[(1-intensity)*sin(time*2pi/period)+intensity, 0]}^exponent\n"+
                    "period variable is located in common configs\n"+
                    "scale multiplier")
                    .defineInRange("scale", 0.15, 0,1);
            FIREFLY_INTENSITY = builder.comment("affects how long the pulse last, not how frequently it occurs. 0.5 for normal sin wave. higher and it won't turn off completely")
                    .defineInRange("intensity", 0.2,-100,1);
            FIREFLY_EXPONENT = builder.comment("affects the shape of the wave. stay under 0.5 for sharper transitions")
                    .defineInRange("exponent", 0.5, 0, 10);
            builder.pop();

            builder.pop();
        }
    }


    public static class cached {
        public static boolean COLORED_BWERING_STAND;
        public static boolean TOOLTIP_HINTS;
        public static int FIREFLY_PAR_MAXAGE;
        public static double FIREFLY_PAR_SCALE;
        public static double FIREFLY_SCALE;
        public static double FIREFLY_INTENSITY;
        public static double FIREFLY_EXPONENT;
        public static double FIREFLY_SPAWN_CHANCE;
        public static int FIREFLY_SPAWN_PERIOD;
        public static boolean PEDESTAL_SPIN;
        public static double PEDESTAL_SPEED;
        public static boolean PEDESTAL_SWORD;
        public static boolean SHELF_TRANSLATE;
        public static double WIND_VANE_POWER_SCALING;
        public static double WIND_VANE_ANGLE_1;
        public static double WIND_VANE_ANGLE_2;
        public static double WIND_VANE_PERIOD_1;
        public static double WIND_VANE_PERIOD_2;
        public static boolean CLOCK_24H;
        public static boolean GLOBE_RANDOM;
        public static boolean CONFIG_BUTTON;

        public static void refresh(){
            //tweaks
            COLORED_BWERING_STAND = tweaks.COLORED_BWERING_STAND.get();
            //general
            TOOLTIP_HINTS = general.TOOLTIP_HINTS.get();
            CONFIG_BUTTON = general.CONFIG_BUTTON.get();
            //particles
            FIREFLY_PAR_MAXAGE = particle.FIREFLY_PAR_MAXAGE.get();
            FIREFLY_PAR_SCALE = particle.FIREFLY_PAR_SCALE.get();
            //entities
            FIREFLY_SCALE = entity.FIREFLY_SCALE.get();
            FIREFLY_INTENSITY = entity.FIREFLY_INTENSITY.get();
            FIREFLY_EXPONENT = entity.FIREFLY_EXPONENT.get();
            //blocks
            FIREFLY_SPAWN_CHANCE = block.FIREFLY_SPAWN_CHANCE.get();
            FIREFLY_SPAWN_PERIOD = block.FIREFLY_SPAWN_PERIOD.get();
            PEDESTAL_SPIN = block.PEDESTAL_SPIN.get();
            PEDESTAL_SPEED = block.PEDESTAL_SPEED.get();
            PEDESTAL_SWORD = block.PEDESTAL_SWORD.get();
            SHELF_TRANSLATE = block.SHELF_TRANSLATE.get();
            WIND_VANE_POWER_SCALING = block.WIND_VANE_POWER_SCALING.get();
            WIND_VANE_ANGLE_1 = block.WIND_VANE_ANGLE_1.get();
            WIND_VANE_ANGLE_2 = block.WIND_VANE_ANGLE_2.get();
            WIND_VANE_PERIOD_1 = block.WIND_VANE_PERIOD_1.get();
            WIND_VANE_PERIOD_2 = block.WIND_VANE_PERIOD_2.get();
            CLOCK_24H = block.CLOCK_24H.get();
            GLOBE_RANDOM = block.GLOBE_RANDOM.get();

            CapturedMobs.refresh();
            GlobeDataGenerator.refreshColorsFromConfig();

        }
    }



}
