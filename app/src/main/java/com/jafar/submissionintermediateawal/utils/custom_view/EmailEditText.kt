package com.jafar.submissionintermediateawal.utils.custom_view

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText

class EmailEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : TextInputEditText(context, attrs) {

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val emailPattern = "^[A-Za-z](.*)(@)(.+)(\\.)(.+)".toRegex()
                error = if (s.toString().isEmpty()) {
                    "Email tidak boleh kosong"
                } else if (!emailPattern.matches(s.toString())) {
                    "Harus sesuai format email"
                } else {
                    null
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

}