package com.andova.face;

import android.graphics.Color;

import com.andova.face.detector.ICameraPreview;
import com.google.android.cameraview.CameraView;

import static com.google.android.cameraview.CameraView.FACING_BACK;
import static com.google.android.cameraview.Constants.FACING_FRONT;

/**
 * Created by Administrator on 2018-03-09.
 * <p>识别代理类</p>
 *
 * @author kzaxil
 * @since 1.0.0
 */
public class DetectorProxy<T> {
    private ICameraPreview mCameraPreview;
    private FaceRectView mFaceRectView;
    private IFaceDetector<T> mFaceDetector;
    private boolean mDrawFaceRect;

    /**
     * 构造函数，需传入自定义相机预览界面
     *
     * @param mCameraPreview 相机预览界面
     */
    private DetectorProxy(ICameraPreview mCameraPreview) {
        this.mCameraPreview = mCameraPreview;
    }

    /**
     * 设置绘制人脸检测框界面
     */
    public void setFaceRectView(FaceRectView mFaceRectView) {
        this.mFaceRectView = mFaceRectView;
    }

    /**
     * 设置人脸检测类，默认实现为原生检测类，可以替换成第三方库检测类
     *
     * @param faceDetector 人脸检测类
     */
    public void setFaceDetector(IFaceDetector<T> faceDetector) {
        if (faceDetector != null) {
            this.mFaceDetector = faceDetector;
        }
        if (mCameraPreview != null) {
            mCameraPreview.setFaceDetector(this.mFaceDetector);
        }
    }

    /**
     * 设置相机检查监听
     */
    public void setCheckListener(ICameraCheckListener mCheckListener) {
        if (mCameraPreview != null) {
            mCameraPreview.setCheckListener(mCheckListener);
        }
    }

    /**
     * 设置检测监听
     */
    public void setDataListener(final IDataListener<T> mDataListener) {
        if (mFaceDetector != null) {
            mFaceDetector.setDataListener(new IDataListener<T>() {
                @Override
                public void onDetectorData(DetectorData<T> detectorData) {
                    if (mDrawFaceRect && mFaceRectView != null && detectorData != null
                            && detectorData.getFaceRectList() != null) {
                        mFaceRectView.drawFaceRect(detectorData);
                    }
                    if (mDataListener != null) {
                        mDataListener.onDetectorData(detectorData);
                    }
                }
            });
        }
    }

    /**
     * 设置相机预览为前置还是后置摄像头
     */
    public void setCameraId(@CameraView.Facing int mCameraId) {
        if (mCameraId == FACING_BACK || mCameraId == FACING_FRONT) {
            if (mCameraPreview != null) {
                mCameraPreview.setCameraId(mCameraId);
            }
        }
    }

    /**
     * 设置像素最低要求
     */
    public void setMinCameraPixels(long mMinCameraPixels) {
        if (mCameraPreview != null) {
            mCameraPreview.setMinCameraPixels(mMinCameraPixels);
        }
    }

    /**
     * 设置检测最大人脸数量
     */
    public void setMaxFacesCount(int mMaxFacesCount) {
        if (mFaceDetector != null) {
            mFaceDetector.setMaxFacesCount(mMaxFacesCount);
        }
    }

    /**
     * 设置是否绘制人脸检测框
     */
    public void setDrawFaceRect(boolean mDrawFaceRect) {
        this.mDrawFaceRect = mDrawFaceRect;
    }

    /**
     * 设置人脸检测框是否是矩形
     */
    public void setFaceIsRect(boolean mFaceIsRect) {
        if (mFaceRectView != null) {
            mFaceRectView.setFaceIsRect(mFaceIsRect);
        }
    }

    /**
     * 设置人脸检测框颜色
     */
    public void setFaceRectColor(int rectColor) {
        if (mFaceRectView != null) {
            mFaceRectView.setRectColor(rectColor);
        }
    }

    /**
     * 开启检测
     */
    public void detector() {
        if (mFaceDetector != null) {
            mFaceDetector.detector();
        }
        openCamera();
    }

