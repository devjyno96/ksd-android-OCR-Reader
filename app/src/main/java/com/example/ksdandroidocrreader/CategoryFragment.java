package com.example.ksdandroidocrreader;

import android.os.Bundle;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class CategoryFragment extends Fragment {

    private RecyclerView categoryView = null;

    private static int[] arr_R_id = {
            R.drawable.aaaa,
            R.drawable.aassqw,
            R.drawable.abnsdv,
            R.drawable.awdcvs,
            R.drawable.bvcv,
            R.drawable.zxcvasd,
            R.drawable.qadbsefd,
            R.drawable.qbmhfsf,
            R.drawable.qmiff,
            R.drawable.qmmmmm,
            R.drawable.qmpppp
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        categoryView = view.findViewById(R.id.categoryfragment_recyclerview);
        categoryView.setHasFixedSize(true);
        categoryView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        categoryView.setAdapter(new CategoryFragmentRecyclerViewAdapter(this, arr_R_id));

        return view;
    }

}

class CategoryFragmentRecyclerViewAdapter extends RecyclerView.Adapter<CategoryFragmentRecyclerViewAdapter.ViewHolder> {

    private int[] localDataSet;
    CategoryFragment fragment;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        //private final TextView textView;

        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view;
            //textView = (TextView) view.findViewById(R.id.categoryfragment_txt);
        }

        public ImageView getImageView() {
            return imageView;
        }
    }

    public CategoryFragmentRecyclerViewAdapter(CategoryFragment fragment, int[] dataSet) {
        this.fragment = fragment;
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        int width = fragment.getResources().getDisplayMetrics().widthPixels / 2;

        ImageView imageView = new ImageView(viewGroup.getContext());
        imageView.setLayoutParams(new LinearLayoutCompat.LayoutParams(width, width));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return new ViewHolder(imageView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        ImageView imageView = viewHolder.imageView;
        viewHolder.getImageView().setImageResource(localDataSet[position]);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.length;
    }

}
