package ru.HappyWorldGames.SmartDiary

import android.content.Context
import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.File
import java.io.StringReader
import java.io.StringWriter

class Files(c: Context) {
    private val appDir = c.getExternalFilesDir(null)
    private val diaryFile = File(appDir, "Diary.xml")

    init {
        if(!diaryFile.exists()) saveDiary()
    }

    fun saveDiary() {
        val xmlSerializer = Xml.newSerializer()
        val writer = StringWriter()
        xmlSerializer.setOutput(writer)
        xmlSerializer.startDocument("UTF-8", false)
        xmlSerializer.startTag("", "Week")
        DataInfo.weekDaysData.forEach { it ->
            xmlSerializer.startTag("", "Day")
            it.forEach {
                xmlSerializer.startTag("", "Lesson")
                xmlSerializer.attribute("", "name", it.nameLesson)
                xmlSerializer.attribute("", "room", it.roomLesson)
                xmlSerializer.attribute("", "time", it.timeLesson.toString())
                xmlSerializer.endTag("", "Lesson")
            }
            xmlSerializer.endTag("", "Day")
        }
        xmlSerializer.endTag("", "Week")
        xmlSerializer.endDocument()

        diaryFile.writeText(writer.toString(), Charsets.UTF_8)
    }

    fun loadDiary() {
        DataInfo.clearWeekDayData()

        val parser = XmlPullParserFactory.newInstance().newPullParser()
        parser.setInput(StringReader(diaryFile.readText(Charsets.UTF_8)))

        var index = 0
        var event = parser.eventType
        while (event != XmlPullParser.END_DOCUMENT) {
            when (event) {
                XmlPullParser.START_TAG -> when(parser.name){
                    "Lesson" -> DataInfo.weekDaysData[index].add(DataInfo.LessonData(parser.getAttributeValue("", "name"), parser.getAttributeValue("", "room"), DataInfo.TimesLesson.parce(parser.getAttributeValue("", "time"))))
                }
                XmlPullParser.END_TAG -> when(parser.name){
                    "Day" -> index++
                }
            }
            event = parser.next()
        }
    }
}