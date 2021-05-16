# Java Fact eXtractor
Java Fact Extractor (***JaFaX***) is a code analysis tool used to find irregularities with Java based software
projects.

## Start Up

- Import project with Maven
- Run `mvn clean install`
- Run with the path you want to analyze as an argument or run inside the project you wish to analyze
- If a JSON file is found inside the directory, it will be used instead of creating an AST tree, otherwise it will create one
- It will then continue to parse the JSON and calculate the relations and metrics. The JSON creation is done 
  in order to allow the user to modify the JSON in case there are multiple projects with classes with the same signature.
  
# Results

## Layout
### Class
|**name**|**type**|**description**| 
|----|----|-----------|
|**type**|`String`|Property needed by the kotlinx serializer, always `Class`
|**id**|`Long`|The unique id of the object (shared among all types)|
|**name**|`String`|The name of the class|
|**fileName**|`String`|The name of the containing file|
|**isInterface**|`Boolean`|A value representing if the class is an interface or not, only appears if its value is true|
|**modifiers**|`List<String>`|The modifiers of the class|
|**container**|`Long`|The id of its container (class or method)|
|**superClass**|`Long`|The id of its superClass|
|**interfaces**|`List<Long>`|The list of ids of its interfaces|
|**isExternal**|`Boolean`|A value representing if the class is from the source project or not, only appears if its value is true|
|**isTypeParameter**|`Boolean`|A value representing if the class ia type parameter or not, only appears if its value is true|
|**instances**|`List<Long>`|The list of ids of its parameter instances. Meaning that when you have `A<T>` and have an instance somewhere declared of `A<String>`, `T` will have `String` in its `parameterInstances`|
|**typeParameters**|`List<Long>`|The list of ids of its type parameters|
|**containedFields**|`List<Long>`|The list of ids of its directly contained fields|
|**containedClasses**|`List<Long>`|The list of ids of its directly contained classes|
|**containedMethods**|`List<Long>`|The list of ids of its directly contained methods|
|**accessedFields**|`List<Long>`|The list of ids of its accessed fields inside the initializer of the class|
|**calledMethods**|`List<Long>`|The list of ids of its called methods inside the initializer of the class|

### Method
|name|type|description| 
|----|----|-----------|
|**type**|`String`|Property needed by the kotlinx serializer, always `Method`
|**id**|`Long`|The unique id of the object (shared among all types)|
|**name**|`String`|The name of the method|
|**signature**|`String`|The signature of the method|
|**isConstructor**|`Boolean`|A value representing if the method is a constructor or not, only appears if its value is true|
|**isDefaultConstructor**|`Boolean`|A value representing if the method is a default constructor or not, only appears if its value is true|
|**returnType**|`Long`|The return type of the method|
|**modifiers**|`List<String>`|The modifiers of the method|
|**container**|`Long`|The id of its container (class or method)|
|**parameters**|`List<Long>`|The list of ids of its parameters|
|**localVariables**|`List<Long>`|The list of ids of its directly contained local variables|
|**typeParameters**|`List<Long>`|The list of ids of its type parameters|
|**cyclomaticComplexity**|`Long`|A value representing the method's cyclomatic complexity|
|**containedClasses**|`List<Long>`|The list of ids of its directly contained classes|
|**containedMethods**|`List<Long>`|The list of ids of its directly contained methods|
|**accessedFields**|`List<Long>`|The list of ids of its directly accessed fields|
|**calledMethods**|`List<Long>`|The list of ids of its directly called methods|

### Attribute
|name|type|description| 
|----|----|-----------|
|**type**|`String`|Property needed by the kotlinx serializer, always `Attribute`
|**id**|`Long`|The unique id of the object (shared among all types)|
|**name**|`String`|The name of the attribute|
|**returnType**|`Long`|The type of the attribute|
|**modifiers**|`List<String>`|The modifiers of the attribute|
|**kind**|`String`|The attribute kind, can be either `Parameter`, `Field` or `LocalVariable`|

## Relations

|name|calculation level|calculation formula| 
|----|-----------------|-------------------|

## Metrics
|name|calculation level|calculation formula|
|----|-----------------|-------------------|

## Anti-Patterns
|name|calculation level|calculation formula|
|----|-----------------|-------------------|