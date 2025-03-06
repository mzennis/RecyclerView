package id.mzennis

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ViewGroup
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable

class LottieSwipeRefreshLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0)
    : SimpleSwipeRefreshLayout(context, attrs, defStyle) {

    private var animationFile: Int = -1
    private val lottieAnimationView by lazy {
        LottieAnimationView(context).apply {
            if (animationFile == -1) {
                throw IllegalStateException("Could not resolve an animation for your pull to refresh layout")
            }

            setAnimation(animationFile)
            repeatCount = LottieDrawable.INFINITE
            val width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100f, context.resources.displayMetrics).toInt()
            val height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50f, context.resources.displayMetrics).toInt()

            layoutParams = LayoutParams(ViewGroup.LayoutParams(width, height))
        }
    }

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.LottieSwipeRefreshLayout, defStyle, 0).let { style ->
            animationFile = style.getResourceId(R.styleable.LottieSwipeRefreshLayout_lottie_rawRes, -1)
            addView(lottieAnimationView)
            style.recycle()
        }

        addProgressListener {
            lottieAnimationView.progress = it
        }

        addTriggerListener {
            lottieAnimationView.resumeAnimation()
        }
    }

    override fun stopRefreshing() {
        super.stopRefreshing()
        lottieAnimationView.pauseAnimation()
    }

    override fun startRefreshing() {
        super.startRefreshing()
        lottieAnimationView.resumeAnimation()
    }
}
