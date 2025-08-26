package expo.modules.nativeinput

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import expo.modules.kotlin.AppContext
import expo.modules.kotlin.viewevent.EventDispatcher
import expo.modules.kotlin.views.ExpoView
import java.net.URL
import android.os.Handler
import android.view.MotionEvent
import kotlin.concurrent.thread

class NativeInputView(context: Context, appContext: AppContext) : ExpoView(context, appContext) {
  internal val editText = EditText(context)
  var isError = false
  var isDisabled = false
  var currentColors: Map<String, String> = mapOf()
  var isSecure = false

  private val onInputFocus by EventDispatcher()
  private val onInputBlur by EventDispatcher()
  private val onInputChange by EventDispatcher()
  private val onInputSubmit by EventDispatcher()
  private val onRightIconClick by EventDispatcher()

  init {
    editText.background = null
    editText.isSingleLine = true

    editText.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

    addView(editText)

    editText.setOnFocusChangeListener { _, hasFocus ->
      if (hasFocus) onInputFocus(emptyMap()) else onInputBlur(emptyMap())
      updateBorder()
    }

    editText.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

      override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        onInputChange(mapOf("text" to p0.toString()))
      }

      override fun afterTextChanged(p0: Editable?) {}
    })

    editText.setOnEditorActionListener { _, i, _ ->
      when (i) {
        EditorInfo.IME_ACTION_GO,
        EditorInfo.IME_ACTION_DONE,
        EditorInfo.IME_ACTION_SEND,
        EditorInfo.IME_ACTION_NEXT -> {
          onInputSubmit(mapOf("text" to editText.text.toString()))
          true
        }
        else -> false
      }
    }

    editText.setOnTouchListener { v, event ->
      if (event.action == MotionEvent.ACTION_UP) {
        val drawableRight = editText.compoundDrawables[2]
        if (drawableRight != null) {
          val bounds = drawableRight.bounds
          val xTouch = event.x.toInt()
          if (xTouch >= editText.width - editText.paddingRight - bounds.width()) {
            onRightIconClick(emptyMap())
            v.performClick()
            return@setOnTouchListener true
          }
        }
      }
      false
    }
  }

  override fun setId(id: Int) {
    super.setId(id)
    editText.id = id
  }

  private fun dpToPx(dp: Number): Int {
    return (dp.toDouble() * context.resources.displayMetrics.density).toInt()
  }

  fun setPadding(padding: Map<String, Number>?) {
    padding?.let {
      val left = dpToPx(it["left"] ?: 0)
      val top = dpToPx(it["top"] ?: 0)
      val right = dpToPx(it["right"] ?: 0)
      val bottom = dpToPx( it["bottom"] ?: 0)
      editText.setPadding(left, top, right, bottom)
    }
  }

  fun updateBorder() {
    val color = when {
      isDisabled -> currentColors["disabled"]?.let { Color.parseColor(it) } ?: Color.TRANSPARENT
      isError -> currentColors["error"]?.let { Color.parseColor(it) } ?: Color.TRANSPARENT
      editText.isFocused -> currentColors["focused"]?.let { Color.parseColor(it) } ?: Color.TRANSPARENT
      else -> currentColors["unfocused"]?.let { Color.parseColor(it) } ?: Color.TRANSPARENT
    }

    val drawable = GradientDrawable().apply {
      setColor(Color.TRANSPARENT)
      setStroke(dpToPx(1), color)
    }
    editText.background = drawable
  }

  fun toggleSecureTextEntry() {
    editText.transformationMethod = if (isSecure) PasswordTransformationMethod.getInstance() else null
    editText.setSelection(editText.text.length)
  }

  fun setRightIcon(source: Map<String, Any>?) {
    source?.let {
      thread {
        try {
          val parsed = Uri.parse(it["source"] as String)
          val drawable: Drawable? = when (parsed.scheme) {
            "http", "https" -> {
              val inputStream = URL(it["source"] as String).openStream()
              Drawable.createFromStream(inputStream, it["source"] as String)
            }
            "file", "content" -> {
              val inputStream = context.contentResolver.openInputStream(parsed)
              Drawable.createFromStream(inputStream, it["source"] as String)
            }
            else -> null
          }

          Handler(Looper.getMainLooper()).post {
            drawable?.setBounds(0, 0, dpToPx(it["size"] as Number), dpToPx(it["size"] as Number))
            editText.setCompoundDrawables(null, null, drawable, null)
          }
        } catch (e: Exception) {
          e.printStackTrace()
          Handler(Looper.getMainLooper()).post {
            editText.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
          }
        }
      }
    }
  }
}
