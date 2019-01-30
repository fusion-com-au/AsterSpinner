package com.fusion.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fusion.asteredittext.AsterEditText;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;

/**
 *
 * Created by scott.carey on 16/05/2017.
 *
 */
public class AsterSpinner extends AsterEditText {

    private DialogPlus selector;
    private BaseAdapter adapter;
    private OnItemClickListener itemListener;
    private String title;
    private float titleTextSize;
    private DisplayInterceptor displayInterceptor;
    private final float TARGET_CONTENT_HEIGHT_PERCENTAGE = 0.6f;

    public interface DisplayInterceptor {
        CharSequence beforeDisplayChanged( Object object );
    }

    public AsterSpinner(Context context) {
        super(context);
    }

    public AsterSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AsterSpinner(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
        init(context, attrs);
    }

    public void init(final Context context, final AttributeSet attrs) {

        TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.AsterSpinner);
        try {
            title = styledAttributes.getString(R.styleable.AsterSpinner_aster_title);
            titleTextSize = styledAttributes.getDimension(R.styleable.AsterSpinner_title_text_size, 0f);
        } finally {
            styledAttributes.recycle();
        }

        setFocusable(false);
        setCursorVisible(false);
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        final int screenHeight =  displayMetrics.heightPixels;
        final int targetHeight = (int)(screenHeight * TARGET_CONTENT_HEIGHT_PERCENTAGE);

        // Title
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(v instanceof AsterSpinner){
                    ((AsterSpinner)v).hideKeyboard();
                }

                if (adapter == null) {
                    throw new IllegalStateException("Adapter not provided.");
                }

                selector = DialogPlus.newDialog(context)
                        .setHeader(R.layout.aster_selector_header)
                        .setAdapter(adapter)
                        .setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(DialogPlus dialog, Object item, View view, int position) {

                                // Header was clicked.
                                if( position >= 0 ) {
                                    if (displayInterceptor != null) {
                                        setText(displayInterceptor.beforeDisplayChanged(adapter.getItem(position)));
                                    } else {
                                        setText(adapter.getItem(position).toString());
                                    }
                                    if (itemListener != null) {
                                        itemListener.onItemClick(dialog, item, view, position);
                                    }
                                }
                                selector.dismiss();
                            }
                        })
                        .setContentHeight(targetHeight)
                        .create();
                TextView headerTitle = (TextView) selector.getHeaderView().findViewById(R.id.aster_header_title);
                selector.getHolderView().setVerticalScrollBarEnabled(false);
                headerTitle.setText(title != null ? title : "Choose from");
                if (titleTextSize > 0) headerTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, titleTextSize);
                headerTitle.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selector.dismiss();
                    }
                });
                selector.show();
            }
        });
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        this.hideKeyboard();
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    public void setAdapter(BaseAdapter adapter) {
        if (adapter != null) {
            this.adapter = adapter;
        }
    }

    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
        if (listener != null) {
            itemListener = listener;
        }
    }

    public void setDisplayInterceptor( DisplayInterceptor displayInterceptor ) {
        this.displayInterceptor = displayInterceptor;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Set the text size of the spinner title.
     * @param textSize The text size in sp.
     */
    public void setTitleTextSize(float textSize) { this.titleTextSize = textSize; }

    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindowToken(), 0);
    }

}

