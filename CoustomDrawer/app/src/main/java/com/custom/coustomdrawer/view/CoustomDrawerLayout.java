package com.custom.coustomdrawer.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.custom.coustomdrawer.R;

public class CoustomDrawerLayout extends RelativeLayout {
    private Context mContext;
    private boolean isShow;
    RelativeLayout mShadeRl;
    ImageView mButtonIv;
    RelativeLayout mContentRl;
    LinearLayout mContainerLl;

    public CoustomDrawerLayout(Context context) {
        super(context);
        mContext = context;
        init(null);
    }

    public CoustomDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    public CoustomDrawerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {

        View.inflate(mContext, R.layout.layout_coustom_drawer, this);
        mShadeRl = (RelativeLayout) findViewById(R.id.rl_shade);
        mButtonIv = (ImageView) findViewById(R.id.iv_button);

        mContentRl = (RelativeLayout) findViewById(R.id.rl_content);
        mContainerLl = (LinearLayout) findViewById(R.id.ll_container);

        mContainerLl.post(new Runnable() {
            @Override
            public void run() {
                RelativeLayout.LayoutParams params = (LayoutParams) mContainerLl.getLayoutParams();
                params.width = 300;
                mContainerLl.setLayoutParams(params);
                mContainerLl.post(new Runnable() {
                    @Override
                    public void run() {
                        closeDrawer(0);
                        isShow = false;
                    }
                });
            }
        });
        if(attrs != null){
            TypedArray typedArray = getResources().obtainAttributes(attrs, R.styleable.CoustomDrawerLayout);
            Drawable drawable = typedArray.getDrawable(R.styleable.CoustomDrawerLayout_image_src);
            mButtonIv.setImageDrawable(drawable);
        }
        mShadeRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShow) {
                    closeDrawer(500);
                    isShow = false;
                }
            }
        });

        mContainerLl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mButtonIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "点击了", Toast.LENGTH_SHORT).show();
                if (isShow) {
                    //隐藏
                    closeDrawer(500);
                    isShow = false;
                } else {
                    //显示
                    openDrawer(500);
                    isShow = true;
                }
            }
        });
    }

    public void closeDrawer(long duration) {
        if (duration == -1) {
            duration = 2000;
        }
        ObjectAnimator animator = ObjectAnimator.ofFloat(mContainerLl, "translationX", 0, mContentRl.getWidth());

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                mButtonIv.setEnabled(false);
                mShadeRl.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mButtonIv.setEnabled(true);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.setDuration(duration);
        animator.start();
    }

    public void openDrawer(long duration) {
        if (duration == -1) {
            duration = 2000;
        }
        ObjectAnimator animator = ObjectAnimator.ofFloat(mContainerLl, "translationX", mContentRl.getWidth(), 0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float aa = (float) valueAnimator.getAnimatedValue();
                aa = mContentRl.getWidth() - aa;
                float mPercent = aa / mContentRl.getWidth();
                if(mPercent> 0.6f){
                    mPercent = 0.6f;
                }
                mShadeRl.setAlpha(mPercent);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                mButtonIv.setEnabled(false);
                mShadeRl.setVisibility(View.VISIBLE);
                mShadeRl.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mButtonIv.setEnabled(true);
                mShadeRl.setClickable(true);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.setDuration(duration);
        animator.start();
    }

}
