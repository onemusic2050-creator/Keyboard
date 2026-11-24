package com.example.mycustomkeyboard;

import android.inputmethodservice.InputMethodService;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.KeyEvent;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.TypedValue;
import java.util.HashMap;
import java.util.Map;

/** Offline keyboard. No network permission or collection of typed text. */
public class CustomKeyboardService extends InputMethodService {
    private boolean symbols = false, customPage = false, caps = false;
    private Typeface customTypeface;
    private final Map<String,String> replacements = new HashMap<>();
    private final int bg = Color.rgb(27,29,33), keyBg = Color.rgb(54,58,64), specialBg = Color.rgb(78,84,93);

    @Override public void onCreate() {
        super.onCreate();
        replacements.put("a", "Ⲁ"); replacements.put("b", "𐐺"); replacements.put("w", "ယ"); replacements.put("x", "ⴳ");
        replacements.put("A", "Ⲁ"); replacements.put("B", "𐐺"); replacements.put("W", "ယ"); replacements.put("X", "ⴳ");
        try { customTypeface = Typeface.createFromAsset(getAssets(), "fonts/custom.ttf"); } catch (Exception ignored) { }
    }

    @Override public View onCreateInputView() {
        LinearLayout root = new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(5),dp(7),dp(5),dp(7)); root.setBackgroundColor(bg);
        LinearLayout top = new LinearLayout(this); top.setGravity(Gravity.CENTER);
        top.addView(key(customPage ? "ABC" : "Custom", specialBg), weight(1));
        top.addView(key("⇧", specialBg), weight(1));
        top.addView(key("🌐", specialBg), weight(1)); root.addView(top);
        if (customPage) {
            row(root, new String[]{"Ⲁ","𐐺","ယ","ⴳ","ɑ","ɓ","ɔ","ɗ","ɛ","ə"});
            row(root, new String[]{"ɟ","ɡ","ɥ","ɨ","ɪ","ŋ","ɵ","ʊ","ʋ"});
            row(root, new String[]{"ʌ","ʒ","ʔ","ʕ","ɕ","ʑ","⌫"});
            row(root, new String[]{"A","B","C","D","E","F","G","H","I","J"});
            row(root, new String[]{"K","L","M","N","O","P","Q","R","S","T"});
            row(root, new String[]{"U","V","W","X","Y","Z"});
        } else if (symbols) {
            row(root, new String[]{"1","2","3","4","5","6","7","8","9","0"});
            row(root, new String[]{"@","#","$","%","&","-","+","(",")","/"});
            row(root, new String[]{"*","\"","'",":",";","!","?","⌫"});
        } else {
            row(root, new String[]{"q","w","e","r","t","y","u","i","o","p"});
            row(root, new String[]{"a","s","d","f","g","h","j","k","l"});
            row(root, new String[]{"⇧","z","x","c","v","b","n","m","⌫"});
        }
        LinearLayout bottom = new LinearLayout(this); bottom.setGravity(Gravity.CENTER);
        bottom.addView(key(symbols ? "ABC" : "?123", specialBg), weight(1));
        bottom.addView(key(","), weight(1)); bottom.addView(key("space"), weight(5));
        bottom.addView(key("."), weight(1)); bottom.addView(key("↵", specialBg), weight(1)); root.addView(bottom);
        return root;
    }
    private void row(LinearLayout root, String[] labels) {
        LinearLayout line = new LinearLayout(this); line.setGravity(Gravity.CENTER); line.setPadding(0,dp(2),0,dp(2));
        for(String s:labels) line.addView(key(s), weight(s.equals("⌫")||s.equals("⇧")?1.25f:1)); root.addView(line);
    }
    private LinearLayout.LayoutParams weight(float w) { return new LinearLayout.LayoutParams(0,dp(46),w); }
    private Button key(String label) { return key(label,keyBg); }
    private Button key(String label,int color) {
        Button b=new Button(this); b.setText(label.equals("space")?" ":label); b.setAllCaps(false);
        b.setTextSize(TypedValue.COMPLEX_UNIT_SP, label.length()>1?15:20); b.setTextColor(Color.WHITE);
        if(customTypeface!=null && label.length()==1) b.setTypeface(customTypeface);
        GradientDrawable shape=new GradientDrawable(); shape.setColor(color); shape.setCornerRadius(dp(7)); b.setBackground(shape); b.setPadding(0,0,0,0);
        b.setOnClickListener(v->press(label)); return b;
    }
    private void press(String label) {
        if(label.equals("Custom")||label.equals("ABC")){customPage=!customPage;symbols=false;setInputView(onCreateInputView());return;}
        if(label.equals("⇧")){caps=!caps;setInputView(onCreateInputView());return;}
        if(label.equals("?123")){symbols=true;customPage=false;setInputView(onCreateInputView());return;}
        if(label.equals("🌐")){try{switchToNextInputMethod(false);}catch(Exception ignored){}return;}
        InputConnection ic=getCurrentInputConnection(); if(ic==null)return;
        if(label.equals("⌫")){ic.deleteSurroundingText(1,0);return;}
        if(label.equals("↵")){ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_ENTER));ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP,KeyEvent.KEYCODE_ENTER));return;}
        if(label.equals("space")){ic.commitText(" ",1);return;}
        String out=label;
        if(!symbols && !customPage && label.length()==1 && Character.isLetter(label.charAt(0))) {
            String base=caps?label.toUpperCase():label.toLowerCase(); out=replacements.containsKey(base)?replacements.get(base):base;
        }
        ic.commitText(out,1);
    }
    private int dp(int v){return (int)(v*getResources().getDisplayMetrics().density);}
}
