package com.dicoding.storyapp.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.ActivityDetailBinding
import com.dicoding.storyapp.service.response.ListStoryItem
import java.io.File

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupView()
        setupData()
    }

    private fun setupData() {
        val storyItem: ListStoryItem? = intent.getParcelableExtra(EXTRA_DATA)
        if (storyItem != null) {
            binding.apply {
                tvNameDetail.text = storyItem.name
                tvDescDetail.text = storyItem.description
                Glide
                    .with(this@DetailActivity)
                    .load(storyItem.userProfileImage)
                    .fitCenter()
                    .apply(
                        RequestOptions
                            .placeholderOf(R.drawable.ic_loading)
                            .error(R.drawable.keyra)
                    ).into(imgUserDetail)
                Glide
                    .with(this@DetailActivity)
                    .load(storyItem.photo)
                    .fitCenter()
                    .apply(
                        RequestOptions
                            .placeholderOf(R.drawable.ic_loading)
                            .error(R.drawable.ic_error)
                    ).into(detailStory)
            }
        } else {
            Toast.makeText(this@DetailActivity, "Data tidak tersedia", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupView() {
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = getString(R.string.title_detail)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.share_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share -> {
                val storyItem: ListStoryItem? = intent.getParcelableExtra(EXTRA_DATA)
                if (storyItem != null) {
                    shareStory(storyItem)
                }
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun shareStory(storyItem: ListStoryItem) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND

        val shareText = "Check out this story: ${storyItem.name}\n\n${storyItem.description}"
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareText)

        val imageUri =
            FileProvider.getUriForFile(this, "com.dicoding.storyapp", File(storyItem.photo))
        sendIntent.type = contentResolver.getType(imageUri)
        sendIntent.putExtra(Intent.EXTRA_STREAM, imageUri)

        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        startActivity(Intent.createChooser(sendIntent, "Share via"))
    }


    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}
