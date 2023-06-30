package club.maxstats.seraph.render

import club.maxstats.seraph.util.mc
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.AbstractClientPlayer
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.client.renderer.entity.RenderPlayer
import net.minecraft.entity.EntityLivingBase

val slimPlayerRenderer = SimplePlayerRender(mc.renderManager, true)
val playerRenderer = SimplePlayerRender(mc.renderManager, false)

fun renderPlayerModel(
    player: EntityLivingBase,
    x: Float,
    y: Float,
    mouseX: Float,
    mouseY: Float,
    scale: Int,
) {
    if (player is AbstractClientPlayer) {
        GlStateManager.enableColorMaterial()
        GlStateManager.pushMatrix()
        GlStateManager.translate(x, y, 50.0f)
        GlStateManager.scale((-scale).toFloat(), scale.toFloat(), scale.toFloat())
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f)

        val f: Float = player.renderYawOffset
        val g: Float = player.rotationYaw
        val h: Float = player.rotationPitch
        val i: Float = player.prevRotationYawHead
        val j: Float = player.rotationYawHead

        GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f)
        RenderHelper.enableStandardItemLighting()
        GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f)
        GlStateManager.rotate(-Math.atan((mouseY / 40.0f).toDouble()).toFloat() * 20.0f, 1.0f, 0.0f, 0.0f)

        player.renderYawOffset = Math.atan((mouseX / 40.0f).toDouble()).toFloat() * 20.0f
        player.rotationYaw = Math.atan((mouseX / 40.0f).toDouble()).toFloat() * 40.0f
        player.rotationPitch = -Math.atan((mouseY / 40.0f).toDouble()).toFloat() * 20.0f
        player.rotationYawHead = player.rotationYaw
        player.prevRotationYawHead = player.rotationYaw

        GlStateManager.translate(0.0f, 0.0f, 0.0f)

        val renderManager = Minecraft.getMinecraft().renderManager
        renderManager.setPlayerViewY(180.0f)
        renderManager.isRenderShadow = false

        val renderer = if (player.skinType == "slim")
            slimPlayerRenderer
        else
            playerRenderer

        renderer.doRender(
            player,
            0.0,
            0.0,
            0.0,
            0f,
            1f
        )

        renderManager.isRenderShadow = true

        player.renderYawOffset = f
        player.rotationYaw = g
        player.rotationPitch = h
        player.prevRotationYawHead = i
        player.rotationYawHead = j

        GlStateManager.popMatrix()
        RenderHelper.disableStandardItemLighting()
        GlStateManager.disableRescaleNormal()
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit)
        GlStateManager.disableTexture2D()
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit)
    }
}

/**
 * Render's a player's base skin model, does not include nametag, armor, or held item
 */
class SimplePlayerRender(
    renderManager: RenderManager,
    slim: Boolean
): RenderPlayer(
    renderManager,
    slim
) {
    override fun renderName(p0: AbstractClientPlayer?, p1: Double, p2: Double, p3: Double) {
        // don't render name
    }

    override fun doRender(entity: AbstractClientPlayer, x: Double, y: Double, z: Double, entityYaw: Float, partialTicks: Float) {
        if (!entity.isUser || renderManager.livingPlayer == entity) {
            var newY = y

            if (entity.isSneaking && entity !is EntityPlayerSP)
                newY = y - 0.125

            setAllVisible(entity)
            super.doRender(entity, x, newY, z, entityYaw, partialTicks)
        }
    }

    private fun setAllVisible(clientPlayer: AbstractClientPlayer) {
        val modelPlayer = getMainModel()

        modelPlayer.setInvisible(true)
        modelPlayer.bipedHeadwear.showModel = true
        modelPlayer.bipedBodyWear.showModel = true
        modelPlayer.bipedLeftLegwear.showModel = true
        modelPlayer.bipedRightLegwear.showModel = true
        modelPlayer.bipedLeftArmwear.showModel = true
        modelPlayer.bipedRightArmwear.showModel = true
        modelPlayer.heldItemLeft = 0
        modelPlayer.heldItemRight = 0
        modelPlayer.aimedBow = false
        modelPlayer.isSneak = clientPlayer.isSneaking
    }
}