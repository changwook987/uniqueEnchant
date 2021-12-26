package io.github.changwook987.uniqueEnchant.plugin

import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.TextComponent
import org.bukkit.inventory.ItemStack
import java.util.*

class UniqueEnchant {
    val name: String
    val level: Int
    val onoff: Boolean

    constructor(name: String, level: Int) {
        this.name = name
        this.level = level
        this.onoff = false
    }

    constructor(name: String, onoff: Boolean) {
        this.name = name
        this.level = 1
        this.onoff = onoff
    }

    fun toTextComponent(): TextComponent {
        return text(
            name + if (!onoff) {
                " $level"
            } else {
                ""
            }
        )
    }

    override fun toString(): String {
        return name + if (onoff) {
            ""
        } else {
            " $level"
        }
    }
}

var ItemStack.uniqueEnchant: List<UniqueEnchant>
    get() {
        val lore = (lore() ?: emptyList()).filterIsInstance<TextComponent>()

        val n = lore.indexOf(text("UNIQUE ENCHANT"))
        if (n == -1) return emptyList()

        val enchants = lore.subList(n + 1, lore.size)

        return List(enchants.size) {
            StringTokenizer(enchants[it].content()).run {
                if (countTokens() == 1) {
                    UniqueEnchant(nextToken(), true)
                } else {
                    UniqueEnchant(nextToken(), nextToken().toIntOrNull() ?: 0)
                }
            }
        }
    }
    set(value) {
        var lore = lore() ?: ArrayList()

        var last = lore.indexOf(text("UNIQUE ENCHANT"))

        if (last == -1) {
            last = lore.size
        } else {
            lore = lore.subList(0, last)
        }

        lore.add(last++, text("UNIQUE ENCHANT"))

        lore.addAll(
            last,
            List(value.size) { value[it].toTextComponent() }
        )

        lore(lore)
    }

fun ItemStack.hasUniqueEnchant(name: String): Boolean {
    return uniqueEnchant.joinToString(" ").contains(name)
}