package com.example.glide.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by POST on 2016/12/15.
 */
public class BitmapUtils {

    /**
     * 获取缩放后的本地图片<br/>
     * 从文件读取方式一
     *
     * @param filePath 文件路径
     * @param width    宽
     * @param height   高
     * @return
     */
    public static Bitmap readBitmapFromFile(String filePath, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;
        int inSampleSize = 1;

        if (srcHeight > height || srcWidth > width) {
            if (srcWidth > srcHeight) {
                inSampleSize = Math.round(srcHeight / height);
            } else {
                inSampleSize = Math.round(srcWidth / width);
            }
        }

        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;

        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 获取缩放后的本地图片<br/>
     * 从文件读取方式二 效率高于方式一
     *
     * @param filePath 文件路径
     * @param width    宽
     * @param height   高
     * @return
     */
    public static Bitmap readBitmapFromFileDescriptor(String filePath, int width, int height) {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(fis.getFD(), null, options);
            float srcWidth = options.outWidth;
            float srcHeight = options.outHeight;
            int inSampleSize = 1;

            if (srcHeight > height || srcWidth > width) {
                if (srcWidth > srcHeight) {
                    inSampleSize = Math.round(srcHeight / height);
                } else {
                    inSampleSize = Math.round(srcWidth / width);
                }
            }

            options.inJustDecodeBounds = false;
            options.inSampleSize = inSampleSize;

            return BitmapFactory.decodeFileDescriptor(fis.getFD(), null, options);
        } catch (Exception ex) {
        }
        return null;
    }

    /**
     * 获取缩放后的本地图片
     * 从输入流中读取文件
     *
     * @param ins    输入流
     * @param width  宽
     * @param height 高
     * @return
     */
    public static Bitmap readBitmapFromInputStream(InputStream ins, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(ins, null, options);
        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;
        int inSampleSize = 1;

        if (srcHeight > height || srcWidth > width) {
            if (srcWidth > srcHeight) {
                inSampleSize = Math.round(srcHeight / height);
            } else {
                inSampleSize = Math.round(srcWidth / width);
            }
        }

        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;

        return BitmapFactory.decodeStream(ins, null, options);
    }

    /**
     * 从资源文件中读取文件
     *
     * @param resources
     * @param resourcesId
     * @param width
     * @param height
     * @return
     */
    public static Bitmap readBitmapFromResource1(Resources resources, int resourcesId, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, resourcesId, options);
        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;
        int inSampleSize = 1;

        if (srcHeight > height || srcWidth > width) {
            if (srcWidth > srcHeight) {
                inSampleSize = Math.round(srcHeight / height);
            } else {
                inSampleSize = Math.round(srcWidth / width);
            }
        }

        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;

        return BitmapFactory.decodeResource(resources, resourcesId, options);
    }

    /**
     * 从资源文件中读取文件<br/>
     * <p>
     * BitmapFactory.decodeResource 加载的图片可能会经过缩放，
     * 该缩放目前是放在 java 层做的，效率比较低，而且需要消耗 java 层的内存。
     * 因此，如果大量使用该接口加载图片，容易导致OOM错误<br/>
     * BitmapFactory.decodeStream 不会对所加载的图片进行缩放，相比之下占用内存少，效率更高。<br/>
     * 这两个接口各有用处，如果对性能要求较高，则应该使用 decodeStream；<br/>
     * 如果对性能要求不高，且需要 Android 自带的图片自适应缩放功能，则可以使用 decodeResource。
     *
     * @param resources
     * @param resourcesId
     * @param width
     * @param height
     * @return
     */
    public static Bitmap readBitmapFromResource2(Resources resources, int resourcesId, int width, int height) {
        InputStream ins = resources.openRawResource(resourcesId);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(ins, null, options);
        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;
        int inSampleSize = 1;

        if (srcHeight > height || srcWidth > width) {
            if (srcWidth > srcHeight) {
                inSampleSize = Math.round(srcHeight / height);
            } else {
                inSampleSize = Math.round(srcWidth / width);
            }
        }

        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;

        return BitmapFactory.decodeStream(ins, null, options);
    }

    /**
     * 从二进制数据读取图片
     *
     * @param data
     * @param width
     * @param height
     * @return
     */
    public static Bitmap readBitmapFromByteArray(byte[] data, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;
        int inSampleSize = 1;

        if (srcHeight > height || srcWidth > width) {
            if (srcWidth > srcHeight) {
                inSampleSize = Math.round(srcHeight / height);
            } else {
                inSampleSize = Math.round(srcWidth / width);
            }
        }

        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;

        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }

    /**
     * 获取缩放后的本地图片<br/>
     * 从assets文件读取图片
     *
     * @param filePath 文件路径
     * @return
     */
    public static Bitmap readBitmapFromAssetsFile(Context context, String filePath) {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(filePath);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * 图片保存文件
     *
     * @param filePath
     * @param b
     * @param quality
     */
    public static void writeBitmapToFile(String filePath, Bitmap b, int quality) {
        try {
            File desFile = new File(filePath);
            FileOutputStream fos = new FileOutputStream(desFile);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            b.compress(Bitmap.CompressFormat.JPEG, quality, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 图片压缩
     *
     * @param image
     * @return
     */
    private static Bitmap compressImage(Bitmap image) {
        if (image == null) {
            return null;
        }
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] bytes = baos.toByteArray();
            ByteArrayInputStream isBm = new ByteArrayInputStream(bytes);
            Bitmap bitmap = BitmapFactory.decodeStream(isBm);
            return bitmap;
        } catch (OutOfMemoryError e) {
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
            }
        }
        return null;
    }

    /**
     * 图片缩放<br/>
     * 根据scale生成一张图片
     *
     * @param bitmap
     * @param scale  等比缩放值
     * @return
     */
    public static Bitmap bitmapScale(Bitmap bitmap, float scale) {
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale); // 长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }

    /**
     * 读取照片exif信息中的旋转角度
     *
     * @param path 照片路径
     * @return角度
     */
    private static int readPictureDegree(String path) {
        if (TextUtils.isEmpty(path)) {
            return 0;
        }
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (Exception e) {
        }
        return degree;
    }

    /**
     * 图片旋转角度
     *
     * @param b
     * @param rotateDegree
     * @return
     */
    private static Bitmap rotateBitmap(Bitmap b, float rotateDegree) {
        if (b == null) {
            return null;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(rotateDegree);
        Bitmap rotaBitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);
        return rotaBitmap;
    }

    /**
     * 图片转二进制
     *
     * @param bm
     * @return
     */
    public byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * Bitmap转Drawable
     *
     * @param resources
     * @param bm
     * @return
     */
    public static Drawable bitmapToDrawable(Resources resources, Bitmap bm) {
        Drawable drawable = new BitmapDrawable(resources, bm);
        return drawable;
    }

    /**
     * Drawable转Bitmap<br/>
     * Drawable 相对Bitmap有很大的内存占用优势。这也是为啥现在主流的图片缓存框架内存缓存那一层采用Drawable作为缓存对象的原因。
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
