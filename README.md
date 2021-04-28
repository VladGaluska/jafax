# Java Fact eXtractor
Java Fact Extractor (***JaFaX***) is a code analysis tool used to find irregularities with Java based software
projects.

## Start Up

- Import project with Maven
- Run `mvn clean install`
- Run with the path you want to analyse as an argument or run inside the project you wish to analyse
- If a JSON file is found inside the directory, it will be used instead of parsing, otherwise it will create one
- It will then continue to parse the JSON and calculate the relations and metrics. The JSON creation is done 
  in order to allow the user to modify the JSON in case there are multiple projects with classes with the same signature.
  
# Results

## JSON structure
### Class
- id: Represents the id of the class

### Method
- id: Represents the id of the method

### Field
- id: Represents the id of the field

### Parameter
- id: Represents the id of the parameter

### Local variable
- id: Represents the id of the local variable

### Annotation
- id: Represents the id of the annotation

### Method call
- id: Represents the id of the method call

### Class Accesses
- id: Represents the id of the class access

### Annotation set
- id: Represents the id of the annotation setting

## Relations

|name|calculation level|calculation formula| 
|----|-----------------|-------------------|

## Metrics
|name|calculation level|calculation formula|
|----|-----------------|-------------------|

## Anti-Patterns
|name|calculation level|calculation formula|
|----|-----------------|-------------------|