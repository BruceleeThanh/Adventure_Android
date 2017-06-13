package studio.crazybt.adventure.utils;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import studio.crazybt.adventure.R;

/**
 * Created by Brucelee Thanh on 03/01/2017.
 */

public class BadgeTabLayout extends TabLayout {
    private final SparseArray<Builder> mTabBuilders = new SparseArray<>();

    public BadgeTabLayout(Context context) {
        super(context);
    }

    public BadgeTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BadgeTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /*Use for tab icon*/
    public Builder with(int position) {
        Tab tab = getTabAt(position);
        return with(tab);
    }

    public Builder with(int position, String title){
        Tab tab = getTabAt(position);
        return with(tab, title);
    }

    public Builder with(int position, int titleResId){
        Tab tab = getTabAt(position);
        return with(tab, getResources().getString(titleResId));
    }

    /**
     * Apply a builder for this tab (icon).
     *
     * @param tab for which we create a new builder or retrieve its builder if existed.
     * @return the required Builder.
     */
    public Builder with(Tab tab) {
        if (tab == null) {
            throw new IllegalArgumentException("Tab must not be null");
        }

        Builder builder = mTabBuilders.get(tab.getPosition());
        if (builder == null) {
            builder = new Builder(this, tab);
            mTabBuilders.put(tab.getPosition(), builder);
        }

        return builder;
    }

    /**
     * Apply a builder for this tab (title).
     *
     * @param tab for which we create a new builder or retrieve its builder if existed.
     * @param title for title of tab (non icon).
     * @return the required Builder.
     */
    public Builder with(Tab tab, String title) {
        if (tab == null) {
            throw new IllegalArgumentException("Tab must not be null");
        }

        Builder builder = mTabBuilders.get(tab.getPosition());
        if (builder == null) {
            builder = new Builder(this, tab, title);
            mTabBuilders.put(tab.getPosition(), builder);
        }

        return builder;
    }

    public static final class Builder {

        /**
         * This badge widget must not support this value.
         */
        private static final int INVALID_NUMBER = 0;

        @Nullable
        final View mView;
        final Context mContext;
        final TabLayout.Tab mTab;
        @Nullable
        TextView mBadgeTextView;
        @Nullable
        ImageView mIconView;
        Drawable mIconDrawable;
        Integer mIconColorFilter;
        int mBadgeCount = INVALID_NUMBER;

        TextView mTitle;

        boolean mHasBadge = false;

        /**
         * This construct take a TabLayout parent to have its context and other attributes sets. And
         * the tab whose icon will be updated.
         *
         * @param parent
         * @param tab
         */
        private Builder(TabLayout parent, @NonNull TabLayout.Tab tab) {
            super();
            this.mContext = parent.getContext();
            this.mTab = tab;
            // initialize current tab's custom view.
            if (tab.getCustomView() != null) {
                this.mView = tab.getCustomView();
            } else {
                this.mView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.tab_custom_icon, parent, false);
            }

            if (mView != null) {
                this.mIconView = (ImageView) mView.findViewById(R.id.tab_icon);
                this.mBadgeTextView = (TextView) mView.findViewById(R.id.tab_badge);
            }

            if (this.mBadgeTextView != null) {
                this.mHasBadge = mBadgeTextView.getVisibility() == View.VISIBLE;
                try {
                    this.mBadgeCount = Integer.parseInt(mBadgeTextView.getText().toString());
                } catch (NumberFormatException er) {
                    er.printStackTrace();
                    this.mBadgeCount = INVALID_NUMBER;
                }
            }

            if (this.mIconView != null) {
                mIconDrawable = mIconView.getDrawable();
            }
        }

        private Builder(TabLayout parent, @NonNull TabLayout.Tab tab, String title) {
            super();
            this.mContext = parent.getContext();
            this.mTab = tab;
            // initialize current tab's custom view.
            if (tab.getCustomView() != null) {
                this.mView = tab.getCustomView();
            } else {
                this.mView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.tab_custom_title, parent, false);
            }

            if (mView != null) {
                this.mTitle = (TextView) mView.findViewById(R.id.tab_title);
                this.mTitle.setText(title);
                this.mBadgeTextView = (TextView) mView.findViewById(R.id.tab_badge);
            }

