package in.jivanmuktas.www.marg.utils;

import android.content.Context;
import android.graphics.Typeface;

import java.lang.reflect.Field;

/**
 * Created by developer on 23-Oct-17.
 */

public class ReplaceFont {

    public static void replaceDefaultFont(Context context,
                                          String nameOfFontBeingReplaced,
                                          String nameOfFontInAssets){
        Typeface customFontTypeface = Typeface.createFromAsset(context.getAssets(),nameOfFontInAssets);
        replaceFont(nameOfFontBeingReplaced,customFontTypeface);
    }

    private static void replaceFont(String nameOfFontBeingReplaced, Typeface customFontTypeface) {
        try{
            Field myfield = Typeface.class.getDeclaredField(nameOfFontBeingReplaced);
            myfield.setAccessible(true);
            myfield.set(null,customFontTypeface);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
