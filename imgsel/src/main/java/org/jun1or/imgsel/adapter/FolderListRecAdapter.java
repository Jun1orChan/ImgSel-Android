package org.jun1or.imgsel.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.jun1or.imgsel.ISNav;
import com.istrong.imgsel.R;
import org.jun1or.imgsel.bean.Folder;

import java.util.ArrayList;
import java.util.List;

public class FolderListRecAdapter extends RecyclerView.Adapter<FolderListRecAdapter.FolderViewHolder> {


    private int mCurSelectedIndex = 0;


    private List<Folder> mFolderList = new ArrayList<>();

    private OnFolderItemClickListener mOnFolderItemClickListener;

    public void setFolderList(List<Folder> folderList) {
        this.mFolderList = folderList == null ? new ArrayList<Folder>() : folderList;
        notifyDataSetChanged();
    }

    public void setOnFolderItemClickListener(OnFolderItemClickListener onFolderItemClickListener) {
        this.mOnFolderItemClickListener = onFolderItemClickListener;
    }

    @Override
    public FolderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FolderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.imgsel_item_folderlist, parent, false));
    }

    @Override
    public void onBindViewHolder(FolderViewHolder holder, final int position) {
        final Folder folder = mFolderList.get(position);
        ISNav.getInstance().displayImage(holder.itemView.getContext(), folder.cover.path, holder.imgThumb);
        holder.tvFolderName.setText(folder.name);
        holder.tvImageSize.setText(folder.images.size() + "张");
        if (position == mCurSelectedIndex) {
            holder.imgCheck.setVisibility(View.VISIBLE);
        } else {
            holder.imgCheck.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemChanged(mCurSelectedIndex);
                mCurSelectedIndex = position;
                notifyItemChanged(mCurSelectedIndex);
                if (mOnFolderItemClickListener != null)
                    mOnFolderItemClickListener.onFolderItemClick(folder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFolderList.size();
    }

    class FolderViewHolder extends RecyclerView.ViewHolder {

        ImageView imgThumb;
        TextView tvFolderName;
        TextView tvImageSize;
        ImageView imgCheck;

        public FolderViewHolder(View itemView) {
            super(itemView);
            imgThumb = (ImageView) itemView.findViewById(R.id.imgThumb);
            tvFolderName = (TextView) itemView.findViewById(R.id.tvFolderName);
            imgCheck = (ImageView) itemView.findViewById(R.id.imgCheck);
            tvImageSize = (TextView) itemView.findViewById(R.id.tvImageSize);
        }
    }

    public interface OnFolderItemClickListener {
        void onFolderItemClick(Folder folder);
    }
}
