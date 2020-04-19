# 图片选择

- 支持图片单选、多选
- 目前支持的自定义配置项：标题、回退图标、文本颜色、最大选择图片数量、是否多选、默认选中的图片列表
- 支持自定义图片加载器

## 引用

1.在根目录build.gradle文件中添加

```groovy

```

2.项目build.gradle文件中添加

```groovy

```

## 使用方法

1.使用前初始化图片加载器
```java
           ISNav.getInstance().init(new ImageLoader() {
               @Override
               public void displayImage(Context context, String path, ImageView imageView) {
                   GlideApp.with(context).load(path).into(imageView);
               }
           });
```


2.启动选择界面

```java
    public void goImageSingleSelectActivity(View view) {
        ImageConfig config = new ImageConfig.Builder()
                .textColor(Color.RED)
                .backImageRes(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material)
                .title("测试标题")
                .multiSelect(false)
                .build();
        ISNav.getInstance().toImageSelectActivity(this, config, 100);
    }
```

2.获取选择结果,在onActivityResult中使用如下方式获取

```java
  if (resultCode != RESULT_OK)
            return;
List<String> imageList = data.getStringArrayListExtra(ImageSelectActivity.KEY_result);
```

3.传入上次选择结果：

```java
    public void goImageMultiSelectActivity(View view) {
        ArrayList<String> images = new ArrayList<>();
        TextView tvImages = (TextView) findViewById(R.id.tvImages);
        if (!TextUtils.isEmpty(tvImages.getText())) {
            String[] paths = tvImages.getText().toString().split(",\n");
            for (int i = 0; i < paths.length; i++) {
                images.add(paths[i]);
            }
        }
        ImageConfig config = new ImageConfig.Builder()
                .maxNum(5)
                .title("测试标题")
                .multiSelect(true)
                .selectedList(images)
                .build();
        ISNav.getInstance().toImageSelectActivity(this, config, 101);
    }
```

详细使用请参考Demo


## 版本记录

### 1.0.0
- 初始版本