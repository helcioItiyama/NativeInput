package expo.modules.nativeinput

import android.os.Build
import android.text.InputType
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.autofill.HintConstants
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition

class NativeInputModule : Module() {

  override fun definition() = ModuleDefinition {
    Name("NativeInput")

    View(NativeInputView::class) {
      Events("onInputFocus", "onInputBlur", "onInputChange", "onInputSubmit", "onRightIconClick")

      Prop("keyboardType") { view: NativeInputView, type: String ->
        val mappedType = when (type) {
          "numeric" -> InputType.TYPE_CLASS_NUMBER
          "phone" -> InputType.TYPE_CLASS_PHONE
          "email" -> InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
          "password" -> InputType.TYPE_TEXT_VARIATION_PASSWORD
          else -> InputType.TYPE_CLASS_TEXT
        }
        view.editText.inputType = mappedType
      }

      Prop("autoComplete") { view: NativeInputView, hint: String ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
          val autofillHints = when(hint) {
            "creditCardExpirationDate" -> HintConstants .AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_DATE
            "creditCardExpirationDay" -> HintConstants.AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_DAY
            "creditCardExpirationMonth" -> HintConstants.AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_MONTH
            "creditCardExpirationYear" -> HintConstants.AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_YEAR
            "creditCardNumber" -> HintConstants.AUTOFILL_HINT_CREDIT_CARD_NUMBER
            "creditCardSecurityCode" -> HintConstants.AUTOFILL_HINT_CREDIT_CARD_SECURITY_CODE
            "emailAddress" -> HintConstants.AUTOFILL_HINT_EMAIL_ADDRESS
            "name" -> HintConstants.AUTOFILL_HINT_PERSON_NAME_GIVEN
            "familyName" -> HintConstants.AUTOFILL_HINT_PERSON_NAME_FAMILY
            "password" -> HintConstants.AUTOFILL_HINT_PASSWORD
            "newPassword" -> HintConstants.AUTOFILL_HINT_NEW_PASSWORD
            "phone" -> HintConstants.AUTOFILL_HINT_PHONE_NUMBER
            "postalAddress" -> HintConstants.AUTOFILL_HINT_POSTAL_ADDRESS
            "postalCode" -> HintConstants.AUTOFILL_HINT_POSTAL_CODE
            "username" -> HintConstants.AUTOFILL_HINT_USERNAME
            "newUsername" -> HintConstants.AUTOFILL_HINT_NEW_USERNAME
            else -> ""
          }
          view.editText.setAutofillHints(autofillHints)
        }
      }

      Prop("importantForAutofill") {view: NativeInputView, important: String ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
          val importance = when(important) {
            "auto" -> View.IMPORTANT_FOR_AUTOFILL_AUTO
            "yes" -> View.IMPORTANT_FOR_AUTOFILL_YES
            "no" -> View.IMPORTANT_FOR_AUTOFILL_NO
            "yesExcludeDescendants" -> View.IMPORTANT_FOR_AUTOFILL_YES_EXCLUDE_DESCENDANTS
            "noExcludeDescendants" -> View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS
            else -> View.IMPORTANT_FOR_AUTOFILL_AUTO
          }
          view.importantForAutofill = importance
        }
      }

      Prop("returnKeyType") {view: NativeInputView, keyType: String ->
        val imeOption = when (keyType) {
          "done" -> EditorInfo.IME_ACTION_DONE
          "go" -> EditorInfo.IME_ACTION_GO
          "next" -> EditorInfo.IME_ACTION_NEXT
          "search" -> EditorInfo.IME_ACTION_SEARCH
          "send" -> EditorInfo.IME_ACTION_SEND
          else -> EditorInfo.IME_ACTION_UNSPECIFIED
        }
        view.editText.isSingleLine = true
        view.editText.imeOptions = imeOption
      }

      Prop("padding") { view: NativeInputView, value: Map<String, Int>? ->
        view.setPadding(value)
      }

      Prop("borderColors") { view: NativeInputView, colors: Map<String, String> ->
        view.currentColors = colors
        view.updateBorder()
      }

      Prop("error") { view: NativeInputView, hasError: Boolean ->
        view.isError = hasError
        view.updateBorder()
      }

      Prop("disabled") { view: NativeInputView, isDisabled: Boolean ->
        view.isDisabled = isDisabled
        view.editText.isEnabled = !isDisabled
      }

      Prop("secureTextEntry") { view: NativeInputView, isSecure: Boolean ->
        view.isSecure = isSecure
        view.toggleSecureTextEntry()
      }

      Prop("rightIcon") {view: NativeInputView, source: Map<String, Any>? ->
        view.setRightIcon(source)
      }

      AsyncFunction("isFocused") { view: NativeInputView, ->
        view.editText.isFocused
      }

      AsyncFunction("focus") { view: NativeInputView, ->
        view.editText.requestFocus()
      }

      AsyncFunction("blur") { view: NativeInputView ->
        view.editText.clearFocus()
      }

      AsyncFunction("setText") { view: NativeInputView, text: String ->
        view.editText.setText(text)
      }

      AsyncFunction("setSelection") {  view: NativeInputView, start: Int, end: Int ->
        view.editText.setSelection(start, end)
      }
    }
  }
}
