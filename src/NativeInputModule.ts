import { NativeModule, requireNativeModule } from 'expo';

declare class NativeInputModule extends NativeModule {}

// This call loads the native module object from the JSI.
export default requireNativeModule<NativeInputModule>('NativeInput');
