package com.snowaze.app.services

import android.content.Context
import com.google.gson.Gson
import com.snowaze.app.model.Difficulty
import com.snowaze.app.model.JsonModel
import com.snowaze.app.model.SkiLift
import com.snowaze.app.model.SkiLiftType
import com.snowaze.app.model.Status
import com.snowaze.app.model.Track
import java.util.UUID

class InitDB {
    companion object {
        fun init(context: Context): String {
            // Pistes
            val ramiers = Track(
                UUID.randomUUID(),
                "Les ramiers",
                emptyList(),
                emptyList(),
                Difficulty.GREEN,
                Status.OPENED
            )
            val bambinos = Track(
                UUID.randomUUID(),
                "Les bambinos",
                emptyList(),
                emptyList(),
                Difficulty.GREEN,
                Status.OPENED
            )
            val monthery = Track(
                UUID.randomUUID(),
                "La Monthery",
                emptyList(),
                emptyList(),
                Difficulty.GREEN,
                Status.OPENED
            )
            val jonctionBasse = Track(
                UUID.randomUUID(),
                "Jonction basse",
                emptyList(),
                emptyList(),
                Difficulty.GREEN,
                Status.OPENED
            )
            val forest = Track(
                UUID.randomUUID(),
                "Le forest",
                emptyList(),
                emptyList(),
                Difficulty.GREEN,
                Status.OPENED
            )
            val bouticariVert = Track(
                UUID.randomUUID(),
                "Bouticari vert",
                emptyList(),
                emptyList(),
                Difficulty.GREEN,
                Status.OPENED
            )

            val casses = Track(
                UUID.randomUUID(),
                "Les casses",
                emptyList(),
                emptyList(),
                Difficulty.BLUE,
                Status.OPENED
            )
            val rhodos = Track(
                UUID.randomUUID(),
                "Les rhodos",
                emptyList(),
                emptyList(),
                Difficulty.BLUE,
                Status.OPENED
            )
            val nibettes = Track(
                UUID.randomUUID(),
                "Les nibettes",
                emptyList(),
                emptyList(),
                Difficulty.BLUE,
                Status.OPENED
            )
            val sDuChamois = Track(
                UUID.randomUUID(),
                "S du chamois",
                emptyList(),
                emptyList(),
                Difficulty.BLUE,
                Status.OPENED
            )
            val lampionsI = Track(
                UUID.randomUUID(),
                "Les lampions",
                emptyList(),
                emptyList(),
                Difficulty.BLUE,
                Status.OPENED
            )
            val lampionsII = Track(
                UUID.randomUUID(),
                "Les lampions",
                emptyList(),
                emptyList(),
                Difficulty.BLUE,
                Status.OPENED
            )
            val clouzeaux = Track(
                UUID.randomUUID(),
                "Les clouzeaux",
                emptyList(),
                emptyList(),
                Difficulty.BLUE,
                Status.OPENED
            )
            val gourq = Track(
                UUID.randomUUID(),
                "Le gourq",
                emptyList(),
                emptyList(),
                Difficulty.BLUE,
                Status.OPENED
            )
            val arbre = Track(
                UUID.randomUUID(),
                "L arbre",
                emptyList(),
                emptyList(),
                Difficulty.BLUE,
                Status.OPENED
            )
            val inglin = Track(
                UUID.randomUUID(),
                "L inglin",
                emptyList(),
                emptyList(),
                Difficulty.BLUE,
                Status.OPENED
            )
            val bouticariBleu = Track(
                UUID.randomUUID(),
                "Bouticari bleu",
                emptyList(),
                emptyList(),
                Difficulty.BLUE,
                Status.OPENED
            )
            val gerabio = Track(
                UUID.randomUUID(),
                "La gérabio",
                emptyList(),
                emptyList(),
                Difficulty.BLUE,
                Status.OPENED
            )

            val jockeys = Track(
                UUID.randomUUID(),
                "Les jockeys",
                emptyList(),
                emptyList(),
                Difficulty.RED,
                Status.OPENED
            )
            val chamoisRed = Track(
                UUID.randomUUID(),
                "Le chamois",
                emptyList(),
                emptyList(),
                Difficulty.RED,
                Status.OPENED
            )
            val ecureuil = Track(
                UUID.randomUUID(),
                "L écureuil",
                emptyList(),
                emptyList(),
                Difficulty.RED,
                Status.OPENED
            )
            val mandarine = Track(
                UUID.randomUUID(),
                "La mandarine",
                emptyList(),
                emptyList(),
                Difficulty.RED,
                Status.OPENED
            )
            val preMeanI = Track(
                UUID.randomUUID(),
                "Pré méan",
                emptyList(),
                emptyList(),
                Difficulty.RED,
                Status.OPENED
            )
            val preMeanII = Track(
                UUID.randomUUID(),
                "Pré méan",
                emptyList(),
                emptyList(),
                Difficulty.RED,
                Status.OPENED
            )
            val ousselat = Track(
                UUID.randomUUID(),
                "L ousselat",
                emptyList(),
                emptyList(),
                Difficulty.RED,
                Status.OPENED
            )
            val barrigart = Track(
                UUID.randomUUID(),
                "Barrigart",
                emptyList(),
                emptyList(),
                Difficulty.RED,
                Status.OPENED
            )
            val draye = Track(
                UUID.randomUUID(),
                "La draye",
                emptyList(),
                emptyList(),
                Difficulty.RED,
                Status.OPENED
            )
            val sagnieres = Track(
                UUID.randomUUID(),
                "Les sagnières",
                emptyList(),
                emptyList(),
                Difficulty.RED,
                Status.OPENED
            )
            val rougeBouticari = Track(
                UUID.randomUUID(),
                "La rouge bouticari",
                emptyList(),
                emptyList(),
                Difficulty.RED,
                Status.OPENED
            )

            val lievre = Track(
                UUID.randomUUID(),
                "Le lièvre",
                emptyList(),
                emptyList(),
                Difficulty.BLACK,
                Status.OPENED
            )
            val tetras = Track(
                UUID.randomUUID(),
                "Le tétras",
                emptyList(),
                emptyList(),
                Difficulty.BLACK,
                Status.OPENED
            )

            // Remontées
            val troikaLift = SkiLift(
                UUID.randomUUID(),
                "La troïka",
                emptyList(),
                emptyList(),
                SkiLiftType.TBAR,
                Status.OPENED
            )
            val torresLift = SkiLift(
                UUID.randomUUID(),
                "Les torres",
                emptyList(),
                emptyList(),
                SkiLiftType.TBAR,
                Status.OPENED
            )
            val moulinLift = SkiLift(
                UUID.randomUUID(),
                "Le moulin",
                emptyList(),
                emptyList(),
                SkiLiftType.TBAR,
                Status.OPENED
            )
            val amoureuxLift = SkiLift(
                UUID.randomUUID(),
                "Les amoureux",
                emptyList(),
                emptyList(),
                SkiLiftType.TBAR,
                Status.OPENED
            )
            val beauregardILift = SkiLift(
                UUID.randomUUID(),
                "Le beauregard I",
                emptyList(),
                emptyList(),
                SkiLiftType.TBAR,
                Status.OPENED
            )
            val beauregardIILift = SkiLift(
                UUID.randomUUID(),
                "Le beauregard II",
                emptyList(),
                emptyList(),
                SkiLiftType.TBAR,
                Status.OPENED
            )
            val steMarieMadeleineLift = SkiLift(
                UUID.randomUUID(),
                "Ste Marie Madeleine",
                emptyList(),
                emptyList(),
                SkiLiftType.TBAR,
                Status.OPENED
            )
            val bouticariILift = SkiLift(
                UUID.randomUUID(),
                "Bouticari I",
                emptyList(),
                emptyList(),
                SkiLiftType.TBAR,
                Status.OPENED
            )
            val bouticariIILift = SkiLift(
                UUID.randomUUID(),
                "Bouticari II",
                emptyList(),
                emptyList(),
                SkiLiftType.TBAR,
                Status.OPENED
            )
            val bouticariIIILift = SkiLift(
                UUID.randomUUID(),
                "Bouticari III",
                emptyList(),
                emptyList(),
                SkiLiftType.TBAR,
                Status.OPENED
            )
            val grandSerreLift = SkiLift(
                UUID.randomUUID(),
                "Le grand serre",
                emptyList(),
                emptyList(),
                SkiLiftType.TBAR,
                Status.OPENED
            )

            val burgeLift = SkiLift(
                UUID.randomUUID(),
                "La burge",
                emptyList(),
                emptyList(),
                SkiLiftType.CHAIRLIFT,
                Status.OPENED
            )
            val cassettesLift = SkiLift(
                UUID.randomUUID(),
                "Les cassettes",
                emptyList(),
                emptyList(),
                SkiLiftType.CHAIRLIFT,
                Status.OPENED
            )

            // Hop Green
            ramiers.hop = listOf(troikaLift, torresLift, burgeLift);
            bambinos.hop = listOf(troikaLift, torresLift, burgeLift);
            monthery.hop = listOf(ramiers, bambinos);
            jonctionBasse.hop = listOf(beauregardILift, beauregardIILift, monthery);
            forest.hop = listOf(
                jonctionBasse,
                bouticariVert,
                bouticariBleu,
                bouticariILift,
                bouticariIILift,
                bouticariIIILift
            );
            bouticariVert.hop =
                listOf(bouticariBleu, bouticariILift, bouticariIILift, bouticariIIILift);

            // Hop Blue
            rhodos.hop = listOf(moulinLift, monthery);
            nibettes.hop = listOf(moulinLift);
            casses.hop = listOf(ramiers, bambinos);
            sDuChamois.hop = listOf(jonctionBasse);
            lampionsI.hop = listOf(mandarine, arbre, lampionsII, clouzeaux, preMeanII);
            lampionsII.hop = listOf(jockeys, sDuChamois, chamoisRed);
            gourq.hop = listOf(lampionsII, clouzeaux, preMeanII);
            clouzeaux.hop = listOf(forest)
            arbre.hop = listOf(forest)
            bouticariBleu.hop = listOf(bouticariILift, bouticariIILift, bouticariIIILift);
            gerabio.hop = listOf(preMeanI, barrigart, inglin)
            inglin.hop = listOf(grandSerreLift, forest, bouticariVert, bouticariBleu);

            // Hop Red
            jockeys.hop = listOf(troikaLift, torresLift, burgeLift, amoureuxLift);
            chamoisRed.hop = listOf(troikaLift, torresLift, burgeLift);
            ecureuil.hop = listOf(jockeys, chamoisRed, sDuChamois);
            mandarine.hop = listOf(jockeys, chamoisRed, sDuChamois);
            preMeanI.hop = listOf(ousselat, preMeanII, lampionsII, clouzeaux)
            preMeanII.hop = listOf(steMarieMadeleineLift)
            barrigart.hop = listOf(forest)
            ousselat.hop = listOf(arbre, lampionsII, clouzeaux, arbre, preMeanII)
            rougeBouticari.hop = listOf(bouticariILift, bouticariIILift, bouticariIIILift);
            draye.hop = listOf(sagnieres, grandSerreLift, inglin);
            sagnieres.hop = listOf(bouticariILift, bouticariIILift, bouticariIIILift);

            // Hop Black
            lievre.hop = listOf(beauregardILift, beauregardIILift, monthery);
            tetras.hop = listOf(monthery);

            // Hop SkiLift
            troikaLift.hop = listOf(ramiers, bambinos);
            torresLift.hop = listOf(ramiers, bambinos);
            moulinLift.hop = listOf(rhodos, nibettes);
            amoureuxLift.hop = listOf(rhodos, nibettes);
            beauregardILift.hop = listOf(lampionsI, lievre);
            beauregardIILift.hop = listOf(ecureuil, gourq);
            amoureuxLift.hop = listOf(casses, beauregardILift, beauregardIILift, monthery);
            burgeLift.hop = listOf(jockeys, sDuChamois, chamoisRed);
            cassettesLift.hop = listOf(preMeanI, inglin, barrigart);
            grandSerreLift.hop = listOf(draye, gerabio);
            steMarieMadeleineLift.hop = listOf(arbre, lampionsII, clouzeaux, preMeanII);
            bouticariILift.hop = listOf(bouticariBleu)
            bouticariIILift.hop = listOf(bouticariVert)
            bouticariIIILift.hop = listOf(forest)

            // Liste des pistes
            val tracks = listOf(
                ramiers,
                bambinos,
                monthery,
                jonctionBasse,
                forest,
                bouticariVert,
                bouticariBleu,
                casses,
                rhodos,
                nibettes,
                sDuChamois,
                lampionsI,
                lampionsII,
                clouzeaux,
                gourq,
                arbre,
                inglin,
                bouticariBleu,
                gerabio,
                jockeys,
                chamoisRed,
                ecureuil,
                mandarine,
                preMeanI,
                preMeanII,
                ousselat,
                barrigart,
                draye,
                sagnieres,
                rougeBouticari,
                lievre,
                tetras
            )

            // Liste des remontées
            val skiLifts = listOf(
                troikaLift,
                torresLift,
                moulinLift,
                amoureuxLift,
                beauregardILift,
                beauregardIILift,
                steMarieMadeleineLift,
                bouticariILift,
                bouticariIILift,
                bouticariIIILift,
                grandSerreLift,
                burgeLift,
                cassettesLift
            )

            // Create a list of TracksJSON from the list of tracks, using the toJSON method
            val jsonTracks = tracks.map { it.toJSON() }

            // Create a json with the list of ski lifts
            val jsonSkiLifts = skiLifts.map { it.toJSON() }

            // Final JSON
            val jsonModel = JsonModel(
                jsonTracks.associateBy { it.id },
                jsonSkiLifts.associateBy { it.id },
            )

            return Gson().toJson(jsonModel);
        }
    }
}