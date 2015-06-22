#@All

The `@All` annotation is a powerful feature of Jukito which allows to execute the same test multiple times with a different input in each execution.
Typical examples for this are:

* having a test executed with all values of an enum.
* having a test executed with different input to test for robustness of the code
* having the same tests executed with the different implementations to ensure that both behave the same.

In order to do so all inputs must be configured in the TestModule of the test class.

```java
public class SimpleAllTest {
    public static class Module extends TestModule {
        protected void configureTest() {
            bindManyInstances(String.class, "Hello", "world");
        }
    }

    @Test
    public void print(@All String s) {
        System.out.println(s);
    }
}
```

The above example will print either "Hello" "world" or "world" "Hello".

Behind the scene Jukito looks for the `@All` annotation. When such an annotation is encountered Jukito will collect all bindings matching the type of the annotated argument.
Finally it will call the test for every bound input executing it multiple times.


## Testing different implementations of an interface
The `@All` annotation can also be used to test different implementations of the same interface or different subclasses of an (abstract) super class.
In this case the types are bound directly instead of concrete instances.

```java
public class SimpleAllTest {
    public static class Module extends TestModule {
        protected void configureTest() {
            bindMany(MyInterface.class, Impl1.class, Impl2.class);
        }
    }

    @Test
    public void print(@All MyInterface x) {
        System.out.println(x.getClass().getSimpleName());
    }
}
```

The above example will print either "Impl1 Impl2" or "Impl2 Impl1".


## The Cartesian Product
Tests are not limited to a single parameter. It is possible to annotated more than one parameter with the `@All` annotation. In this case Jukito will form the Cartesian product of all inputs to the test.

```java
public class CartesianProductAllTest {
    public static class Module extends TestModule {
        protected void configureTest() {
            bindManyInstances(Integer.class, 1, 2);
            bindManyInstances(String.class, "a", "b");
        }
    }

    @Test
    public void print(@All String s, @All Integer i) {
        System.out.println(s + i);
    }
}
```

The above example will print the four strings "a1", "a2", "b1", and "b2". The order of them is not guaranteed.
**Warning**: the number of test executions increases dramatically for Cartesian products. The execution time of all tests will grow linear with the number of executions.
i:e.: a test method with three parameters with an `@All` annotation and four bindings per parameter will be executed 4x4x4 = 64 times. Having five bindings for the same test method will lead to 5x5x5 = 125 executions.


## Grouping by names
Sometimes it is desirable to have different groups of bindings of the same class to be used within the same test. Jukito supports this by using a name to identify to which group a binding belongs and which group should be used for executing a test.

```java
public class NamedAllTest {
    public static class Module extends TestModule {
        protected void configureTest() {
            bindManyNamedInstances(Integer.class, "even", 2, 4, 6);
            bindManyNamedInstances(Integer.class, "odd", 1, 3, 5);
        }
    }

    @Test
    public void printEven(@All("even") Integer i) {
        System.out.println("even " + i);
    }

    @Test
    public void printOdd(@All("odd") Integer i) {
        System.out.println("odd " + i);
    }
}
```

The above example will print the six strings "even 2", "even 4", "even 6", "odd 1", "odd 3", "odd 5". The order of them is not guaranteed.
