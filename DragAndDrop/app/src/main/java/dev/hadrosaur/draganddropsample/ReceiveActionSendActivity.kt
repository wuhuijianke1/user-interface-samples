package dev.hadrosaur.draganddropsample

import android.content.ClipData
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.widget.ImageView
import android.widget.Toast

class ReceiveActionSendActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receive_action_send)

        findViewById<View>(R.id.dropTestEditText).apply {
            setOnDragListener(object : View.OnDragListener{
                override fun onDrag(v: View, event: DragEvent): Boolean {

                    when(event.action) {
                        DragEvent.ACTION_DRAG_STARTED -> {
                            v.setBackgroundColor(resources.getColor(android.R.color.holo_green_light, null))
                        }

                        DragEvent.ACTION_DRAG_ENTERED -> {
                            v.setBackgroundColor(resources.getColor(android.R.color.holo_blue_light, null))
                        }

                        DragEvent.ACTION_DROP -> {
                            showToast(" drop event")
                        }

                        DragEvent.ACTION_DRAG_ENDED -> {
                            v.setBackgroundColor(resources.getColor(android.R.color.holo_red_light, null))
                        }
                    }
                    return true
                }
            })
        }

        findViewById<View>(R.id.dropTestImageView).apply {
            setOnDragListener(object : View.OnDragListener{
                override fun onDrag(v: View, event: DragEvent): Boolean {

                    when(event.action) {
                        DragEvent.ACTION_DRAG_STARTED -> {
                            v.setBackgroundColor(resources.getColor(android.R.color.holo_green_light, null))
                        }

                        DragEvent.ACTION_DRAG_ENTERED -> {
                            v.setBackgroundColor(resources.getColor(android.R.color.holo_blue_light, null))
                        }

                        DragEvent.ACTION_DROP -> {
                            val clipData = event.clipData
                            val clipItem = clipData.getItemAt(0)
                            try {
                                handleImageDrop(clipItem, v as ImageView)
                                showToast(" drop event read uri ok")
                            } catch (e:Exception) {
                                showToast(" drop event read uri fail e=${e.message}")
                            }
                        }

                        DragEvent.ACTION_DRAG_ENDED -> {
                            v.setBackgroundColor(resources.getColor(android.R.color.holo_red_light, null))
                        }
                    }
                    return true
                }
            })
        }

    }

    private fun showToast(msg:String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun handleImageDrop(item: ClipData.Item, imageView:ImageView) {
        item.uri?.let { uri ->
            val size = 72.px
            decodeSampledBitmapFromUri(
                contentResolver,
                uri,
                size,
                size
            )?.let { bitmap ->

                val drawable = BitmapDrawable(resources, bitmap).apply {
                    val ratio =
                        intrinsicHeight.toFloat() / intrinsicWidth.toFloat()
                    setBounds(0, 0, size, (size * ratio).toInt())
                }
                imageView.setImageDrawable(drawable)
            }
        } ?: run {
            MainActivity.logE("Clip data is missing URI")
        }
    }
}