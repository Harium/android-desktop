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
package android.view;

import java.util.ArrayList;

/**
 * <p>
 * A <code>ViewGroup</code> is a special view that can contain other views
 * (called children.) The view group is the base class for layouts and views
 * containers. This class also defines the
 * {@link android.view.ViewGroup.LayoutParams} class which serves as the base
 * class for layouts parameters.
 * </p>
 * <p>
 * <p>
 * Also see {@link LayoutParams} for layout attributes.
 * </p>
 * <p>
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For more information about creating user interface layouts, read the
 * <a href="{@docRoot}guide/topics/ui/declaring-layout.html">XML Layouts</a> developer
 * guide.</p></div>
 * <p>
 * <p>Here is a complete implementation of a custom ViewGroup that implements
 * a simple {@link android.widget.FrameLayout} along with the ability to stack
 * children in left and right gutters.</p>
 * <p>
 * {@sample development/samples/ApiDemos/src/com/example/android/apis/view/CustomLayout.java
 * Complete}
 * <p>
 * <p>If you are implementing XML layout attributes as shown in the example, this is the
 * corresponding definition for them that would go in <code>res/values/attrs.xml</code>:</p>
 * <p>
 * {@sample development/samples/ApiDemos/res/values/attrs.xml CustomLayout}
 * <p>
 * <p>Finally the layout manager can be used in an XML layout like so:</p>
 * <p>
 * {@sample development/samples/ApiDemos/res/layout/custom_layout.xml Complete}
 *
 * @attr ref android.R.styleable#ViewGroup_clipChildren
 * @attr ref android.R.styleable#ViewGroup_clipToPadding
 * @attr ref android.R.styleable#ViewGroup_layoutAnimation
 * @attr ref android.R.styleable#ViewGroup_animationCache
 * @attr ref android.R.styleable#ViewGroup_persistentDrawingCache
 * @attr ref android.R.styleable#ViewGroup_alwaysDrawnWithCache
 * @attr ref android.R.styleable#ViewGroup_addStatesFromChildren
 * @attr ref android.R.styleable#ViewGroup_descendantFocusability
 * @attr ref android.R.styleable#ViewGroup_animateLayoutChanges
 * @attr ref android.R.styleable#ViewGroup_splitMotionEvents
 * @attr ref android.R.styleable#ViewGroup_layoutMode
 */
public abstract class ViewGroup extends View {
    private static final String TAG = "ViewGroup";
    private static final boolean DBG = false;
    /**
     * Views which have been hidden or removed which need to be animated on
     * their way out.
     * This field should be made private, so it is hidden from the SDK.
     * {@hide}
     */
    protected ArrayList<View> mDisappearingChildren;

    // The view contained within this ViewGroup that has or contains focus.
    private View mFocused;
    // The view contained within this ViewGroup (excluding nested keyboard navigation clusters)
    // that is or contains a default-focus view.
    private View mDefaultFocus;
    // The last child of this ViewGroup which held focus within the current cluster
    View mFocusedInCluster;

    //[...]

    // For debugging only.  You can see these in hierarchyviewer.
    @SuppressWarnings({"FieldCanBeLocal", "UnusedDeclaration"})
    //@ViewDebug.ExportedProperty(category = "events")
    private long mLastTouchDownTime;
    //@ViewDebug.ExportedProperty(category = "events")
    private int mLastTouchDownIndex = -1;
    @SuppressWarnings({"FieldCanBeLocal", "UnusedDeclaration"})
    //@ViewDebug.ExportedProperty(category = "events")
    private float mLastTouchDownX;
    @SuppressWarnings({"FieldCanBeLocal", "UnusedDeclaration"})
    //@ViewDebug.ExportedProperty(category = "events")
    private float mLastTouchDownY;

    // True if the view group itself received a hover event.
    // It might not have actually handled the hover event.
    private boolean mHoveredSelf;
    // The child capable of showing a tooltip and currently under the pointer.
    private View mTooltipHoverTarget;
    // True if the view group is capable of showing a tooltip and the pointer is directly
    // over the view group but not one of its child views.
    private boolean mTooltipHoveredSelf;

