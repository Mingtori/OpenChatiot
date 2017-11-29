package com.min.openchatiot;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by thstj on 2017-10-26.
 */

public interface MessageViewHolder {

    //text관련 view 클래스
    class TextItemHolder extends RecyclerView.ViewHolder{

        public TextView textViewId;
        public TextView textViewMessage;
        public TextView textViewTime;

        public TextItemHolder(View itemView) {
            super(itemView);

            textViewId = itemView.findViewById(R.id.textView_id);
            textViewMessage = itemView.findViewById(R.id.textView_message);
            textViewTime = itemView.findViewById(R.id.textView_time);
        }
    }
}
