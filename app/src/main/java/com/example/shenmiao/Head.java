package com.example.shenmiao;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.shenmiao.PopupWindow.CommonPopupWindow;
import com.example.shenmiao.PopupWindow.CommonUtil;
import com.example.shenmiao.utils.FileStorage;
import com.example.shenmiao.utils.SPUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class Head extends AppCompatActivity implements CommonPopupWindow.ViewInterface, EasyPermissions.PermissionCallbacks {
    private CommonPopupWindow commonPopupWindow;
    private ImageView mIcon;
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    private String TAG = "";
    //Uri代表要操作的数据，Android上可用的每种资源 - 图像、视频片段等都可以用Uri来表示。换句话说：android系统中任何可用的资源（图像、视频、文件）
    // 都可以用uri表示。
    //uri讲解：
    //1.uri属性有以下4部分组成：android:scheme、android:host、android:port、android:path
    //其中host和port2个统称为authority。
    //2.要使authority（host和port）有意义，必须指定scheme；要使path有意义，必须使scheme和authority（host和port）有意义
    private Uri uri;
    private int type;
    Uri cropUri;
    String identifynumber,pho;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.head);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        Bundle bundle = getIntent().getExtras();
        identifynumber = bundle.getString("identifynumber");
        pho = bundle.getString("head");
        mIcon = findViewById(R.id.icon);
        if(pho.equals("p1"))
        {
            mIcon.setImageResource(R.mipmap.p1);
        }
        else{
            loadCircleImage(this, pho, mIcon);
        }
        mIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAll(v);
            }
        });
    }

    //全屏弹出
    public void showAll(View view) {
        if (commonPopupWindow != null && commonPopupWindow.isShowing()) return;
        View upView = LayoutInflater.from(this).inflate(R.layout.popup_up, null);
        //测量View的宽高
        CommonUtil.measureWidthAndHeight(upView);
        commonPopupWindow = new CommonPopupWindow.Builder(this)
                .setView(R.layout.popup_up)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, upView.getMeasuredHeight())
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setAnimationStyle(R.style.AnimUp)
                .setViewOnclickListener(this)
                .create();
        commonPopupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void getChildView(View view, int layoutResId) {
        switch (layoutResId) {
            case R.layout.popup_up:
                Button btn_take_photo = (Button) view.findViewById(R.id.btn_take_photo);
                Button btn_select_photo = (Button) view.findViewById(R.id.btn_select_photo);
                Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
                btn_take_photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        type = 1;
                        getPermission();  //写个if  判断现在6.0以上版本  不是直接调用方法
                        if (commonPopupWindow != null) {
                            commonPopupWindow.dismiss();
                        }
                    }
                });
                btn_select_photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        type = 2;
                        getPermission();
                        if (commonPopupWindow != null) {
                            commonPopupWindow.dismiss();
                        }
                    }
                });
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (commonPopupWindow != null) {
                            commonPopupWindow.dismiss();
                        }
                    }
                });
                view.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (commonPopupWindow != null) {
                            commonPopupWindow.dismiss();
                        }
                        return true;
                    }
                });
                break;
        }
    }


    //获取权限
    public void getPermission() {
        //检测是否有权限
        if (EasyPermissions.hasPermissions(this, permissions)) {
            switch (type) {
                case 1:
                    getCamera();
                    break;
                case 2:
                    getPhotoAlbum();
                    break;
            }
        } else {
            EasyPermissions.requestPermissions(this, "用于读取相册和拍照功能", 1, permissions);
        }
    }

    /* intent 的重要属性
     * Action：Action属性的值为一个字符串，它代表了系统中已经定义了一系列常用的动作。
     * 通过setAction()方法或在清单文件AndroidManifest.xml中设置。默认为：DEFAULT。
     *Data：Data通常是URI格式定义的操作数据。例如：tel:// 。通过setData()方法设置。
     *Category：Category属性用于指定当前动作（Action）被执行的环境。通过addCategory()方法或在清单文件AndroidManifest.xml中设置。
     *默认为：CATEGORY_DEFAULT。
     *Extras：Extras属性主要用于传递目标组件所需要的额外的数据。通过putExtras()方法设置。
     */
    //相册
    public void getPhotoAlbum() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");  //type  指定获取 image类型的所有文件
        startActivityForResult(intent, 2);
        //系统相册选图返回的Uri是可以直接使用的，不需要也不能使用FileProvider进行转换
    }

    //照相功能
    /*思路
     * 首先是调用相机  意图
     * 第二步 获取图片路径
     * 最后保存并返回
     */
    private void getCamera() {
        File file = new FileStorage().createCropFile();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Log.d(TAG, "getCamera: 111111111111111111");
            uri = FileProvider.getUriForFile(this, "com.example.shenmiao.fileProvider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//临时授予读写权限
        } else {
            //低版本路径转成uri
            Log.d(TAG, "getCamera: 222222222222222222");
            uri = Uri.fromFile(file);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);  //将图片保存在这个位置
        startActivityForResult(intent, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    //权限申请成功
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        switch (type) {
            case 1:
                getCamera();
                break;
            case 2:
                getPhotoAlbum();
                break;
        }
    }

    //权限申请失败时的回调
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        showToast("请在应用管理里面对应用进行重新授权");
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            //照相
            case 1:
                //此处不可写成 data.getData
                // 因为上面通过putExtra  将地址存在了 uri  这个指定的路径里面
                Log.d(TAG, "onActivityResult: 11111111111111111"+uri);
                startPhotoZoom(uri);
                break;
            //相册
            case 2:
                if(data == null)
                    return;
                //从相册返回的uri  可以看在getPhotoAlbum处的注解
                startPhotoZoom(data.getData());
                // content                  media
                Log.i("文件地址", data.getScheme() + " " + data.getData().getAuthority() +
                        data.getData().getHost() + data.getData().getPort() + "  " + data.getData().getPath() + "\n" + data.getData());
                //content://media/external/images/media/1036430   path: /external/images/media/1036430
                break;
            //裁剪
            case 3:
                if(data == null)
                    return;
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    //裁剪处有对照表  键名为data   类型是 parcelable   value (因为写的是true )  bitmap
                    Bitmap bitmap = bundle.getParcelable("data");
                    String path = saveImage(bitmap);
                    loadCircleImage(this, path, mIcon);
                    MyDatabaseHelper dbHelper = new MyDatabaseHelper(this);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.execSQL("UPDATE Informationtable SET head='"+path +"'WHERE identifynumber='"+identifynumber+"'");
                }


                Log.i("Uri", cropUri.toString());
          /*      if (cropUri!=null){
                    Bitmap bitmap ;  当使用false时
                    try {   这种方法提示获取的地址不存在
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(cropUri));
                        loadCircleImage(this,bitmap,mIcon);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }*/
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //裁剪方法   注意路径是否相同
    private void startPhotoZoom(Uri uri) {
        File file = new FileStorage().createCropFile();
                /*   方法                               描述
        File(File dir, String name) File对象类型的目录路径，name为文件名或目录名。
        File(String path)   path为新File对象的路径。
        File(String dirPath, String name)   dirPath为指定的文件路径，name为文件名或目录名。
        File(URI uri)   使用URI指定路径来创建新的File对象。*/

        cropUri = Uri.fromFile(file);  //file 类型转成了 uri 类型 最后在下面使用   最终将裁剪后的图片在这个指定的位置

        //调用系统裁剪的意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
/*      crop	    String	   发送裁剪信号
        aspectX	    int	       X方向上的比例
        aspectY	    int	       Y方向上的比例
        outputX	    int	       裁剪区的宽
        outputY 	int	       裁剪区的高
        scale	    boolean	   是否保留比例
        return-data	boolean	   是否将数据保留在Bitmap中返回
        data	   Parcelable  相应的Bitmap数据
        circleCrop	String	   圆形裁剪区域？
        MediaStore.EXTRA_OUTPUT ("output")	URI	 将URI指向相应的file:///...，详见代码示例
        outputFormat	String	输出格式，一般设为Bitmap格式：Bitmap.CompressFormat.JPEG.toString()
        noFaceDetection	boolean	是否取消人脸识别功能*/
        intent.setDataAndType(uri, "image/*");  //设置data （uri） 和type  类型
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1); // 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300); // 输出图片大小
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true);
        //Intent 的data域最大传递的值的大小约为1M，所以图片的BITMAP当超过1M时就会失败 : 无法传递大图  false 传递uri
        intent.putExtra("return-data", true);  //true  表示返回的是bitmap对象  为true的情况下 一般在图片尺寸480*480 崩溃
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);  //将 图像转移保存到  -》 croupUri   ：   uri位置
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        Log.d("111111111111111cropUri:",cropUri.toString());
        startActivityForResult(intent, 3);
    }


    public void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 加载圆形图片
     */
    public static void loadCircleImage(Context context, String path, ImageView imageView) {
        // RequestOptions  扩展glide  自定义加载方式
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .circleCrop()//设置圆形
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(path).apply(options).into(imageView);
    }

    //获取图像的String类型path 地址   可以用于保存或者上传到服务器
    public String saveImage(Bitmap bmp) {
        File file = new FileStorage().createIconFile();
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);  //图片压缩
            fos.flush();
            fos.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent2 = new Intent("android.intent.action.CART_BROADCAST");
        intent2.putExtra("data","refresh");
        LocalBroadcastManager.getInstance(Head.this).sendBroadcast(intent2);
        sendBroadcast(intent2);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
