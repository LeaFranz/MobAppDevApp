package at.mobappdev.flytta.reminderlist

/**
 * data for reminder list item
 * class whose main purpose is to hold data.
 */
data class ReminderListItem(val reminderID:String, val reminderName:String="", val interval:String="", val days:ArrayList<*>, val timeStart:String="", val timeEnd:String="")
