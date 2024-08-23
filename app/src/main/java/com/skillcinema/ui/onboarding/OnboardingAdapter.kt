package com.skillcinema.ui.onboarding

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skillcinema.R
import com.skillcinema.databinding.FragmentOnboardingPagerBinding

class OnboardingAdapter : RecyclerView.Adapter<OnboardingViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        return OnboardingViewHolder(
            FragmentOnboardingPagerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        holder.binding.apply {
            onBoardingTextView.text = when (position + 1) {
                1 -> root.context.resources.getString(R.string.onboarding_1_text)
                2 -> root.context.resources.getString(R.string.onboarding_2_text)
                3 -> root.context.resources.getString(R.string.onboarding_3_text)
                else -> root.context.resources.getString(R.string.onboarding_1_text)
            }
            onBoardingTextView.setTextColor(R.color.black)
            onBoardingImageView.setImageResource(
                when (position + 1) {
                    1 -> R.drawable.ic_onboarding_1
                    2 -> R.drawable.ic_onboarding_2
                    3 -> R.drawable.ic_onboarding_3
                    else -> R.drawable.ic_onboarding_1
                }
            )
            onBoardingScreenNumber.setImageResource(
                when (position + 1) {
                    1 -> R.drawable.ic_onboarding_screen_number
                    2 -> R.drawable.ic_onboarding_screen_number2
                    3 -> R.drawable.ic_onboarding_screen_number3
                    else -> R.drawable.ic_onboarding_screen_number
                }
            )
        }
    }
    override fun getItemCount(): Int = 3
}
class OnboardingViewHolder(val binding: FragmentOnboardingPagerBinding): RecyclerView.ViewHolder(binding.root)