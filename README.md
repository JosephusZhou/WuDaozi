# WuDaozi

[![](https://jitpack.io/v/JosephusZhou/WuDaozi.svg)](https://jitpack.io/#JosephusZhou/WuDaozi)

Android image selector, written with Kotlin, inspired by [zhihu/Matisse](https://github.com/zhihu/Matisse).

## Features

- Select images in Activity or Fragment
- Select images with size filter and type filter
- Support custom styles

## How to use

You can copy the source code and do whaterver you want to do.

Or use Gradle.

**Step 1.** Add [JitPack](https://jitpack.io) repository in your root `build.gradle`

```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

**Step 2.** Add the dependency in your app `build.gradle`

```groovy
dependencies {
	implementation 'com.github.JosephusZhou:WuDaozi:1.0.2'
}
```

**Step 3.** Request permission

- `android.permission.READ_EXTERNAL_STORAGE`

**Step 4.** Start to select images from `Activity` or `Fragment`

```kotlin
WuDaozi.with(this)
        .theme(R.style.CustomWuDaoziTheme)
        .imageLoader(GlideLoader())
        .columnsCount(4)
        .maxSelectableCount(9)
        .filter(minByteSize = 1024 * 10, selectedTypes = arrayOf(Filter.Type.JPG))
        .start()
```

**Step 5.** Receive result in `onActivityResult()`

```kotlin
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            WuDaozi.REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val bundle = data.extras
                    bundle?.let {
                        val uriList: ArrayList<Uri> = it.getParcelableArrayList<Uri>(WuDaozi.BUNDLE_KEY) as ArrayList<Uri>
                        for(uri in uriList) {
                            Log.e("WuDaozi", "->$uri")
                        }
                    }
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }
```

## How to customize

- `theme()`: UI theme
- `imageLoader()`: the image engine to load images
- `columnsCount()`: how many images can be displayed in one line
- `maxSelectableCount()`: how many images can be selected at most
- `filter()`: filter images of specific sizes and types

#### ImageLoader

Implement the `ImageLoader` interface and all methods.

####  Theme

Build your own theme base on `WuDaozi.Theme`.

## License

```
Copyright 2019 JosephusZhou

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```