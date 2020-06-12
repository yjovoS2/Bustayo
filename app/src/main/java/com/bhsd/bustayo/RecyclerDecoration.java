package com.bhsd.bustayo;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerDecoration extends RecyclerView.ItemDecoration {
    //리사이클러뷰 사이 구분선 및 간격 - test용
    private final int divHeight;

    public RecyclerDecoration(int divHeight){
        this.divHeight = divHeight;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        if(parent.getChildAdapterPosition(view)!=parent.getAdapter().getItemCount() -1)
            outRect.bottom = divHeight;
    }

    //fragment에
    // RecyvlerDecoration spaceDecoration = new RecyclerDecoration(20);
    // 라사이클러뷰.addItemDecoration(spaceDecoration);
}
