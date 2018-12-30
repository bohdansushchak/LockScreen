package com.sushchak.bohdan.lockscreen.control

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import com.sushchak.bohdan.lockscreen.R
import kotlinx.android.synthetic.main.alert_view.view.*

class LayoutAlertView(context: Context) : FrameLayout(context), View.OnClickListener{

    constructor(context:Context, title: String, content: String) : this(context){
        this.title = title
        this.content = content
    }

    init {
        val view = inflate(context, R.layout.alert_view, null)
        addView(view)

        btnOk.setOnClickListener(this)
    }

    var title: String
        get() = tvTitle?.text.toString()
        set(value) {
            tvTitle?.text = value
        }

    var content: String
        get() = tvContent?.text.toString()
        set(value) {
            tvContent?.text = value
        }

    var buttonText: String
        get() = btnOk?.text.toString()
        set(value) {
            btnOk?.text = value
        }

    var onClickAlert: (() -> Unit)? = null

    override fun onClick(v: View?) {
        onClickAlert?.invoke()
    }
}