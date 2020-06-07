package at.mobappdev.flytta.reminderlist

data class ReminderListItem(val reminderID:String, val reminderName:String="", val interval:String="", val days:ArrayList<*>, val timeStart:String="", val timeEnd:String="")
