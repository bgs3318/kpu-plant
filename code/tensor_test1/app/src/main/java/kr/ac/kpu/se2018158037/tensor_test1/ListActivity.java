package kr.ac.kpu.se2018158037.tensor_test1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by Administrator on 2017-08-07.
 */

public class ListActivity extends BaseAdapter {

    private Context context;
    private List<User> userlist;
    private LayoutInflater inflate;
    private ViewHolder viewHolder;


    public ListActivity(List<User> userlist, Context context){
        this.userlist = userlist;
        this.context = context;
        this.inflate = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return userlist.size();
    }

    @Override
    public Object getItem(int i) {

        return userlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v = View.inflate(context,R.layout.listview,null);

        TextView email =v.findViewById(R.id.demail);
        TextView fname = v.findViewById(R.id.pname);

        email.setText(userlist.get(position).getUEmail());
        fname.setText(userlist.get(position).getplant_name());
        return v;
    }

    class ViewHolder{
        public TextView label;
    }

}
