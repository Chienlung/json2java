/**
 * 
 */
package json2java;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Stack;

/**
 * @author Chienlung
 *
 */
public class JsonToJava {
  private static String preProcess(String path) throws NotHappyException, IOException {
    String content = null;
    FileInputStream fis = new FileInputStream(path);
    byte[] buffer = new byte[4096];
    int bytesReadNum = 0;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    while ((bytesReadNum = fis.read(buffer)) != -1) {
      baos.write(buffer, 0, bytesReadNum);
    }
    content = baos.toString();

    content = content.replaceAll("[\\s\"]", ""); // 去掉空白字符与引号
    if (content.length() < 2) {
      throw new NotHappyException("Unexpected json content.");
    }
    content = content.substring(1, content.length()); // remove the first {
    content = content.substring(0, content.length() - 1); // remove the last }

    if (content.matches("[\\w\\.,\\:\\{\\}\\[\\]]+")) {
      return content;
    } else {
      throw new NotHappyException("Unexpected letters in the json string");
    }
  }

  private static LinkedList<String> shuntingYard(String content) throws NotHappyException {
    if (content == null || content.length() == 0) {
      throw new NotHappyException("parameter content is blank.");
    }
    LinkedList<String> result = new LinkedList<String>();
    // 运算符包括：冒号，左花括号，逗号，右花括号，左方括号，右方括号
    Stack<Character> stack = new Stack<Character>();
    StringBuilder tokenBuilder = new StringBuilder();
    for (int i = 0; i < content.length(); i++) {
      char item = content.charAt(i);
      switch (item) {
        case '{':
          stack.push(item);
          break;
        case '}':
          if (tokenBuilder.length() != 0) {
            result.add(tokenBuilder.toString()); // tokenBuilder遇到非operator就表明形成一个新的token，并加入输出list
            tokenBuilder.setLength(0);
          }
          while (!stack.isEmpty() && !stack.peek().equals('{')) {
            result.add(String.valueOf(stack.pop()));
          }
          if (stack.isEmpty() || !stack.peek().equals('{')) {
            throw new NotHappyException("The json string is invalid");
          }
          result.add(String.valueOf(stack.pop()));
          break;
        case '[':
          stack.push(item);
          break;
        case ']':
          if (tokenBuilder.length() != 0) {
            result.add(tokenBuilder.toString()); // tokenBuilder遇到非operator就表明形成一个新的token，并加入输出list
            tokenBuilder.setLength(0);
          }
          while (!stack.isEmpty() && !stack.peek().equals('[')) { // pop栈直到遇到一个'['
            result.add(String.valueOf(stack.pop()));
          }
          if (stack.isEmpty() || !stack.peek().equals('[')) {
            throw new NotHappyException("The json string is invalid");
          }
          result.add(String.valueOf(stack.pop())); // 将'['也加入输出队列
          break;
        case ':':
          if (tokenBuilder.length() != 0) {
            result.add(tokenBuilder.toString()); // tokenBuilder遇到非operator就表明形成一个新的token，并加入输出list
            tokenBuilder.setLength(0);
          }
          stack.push(item);
          break;
        case ',':
          if (tokenBuilder.length() != 0) {
            result.add(tokenBuilder.toString()); // tokenBuilder遇到非operator就表明形成一个新的token，并加入输出list
            tokenBuilder.setLength(0);
          }
          while (!stack.isEmpty() && (!stack.peek().equals('{') || stack.peek().equals('['))) { // pop栈直到遇到一个'{'或'['
            result.add(String.valueOf(stack.pop()));
          }
          if (stack.isEmpty() || (!stack.peek().equals('{') && stack.peek().equals('['))) {
            throw new NotHappyException("This json string is invalid.");
          }
          break;
        default:
          tokenBuilder.append(item);
      }
    }
    return result;
  }

