package net.author

import net.botwithus.rs3.game.Coordinate

data class Fish(
val name : String,
val position: Coordinate,
    val bank : String,
    val interact : String
)

val fish = mutableListOf(
    Fish(
        name = "Tuna",
        position = Coordinate(3240,3253,0),
        bank = "Bank chest",
        interact = "Lure"
    )
)
