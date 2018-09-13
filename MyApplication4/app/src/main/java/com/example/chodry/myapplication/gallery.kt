package com.example.chodry.myapplication

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.camera.*
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import com.example.chodry.myapplication.classifier.*
import com.example.chodry.myapplication.classifier.tensorflow.ImageClassifierFactory
import com.example.chodry.myapplication.utils.getCroppedBitmap
import java.io.File
import java.io.IOException
import android.Manifest
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.example.chodry.myapplication.utils.getUriFromFilePath
import com.github.barteksc.pdfviewer.PDFView

private const val REQUEST_PERMISSIONS = 1
private const val GALLERY = 1

class gallery : AppCompatActivity(){

    private val handler = Handler()
    private lateinit var classifier: Classifier
    private var photoFilePath = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gallery)

        createClassifier()
        choosePhotoFromGallery()


    }

    //open gallery
    private fun choosePhotoFromGallery(){
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val file = File(photoFilePath)
        if (requestCode == GALLERY){
            if (data != null){
                val contentURI = data!!.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    //val path = saveImage(bitmap)
                    Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show()
                    val croppedBitmap = getCroppedBitmap(bitmap)
                    classifyAndShowResult(croppedBitmap)
                    iv.setImageBitmap(bitmap)

                }catch (e: IOException){
                    e.printStackTrace()
                    Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun classifyAndShowResult(croppedBitmap: Bitmap) {
        runInBackground(
                Runnable {
                    val result = classifier.recognizeImage(croppedBitmap)
                    showResult(result)
                })
    }

    private fun createClassifier() {
        classifier = ImageClassifierFactory.create(
                assets,
                GRAPH_FILE_PATH,
                LABELS_FILE_PATH,
                IMAGE_SIZE,
                GRAPH_INPUT_NAME,
                GRAPH_OUTPUT_NAME
        )
    }

    @Synchronized
    private fun runInBackground(runnable: Runnable) {
        handler.post(runnable)
    }

    private fun showResult(result: Result) {
        textResult.text = result.result.toUpperCase()
        layoutContainer.setBackgroundColor(getColorFromResult(result.result))
    }

    @Suppress("DEPRECATION")
    private fun getColorFromResult(result: String): Int {
        if (result == getString(R.string.healthy)) {
            return resources.getColor(R.color.healthy)
        } else if (result==getString(R.string.cbsd)) {
            return resources.getColor(R.color.cbsd)
        }
        else if (result==getString(R.string.cgm)){
            return resources.getColor(R.color.cgm)
        }
        else if (result==getString(R.string.cbb)){
            return resources.getColor(R.color.cbb)
        }
        else
            return resources.getColor(R.color.cmd)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main_menu2, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.gallery) {
            choosePhotoFromGallery()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}