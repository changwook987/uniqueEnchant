package io.github.changwook987.uniqueEnchant.util

import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.util.Vector
import kotlin.math.cos
import kotlin.math.sin

fun Location.drawCircle(r: Double, color: Color = Color.BLACK, start: Int = 0, end: Int = 359) {
    val yaw = -Math.toRadians(yaw.toDouble())
    val pitch = Math.toRadians(pitch.toDouble())

    var it = start
    while (it != end) {
        val loc = toVector()
        val angle = Math.toRadians(it.toDouble())

        val x = cos(angle)
        val y = sin(angle)

        val vec = Vector(x, y, 0.0).normalize().multiply(r).rotateAroundX(pitch).rotateAroundY(yaw)
        loc.add(vec)

        world.spawnParticle(
            Particle.REDSTONE,
            Location(world, loc.x, loc.y, loc.z),
            1,
            Particle.DustOptions(
                color,
                1f
            )
        )
        it++
        it %= 360
    }
}