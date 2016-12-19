package com.example.disklrucache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.disklrucache.Utils.FileUtils;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 博客地址：http://blog.csdn.net/guolin_blog/article/details/28863651
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String imageUrl;
    private String key;
    private DiskLruCache mDiskLruCache;

    private ImageView mImage;

    private Button btn_write;
    private Button btn_read;
    private Button btn_remove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImage = (ImageView) findViewById(R.id.iv_cache);
        btn_write = (Button) findViewById(R.id.btn_write);
        btn_read = (Button) findViewById(R.id.btn_read);
        btn_remove = (Button) findViewById(R.id.btn_remove);

        btn_write.setOnClickListener(this);
        btn_read.setOnClickListener(this);
        btn_remove.setOnClickListener(this);

        imageUrl = "http://img.my.csdn.net/uploads/201309/01/1378037235_7476.jpg";
        key = FileUtils.hashKeyForDisk(imageUrl);
        mDiskLruCache = FileUtils.open(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_write:
                //写入缓存
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                            if (editor != null) {
                                OutputStream outputStream = editor.newOutputStream(0);
                                if (FileUtils.downloadUrlToStream(imageUrl, outputStream)) {
                                    editor.commit();
                                } else {
                                    editor.abort();
                                }
                            }
                            mDiskLruCache.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;

            case R.id.btn_read:
                //读取缓存
                try {
                    DiskLruCache.Snapshot snapShot = mDiskLruCache.get(key);
                    if (snapShot != null) {
                        InputStream is = snapShot.getInputStream(0);
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        mImage.setImageBitmap(bitmap);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.btn_remove:
                //移除缓存
                try {
                    mDiskLruCache.remove(key);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            //返回当前缓存路径下所有缓存数据的总字节数，以byte为单位
//            mDiskLruCache.size();

            //用于将内存中的操作记录同步到日志文件（也就是journal文件）当中
            //比较标准的做法就是在Activity的onPause()方法中去调用一次flush()方法就可以了
//            mDiskLruCache.flush();

            //用于将DiskLruCache关闭掉，是和open()方法对应的一个方法。
            // 关闭掉了之后就不能再调用DiskLruCache中任何操作缓存数据的方法，
            // 通常只应该在Activity的onDestroy()方法中去调用close()方法
//            mDiskLruCache.close();

            //用于将所有的缓存数据全部删除
//            mDiskLruCache.delete();
        }
    }
}
