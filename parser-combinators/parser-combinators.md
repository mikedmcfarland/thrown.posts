---
title: parser combinators
---

Used them for content creation tools, like level descriptions.

## What are they
Monads which consume a stream of characters, and aquire the relevant parts.

##Targets

Used for describing levels, the parameters that made them up, including
molecules, the state of the molecules, the ratios, spawn rate.

Used for describing different molecules themselves. There were already existing
formats but none that actually worked in c#/unity without a substantial fee.

##Starting with the use cases

Have some examples of the format itself, and what the results of parsing it
will be.

## Boil it down into grammer

* Break 


## Test the individual components

After creating the grammer you are left with a bunch of indvidual, statelss,
parseters. This makes testing significantly easier, since exposing the grammer
is rather safe, and you can then write unit tests indvidually for each portion
of the grammer.


themselves, parsers. You can feed them bitsized parts of the file, and test
that they gather the appropriate parameters.

* They are stateless which makes
