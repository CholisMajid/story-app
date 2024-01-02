package com.cholis.mystoryapp.view.liststory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.cholis.mystoryapp.repository.RepoStory
import com.cholis.mystoryapp.response.Story

class ListStoryVM(storyRepository: RepoStory) : ViewModel() {
    val stories: LiveData<PagingData<Story>> =
        storyRepository.getStory().cachedIn(viewModelScope)
}