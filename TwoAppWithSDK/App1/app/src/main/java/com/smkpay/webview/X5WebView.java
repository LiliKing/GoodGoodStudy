package com.smkpay.webview;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.xianglijin.app1.R;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient.CustomViewCallback;
import com.tencent.smtt.export.external.interfaces.JsPromptResult;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class X5WebView extends WebView {

    // /////////////////////////////////////////////////
    // add private object

    // ////////////////////////////////////////////////
    // file chooser result code
    public static final int FILE_CHOOSER = 0;
    private String resourceUrl = "";
    private WebView smallWebView;
    private static boolean isSmallWebViewDisplayed = false;
    private boolean isClampedY = false; // 是否下拉状态？
    private Map<String, Object> mJsBridges;
    //	private TextView tog;
//	RelativeLayout.LayoutParams layoutParams;
//	private RelativeLayout refreshRela;
    TextView title;
    ProgressBar progressBar;
    private Context mContext;
    public ValueCallback<Uri> mUploadMessage;// 表单的数据信息
    public ValueCallback<Uri[]> mUploadCallbackAboveL;
    public final static int FILECHOOSER_RESULTCODE = 1;// 表单的结果回调
    public Uri imageUri;
    private LoadShareView mLoadShareView;
    private LoadUrlOperation mLoadUrlOperation;
    private Activity mWebActivity;
    private String mCurrentUrl = "";
    private HashMap<String, String> mUrlTitles = new HashMap<>();

    public interface LoadShareView {
        void jsToLoadShare(String shareText);
    }

    public void setLoadShareInterface(LoadShareView loadShareView) {
        mLoadShareView = loadShareView;
    }

    public interface LoadUrlOperation {
        void jsToOperation();
    }

    public void setLoadUrlOperation(LoadUrlOperation loadUrlOperation) {
        mLoadUrlOperation = loadUrlOperation;
    }

    public void setWebActivity(Activity activity) {
        mWebActivity = activity;
    }

    public X5WebView(Context arg0) {
        super(arg0);
        mContext = arg0;
        setBackgroundColor(85621);
    }


    @SuppressLint("SetJavaScriptEnabled")
    public X5WebView(Context arg0, AttributeSet arg1, boolean useCache) {
        super(arg0, arg1);
        mContext = arg0;
        progressBar = (ProgressBar) ((Activity) getContext()).findViewById(R.id.pb_process);

        WebSettings webSetting = this.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setDefaultTextEncodingName("utf-8");//设置字符编码
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(false);
        webSetting.setBuiltInZoomControls(false);
        webSetting.setUseWideViewPort(false);
        webSetting.setSupportMultipleWindows(false);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setBlockNetworkImage(false);
        // 设置Application Caches缓存
        webSetting.setAppCacheEnabled(useCache);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        // 开启database storage API 功能
        webSetting.setDatabaseEnabled(true);
        // 开启DOM storage API 功能
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        // webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSetting.setPluginState(WebSettings.PluginState.ON);
        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // 加载url的缓存模式
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSetting.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        this.getView().setClickable(true);
        this.getView().setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        this.addJavascriptInterface(new SMKJavascriptInterface(), "smknative");
        // 加载后，滑动页面到底/顶后仍能继续滑动，页面加载显示异常
//        this.setWebViewClientExtension(new X5WebViewEventHandler(this));// 配置X5webview的事件处理
        // WebClient settings
        this.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
//                 mCurrentUrl = url;
            }

            @Override
            public void onPageFinished(WebView webView, String url) {
                super.onPageFinished(webView, url);
                // 加载显示网页标题 mUrlTitles.get(url)

            }

            /**
             * 防止加载网页时调起系统浏览器
             */
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                return true;
                // return false;
            }

            public void onReceivedHttpAuthRequest(
                    WebView webview,
                    com.tencent.smtt.export.external.interfaces.HttpAuthHandler httpAuthHandlerhost,
                    String host, String realm) {
                boolean flag = httpAuthHandlerhost
                        .useHttpAuthUsernamePassword();
                Log.i("X5WebView", "useHttpAuthUsernamePassword is" + flag);
                Log.i("X5WebView", "HttpAuth host is" + host);
                Log.i("X5WebView", "HttpAuth realm is" + realm);

            }

            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                // super.onReceivedSslError(webView, sslErrorHandler, sslError);
                // Ignore SSL certificate errors
                sslErrorHandler.proceed(); // 接受证书
                // handler.cancel(); // 默认的处理方式，WebView变成空白页
                // handleMessage(Message msg); // 其他处理
            }

            @Override
            public void onDetectedBlankScreen(String arg0, int arg1) {
                super.onDetectedBlankScreen(arg0, arg1);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView arg0,
                                                              String arg1) {
                return super.shouldInterceptRequest(arg0, arg1);
            }

        });

        // webchromeclient settings
        this.setWebChromeClient(new WebChromeClient() {
            View myVideoView;
            View myNormalView;
            CustomViewCallback callback;

            /**
             * 全屏播放配置
             */
            @Override
            public void onShowCustomView(View view,
                                         CustomViewCallback customViewCallback) {
                FrameLayout normalView = (FrameLayout) ((Activity) getContext())
                        .findViewById(R.id.wv_x5);
                ViewGroup viewGroup = (ViewGroup) normalView.getParent();
                viewGroup.removeView(normalView);
                viewGroup.addView(view);
                myVideoView = view;
                myNormalView = normalView;
                callback = customViewCallback;
            }

            @Override
            public void onHideCustomView() {
                if (callback != null) {
                    callback.onCustomViewHidden();
                    callback = null;
                }
                if (myVideoView != null) {
                    ViewGroup viewGroup = (ViewGroup) myVideoView.getParent();
                    viewGroup.removeView(myVideoView);
                    viewGroup.addView(myNormalView);
                }
            }

            @Override
            public void onProgressChanged(WebView webview, int progress) {
                if (progressBar != null) {
                    setProgress(progress);
                } else {
                    super.onProgressChanged(webview, progress);
                }
            }

            @Override
            public void openFileChooser(ValueCallback<Uri> uploadFile,
                                        String acceptType, String captureType) {
//                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//                i.addCategory(Intent.CATEGORY_OPENABLE);
//                i.setType("*/*");
//                ((Activity) (X5WebView.this.getContext()))
//                        .startActivityForResult(
//                                Intent.createChooser(i, "choose files"),
//                                X5WebView.FILE_CHOOSER);
//                super.openFileChooser(uploadFile, acceptType, captureType);
                mUploadMessage = uploadFile;
                take();
            }

            @Override
            public void onShowCustomView(View arg0, int arg1,
                                         CustomViewCallback arg2) {
                CustomViewCallback callback = new CustomViewCallback() {

                    @Override
                    public void onCustomViewHidden() {
                        Log.i("X5WebView", "video view hidden");
                    }
                };
                super.onShowCustomView(arg0, arg1, arg2);
            }

            /**
             * webview 的窗口转移
             */
            @Override
            public boolean onCreateWindow(WebView arg0, boolean arg1,
                                          boolean arg2, Message msg) {
                if (X5WebView.isSmallWebViewDisplayed == true) {

                    WebViewTransport webViewTransport = (WebViewTransport) msg.obj;
                    WebView webView = new WebView(X5WebView.this.getContext()) {

                        protected void onDraw(Canvas canvas) {
                            super.onDraw(canvas);
                            Paint paint = new Paint();
                            paint.setColor(Color.GREEN);
                            paint.setTextSize(15);
                            canvas.drawText("新建窗口", 10, 10, paint);
                        }

                    };
                    webView.setWebViewClient(new WebViewClient() {
                        public boolean shouldOverrideUrlLoading(WebView arg0,
                                                                String arg1) {
                            arg0.loadUrl(arg1);
                            return true;
                        }

                        ;
                    });
                    FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(400, 600);
                    lp.gravity = Gravity.CENTER_HORIZONTAL
                            | Gravity.CENTER_VERTICAL;
                    X5WebView.this.addView(webView, lp);

                    webViewTransport.setWebView(webView);
                    msg.sendToTarget();
                }
                return true;
            }

            @Override
            public boolean onJsAlert(WebView webView, String url, String message,
                                     JsResult jsResult) {
//                Builder builder = new Builder(getContext());
//                builder.setTitle("X5内核");
//                builder.setPositiveButton("确定",
//                        new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog,
//                                                int which) {
//                                dialog.dismiss();
//                            }
//                        });
//                builder.show();
                jsResult.confirm();
                // return true;
//                Log.i("X5WebView", "setX5webview = null");
                return super.onJsAlert(webView, url, message, jsResult);

            }

            /**
             * 对应js 的通知弹框 ，可以用来实现js 和 android之间的通信
             */
            @Override
            public boolean onJsPrompt(WebView arg0, String arg1, String arg2,
                                      String arg3, JsPromptResult arg4) {
                // 在这里可以判定js传过来的数据，用于调起android native 方法
                if (X5WebView.this.isMsgPrompt(arg1)) {
                    if (X5WebView.this.onJsPrompt(arg2, arg3)) {
                        return true;
                    } else {
                        return false;
                    }
                }
                return super.onJsPrompt(arg0, arg1, arg2, arg3, arg4);
            }

            @Override
            public void onReceivedTitle(WebView arg0, String title) {
                super.onReceivedTitle(arg0, title);
//                if (StringUtil.isBlank(title)){
//                    title = "";
//                }
//                mUrlTitles.put(mCurrentUrl, title + "");
            }
        });
    }


    /*
        // 绘制webview的标记，用于判断正在使用的是系统WebView还是X5WebView
        @Override
        protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
            boolean ret = super.drawChild(canvas, child, drawingTime);
            canvas.save();
            Paint paint = new Paint();
            paint.setColor(0x7fff0000);
            paint.setTextSize(24.f);
            paint.setAntiAlias(true);
            if (getX5WebViewExtension() != null) {
                // Log.d(TAG, "drawChild--X5 Core");
                canvas.drawText(this.getContext().getPackageName() + "-pid:"
                        + android.os.Process.myPid(), 10, 50, paint);
                canvas.drawText(
                        "X5  Core:" + QbSdk.getTbsVersion(this.getContext()), 10,
                        100, paint);
            } else {
                // Log.d(TAG, "drawChild--Sys Core");
                canvas.drawText(this.getContext().getPackageName() + "-pid:"
                        + android.os.Process.myPid(), 10, 50, paint);
                canvas.drawText("Sys Core", 10, 100, paint);
            }
            canvas.drawText(Build.MANUFACTURER, 10, 150, paint);
            canvas.drawText(Build.MODEL, 10, 200, paint);
            canvas.restore();
            return ret;
        }
    */

    public interface OnNotHaveAppMarket {
        void notHaveAppMarket();
    }

    private OnNotHaveAppMarket mNotHaveAppMarket;

    public void setNotHaveAppMarket(OnNotHaveAppMarket notHaveAppMarket) {
        this.mNotHaveAppMarket = notHaveAppMarket;
    }

    public final class SMKJavascriptInterface {
        @JavascriptInterface
        public void openLoginView() {

        }

        @JavascriptInterface
        public void openAppstore() {

        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
        if (requestCode != FILECHOOSER_RESULTCODE || mUploadCallbackAboveL == null) {
            return;
        }

        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                results = new Uri[]{imageUri};
            } else {
                String dataString = data.getDataString();
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        if (results != null) {
            mUploadCallbackAboveL.onReceiveValue(results);
            mUploadCallbackAboveL = null;
        } else {
            results = new Uri[]{imageUri};
            mUploadCallbackAboveL.onReceiveValue(results);
            mUploadCallbackAboveL = null;
        }
        return;
    }

    private void take() {
        File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyApp");
        if (!imageStorageDir.exists()) {
            imageStorageDir.mkdirs();
        }
        File file = new File(imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        imageUri = Uri.fromFile(file);
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = ((Activity) mContext).getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent i = new Intent(captureIntent);
            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            i.setPackage(packageName);
            i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            cameraIntents.add(i);
        }
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        Intent chooserIntent = Intent.createChooser(i, "Image Chooser");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));
        ((Activity) mContext).startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    public static void setSmallWebViewEnabled(boolean enabled) {
        isSmallWebViewDisplayed = enabled;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        // Log.i("X5WebView","webview scroll y is" + this.getView().getScrollY());
        // Log.i("X5WebView","real webview scroll y is" + this.getScrollY());
        // Log.i("X5WebView", "webview webscroll y is" + this.getWebScrollY());
        super.onScrollChanged(l, t, oldl, oldt);
    }


    /**
     * 当webchromeClient收到 web的prompt请求后进行拦截判断，用于调起本地android方法
     *
     * @param methodName 方法名称
     * @param blockName  区块名称
     * @return true ：调用成功 ； false ：调用失败
     */
    private boolean onJsPrompt(String methodName, String blockName) {
        String tag = SecurityJsBridgeBundle.BLOCK + blockName + "-"
                + SecurityJsBridgeBundle.METHOD + methodName;

        if (this.mJsBridges != null && this.mJsBridges.containsKey(tag)) {
            ((SecurityJsBridgeBundle) this.mJsBridges.get(tag)).onCallMethod();
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判定当前的prompt消息是否为用于调用native方法的消息
     *
     * @param msg 消息名称
     * @return true 属于prompt消息方法的调用
     */
    private boolean isMsgPrompt(String msg) {
        if (msg != null
                && msg.startsWith(SecurityJsBridgeBundle.PROMPT_START_OFFSET)) {
            return true;
        } else {
            return false;
        }
    }

    // TBS: Do not use @Override to avoid false calls
    public boolean tbs_dispatchTouchEvent(MotionEvent ev, View view) {
        boolean r = super.super_dispatchTouchEvent(ev);
        Log.d("X5WebView", "dispatchTouchEvent " + ev.getAction() + " "
                + r);
        // MotionEvent.ACTION_DOWN 0
        // up 1
        // move 2

        return r;
    }

    // TBS: Do not use @Override to avoid false calls
    public boolean tbs_onInterceptTouchEvent(MotionEvent ev, View view) {
        boolean r = super.super_onInterceptTouchEvent(ev);
        return r;
    }

    protected void tbs_onScrollChanged(int l, int t, int oldl, int oldt,
                                       View view) {
        Log.i("X5WebView", "tbs_onScrollChanged ");
        super_onScrollChanged(l, t, oldl, oldt);
    }

    protected void tbs_onOverScrolled(int scrollX, int scrollY,
                                      boolean clampedX, boolean clampedY, View view) {
        Log.i("X5WebView", "scrollY is " + scrollY);
        /*if (this.tog == null) {
            this.tog = (TextView) ((Activity) getContext())
					.findViewById(R.id.refreshText);
			layoutParams = (RelativeLayout.LayoutParams) (this.tog
					.getLayoutParams());
			this.refreshRela = (RelativeLayout) ((Activity) getContext())
					.findViewById(R.id.refreshPool);
		}*/
        if (isClampedY && !clampedY) {
            this.reload();
        }
        if (clampedY) {
            this.isClampedY = true;

        } else {
            this.isClampedY = false;
            // layoutParams.setMargins((int)tog.getX(),0,
            // (int)tog.getX()+tog.getWidth(), tog.getHeight());
        }
        super_onOverScrolled(scrollX, scrollY, clampedX, clampedY);
    }

    protected void tbs_computeScroll(View view) {
        super_computeScroll();
    }

    protected boolean tbs_overScrollBy(int deltaX, int deltaY, int scrollX,
                                       int scrollY, int scrollRangeX, int scrollRangeY,
                                       int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent,
                                       View view) {
        Log.i("X5WebView", "tbs_overScrollBy deltaY is" + deltaY);
        if (this.isClampedY) {
            /*if ((refreshRela.getTop() + (-deltaY)) / 2 < 255) {
                this.tog.setAlpha((refreshRela.getTop() + (-deltaY)) / 2);
			} else {
				this.tog.setAlpha(255);
			}
			this.refreshRela.layout(refreshRela.getLeft(), refreshRela.getTop()
					+ (-deltaY), refreshRela.getRight(),
					refreshRela.getBottom() + (-deltaY));*/
            this.layout(this.getLeft(), this.getTop() + (-deltaY) / 2,
                    this.getRight(), this.getBottom() + (-deltaY) / 2);
        }
        return super_overScrollBy(deltaX, deltaY, scrollX, scrollY,
                scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY,
                isTouchEvent);
    }

    public void setTitle(TextView title) {
        this.title = title;
    }

    protected boolean tbs_onTouchEvent(MotionEvent event, View view) {
        if (event.getAction() == MotionEvent.ACTION_UP /*&& this.tog != null*/) {
            this.isClampedY = false;
            /*this.tog.setAlpha(0);
            this.refreshRela.layout(refreshRela.getLeft(), 0,
					refreshRela.getRight(), refreshRela.getBottom());*/
            this.layout(this.getLeft(), 0, this.getRight(), this.getBottom());
        }
        return super_onTouchEvent(event);
    }

    private void setProgress(int progress) {
        if (progressBar != null) {
            if (progress == 100) {
                progressBar.setVisibility(View.GONE);
            } else {
                progressBar.setProgress(progress);
                if (progressBar.getVisibility() != View.VISIBLE) {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        }
    }

}
