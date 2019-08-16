<p align="center"><img src="http://i64.tinypic.com/2ak9sox.png"></p>

CircularProgressBar
=================

<img src="/preview/preview.gif" alt="sample" title="sample" width="300" height="480" align="right" vspace="24" />

[![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)
[![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=14)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/1f1cf02d760848af8c6b63e7bb0a1db8)](https://app.codacy.com/app/lopspower/CircularProgressBar?utm_source=github.com&utm_medium=referral&utm_content=lopspower/CircularProgressBar&utm_campaign=Badge_Grade_Dashboard)
<br>
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-CircularProgressBar-lightgrey.svg?style=flat)](https://android-arsenal.com/details/1/2845)
[![Twitter](https://img.shields.io/badge/Twitter-@LopezMikhael-blue.svg?style=flat)](http://twitter.com/lopezmikhael)

This is an Android project allowing to realize a circular ProgressBar in the simplest way possible.

<a href="https://play.google.com/store/apps/details?id=com.mikhaellopez.lopspower">
  <img alt="Android app on Google Play" src="https://developer.android.com/images/brand/en_app_rgb_wo_45.png" />
</a>

USAGE
-----

To make a circular ProgressBar add CircularProgressBar in your layout XML and add CircularProgressBar library in your project or you can also grab it via Gradle:

```groovy
implementation 'com.mikhaellopez:circularprogressbar:3.0.0'
```

XML
-----

```xml
<com.mikhaellopez.circularprogressbar.CircularProgressBar
    android:id="@+id/circularProgressBar"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:cpb_background_progressbar_color="#b6bbd8"
    app:cpb_background_progressbar_width="5dp"
    app:cpb_progress_direction="to_right"
    app:cpb_progressbar_color="#3f51b5"
    app:cpb_progressbar_width="10dp"
    app:cpb_round_border="false" />
```

You must use the following properties in your XML to change your CircularProgressBar.

| Properties                                       | Type                                                         | Default               |
| ------------------------------------------------ | ------------------------------------------------------------ | --------------------- |
| `app:cpb_progress`                               | integer                                                      | 0                     |
| `app:cpb_progress_max`                           | integer                                                      | 100                   |
| `app:cpb_indeterminate_mode`                     | boolean                                                      | false                 |
| `app:cpb_progressbar_color`                      | color                                                        | BLACK                 |
| `app:cpb_progressbar_color_start`                | color                                                        | cpb_progressbar_color |
| `app:cpb_progressbar_color_end`                  | color                                                        | cpb_progressbar_color |
| `app:cpb_progressbar_color_direction`            | left_to_right, right_to_left, top_to_bottom or bottom_to_top | left_to_right         |
| `app:cpb_progressbar_width`                      | dimension                                                    | 7dp                   |
| `app:cpb_background_progressbar_color`           | color                                                        | GRAY                  |
| `app:cpb_background_progressbar_color_start`     | color                                                        | GRAY                  |
| `app:cpb_background_progressbar_color_end`       | color                                                        | GRAY                  |
| `app:cpb_background_progressbar_color_direction` | left_to_right, right_to_left, top_to_bottom or bottom_to_top | left_to_right         |
| `app:cpb_background_progressbar_width`           | dimension                                                    | 3dp                   |
| `app:cpb_round_border`                           | boolean                                                      | false                 |
| `app:cpb_start_angle`                            | float                                                        | 0f (=top)             |
| `app:cpb_progress_direction`                     | to_right or to_left                                          | to_right              |

KOTLIN
-----

```kotlin
val circularProgressBar = findViewById<CircularProgressBar>(R.id.yourCircularProgressbar)
circularProgressBar.apply {
    // Set Progress
    progress = 65f
    // or with animation
    setProgressWithAnimation(65f, 1000) // =1s, Default duration = 1500ms

    // Set Progress Max
    progressMax = 200f

    // Set ProgressBar Color
    progressBarColor = Color.BLACK
    // or with gradient
    progressBarColorStart = Color.GRAY
    progressBarColorEnd = Color.RED
    progressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM

    // Set background ProgressBar Color
    backgroundProgressBarColor = Color.GRAY
    // or with gradient
    backgroundProgressBarColorStart = Color.WHITE
    backgroundProgressBarColorEnd = Color.RED
    backgroundProgressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM

    // Set Width
    progressBarWidth = 7f // in DP
    backgroundProgressBarWidth = 3f // in DP

    // Other
    roundBorder = true
    startAngle = 180f
    progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
}
```

### Listener

```kotlin
circularProgressBar.onProgressChangeListener = { progress ->
    // Do something
}

circularProgressBar.onIndeterminateModeChangeListener = { isEnable ->
    // Do something
}
```

JAVA
-----

```java
CircularProgressBar circularProgressBar = (CircularProgressBar)findViewById(R.id.yourCircularProgressbar);
// Set Progress
circularProgressBar.setProgress(65f);
// or with animation
circularProgressBar.setProgressWithAnimation(65f, 1000); // =1s, Default duration = 1500ms

// Set Progress Max
circularProgressBar.setProgressMax(200f);

// Set ProgressBar Color
circularProgressBar.setProgressBarColor(Color.BLACK);
// or with gradient
circularProgressBar.setProgressBarColorStart(Color.GRAY);
circularProgressBar.setProgressBarColorEnd(Color.RED);
circularProgressBar.setProgressBarColorDirection(CircularProgressBar.GradientDirection.TOP_TO_BOTTOM);

// Set background ProgressBar Color
circularProgressBar.setBackgroundProgressBarColor(Color.GRAY);
// or with gradient
circularProgressBar.setBackgroundProgressBarColorStart(Color.WHITE);
circularProgressBar.setBackgroundProgressBarColorEnd(Color.RED);
circularProgressBar.setBackgroundProgressBarColorDirection(CircularProgressBar.GradientDirection.TOP_TO_BOTTOM);

// Set Width
circularProgressBar.setProgressBarWidth(7f); // in DP
circularProgressBar.setBackgroundProgressBarWidth(3f); // in DP

// Other
circularProgressBar.setRoundBorder(true);
circularProgressBar.setStartAngle(180f);
circularProgressBar.setProgressDirection(CircularProgressBar.ProgressDirection.TO_RIGHT);
```

### Listener

```java
circularProgressBar.setOnIndeterminateModeChangeListener(isEnable -> {
    // Do something
    return Unit.INSTANCE;
});

 circularProgressBar.setOnProgressChangeListener(progress -> {
    // Do something
    return Unit.INSTANCE;
});
```

LICENCE
-----

CircularProgressBar by [Lopez Mikhael](http://mikhaellopez.com/) is licensed under a [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).
Based on a work at [Pedramrn/CircularProgressBar](https://github.com/Pedramrn/CircularProgressBar).
