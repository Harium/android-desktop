/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.widget;

import android.annotation.NonNull;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;

import java.util.ArrayList;
/**
 * FrameLayout is designed to block out an area on the screen to display
 * a single item. Generally, FrameLayout should be used to hold a single child view, because it can
 * be difficult to organize child views in a way that's scalable to different screen sizes without
 * the children overlapping each other. You can, however, add multiple children to a FrameLayout
 * and control their position within the FrameLayout by assigning gravity to each child, using the
 * <a href="FrameLayout.LayoutParams.html#attr_android:layout_gravity">{@code
 * android:layout_gravity}</a> attribute.
 * <p>Child views are drawn in a stack, with the most recently added child on top.
 * The size of the FrameLayout is the size of its largest child (plus padding), visible
 * or not (if the FrameLayout's parent permits). Views that are {@link android.view.View#GONE} are
 * used for sizing
 * only if {@link #setMeasureAllChildren(boolean) setConsiderGoneChildrenWhenMeasuring()}
 * is set to true.
 *
 * @attr ref android.R.styleable#FrameLayout_measureAllChildren
 */
public class FrameLayout extends ViewGroup {
    private static final int DEFAULT_CHILD_GRAVITY = Gravity.TOP | Gravity.START;
    @ViewDebug.ExportedProperty(category = "measurement")
    boolean mMeasureAllChildren = false;
    @ViewDebug.ExportedProperty(category = "padding")
    private int mForegroundPaddingLeft = 0;
    @ViewDebug.ExportedProperty(category = "padding")
    private int mForegroundPaddingTop = 0;
    @ViewDebug.ExportedProperty(category = "padding")
    private int mForegroundPaddingRight = 0;
    @ViewDebug.ExportedProperty(category = "padding")
    private int mForegroundPaddingBottom = 0;
    private final ArrayList<View> mMatchParentChildren = new ArrayList<>(1);
    public FrameLayout(@NonNull Context context) {
        //super(context);
    }

    /**
     * Per-child layout information for layouts that support margins.
     * See {@link android.R.styleable#FrameLayout_Layout FrameLayout Layout Attributes}
     * for a list of all child view attributes that this class supports.
     *
     * @attr ref android.R.styleable#FrameLayout_Layout_layout_gravity
     */
    public static class LayoutParams {
        /**
         * Value for {@link #gravity} indicating that a gravity has not been
         * explicitly specified.
         */
        public static final int UNSPECIFIED_GRAVITY = -1;
        /**
         * The gravity to apply with the View to which these layout parameters
         * are associated.
         * <p>
         * The default value is {@link #UNSPECIFIED_GRAVITY}, which is treated
         * by FrameLayout as {@code Gravity.TOP | Gravity.START}.
         *
         * @see android.view.Gravity
         * @attr ref android.R.styleable#FrameLayout_Layout_layout_gravity
         */
        public int gravity = UNSPECIFIED_GRAVITY;

        public LayoutParams(int width, int height) {
            //super(width, height);
        }
        /**
         * Creates a new set of layout parameters with the specified width, height
         * and weight.
         *
         * @param width the width, either {@link #MATCH_PARENT},
         *              {@link #WRAP_CONTENT} or a fixed size in pixels
         * @param height the height, either {@link #MATCH_PARENT},
         *               {@link #WRAP_CONTENT} or a fixed size in pixels
         * @param gravity the gravity
         *
         * @see android.view.Gravity
         */
        public LayoutParams(int width, int height, int gravity) {
            //super(width, height);
            this.gravity = gravity;
        }
        public LayoutParams(@NonNull ViewGroup.LayoutParams source) {
            //super(source);
        }

        /**
         * Copy constructor. Clones the width, height, margin values, and
         * gravity of the source.
         *
         * @param source The layout params to copy from.
         */
        public LayoutParams(@NonNull LayoutParams source) {
            //super(source);
            this.gravity = source.gravity;
        }
    }
}
