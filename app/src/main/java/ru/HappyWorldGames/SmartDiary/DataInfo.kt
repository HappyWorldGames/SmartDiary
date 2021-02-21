package ru.HappyWorldGames.SmartDiary

import android.R.attr.x
import android.R.attr.y
import java.util.*


class DataInfo {
    companion object {
        val weekDaysData = arrayOf(mutableListOf<LessonData>(), mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf())

        fun clearWeekDayData(){
            weekDaysData.forEach {
                it.clear()
            }
        }

        fun getDayOfWeek() : Int {
            return Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1
        }
        private fun getTimeNow() : Float {
            val calendar = Calendar.getInstance()
            return "${calendar.get(Calendar.HOUR)}.${calendar.get(Calendar.MINUTE)}".toFloat()
        }
        fun getLessonNow() : LessonData? {
            var current : LessonData? = null
            val time = getTimeNow()
            weekDaysData[getDayOfWeek()].forEach {
                if(time > it.timeLesson.start.toFloat() && time < it.timeLesson.end.toFloat()) current = it
            }
            return current
        }
    }

    class LessonData(var nameLesson: String = "", var roomLesson: String = "", var timeLesson: TimesLesson = TimesLesson(TimeLesson(0, 0), TimeLesson(0, 0)))
    class TimesLesson(var start: TimeLesson, var end: TimeLesson){
        companion object {
            fun parce(times: String) : TimesLesson {
                val split = times.split("-")
                return TimesLesson(TimeLesson.parce(split[0]), TimeLesson.parce(split[1]))
            }
        }

        override fun toString(): String {
            return "$start-$end"
        }
    }
    class TimeLesson(var hour: Int, var minute: Int) {
        companion object {
            fun parce(time: String) : TimeLesson {
                val split = time.split(":")
                return TimeLesson(split[0].toInt(), split[1].toInt())
            }
        }
        override fun toString(): String {
            return (if(hour<10) "0$hour" else "$hour") + ":" + (if(minute<10) "0$minute" else "$minute")
        }
        fun toFloat() : Float {
            return "$hour.$minute".toFloat()
        }
    }
}