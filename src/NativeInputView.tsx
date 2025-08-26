import { requireNativeView } from 'expo';
import * as React from 'react';

import { NativeInputViewProps, NativeInputViewRef } from './NativeInput.types';
import { Image } from 'react-native';

const NativeView = requireNativeView<NativeInputViewProps & {ref: React.Ref<NativeInputViewRef>}>('NativeInput');

const NativeInputView = React.forwardRef<
  React.ComponentRef<typeof NativeView>,
  NativeInputViewProps
>((props, ref) => {
  const nativeViewRef = React.useRef<NativeInputViewRef>(null);

  React.useImperativeHandle(ref, () => {
    return {
      focus: () => { nativeViewRef.current?.focus() },
      blur: () => { nativeViewRef.current?.blur() },
      isFocused: () => (nativeViewRef.current?.isFocused() ?? false),
      setText: (text) => nativeViewRef.current?.setText(text),
      getNativeRef: () => nativeViewRef.current,
      setSelection: nativeViewRef.current?.setSelection
    }
  })

  return <NativeView 
    ref={nativeViewRef} 
    {...props} 
    onInputChange={({nativeEvent}) => {
      props.onChangeText?.(nativeEvent.text)
    }}
    rightIcon={props.rightIcon ?{
      source: Image.resolveAssetSource(props.rightIcon?.source as number)?.uri,
      size: props.rightIcon?.size ?? 24,
    } : undefined}
  />
})

export default NativeInputView;