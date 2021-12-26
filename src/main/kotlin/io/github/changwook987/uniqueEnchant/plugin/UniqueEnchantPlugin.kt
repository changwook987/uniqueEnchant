package io.github.changwook987.uniqueEnchant.plugin

import io.github.monun.kommand.KommandSource
import io.github.monun.kommand.StringType
import io.github.monun.kommand.kommand
import org.bukkit.Material
import org.bukkit.plugin.java.JavaPlugin

class UniqueEnchantPlugin : JavaPlugin() {
    override fun onEnable() {
        logger.info("onEnable")

        kommand {
            register("unique") {
                then("info") {
                    executes {
                        sender.sendMessage("새로운 인챈트가 추가되는 플러그인 입니다.")
                    }
                }
                then("enchant") {
                    requires { playerOrNull != null }

                    then("enchantment" to string(StringType.SINGLE_WORD)) {
                        then("level") {
                            then("level" to int(0, 5)) {
                                executes {
                                    val enchant: String = it["enchantment"]
                                    val level: Int = it["level"]

                                    val enchantment = UniqueEnchant(enchant, level)
                                    enchant(enchantment)
                                }
                            }
                        }

                        then("onoff") {
                            then("onoff" to bool()) {
                                executes {
                                    val enchant: String = it["enchantment"]
                                    val onoff: Boolean = it["onoff"]

                                    val enchantment = UniqueEnchant(enchant, onoff)
                                    enchant(enchantment)
                                }
                            }
                        }
                    }
                    then("debug") {
                        executes {
                            val item = player.inventory.itemInMainHand

                            sender.sendMessage(item.uniqueEnchant.joinToString("\n"))
                        }
                    }
                }
            }
            register("bomb") {
                requires { playerOrNull != null }
                executes {
                    val world = player.world

                    world.createExplosion(player.location, 50f, true, true, player)
                }
            }
        }

        EventListener(this)
    }

    private fun KommandSource.enchant(enchant: UniqueEnchant) {
        val item = player.inventory.itemInMainHand

        if (item.type == Material.AIR) {
            player.sendMessage("아이템을 손에 들고 사용해주세요")
        } else {
            val enchantment = item.uniqueEnchant.filterNot { it.name == enchant.name }.toMutableList()

            if (enchant.onoff) {
                enchantment.add(enchant)
            } else if (enchant.level > 0) {
                enchantment.add(enchant)
            }

            item.uniqueEnchant = enchantment
        }
    }
}