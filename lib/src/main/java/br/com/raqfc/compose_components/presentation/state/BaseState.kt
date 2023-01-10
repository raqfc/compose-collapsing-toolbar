package br.com.raqfc.compose_components.presentation.state

abstract class BaseState<T: BaseState<T>> {

    abstract fun isValid(): Boolean
    abstract fun focusOnFirstInvalid()
    abstract fun forceValidate(): T
}