package action.eeg.yishi.dogeeg.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import action.eeg.yishi.dogeeg.R;

/**
 * Created by yishikeji_04 on 2017/3/3.
 */

public class BottomView extends View {
    private int mColor;
    private Bitmap mIconBitmap;
    private String mText;
    private int mTextSize= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12,getResources().getDisplayMetrics());

    private Bitmap mBitmap; //位图
    private Canvas mCanvas; //画布
    private Paint mPaint;    //画笔

    private float mAlpha;       //透明度

    private Rect mIconRect;  //图标边距
    private Rect mTextBound; //文字范围
    private Paint mTextPaint;

    public BottomView(Context context) {
        this(context,null);
    }

    public BottomView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    /*
    * 获取自定义属性值*/
    public BottomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray styles=context.obtainStyledAttributes(attrs, R.styleable.BottomView);//获取自定义style
        int n=styles.getIndexCount();//获取自定义style个数
        for(int i=0;i<n;i++){
            int attr=styles.getIndex(i);
            switch (attr){
                case R.styleable.BottomView_icon:
                    BitmapDrawable drawable= (BitmapDrawable) styles.getDrawable(attr);
                    mIconBitmap=drawable.getBitmap();
                    break;
                case R.styleable.BottomView_text:
                    mText=styles.getString(attr);
                    break;
                case R.styleable.BottomView_textSize:
                    mTextSize= (int) styles.getDimension(attr,TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12,getResources().getDisplayMetrics()));
                    break;
                case R.styleable.BottomView_color:
                    mColor=styles.getColor(attr,0xFF45C01A);
                    break;
            }
        }
        styles.recycle();//回收自定义style对象

        mTextBound=new Rect();//text边距
        mTextPaint=new Paint();
        mTextPaint.setTextSize(mTextSize);        //设置textSize
        mTextPaint.getTextBounds(mText,0,mText.length(),mTextBound);//***获取text边距***

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {//测量尺寸
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int iconWidth=Math.min(getMeasuredWidth()-getPaddingLeft()-getPaddingRight(),getMeasuredHeight()-getPaddingTop()-getPaddingBottom()-mTextBound.height());
        int left=(getMeasuredWidth()-iconWidth)/2;
        int top=(getMeasuredHeight()-(mTextBound.height()+iconWidth))/2;
        mIconRect=new Rect(left,top,left+iconWidth,iconWidth+top); //获得icon尺寸

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mIconBitmap,null,mIconRect,null);//绘制原icon
//        //内存去准备bitmap,setAlpha,纯色,Xfermode,图标
        int alpha= (int) Math.ceil(255*mAlpha);
        setupTargetBitpmap(alpha);
//        //1.绘制原文本 2.绘制变色文本
        drawSourceText(canvas,alpha);
        drawTargetText(canvas,alpha);
        canvas.drawBitmap(mBitmap,0,0,null );
    }

    /*
    * 绘制原文本
    * */
    private void drawSourceText(Canvas canvas, int mAlpha) {
        mTextPaint.setColor(Color.parseColor("GREY"));
        mTextPaint.setAlpha(255-mAlpha);
        int x=getMeasuredWidth()/2-mTextBound.width()/2;
        int y=mIconRect.bottom+mTextBound.height();
        canvas.drawText(mText,x,y,mTextPaint);
    }
    /*
   * 绘制变色文本
   * */
    private void drawTargetText(Canvas canvas, int mAlpha) {
        mTextPaint.setColor(mColor);
        mTextPaint.setAlpha(mAlpha);
        int x=getMeasuredWidth()/2-mTextBound.width()/2;
        int y=mIconRect.bottom+mTextBound.height();
        canvas.drawText(mText,x,y,mTextPaint);
    }

    /*
    * 在内存中绘制可变色的Icon
     */
    private void setupTargetBitpmap(int alpha) {
        mBitmap=Bitmap.createBitmap(getMeasuredWidth(),getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        mCanvas=new Canvas(mBitmap);
        mPaint=new Paint();
        mPaint.setColor(mColor);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setAlpha(alpha);
        mCanvas.drawRect(mIconRect,mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mCanvas.drawBitmap(mIconBitmap,null,mIconRect,mPaint);
    }

    /*
    * 外界设置Alpha值
    * */
    public void setIconAlpha(float alpha){
        this.mAlpha=alpha;
        invalidateView();
    }

    private void invalidateView() {
       if(Looper.getMainLooper()==Looper.myLooper()){
           invalidate();
       } else{
           postInvalidate();
       }
    }
    private static final String INSTANCE_STATE="instance_state";
    private static final String STATE_ALPHA="state_alpha";

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle=new Bundle();
        bundle.putParcelable(INSTANCE_STATE,super.onSaveInstanceState());
        bundle.putFloat(STATE_ALPHA,mAlpha);
        return  bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Bundle){
            Bundle bundle= (Bundle) state;
            mAlpha=bundle.getFloat(STATE_ALPHA);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
        }

    }
}
