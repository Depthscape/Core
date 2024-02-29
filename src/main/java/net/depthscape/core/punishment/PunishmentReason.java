package net.depthscape.core.punishment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PunishmentReason {

//    SWEARING("Inappropriate (or rude)", 12 * 60 * 60, 7 * 60 * 60 * 24),
//    TROLLING("Trolling", 12 * 60 * 60, 7 * 60 * 60 * 24),
//    DISCRIMINATION("Negative behaviour", 12 * 60 * 60, 7 * 60 * 60 * 24),
//    SPAMMING("Spam", 12 * 60 * 60, 7 * 60 * 60 * 24),
//    VERBAL_ABUSE("Verbal abuse", 7 * 60 * 60 * 24, 30 * 60 * 60 * 24),
//    DEATH_THREATS("Death Threats", 7 * 60 * 60 * 24, 30 * 60 * 60 * 24),
//    DDOS_THREATS("DDoS Threats", -1, -1),
//    EXTREME_NEGATIVE_BEHAVIOUR("Extreme Negative behaviour", 12 * 60 * 60, 7 * 60 * 60 * 24),
//
//    ADVERTISING_SOCIAL_MEDIA("Advertising social media", 12 * 60 * 60, 7 * 60 * 60 * 24),
//    ADVERTISING_SERVER("Advertising", 12 * 60 * 60, 7 * 60 * 60 * 24),
//    ADVERTISING_WEBSITE("Website advertising", 12 * 60 * 60, 7 * 60 * 60 * 24),
//    SCAMMING("Attempting to scam", 12 * 60 * 60, 7 * 60 * 60 * 24),
//    PHISHING("Attempting to phising", 12 * 60 * 60, 7 * 60 * 60 * 24),
//
//    HACKING("Blacklisted modifications", 12 * 60 * 60, 7 * 60 * 60 * 24),
//    EXPLOITING("Exploiting", 12 * 60 * 60, 7 * 60 * 60 * 24),
//
//    BAD_NAME("Bad name. Please change before returning", 12 * 60 * 60, 7 * 60 * 60 * 24),
//    INAPPROPRIATE_SKIN("Inappropriate skin (or cape). Please change before returning.", 12 * 60 * 60, 7 * 60 * 60 * 24),
//    INAPPROPRIATE_CAPE("Inappropriate skin (or cape). Please change before returning.", 12 * 60 * 60, 7 * 60 * 60 * 24),
//    INAPPROPRIATE_BUILD("Inappropriate build", 12 * 60 * 60, 7 * 60 * 60 * 24);

    // ----------

    // Chat
    ADVERTISING("Advertising", 60 * 60 * 24, PunishmentType.MUTE, 60 * 60 * 24 * 3, PunishmentType.MUTE),
    SPAMMING("Spam", 60 * 15, PunishmentType.MUTE, 60 * 60 * 30, PunishmentType.MUTE),
    THREATS("Threats", 60 * 60 * 24, PunishmentType.MUTE, 60 * 60 * 24 * 2, PunishmentType.TEMP_BAN),
    TOXICITY("Toxicity", 60 * 30, PunishmentType.MUTE, 60 * 60, PunishmentType.MUTE),
    DISRESPECT("Disrespect", 60 * 30, PunishmentType.MUTE, 60 * 60, PunishmentType.MUTE),
    DISCRIMINATION("Discrimination", 60 * 60 * 24, PunishmentType.MUTE, 60 * 60 * 24 * 2, PunishmentType.TEMP_BAN),

    // Behaviour
    ABUSE("Abuse", 60 * 60 * 24 * 3, PunishmentType.TEMP_BAN, 60 * 60 * 24 * 7, PunishmentType.TEMP_BAN),
    GRIEFING("Griefing", 60 * 60 * 24 * 3, PunishmentType.TEMP_BAN, 60 * 60 * 24 * 7, PunishmentType.TEMP_BAN),
    BAN_EVASION("Ban Evasion", -1, PunishmentType.IP_BAN, -1, PunishmentType.IP_BAN),
    DUPING("Duping", -1, PunishmentType.IP_BAN, -1, PunishmentType.IP_BAN),
    MISC_BEHAVIOR("Negative Behaviour", 60 * 60 * 24 * 3, PunishmentType.TEMP_BAN, 60 * 60 * 24 * 7, PunishmentType.TEMP_BAN),

    // Mods
    XRAY("X-Ray", 60 * 60 * 24 * 30, PunishmentType.TEMP_BAN, -1, PunishmentType.IP_BAN),
    COMBAT_HACKS("Combat Hacks", 60 * 60 * 24 * 30, PunishmentType.TEMP_BAN, -1, PunishmentType.IP_BAN),
    MOVEMENT_HACKS("Movement Hacks", 60 * 60 * 24 * 30, PunishmentType.TEMP_BAN, -1, PunishmentType.IP_BAN),
    ANTI_AFK("Anti-AFK", 60 * 60 * 24 * 30, PunishmentType.TEMP_BAN, -1, PunishmentType.IP_BAN),
    BOTTING("Botting", 60 * 60 * 24 * 30, PunishmentType.TEMP_BAN, -1, PunishmentType.IP_BAN),
    MISC_HACKS("Miscellaneous Hacks", 60 * 60 * 24 * 30, PunishmentType.TEMP_BAN, -1, PunishmentType.IP_BAN);


    private final String reason;
    private final long firstTime;
    private final PunishmentType firstType;
    private final long secondTime;
    private final PunishmentType secondType;


    public long getDuration(int count) {
        if (count == 0) {
            return firstTime;
        } else if (count == 1) {
            return secondTime;
        } else {
            return -1;
        }
    }

    public PunishmentType getType(int count) {
        if (count == 0) {
            return firstType;
        } else if (count == 1) {
            return secondType;
        } else {
            return PunishmentType.IP_BAN;
        }
    }

    public static PunishmentReason fromReasonString(String reason) {
        for (PunishmentReason r : PunishmentReason.values()) {
            if (r.getReason().equalsIgnoreCase(reason)) {
                return r;
            }
        }
        return null;
    }
}
