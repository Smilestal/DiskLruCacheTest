package com.example.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.GlideModule;
import com.example.glide.interfacepackage.IDataModel;
import com.example.glide.interfacepackage.MyDataLoader;

import java.io.File;
import java.io.InputStream;

/**
 * 博客地址：http://www.cnblogs.com/whoislcj/p/5565012.html
 */
public class MyGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // Apply options to the builder here.

        //=============设置Glide内存缓存大小===============
        int maxMemory = (int) Runtime.getRuntime().maxMemory();//获取系统分配给应用的总内存大小
        int memoryCacheSize = maxMemory / 8;//设置图片内存缓存占用八分之一
        //设置内存缓存大小
        builder.setMemoryCache(new LruResourceCache(memoryCacheSize));

        //获取默认的内存使用计算函数
        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();

        //=============设置Glide磁盘缓存大小===============
        File cacheDir = context.getExternalCacheDir();//指定的是数据的缓存地址
        int diskCacheSize = 1024 * 1024 * 30;//最多可以缓存多少字节的数据
        //设置磁盘缓存大小
        builder.setDiskCache(new DiskLruCacheFactory(cacheDir.getPath(), "glide", diskCacheSize));
        //存放在data/data/xxxx/cache/
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, "glide", diskCacheSize));
        //存放在外置文件浏览器
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, "glide", diskCacheSize));

        //设置图片解码格式
        //默认格式RGB_565使用内存是ARGB_8888的一半，但是图片质量就没那么高了，而且不支持透明度
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);

        //设置BitmapPool缓存内存大小
        builder.setBitmapPool(new LruBitmapPool(memoryCacheSize));

        //设置一个用来检索cache中没有的Resource的ExecutorService
        //为了使缩略图请求正确工作，实现类必须把请求根据Priority优先级排好序。
//        builder.setDiskCacheService(ExecutorService service);
//        builder.setResizeService(ExecutorService service);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        // register ModelLoaders here.
        glide.register(IDataModel.class, InputStream.class, new MyDataLoader.Factory());
    }
}
