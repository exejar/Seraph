package club.maxstats.seraph.gui

import club.maxstats.kolour.gui.*
import club.maxstats.kolour.util.Color
import club.maxstats.seraph.render.renderPlayerModel
import club.maxstats.seraph.util.createEntity
import club.maxstats.seraph.util.createGameProfile
import club.maxstats.seraph.util.mc
import net.minecraft.client.entity.EntityOtherPlayerMP
import net.minecraft.client.gui.GuiScreen

private val statLookup = component {
    id = "stat-lookup"
    style {
        width = 20.rem
        height = 25.rem
        position = Position.RELATIVE
        alignContent = ContentAlignment.MIDDLE
        alignItems = ItemAlignment.MIDDLE
        direction = AlignDirection.COLUMN

        backgroundColor = Color(35, 35, 35, 200)
        borderRadius = Radius(8.px)
    }

    (+playerLookup).style.modify {
        margin.bottom = 1.rem
    }

    component {
        id = "player-view"

        style {
            width = 4.rem
            height = 6.rem

            backgroundColor = Color(60, 60, 60, 200)
            borderRadius = Radius(8.px)

            var player = ""
            var entity: EntityOtherPlayerMP? = null

            onRender {
                // player retrieval test
                if (player.isEmpty()) {
                    player = getComponentById("stat-lookup-input")?.style?.text ?: "exejar"

                    val gameProfile = player.createGameProfile()
                    entity = gameProfile?.createEntity()
                }

                if (entity != null) {
                    val scale = 40
                    renderPlayerModel(
                        entity!!,
                        (width / 2),
                        (height / 2) + scale,
                        -(mouseX.toFloat() - (x + width / 2)),
                        -(mouseY.toFloat() - (y + height / 2)),
                        scale
                    )
                }
            }
        }
    }

    (+statBox).style.modify {
        margin.top = 1.rem
    }
}

private val playerLookup = component {
    id = "stat-lookup-search"

    style {
        width = 10.rem
        height = 2.5.rem
        position = Position.RELATIVE
        alignItems = ItemAlignment.MIDDLE
        direction = AlignDirection.COLUMN

        component {
            id = "stat-lookup-title"

            style {
                position = Position.RELATIVE
                width = 10.rem
                height = 1.rem
                padding.left = 0.25.rem

                alignItems = ItemAlignment.MIDDLE
            }

            paragraph {
                style {
                    text = "Player"
                    margin.bottom = 0.5.rem
                }
            }
        }

        component {
            id = "stat-lookup-textbar"

            style {
                position = Position.RELATIVE
                width = 10.rem
                height = 1.rem

                alignItems = ItemAlignment.MIDDLE

                padding = Sides(0.25.rem, 0.px, 0.25.rem, 0.px)

                backgroundColor = Color(60, 60, 60, 200)
                borderRadius = Radius(8.px)
            }

            paragraph {
                id = "stat-lookup-input"
                style {
                    text = "Terrible"
                }
            }
        }
    }
}

