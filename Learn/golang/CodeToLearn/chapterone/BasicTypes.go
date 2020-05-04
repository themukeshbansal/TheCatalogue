package chapterone

import (
	"fmt"
)

// IntegerType : Explains the use and different types of int
func IntegerType() {
	fmt.Println()
	var signedIntWith8Bytes int8
	for index := 0; index < 129; index++ {
		signedIntWith8Bytes++
	}
	fmt.Println(signedIntWith8Bytes)
}
