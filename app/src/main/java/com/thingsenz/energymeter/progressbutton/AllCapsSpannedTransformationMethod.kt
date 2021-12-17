package com.thingsenz.energymeter.progressbutton

import android.content.Context
import android.graphics.Rect
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.method.TransformationMethod
import android.view.View
import java.util.*

class AllCapsSpannedTransformationMethod(context: Context): TransformationMethod {


    private val locale: Locale?=context.resources.configuration.locale

    override fun getTransformation(source: CharSequence?,view: View): CharSequence? {

        if (source==null)
            return null
        val upperCaseText=source.toString().toUpperCase(locale?:Locale.getDefault())
        if (source is Spanned) {
            val spannable=SpannableString(upperCaseText)
            TextUtils.copySpansFrom(source,0,source.length,null,spannable,0)
            return spannable
        } else
            return upperCaseText

    }

    override fun onFocusChanged(p0: View?, p1: CharSequence?, p2: Boolean, p3: Int, p4: Rect?) {

    }

}