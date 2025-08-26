import { NativeInputView, NativeInputViewRef } from 'native-input';
import { useRef, useState } from 'react';
import { Alert, SafeAreaView, ScrollView, StyleSheet, Text, TouchableOpacity } from 'react-native';

const inputColors = {
  focused: "#000000",
  unfocused: "#aaaaaa",
  error: "#f60303"
}

const padding = {
  left: 12,
  right: 12,
}

export default function App() {
  const hide = require("./assets/hide.png")
  const show = require("./assets/show.png")

  const [name, setName] = useState("")
  const [email, setEmail] = useState("")
  const [familyName, setFamilyName] = useState("")
  const [password, setPassword] = useState("")
  const [error, setError] = useState(false)
  const [showPassword, setShowPassword] = useState(true)

  const emailRef = useRef<NativeInputViewRef>(null)
  const familyNameRef = useRef<NativeInputViewRef>(null)
  const passwordRef = useRef<NativeInputViewRef>(null)

  const handleSubmit = () => {
    console.log("Submit")
    passwordRef.current?.blur()
    setError(true)
    Alert.alert(
      "Credential Error", 
      "Email or Password is incorrect",
      [{text: "OK", onPress: () => {
        setError(false),
        setEmail("")
        setPassword("")
        emailRef.current?.focus()
      }}]
    )
  }

  const hasNameError = name.length > 0 && name.length < 2
  const hasFamilyNameError = familyName.length > 0 && familyName.length < 2
  const passwordIcon = showPassword ? show : hide

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView style={styles.scrollView}>
        <Text style={styles.title}>Sign-up</Text>

        <Text style={styles.label}>Given Name</Text>
        <NativeInputView
          text={name}
          style={styles.input}
          padding={padding}
          borderColors={inputColors}
          onChangeText={setName}
          onInputFocus={() => {console.log("Name input on focus")}}
          onInputBlur={() => {console.log("Name input on blur")}}
          error={hasNameError}
          onInputSubmit={familyNameRef.current?.focus}
          returnKeyType="next"
        />

        <Text style={styles.label}>Family Name</Text>
        <NativeInputView
          ref={familyNameRef}
          text={familyName}
          style={styles.input}
          padding={padding}
          borderColors={inputColors}
          onChangeText={setFamilyName}
          importantForAutofill="yes"
          error={hasFamilyNameError}
          onInputSubmit={emailRef.current?.focus}
          returnKeyType="next"
        />

        <Text style={styles.label}>Email</Text>
        <NativeInputView
          ref={emailRef}
          style={styles.input}
          padding={padding}
          borderColors={inputColors}
          text={email}
          onChangeText={setEmail}
          keyboardType="email"
          autoComplete="username"
          importantForAutofill="yes"
          onInputSubmit={passwordRef.current?.focus}
          error={error}
          returnKeyType="next"
        />

        <Text style={styles.label}>Password</Text>
        <NativeInputView
          ref={passwordRef}
          text={password}
          style={styles.input}
          padding={padding}
          borderColors={inputColors}
          onChangeText={setPassword}
          autoComplete="password"
          importantForAutofill="yes"
          secureTextEntry={showPassword}
          onInputSubmit={handleSubmit}
          onRightIconClick={() => {setShowPassword(state => !state)}}
          rightIcon={{ source: passwordIcon, size: 24 }}
          error={error}
          returnKeyType="done"
        />

        <TouchableOpacity style={styles.button} onPress={handleSubmit}>
          <Text style={styles.buttonLabel}>Submit</Text>
        </TouchableOpacity>
        </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#c9ebf7aa"
  },
  title: {
    fontSize: 20,
    fontWeight: "bold",
    textAlign: "center",
    marginBottom: 24
  },
  label: {
    fontSize: 16,
    marginBottom: 8
  },
  scrollView: {
    paddingTop: 32,
    marginHorizontal: 12,
  },
  input: {
    width: "100%", 
    height: 50,
    marginBottom: 24,
    backgroundColor: "#ffffffff"
  },
  button: {
    backgroundColor: "#20055eaa",
    padding: 16,
    borderRadius: 8,
    alignItems: "center",
    marginTop: 12,
  },
  buttonLabel: {
    color: "white",
    textTransform: "uppercase"
  }
});
