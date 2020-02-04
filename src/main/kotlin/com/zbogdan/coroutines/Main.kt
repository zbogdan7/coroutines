package com.zbogdan.coroutines

import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

fun main() {
    val start = System.currentTimeMillis()

    val format = SimpleDateFormat("ss.SSS")

    fun format(): String = format.format(Date())
    fun threadName(): String = Thread.currentThread().name

    GlobalScope.launch {
        //Creating new Coroutine in the global scope
        delay(2000L)
        println("${format()}: Two seconds running on thread ${threadName()}")

        for (i in 100 downTo 0) { //Every 0.5 seconds scheduler will execute the iteration.
            println("${format()}: outputs down to $i on thread ${threadName()}")
            delay(500L)
        }
    }

    GlobalScope.launch {
        //Creating another coroutine in the global scope
        delay(2000L)
        println("${format()}: Another two seconds running on thread ${threadName()}")
    }

    GlobalScope.launch {
        //Creating third coroutine in the global scope as well.
        coroutineScope {
            //Coroutine with scope of outer one.
            //Creating a new coroutine
            launch {
                //Inner coroutine one
                delay(3000L)
                println("${format()}: Three seconds running on thread ${threadName()}")

                coroutineScope {
                    //Creating another sub-scope
                    //another coroutine
                    launch {
                        //Coroutine of 2D-scoped coroutine
                        delay(5000L)
                        println("${format()}: Five seconds running on thread ${threadName()}")
                    }
                }
            }

            delay(1000L)
            println("${format()}: One seconds running on thread ${threadName()}")
        }
    }

    //Block execution of Main thread until 30 seconds passes.
    runBlocking {
        delay(30000L)
    }

    //Calculating the time wasted on the entire execution (useless but clearly seen)
    val stop = System.currentTimeMillis()
    val duration = (stop - start) / 1000 //get seconds instead of millis.

    //The last statement of the execution on main thread (This message will be printed only after 30 seconds due to blocking main thread).
    println("${format()}: Coroutine scope is over on thread ${threadName()}. Summary execution time: $duration")

}