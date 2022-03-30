package com.myperishableplanner.v21001

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.myperishableplanner.v21001.dto.Item
import com.myperishableplanner.v21001.service.ItemService
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class ItemTest {
    @get : Rule
    var rule: TestRule = InstantTaskExecutorRule()
    lateinit var itemService : ItemService
    var allItems : List<Item>? = ArrayList<Item>()
    @Test
     fun `Given Item data are available when I search for 454004 then I should receive Apple`() = runTest()
    {
        givenItemserviceIsInitialized()
        whenItemDataAreReadAndParsed()
        thenTheItemCollectionShouldContainApple()
    }

    private fun givenItemserviceIsInitialized()
    {
        itemService = ItemService()
    }

    private suspend fun whenItemDataAreReadAndParsed()
    {
        allItems = itemService.fetchItems("apple")
    }

    private fun thenTheItemCollectionShouldContainApple()
    {
        assertNotNull(allItems)
        assertTrue(allItems!!.isNotEmpty())
        var containsApple = false
        allItems!!.forEach {
           if (it.id.equals(454004) && it.name.equals("APPLE"))
               containsApple = true

        }
     assertTrue(containsApple)
    }
}