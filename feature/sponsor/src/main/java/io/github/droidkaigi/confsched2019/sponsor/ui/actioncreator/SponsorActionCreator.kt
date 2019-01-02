package io.github.droidkaigi.confsched2019.sponsor.ui.actioncreator

import androidx.lifecycle.Lifecycle
import io.github.droidkaigi.confsched2019.action.Action
import io.github.droidkaigi.confsched2019.di.PageScope
import io.github.droidkaigi.confsched2019.dispatcher.Dispatcher
import io.github.droidkaigi.confsched2019.ext.android.coroutineScope
import io.github.droidkaigi.confsched2019.model.LoadingState
import io.github.droidkaigi.confsched2019.system.actioncreator.ErrorHandler
import io.github.droidkaigi.confsched2019.data.repository.SponsorRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class SponsorActionCreator @Inject constructor(
    override val dispatcher: Dispatcher,
    private val sponsorRepository: SponsorRepository,
    @PageScope private val lifecycle: Lifecycle
) : CoroutineScope by lifecycle.coroutineScope,
    ErrorHandler {
    fun load() = launch {
        try {
            dispatcher.dispatch(Action.SponsorLoadingStateChanged(LoadingState.LOADING))
            sponsorRepository.refresh()
            dispatcher.dispatch(Action.SponsorLoaded(sponsorRepository.sponsors()))
            dispatcher.dispatch(Action.SponsorLoadingStateChanged(LoadingState.LOADED))
        } catch (e: Exception) {
            onError(e)
            dispatcher.dispatch(Action.SponsorLoadingStateChanged(LoadingState.INITIALIZED))
        }
    }

    fun openSponsorLink(url: String) = launch {
        dispatcher.dispatch(Action.SponsorOpenLink(url))
    }

    fun clearSponsorLink() = launch {
        dispatcher.dispatch(Action.SponsorOpenLink(null))
    }
}
