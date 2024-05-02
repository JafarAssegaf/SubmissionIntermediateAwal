package com.jafar.submissionintermediateawal.utils.custom_view

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText

class PasswordEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : TextInputEditText(context, attrs) {

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                error = if (s.toString().isEmpty()) {
                    "Password tidak boleh kosong"
                } else if (s.toString().length < 8) {
                    "Password tidak boleh kurang dari 8 karakter"
                } else {
                    null
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

}