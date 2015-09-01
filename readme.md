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

�����沨��ʽʵ����һ�������������������Խ��Զ����json�ļ������java bean���롣

���õ���json�ļ���ʽ�����ϸ�ģ����Ǹ����Լ��������Ƶġ�json��ʽ�Ķ����`definition/json-format.json`, `example/AdResponse.json`
������һ��ʵ��ʹ�õ�json���ӣ�`example/AdResponse.java`�����Ӧ�Ĵ������������԰�����ļ��ŵ����ʵ�package�С�

##ʹ��
###1 ����`JsonToJava.java`
###2 ����`JsonToJava jsonFileFullPathName`