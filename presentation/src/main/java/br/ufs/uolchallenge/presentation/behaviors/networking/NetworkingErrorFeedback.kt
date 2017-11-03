package br.ufs.uolchallenge.presentation.behaviors.networking

import br.ufs.uolchallenge.domain.CommunicationError
import io.reactivex.*


/**
 * Created by bira on 11/3/17.
 */

class NetworkingErrorFeedback<T>(
        val passiveView: NetworkingErrorView,
        val uiScheduler: Scheduler) : ObservableTransformer<T, T> {

    override fun apply(upstream: Observable<T>): ObservableSource<T> {
        return upstream.doOnError { handleIfNetworkingError(it) }
    }

    private fun handleIfNetworkingError(throwable: Throwable): Observable<T> {

        if (throwable is CommunicationError) {
            Completable.fromAction(passiveView.reportNetworkingError())
                    .observeOn(uiScheduler)
                    .subscribe()
        }

        return Observable.error(throwable)
    }
}