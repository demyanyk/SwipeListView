package com.example.learncard.modifiedlistview;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by a.demaynyk (demaynik@ukr.net) on 11/1/2014.
 */
public abstract class StableArrayAdapter<T> extends BaseAdapter {
    private List<T> mList;
    private Map<T, Integer> mItemsIds;
    private int mUniqueNextId = 0;
    private ListView mListView;
    private final Object mLock = new Object();
    private final View.OnTouchListener mOnTouchListener = new MyOnTouchListener();

    public StableArrayAdapter(ListView listView, List<T> mList) {
        this.mList = mList;
        this.mListView = listView;
        init();
    }

    private void init() {
        mItemsIds = new HashMap<T, Integer>(Math.round(mList.size() * 1.5f));
        for (T type : mList) {
            mItemsIds.put(type, getUniqueId());
        }
    }

    public void remove(int position) {
        synchronized (mLock) {
            T t = mList.get(position);
            mItemsIds.remove(t);
            mList.remove(position);
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mItemsIds.get(mList.get(position));
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = getItemView(position, convertView, parent);
        view.setOnTouchListener(mOnTouchListener);
        return view;
    }

    protected abstract View getItemView(int position, View convertView, ViewGroup parent);

    private int getUniqueId() {
        return mUniqueNextId++;
    }

    private class MyOnTouchListener implements View.OnTouchListener {
        private float downX;
        private float downY;
        private boolean isDown;
        private boolean isHorizontalScroll;
        private float deltaLastMove;
        private float lastMoveX;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (isDown) {
                        return true;
                    }
                    downX = event.getX();
                    downY = event.getY();
                    lastMoveX = downX;
                    isDown = true;
                    break;
                case MotionEvent.ACTION_MOVE:
                    deltaLastMove = lastMoveX - event.getX();
                    lastMoveX = event.getX();
                    if (!isHorizontalScroll) {
                        float deltaMoveAbs = Math.abs(deltaLastMove);
                        if (deltaMoveAbs > 50) {
                            isHorizontalScroll = true;
                            mListView.requestDisallowInterceptTouchEvent(true);

                        }
                    }
                    if (isHorizontalScroll) {
                        v.setX(v.getX() + deltaLastMove);
                        return true;
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    v.setX(v.getX() - downX - event.getX());

                    realiseValues();
                    break;
            }
            return false;
        }

        private void realiseValues() {
            isDown = false;
            isHorizontalScroll = false;
            mListView.requestDisallowInterceptTouchEvent(false);
        }
    }
}
