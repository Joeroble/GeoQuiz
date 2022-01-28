package com.bignerdranch.android.geoquiz

import androidx.annotation.StringRes
// creates the dat aclass Question that is used to store the question and answer data.
data class Question(@StringRes val textResID: Int, val answer: Boolean)