    protected int mGroupFlags;
    /**
     * Either {@link #LAYOUT_MODE_CLIP_BOUNDS} or {@link #LAYOUT_MODE_OPTICAL_BOUNDS}.
     */
    private int mLayoutMode = LAYOUT_MODE_UNDEFINED;
    /**
     * NOTE: If you change the flags below make sure to reflect the changes
     * the DisplayList class
     */
    // When set, ViewGroup invalidates only the child's rectangle
    // Set by default
    static final int FLAG_CLIP_CHILDREN = 0x1;
    // When set, ViewGroup excludes the padding area from the invalidate rectangle
    // Set by default
    private static final int FLAG_CLIP_TO_PADDING = 0x2;
    // When set, dispatchDraw() will invoke invalidate(); this is set by drawChild() when
    // a child needs to be invalidated and FLAG_OPTIMIZE_INVALIDATE is set
    static final int FLAG_INVALIDATE_REQUIRED = 0x4;
    // When set, dispatchDraw() will run the layout animation and unset the flag
    private static final int FLAG_RUN_ANIMATION = 0x8;
    // When set, there is either no layout animation on the ViewGroup or the layout
    // animation is over
    // Set by default
    static final int FLAG_ANIMATION_DONE = 0x10;
    // If set, this ViewGroup has padding; if unset there is no padding and we don't need
    // to clip it, even if FLAG_CLIP_TO_PADDING is set
    private static final int FLAG_PADDING_NOT_NULL = 0x20;
    /**
     * @deprecated - functionality removed
     */
    @Deprecated
    private static final int FLAG_ANIMATION_CACHE = 0x40;
    // When set, this ViewGroup converts calls to invalidate(Rect) to invalidate() during a
    // layout animation; this avoid clobbering the hierarchy
    // Automatically set when the layout animation starts, depending on the animation's
    // characteristics
    static final int FLAG_OPTIMIZE_INVALIDATE = 0x80;
    // When set, the next call to drawChild() will clear mChildTransformation's matrix
    static final int FLAG_CLEAR_TRANSFORMATION = 0x100;
    // When set, this ViewGroup invokes mAnimationListener.onAnimationEnd() and removes
    // the children's Bitmap caches if necessary
    // This flag is set when the layout animation is over (after FLAG_ANIMATION_DONE is set)
    private static final int FLAG_NOTIFY_ANIMATION_LISTENER = 0x200;
    /**
     * When set, the drawing method will call {@link #getChildDrawingOrder(int, int)}
     * to get the index of the child to draw for that iteration.
     *
     * @hide
     */
    protected static final int FLAG_USE_CHILD_DRAWING_ORDER = 0x400;
    /**
     * When set, this ViewGroup supports static transformations on children; this causes
     * {@link #getChildStaticTransformation(View, android.view.animation.Transformation)} to be
     * invoked when a child is drawn.
     * <p>
     * Any subclass overriding
     * {@link #getChildStaticTransformation(View, android.view.animation.Transformation)} should
     * set this flags in {@link #mGroupFlags}.
     * <p>
     * {@hide}
     */
    protected static final int FLAG_SUPPORT_STATIC_TRANSFORMATIONS = 0x800;
    // UNUSED FLAG VALUE: 0x1000;
    /**
     * When set, this ViewGroup's drawable states also include those
     * of its children.
     */
    private static final int FLAG_ADD_STATES_FROM_CHILDREN = 0x2000;
    /**
     * @deprecated functionality removed
     */
    @Deprecated
    private static final int FLAG_ALWAYS_DRAWN_WITH_CACHE = 0x4000;
    /**
     * @deprecated functionality removed
     */
    @Deprecated
    private static final int FLAG_CHILDREN_DRAWN_WITH_CACHE = 0x8000;
    /**
     * When set, this group will go through its list of children to notify them of
     * any drawable state change.
     */
    private static final int FLAG_NOTIFY_CHILDREN_ON_DRAWABLE_STATE_CHANGE = 0x10000;
    private static final int FLAG_MASK_FOCUSABILITY = 0x60000;
    /**
     * This view will get focus before any of its descendants.
     */
    public static final int FOCUS_BEFORE_DESCENDANTS = 0x20000;
    /**
     * This view will get focus only if none of its descendants want it.
     */
    public static final int FOCUS_AFTER_DESCENDANTS = 0x40000;
    /**
     * This view will block any of its descendants from getting focus, even
     * if they are focusable.
     */
    public static final int FOCUS_BLOCK_DESCENDANTS = 0x60000;
    /**
     * Used to map between enum in attrubutes and flag values.
     */
    private static final int[] DESCENDANT_FOCUSABILITY_FLAGS =
            {FOCUS_BEFORE_DESCENDANTS, FOCUS_AFTER_DESCENDANTS,
                    FOCUS_BLOCK_DESCENDANTS};
    /**
     * When set, this ViewGroup should not intercept touch events.
     * {@hide}
     */
    protected static final int FLAG_DISALLOW_INTERCEPT = 0x80000;
    /**
     * When set, this ViewGroup will split MotionEvents to multiple child Views when appropriate.
     */
    private static final int FLAG_SPLIT_MOTION_EVENTS = 0x200000;
    /**
     * When set, this ViewGroup will not dispatch onAttachedToWindow calls
     * to children when adding new views. This is used to prevent multiple
     * onAttached calls when a ViewGroup adds children in its own onAttached method.
     */
    private static final int FLAG_PREVENT_DISPATCH_ATTACHED_TO_WINDOW = 0x400000;
    /**
     * When true, indicates that a layoutMode has been explicitly set, either with
     * an explicit call to {@link #setLayoutMode(int)} in code or from an XML resource.
     * This distinguishes the situation in which a layout mode was inherited from
     * one of the ViewGroup's ancestors and cached locally.
     */
    private static final int FLAG_LAYOUT_MODE_WAS_EXPLICITLY_SET = 0x800000;
    static final int FLAG_IS_TRANSITION_GROUP = 0x1000000;
    static final int FLAG_IS_TRANSITION_GROUP_SET = 0x2000000;
    /**
     * When set, focus will not be permitted to enter this group if a touchscreen is present.
     */
    static final int FLAG_TOUCHSCREEN_BLOCKS_FOCUS = 0x4000000;
    /**
     * When true, indicates that a call to startActionModeForChild was made with the type parameter
     * and should not be ignored. This helps in backwards compatibility with the existing method
     * without a type.
     *
     * @see #startActionModeForChild(View, android.view.ActionMode.Callback)
     * @see #startActionModeForChild(View, android.view.ActionMode.Callback, int)
     */
    private static final int FLAG_START_ACTION_MODE_FOR_CHILD_IS_TYPED = 0x8000000;
    /**
     * When true, indicates that a call to startActionModeForChild was made without the type
     * parameter. This helps in backwards compatibility with the existing method
     * without a type.
     *
     * @see #startActionModeForChild(View, android.view.ActionMode.Callback)
     * @see #startActionModeForChild(View, android.view.ActionMode.Callback, int)
     */
    private static final int FLAG_START_ACTION_MODE_FOR_CHILD_IS_NOT_TYPED = 0x10000000;
    /**
     * When set, indicates that a call to showContextMenuForChild was made with explicit
     * coordinates within the initiating child view.
     */
    private static final int FLAG_SHOW_CONTEXT_MENU_WITH_COORDS = 0x20000000;
    /**
     * Indicates which types of drawing caches are to be kept in memory.
     * This field should be made private, so it is hidden from the SDK.
     * {@hide}
     */
    protected int mPersistentDrawingCache;
    /**
     * Used to indicate that no drawing cache should be kept in memory.
     */
    public static final int PERSISTENT_NO_CACHE = 0x0;
    /**
     * Used to indicate that the animation drawing cache should be kept in memory.
     */
    public static final int PERSISTENT_ANIMATION_CACHE = 0x1;
    /**
     * Used to indicate that the scrolling drawing cache should be kept in memory.
     */
    public static final int PERSISTENT_SCROLLING_CACHE = 0x2;
    /**
     * Used to indicate that all drawing caches should be kept in memory.
     */
    public static final int PERSISTENT_ALL_CACHES = 0x3;
    // Layout Modes
    private static final int LAYOUT_MODE_UNDEFINED = -1;
    /**
     * This constant is a {@link #setLayoutMode(int) layoutMode}.
     * Clip bounds are the raw values of {@link #getLeft() left}, {@link #getTop() top},
     * {@link #getRight() right} and {@link #getBottom() bottom}.
     */
    public static final int LAYOUT_MODE_CLIP_BOUNDS = 0;
    /**
     * This constant is a {@link #setLayoutMode(int) layoutMode}.
     * Optical bounds describe where a widget appears to be. They sit inside the clip
     * bounds which need to cover a larger area to allow other effects,
     * such as shadows and glows, to be drawn.
     */
    public static final int LAYOUT_MODE_OPTICAL_BOUNDS = 1;
    /**
     * @hide
     */
    public static int LAYOUT_MODE_DEFAULT = LAYOUT_MODE_CLIP_BOUNDS;
    /**
     * We clip to padding when FLAG_CLIP_TO_PADDING and FLAG_PADDING_NOT_NULL
     * are set at the same time.
     */
    protected static final int CLIP_TO_PADDING_MASK = FLAG_CLIP_TO_PADDING | FLAG_PADDING_NOT_NULL;
    // Index of the child's left position in the mLocation array
    private static final int CHILD_LEFT_INDEX = 0;
    // Index of the child's top position in the mLocation array
    private static final int CHILD_TOP_INDEX = 1;
    // Child views of this ViewGroup
    private View[] mChildren;
    // Number of valid children in the mChildren array, the rest should be null or not
    // considered as children
    private int mChildrenCount;
    // Whether layout calls are currently being suppressed, controlled by calls to
    // suppressLayout()
    boolean mSuppressLayout = false;
    // Whether any layout calls have actually been suppressed while mSuppressLayout
    // has been true. This tracks whether we need to issue a requestLayout() when
    // layout is later re-enabled.
    private boolean mLayoutCalledWhileSuppressed = false;
    private static final int ARRAY_INITIAL_CAPACITY = 12;
    private static final int ARRAY_CAPACITY_INCREMENT = 12;
    private static float[] sDebugLines;

