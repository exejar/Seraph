package club.maxstats.seraph.gui

import club.maxstats.kolour.gui.*
import club.maxstats.kolour.render.FontStyle
import club.maxstats.kolour.util.Color
import club.maxstats.seraph.render.renderPlayerModel
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen

private val configCategory = component {
    style {
        position = Position.RELATIVE
        alignItems = ItemAlignment.MIDDLE
        alignContent = ContentAlignment.MIDDLE

        width = 5.rem
        height = 5.rem
        margin = Sides(1.rem, 0.px, 1.rem, 0.px)

        backgroundColor = Color(60, 60, 60, 255)
        borderRadius = Radius(10.px)
    }
}

private val mainScreen = gui {
    id = "main-screen"

    style {
        position = Position.FIXED
        alignItems = ItemAlignment.MIDDLE
        alignContent = ContentAlignment.MIDDLE

        width = 100.vw
        height = 100.vh

        backgroundColor = Color(25, 25, 25, 100)
        blur = 18.px
    }

    component {
        id = "content"

        style {
            position = Position.ABSOLUTE
            top = 100.vh
            width = 100.vw
            height = 1.rem
        }

        component {
            id = "left-panel"

            style {
                position = Position.RELATIVE
                direction = AlignDirection.COLUMN
                alignItems = ItemAlignment.MIDDLE
                alignContent = ContentAlignment.MIDDLE

                width = 60.vw
                height = 100.vh
            }

            component {
                id = "stat-container"

                style {
                    position = Position.RELATIVE
                    alignItems = ItemAlignment.MIDDLE
                    alignContent = ContentAlignment.MIDDLE

                    width = 60.vw
                    height = 50.vh
                }

                component {
                    id = "player-container"

                    style {
                        alignItems = ItemAlignment.MIDDLE
                        alignContent = ContentAlignment.MIDDLE
                        width = 5.rem
                        height = 8.rem
                        backgroundColor = Color(45, 45, 45, 255)
                        borderRadius = Radius(6.px)
                    }

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

            style {
                position = Position.RELATIVE
                direction = AlignDirection.COLUMN
                alignItems = ItemAlignment.MIDDLE
                alignContent = ContentAlignment.MIDDLE
                height = 100.vh
                width = 40.vw
            }

            header {
                style {
                    fontStyle = FontStyle.BOLD
                    text = "Seraph Settings"
                    margin.bottom = 4.rem
                }
            }
            +configCategory + {
                style.modify {
                    bottom = 2.rem
                }

                paragraph {
                    style {
                        text = "General"
                    }
                }
            }
            +configCategory + {
                paragraph {
                    style {
                        text = "Display"
                    }
                }
            }
            +configCategory + {
                style.modify {
                    top = 2.rem
                }

                paragraph {
                    style {
                        text = "Other"
                    }
                }
            }
        }
    }
}

class ConfigGui: GuiScreen() {
    var currentScreen: GuiComponent? = null

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