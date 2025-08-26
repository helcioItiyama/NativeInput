import ExpoModulesCore
import WebKit

class NativeInputView: ExpoView {
  let textField = ExtendedTextField()
  private var rightButton: UIButton?
  private var rightContainer: UIView?

  var currentColors: [String: String] = [:] { didSet { updateBorder() } }
  var isError: Bool = false { didSet { updateBorder() } }
  var isDisabled: Bool = false { didSet { textField.isEnabled = !isDisabled } }
  var isSecure: Bool = false { didSet { textField.isSecureTextEntry = isSecure } }
    
  private var padding: UIEdgeInsets = .zero
    
  let onInputChange = EventDispatcher()
  let onInputFocus = EventDispatcher()
  let onInputBlur = EventDispatcher()
  let onInputSubmit = EventDispatcher()
  let onRightIconClick = EventDispatcher()

  required init(appContext: AppContext? = nil) {
    super.init(appContext: appContext)
    textField.borderStyle = .roundedRect
    textField.addTarget(self, action: #selector(textChanged), for: .editingChanged)
    textField.delegate = self
    addSubview(textField)
    updateBorder()
  }

  override func layoutSubviews() {
    super.layoutSubviews()
    textField.frame = bounds
  }
    
  func setPadding(_ top: CGFloat?, _ right: CGFloat?, _ bottom: CGFloat?, _ left: CGFloat?) {
    padding.top = top ?? 0
    padding.right = right ?? 0
    padding.bottom = bottom ?? 0
    padding.left = left ?? 0
    
    textField.contentPadding = padding
    
    if let button = rightButton {
        let size = button.frame.size.width
        let containerWidth = size + padding.right
        rightContainer?.frame = CGRect(x: 0, y: 0, width: containerWidth, height: size)
        button.frame.origin = CGPoint(x: 0, y: 0)
    }
    
    setNeedsLayout()
  }
      
  func setRightIcon(_ source: [String: Any]?) {
    guard let source = source else {
      textField.rightView = nil
      rightButton = nil
      rightContainer = nil
      return
    }
      
    let size = (source["size"] as? CGFloat) ?? 24
    let button = rightButton ?? UIButton(type: .custom)
    button.frame.size = CGSize(width: size, height: size)
    button.addTarget(self, action: #selector(rightIconTapped), for: .touchUpInside)
    rightButton = button
    
    let containerWidth = size + padding.right
    let container = UIView(frame: CGRect(x: 0, y: 0, width: containerWidth, height: size))
    container.addSubview(button)
    button.frame.origin = CGPoint(x: 0, y: 0)
    
    rightContainer = container
    textField.rightView = container
    textField.rightViewMode = .always

    if let uri = source["source"] as? String {
      if uri.hasPrefix("file://") || uri.hasPrefix("/") {
        if let image = UIImage(contentsOfFile: uri.replacingOccurrences(of: "file://", with: "")) {
          button.setImage(image, for: .normal)
        }
      } else if let url = URL(string: uri) {
        URLSession.shared.dataTask(with: url) { data, _, _ in
          guard let data = data, let image = UIImage(data: data) else { return }
          DispatchQueue.main.async {
            button.setImage(image, for: .normal)
          }
        }.resume()
      }
    }
  }
      
  @objc private func rightIconTapped() {
    let currentText = textField.text ?? ""
    let selectedRange = textField.selectedTextRange
    isSecure.toggle()
    textField.isSecureTextEntry = isSecure
    textField.text = currentText
    textField.selectedTextRange = selectedRange
    onRightIconClick([:])
  }
      
  func updateBorder() {
    let layerRef = textField.layer
    layerRef.borderWidth = 1
    
    if isError, let errorColor = currentColors["error"] {
      layerRef.borderColor = UIColor(hex: errorColor).cgColor
      return
    }
    
    if isDisabled, let disableColor = currentColors["disabled"] {
      layerRef.borderColor = UIColor(hex: disableColor).cgColor
      return
    }
    
    if textField.isFirstResponder, let focusColor = currentColors["focused"] {
      layerRef.borderColor = UIColor(hex: focusColor).cgColor
      return
    }
    
    if let normalColor = currentColors["unfocused"] {
      layerRef.borderColor = UIColor(hex: normalColor).cgColor
    }
  }
      
  @objc private func textChanged() {
    onInputChange(["text": textField.text ?? ""])
  }
}

extension NativeInputView: UITextFieldDelegate {
  func textFieldDidBeginEditing(_ textField: UITextField) {
    updateBorder()
    onInputFocus([:])
  }
    
  func textFieldDidEndEditing(_ textField: UITextField) {
    updateBorder()
    onInputBlur([:])
  }
    
  func textFieldShouldReturn(_ textField: UITextField) -> Bool {
    onInputSubmit(["text": textField.text ?? ""])
    return true
  }
}

final class ExtendedTextField: UITextField {
  var contentPadding: UIEdgeInsets = .zero { didSet { setNeedsLayout() } }
    
  override var isSecureTextEntry: Bool {
    didSet {
      if isFirstResponder {
        _ = becomeFirstResponder()
      }
    }
  }

  override func becomeFirstResponder() -> Bool {
    let success = super.becomeFirstResponder()
      if isSecureTextEntry, let text = self.text {
        self.text = ""
        insertText(text)
      }
    return success
  }

  override func textRect(forBounds bounds: CGRect) -> CGRect { inset(bounds) }
  override func editingRect(forBounds bounds: CGRect) -> CGRect { inset(bounds) }
  override func placeholderRect(forBounds bounds: CGRect) -> CGRect { inset(bounds) }

  private func inset(_ bounds: CGRect) -> CGRect {
    var insets = contentPadding
    if let left = leftView, leftViewMode != .never { insets.left += left.bounds.width + 6 }
    if let right = rightView, rightViewMode != .never { insets.right += right.bounds.width + 6 }
    return bounds.inset(by: insets)
  }
}