    // The set of views that are currently being transitioned. This list is used to track views
    // being removed that should not actually be removed from the parent yet because they are
    // being animated.
    private ArrayList<View> mTransitioningViews;
    // List of children changing visibility. This is used to potentially keep rendering
    // views during a transition when they otherwise would have become gone/invisible
    private ArrayList<View> mVisibilityChangingChildren;
    // Temporary holder of presorted children, only used for
    // input/software draw dispatch for correctly Z ordering.
    private ArrayList<View> mPreSortedChildren;
    // Indicates how many of this container's child subtrees contain transient state

    /**
     * LayoutParams are used by views to tell their parents how they want to be
     * laid out. See
     * {@link android.R.styleable#ViewGroup_Layout ViewGroup Layout Attributes}
     * for a list of all child view attributes that this class supports.
     * <p>
     * <p>
     * The base LayoutParams class just describes how big the view wants to be
     * for both width and height. For each dimension, it can specify one of:
     * <ul>
     * <li>FILL_PARENT (renamed MATCH_PARENT in API Level 8 and higher), which
     * means that the view wants to be as big as its parent (minus padding)
     * <li> WRAP_CONTENT, which means that the view wants to be just big enough
     * to enclose its content (plus padding)
     * <li> an exact number
     * </ul>
     * There are subclasses of LayoutParams for different subclasses of
     * ViewGroup. For example, AbsoluteLayout has its own subclass of
     * LayoutParams which adds an X and Y value.</p>
     * <p>
     * <div class="special reference">
     * <h3>Developer Guides</h3>
     * <p>For more information about creating user interface layouts, read the
     * <a href="{@docRoot}guide/topics/ui/declaring-layout.html">XML Layouts</a> developer
     * guide.</p></div>
     *
     * @attr ref android.R.styleable#ViewGroup_Layout_layout_height
     * @attr ref android.R.styleable#ViewGroup_Layout_layout_width
     */
    public static class LayoutParams {
        /**
         * Special value for the height or width requested by a View.
         * FILL_PARENT means that the view wants to be as big as its parent,
         * minus the parent's padding, if any. This value is deprecated
         * starting in API Level 8 and replaced by {@link #MATCH_PARENT}.
         */
        @SuppressWarnings({"UnusedDeclaration"})
        @Deprecated
        public static final int FILL_PARENT = -1;
        /**
         * Special value for the height or width requested by a View.
         * MATCH_PARENT means that the view wants to be as big as its parent,
         * minus the parent's padding, if any. Introduced in API Level 8.
         */
        public static final int MATCH_PARENT = -1;
        /**
         * Special value for the height or width requested by a View.
         * WRAP_CONTENT means that the view wants to be just large enough to fit
         * its own internal content, taking its own padding into account.
         */
        public static final int WRAP_CONTENT = -2;
        /**
         * Information about how wide the view wants to be. Can be one of the
         * constants FILL_PARENT (replaced by MATCH_PARENT
         * in API Level 8) or WRAP_CONTENT, or an exact size.
         */
        public int width;
        /**
         * Information about how tall the view wants to be. Can be one of the
         * constants FILL_PARENT (replaced by MATCH_PARENT
         * in API Level 8) or WRAP_CONTENT, or an exact size.
         */
        @ViewDebug.ExportedProperty(category = "layout", mapping = {
                @ViewDebug.IntToString(from = MATCH_PARENT, to = "MATCH_PARENT"),
                @ViewDebug.IntToString(from = WRAP_CONTENT, to = "WRAP_CONTENT")
        })
        public int height;

