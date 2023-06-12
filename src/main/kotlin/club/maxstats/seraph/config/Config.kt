package club.maxstats.seraph.config

import club.maxstats.kolour.gui.*
import club.maxstats.kolour.util.Color
import net.minecraft.client.gui.GuiScreen

class ConfigGui: GuiScreen() {
    val mainScreen = gui {
        position = Position.FIXED
        alignItems = ItemAlignment.MIDDLE
        alignContent = ContentAlignment.MIDDLE

        width = 100.vw
        height = 100.vh

        backgroundColor = Color(25, 25, 25, 100)
        blur = 18.px

        component {
            position = Position.ABSOLUTE
            alignItems = ItemAlignment.MIDDLE
            alignContent = ContentAlignment.MIDDLE

            top = 20.vh
            width = 100.vw
            height = 1.rem

            header {
                wrapText = false
                text = "Seraph Configuration Settings"
                color = Color.white
            }
        }

        component {
            position = Position.RELATIVE
            alignItems = ItemAlignment.MIDDLE
            alignContent = ContentAlignment.MIDDLE

            width = 100.vw
            height = 5.rem

            component {
                fontSize = 12
                width = 5.rem
                height = 5.rem
                margin = Sides(1.rem, 0.px, 1.rem, 0.px)

                backgroundColor = Color(60, 60, 60, 255)
                borderRadius = Radius(10.px)
            }
            component {
                fontSize = 12
                width = 5.rem
                height = 5.rem
                margin = Sides(1.rem, 0.px, 1.rem, 0.px)

                backgroundColor = Color(60, 60, 60, 255)
                borderRadius = Radius(10.px)
            }
            component {
                fontSize = 12
                width = 5.rem
                height = 5.rem
                margin = Sides(1.rem, 0.px, 1.rem, 0.px)

                backgroundColor = Color(60, 60, 60, 255)
                borderRadius = Radius(10.px)
            }
        }
    }

    override fun drawScreen(
        mouseX: Int,
        mouseY: Int,
        partialTicks: Float
    ) {
        mainScreen.update(mouseX, mouseY)
        mainScreen.render(mouseX, mouseY)
    }

    override fun keyTyped(
        char: Char,
        keyCode: Int
    ) {
        super.keyTyped(char, keyCode)
    }

    override fun mouseClicked(
        mouseX: Int,
        mouseY: Int,
        mouseButton: Int
    ) {

    }

    override fun mouseReleased(
        mouseX: Int,
        mouseY: Int,
        mouseButton: Int
    ) {

    }

    fun mouseScrolled(
        mouseX: Int,
        mouseY: Int,
        mouseButton: Int
    ) {

    }

    override fun doesGuiPauseGame() = false
}