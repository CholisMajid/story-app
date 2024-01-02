package com.cholis.mystoryapp.view.liststory

import android.annotation.SuppressLint
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.cholis.mystoryapp.DataDummy
import com.cholis.mystoryapp.MainDispatcherRule
import com.cholis.mystoryapp.getOrAwaitValue
import com.cholis.mystoryapp.repository.RepoStory
import com.cholis.mystoryapp.response.Story
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ListStoryVMTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var repoStory: RepoStory

    @SuppressLint("CheckResult")
    @Test
    fun `when Get Stories Should Not Null and Return Data`() = runTest {
        val dummyGetStoryResponse = DataDummy.generateDummyStory()
        val data: PagingData<Story> =
            StoryPagingStore.snapshot(dummyGetStoryResponse.listStory)
        val expectedStory = MutableLiveData<PagingData<Story>>()
        expectedStory.value = data


        Mockito.mockStatic(Log::class.java)
        Mockito.`when`(repoStory.getStory()).thenReturn(expectedStory)

        val listStoryVM = ListStoryVM(repoStory)
        val actualStory: PagingData<Story> = listStoryVM.stories.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = AdapterStory.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyGetStoryResponse.listStory, differ.snapshot())
        Assert.assertEquals(dummyGetStoryResponse.listStory.size, differ.snapshot().size)
        Assert.assertEquals(dummyGetStoryResponse.listStory[0], differ.snapshot()[0])

    }

    @Test
    fun `when Get Stories Empty Should Return No Data`() = runTest {
        val data: PagingData<Story> = PagingData.from(emptyList())
        val expectedStory = MutableLiveData<PagingData<Story>>()
        expectedStory.value = data

        Mockito.`when`(repoStory.getStory()).thenReturn(expectedStory)
        val listStoryVM = ListStoryVM(repoStory)
        val actualStory: PagingData<Story> = listStoryVM.stories.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = AdapterStory.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        Assert.assertTrue(differ.snapshot().isEmpty())
    }

}

class StoryPagingStore : PagingSource<Int, LiveData<List<Story>>>() {

    companion object {
        fun snapshot(listStory: List<Story>): PagingData<Story> {
            return PagingData.from(listStory)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<Story>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<Story>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }

}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}