        /**
         * Creates a new set of layout parameters. The values are extracted from
         * the supplied attributes set and context. The XML attributes mapped
         * to this set of layout parameters are:
         *
         * <ul>
         *   <li><code>layout_width</code>: the width, either an exact value,
         *   {@link #WRAP_CONTENT}, or {@link #FILL_PARENT} (replaced by
         *   {@link #MATCH_PARENT} in API Level 8)</li>
         *   <li><code>layout_height</code>: the height, either an exact value,
         *   {@link #WRAP_CONTENT}, or {@link #FILL_PARENT} (replaced by
         *   {@link #MATCH_PARENT} in API Level 8)</li>
         * </ul>
         *
         * @param c the application environment
         * @param attrs the set of attributes from which to extract the layout
         *              parameters' values
         */
        /*public LayoutParams(Context c, AttributeSet attrs) {

        }*/

        /**
         * Creates a new set of layout parameters with the specified width
         * and height.
         *
         * @param width  the width, either {@link #WRAP_CONTENT},
         *               {@link #FILL_PARENT} (replaced by {@link #MATCH_PARENT} in
         *               API Level 8), or a fixed size in pixels
         * @param height the height, either {@link #WRAP_CONTENT},
         *               {@link #FILL_PARENT} (replaced by {@link #MATCH_PARENT} in
         *               API Level 8), or a fixed size in pixels
         */
        public LayoutParams(int width, int height) {
            this.width = width;
            this.height = height;
        }

