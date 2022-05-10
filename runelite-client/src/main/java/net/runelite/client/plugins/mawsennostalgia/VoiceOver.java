package net.runelite.client.plugins.mawsennostalgia;

import java.util.HashMap;
import java.util.Map;

public enum VoiceOver {

    SIPHON("A siphon will solve this!", "a_siphon_will_solve_this.wav"),
    AT_LAST("AT LAST!", "at_last.wav"),
    CONTAIN_THIS("Contain this!", "contain_this.wav"),
    CRUOR("Cruor!", "cruor.wav"),
    CRUOR_FAIL("Cruor, don't fail me!", "cruor_dont_fail_me.wav"),
    DARKEN_SHADOW("Darken my shadow!", "darken_my_shadow.wav"),
    BLOOD_SACRIFICE("I demand a blood sacrifice!", "demand_blood_sacrifice.wav"),
    PRISON_OF_ICE("Die now, in a prison of ice!", "die_now_in_prison_of_ice.wav"),
    EMBRACE_DARKNESS("Embrace darkness!", "embrace_darkness.wav"),
    FEAR_THE_SHADOW("Fear the shadow!", "fear_the_shadow.wav"),
    SOUL_WITH_SMOKE("Fill my soul with smoke!", "fill_my_soul_with_smoke.wav"),
    FLOOD_MY_LUNGS("Flood my lungs with blood!", "flood_my_lungs_with_blood.wav"),
    FUMUS("Fumus!", "fumus.wav"),
    FUMUS_FAIL("Fumus, don't fail me!", "fumus_dont_fail_me.wav"),
    GLACIES("Glacies!", "glacies.wav"),
    GLACIES_FAIL("Glacies, don't fail me!", "glacies_dont_fail_me.wav"),
    POWER_OF_ICE("Infuse me with the power of ice!", "infuse_me_withe_power_of_ice.wav"),
    NO_ESCAPE("NO ESCAPE!", "no_escape.wav"),
    POWER_OF_ZAROS("NOW, THE POWER OF ZAROS!", "now_the_power_of_zaros.wav"),
    TASTE_MY_WRATH("Taste my wrath!", "taste_my_wrath.wav"),
    THERE_IS("There is...", "there_is.wav"),
    UMBRA("Umbra!", "umbra.wav"),
    UMBRA_FAIL("Umbra, don't fail me!", "umbra_dont_fail_me.wav"),
    VIRUS_FLOW("Let the virus flow through you!", "virus_flow_through_you.wav"),

    mawsenSIPHON("mawsenA siphon will solve this!", "mawsen_a_siphon_will_solve_this.wav"),
    mawsenAT_LAST("mawsenAT LAST!", "mawsen_at_last.wav"),
    mawsenCONTAIN_THIS("mawsenContain this!", "mawsen_contain_this.wav"),
    mawsenCRUOR("mawsenCruor!", "mawsen_cruor.wav"),
    mawsenCRUOR_FAIL("mawsenCruor, don't fail me!", "mawsen_cruor_dont_fail_me.wav"),
    mawsenDARKEN_SHADOW("mawsenDarken my shadow!", "mawsen_darken_my_shadow.wav"),
    mawsenBLOOD_SACRIFICE("mawsenI demand a blood sacrifice!", "mawsen_demand_blood_sacrifice.wav"),
    mawsenPRISON_OF_ICE("mawsenDie now, in a prison of ice!", "mawsen_die_now_in_prison_of_ice.wav"),
    mawsenEMBRACE_DARKNESS("mawsenEmbrace darkness!", "mawsen_embrace_darkness.wav"),
    mawsenFEAR_THE_SHADOW("mawsenFear the shadow!", "mawsen_fear_the_shadow.wav"),
    mawsenSOUL_WITH_SMOKE("mawsenFill my soul with smoke!", "mawsen_fill_my_soul_with_smoke.wav"),
    mawsenFLOOD_MY_LUNGS("mawsenFlood my lungs with blood!", "mawsen_flood_my_lungs_with_blood.wav"),
    mawsenFUMUS("mawsenFumus!", "mawsen_fumus.wav"),
    mawsenFUMUS_FAIL("mawsenFumus, don't fail me!", "mawsen_fumus_dont_fail_me.wav"),
    mawsenGLACIES("mawsenGlacies!", "mawsen_glacies.wav"),
    mawsenGLACIES_FAIL("mawsenGlacies, don't fail me!", "mawsen_glacies_dont_fail_me.wav"),
    mawsenPOWER_OF_ICE("mawsenInfuse me with the power of ice!", "mawsen_infuse_me_withe_power_of_ice.wav"),
    mawsenNO_ESCAPE("mawsenNO ESCAPE!", "mawsen_no_escape.wav"),
    mawsenPOWER_OF_ZAROS("mawsenNOW, THE POWER OF ZAROS!", "mawsen_now_the_power_of_zaros.wav"),
    mawsenTASTE_MY_WRATH("mawsenTaste my wrath!", "mawsen_taste_my_wrath.wav"),
    mawsenTHERE_IS("mawsenThere is...", "mawsen_there_is.wav"),
    mawsenUMBRA("mawsenUmbra!", "mawsen_umbra.wav"),
    mawsenUMBRA_FAIL("mawsenUmbra, don't fail me!", "mawsen_umbra_dont_fail_me.wav"),
    mawsenVIRUS_FLOW("mawsenLet the virus flow through you!", "mawsen_virus_flow_through_you.wav");

    private static final Map<String, VoiceOver> triggerLines = new HashMap<>();

    static {
        for (VoiceOver voiceOver : values()) {
            triggerLines.put(voiceOver.triggerLine, voiceOver);
        }
    }

    private final String triggerLine;
    private final String file;

    VoiceOver(String triggerLine, String file) {
        this.triggerLine = triggerLine;
        this.file = file;
    }

    public static VoiceOver forTriggerLine(String triggerLine, String custom) {
        return triggerLines.get(custom+triggerLine);
    }

    public String trigger() {
        return triggerLine;
    }

    public String file() {
        return file;
    }
}