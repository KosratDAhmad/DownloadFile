package com.kosratdahmad.downloadfiles;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.ArrayRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;

/**
 * Simple adapter example for custom items in the dialog
 */
class ButtonItemAdapter extends RecyclerView.Adapter<ButtonItemAdapter.ButtonVH> {

    public interface Callback {
        void onItemClicked(int index);

        void onButtonClicked(int index);
    }

    private final CharSequence[] mItems;
    private Callback mCallback;

    public ButtonItemAdapter(Context context, @ArrayRes int arrayResId) {
        this(context.getResources().getTextArray(arrayResId));
    }

    private ButtonItemAdapter(CharSequence[] items) {
        this.mItems = items;
    }

    public void setCallback(Callback mCallback) {
        this.mCallback = mCallback;
    }

    @Override
    public ButtonVH onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dialog_customlistitem, parent, false);
        return new ButtonVH(view, this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ButtonVH holder, int position) {
        holder.title.setText(mItems[position] + " (" + position + ")");
        holder.button.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mItems.length;
    }

    public static class ButtonVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        final RadioButton title;
        final ImageView button;
        final ProgressBar progress;
        final ButtonItemAdapter adapter;

        public ButtonVH(View itemView, ButtonItemAdapter adapter) {
            super(itemView);
            title = (RadioButton) itemView.findViewById(R.id.md_title);
            button = (ImageView) itemView.findViewById(R.id.md_button);
            progress = (ProgressBar) itemView.findViewById(R.id.progress);

            this.adapter = adapter;
            itemView.setOnClickListener(this);
            button.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (adapter.mCallback == null)
                return;
            if (view instanceof ImageView) {
                adapter.mCallback.onButtonClicked(getAdapterPosition());
            } else {
                adapter.mCallback.onItemClicked(getAdapterPosition());
            }
        }
    }
}
