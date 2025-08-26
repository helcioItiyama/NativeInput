import ExpoModulesCore

public class NativeInputModule: Module {
    public func definition() -> ModuleDefinition {
        
        Name("NativeInput")
        
        View(NativeInputView.self) {
            
            Prop("text") {(view: NativeInputView, text: String) in
                view.textField.text = text
            }
            
            Prop("keyboardType") { (view: NativeInputView, type: String) in
                switch type {
                    case "numeric": view.textField.keyboardType = .numberPad
                    case "phone": view.textField.keyboardType = .phonePad
                    case "email": view.textField.keyboardType = .emailAddress
                    case "password": view.textField.keyboardType = .default
                    default: view.textField.keyboardType = .default
                }
            }
            
            Prop("autoComplete") { (view: NativeInputView, hint: String) in
                if #available(iOS 10.0, *) {
                    switch hint {
                        case "email": view.textField.textContentType = .emailAddress
                        case "password": view.textField.textContentType = .password
                        case "newPassword": view.textField.textContentType = .newPassword
                        case "username": view.textField.textContentType = .username
                        case "phone": view.textField.textContentType = .telephoneNumber
                        default: view.textField.textContentType = .none
                     }
                 }
             }
            
            Prop("returnKeyType") { (view: NativeInputView, keyType: String) in
                switch keyType {
                    case "done": view.textField.returnKeyType = .done
                    case "go": view.textField.returnKeyType = .go
                    case "next": view.textField.returnKeyType = .next
                    case "search": view.textField.returnKeyType = .search
                    case "send": view.textField.returnKeyType = .send
                    default: view.textField.returnKeyType = .default
                 }
            }

            Prop("padding") { (view: NativeInputView, value: [String: Int]?) in
                guard let value = value else { return }
                let top = CGFloat(value["top"] ?? 0)
                let right = CGFloat(value["right"] ?? 0)
                let bottom = CGFloat(value["bottom"] ?? 0)
                let left = CGFloat(value["left"] ?? 0)
                view.setPadding(top, right, bottom, left)
            }

            Prop("borderColors") { (view: NativeInputView, colors: [String: String]) in
                view.currentColors = colors
            }

            Prop("error") { (view: NativeInputView, hasError: Bool) in
                view.isError = hasError
            }

            Prop("disabled") { (view: NativeInputView, isDisabled: Bool) in
                view.isDisabled = isDisabled
            }

            Prop("secureTextEntry") { (view: NativeInputView, isSecure: Bool) in
                view.textField.isSecureTextEntry = isSecure
            }

            Prop("rightIcon") { (view: NativeInputView, source: [String: Any]?) in
                view.setRightIcon(source)
            }
            
            AsyncFunction("setText") { (view: NativeInputView, text: String) in
                view.textField.text = text
            }
            
            AsyncFunction("isFocused") { (view: NativeInputView) in
                view.isFocused
            }

            AsyncFunction("focus") { (view: NativeInputView) in
                view.textField.becomeFirstResponder()
            }

            AsyncFunction("blur") { (view: NativeInputView) in
                view.textField.resignFirstResponder()
            }
        
            Events(
                "onInputChange",
                "onInputSubmit",
                "onInputFocus",
                "onInputBlur",
                "onRightIconClick"
            )
        }
    }
}
