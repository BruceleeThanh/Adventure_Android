package studio.crazybt.adventure.utils;

import android.content.Context;
import android.support.annotation.DimenRes;
/**
 * Created by Brucelee Thanh on 08/01/2017.
 */

public final class ResourceUtils {

    private ResourceUtils() throws InstantiationException {
        throw new InstantiationException("This utility class is created for instantiation");
    }

    public static float getDimension(Context context, @DimenRes int resourceId) {
        return context.getResources().getDimension(resourceId);
    }

    public static int getDimensionPixelSize(Context context, @DimenRes int resourceId) {
        return context.getResources().getDimensionPixelSize(resourceId);
    }

}