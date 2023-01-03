## Лабораторная работа №5
Вариант 1.
## Грамматика для описания модели БД:
```
[DB] -> [Tables]
[Tables] -> [Table] | [Table][TableDelimeter][Tables]
[Table] -> [Name][LeftParenthesis][TableContents][RightParenthesis]
[TableContents] -> [ID][Attributes][Connections]
[ID] -> [IDDenomination][LeftParenthesis][Attributes][RightParenthesis]
[Attributes] -> [Attribute] | [Attribute][AttributeDelimeter][Attributes]
[Attribute] -> [Name][AttributeTypeDelimeter][AttributeType]
[Connections] -> [Connection] | [Connection][ConnectionDelimeter][Connections]
[Connection] -> [ConnectionDenomination][Name][ConnectionType]
[ConnectionType] -> [0.1Denomination] | [0.NDenomination] | [1.1Denomination] | [1.NDenomination]
[LeftParenthesis] -> '{'
[RightParenthesis] -> '}'
[IDDenomination] -> 'ID'
[AttributeDelimeter] -> ','
[ConnectionDelimeter] -> ','
[Name] -> [A-Za-z][NameRight]
[NameRight] -> [A-Za-z][NameRight] | [Empty]
[AttributeType] -> 'int' | 'float' | 'money' | 'datetime' | 'char' | 'string'
[Empty] = ''

ПАРАМЕТРИЗУЮТСЯ:
[TableDelimeter] DEFAULT ','
[AttributeTypeDelimeter] DEFAULT ':'
[ConnectionDenomination] DEFAULT 'CONNECTION'
[0.1Denomination] DEFAULT '0.1'
[0.NDenomination] DEFAULT '0.N'
[1.1Denomination] DEFAULT '1.1'
[1.NDenomination] DEFAULT '1.N'
```

## Компиляция программы:
Внутри директории src ```javac Main.java``` <br>
## Запуск программы:
```java Main.java path``` либо  ```java Main.java path/testname.txt path/syntaxname.txt```  <br>
где **path** - путь к папке, в которой лежат файлы **test.txt** и **syntax.txt** (**syntax.txt** может быть пустым или не существовать),
**testname.txt** и **syntaxname.txt** - имена файлов, в которых находится описание модели и пользовательского синтаксиса соответственно <br>