// 
// Decompiled by Procyon v0.5.36
// 

package io.fazal.cloudpass.challenges;

public enum ChallengeType
{
    BREAK("Break", 0), 
    CONSUME("Consume", 1), 
    CRAFT("Craft", 2), 
    FACTION_CREATE("FactionCreate", 3), 
    FISH("Fish", 4), 
    KILL_BOSS_MOB("KillBossMob", 5), 
    KILL_ENEMY_PLAYER("KillEnemyPlayer", 6), 
    KILL_MOB("KillMob", 7), 
    KILL_PLAYER("KillPlayer", 8), 
    KILL_PLAYER_REGION("KillPlayerRegion", 9), 
    MINE_SPAWNER("MineSpawner", 11), 
    OPEN_CRATE("OpenCrate", 12), 
    OPEN_SUPPLY_CRATE("OpenSupplyCrate", 13), 
    PLACE("Place", 14), 
    PLACE_SPAWNER("PlaceSpawner", 15), 
    TYPE("Type", 16), 
    VOTE("Vote", 17);
    
    private final String name;
    
    ChallengeType(final String name, final int index) {
        this.name = name;
    }
    
    public static ChallengeType getByName(final String name) {
        ChallengeType[] values;
        for (int index = (values = values()).length, i = 0; i < index; ++i) {
            final ChallengeType challenge = values[i];
            if (challenge.getName().equalsIgnoreCase(name) || challenge.name.equalsIgnoreCase(name)) {
                return challenge;
            }
        }
        return null;
    }
    
    public String getName() {
        return this.name;
    }
}
