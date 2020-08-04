package com.eahm.kotlincoroutines.models

data class MenuElement (
    val title : String = "",
    val description : String = "",
    val activity : Class<*>
)
