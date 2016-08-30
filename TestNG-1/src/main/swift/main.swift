import Foundation


var result = ""
for (var i=1; i<Process.arguments.count; i++) {
	if (i>1) {
		result = result + " "
	}
	result = result + Process.arguments[i]
}

print(result)