package in.jivanmuktas.www.marg.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.json.JSONArray;
import in.jivanmuktas.www.marg.R;

public class NodataFoundRecycler extends RecyclerView.Adapter<NodataFoundRecycler.ViewHolder> {
    // Declare Variables
    Context context;
    JSONArray responseArray;

    public NodataFoundRecycler(Context context) {
        this.context = context;
        this.responseArray = responseArray;
    }

    @Override
    public NodataFoundRecycler.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.nodatafound, parent,false);
        return new NodataFoundRecycler.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final NodataFoundRecycler.ViewHolder holder, int position) {
    }
    @Override
    public int getItemCount() {
        return responseArray.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);

        }
    }

}
