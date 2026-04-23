abstract class A<out T>

open class C

class D : C()

class B<T : C> : A<T>()

fun main(args: Array<String>) {
    var a: A<C>

    a = B<D>()

//    println(a)
}