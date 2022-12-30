package br.com.raqfc.compose_components.state

abstract class BaseState<T: BaseState<T>> {

    abstract fun isValid(): Boolean
    abstract fun focusOnFirstInvalid()
    abstract fun forceValidate(): T
}