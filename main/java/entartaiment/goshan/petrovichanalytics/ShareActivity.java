package entartaiment.goshan.petrovichanalytics;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.UUID;

public class ShareActivity extends AppCompatActivity {
    private EditText mNameEditText;
    private EditText mPriceEditText;
    private EditText mNumberEditText;
    private TextView mPriceTextView;
    private ImageView mImageView;
    private TextView mProcentTextView;
    private Share mShare;
    private boolean isAdd;
    private Thread mThread;
    private boolean isThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        mNameEditText=(EditText) findViewById(R.id.name_edit_text);
        mPriceEditText=(EditText) findViewById(R.id.price_edit_text);
        mNumberEditText=(EditText) findViewById(R.id.number_edit_text);
        mPriceTextView=(TextView) findViewById(R.id.action_price_textview);
        mProcentTextView=(TextView) findViewById(R.id.action_percent_textview);
        mImageView=(ImageView) findViewById(R.id.action_image_imageview);

        mNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mShare.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mPriceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    mShare.setPrice(Double.parseDouble(s.toString()));
                }
                catch(NumberFormatException e){e.printStackTrace();}
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    mShare.setNum(Integer.parseInt(s.toString()));
                }
                catch(Exception e){}
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        UUID mUUID=(UUID) getIntent().getSerializableExtra(MainActivity.TAG);
        isAdd=(boolean) getIntent().getSerializableExtra("add");
        if(!isAdd) {
            mShare = ShareArray.getShareArray(this).getShare(mUUID);
            mNameEditText.setText(mShare.getName());
            mPriceEditText.setText(String.valueOf(mShare.getPrice()));
            mNumberEditText.setText(String.valueOf(mShare.getNum()));
        }
        else mShare=new Share(mUUID);
        isThread=true;
        mThread=new Thread(new Runnable() {
            @Override
            public void run() {
                while(isThread)
                {
                    ShareTask task=new ShareTask();
                    task.execute();
                    try {
                        Thread.sleep(1000 * 3);
                    }
                    catch(InterruptedException e){e.printStackTrace();}
                }
            }
        });
        mThread.start();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(mShare.getName()!=null) {
            if (!isAdd) {
                ShareArray.getShareArray(this).updateShare(mShare, mShare.getUUID());
            } else ShareArray.getShareArray(this).addShare(mShare);
        }
        isThread=false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.icon_delete:
                ShareArray.getShareArray(this).deleteShare(mShare);
                Intent intent=new Intent(this,MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private class ShareTask extends AsyncTask<Void,Void,Bundle>
    {
        @Override
        protected Bundle doInBackground(Void... params) {
            Bundle bundle=ShareFetchr.fetchItems(mShare.getName());
            return bundle;
        }

        @Override
        protected void onPostExecute(Bundle bundle) {
            if(bundle!=null) {
                double price = (double) bundle.getSerializable(ShareFetchr.TAG_PRICE);
                double procent = (double) bundle.getSerializable(ShareFetchr.TAG_PROCENT);
                if (price == 0) {
                    mPriceTextView.setText("Not found");
                    mProcentTextView.setText(null);
                    mImageView.setImageDrawable(null);
                }
                else {
                    mPriceTextView.setText(String.valueOf(price));
                    if (procent > 0) mImageView.setImageDrawable(getDrawable(R.drawable.up));
                    else mImageView.setImageDrawable(getDrawable(R.drawable.down));
                    mProcentTextView.setText(String.valueOf(procent));
                }
            }
            else{

                mPriceTextView.setText("Not found");
                mProcentTextView.setText(null);
                mImageView.setImageDrawable(null);
            }

        }
    }
}