  private static String reversePolishNotationParser(LinkedList<String> list)
      throws NotHappyException {
    if (list == null || list.isEmpty()) {
      throw new NotHappyException("Parameter list is null or empty");
    }

    Stack<String> stack = new Stack<String>();
    StringBuilder statementBuilder = new StringBuilder();
    StringBuilder result = new StringBuilder();
    StringBuilder tmp = new StringBuilder();
    while (!list.isEmpty()) {
      String item = list.poll();
      switch (item) {
        case ":": {// 栈中弹出两个operand
          String operand = stack.pop(); // int
          String operand2 = stack.pop(); // i
          statementBuilder.append("private ").append(operand).append(" ").append(operand2)
              .append(";\n"); // int i;
          statementBuilder.append("public ").append(operand).append(" get").append(operand2.substring(0, 1).toUpperCase()).append(operand2.substring(1)).append("() {\n return ").append(operand2).append(";\n}\n");
          statementBuilder.append("public void ").append("set").append(operand2.substring(0, 1).toUpperCase()).append(operand2.substring(1)).append("(").append(operand).append(" ").append(operand2).append(") {\n this.").append(operand2).append(" = ").append(operand2).append(";\n}\n");
          break;
        }
        case "{": {
          String next = list.poll(); // {后面必为:
          assert (next.equals(":"));
          if (list.isEmpty()) { // 若此时list为空，则栈顶就是一个package的名字
            result.append("package ").append(stack.pop()).append(";\n").append(tmp);
            return result.toString();
          } else { // 否则栈顶就是一个class的名字
            tmp.append("class ").append(stack.pop()).append(" {\n").append(statementBuilder)
                .append("}\n");
            statementBuilder.setLength(0);;
          }
          break;
        }
        case "[": {
          String next = list.poll();// [后面必为:
          assert (next.equals(":"));
          String operand = stack.pop(); // App
          String operand2 = stack.pop(); // app
          switch (operand) {
            case "int":
              operand = "Integer";
              break;
            case "long":
              operand = "Long";
              break;
            case "float":
              operand = "Float";
              break;
            case "double":
              operand = "Double";
              break;
            case "char":
              operand = "Character";
              break;
            case "boolean":
              operand = "Boolean";
              break;
            default:
              break;
          }
          statementBuilder.append("private List<").append(operand).append("> ").append(operand2)
              .append(";\n"); // List<Integer> i;
          statementBuilder.append("public ").append("List<").append(operand).append("> ").append(" get").append(operand2.substring(0, 1).toUpperCase()).append(operand2.substring(1)).append("() {\n return ").append(operand2).append(";\n}\n");
          statementBuilder.append("public void ").append("set").append(operand2.substring(0, 1).toUpperCase()).append(operand2.substring(1)).append("(").append("List<").append(operand).append("> ").append(" ").append(operand2).append(") {\n this.").append(operand2).append(" = ").append(operand2).append(";\n}\n");
          
          break;
        }
        default: // operand直接入栈
          stack.push(item);
      }
    }
    return null;
  }
  // 把出第一个class之外的class都移到第一个class内部并且作为public static
  private static String postProcess(String javaCode) {
    StringBuilder finalCodeBuilder = new StringBuilder();

    int firstClassPos = javaCode.indexOf("class");
    if (firstClassPos == -1) {
      return javaCode;
    }
    finalCodeBuilder.append(javaCode.substring(0, firstClassPos)).append("public ");

    int secondClassPos = javaCode.indexOf("class", firstClassPos + 1);
    if (secondClassPos == -1) {
      finalCodeBuilder.append(javaCode.substring(firstClassPos));
      return finalCodeBuilder.toString();
    }

    int cpOfFirstClass = secondClassPos - 2;// }\nclass


    finalCodeBuilder.append(javaCode.substring(firstClassPos, cpOfFirstClass));
    int begin = cpOfFirstClass;
    while (true) {
      int classPos = javaCode.indexOf("class", begin);
      if (classPos == -1) {
        break;
      }
      int nextClassPos = javaCode.indexOf("class", classPos + 1);
      if (nextClassPos == -1) {
        finalCodeBuilder.append("public static ").append(javaCode.substring(classPos, javaCode.length()));
        break;
      }
      finalCodeBuilder.append("public static ").append(javaCode.substring(classPos, nextClassPos));

      begin = nextClassPos;
    }
    finalCodeBuilder.append("}");
    return finalCodeBuilder.toString();
  }
  public static void main(String[] args) throws NotHappyException, IOException {
    if (args.length != 1) {
      System.err.println("Usage: JsonToJava jsonFile");
      return;
    }
    String javaCode = postProcess(reversePolishNotationParser(shuntingYard(preProcess(args[0]))));
    if (javaCode == null) {
      throw new NotHappyException("Wrong result java code");
    }
    System.out.println(javaCode);
  }
}


