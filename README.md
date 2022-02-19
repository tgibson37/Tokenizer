Tokenizer separates one large String into a List of tokens. My thought is to
tokenize C, tinyC, Java and maybe others. Inner classes for each token type 
define two functions. So far just two are defined, Symbol and Number.
Tom Gibson, 12 Feb 2022.
---
19 Feb, 22
Now have these 6 token types:
		types.add(new Number());
		types.add(new Decl());
		types.add(new Symbol());
		types.add(new Comment1());
		types.add(new Relation());
		types.add(new Block());
		types.add(new Unknown());
Testing: reads testfile instead of String constants. 
Several new parse tools.
---
