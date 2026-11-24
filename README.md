# My Custom Keyboard (Android IME)

A local/offline Android keyboard project. It contains no network permission or typed-text collection.

## Included
- QWERTY layout with shift, backspace, enter, space, symbols toggle, and Android keyboard switch
- Special-character page (tap **Custom**) with a dedicated set of visible glyph keys
- Requested mappings on the normal alphabet: a/A -> Ⲁ, b/B -> 𐐺, w/W -> ယ, x/X -> ⴳ
- Custom font asset copied from `Myfont-Regular.ttf` as `app/src/main/assets/fonts/custom.ttf`

Note: The Calligraphr PDF is a template, not an encoded character-map file. This project includes the specified mappings and a custom-character page, but does not claim to have extracted every glyph from the template. The font is used for keyboard key labels where Android can render its glyphs; the typed output is Unicode.

## Build APK
1. Install Android Studio on a computer and open this folder as a project.
2. Let Gradle sync. If prompted, install Android SDK Platform 35 and compatible build tools.
3. Select **Build > Build Bundle(s) / APK(s) > Build APK(s)**.
4. Install the generated APK on your Android phone.
5. On the phone, go to Settings > System > Languages & input > On-screen keyboard (names vary), enable **My Custom Keyboard**, then select it from the keyboard picker.

This project source has not been compiled or device-tested in this environment.
