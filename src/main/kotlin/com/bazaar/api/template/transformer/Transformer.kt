package com.bazaar.api.template.transformer


interface Transformer <A, B> {
    fun transform(source: A): B
}