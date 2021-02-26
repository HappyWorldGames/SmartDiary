package ru.HappyWorldGames.SmartDiary

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import ru.HappyWorldGames.SmartDiary.View.ViewPagerAdapter

class MainActivity : AppCompatActivity() {
    private val sharedPreference by lazy { getSharedPreferences("setting", Context.MODE_PRIVATE) }
    companion object {
        var edit = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Files(this).loadDiary()

        if(!NotificationService.running && sharedPreference.getBoolean("notification", false)) NotificationService.startService(this)
        else if(NotificationService.running && !sharedPreference.getBoolean("notification", false)) NotificationService.stopService(this)

        setContentView(R.layout.activity_main)
        addbutton.hide()
        viewPager.adapter = ViewPagerAdapter(this)
        viewPager.currentItem = DataInfo.getDayOfWeek()

        editbutton.setOnClickListener {
            edit = !edit
            editbutton.setImageResource(if(edit) R.drawable.check_mark else R.drawable.edit)
            if(edit) addbutton.show() else {
                addbutton.hide()
                Files(this).saveDiary()
            }

            (viewPager.adapter as ViewPagerAdapter).notifyDataSetChanged()
        }
        addbutton.setOnClickListener {
            DataInfo.weekDaysData[viewPager.currentItem].add(DataInfo.LessonData())
            (viewPager.adapter as ViewPagerAdapter).notifyItemChanged(viewPager.currentItem)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(0, 1, 0, "Setting").setIcon(R.drawable.setting).setShowAsAction(1)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            1 -> startActivity(Intent(this, SettingActivity::class.java))
        }
        return true
    }
}