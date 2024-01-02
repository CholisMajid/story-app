package com.cholis.mystoryapp.view.customview

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.cholis.mystoryapp.R

class CustomViewEditText : AppCompatEditText, View.OnTouchListener {

    private lateinit var icPerson: Drawable
    private lateinit var icEmail: Drawable
    private lateinit var icPassword: Drawable

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        icPerson = ContextCompat.getDrawable(context, R.drawable.ic_baseline_person_24)!!
        icEmail = ContextCompat.getDrawable(context, R.drawable.ic_baseline_email_24)!!
        icPassword = ContextCompat.getDrawable(context, R.drawable.ic_baseline_lock_24)!!

        setOnTouchListener(this)

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) showClearButton() else hideClearButton()
            }

            override fun afterTextChanged(s: Editable?) {
                when (id) {
                    R.id.nameEditText -> {
                        if (s.toString().isEmpty()) {
                            this@CustomViewEditText.error = resources.getString(R.string.name_error)
                        }
                    }
                    R.id.emailEditText -> {
                        val email = s.toString().trim()
                        val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
                        if (!isEmailValid) {
                            this@CustomViewEditText.error = resources.getString(R.string.email_error)
                        }
                    }
                    R.id.passwordEditText -> {
                        if (this@CustomViewEditText.text?.trim().toString().length < 8) {
                            this@CustomViewEditText.error = resources.getString(R.string.password_error)
                        }
                    }
                }
            }
        }

        addTextChangedListener(textWatcher)
    }

    private fun hideClearButton() {
        setButtonDrawable(startOfTheText = getIconForId())
    }

    private fun showClearButton() {
        setButtonDrawable(startOfTheText = getIconForId())
    }

    private fun getIconForId(): Drawable {
        return when (id) {
            R.id.nameEditText -> icPerson
            R.id.emailEditText -> icEmail
            R.id.passwordEditText -> icPassword
            else -> icPerson
        }
    }

    private fun setButtonDrawable(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(startOfTheText, topOfTheText, endOfTheText, bottomOfTheText)
    }

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        return false
    }
}
