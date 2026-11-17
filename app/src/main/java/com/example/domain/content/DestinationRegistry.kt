package com.example.domain.content

import com.example.R
import com.example.domain.model.Destination

object DestinationRegistry {
    val destinations: List<Destination> = listOf(
        Destination("walk_park", "City Central Park", 5.0, "🌳", "A refreshing stroll across the heart of the city", nameRes = R.string.dest_park_name, descriptionRes = R.string.dest_park_desc),
        Destination("walk_bridge", "Golden Suspension Bridge", 12.0, "🌉", "Crossing the grand scenic strait", nameRes = R.string.dest_bridge_name, descriptionRes = R.string.dest_bridge_desc),
        Destination("walk_lake", "Alpine Mountain Lake", 25.0, "🏔️", "Crisp alpine air and mirrored waters", nameRes = R.string.dest_lake_name, descriptionRes = R.string.dest_lake_desc),
        Destination("walk_marathon", "Marathon Legend", 42.2, "🏅", "The ancient standard of endurance", nameRes = R.string.dest_marathon_name, descriptionRes = R.string.dest_marathon_desc),
        Destination("walk_canyon", "Red Rock Grand Canyon", 75.0, "🏜️", "Vast carved stone horizons", nameRes = R.string.dest_canyon_name, descriptionRes = R.string.dest_canyon_desc),
        Destination("walk_fuji", "Mount Fuji Circuit", 120.0, "🗻", "Encircling the sacred volcanic peak", nameRes = R.string.dest_fuji_name, descriptionRes = R.string.dest_fuji_desc),
        Destination("walk_pyramids", "Giza Plateau & Nile", 180.0, "🐫", "Traversing historical desert wonderlands", nameRes = R.string.dest_pyramids_name, descriptionRes = R.string.dest_pyramids_desc),
        Destination("walk_camino", "Camino Pilgrimage", 260.0, "🚶", "Historic pilgrim path of contemplation", nameRes = R.string.dest_camino_name, descriptionRes = R.string.dest_camino_desc),
        Destination("walk_iceland", "Icelandic Ring Trail", 380.0, "🌋", "Waterfalls, black sand, and glaciers", nameRes = R.string.dest_iceland_name, descriptionRes = R.string.dest_iceland_desc),
        Destination("walk_wall", "Great Ancient Wall", 500.0, "🏯", "Ancient stone spine across the mountain ridges", nameRes = R.string.dest_wall_name, descriptionRes = R.string.dest_wall_desc),
        Destination("walk_sahara", "Sahara Oasis Crossing", 600.0, "✨", "Golden sand dunes beneath vast night stars", nameRes = R.string.dest_sahara_name, descriptionRes = R.string.dest_sahara_desc),
        Destination("walk_everest", "Himalayan Base Passage", 680.0, "❄️", "Walking toward the roof of the earth", nameRes = R.string.dest_everest_name, descriptionRes = R.string.dest_everest_desc)
    )

    fun getNextDestination(totalKm: Double): Destination? {
        return destinations.firstOrNull { it.kmRequired > totalKm }
    }

    fun getCompletedDestinations(totalKm: Double): List<Destination> {
        return destinations.filter { it.kmRequired <= totalKm }
    }
}

