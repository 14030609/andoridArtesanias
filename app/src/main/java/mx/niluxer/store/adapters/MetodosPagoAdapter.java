package mx.niluxer.store.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mx.niluxer.store.R;
import mx.niluxer.store.data.model.MetodosPago;

public class MetodosPagoAdapter extends RecyclerView.Adapter<MetodosPagoAdapter.ViewHolder> {

    private List<MetodosPago> mItems;
    private Context mContext;
    private MetodosPagoItemListener mItemListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        public TextView tvidMetodosPago, tvnombre,tvdescripcion;
        MetodosPagoItemListener mItemListener;

        public ViewHolder(View itemView, MetodosPagoItemListener postItemListener) {
            super(itemView);
            tvidMetodosPago  = (TextView) itemView.findViewById(R.id.tvidMetodosPago);
            tvnombre = (TextView) itemView.findViewById(R.id.tvnombre);
            tvdescripcion = (TextView) itemView.findViewById(R.id.tvdescripcion);

            this.mItemListener = postItemListener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            MetodosPago item = getItem(getAdapterPosition());
            this.mItemListener.onMetodosPagoClick(item.getIdMetodosPago());

            notifyDataSetChanged();
        }

        @Override
        public boolean onLongClick(View v) {
            MetodosPago item = getItem(getAdapterPosition());
            this.mItemListener.onMetodosPagoLongClick(item);

            //notifyDataSetChanged();
            return true;
        }
    }

    public MetodosPagoAdapter(Context context, List<MetodosPago> posts, MetodosPagoItemListener itemListener) {
        mItems = posts;
        mContext = context;
        mItemListener = itemListener;
    }

    @Override
    public MetodosPagoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View MetodosPagoView = inflater.inflate(R.layout.metodospago_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(MetodosPagoView, this.mItemListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MetodosPagoAdapter.ViewHolder holder, int position) {

        MetodosPago item = mItems.get(position);
        TextView textView1 = holder.tvidMetodosPago;
        TextView textView2 = holder.tvnombre;
        TextView textView3 = holder.tvdescripcion;
        textView1.setText(item.getIdMetodosPago());
        textView2.setText(item.getNombre());
        textView3.setText(item.getDescripcion());


        /*OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();
        Picasso picasso = new Picasso.Builder(mContext)
                .downloader(new OkHttp3Downloader(okHttpClient))
                .build();
        picasso.get().load("http://192.168.1.66/~niluxer/wordpress/wp-content/uploads/2018/05/long-sleeve-tee-2.jpg").into(holder.ivMetodosPagoImage, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void updateMetodosPagos(List<MetodosPago> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    private MetodosPago getItem(int adapterPosition) {
        return mItems.get(adapterPosition);
    }

    public interface MetodosPagoItemListener {
        void onMetodosPagoClick(long id);
        void onMetodosPagoLongClick(MetodosPago MetodosPago);
    }
}
