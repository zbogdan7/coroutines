package com.zbogdan.coroutines

import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

fun coroutineStart() {
    val start = System.currentTimeMillis()

    val format = SimpleDateFormat("ss.SSS")

    fun timeInMillis(): String = format.format(Date())
    fun threadName(): String = Thread.currentThread().name
    fun printThreadMessage(msg: String): Unit = println("${timeInMillis()}: $msg running on thread ${threadName()}")

    GlobalScope.launch {
        //Creating new Coroutine in the global scope
        delay(2000L)
        printThreadMessage("Two seconds ")

        for (i in 100 downTo 0) { //Every 0.5 seconds scheduler will execute the iteration.
            println("${timeInMillis()}: ticks $i on ${threadName()} thread")
            delay(500L)
        }
    }

    GlobalScope.launch {
        //Creating another coroutine in the global scope
        delay(2000L)
        printThreadMessage("Another two seconds ")
    }

    GlobalScope.launch {

        val retrievedValue: Deferred<String> = async {
            delay(15000L)
            "${timeInMillis()}: Fifteen seconds deferred value on ${threadName()} thread"
        }

        //Creating third coroutine in the global scope as well.
        coroutineScope {
            //Coroutine with scope of outer one.
            //Creating a new coroutine
            launch {
                //Inner coroutine one
                delay(3000L)
                printThreadMessage("Three seconds ")

                coroutineScope {
                    //Creating another sub-scope
                    //another coroutine
                    launch {
                        //Coroutine of 2D-scoped coroutine
                        delay(5000L)
                        printThreadMessage("Five seconds ")
                    }
                }
            }

            delay(1000L)
            printThreadMessage("Fifteen seconds ${retrievedValue.await()} ")
            printThreadMessage("One second ")
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
    println("${timeInMillis()}: Coroutine scope is over on ${threadName()} thread. Summary execution time: $duration")

}

fun main() = coroutineStart()