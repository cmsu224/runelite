/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.socket.plugins.socketvangpots;

public class CoxUtil {
    private static final int COX_ROOM_MASK = 67059680;
    private static final int FL_END1 = 6689792;
    private static final int FL_END2 = 6689824;
    private static final int FL_END3 = 6755360;
    private static final int LOBBY_CCW = 6689856;
    private static final int LOBBY_THRU = 6755392;
    private static final int LOBBY_CW = 6820928;
    private static final int SCAVS_SM_CCW = 6689888;
    private static final int SCAVS_SM_THRU = 6755424;
    private static final int SCAVS_SM_CW = 6820960;
    private static final int SHAMANS_CCW = 6689920;
    private static final int SHAMANS_THRU = 6755456;
    private static final int SHAMANS_CW = 6820992;
    private static final int VASA_CCW = 6689952;
    private static final int VASA_THRU = 6755488;
    private static final int VASA_CW = 6821024;
    private static final int VANGUARDS_CCW = 6689984;
    private static final int VANGUARDS_THRU = 6755520;
    private static final int VANGUARDS_CW = 6821056;
    private static final int ICE_DEMON_CCW = 6690016;
    private static final int ICE_DEMON_THRU = 6755552;
    private static final int ICE_DEMON_CW = 6821088;
    private static final int THIEVING_CCW = 6690048;
    private static final int THIEVING_THRU = 6755584;
    private static final int THIEVING_CW = 6821120;
    private static final int FARM_FISH_CCW = 6690112;
    private static final int FARM_FISH_THRU = 6755648;
    private static final int FARM_FISH_CW = 6821184;
    private static final int FL_START1_CCW = 6690368;
    private static final int FL_START1_THRU = 6755904;
    private static final int FL_START1_CW = 6821440;
    private static final int FL_START2_CCW = 0x661660;
    private static final int FL_START2_THRU = 6755936;
    private static final int FL_START2_CW = 6821472;
    private static final int SCAVS_LG_CCW = 23467104;
    private static final int SCAVS_LG_THRU = 23532640;
    private static final int SCAVS_LG_CW = 23598176;
    private static final int MYSTICS_CCW = 23467136;
    private static final int MYSTICS_THRU = 23532672;
    private static final int MYSTICS_CW = 23598208;
    private static final int TEKTON_CCW = 23467168;
    private static final int TEKTON_THRU = 23532704;
    private static final int TEKTON_CW = 23598240;
    private static final int MUTTADILES_CCW = 23467200;
    private static final int MUTTADILES_THRU = 23532736;
    private static final int MUTTADILES_CW = 23598272;
    private static final int TIGHTROPE_CCW = 23467232;
    private static final int TIGHTROPE_THRU = 23532768;
    private static final int TIGHTROPE_CW = 23598304;
    private static final int FARM_BATS_CCW = 23467328;
    private static final int FARM_BATS_THRU = 23532864;
    private static final int FARM_BATS_CW = 23598400;
    private static final int GUARDIANS_CCW = 40244352;
    private static final int GUARDIANS_THRU = 40309888;
    private static final int GUARDIANS_CW = 40375424;
    private static final int VESPULA_CCW = 40244384;
    private static final int VESPULA_THRU = 40309920;
    private static final int VESPULA_CW = 40375456;
    private static final int CRABS_CCW = 40244448;
    private static final int CRABS_THRU = 40309984;
    private static final int CRABS_CW = 40375520;
    private static final int OLM_ROOM_MASK = 66994112;
    private static final int OLM_ = 6559296;
    public static final int FL_START = 0;
    public static final int FL_END = 1;
    public static final int SCAVENGERS = 2;
    public static final int FARMING = 3;
    public static final int SHAMANS = 4;
    public static final int VASA = 5;
    public static final int VANGUARDS = 6;
    public static final int MYSTICS = 7;
    public static final int TEKTON = 8;
    public static final int MUTTADILES = 9;
    public static final int GUARDIANS = 10;
    public static final int VESPULA = 11;
    public static final int ICE_DEMON = 12;
    public static final int THIEVING = 13;
    public static final int TIGHTROPE = 14;
    public static final int CRABS = 15;
    public static final int OLM = 16;
    public static final int UNKNOWN = 17;
    private static final char[] ROOM_SORTS = new char[]{'*', '*', 'S', 'F', 'C', 'C', 'C', 'C', 'C', 'C', 'C', 'C', 'P', 'P', 'P', 'P', 'O'};
    private static final String[] ROOM_NAMES = new String[]{"Floor start", "Floor end", "Scavengers", "Farming", "Shamans", "Vasa", "Vanguards", "Mystics", "Tekton", "Muttadiles", "Guardians", "Vespula", "Ice demon", "Thieving", "Tightrope", "Crabs", "Olm"};

    public static int getroom_type(int zonecode) {
        switch (zonecode & 0x3FF3FE0) {
            case 6689856: 
            case 6690368: 
            case 0x661660: 
            case 6755392: 
            case 6755904: 
            case 6755936: 
            case 6820928: 
            case 6821440: 
            case 6821472: {
                return 0;
            }
            case 6689792: 
            case 6689824: 
            case 6755360: {
                return 1;
            }
            case 6689888: 
            case 6755424: 
            case 6820960: 
            case 23467104: 
            case 23532640: 
            case 23598176: {
                return 2;
            }
            case 6690112: 
            case 6755648: 
            case 6821184: 
            case 23467328: 
            case 23532864: 
            case 23598400: {
                return 3;
            }
            case 6689920: 
            case 6755456: 
            case 6820992: {
                return 4;
            }
            case 6689952: 
            case 6755488: 
            case 6821024: {
                return 5;
            }
            case 6689984: 
            case 6755520: 
            case 6821056: {
                return 6;
            }
            case 23467136: 
            case 23532672: 
            case 23598208: {
                return 7;
            }
            case 23467168: 
            case 23532704: 
            case 23598240: {
                return 8;
            }
            case 23467200: 
            case 23532736: 
            case 23598272: {
                return 9;
            }
            case 40244352: 
            case 40309888: 
            case 40375424: {
                return 10;
            }
            case 40244384: 
            case 40309920: 
            case 40375456: {
                return 11;
            }
            case 6690016: 
            case 6755552: 
            case 6821088: {
                return 12;
            }
            case 6690048: 
            case 6755584: 
            case 6821120: {
                return 13;
            }
            case 23467232: 
            case 23532768: 
            case 23598304: {
                return 14;
            }
            case 40244448: 
            case 40309984: 
            case 40375520: {
                return 15;
            }
        }
        if ((zonecode & 0x3FE3FC0) == 6559296) {
            return 16;
        }
        return 17;
    }

    public static char getroom_sort(int roomtype) {
        if (roomtype >= 0 && roomtype < 17) {
            return ROOM_SORTS[roomtype];
        }
        return '?';
    }

    public static String getroom_name(int roomtype) {
        if (roomtype >= 0 && roomtype < 17) {
            return ROOM_NAMES[roomtype];
        }
        return "Unknown";
    }

    public static int room_winding(int zonecode) {
        return (zonecode >> 16 & 0xFF) - 103 & 3;
    }

    public static int room_rot(int zonecode) {
        return zonecode >> 1 & 3;
    }

    public static int room_exitside(int zonecode) {
        return CoxUtil.room_winding(zonecode) + CoxUtil.room_rot(zonecode) & 3;
    }
}

