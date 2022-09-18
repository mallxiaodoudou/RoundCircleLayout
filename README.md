## RoundCircleLayout

可以支持圆角与圆形裁剪的RoundCircleLayout。基于Outline与Shader实现，无需配置硬件加速，无兼容性问题。

依赖方法：
```
implementation "io.github.liukai2530533:round_circle_imageview:1.0.1"
```

#### 如何使用：

自定义属性如下：
```xml
    <attr name="is_circle" format="boolean" />
    <attr name="round_radius" format="dimension" />
    <attr name="topLeft" format="dimension" />
    <attr name="topRight" format="dimension" />
    <attr name="bottomLeft" format="dimension" />
    <attr name="bottomRight" format="dimension" />
    <attr name="round_circle_background_color" format="color" />
    <attr name="round_circle_background_drawable" format="reference" />
    <attr name="is_bg_center_crop" format="boolean" />
```

在xml中使用如下：
```xml
          <com.newki.round_circle_layout.RoundCircleConstraintLayout
                android:id="@+id/layout_2"
                android:layout_width="@dimen/d_150dp"
                android:layout_height="@dimen/d_150dp"
                android:layout_marginTop="@dimen/d_10dp"
                app:is_circle="true"
                app:round_circle_background_color="#ff00ff"
                app:round_radius="@dimen/d_40dp"/>


          <com.newki.round_circle_layout.RoundCircleLinearLayout
                android:id="@+id/layout_3"
                android:layout_width="@dimen/d_150dp"
                android:layout_height="@dimen/d_150dp"
                android:layout_marginTop="@dimen/d_10dp"
                app:bottomRight="@dimen/d_20dp"
                app:round_circle_background_drawable="@drawable/chengxiao"
                app:topLeft="@dimen/d_20dp"
                app:topRight="@dimen/d_40dp"/>

           <com.newki.round_circle_layout.RoundCircleConstraintLayout
                android:layout_width="@dimen/d_150dp"
                android:layout_height="@dimen/d_150dp"
                android:layout_marginTop="@dimen/d_10dp"
                app:is_bg_center_crop="false"
                app:is_circle="true"
                app:round_circle_background_drawable="@drawable/chengxiao">
```

效果如下：

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/434639cbc3514107a103866308d88232~tplv-k3u1fbpfcp-zoom-1.image)


同时也支持代码中设置：

```kotlin
      val layout_3 = findViewById<RoundCircleLinearLayout>(R.id.layout_3)

        findViewById<ViewGroup>(R.id.layout_2).setOnClickListener {

            val drawable = resources.getDrawable(R.drawable.chengxiao)

            it.background = drawable

            layout_3.background = drawable
        }
```


效果：

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/8b037e40c18a48b9bf70a8617ca857ae~tplv-k3u1fbpfcp-zoom-1.image)


具体的实现可以参考我的博客：

https://juejin.cn/post/7143410101951397919