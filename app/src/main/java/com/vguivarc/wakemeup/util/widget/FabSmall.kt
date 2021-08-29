package com.vguivarc.wakemeup.util.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.vguivarc.wakemeup.R
import kotlinx.android.synthetic.main.view_fab_small.view.*

class FabSmall(context: Context, attrs : AttributeSet) : ConstraintLayout(context, attrs){

    var offsetYAnimation = 0.0f

    val fabText : TextView
        get() = fab_text

    init {
        View.inflate(context, R.layout.view_fab_small, this)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.FabSmall)
        fab_text.text = attributes.getString(R.styleable.FabSmall_name)
        fab_button.setImageResource(attributes.getResourceId(R.styleable.FabSmall_iconSrc, R.mipmap.ic_launcher))
        offsetYAnimation=attributes.getDimension(R.styleable.FabSmall_offset_y, offsetYAnimation)

        attributes.recycle()
    }

    override fun setOnClickListener(l: OnClickListener?) {
        fab_button.setOnClickListener(l)
    }

}