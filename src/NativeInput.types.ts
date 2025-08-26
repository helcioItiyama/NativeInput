import type { ViewProps } from 'react-native';

export type TextPayload = {
  text: string;
};

export type EventPayload<T> = {
 nativeEvent: T
};

export type NativeInputViewRef = {
  focus: () => void;
  blur: () => void;
  isFocused: () => boolean
  setText: (text: string) => void
  setSelection?: (start: number, end: number) => void
}

export interface NativeInputViewProps extends ViewProps {
  text?: string;
  onInputChange?: (e: EventPayload<TextPayload>) => void
  onChangeText?: (text: string) => void;
  onInputBlur?: () => void;
  onInputFocus?: () => void;
  onInputSubmit?: (e: EventPayload<TextPayload>) => void;
  onRightIconClick?: () => void;
  borderColors?: {
    focused?: string;
    unfocused?: string;
    disabled?: string;
    error?: string;
  };
  keyboardType?: "numeric" | "email" | "password" | "phone" ;
  importantForAutofill?: "auto" | "yes" | "no" | "yesExcludeDescendants" | "noExcludeDescendants"
  returnKeyType?: "done" | "go" | "next" | "search" | "send";
  disabled?: boolean;
  error?: boolean;
  padding?: {
    top?: number;
    right?: number;
    bottom?: number;
    left?: number;
  }
  rightIcon?: {source: string | number, size: number}
  secureTextEntry?: boolean;
  autoComplete?: string;
};
