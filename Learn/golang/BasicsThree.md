# Basics : More types like structs, slices and maps

## Pointers : Quick Catchup

Go has pointers. A pointer holds the memory address of a value.

The type *T is a pointer to a T value. Its zero value is nil.

```golang
var p *int
```

The & operator generates a pointer to its operand.

```golang
i := 42
p = &i
```

The * operator denotes the pointer's underlying value.

```golang
fmt.Println(*p) // read i through the pointer p
*p = 21         // set i through the pointer p
```

This is known as "dereferencing" or "indirecting".

Unlike C, Go has no pointer arithmetic.

## Structs

A struct is a collection of fields.
Struct fields are accessed using a dot.

```golang
package main

import "fmt"

type Vertex struct {
    X int
    Y int
}

func main() {
    v := Vertex{1, 2}
    v.X = 4
    fmt.Println(v.X)
}
```

### Pointers to structs

Struct fields can be accessed through a struct pointer.

To access the field X of a struct when we have the struct pointer p we could write ```(*p).X```. However, that notation is cumbersome, so the language permits us instead to write just ```p.X```, without the explicit dereference.

### struct literals

A struct literal denotes a newly allocated struct value by listing the values of its fields.

You can list just a subset of fields by using the Name: syntax. (And the order of named fields is irrelevant.)

The special prefix & returns a pointer to the struct value.

```golang
    v1 = Vertex{1, 2}  // has type Vertex
    v2 = Vertex{X: 1}  // Y:0 is implicit
    v3 = Vertex{}      // X:0 and Y:0
    p  = &Vertex{1, 2} // has type *Vertex
```

## Arrays

The type [n]T is an array of n values of type T.

The expression

```golang
var a [10]int
```

declares a variable a as an array of ten integers.

An array's length is part of its type, so arrays cannot be resized. This seems limiting, but don't worry; Go provides a convenient way of working with arrays.

## Slices

An array has a fixed size. A slice, on the other hand, is a dynamically-sized, flexible view into the elements of an array. In practice, slices are much more common than arrays.
```The type []T is a slice with elements of type T.```
A slice is formed by specifying two indices, a low and high bound, separated by a colon:

```golang
a[low : high]
```

This selects a half-open range which includes the first element, but excludes the last one.

The following expression creates a slice which includes elements 1 through 3 of a:

a[1:4]