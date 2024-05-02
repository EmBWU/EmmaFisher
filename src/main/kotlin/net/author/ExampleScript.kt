package net.author

import net.botwithus.api.game.hud.inventories.Backpack
import net.botwithus.api.game.hud.inventories.DepositBox
import net.botwithus.internal.scripts.ScriptDefinition
import net.botwithus.rs3.game.Client
import net.botwithus.rs3.game.minimenu.MiniMenu
import net.botwithus.rs3.game.minimenu.actions.ComponentAction
import net.botwithus.rs3.game.queries.builders.characters.NpcQuery
import net.botwithus.rs3.game.queries.builders.components.ComponentQuery
import net.botwithus.rs3.game.queries.builders.items.InventoryItemQuery
import net.botwithus.rs3.game.queries.builders.objects.SceneObjectQuery
import net.botwithus.rs3.game.scene.entities.characters.player.Player
import net.botwithus.rs3.game.scene.entities.`object`.SceneObject
import net.botwithus.rs3.imgui.NativeBoolean
import net.botwithus.rs3.imgui.NativeInteger
import net.botwithus.rs3.script.Execution
import net.botwithus.rs3.script.LoopingScript
import net.botwithus.rs3.script.ScriptConsole
import net.botwithus.rs3.script.config.ScriptConfig
import java.util.*
import java.util.regex.Pattern


class ExampleScript(
    name: String,
    scriptConfig: ScriptConfig,
    scriptDefinition: ScriptDefinition
) : LoopingScript(name, scriptConfig, scriptDefinition) {

    val random: Random = Random()
    var botState: BotState = BotState.IDLE
    var getCurrentFish = 0


    enum class BotState {
        IDLE,
        SKILLING,

    }

    override fun initialize(): Boolean {
        super.initialize()
        // Set the script graphics context to our custom one
        this.sgc = ExampleGraphicsContext(this, console)
        println("My script loaded!")
        return true;
    }

    override fun onLoop() {
        val player = Client.getLocalPlayer();
        if (Client.getGameState() != Client.GameState.LOGGED_IN || player == null || botState == BotState.IDLE) {
            Execution.delay(random.nextLong(2500, 5500))
            return
        }
        when (botState) {
            BotState.SKILLING -> {
                Execution.delay(handleSkilling(player))
                return
            }


            else -> {
                println("Unexpected bot state, report to author!")
            }
        }
        Execution.delay(random.nextLong(200, 1000))
        return
    }

    fun Logout() {
        MiniMenu.interact(ComponentAction.COMPONENT.type, 1, 7, 93782016)

        val logoutmenu = ComponentQuery.newQuery(1433).componentIndex(71).results().first()
        if (logoutmenu != null) {
            ScriptConsole.println("Logout Status: " + logoutmenu.interact(1))
            Execution.delay(200)
            MiniMenu.interact(ComponentAction.COMPONENT.type, 1, -1, 93913173)
        } else {
            ScriptConsole.println("Logout Button not found.")
        }
    }

    private fun handleSkilling(player: Player): Long {
        val getCurrentFish = fish[getCurrentFish]
        if (player.isMoving || player.animationId != -1)
            return random.nextLong(1000, 2000)
        val fishbait = InventoryItemQuery.newQuery().name("Feather").results().first()
        if (fishbait == null && getCurrentFish.interact == "Bait") {
            Logout()
            return random.nextLong(1000, 2000)
        }
        if (Backpack.isFull()) {
            while (true) {
                var hasFish = InventoryItemQuery.newQuery().name("Raw trout").results().random()
                var hasFish2 = InventoryItemQuery.newQuery().name("Raw salmon").results().random()
                if (hasFish != null) {
                    Backpack.interact("Raw trout", "Drop")
                    return random.nextLong(300, 500) // delay after dropping each fish
                } else if (hasFish2 != null) {
                    Backpack.interact("Raw salmon", "Drop")
                    return random.nextLong(300, 500) // delay after dropping each fish
                } else {
                    break
                }
            }
        } else {
            val FishingSpot = NpcQuery.newQuery().name("Fishing spot").results().nearest()
            if (FishingSpot != null) {
                FishingSpot.interact(getCurrentFish.interact).also { println("Interacted with " + FishingSpot.name) }
            }
        }

        return random.nextLong(1000, 3000)
    }
}