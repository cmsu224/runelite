/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.http.api.worlds.WorldRegion
 */
package net.runelite.client.plugins.maz.plugins.socket.plugins.socketworldhopper;

import net.runelite.http.api.worlds.WorldRegion;

enum RegionFilterMode {
    AUSTRALIA(WorldRegion.AUSTRALIA),
    GERMANY(WorldRegion.GERMANY),
    UNITED_KINGDOM(WorldRegion.UNITED_KINGDOM){

        public String toString() {
            return "U.K.";
        }
    }
    ,
    UNITED_STATES(WorldRegion.UNITED_STATES_OF_AMERICA){

        public String toString() {
            return "USA";
        }
    };

    private final WorldRegion region;

    static RegionFilterMode of(WorldRegion region) {
        switch (region) {
            case UNITED_STATES_OF_AMERICA: {
                return UNITED_STATES;
            }
            case UNITED_KINGDOM: {
                return UNITED_KINGDOM;
            }
            case AUSTRALIA: {
                return AUSTRALIA;
            }
            case GERMANY: {
                return GERMANY;
            }
        }
        throw new IllegalStateException();
    }

    private RegionFilterMode(WorldRegion region) {
        this.region = region;
    }

    public WorldRegion getRegion() {
        return this.region;
    }
}

