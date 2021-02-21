package ru.HappyWorldGames.SmartDiary.View

import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import ru.HappyWorldGames.SmartDiary.DataInfo
import ru.HappyWorldGames.SmartDiary.MainActivity
import ru.HappyWorldGames.SmartDiary.R

class RecyclerViewAdapter(private val globalPos: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount() = DataInfo.weekDaysData[globalPos].size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(if (!MainActivity.edit) R.layout.recyclerview_item else R.layout.recyclerview_item_edit, parent, false)
        return if(!MainActivity.edit) MyViewHolder(itemView) else MyViewHolderEdit(itemView, this)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(!MainActivity.edit) (holder as MyViewHolder).bind(globalPos, position)
        else (holder as MyViewHolderEdit).bind(globalPos, position)
    }

    override fun getItemViewType(position: Int): Int {
        return if(!MainActivity.edit) 0 else 1
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameLesson : TextView = itemView.findViewById(R.id.lesson_name)
        private val roomLesson : TextView = itemView.findViewById(R.id.lesson_room)
        private val timeLesson : TextView = itemView.findViewById(R.id.lesson_time)

        fun bind(globalPos: Int, localPos: Int) {
            val lessonData = DataInfo.weekDaysData[globalPos][localPos]
            nameLesson.text = lessonData.nameLesson
            roomLesson.text = lessonData.roomLesson
            timeLesson.text = lessonData.timeLesson.toString()
        }
    }

    class MyViewHolderEdit(itemView: View, adapter: RecyclerViewAdapter) : RecyclerView.ViewHolder(itemView) {
        private var globalPos = -1
        private var localPos = -1

        private val nameLesson : TextInputLayout = itemView.findViewById(R.id.lesson_name)
        private val roomLesson : TextInputLayout = itemView.findViewById(R.id.lesson_room)
        private val timeLesson : TextInputLayout = itemView.findViewById(R.id.lesson_time)

        private val removeButton : Button = itemView.findViewById(R.id.remove)
        private val moveButton : Button = itemView.findViewById(R.id.move)

        init {
            nameLesson.editText?.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {}
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    DataInfo.weekDaysData[globalPos][localPos].nameLesson = nameLesson.editText!!.text.toString()
                }
            })
            roomLesson.editText?.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {}
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    DataInfo.weekDaysData[globalPos][localPos].roomLesson = roomLesson.editText!!.text.toString()
                }
            })
            timeLesson.editText?.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {}
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if(timeLesson.editText?.length()!! > 10) DataInfo.weekDaysData[globalPos][localPos].timeLesson = DataInfo.TimesLesson.parce(timeLesson.editText!!.text.toString())
                }
            })
            TimeEditor(timeLesson.editText)

            removeButton.setOnClickListener {
                DataInfo.weekDaysData[globalPos].removeAt(adapterPosition)
                adapter.notifyItemRemoved(adapterPosition)
            }
            moveButton.visibility = View.GONE
        }

        fun bind(globalPos: Int, localPos: Int) {
            this.globalPos = globalPos
            this.localPos = localPos

            val lessonData = DataInfo.weekDaysData[globalPos][localPos]
            nameLesson.editText?.setText(lessonData.nameLesson)
            roomLesson.editText?.setText(lessonData.roomLesson)
            timeLesson.editText?.setText(lessonData.timeLesson.toString())
        }
    }

    class TimeEditor(private val editText: EditText?) {

        var text = ""
        init {
            editText?.isLongClickable = false
            editText?.setOnClickListener {
                editText?.setSelection(editText?.length())
            }
            editText?.setOnFocusChangeListener { v, hasFocus ->
                if(hasFocus) {
                    editText.hint = editText.text
                    text = editText.text.toString()
                    editText.setText("")
                }else if(editText.text.isEmpty() || editText.length() < 11) editText.setText(text)
            }
            editText?.filters = arrayOf(object : InputFilter {
                override fun filter(source: CharSequence?, start: Int, end: Int, dest: Spanned?, dstart: Int, dend: Int): CharSequence? {
                    if (source!!.isEmpty()) return null
                    var result = dest.toString().substring(0, dstart) + source.toString().substring(start, end) + dest.toString().substring(dend, dest!!.length)

                    if (result.length > 11) return ""
                    var allowEdit = true
                    var c: Char
                    if (result.isNotEmpty()) {
                        c = result[0]
                        allowEdit = allowEdit and (c in '0'..'2')
                    }
                    if (result.length > 1) {
                        c = result[1]
                        allowEdit = if (result[0] == '0' || result[0] == '1') allowEdit and (c in '0'..'9') else allowEdit and (c in '0'..'3')
                    }
                    if (result.length > 3) {
                        c = result[3]
                        allowEdit = allowEdit and (c in '0'..'5')
                    }else if (result.length > 2) {
                        c = result[2]
                        allowEdit = allowEdit and (c in '0'..'5')
                        return if(allowEdit) ":$c" else ":"
                    }
                    if (result.length > 4) {
                        c = result[4]
                        allowEdit = allowEdit and (c in '0'..'9')
                    }
                    if (result.length > 6) {
                        c = result[6]
                        allowEdit = allowEdit and (c in '0'..'2')
                    }else if (result.length > 5) {
                        c = result[5]
                        allowEdit = allowEdit and (c in '0'..'2')
                        return if(allowEdit) "-$c" else "-"
                    }
                    if (result.length > 7) {
                        c = result[7]
                        allowEdit = if (result[6] == '0' || result[6] == '1') allowEdit and (c in '0'..'9') else allowEdit and (c in '0'..'3')
                    }
                    if (result.length > 9) {
                        c = result[9]
                        allowEdit = allowEdit and (c in '0'..'5')
                    }else if (result.length > 8) {
                        c = result[8]
                        allowEdit = allowEdit and (c in '0'..'5')
                        return if(allowEdit) ":$c" else ":"
                    }
                    if (result.length > 10) {
                        c = result[10]
                        allowEdit = allowEdit and (c in '0'..'9')
                    }
                    return if (allowEdit) null else ""
                }
            })
        }
    }
}