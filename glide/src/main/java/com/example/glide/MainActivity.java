package com.example.glide;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.StringSignature;
import com.example.glide.interfacepackage.JpgDataModel;
import com.example.glide.interfacepackage.MyDataLoader;
import com.example.glide.interfacepackage.WebpDataModel;

/**
 * 博客地址：http://www.cnblogs.com/whoislcj/p/5558168.html<br/>
 * <p>
 * Glide特点:<br/>
 * <p>
 * 可配置度高，自适应程度高<br/>
 * 支持常见图片格式 Jpg png gif webp<br/>
 * 支持多种数据源  网络、本地、资源、Assets 等<br/>
 * 高效缓存策略    支持Memory和Disk图片缓存 默认Bitmap格式采用RGB_565内存使用至少减少一半<br/>
 * 生命周期集成   根据Activity/Fragment生命周期自动管理请求<br/>
 * 高效处理Bitmap  使用Bitmap Pool使Bitmap复用，主动调用recycle回收需要回收的Bitmap，减小系统回收压力<br/>
 */
public class MainActivity extends AppCompatActivity {

    private String imageUrl;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.iv_glide);

//        Glide.with(Context context);// 绑定Context
//        Glide.with(Activity activity);// 绑定Activity
//        Glide.with(FragmentActivity activity);// 绑定FragmentActivity
//        Glide.with(Fragment fragment);// 绑定Fragment

        imageUrl = "http://img.my.csdn.net/uploads/201309/01/1378037235_7476.jpg";

        //简单的加载图片实例
        Glide.with(this).load(imageUrl).into(imageView);

        Glide.with(this).load(imageUrl)
                //设置加载中以及加载失败图片
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                //设置跳过内存缓存
                .skipMemoryCache(true)
                //设置下载优先级
                .priority(Priority.NORMAL)
                //设置缓存策略
                // all:     缓存源资源和转换后的资源
                // none:    不作任何磁盘缓存
                // source:  缓存源资源
                // result：  缓存转换后的资源
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                //设置加载动画
                .animate(R.anim.item_alpha_in)
                //设置缩略图支持 会先加载缩略图 然后再加载全图
                .thumbnail(0.1f)
                //设置加载尺寸
                .override(800, 800)
                //设置动态转换
                .centerCrop()
                // api提供了比如：centerCrop()、fitCenter()等函数
                // 也可以通过自定义Transformation，如：
                .transform(new GlideRoundTransform(this))
                .into(imageView);

        //设置要加载的内容
        //项目中有很多需要先下载图片然后再做一些合成的功能，比如项目中出现的图文混排
        Glide.with(this).load(imageUrl).centerCrop().into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                imageView.setImageDrawable(resource);
            }
        });

        //设置监听的用处 可以用于监控请求发生错误来源，以及图片来源 是内存还是磁盘
        Glide.with(this).load(imageUrl).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                //imageView.setImageDrawable(resource);
                return false;
            }
        }).into(imageView);

        //显示gif静态图片
        Glide.with(this).load(imageUrl).asBitmap().into(imageView);
        //显示gif动态图片
        Glide.with(this).load(imageUrl).asGif().into(imageView);

        Glide.get(this).clearDiskCache();//清理磁盘缓存 需要在子线程中执行
        Glide.get(this).clearMemory();//清理内存缓存  可以在UI主线程中进行

        //加载jpg图片
        Glide.with(this).using(new MyDataLoader(this)).load(new JpgDataModel(imageUrl)).into(imageView);
        //加载webp图片
        Glide.with(this).using(new MyDataLoader(this)).load(new WebpDataModel(imageUrl)).into(imageView);

        //使用StringSignature
        Glide.with(this).load(new JpgDataModel(imageUrl)).signature(new StringSignature("1.0.0")).into(imageView);

        //使用MediaStoreSignature
//        Glide.with(this) .load(mediaStoreUri).signature(new MediaStoreSignature(mimeType, dateModified, orientation)).into(imageView);
    }
}
