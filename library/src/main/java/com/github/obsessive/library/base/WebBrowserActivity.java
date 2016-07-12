package com.github.obsessive.library.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.github.obsessive.library.R;

import java.util.HashMap;
import java.util.Map;

/**
 * 展示网页内容
 *
 * @author ice_coffee
 */
public class WebBrowserActivity extends Activity
{

    /**
     * 标题
     */
    private String title;
    private String params;
    private String mUrlStr;

    private WebView mWebView;

    private Map<String, String> mAdditionalHttpHeaders;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_browser);

        init();
    }

    /**
     * 控件初始化
     */
    private void init()
    {
        mAdditionalHttpHeaders = new HashMap<>();

        Intent intent = getIntent();

        if (null != intent)
        {
            mUrlStr = intent.getStringExtra("url");
            title = intent.getStringExtra("title");
            params = intent.getStringExtra("params");
        }

        mWebView = (WebView) this.findViewById(R.id.wv_web);

        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        openUrl(mUrlStr);
    }

    @SuppressLint("JavascriptInterface")
    private void openUrl(String url)
    {
        WebSettings settings = mWebView.getSettings();

        //设置js调用
        JavaScriptInterface jsInterface = new JavaScriptInterface(this);
        mWebView.addJavascriptInterface(jsInterface, "JSInterface");

        settings.setDomStorageEnabled(true);
        settings.setLoadsImagesAutomatically(true);
        // 支持js
        settings.setJavaScriptEnabled(true);

        // 0为手机默认, 1为PC台机，2为IPHONE
        settings.setUserAgentString("0");
        settings.setDefaultTextEncodingName("utf-8");
        // 是否缩放
        settings.setSupportZoom(true);

        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        // 是否支持手势缩放
        settings.setBuiltInZoomControls(true);

        //设置当前链接跳转在当前browser中响应
        mWebView.setWebViewClient(new WebViewClient()
        {
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                //手机号不可点击
                if (url != null)
                {
                    if (url.length() > 3)
                    {
                        if (url.substring(0, 4).equals("tel:"))
                        {
                            return false;
                        }
                    }
                }

                view.loadUrl(url, mAdditionalHttpHeaders);
                return true;
            }

            //页面加载时会调用此方法, 但是在片段导航跳转时不会调用此方法
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                super.onPageStarted(view, url, favicon);
                if (!TextUtils.isEmpty(url))
                {
                    String[] strs = url.split("\\?");

                    if ("http://apiv3.bajiebao.com/bjbRechCash/callback_jd".equals(strs[0]))
                    {
                        setResult(1);
                        finish();
                    }
                }
            }
        });

        /**
         * 主要处理解析，渲染网页等浏览器做的事情
         * WebChromeClient是辅助WebView处理Javascript的对话框，网站图标，网站title，加载进度等
         */
        mWebView.setWebChromeClient(new WebChromeClient()
        {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result)
            {
                result.confirm();
                return super.onJsAlert(view, url, message, result);
            }
        });

        if (TextUtils.isEmpty(params))
        {
            //加载url地址
            mWebView.loadUrl(mUrlStr, mAdditionalHttpHeaders);
        }
        else
        {
            //加载本地html文件
            mWebView.loadData(params, "text/html", "UTF-8");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            if (TextUtils.isEmpty(params))
            {
                mWebView.loadUrl(mUrlStr, mAdditionalHttpHeaders);
            }
            else
            {
                mWebView.loadData(params, "text/html", "UTF-8");
            }

        }
    }

    public class JavaScriptInterface
    {
        Context mContext;

        public JavaScriptInterface(Context context)
        {
            mContext = context;
        }

        @JavascriptInterface
        public void startVideo(String videoAddress)
        {
            // 调用播放器(这里看你自己怎么写了)
            Intent it = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.parse(videoAddress);
            it.setDataAndType(uri, "video/*");
            startActivity(it);
        }
    }

    @Override
    public void onBackPressed()
    {
        boolean isBack = false;
        if (mWebView.canGoBack())
        {
            mWebView.goBack();
            isBack = true;
        }
        if (!isBack)
        {
            super.onBackPressed();
        }
    }

    /**
     * 两次返回键退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            new AlertDialog.Builder(WebBrowserActivity.this)
                    .setMessage("确定要返回")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            finish();
                        }
                    }).setNegativeButton("取消", null).show();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (mWebView != null)
        {
            LinearLayout parentView = (LinearLayout) findViewById(R.id.ll_web_browser);
            parentView.removeView(mWebView);
            mWebView.stopLoading();
            mWebView.removeAllViews();
            mWebView.destroy();
        }
    }

}
