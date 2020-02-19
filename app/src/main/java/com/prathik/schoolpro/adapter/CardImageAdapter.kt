package com.prathik.schoolpro.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.content.Context
import android.graphics.Color
import android.support.v7.widget.GridLayoutManager
import android.widget.ImageView
import android.widget.Toast
import com.prathik.schoolpro.R


class CardImageAdapter(
    var context: Context,
    var skinIds: Array<Int>,
    skinSelectedPosition: Int,
    onSkinSelectedListener: OnSkinSelectedListener
):BaseAdapter() {
    private  var mContext=context
    private  var skinSelectedPosition=skinSelectedPosition
    private var onSkinSelectedListener=onSkinSelectedListener

    override fun getCount(): Int {
        return skinIds.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
     //   Toast.makeText(context,"posss : $skinSelectedPosition",Toast.LENGTH_SHORT).show()
        return 0
    }

    // create a new ImageView for each item referenced by the Adapter
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val imageView: ImageView

        if (convertView == null) {
            imageView = ImageView(mContext)
            imageView.layoutParams = GridLayoutManager.LayoutParams(200 , 120)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            imageView.setPadding(2, 2, 2, 2)
        } else {
            imageView = convertView as ImageView
        }
        imageView.setImageResource(skinIds[position])

        if(skinSelectedPosition==position){
            imageView.setBackgroundColor(Color.parseColor("#F44336"));
            imageView.setPadding(4,4,4,4)
          //  imageView.setImageResource(R.drawable.bob)
        }

        imageView.setOnClickListener {
            onSkinSelectedListener.onSkinSelected(position)
        }

        return imageView
    }





   // data class SkinModal(var skin:Int, var isSelected:Boolean)

    interface OnSkinSelectedListener{
         fun onSkinSelected(position:Int)
    }
}