        /**
         * Copy constructor. Clones the width and height values of the source.
         *
         * @param source The layout params to copy from.
         */
        public LayoutParams(LayoutParams source) {
            this.width = source.width;
            this.height = source.height;
        }

        /**
         * Used internally by MarginLayoutParams.
         *
         * @hide
         */
        LayoutParams() {
        }

        /**
         * Resolve layout parameters depending on the layout direction. Subclasses that care about
         * layoutDirection changes should override this method. The default implementation does
         * nothing.
         *
         * @param layoutDirection the direction of the layout
         *                        <p>
         *                        {@link View#LAYOUT_DIRECTION_LTR}
         *                        {@link View#LAYOUT_DIRECTION_RTL}
         */
        public void resolveLayoutDirection(int layoutDirection) {
        }

        /**
         * Returns a String representation of this set of layout parameters.
         *
         * @param output the String to prepend to the internal representation
         * @return a String with the following format: output +
         * "ViewGroup.LayoutParams={ width=WIDTH, height=HEIGHT }"
         * @hide
         */
        public String debug(String output) {
            return output + "ViewGroup.LayoutParams={ width="
                    + sizeToString(width) + ", height=" + sizeToString(height) + " }";
        }

        /**
         * Converts the specified size to a readable String.
         *
         * @param size the size to convert
         * @return a String instance representing the supplied size
         * @hide
         */
        protected static String sizeToString(int size) {
            if (size == WRAP_CONTENT) {
                return "wrap-content";
            }
            if (size == MATCH_PARENT) {
                return "match-parent";
            }
            return String.valueOf(size);
        }
    }
}