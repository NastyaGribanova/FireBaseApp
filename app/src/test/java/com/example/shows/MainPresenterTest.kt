package com.example.shows

import com.example.shows.presenters.MainPresenter
import com.example.shows.repository.SignInRepositoryImpl
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class MainPresenterTest {

    var presenter: MainPresenter? = null

    @Before
    fun beforeFun() {
        val authRepositoryImpl: SignInRepositoryImpl = Mockito.mock(SignInRepositoryImpl::class.java)
        presenter = MainPresenter(authRepositoryImpl)
    }

    @Test
    fun changesFromDialogTest(){
        presenter?.changesFromDialog("name", "genre", 100)
        Assert.assertEquals("name",
            presenter?.getDataSource()?.size?.let { presenter?.getDataSource()?.get(it-1)?.name })
    }

    @Test
    fun deleteTest(){
        val size: Int? = presenter?.getDataSource()?.size?.minus(1)
        presenter?.getDataSource()?.get(0)?.let { presenter?.delete(it) }
        Assert.assertEquals(size, presenter?.getDataSource()?.size)
    }

    @Test
    @Throws(NullPointerException::class)
    fun crashTest() {
        presenter?.causeCrash()
    }
}
