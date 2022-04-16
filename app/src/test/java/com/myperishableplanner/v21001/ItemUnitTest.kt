package com.myperishableplanner.v21001

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.myperishableplanner.v21001.dto.Item
import com.myperishableplanner.v21001.service.ItemService
import io.mockk.impl.annotations.MockK
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import io.mockk.MockKAnnotations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.rules.TestRule
import kotlinx.coroutines.newSingleThreadContext
import org.junit.After
import org.junit.Before
import io.mockk.coEvery

class ItemUnitTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()


    lateinit var itemService: ItemService
    lateinit var mvm: ItemViewModel

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @MockK
    lateinit var mockItemService: ItemService
    @Before
    fun populateItems() {
        Dispatchers.setMain(mainThreadSurrogate)
        MockKAnnotations.init(this)
        mvm = ItemViewModel()

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }
    @Test
    fun `given a item dto when id is 454004 and name is apple then code is 454004 and name is apple`() {
        var item = Item(454004, "APPLE", "TREECRISP 2 GO")
        Assert.assertTrue(item.id.equals(454004))
        Assert.assertTrue(item.name.equals("APPLE"))
        Assert.assertTrue(item.brand.equals("TREECRISP 2 GO"))
    }

    @Test
    fun `given a view model with live data when populated with item then results should contain Milk`() {
        givenViewModelIsInitializedWithMockData()
        whenJSONDataAreReadAndParsed()
        thenResultsShouldContainMilk()
    }

    private fun givenViewModelIsInitializedWithMockData() {
        val items = ArrayList<Item>()
        items.add(Item(1, "Milk", "Milk"))
        coEvery {mockItemService.fetchItems()} returns items

        mvm.itemService =mockItemService
    }

    private fun whenJSONDataAreReadAndParsed() {
        mvm.fetchItems()
        var test= "test"
    }

    private fun thenResultsShouldContainMilk() {
        var allItems: List<Item>? = ArrayList<Item>()
        val latch = CountDownLatch(1);
        val observer = object : Observer<List<Item>> {
            override fun onChanged(t: List<Item>?) {
                allItems = t
                latch.countDown()
                mvm.items.removeObserver(this)
            }
        }
        mvm.items.observeForever(observer)

        latch.await(1, TimeUnit.SECONDS)
        Assert.assertNotNull(allItems)
        Assert.assertTrue(allItems!!.contains(Item(1, "Milk","Milk")))

    }




}