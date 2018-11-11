package in.jivanmuktas.www.marg.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import in.jivanmuktas.www.marg.R;
import in.jivanmuktas.www.marg.model.IasCoaching;

public class IASCoachingAdapter extends RecyclerView.Adapter<IASCoachingAdapter.MyViewHolder> {

    ArrayList<IasCoaching> arrayList = new ArrayList<>();
    Context context;

    public IASCoachingAdapter(ArrayList<IasCoaching> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ias_card_view,parent,false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        IasCoaching iasCoaching = arrayList.get(position);
        holder.date.setText(iasCoaching.getCoaching_date());
        holder.subject.setText(iasCoaching.getSubject_name());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView date,subject;
        public MyViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.date);
            subject = (TextView) itemView.findViewById(R.id.subject);
        }
    }
}
