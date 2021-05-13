#[cfg(target_os = "android")]
#[allow(non_snake_case)]
pub mod android {
    extern crate jni;
    use self::jni::objects::{JClass};
    use self::jni::JNIEnv;
    use jni::sys::{jbyteArray};
    use std::slice;

    #[no_mangle]
    pub unsafe extern "system" fn Java_com_dimension_maskwalletcore_MaskWalletCore_request(
        _env: JNIEnv,
        _class: JClass,
        input: jbyteArray,
    ) -> jbyteArray {
        let data = _env.convert_byte_array(input).unwrap();
        let bytes = interface::call_api(&data);
        _env.byte_array_from_slice(&bytes).unwrap()
    }
}