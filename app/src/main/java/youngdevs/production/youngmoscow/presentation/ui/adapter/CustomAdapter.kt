package youngdevs.production.youngmoscow.presentation.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import youngdevs.production.youngmoscow.R

class CustomAdapter(context: Context, resource: Int, objects: Array<String>) :
    ArrayAdapter<String>(context, resource, objects) {

    private val layoutInflater = LayoutInflater.from(context)
    private val textColor = ContextCompat.getColor(context, R.color.bgColor)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false)

        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = getItem(position)

        // Установка цвета текста
        textView.setTextColor(textColor)

        return view
    }
}
