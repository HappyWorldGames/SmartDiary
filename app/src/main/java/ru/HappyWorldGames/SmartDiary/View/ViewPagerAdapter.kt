package ru.HappyWorldGames.SmartDiary.View

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_page.view.*
import ru.HappyWorldGames.SmartDiary.MainActivity
import ru.HappyWorldGames.SmartDiary.R

class ViewPagerAdapter(var c: Context) : RecyclerView.Adapter<PagerVH>() {

    private val colors = c.resources.getIntArray(R.array.weekcolors)
    private val textColors = c.resources.getIntArray(R.array.weektextcolors)
    private val days = c.resources.getStringArray(R.array.weekdays)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerVH =
            PagerVH(LayoutInflater.from(parent.context).inflate(R.layout.item_page, parent, false))

    override fun getItemCount(): Int = colors.size

    override fun onBindViewHolder(holder: PagerVH, position: Int) = holder.itemView.run {
        pageNum.text = days[position]
        pageNum.setTextColor(textColors[position])

        recyclerView.layoutManager = LinearLayoutManager(c)
        recyclerView.adapter = RecyclerViewAdapter(position)

        val dividerItemDecoration = DividerItemDecoration(recyclerView.context, (recyclerView.layoutManager as LinearLayoutManager).orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)

        container.setBackgroundColor(colors[position])
    }

    override fun getItemViewType(position: Int): Int {
        return if(!MainActivity.edit) 0 else 1
    }
}

class PagerVH(itemView: View) : RecyclerView.ViewHolder(itemView)