

package com.indra.rover.mwsi.ui.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.indra.rover.mwsi.utils.Utils;

public class SignatureArea extends SurfaceView implements SurfaceHolder.Callback {

    public SignatureArea(Context context) {
        this(context, null);
    }

    public SignatureArea(Context context, AttributeSet attrs) {
        super(context, attrs);

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        setFocusable(true); // make sure we get key events

        black = new Paint();
        black.setColor(Color.BLACK);

        fontPaint = new Paint();
        fontPaint.setColor(Color.BLUE);
        fontPaint.setTextSize(25);
         clearPaint = new Paint();
        setWillNotDraw(false);

    }

    private float lastPointX;
    private float lastPointY;
    private Paint black;
    private Paint fontPaint;
    private Paint clearPaint;
    private Bitmap signatureAreaContent = null;
    String receipient;
    boolean isEditMode = true;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(isEditMode) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                lastPointX = event.getX();
                lastPointY = event.getY();

            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                Canvas c = new Canvas(signatureAreaContent);
                c.drawLine(lastPointX, lastPointY, event.getX(), event.getY(), black);
                lastPointX = event.getX();
                lastPointY = event.getY();
                postInvalidate();

            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                Canvas c = new Canvas(signatureAreaContent);
                c.drawLine(lastPointX, lastPointY, event.getX(), event.getY(), black);
                postInvalidate();
            }
        }
        return true;
    }


    public void clearSignature(){
        Paint lightGray = new Paint();
        lightGray.setColor(Color.LTGRAY);
        Canvas c = new Canvas(signatureAreaContent);
        c.drawRect(0, 0, signatureAreaContent.getWidth(), signatureAreaContent.getHeight(), lightGray);
        invalidate();

    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Paint lightGray = new Paint();
        lightGray.setColor(Color.LTGRAY);

        signatureAreaContent = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(signatureAreaContent);
        c.drawRect(0, 0, width, height, lightGray);
     //   drawRecipient();
        invalidate();
    }

    public void surfaceCreated(SurfaceHolder holder) {
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (signatureAreaContent != null) {
            canvas.drawBitmap(signatureAreaContent, 0, 0, null);
        }


        super.onDraw(canvas);
    }

    public Bitmap getBitmap() {
        return signatureAreaContent;
    }

    public void setBitMapSignature(Bitmap bitmap){
        this.signatureAreaContent = bitmap;
       // invalidate();
    }

    public void setEditMode(boolean isEditMode){
        this.isEditMode = isEditMode;
    }



    public void setReceipient(String receipient){
        this.receipient = receipient;
      //  drawRecipient();
     //   invalidate();
    }
    /*
    public void drawRecipient(){
        Canvas c = new Canvas(signatureAreaContent);
        StringBuilder builder = new StringBuilder();
        builder.append("Recipient Name : ");
        if(Utils.isNotEmpty(receipient)){
            builder.append(receipient);
        }
        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawRect(0, 0, width, height, clearPaint);
        c.drawText(builder.toString(),10,c.getHeight()-50,fontPaint);
    }
    */


}
