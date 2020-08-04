package org.jun1or.imgsel.image;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;

/**
 * 实现MediaScannerConnection.MediaScannerConnectionClient
 */
public class MediaScanner implements MediaScannerConnection.MediaScannerConnectionClient {

    /**
     * 扫描对象
     */
    private MediaScannerConnection mMediaScannerConnection = null;

    public MediaScanner(Context context) {
        //实例化
        mMediaScannerConnection = new MediaScannerConnection(context, this);
    }

    /**
     * 文件路径集合
     **/
    private String[] mFilePaths;
    /**
     * 文件MimeType集合
     **/
    private String[] mMimeTypes;

    /**
     * 扫描文件
     *
     * @param filePaths
     * @param mimeTypes
     * @author YOLANDA
     */
    public void scanFiles(String[] filePaths, String[] mimeTypes) {
        this.mFilePaths = filePaths;
        this.mMimeTypes = mimeTypes;
        mMediaScannerConnection.connect();//连接扫描服务
    }

    @Override
    public void onMediaScannerConnected() {
        for (int i = 0; i < mFilePaths.length; i++) {
            //服务回调执行扫描
            mMediaScannerConnection.scanFile(mFilePaths[i], mMimeTypes[i]);
        }
        mFilePaths = null;
        mMimeTypes = null;
    }

    private int scanTimes = 0;

    /**
     * 扫描一个文件完成
     *
     * @param path
     * @param uri
     */
    @Override
    public void onScanCompleted(String path, Uri uri) {
        scanTimes++;
        if (scanTimes == mFilePaths.length) {
            //如果扫描完了全部文件
            //断开扫描服务
            mMediaScannerConnection.disconnect();
            //复位计数
            scanTimes = 0;
        }
    }
}
