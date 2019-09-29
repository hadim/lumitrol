package org.hadim.lumitrol.base

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import org.hadim.lumitrol.model.Repository

abstract class BaseViewModel(
    protected val savedStateHandle: SavedStateHandle,
    protected val repository: Repository
) : ViewModel()
