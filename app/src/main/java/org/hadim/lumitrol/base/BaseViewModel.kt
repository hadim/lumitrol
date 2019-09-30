package org.hadim.lumitrol.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import org.hadim.lumitrol.model.Repository

abstract class BaseViewModel(
    application: Application,
    val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    var repository: Repository = Repository.getRepository(application)
}
