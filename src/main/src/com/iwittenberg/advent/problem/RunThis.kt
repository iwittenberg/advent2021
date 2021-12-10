package com.iwittenberg.advent.problem

@Target(AnnotationTarget.CLASS)
annotation class RunThis(val andOnlyThis: Boolean = false)
