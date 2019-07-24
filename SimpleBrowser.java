import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class SimpleBrowser extends Activity {

    WebView web;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "URL 설정").setIcon(android.R.drawable.ic_search_category_default).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.add(0, 1, 0, "새로 고침").setIcon(null).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        menu.add(0, 2, 0, "뒤로").setIcon(null).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        menu.add(0, 3, 0, "앞으로").setIcon(null).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        menu.add(0, 4, 0, "종료").setIcon(null).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch(item.getItemId()){
                case 0:
                    inputUrl();
                    break;
                case 1:
                    web.reload();
                    break;
                case 2:
                    if(web.canGoBack()) web.goBack();
                    break;
                case 3:
                    if(web.canGoForward()) web.goForward();
                    break;
                case 4:
                    finish();
                    break;
            }
        } catch (Exception e) {
            toast(e.toString());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try{
            getActionBar().setTitle("Simple Browser");

            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(1);

            web = new WebView(this);
            web.getSettings().setJavaScriptEnabled(true);
            web.setWebChromeClient(new WebChromeClient());
            web.setWebViewClient(new WebViewClient());
            String url = getIntent().getStringExtra("url");
            if(url==null) web.loadUrl("https://m.naver.com");
            else web.loadUrl(url);
            layout.addView(web);

            setContentView(layout);
        }
        catch(Exception e) {
            toast(e.toString());
        }
    }

    public void inputUrl() {
        try {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(1);
            TextView txt1 = new TextView(this);
            txt1.setText("URL : ");
            txt1.setTextSize(18);
            txt1.setTextColor(Color.BLACK);
            layout.addView(txt1);
            final EditText txt2 = new EditText(this);
            txt2.setHint("URL을 입력하세요...");
            txt2.setText(web.getUrl());
            txt2.setSingleLine(true);
            layout.addView(txt2);
            int pad = dip2px(10);
            layout.setPadding(pad, pad, pad, pad);
            ScrollView scroll = new ScrollView(this);
            scroll.addView(layout);
            dialog.setView(scroll);
            dialog.setTitle("URL 입력");
            dialog.setNegativeButton("취소", null);
            dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int whichButton) {
                    String url = txt2.getText().toString();
                    if (url.equals("")) {
                        toast("URL이 입력되지 않았습니다.");
                        inputUrl();
                    } else {
                        web.loadUrl(url);
                    }
                }
            });
            dialog.show();
        } catch (Exception e) {
            toast(e.toString());
        }
    }

    @Override
    public void onBackPressed(){
        if (web.canGoBack()) web.goBack();
        else finish();
    }

    private int dip2px(int dips){
        return (int)Math.ceil(dips*this.getResources().getDisplayMetrics().density);
    }

    public void toast(final String msg) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }
}
