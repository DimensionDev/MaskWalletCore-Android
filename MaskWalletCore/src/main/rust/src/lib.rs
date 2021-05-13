#[cfg(target_os = "android")]
#[allow(non_snake_case)]
pub mod android {
    extern crate jni;
    use self::jni::objects::{JClass, JString};
    use self::jni::JNIEnv;
    use jni::sys::{jstring};


    #[no_mangle]
    pub unsafe extern "system" fn Java_com_dimension_maskwalletcore_MaskWalletCore_test(
        _env: JNIEnv,
        _class: JClass,
        input: JString,
    ) -> jstring {
        let input_buffer: String = _env
            .get_string(input)
            .expect("Couldn't get java string!")
            .into();
        return _env.new_string(input_buffer).expect("Couldn't create java string!").into_inner();
    }

}