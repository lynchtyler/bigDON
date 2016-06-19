package  com.flashboomlet.data.models

/**
  * Case class for the Estimates data
  *
  * @param choice the choice tag line ("Last Name", Other)
  * @param value current value
  * @param lead_confidence the confidence that the choice is leading
  */
case class PollData(
  choice: String,
  value: Float,
  lead_confidence: Float
)
