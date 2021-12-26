package io.github.changwook987.uniqueEnchant.plugin

import io.github.changwook987.uniqueEnchant.util.drawCircle
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.LightningStrike
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Logger
import kotlin.math.PI
import kotlin.math.atan2

class EventListener(
    plugin: JavaPlugin
) : Listener {
    private val logger: Logger

    init {
        plugin.server.pluginManager.registerEvents(this, plugin)
        this.logger = plugin.logger
    }


    @EventHandler
    fun lighting(event: EntityDamageByEntityEvent) {
        if (event.damager is Player) {
            val player = (event.damager as Player).player ?: return
            val item = player.inventory.itemInMainHand

            if (item.hasUniqueEnchant("lightning")) {
                event.entity.apply {
                    val lightning = world.spawnEntity(location, EntityType.LIGHTNING) as LightningStrike
                    lightning.setCausingPlayer(player)
                }
            }
        }
    }

    @EventHandler
    fun ludensEcho(event: EntityDamageByEntityEvent) {
        if (event.damager is Player) {
            val player = (event.damager as Player).player ?: return
            val item = player.inventory.itemInMainHand

            if (item.hasUniqueEnchant("ludens-Echo")) {
                event.entity.apply {
                    val entities =
                        getNearbyEntities(3.0, 3.0, 3.0).filterIsInstance<LivingEntity>().filterNot { it == player }
                    for (entity in entities) {
                        val pos = entity.location.toVector().midpoint(location.toVector())

                        val angle = entity.location.toVector().subtract(location.toVector()).setY(0).normalize().run {
                            val theta = atan2(-x, z)
                            Math.toDegrees((theta + PI.times(2)) % PI.times(2)).toFloat()
                        }

                        val loc = Location(world, pos.x, pos.y, pos.z, angle.plus(90), 0f)
                        val length = entity.location.toVector().subtract(location.toVector()).setY(0).length()

                        loc.drawCircle(length, Color.RED, 0, 180)
                        entity.damage(event.damage)
                    }
                }
            }
        }
    }
}