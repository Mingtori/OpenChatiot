package com.min.openchatiot;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thstj on 2017-10-26.
 */

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TEXT_ME = 0;
    private final int TEXT_YOU = 1;

    private List<Message> messageList;
    private Context context;

    MessageAdapter(Context context) {
        this.context = context;
        messageList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //view type에 따라 다른 itemview 로드( 본인 혹은 상대방에 따라 다른 레이아웃이 적용되기 때문
        if(viewType == TEXT_ME) {
            return new MessageViewHolder.TextItemHolder(LayoutInflater.from(context).inflate(R.layout.item_text_me, parent, false));
        }
        else  {
            return new MessageViewHolder.TextItemHolder(LayoutInflater.from(context).inflate(R.layout.item_text_you, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //실제 데이터를 뷰에 표시
        if(getItemViewType(position) == TEXT_ME || getItemViewType(position) == TEXT_YOU) {
            MessageViewHolder.TextItemHolder itemHolder = (MessageViewHolder.TextItemHolder) holder;
            itemHolder.textViewId.setText(messageList.get(position).getId());
            itemHolder.textViewMessage.setText(messageList.get(position).getText());
            itemHolder.textViewTime.setText(messageList.get(position).getTime());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);

        /*
        getItemViewType의 경우 ItemView 타입을 사용자 임의로 변경할 수 있음.
        (layout내의 item_text_me, item_text_you 등의 파일을 말합니다)
         */
        if(message.getWho().equals("me")) {
            return TEXT_ME;
        }
        else {
            return TEXT_YOU;
        }
    }

    public void addMessage(Message message) {
        messageList.add(message);
        notifyItemInserted(messageList.size());
    }
}
