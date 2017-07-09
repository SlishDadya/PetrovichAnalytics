package entartaiment.goshan.petrovichanalytics;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
public static final String TAG="goshan.entertaiment.MainActivity";

private RecyclerView mRecyclerView;
private ShareAdapter mAdapter;
private boolean isThread=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView=(RecyclerView) findViewById(R.id.list_view_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ShareService.setServiceAlarm(this,true);
        updateUI();
    }

    private void updateUI() {
        if(mAdapter==null) {
            mAdapter = new ShareAdapter(ShareArray.getShareArray(getApplicationContext()).getArray());
            mRecyclerView.setAdapter(mAdapter);
        }
        else
        {   mAdapter.mShares=ShareArray.getShareArray(getApplicationContext()).getArray();
            mAdapter.notifyDataSetChanged();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
        isThread=true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.icon_add:
                Intent intent = new Intent(this, ShareActivity.class);
                intent.putExtra(TAG, UUID.randomUUID());
                intent.putExtra("add", true);
                startActivity(intent);
                return true;

            case R.id.icon_settings:
                Intent intent2 = new Intent(this, SettingsActivity.class);
                startActivity(intent2);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isThread = false;
    }
    private class ShareHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public Share mShare;
        public TextView mNameView;
        public TextView mPriceView;
        public ImageView mImageView;
        public TextView mProcentView;
        public TextView mZalupa;
        public ShareHolder(View v)
        {
            super(v);
            mNameView = (TextView) v.findViewById(R.id.name_text_view);
            mPriceView=(TextView) v.findViewById(R.id.current_price);
            mImageView=(ImageView) v.findViewById(R.id.image_view_list_item);
            mProcentView=(TextView) v.findViewById(R.id.text_view_action_price);
            mZalupa=(TextView) v.findViewById(R.id.zalupa);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent=new Intent(MainActivity.this,ShareActivity.class);
            intent.putExtra(TAG,mShare.getUUID());
            intent.putExtra("add",false);
            startActivity(intent);
        }
        public void startThread()
        {
            Thread thread=new Thread(new Runnable() {
                @Override
                public void run() {
                    while(true) {
                        while (isThread) {
                            ShareTask task = new ShareTask();
                            task.execute();
                            try {
                                Thread.sleep(6 * 1000);
                            } catch (InterruptedException x) {
                            }

                        }
                    }
                }
            });
            thread.start();
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
                    if (price == 0){
                        mPriceView.setText("Not found");
                        mProcentView.setText(null);
                        mImageView.setImageDrawable(null);
                        mZalupa.setText(null);

                    }
                    else {
                        mPriceView.setText(String.valueOf(price));
                        if (procent > 0) mImageView.setImageDrawable(getDrawable(R.drawable.rsz_up));
                        else mImageView.setImageDrawable(getDrawable(R.drawable.rsz_down));
                        mProcentView.setText(String.valueOf(procent));
                        mZalupa.setText("You gain: "+ String.format("%.2f",mShare.getNum()*(price-mShare.getPrice()))+" dollars");
                    }
                }
                else{

                    mPriceView.setText("Not found");
                    mProcentView.setText(null);
                    mImageView.setImageDrawable(null);
                    mZalupa.setText(null);

                }

            }
        }


    }
    private class ShareAdapter extends RecyclerView.Adapter<ShareHolder>
    {
        public ArrayList<Share> mShares;
        public ShareAdapter(ArrayList<Share> shares)
        {
            mShares=shares;
        }
        @Override
        public ShareHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater=LayoutInflater.from(MainActivity.this);
            View v=inflater.inflate(R.layout.item_list,parent,false);

            return new ShareHolder(v);
        }

        @Override
        public int getItemCount() {
            return mShares.size();
        }

        @Override
        public void onBindViewHolder(ShareHolder holder, int position) {
            Share share=mShares.get(position);
            holder.mShare=share;
            if(share.getName()!=null) {
                holder.mNameView.setText(share.getName().toUpperCase() + "(" + share.getNum() + ")");
            }
           // holder.mPriceView.setText(String.valueOf(share.getPrice())+"("+share.getNum()+")");
            holder.startThread();

        }

    }
}
