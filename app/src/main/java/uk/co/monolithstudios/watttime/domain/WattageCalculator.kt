package uk.co.monolithstudios.watttime.domain

import org.threeten.bp.Duration

class WattageCalculator {

  fun convertMicrowaveTime(userMicrowaveWattage: Int, productMicrowaveWattage: Int, duration: Duration): Duration {
    val requiredMicrowaveWattage = productMicrowaveWattage * duration.seconds
    val convertedRequiredNumberOfSeconds = requiredMicrowaveWattage / userMicrowaveWattage
    return Duration.ofSeconds(convertedRequiredNumberOfSeconds)
  }
}