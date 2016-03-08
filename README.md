<p align="center"><img src="http://i64.tinypic.com/2ak9sox.png"></p>

CircularProgressBar
=================
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-CircularProgressBar-lightgrey.svg?style=flat)](https://android-arsenal.com/details/1/2845)
[![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)
[![API](https://img.shields.io/badge/API-11%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=11)
[![Twitter](https://img.shields.io/badge/Twitter-@LopezMikhael-blue.svg?style=flat)](http://twitter.com/lopezmikhael)

This is an Android project allowing to realize a circular ProgressBar in the simplest way possible.

<img src="/preview/preview.gif" alt="sample" title="sample" width="300" height="448" align="right" vspace="24" />

USAGE
-----

To make a circular ProgressBar add CircularProgressBar in your layout XML and add CircularProgressBar library in your project or you can also grab it via Gradle:

```groovy
compile 'com.mikhaellopez:circularprogressbar:1.1.1'
```

XML
-----

```xml
<com.mikhaellopez.circularprogressbar.CircularProgressBar
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:cpb_background_progressbar_color="#FFCDD2"
    app:cpb_background_progressbar_width="5dp"
    app:cpb_progressbar_color="#F44336"
    app:cpb_progressbar_width="10dp" />
```

You must use the following properties in your XML to change your CircularProgressBar.


#####Properties:

* `app:cpb_progress`                      (integer)   -> default 0
* `app:cpb_progressbar_color`             (color)     -> default BLACK
* `app:cpb_background_progressbar_color`  (color)     -> default GRAY
* `app:cpb_progressbar_width`             (dimension) -> default 7dp
* `app:cpb_background_progressbar_width`  (dimension) -> default 3dp


JAVA
-----

```java
CircularProgressBar circularProgressBar = (CircularProgressBar)findViewById(R.id.yourCircularProgressbar);
circularProgressBar.setColor(ContextCompat.getColor(this, R.color.progressBarColor));
circularProgressBar.setBackgroundColor(ContextCompat.getColor(this, R.color.backgroundProgressBarColor));
circularProgressBar.setProgressBarWidth(getResources().getDimension(R.dimen.progressBarWidth));
circularProgressBar.setBackgroundProgressBarWidth(getResources().getDimension(R.dimen.backgroundProgressBarWidth));
int animationDuration = 2500; // 2500ms = 2,5s
circularProgressBar.setProgressWithAnimation(65, animationDuration); // Default duration = 1500ms
```

LICENCE
-----

CircularProgressBar by [Lopez Mikhael](http://mikhaellopez.com/) is licensed under a [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).
Based on a work at https://github.com/Pedramrn/CircularProgressBar.
