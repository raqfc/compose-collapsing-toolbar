package br.com.justworks.volan2.common.presentation

abstract class BaseState<T: BaseState<T>> {

    abstract fun isValid(): Boolean
    abstract fun focusOnFirstInvalid()
    abstract fun forceValidate(): T
}