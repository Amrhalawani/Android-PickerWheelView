package com.amrh.verticalpicker

import android.content.Context
import android.widget.Toast

class MyToast(context: Context?, text:String)  {

//for testing

    init {
    Toast.makeText(context,text,Toast.LENGTH_SHORT).show()
    }

}