private val statBox = component {
    id = "stat-box"

    style {
        width = 10.rem
        height = 9.rem
        position = Position.RELATIVE
        direction = AlignDirection.COLUMN
    }

    component {
        id = "gamemode-swap"

        style {
            width = 4.rem
            height = 1.rem
            position = Position.RELATIVE

            alignItems = ItemAlignment.MIDDLE
            alignContent = ContentAlignment.MIDDLE

            backgroundColor = Color(60, 60, 60, 200)
            borderRadius = Radius(8.px)

            paragraph {
                style {
                    text = "Bedwars"
                }
            }
        }
    }

    component {
        id = "stat-view"

        style {
            width = 10.rem
            height = 7.rem
            position = Position.RELATIVE

            alignItems = ItemAlignment.MIDDLE
            alignContent = ContentAlignment.AROUND
            direction = AlignDirection.COLUMN

            margin.top = 0.25.rem

            backgroundColor = Color(60, 60, 60, 200)
            borderRadius = Radius(8.px)
        }

        +statBar + {
            id = "primary-stats"

            +stat + {
                paragraph {
                    style {
                        text = "Star"
                    }
                }
                paragraph {
                    style {
                        text = "4"
                    }
                }
            }
            +stat + {
                paragraph {
                    style {
                        text = "WS"
                    }
                }
                paragraph {
                    style {
                        text = "43"
                    }
                }
            }
            +stat + {
                paragraph {
                    style {
                        text = "FKDR"
                    }
                }
                paragraph {
                    style {
                        text = "111"
                    }
                }
            }
            +stat + {
                paragraph {
                    style {
                        text = "BBLR"
                    }
                }
                paragraph {
                    style {
                        text = "13"
                    }
                }
            }
        }

        +statBar + {
            id = "secondary-stats"

            +stat + {
                paragraph {
                    style {
                        text = "Games"
                    }
                }
                paragraph {
                    style {
                        text = "43"
                    }
                }
            }
            +stat + {
                paragraph {
                    style {
                        text = "Wins"
                    }
                }
                paragraph {
                    style {
                        text = "43"
                    }
                }
            }
            +stat + {
                paragraph {
                    style {
                        text = "FKS"
                    }
                }
                paragraph {
                    style {
                        text = "111"
                    }
                }
            }
            +stat + {
                paragraph {
                    style {
                        text = "Broken"
                    }
                }
                paragraph {
                    style {
                        text = "26"
                    }
                }
            }
        }

        +statBar + {
            id = "tertiary-stats"

            +stat + {
                paragraph {
                    style {
                        text = "Kills"
                    }
                }
                paragraph {
                    style {
                        text = "90"
                    }
                }
            }
            +stat + {
                paragraph {
                    style {
                        text = "Losses"
                    }
                }
                paragraph {
                    style {
                        text = "0"
                    }
                }
            }
            +stat + {
                paragraph {
                    style {
                        text = "FDS"
                    }
                }
                paragraph {
                    style {
                        text = "0"
                    }
                }
            }
            +stat + {
                paragraph {
                    style {
                        text = "Lost"
                    }
                }
                paragraph {
                    style {
                        text = "2"
                    }
                }
            }
        }
    }
}

private val statBar = component {
    style {
        width = 10.rem
        height = 2.rem
        position = Position.RELATIVE

        alignItems = ItemAlignment.MIDDLE
        alignContent = ContentAlignment.AROUND
    }
}

private val stat = component {
    style {
        width = 2.rem
        height = 2.rem
        position = Position.RELATIVE

        direction = AlignDirection.COLUMN
        alignItems = ItemAlignment.MIDDLE
        alignContent = ContentAlignment.MIDDLE
    }
}

private val closeButton = component {
    id = "close-button"

    style {
        width = 1.rem
        height = 1.rem
        position = Position.ABSOLUTE

        top = 0.5.rem
        right = 0.5.rem

        alignItems = ItemAlignment.MIDDLE
        alignContent = ContentAlignment.MIDDLE

        backgroundColor = Color(150, 25, 25, 175)
        borderRadius = Radius(8.px)

        onClick {
            // Close Screen
            mc.displayGuiScreen(null)
        }

        paragraph {
            style {
                text = "X"
            }
        }
    }
}

private val recentPlayers = component {
    id = "recent-players"
    style {
        width = 11.rem
        height = 11.rem
        position = Position.RELATIVE

        alignItems = ItemAlignment.MIDDLE
        alignContent = ContentAlignment.MIDDLE
        direction = AlignDirection.COLUMN

        backgroundColor = Color(35, 35, 35, 200)
        borderRadius = Radius(8.px)
    }

    paragraph {
        style {
            height = 1.rem
            text = "Recent Players"
        }
    }

    component {
        id = "recent-players-list"

        style {
            width = 9.5.rem
            height = 8.rem

            margin.top = 0.5.rem

            backgroundColor = Color(60, 60, 60, 200)
            borderRadius = Radius(8.px)
        }
    }
}

private val mainScreen = gui {
    id = "main"
    style {
        width = 100.vw
        height = 100.vh
        position = Position.FIXED
        blur = 18.px
        backgroundColor = Color(25, 25, 25, 50)
        alignContent = ContentAlignment.MIDDLE
        alignItems = ItemAlignment.MIDDLE
    }

    (+statLookup) + {
        +closeButton

        style = style.modify {
            margin.left = 7.rem
        }
    }
    (+recentPlayers).style.modify {
        margin.left = 1.5.rem
    }
}

class SeraphGui: GuiScreen() {
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