package com.grietenenknapen.sithandroid.service.storage.tools;


import com.grietenenknapen.sithandroid.model.database.Player;
import com.grietenenknapen.sithandroid.model.database.SithCard;
import com.grietenenknapen.sithandroid.model.game.GameCardType;

import java.util.ArrayList;
import java.util.List;

public class DatabaseDefaults {

    public static SithCard getSithCardBB8() {
        return SithCard.newBuilder()
                .cardType(GameCardType.BB8)
                .name("BB8")
                .imageResId("card_bb8")
                .soundResId("bb8")
                .colorResId("card_bb8")
                .nameResId("bb8")
                .build();
    }

    public static SithCard getSithCardBobaFett() {
        return SithCard.newBuilder()
                .cardType(GameCardType.BOBA_FETT)
                .name("Boba Fett")
                .imageResId("card_boba_fett")
                .soundResId("boba_fett")
                .colorResId("card_boba_fett")
                .nameResId("boba_fett")
                .build();
    }

    public static SithCard getSithCardChewBacca() {
        return SithCard.newBuilder()
                .cardType(GameCardType.CHEWBACCA)
                .name("Chewbacca")
                .imageResId("card_chewbacca")
                .soundResId("chewbacca")
                .colorResId("card_chewbacca")
                .nameResId("chewbacca")
                .build();
    }

    public static SithCard getSithCardDarthMaul() {
        return SithCard.newBuilder()
                .cardType(GameCardType.SITH)
                .name("Darth Maul")
                .imageResId("card_darth_maul")
                .soundResId("darth_maul")
                .colorResId("card_darth_maul")
                .nameResId("darth_maul")
                .build();
    }

    public static SithCard getSithCardDartVader() {
        return SithCard.newBuilder()
                .cardType(GameCardType.SITH)
                .name("Darth Vader")
                .imageResId("card_darth_vader")
                .soundResId("darth_vader")
                .colorResId("card_darth_vader")
                .nameResId("darth_vader")
                .build();
    }

    public static SithCard getSithCardFinn() {
        return SithCard.newBuilder()
                .cardType(GameCardType.PEEPING_FINN)
                .name("Finn")
                .imageResId("card_finn")
                .soundResId("finn")
                .colorResId("card_finn")
                .nameResId("finn")
                .build();
    }

    public static SithCard getSithCardHanSolo() {
        return SithCard.newBuilder()
                .cardType(GameCardType.HAN_SOLO)
                .name("Han Solo")
                .imageResId("card_han_solo")
                .soundResId("han_solo")
                .colorResId("card_han_solo")
                .nameResId("han_solo")
                .build();
    }

    public static SithCard getSithCardKyloRen() {
        return SithCard.newBuilder()
                .cardType(GameCardType.KYLO_REN)
                .name("Kylo Ren")
                .imageResId("card_kylo_ren")
                .soundResId("kylo_ren")
                .colorResId("card_kylo_ren")
                .nameResId("kylo_ren")
                .build();
    }

    public static SithCard getSithCardLuke() {
        return SithCard.newBuilder()
                .cardType(GameCardType.JEDI)
                .name("Luke Skywalker")
                .imageResId("card_luke")
                .soundResId("luke")
                .colorResId("card_luke")
                .nameResId("luke_skywalker")
                .build();
    }

    public static SithCard getSithCardMazKanata() {
        return SithCard.newBuilder()
                .cardType(GameCardType.MAZ_KANATA)
                .name("Maz Kanata")
                .imageResId("card_maz_kanata")
                .soundResId("maz_kanata")
                .colorResId("card_maz_kanata")
                .nameResId("maz_kanata")
                .build();
    }

    public static SithCard getSithCardObiWanKenobi() {
        return SithCard.newBuilder()
                .cardType(GameCardType.JEDI)
                .name("Obi-Wan Kenobi")
                .imageResId("card_obi_wan")
                .soundResId("obi_wan")
                .colorResId("card_obi_wan")
                .nameResId("obi_wan_kenobi")
                .build();
    }

    public static SithCard getSithCardRey() {
        return SithCard.newBuilder()
                .cardType(GameCardType.JEDI)
                .name("Rey")
                .imageResId("card_rey")
                .soundResId("rey")
                .colorResId("card_rey")
                .nameResId("rey")
                .build();
    }

    public static SithCard getSithCardTheEmperor() {
        return SithCard.newBuilder()
                .cardType(GameCardType.SITH)
                .name("The Emperor")
                .imageResId("card_the_emperor")
                .soundResId("the_emperor")
                .colorResId("card_the_emperor")
                .nameResId("the_emperor")
                .build();
    }

    public static List<Player> generateBros() {
        List<Player> players = new ArrayList<>();
        return players;
    }

}
