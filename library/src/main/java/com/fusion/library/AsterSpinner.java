package com.fusion.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
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
    private DisplayInterceptor displayInterceptor;

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

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AsterSpinner);
        try {
            title = a.getString(R.styleable.AsterSpinner_aster_title);
        } finally {
            a.recycle();
        }

        setFocusable(false);
        setCursorVisible(false);


        // Title
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

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
                        .setPadding(40, 0, 40, 40)
                        .setContentHeight(1000)
                        .create();
                TextView headerTitle = (TextView) selector.getHeaderView().findViewById(R.id.aster_header_title);
                selector.getHolderView().setVerticalScrollBarEnabled(false);
                headerTitle.setText(title != null ? title : "Choose from");
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

}

