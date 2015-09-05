# json2java
A code generator based on reverse-polish-notation which can translate a self-defined JSON file into JAVA bean code.

The format of json file is not strict but customized. Please check the `definition/json-format.json`, you will 
see the definition.

The file `example/AdResponse.json` displays an example json content, and the `example/AdResponse.java` corresponds 
the output of the former json file. You can put output file in the appropriate package.

##Usage
###1 Compile `JsonToJava.java`
###2 run `JsonToJava jsonFileFullPathName`

---

根据逆波兰式实现了一个代码生成器，它可以将自定义的json文件翻译成java bean代码。

所用到的json文件格式不是严格的，而是根据自己的需求定制的。json格式的定义见`definition/json-format.json`, `example/AdResponse.json`
定义了一个实际使用的json例子，`example/AdResponse.java`是其对应的代码输出。你可以把输出文件放到合适的package中。

##使用
###1 编译`JsonToJava.java`
###2 运行`JsonToJava jsonFileFullPathName`