            if (this.mBadgeTextView != null) {
                this.mHasBadge = mBadgeTextView.getVisibility() == View.VISIBLE;
                try {
                    this.mBadgeCount = Integer.parseInt(mBadgeTextView.getText().toString());
                } catch (NumberFormatException er) {
                    er.printStackTrace();
                    this.mBadgeCount = INVALID_NUMBER;
                }
            }

            if (this.mIconView != null) {
                mIconDrawable = mIconView.getDrawable();
            }
        }

        /**
         * The related Tab is about to have a badge
         *
         * @return this builder
         */
        public Builder hasBadge() {
            mHasBadge = true;
            return this;
        }

        /**
         * The related Tab is not about to have a badge
         *
         * @return this builder
         */
        public Builder noBadge() {
            mHasBadge = false;
            return this;
        }

        /**
         * Dynamically set the availability of tab's badge
         *
         * @param hasBadge
         * @return this builder
         */
        // This method is used for DEBUG purpose only
    /*hide*/
        public Builder badge(boolean hasBadge) {
            mHasBadge = hasBadge;
            return this;
        }

        /**
         * Set icon custom drawable by Resource ID;
         *
         * @param drawableRes
         * @return this builder
         */
        public Builder icon(int drawableRes) {
            mIconDrawable = getDrawableCompat(mContext, drawableRes);
            return this;
        }

        /**
         * Set icon custom drawable by Drawable Object
         *
         * @param drawable
         * @return this builder
         */
        public Builder icon(Drawable drawable) {
            mIconDrawable = drawable;
            return this;
        }

        /**
         * Set drawable color. Use this when user wants to change drawable's color filter
         *
         * @param color
         * @return this builder
         */
        public Builder iconColor(Integer color) {
            mIconColorFilter = color;
            return this;
        }

        /**
         * increase current badge by 1
         *
         * @return this builder
         */
        public Builder increase() {
            mBadgeCount =
                    mBadgeTextView == null ?
                            INVALID_NUMBER
                            :
                            Integer.parseInt(mBadgeTextView.getText().toString()) + 1;
            return this;
        }

        /**
         * decrease current badge by 1
         *
         * @return
         */
        public Builder decrease() {
            mBadgeCount =
                    mBadgeTextView == null ?
                            INVALID_NUMBER
                            :
                            Integer.parseInt(mBadgeTextView.getText().toString()) - 1;
            return this;
        }

        /**
         * set badge count
         *
         * @param count expected badge number
         * @return this builder
         */
        public Builder badgeCount(int count) {
            mBadgeCount = count;
            return this;
        }

        /**
         * Build the current Tab icon's custom view
         */
        public void build() {
            if (mView == null) {
                return;
            }

            // update badge counter
            if (mBadgeTextView != null) {
                mBadgeTextView.setText(formatBadgeNumber(mBadgeCount));

                if (mHasBadge) {
                    mBadgeTextView.setVisibility(View.VISIBLE);
                } else {
                    // set to View#INVISIBLE to not screw up the layout
                    mBadgeTextView.setVisibility(View.INVISIBLE);
                }
            }

            // update icon drawable
            if (mIconView != null && mIconDrawable != null) {
                mIconView.setImageDrawable(mIconDrawable.mutate());
                // be careful if you modify this. make sure your result matches your expectation.
                if (mIconColorFilter != null) {
                    mIconDrawable.setColorFilter(mIconColorFilter, PorterDuff.Mode.MULTIPLY);
                }
            }
            if(mTitle != null){
                if(mIconColorFilter != null) {
                    mTitle.setTextColor(mIconColorFilter);
                }
            }

            mTab.setCustomView(mView);
        }
    }

    private static Drawable getDrawableCompat(Context context, int drawableRes) {
        Drawable drawable = null;
        try {
            if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                drawable = context.getResources().getDrawable(drawableRes);
            } else {
                drawable = context.getResources().getDrawable(drawableRes, context.getTheme());
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        return drawable;
    }

    /**
     * This format must follow User's badge policy.
     *
     * @param value of current badge
     * @return corresponding badge number. TextView need to be passed by a String/CharSequence
     */
    private static String formatBadgeNumber(int value) {
        if (value < 0) {
            return "-" + formatBadgeNumber(-value);
        }

        if (value <= 99) {
            // equivalent to String#valueOf(int);
            return Integer.toString(value);
        }

        // my own policy
        return "99+";
    }
}
