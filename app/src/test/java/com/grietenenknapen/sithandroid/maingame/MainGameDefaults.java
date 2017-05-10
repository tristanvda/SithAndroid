package com.grietenenknapen.sithandroid.maingame;

import com.grietenenknapen.sithandroid.model.database.SithCard;
import com.grietenenknapen.sithandroid.model.game.GameCardType;

class MainGameDefaults {

    private MainGameDefaults() {

    }

    static SithCard getSithCardBB8() {
        return SithCard.newBuilder()
                ._id(10L)
                .cardType(GameCardType.BB8)
                .name("BB8")
                .imageResId("card_bb8")
                .soundResId("bb8")
                .colorResId("card_bb8")
                .nameResId("bb8")
                .build();
    }

    static SithCard getSithCardBobaFett() {
        return SithCard.newBuilder()
                ._id(15L)
                .cardType(GameCardType.BOBA_FETT)
                .name("Boba Fett")
                .imageResId("card_boba_fett")
                .soundResId("boba_fett")
                .colorResId("card_boba_fett")
                .nameResId("boba_fett")
                .build();
    }

    static SithCard getSithCardChewBacca() {
        return SithCard.newBuilder()
                ._id(20L)
                .cardType(GameCardType.CHEWBACCA)
                .name("Chewbacca")
                .imageResId("card_chewbacca")
                .soundResId("chewbacca")
                .colorResId("card_chewbacca")
                .nameResId("chewbacca")
                .build();
    }

    static SithCard getSithCardDarthMaul() {
        return SithCard.newBuilder()
                ._id(25L)
                .cardType(GameCardType.SITH)
                .name("Darth Maul")
                .imageResId("card_darth_maul")
                .soundResId("darth_maul")
                .colorResId("card_darth_maul")
                .nameResId("darth_maul")
                .build();
    }

    static SithCard getSithCardDartVader() {
        return SithCard.newBuilder()
                ._id(30L)
                .cardType(GameCardType.SITH)
                .name("Darth Vader")
                .imageResId("card_darth_vader")
                .soundResId("darth_vader")
                .colorResId("card_darth_vader")
                .nameResId("darth_vader")
                .build();
    }

    static SithCard getSithCardFinn() {
        return SithCard.newBuilder()
                ._id(40L)
                .cardType(GameCardType.PEEPING_FINN)
                .name("Finn")
                .imageResId("card_finn")
                .soundResId("finn")
                .colorResId("card_finn")
                .nameResId("finn")
                .build();
    }

    static SithCard getSithCardHanSolo() {
        return SithCard.newBuilder()
                ._id(50L)
                .cardType(GameCardType.HAN_SOLO)
                .name("Han Solo")
                .imageResId("card_han_solo")
                .soundResId("han_solo")
                .colorResId("card_han_solo")
                .nameResId("han_solo")
                .build();
    }

    static SithCard getSithCardKyloRen() {
        return SithCard.newBuilder()
                ._id(60L)
                .cardType(GameCardType.KYLO_REN)
                .name("Kylo Ren")
                .imageResId("card_kylo_ren")
                .soundResId("kylo_ren")
                .colorResId("card_kylo_ren")
                .nameResId("kylo_ren")
                .build();
    }

    static SithCard getSithCardLuke() {
        return SithCard.newBuilder()
                ._id(70L)
                .cardType(GameCardType.JEDI)
                .name("Luke Skywalker")
                .imageResId("card_luke")
                .soundResId("luke")
                .colorResId("card_luke")
                .nameResId("luke_skywalker")
                .build();
    }

    static SithCard getSithCardMazKanata() {
        return SithCard.newBuilder()
                ._id(80L)
                .cardType(GameCardType.MAZ_KANATA)
                .name("Maz Kanata")
                .imageResId("card_maz_kanata")
                .soundResId("maz_kanata")
                .colorResId("card_maz_kanata")
                .nameResId("maz_kanata")
                .build();
    }

    static SithCard getSithCardObiWanKenobi() {
        return SithCard.newBuilder()
                ._id(90L)
                .cardType(GameCardType.JEDI)
                .name("Obi-Wan Kenobi")
                .imageResId("card_obi_wan")
                .soundResId("obi_wan")
                .colorResId("card_obi_wan")
                .nameResId("obi_wan_kenobi")
                .build();
    }

    static SithCard getSithCardRey() {
        return SithCard.newBuilder()
                ._id(100L)
                .cardType(GameCardType.JEDI)
                .name("Rey")
                .imageResId("card_rey")
                .soundResId("rey")
                .colorResId("card_rey")
                .nameResId("rey")
                .build();
    }

    static SithCard getSithCardTheEmperor() {
        return SithCard.newBuilder()
                ._id(110L)
                .cardType(GameCardType.SITH)
                .name("The Emperor")
                .imageResId("card_the_emperor")
                .soundResId("the_emperor")
                .colorResId("card_the_emperor")
                .nameResId("the_emperor")
                .build();
    }

    static SithCard getSithCardGeneralGrievous() {
        return SithCard.newBuilder()
                ._id(120L)
                .cardType(GameCardType.SITH)
                .name("General Grievous")
                .imageResId("card_general_grievous")
                .soundResId("general_grievous")
                .colorResId("card_general_grievous")
                .nameResId("general_grievous")
                .build();
    }
}
