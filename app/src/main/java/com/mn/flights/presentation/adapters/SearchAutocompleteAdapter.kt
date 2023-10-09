package com.mn.flights.presentation.adapters

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import com.mn.flights.databinding.ItemCityAutocompleteBinding
import com.mn.flights.domain.repositories.AutoSuggestPlace

class SearchAutocompleteAdapter(
    context: Context,
    private val items: MutableList<AutoSuggestPlace>
) : ArrayAdapter<AutoSuggestPlace>(context, 0, items) {

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): AutoSuggestPlace? =
        if (items.isNotEmpty()) items[position] else null

    override fun addAll(collection: MutableCollection<out AutoSuggestPlace>) {
        items.addAll(collection)
    }

    private class ViewHolder(val binding: ItemCityAutocompleteBinding)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val item = getItem(position)
        val holder: ViewHolder
        val view: View

        if (convertView == null) {
            val binding =
                ItemCityAutocompleteBinding.inflate(LayoutInflater.from(context), parent, false)
            view = binding.root
            holder = ViewHolder(binding)
            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        item?.let {
            val spannableString = SpannableString(it.name)
            if (it.highlighting.isNotEmpty() && it.name.length > it.highlighting[1]) {
                spannableString.setSpan(
                    StyleSpan(Typeface.BOLD),
                    it.highlighting[0],
                    it.highlighting[1],
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE
                )
            }

            holder.binding.image.isVisible = it.isAirport
            holder.binding.title.text = spannableString
            holder.binding.subtitle.text = it.country
            holder.binding.divider.isVisible = items.lastIndex != position
        }

        return view
    }
}
