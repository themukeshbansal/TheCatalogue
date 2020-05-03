package chapterone

import (
	"fmt"
)

func main() {
	fmt.Println()
	var signedIntWith8Bytes int8
	for index := 0; index < 129; index++ {
		signedIntWith8Bytes++
	}
	fmt.Println(signedIntWith8Bytes)
}
