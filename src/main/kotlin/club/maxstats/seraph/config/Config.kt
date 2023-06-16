package club.maxstats.seraph.config

import club.maxstats.kolour.gui.*
import club.maxstats.kolour.render.FontStyle
import club.maxstats.kolour.util.Color
import club.maxstats.seraph.render.renderPlayerModel
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.inventory.GuiInventory
import net.minecraft.client.renderer.EntityRenderer
import net.minecraft.client.renderer.entity.RenderPlayer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.util.ChatComponentText
import org.lwjgl.input.Mouse

val configCategory = component {
    position = Position.RELATIVE
    alignItems = ItemAlignment.MIDDLE
    alignContent = ContentAlignment.MIDDLE

    width = 5.rem
    height = 5.rem
    margin = Sides(1.rem, 0.px, 1.rem, 0.px)

    backgroundColor = Color(60, 60, 60, 255)
    borderRadius = Radius(10.px)
}

val mainScreen = gui {
    id = "main-screen"
    position = Position.FIXED
    alignItems = ItemAlignment.MIDDLE
    alignContent = ContentAlignment.MIDDLE

    width = 100.vw
    height = 100.vh

    backgroundColor = Color(25, 25, 25, 100)
    blur = 18.px

    component {
        id = "content"
        position = Position.ABSOLUTE
        top = 100.vh
        width = 100.vw
        height = 1.rem

        component {
            id = "left-panel"
            position = Position.RELATIVE
            direction = AlignDirection.COLUMN
            alignItems = ItemAlignment.MIDDLE
            alignContent = ContentAlignment.MIDDLE

            width = 60.vw
            height = 100.vh

            component {
                id = "stat-container"
                position = Position.RELATIVE
                alignItems = ItemAlignment.MIDDLE
                alignContent = ContentAlignment.MIDDLE

                width = 60.vw
                height = 50.vh

                component {
                    id = "player-container"
                    alignItems = ItemAlignment.MIDDLE
                    alignContent = ContentAlignment.MIDDLE
                    width = 5.rem
                    height = 8.rem
                    backgroundColor = Color(45, 45, 45, 255)
                    borderRadius = Radius(6.px)

                    onRender {
                        // render player model using minecraft's method
                        val scale = 40
                        renderPlayerModel(
                            Minecraft.getMinecraft().thePlayer,
                            (width / 2),
                            (height / 2) + (scale - 5),
                            0f,
                            0f,
                            scale
                        )
                    }
                }
            }
        }
        component {
            id = "right-panel"
            position = Position.RELATIVE
            direction = AlignDirection.COLUMN
            alignItems = ItemAlignment.MIDDLE
            alignContent = ContentAlignment.MIDDLE
            height = 100.vh
            width = 40.vw

            header {
                fontStyle = FontStyle.BOLD
                text = "Seraph Settings"
                margin.bottom = 4.rem
            }
            (+configCategory).modify {
                bottom = 2.rem

                paragraph {
                    text = "General"
                }
            }
            (+configCategory).modify {
                paragraph {
                    text = "Display"
                }
            }
            (+configCategory).modify {
                top = 2.rem

                paragraph {
                    text = "Other"
                }
            }
        }
    }
}

class ConfigGui: GuiScreen() {
    var currentScreen: club.maxstats.kolour.gui.GuiScreen? = null

    override fun drawScreen(
        mouseX: Int,
        mouseY: Int,
        partialTicks: Float
    ) {
        if (currentScreen === null)
            currentScreen = mainScreen

        currentScreen!!.update(mouseX, mouseY)
        currentScreen!!.render(mouseX, mouseY)
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
        currentScreen!!.click(
            mouseX,
            mouseY,
            mouseButton
        )
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