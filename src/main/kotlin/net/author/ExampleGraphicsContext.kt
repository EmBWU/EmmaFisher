package net.author

import net.botwithus.rs3.imgui.ImGui
import net.botwithus.rs3.imgui.NativeInteger
import net.botwithus.rs3.script.ScriptConsole
import net.botwithus.rs3.script.ScriptGraphicsContext

class ExampleGraphicsContext(
    private val script: ExampleScript,
    console: ScriptConsole
) : ScriptGraphicsContext (console) {

    override fun drawSettings() {
        super.drawSettings()
        ImGui.Begin("Emmas Fisher", 0)
        ImGui.SetWindowSize(250f, -1f)
        ImGui.Text("State: " + script.botState)

        val currentItem = NativeInteger(script.getCurrentFish)
        if (ImGui.Combo("Select Shop", currentItem, *fish.map { it.name }.toTypedArray())) {
            for (i in fish.indices) {
                if (currentItem.get() == i) {
                    script.getCurrentFish = i
                }
            }
        }

        if (ImGui.Button("Start")) {
            script.botState = ExampleScript.BotState.SKILLING;
        }
        ImGui.SameLine()
        if (ImGui.Button("Stop")) {
            script.botState = ExampleScript.BotState.IDLE
        }
        ImGui.End()
    }

    override fun drawOverlay() {
        super.drawOverlay()
    }

}