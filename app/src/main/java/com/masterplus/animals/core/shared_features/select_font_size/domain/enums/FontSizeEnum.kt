package com.masterplus.animals.core.shared_features.select_font_size.domain.enums

import com.masterplus.animals.core.domain.utils.UiText
import com.masterplus.animals.core.shared_features.preferences.domain.model.IEnumPrefValue

/*
   * BodySmallSize = 12.sp
   * BodyMediumSize = 14.sp
   * BodyLargeSize = 16.sp
   * TitleSmallSize = 14.sp
   * TitleMediumSize = 16.sp
   * TitleLargeSize = 22.sp
   *
   * LabelSmallSize = 11.sp
   * LabelMediumSize = 12.sp
   * LabelLargeSize = 14.sp
   *
 */


enum class FontSizeEnum(
    override val keyValue: Int,
    val extra: Int,
    val scale: Float,
    val description: UiText
): IEnumPrefValue {

    Small(
        keyValue = 1,
        description = UiText.Text("Küçük"),
        extra = -2,
        scale = 0.9f
    ),
    Medium(
        keyValue = 2,
        description = UiText.Text("Orta"),
        extra = 0,
        scale = 1.0f
    ),
    Large(
        keyValue = 3,
        description = UiText.Text("Büyük"),
        extra = 2,
        scale = 1.15f
    ),
    Large2X(
        keyValue = 4,
        description = UiText.Text("2X Büyük"),
        extra = 4,
        scale = 1.3f
    ),
    Large3X(
        keyValue = 5,
        description = UiText.Text("3X Büyük"),
        extra = 6,
        scale = 1.45f
    );

    companion object {

        val DEFAULT = Medium

        fun fromKeyValue(keyValue: Int): FontSizeEnum{
            entries.forEach {
                if(it.keyValue == keyValue) return it
            }
            return DEFAULT
        }
    }


}