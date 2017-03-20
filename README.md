# PictureCarousel
[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Download](https://api.bintray.com/packages/tangsiyuan/maven/myokhttp/images/download.svg) ](https://bintray.com/tangsiyuan/maven/myokhttp/_latestVersion)

先上效果图：


![效果图.gif](http://upload-images.jianshu.io/upload_images/2178834-0f229b48ce074c32.gif?imageMogr2/auto-orient/strip)


可以看到，我们实现了一个简易的图片轮播器。下面我们从设计到实现一步一步来实现这个自定义的控件。
先给上地址**<https://github.com/Reoger/PictureCarousel>**，源代码。

# 使用
1.  添加依赖。

2.在需要的放置图片轮播的地方放置：、
```
  <com.hut.reoger.pictruecarousel.imageBanner
        android:id="@+id/ban"
        android:layout_width="match_parent"
        android:layout_height="170dp"
        app:time_interval="1000"
        />
```
其中的**   app:time_interval="1000"** 数字代表的是自动轮播时的时间间隔。
3. 在java代码中添加图片数据
```
 List<Bitmap> bitmaps = new ArrayList<>();
    private void initData() {
        for (int i = 0; i < id.length; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),id[i]);
            bitmaps.add(bitmap);
        }
        ban.addImageToImageBarnner(bitmaps);
/ ban = (imageBanner) findViewById(R.id.ban);
    }
```
如果需要添加监听事件：
```
 ban.setOnImageViewSelectClick(new imageBanner.OnImageViewSelectClick() {
            @Override
            public void OnClickListener(int position) {
                Toast.makeText(MainActivity.this,"hello"+position,Toast.LENGTH_SHORT).show();
            }
        });
```
也可以通过实现添加imageBanner.OnImageViewSelectClick()接口，实现监听。

# 思路（1）

利用ViewPager来实现。利用ViewPager来实现图片轮播是比较简单的，如果有兴趣，可以参照我之前的博客[利用ViewPager实现引导界面](http://blog.csdn.net/reoger/article/details/53039048)，可以很方便的实现一个图片轮播。但这个不是我们今天的重点。在此不多做介绍。

# 思路（2）
直接自定义个控件，自己一步一步来实现。这个就是今天我们的重点。
首先来分析一下，我们要实现的效果应该怎么去做。

![效果图分析.png](http://upload-images.jianshu.io/upload_images/2178834-fa859c84cdebb0e8.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

从这个图我们可以看出来，轮播图主要由两部分组成，一部分用于显示图片，另一部分用于显示下面指示的小白点。看起来小白点是在图片的上面。这样，自然而然的我们就会想到用FrameLayout来实现这个效果，要实现的效果如下：

![效果预览图.png](http://upload-images.jianshu.io/upload_images/2178834-aa167c88e5cfaf9c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
通过FrameLayout将两个控件嵌套就可以很容易的实现。

整体思路有了，接下来就是一个一个来实现了。首先是显示图片的部分：很简单的思想就是直接用一个ScrollView来包装图片，通过一个横向的ScrollView就可以比较简单的实现显示多张图片。但是这里，我们采用自定义View的方法，具体的思路就是：
在初始化view的时候，先测量出图片的总长度，然后以图片的总长度定义为自定义vie的长度：
![Paste_Image.png](http://upload-images.jianshu.io/upload_images/2178834-1ef7fcb5a1de9759.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

实现滑动效果的话，也很简单。实现思路如下：首先拦截在本控件上的点击事件，当检测到手指移动的时候，自定义view按照手移动的位置进行移动，注意，这里我们需要给当前图片一个索引值，当手指移动的距离没有一张图片的时候，我们需要计算出来我们具体需要移动的偏移量，在手指抬起的时候，就需要将图片移动到偏移量的位置，否则会导致显示两张不全的图片。在检测到后面没有图片的时候，我们需要将偏移量设置为0；

实现自动轮播，我们只需要借助一个定时器即可实现。每隔多长时间我们就模拟一次手指的滑动即可。定义器我们可以借助Timer类来实现。

对了，我们还需要在图片上添加点击事件，我们可以通过在这个view类中添加一个接口，当判断手指在图片上有点击事件的时候（点击事件的判断：当手机按下时，设置一个变量为ture，移动时就将该变量设置为false，到手指抬起的时候，如果该变量还是为true的话，即可以判断是点击事件）我们手动调用我们在该类中的接口（注意要传入图片的索引值，否则无法判断是哪一张图片的点击事件）。如果需要点击事件的话，我们可以直接同Button控件一张为我们写的控件添加点击事件了。

到次，显示图片的控件就差不多了，接下来就是实现下面的知识点。
首先，指示点的布局采用简单的LinearLayout即可，因为我们可以看到，他们明显就是在一个水平线上。然后，指示点的数据肯定同照片的数量一样多。最主要的是，我们需要用这些点来显示当前显示的图片，这一点其实也很简单就可以实现。在我们前面的图片控件中，我们有一个图片索引值，当图片发生改变的时候，索引值也改变。我们只需要将索引值传给指示点，让特殊显示当前的索引值即可。

整的来说，轮播图实现起来是比较简单的，但是还是需要一定的基础，通过造这么一个轮子，至少增加了自己对自定义View的理解和动手能力。
造轮子之路，还需坚持。