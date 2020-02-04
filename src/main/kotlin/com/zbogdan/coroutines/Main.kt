package com.zbogdan.coroutines

import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

fun main() {

    val format = SimpleDateFormat("ss.SSS")

    fun format(): String = format.format(Date())
    fun threadName(): String = Thread.currentThread().name

    GlobalScope.launch {
        delay(2000L)
        println("${format()}: Two seconds running on thread ${threadName()}")

        for (i in 100 downTo 0) {
            println("${format()}: outputs down to $i on thread ${threadName()}")
            delay(500L)
        }
    }

    GlobalScope.launch {
        delay(2000L)
        println("${format()}: Another two seconds running on thread ${threadName()}")
    }

    GlobalScope.launch {
        coroutineScope {
            //Creating a new coroutine
            launch {
                delay(3000L)
                println("${format()}: Three seconds running on thread ${threadName()}")

                coroutineScope {
                    //another coroutine
                    launch {
                        delay(5000L)
                        println("${format()}: Five seconds running on thread ${threadName()}")
                    }
                }
            }

            delay(1000L)
            println("${format()}: One seconds running on thread ${threadName()}")
        }
    }

    runBlocking{
        delay(30000L)
    }
    println("${format()}: Coroutine scope is over on thread ${threadName()}")
}