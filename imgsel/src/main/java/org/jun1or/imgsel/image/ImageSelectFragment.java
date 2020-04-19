package org.jun1or.imgsel.image;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.istrong.imgsel.R;
import org.jun1or.imgsel.adapter.FolderListRecAdapter;
import org.jun1or.imgsel.adapter.ImageListRecAdapter;
import org.jun1or.imgsel.bean.Folder;
import org.jun1or.imgsel.ImageConfig;
import org.jun1or.imgsel.bean.Image;
import org.jun1or.imgsel.preview.ImagePreviewActivity;

import com.istrong.util.FileUtil;
import com.istrong.widget.divider.DividerItemDecoration;
import com.istrong.widget.divider.GridDividerItemDecoration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ImageSelectFragment extends Fragment implements View.OnClickListener, FolderListRecAdapter.OnFolderItemClickListener,
        ImageListRecAdapter.OnItemClickListener {

//    public static final int REQUEST_CODE_TAKEPHOTO = 100;
//    public static final int REQUEST_CODE_VIDEO = 200;

    public static final String KEY_Config = "config";

    private List<Folder> mFolderList = new ArrayList<>();
    public List<Image> mImageList = new ArrayList<>();

    private ImageListRecAdapter mImageListRecAdapter;
    private FolderListRecAdapter mFolderListRecAdapter;
    private LinearLayout mLlFolderContainer;
    private RecyclerView mRecFolderList;

    private PressWatchTextView mTvFolderName;

    private ImageConfig mISConfig;

//    private File mTakePhotoFile = null;


    public static ImageSelectFragment instance() {
        ImageSelectFragment fragment = new ImageSelectFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.imgsel_fragment_image_select, null, false);
        mISConfig = getArguments().getParcelable(KEY_Config);
        if (mISConfig == null)
            mISConfig = new ImageConfig.Builder().build();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecImageList(view);
        initFolderList(view);
        initBot(view);
        getActivity().getSupportLoaderManager().initLoader(0, null, mLoaderCallback);
    }

    private void initFolderList(View view) {
        mLlFolderContainer = (LinearLayout) view.findViewById(R.id.llFolderContainer);
        mLlFolderContainer.setOnClickListener(this);
        mRecFolderList = (RecyclerView) view.findViewById(R.id.recFolderList);
        mRecFolderList.setHasFixedSize(true);
        mRecFolderList.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mRecFolderList.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL, R.drawable.imgsel_divider_split_line, false));
        mFolderListRecAdapter = new FolderListRecAdapter();
        mFolderListRecAdapter.setFolderList(mFolderList);
        mFolderListRecAdapter.setOnFolderItemClickListener(this);
        mRecFolderList.setAdapter(mFolderListRecAdapter);
    }

    private void initRecImageList(View view) {
        RecyclerView recImageList = (RecyclerView) view.findViewById(R.id.recImageList);
        recImageList.setLayoutManager(new GridLayoutManager(view.getContext(), 4));
        recImageList.setHasFixedSize(true);
        recImageList.addItemDecoration(new GridDividerItemDecoration(view.getContext()));
        mImageListRecAdapter = new ImageListRecAdapter();
        mImageListRecAdapter.setOnItemClickListener(this);
        mImageListRecAdapter.setCheckedList(convert(mISConfig.mImgList));
        mImageListRecAdapter.setISConfig(mISConfig);
        recImageList.setAdapter(mImageListRecAdapter);
    }

    private List<Image> convert(List<String> oriImageList) {
        ArrayList<Image> imageList = new ArrayList<>();
        if (oriImageList == null)
            return imageList;
        for (String path : oriImageList) {
            Image image = new Image();
            image.path = path;
            image.name = FileUtil.getFileName(path);
            imageList.add(image);
        }
        return imageList;
    }

    private void initBot(final View view) {
        final RightBottomTriAngleView rightBottomTriAngleView = (RightBottomTriAngleView) view.findViewById(R.id.triangleView);
//        rightBottomTriAngleView.setTriangleColor(mISConfig.mTextColor);
        mTvFolderName = (PressWatchTextView) view.findViewById(R.id.tvFolderName);
//        mTvFolderName.setTextColor(mISConfig.mTextColor);
        mTvFolderName.setText("所有图片");
        mTvFolderName.setOnClickListener(this);
        mTvFolderName.setOnPressWatchListener(new PressWatchTextView.OnPressWatchListener() {
            @Override
            public void onPressWath(boolean pressed) {
                if (pressed) {
                    rightBottomTriAngleView.setTriangleAlpha(100);
                } else {
                    rightBottomTriAngleView.setTriangleAlpha(255);
                }
            }
        });
        TextView tvPreview = (TextView) view.findViewById(R.id.tvPreView);
//        tvPreview.setTextColor(mISConfig.mTextColor);
        tvPreview.setOnClickListener(this);
    }

    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media._ID
        };

        private final String[] VIDEO_PROJECTION = {
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DURATION,
        };

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(getActivity(),
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                    null, null, MediaStore.Images.Media.DATE_ADDED + " DESC");
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null) {
                int count = data.getCount();
                if (count > 0) {
                    List<Image> tempImageList = new ArrayList<>();
                    data.moveToFirst();
                    do {
                        String path = data.getString(data.getColumnIndexOrThrow(VIDEO_PROJECTION[0]));
                        String name = data.getString(data.getColumnIndexOrThrow(VIDEO_PROJECTION[1]));
//                        Log.e("TAG", "========" + data.getInt(data.getColumnIndexOrThrow(VIDEO_PROJECTION[3])));
                        Image image;
                        image = new Image(path, name);
                        File imageFile = new File(path);
                        File folderFile = imageFile.getParentFile();
                        if (folderFile == null || !imageFile.exists() || imageFile.length() < 10) {
                            continue;
                        }
                        tempImageList.add(image);
                        Folder parent = null;
                        for (Folder folder : mFolderList) {
                            if (TextUtils.equals(folder.path, folderFile.getAbsolutePath())) {
                                parent = folder;
                            }
                        }
                        if (parent != null) {
                            parent.images.add(image);
                        } else {
                            parent = new Folder();
                            parent.name = folderFile.getName();
                            parent.path = folderFile.getAbsolutePath();
                            parent.cover = image;
                            List<Image> imageList = new ArrayList<>();
                            imageList.add(image);
                            parent.images = imageList;
                            mFolderList.add(parent);
                        }
                    } while (data.moveToNext());
                    mImageList.clear();
                    mImageList.addAll(tempImageList);
                    if (mImageList.size() == 0) {
                        return;
                    }
                    mImageListRecAdapter.setImageList(mImageList);
                    Folder folder = new Folder();
                    folder.cover = mImageList.get(0);
                    folder.images = mImageList;
                    folder.name = "所有图片";
                    folder.path = "";
                    mFolderList.add(0, folder);
                    mFolderListRecAdapter.setFolderList(mFolderList);
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tvFolderName) {
            //显示或者隐藏文件夹列表
            toggleFolderList(v);
        } else if (id == R.id.tvPreView) {
            //预览
            if (mImageListRecAdapter.getCheckedList().size() == 0)
                return;
            Intent intent = new Intent(v.getContext(), ImagePreviewActivity.class);
            intent.putExtra(ImagePreviewActivity.KEY_config, mISConfig);
            intent.putParcelableArrayListExtra(ImagePreviewActivity.KEY_IMAGELIST, (ArrayList<Image>) mImageListRecAdapter.getCheckedList());
            getActivity().startActivityForResult(intent, 100);
        } else if (id == R.id.llFolderContainer) {
            if (mLlFolderContainer.getVisibility() == View.VISIBLE) {
                toggleFolderList(v);
            }
        }
    }

    private void toggleFolderList(View view) {
        if (mLlFolderContainer.getVisibility() == View.VISIBLE)
            hideFolderList(view.getContext());
        else
            showFolderList(view.getContext());
    }

    private void hideFolderList(Context context) {
        if (mLlFolderContainer.getVisibility() == View.VISIBLE) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.imgsel_slide_to_bottom);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mLlFolderContainer.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mRecFolderList.startAnimation(animation);
        }
    }

    private void showFolderList(Context context) {
        if (mLlFolderContainer.getVisibility() == View.GONE) {
            mLlFolderContainer.setVisibility(View.VISIBLE);
            mRecFolderList.startAnimation(AnimationUtils.loadAnimation(context, R.anim.imgsel_slide_to_up));
        }
    }

    @Override
    public void onFolderItemClick(Folder folder) {
        mImageListRecAdapter.setImageList(folder.images);
        mTvFolderName.setText(folder.name);
        hideFolderList(getContext());
    }

    @Override
    public void onMediaCheckClick(List<Image> checkedImageList, Image curImage) {
        ((ImageSelectActivity) getActivity()).setChoiceText(checkedImageList);
    }

    @Override
    public void onMediaClick(Image curImage, int position) {
        Intent intent = new Intent(getActivity(), ImagePreviewActivity.class);
        ImagePreviewActivity.sImageList = mImageListRecAdapter.getImageList();//防止界面传输量太大，导致奔溃
        intent.putExtra(ImagePreviewActivity.KEY_config, mISConfig);
        intent.putExtra(ImagePreviewActivity.KEY_index, position);
        startActivity(intent);
    }

    @Override
    public void onReachMaxNum(int maxNum) {
        if (getActivity() != null)
            Toast.makeText(getActivity(), String.format(getString(R.string.imgsel_reach_max_num), maxNum + ""), Toast.LENGTH_SHORT).show();
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode != RESULT_OK) {
//            return;
//        }
//        if (requestCode == REQUEST_CODE_TAKEPHOTO) {
//            resultData(mTakePhotoFile.getAbsolutePath());
//        } else if (requestCode == REQUEST_CODE_VIDEO) {
//            Uri videoUri = data.getData();
//            String filePath = AppUtil.getRealFilePath(getActivity(), videoUri);
//            if (TextUtils.isEmpty(filePath)) {
//                Toast.makeText(getActivity(), "视频录制失败！", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            resultData(filePath);
//        }
//    }

    private void resultData(String filePath) {
        Intent intent = new Intent();
        List<Image> photoList = new ArrayList();
        Image image = new Image(filePath, FileUtil.getFileName(filePath));
        photoList.add(image);
        intent.putParcelableArrayListExtra(ImageSelectActivity.KEY_result, (ArrayList) photoList);
        getActivity().setResult(RESULT_OK, intent);
        getActivity().finish();
    }

}
