package com.sushchak.bohdan.lockscreen.control

import android.content.Context
import android.support.v7.widget.CardView
import android.text.Layout
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class AlertView(
    context: Context
) : CardView(context), View.OnClickListener {

    private val linearLayout = LinearLayout(context)
    private val tvContent = TextView(context)
    private val tvTitle = TextView(context)
    private val button = Button(context)

    constructor(context:Context, title: String, content: String) : this(context){
        this.title = title
        this.content = content
    }

    init {
        createView()
    }

    private fun createView() {

        linearLayout.orientation = LinearLayout.VERTICAL
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.setMargins(15, 10, 15, 10)
        linearLayout.layoutParams = params

        tvTitle.apply {
            textSize = 16f
            text = title
            layoutParams = ViewGroup.LayoutParams (ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)

        }

        tvContent.apply {
            textSize = 14f
            text = content
        }

        button.apply {
            text = buttonText
            setOnClickListener(this@AlertView)
        }

        linearLayout.addView(tvTitle)
        linearLayout.addView(tvContent)
        linearLayout.addView(button)

        addView(linearLayout)
    }

    var title: String
        get() = tvTitle.text.toString()
        set(value) {
            tvTitle.text = value
        }

    var content: String
        get() = tvContent.text.toString()
        set(value) {
            tvContent.text = value
        }

    var buttonText: String
        get() = button.text.toString()
        set(value) {
            button.text = value
        }

    var onClickAlert: (() -> Unit)? = null

    override fun onClick(v: View?) {
        onClickAlert?.invoke()
    }
}