    /**
     * 打开相机
     */
    public void openCamera() {
        if (mCameraPreview != null) {
            mCameraPreview.openCamera();
        }
    }

    /**
     * 关闭相机
     */
    public void closeCamera() {
        if (mCameraPreview != null) {
            mCameraPreview.closeCamera();
        }
    }

    /**
     * 获取相机ID
     */
    @CameraView.Facing
    public int getCameraId() {
        if (mCameraPreview != null) {
            return mCameraPreview.getCameraId();
        }
        return FACING_BACK;
    }

    /**
     * 释放资源
     */
    public void release() {
        if (mCameraPreview != null) {
            mCameraPreview.release();
        }
    }

    public static class Builder<T> {
        private static final int MIN_CAMERA_PIXELS = 5000000;
        private static final int MAX_DETECTOR_FACES = 5;

        private ICameraPreview mCameraPreview;
        private FaceRectView mFaceRectView;
        private ICameraCheckListener mCheckListener;
        private IDataListener<T> mDataListener;
        private IFaceDetector<T> mFaceDetector = new SystemFaceDetector<>();
        private int mCameraId = FACING_BACK;
        private long mMinCameraPixels = MIN_CAMERA_PIXELS;
        private int mMaxFacesCount = MAX_DETECTOR_FACES;
        private int mFaceRectColor = Color.rgb(255, 203, 15);
        private boolean mDrawFaceRect = false;
        private boolean mFaceIsRect = false;

        public Builder(ICameraPreview mCameraPreview) {
            this.mCameraPreview = mCameraPreview;
        }

        public Builder setFaceRectView(FaceRectView mFaceRectView) {
            this.mFaceRectView = mFaceRectView;
            return this;
        }

        public Builder setFaceDetector(IFaceDetector<T> mFaceDetector) {
            this.mFaceDetector = mFaceDetector;
            return this;
        }

        public Builder setCameraId(@CameraView.Facing int mCameraId) {
            this.mCameraId = mCameraId;
            return this;
        }

        public Builder setMinCameraPixels(long mMinCameraPixels) {
            this.mMinCameraPixels = mMinCameraPixels;
            return this;
        }

        public Builder setCheckListener(ICameraCheckListener mCheckListener) {
            this.mCheckListener = mCheckListener;
            return this;
        }

        public Builder setDataListener(IDataListener<T> mDataListener) {
            this.mDataListener = mDataListener;
            return this;
        }

        public Builder setMaxFacesCount(int mMaxFacesCount) {
            this.mMaxFacesCount = mMaxFacesCount;
            return this;
        }

        public Builder setDrawFaceRect(boolean mDrawFaceRect) {
            this.mDrawFaceRect = mDrawFaceRect;
            return this;
        }

        public Builder setFaceIsRect(boolean mFaceIsRect) {
            this.mFaceIsRect = mFaceIsRect;
            return this;
        }

        public Builder setFaceRectColor(int mFaceRectColor) {
            this.mFaceRectColor = mFaceRectColor;
            return this;
        }

        public DetectorProxy build() {
            DetectorProxy detectorProxy = new DetectorProxy(mCameraPreview);
            detectorProxy.setFaceDetector(mFaceDetector);
            detectorProxy.setCheckListener(mCheckListener);
            detectorProxy.setDataListener(mDataListener);
            detectorProxy.setMaxFacesCount(mMaxFacesCount);
            detectorProxy.setMinCameraPixels(mMinCameraPixels);
            if (mFaceRectView != null && mDrawFaceRect) {
                detectorProxy.setFaceRectView(mFaceRectView);
                detectorProxy.setDrawFaceRect(mDrawFaceRect);
                detectorProxy.setFaceRectColor(mFaceRectColor);
                detectorProxy.setFaceIsRect(mFaceIsRect);
            }
            detectorProxy.setCameraId(mCameraId);
            return detectorProxy;
        }
    }
}
