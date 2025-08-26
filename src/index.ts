// Reexport the native module. On web, it will be resolved to NativeInputModule.web.ts
// and on native platforms to NativeInputModule.ts
export { default } from './NativeInputModule';
export { default as NativeInputView } from './NativeInputView';
export * from  './NativeInput.types';
