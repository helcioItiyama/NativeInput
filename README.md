# Expo Native TextInput

A **fully-featured, native `TextInput` module for Expo** with support for controlled input, right-side icons, secure text entry, and customizable borders. Built with **Kotlin (Android)** and **Swift (iOS)** for seamless cross-platform behavior.  

---

<p align="center" >
    <img
      src="https://github.com/helcioItiyama/NativeInput/blob/main/doc/assets/input.gif?raw=true"
      alt="Android demo"
      width="39.2%"
    >
    <img
      src="https://github.com/helcioItiyama/NativeInput/blob/main/doc/assets/input2.gif?raw=true"
      alt="iOS Demo"
      width="40%"
    >
  <br>
</div>

## ✨ Features

- ✅ **Controlled input** with stable keyboard behavior.
- 🔒 **Secure text entry toggle** (show/hide password).
- 🎨 **Customizable borders** for focused, unfocused, error, and disabled states.
- 🖼️ **Right-side icon support** with adjustable size and proper alignment. 

## 🚀 Usage
```javascript
import { NativeInputView, NativeInputViewRef } from 'native-input';
import { useRef, useState } from 'react';

export default function App() {
  const [text, setText] = useState("")
  const [error, setError] = useState(false)
  const [showPassword, setShowPassword] = useState(true)

  const inputRef = useRef<NativeInputViewRef>(null)

  return (
    <NativeInputView
      ref={inputRef}
      borderColors={{
        focused: "#0b278c",
        unfocused: "#000000",
        error: "#9d0606",
      }}
      text={text}
      onChangeText={setText}
      secureTextEntry={showPassword}
      rightIcon={{ 
        source: require("./assets/eye.png"), 
        size: 24 
      }}
      onRightIconClick={() => {
        setShowPassword(state => !state)
      }}
    />
  )
}
```

---
