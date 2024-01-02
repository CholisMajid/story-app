package com.cholis.mystoryapp

import com.cholis.mystoryapp.response.GetStoryResponse
import com.cholis.mystoryapp.response.Story

object DataDummy {
    fun generateDummyStory(): GetStoryResponse {
        val listStory = ArrayList<Story>()
        for (i in 0..100) {
            val storyItem = Story(
                id = "story-$i",
                name = "kiwkiw",
                description = "test",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1699323552028_TfoNwqFu.jpg",
                createdAt = "2023-11-07T02:19:12.032Z",
                lat = 0.0,
                lon = 0.0
            )
            listStory.add(storyItem)
        }
        return GetStoryResponse(
            error = false, message = "Stories fetched successfully", listStory = listStory
        )
    }
}